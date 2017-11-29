package com.test.navigationdrawer1;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.navigationdrawer1.Adapters.ServiceListViewAdapter;
import com.test.navigationdrawer1.Network.DeviceType;
import com.test.navigationdrawer1.Network.Message;
import com.test.navigationdrawer1.Network.MessageContent;
import com.test.navigationdrawer1.Network.ObjectType;
import com.test.navigationdrawer1.REST.IHttpResponseMethods;
import com.test.navigationdrawer1.REST.Models.HistorialEstadoUsuario;
import com.test.navigationdrawer1.REST.Models.Usuario;
import com.test.navigationdrawer1.REST.WebApi;
import com.test.navigationdrawer1.Tasks.SendMessageTask;
import com.test.navigationdrawer1.Utils.NetworkUtil;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import org.apache.commons.lang3.SerializationUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import edu.rit.se.wifibuddy.DnsSdService;
import edu.rit.se.wifibuddy.WifiDirectHandler;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public WebApi api;
    public Location location;
    WifiDirectHandler wifiDirectHandler;
    String TAG = "Main";
    Context context;
    NetworkUtil networkUtil;
    SharedPreferences pref;
    ListView deviceList;
    ServiceListViewAdapter servicesListAdapter;
    List<DnsSdService> services = new ArrayList<>();
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        api = new WebApi(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectectedScreen(R.id.nav_search);
        networkUtil = NetworkUtil.getInstance(DeviceType.EMITTER);

        setupLocationProvider();
        checkForUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();

        setServiceList();
        //initEnableExternalStorage();
        registerCommunicationReceiver();
        addWifiService();

        //showDeviceInformation();

        //prepareResetButton();
        checkForCrashes();
    }

    @Override
    public void onPause() {
        super.onPause();

        removeWifiP2pService();
        removeWifiService();

        unregisterManagers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

    private void checkForCrashes() {
        //CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        //UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }


    private void removeWifiService() {
        if (wifiDirectHandler != null) {
            Log.w(TAG, "Service Wifi removed");
            wifiDirectHandler.removeGroup();
            context.stopService(new Intent(context, ServiceConnection.class));
            context.unbindService(wifiServiceConnection);
        }
    }

    private void removeWifiP2pService() {
        if (wifiDirectHandler != null) {
            Log.w(TAG, "Service P2p removed");
            wifiDirectHandler.removeService();
        }
    }

    public void setupLocationProvider() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new AlertDialog.Builder(this)
                .setTitle(getString(R.string.location_activate_title))
                .setMessage(getString(R.string.location_activate_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent= new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
        }

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                MainActivity.this.location = location;
                Log.e("LOCATION", "" + location.getLatitude() + "/" + location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("GPS", "SÍ SE REQUIRIERON PERMISOS");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5*1000, 0, locationListener);
        Log.e("GPS", "NO SE REQUIRIERON PERMISOS");

        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null) {
            Log.e("LOCATION", "" + location.getLatitude() + "/" + location.getLongitude());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            displaySelectectedScreen(id);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectectedScreen(id);
        return true;
    }

    public void displaySelectectedScreen(int itemId) {
        Fragment fragment = null;
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (itemId == R.id.nav_search) {
            fragment = new SearchFragment();
            getSupportActionBar().setTitle(getString(R.string.search_fragment_title));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetServiceDiscovery();
                    Toast.makeText(getApplicationContext(), "Reset", Toast.LENGTH_LONG).show();
                }
            });

            fab.setImageResource(R.drawable.ic_search);
        } else if (itemId == R.id.nav_map) {
            fragment = new MapFragment();
            getSupportActionBar().setTitle(getString(R.string.map_fragment_title));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "New fragment", Toast.LENGTH_LONG).show();
                    Fragment fragment1 = new FormReportFragment();

                    if (fragment1 != null) {

                        fab.setImageResource(R.drawable.ic_pin_drop);

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment1);
                        ft.commit();
                    }
                }
            });

            fab.setImageResource(R.drawable.ic_my_location);
        } else if (itemId == R.id.nav_status) {
            fragment = new StatusFragment();
            getSupportActionBar().setTitle(getString(R.string.status_fragment_title));

            fab.setImageResource(R.drawable.ic_favorite);
        } /*else if (itemId == R.id.nav_add_report) {
            fragment = new FormReportFragment();
            getSupportActionBar().setTitle(getString(R.string.report_fragment_title));

            fab.setImageResource(R.drawable.ic_pin_drop);
        }*/ else if (itemId == R.id.action_settings) {
            fragment = new RoleFragment();
            getSupportActionBar().setTitle(getString(R.string.role_fragment_title));

            fab.setImageResource(R.drawable.ic_account_circle);
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void registerCommunicationReceiver() {
        CommunicationReceiver communicationReceiver = new CommunicationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiDirectHandler.Action.DNS_SD_SERVICE_AVAILABLE);
        filter.addAction(WifiDirectHandler.Action.WIFI_STATE_CHANGED);
        filter.addAction(WifiDirectHandler.Action.SERVICE_CONNECTED);
        filter.addAction(WifiDirectHandler.Action.MESSAGE_RECEIVED);
        filter.addAction(WifiDirectHandler.Action.DEVICE_CHANGED);
        LocalBroadcastManager.getInstance(this).registerReceiver(communicationReceiver, filter);
        Log.i(TAG, "Communication Receiver registered");
    }

    private void addWifiService() {
        Log.w(TAG, "Service Wifi added");
        Intent intent = new Intent(context, WifiDirectHandler.class);
        bindService(intent, wifiServiceConnection, BIND_AUTO_CREATE);
        startService(intent);
    }

    private void addWifiP2pService() {
        pref = this.getSharedPreferences(getString(R.string.preference_device_key), MODE_PRIVATE);
        int deviceTypePref = pref.getInt(getString(R.string.preference_device_type), DeviceType.EMITTER.getCode());
        DeviceType deviceType = DeviceType.get(deviceTypePref);
        Log.e("addWifiP2pService", deviceType.toString());

        if (wifiDirectHandler.getWifiP2pServiceInfo() == null) {
            HashMap<String, String> record = new HashMap<>();
            record.put("Name", wifiDirectHandler.getThisDevice().deviceName);
            record.put("Address", wifiDirectHandler.getThisDevice().deviceAddress);
            record.put("DeviceType", deviceType.toString());
            //record.put("DeviceType", "TEST");
            wifiDirectHandler.addLocalService("Wi-Fi Buddy", record);
            Log.d(TAG, "Service P2p added");
        } else {
            Log.w(TAG, "Service P2p already added");
        }
    }

    private void setServiceList() {
        deviceList = (ListView)findViewById(R.id.device_list);
        servicesListAdapter = new ServiceListViewAdapter(this, services);
        deviceList.setAdapter(servicesListAdapter);
        services.clear();
        servicesListAdapter.notifyDataSetChanged();
    }

    public class CommunicationReceiver extends BroadcastReceiver {

        private static final String TAG = WifiDirectHandler.TAG + "CSearchReceiver";
        //private MainActivity activity;

        @Override
        public void onReceive(Context context, Intent intent) {
            //activity = (MainActivity) context;
            // Get the intent sent by WifiDirectHandler when a service is found
            if (intent.getAction().equals(WifiDirectHandler.Action.DEVICE_CHANGED)) {
                // This device's information has changed
                Log.i(TAG, "This device changed");
                Log.d(TAG, wifiDirectHandler.getThisDeviceAddress());
                if (wifiDirectHandler.getThisDevice() != null) {
                    addWifiP2pService();
                    //prepareResetButton();
                    //showDeviceInformation();
                    wifiDirectHandler.continuouslyDiscoverServices();
                }

            } else if (intent.getAction().equals(WifiDirectHandler.Action.WIFI_STATE_CHANGED)) {
                // Wi-Fi has been enabled or disabled
                Log.i(TAG, "Wi-Fi state changed");
            } else if (intent.getAction().equals(WifiDirectHandler.Action.SERVICE_CONNECTED)) {
                // This device has connected to another device broadcasting the same service
                Log.i(TAG, "Service connected onReceive()");
                pref = context.getSharedPreferences(getString(R.string.preference_device_key), MODE_PRIVATE);
                int deviceTypePref = pref.getInt(getString(R.string.preference_device_type),999);
                DeviceType deviceType = DeviceType.get(deviceTypePref);
                processConnectionIn(deviceType);
                //TODO: change deviceType broadcasted in service or halt broadcast or handle exception into slave

            } else if (intent.getAction().equals(WifiDirectHandler.Action.MESSAGE_RECEIVED)) {
                // A message from the Communication Manager has been received
                Log.i(TAG, "Message received");
                new ReceiveMessageTask().execute(intent.getByteArrayExtra(WifiDirectHandler.MESSAGE_KEY));

            } else if (intent.getAction().equals(WifiDirectHandler.Action.DNS_SD_SERVICE_AVAILABLE)) {
                Log.d(TAG, "Service Discovered and Accessed " + (new Date()).getTime());
                String serviceKey = intent.getStringExtra(WifiDirectHandler.SERVICE_MAP_KEY);
                DnsSdService service = wifiDirectHandler.getDnsSdServiceMap().get(serviceKey);

                if (service.getSrcDevice() == null) {
                    Log.w(TAG, "Unaccesible source device.");
                    return;
                }

                if (service.getSrcDevice().deviceName.equals("")) {
                    Log.w(TAG, "Unaccesible source device name.");
                    return;
                }

                if (servicesListAdapter.addUnique(service)) {
                    if (networkUtil.canConnectTo(service)) {
                        onServiceClick(service);
                    }
                }

                Log.d(TAG + "TEST", "ServicesList count: " + servicesListAdapter.getCount());
            }
        }
    }

    private ServiceConnection wifiServiceConnection = new ServiceConnection() {

        /**
         * Called when a connection to the Service has been established, with the IBinder of the
         * communication channel to the Service.
         * @param name The component name of the service that has been connected
         * @param service The IBinder of the Service's communication channel
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "Binding WifiDirectHandler service");
            Log.i(TAG, "ComponentName: " + name);
            Log.i(TAG, "Service: " + service);
            WifiDirectHandler.WifiTesterBinder binder = (WifiDirectHandler.WifiTesterBinder) service;

            wifiDirectHandler = binder.getService();
            Log.i(TAG, "WifiDirectHandler service bound");

            //showDeviceInformation();
        }

        /**
         * Called when a connection to the Service has been lost.  This typically
         * happens when the process hosting the service has crashed or been killed.
         * This does not remove the ServiceConnection itself -- this
         * binding to the service will remain active, and you will receive a call
         * to onServiceConnected when the Service is next running.
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "WifiDirectHandler service unbound");
        }
    };

    public WifiDirectHandler getWifiHandler() {
        return wifiDirectHandler;
    }

    public void onServiceClick(DnsSdService service) {
        Log.i(TAG, "\nService List item tapped");

        if (service.getSrcDevice().status == WifiP2pDevice.CONNECTED) {
            /*if (chatFragment == null) {
                chatFragment = new ChatFragment();
            }
            replaceFragment(chatFragment);*/
            Log.i(TAG, "Switching to Chat fragment");
        } else if (service.getSrcDevice().status == WifiP2pDevice.AVAILABLE) {
            String sourceDeviceName = service.getSrcDevice().deviceName;
            if (sourceDeviceName.equals("")) {
                sourceDeviceName = "other device";
            }
            Toast.makeText(this, "Inviting " + sourceDeviceName + " to connect", Toast.LENGTH_LONG).show();
            wifiDirectHandler.initiateConnectToService(service);
        } else {
            Log.e(TAG, "Service not available");
            Toast.makeText(this, "Service not available", Toast.LENGTH_LONG).show();
        }
    }

    public void resetServiceDiscovery(){

        removeWifiP2pService();
        removeWifiService();

        setServiceList();
        //initEnableExternalStorage();
        ////registerCommunicationReceiver();
        addWifiService();


        // Clear the list, notify the list adapter, and start discovering services again
        Log.i(TAG, "Resetting Service discovery");
        services.clear();
        servicesListAdapter.notifyDataSetChanged();
        wifiDirectHandler.resetServiceDiscovery();
    }

    private void processConnectionIn(DeviceType myType) {
        Log.d(TAG, "processConnectionIn(); " + myType.toString());
        String msg;
        switch (myType) {
            case ACCESS_POINT:
                Log.i(TAG, "ACCESS_POINT");
                Log.d(TAG, "Waiting for incoming messages.");
                break;
            case ACCESS_POINT_WREQ:
                Log.i(TAG, "ACCESS_POINT_WREQ");
                Log.d(TAG, "Waiting for incoming messages.");
                break;
            case ACCESS_POINT_WRES:
                Log.i(TAG, "ACCESS_POINT_WRES");
                Log.d(TAG, "Waiting for incoming messages.");
                break;
            case EMITTER:
                Log.i(TAG, "EMITTER");

                pref = this.getSharedPreferences(getString(R.string.preference_user_key), MODE_PRIVATE);
                String userId = pref.getString(getString(R.string.preference_user_id), "0");

                Usuario usr = new Usuario();
                usr.id = userId;
                msg = gson.toJson(usr);

                new SendMessageTask().execute(getWifiHandler(), msg, ObjectType.HELLO);
                break;
            default:
                //new SendMessageTask().execute(activity, json.toJson(activity.curDevice), ObjectType.HELLO);
                break;
        }
    }

    private class ReceiveMessageTask extends AsyncTask<byte[], Void, Message> {
        protected Message doInBackground(byte[]... objects) {
            byte[] readMessage = objects[0];
            Message message = SerializationUtils.deserialize(readMessage);
            switch(message.messageType) {
                case TEXT:
                    Log.i(TAG, "Text message received");
                    //return new String(message.message);
                    return message;
            }
            Log.d(TAG,"ReceiveMessageTask");
            return null;
        }

        protected void onProgressUpdate(Void... progress) {
        }

        protected void onPostExecute(Message result) {
            processReceivedMessage(result);
        }
    }

    private void processReceivedMessage(Message msg) {
        String s = new String(msg.message);

        if (s.equals("")) return;
        Log.d(TAG, "Received: " + s);

        Toast.makeText(getApplicationContext(), "Recibido: " + s, Toast.LENGTH_LONG).show();

        switch (msg.objectType) {
            case HELLO:
                pref = this.getSharedPreferences(getString(R.string.preference_user_key), MODE_PRIVATE);
                final String myUserId = pref.getString(getString(R.string.preference_user_id), "0");

                Gson gson = new Gson();
                Type listType = new TypeToken<Usuario>(){}.getType();
                final Usuario usr = gson.fromJson(s, listType);

                api.responseMethods = historialEstadoUsuario;
                api.CreateHistorialEstadoUsuarios(new HistorialEstadoUsuario(){{
                    this.id = Integer.parseInt(usr.id); //this is from the found user
                    this.idEventoHue = 1; //Constant //TODO: write enum
                    this.idEdoUsrHue = 1; //Constant //TODO: write enum
                    this.idUsrRegistroHue = Integer.parseInt(myUserId); //my user id
                }});
                break;
            case ACK:
                break;
            default:
        }
    }

    IHttpResponseMethods historialEstadoUsuario = new IHttpResponseMethods() {
        @Override
        public void response(String jsonResponse) {
            Toast.makeText(getApplicationContext(), jsonResponse,
                    Toast.LENGTH_LONG).show();
            //TODO: send ACK message; then wait few seconds and close connection
            removeWifiP2pService();
            removeWifiService();

            unregisterManagers();

            registerCommunicationReceiver();
            addWifiService();
        }

        @Override
        public void error(String error) {
            Toast.makeText(getApplicationContext(), error,
                    Toast.LENGTH_LONG).show();
            //TODO: send ACK message; then wait few seconds and close connection
            removeWifiP2pService();
            removeWifiService();

            unregisterManagers();

            registerCommunicationReceiver();
            addWifiService();
        }
    };
}

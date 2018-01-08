package com.test.navigationdrawer1.MapTabs;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.navigationdrawer1.Details.PersonDetailFragment;
import com.test.navigationdrawer1.R;
import com.test.navigationdrawer1.REST.IHttpResponseMethods;
import com.test.navigationdrawer1.REST.Models.HistorialEstadoUsuario;
import com.test.navigationdrawer1.REST.Models.HueUserData;
import com.test.navigationdrawer1.REST.WebApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleMapFragment extends Fragment {
    WebApi api;
    MapView mMapView;
    private GoogleMap googleMap;
    SharedPreferences pref;
    List<HueUserData> historial_edo_usuarios;

    public PeopleMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_people_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();
        pref = getActivity().getSharedPreferences(getString(R.string.preference_device_key), MODE_PRIVATE);
        String lat = pref.getString(getString(R.string.location_lat), "");
        String lng = pref.getString(getString(R.string.location_lng), "");

        api = new WebApi(getActivity());
        api.responseMethods = queryHistorialEstadoUsuarios;
        api.QueryHistorialEstadoUsuariosByLocation(lat, lng);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        return false;
                    }
                });
            }
        });

        return rootView;
    }

    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Toast.makeText(getActivity(), marker.getPosition().latitude + "/" + marker.getPosition().longitude, Toast.LENGTH_LONG).show();

            String lat = String.valueOf(marker.getPosition().latitude);
            String lon = String.valueOf(marker.getPosition().longitude);

            /*if (marker.isInfoWindowShown()) {
                PersonDetailFragment fragment = new PersonDetailFragment();

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }*/

            // true if the listener has consumed the event (i.e., the default behavior should
            // not occur); false otherwise (i.e., the default behavior should occur). The default
            // behavior is for the camera to move to the marker and an info window to appear.
            return false;
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    IHttpResponseMethods queryHistorialEstadoUsuarios = new IHttpResponseMethods() {
        @Override
        public void response(String jsonResponse) {
            //Toast.makeText(getContext(), jsonResponse, Toast.LENGTH_LONG).show();
            Log.w("TEST", jsonResponse);
            String jsonArray = "";

            try {
                JSONObject object = new JSONObject(jsonResponse);
                JSONArray results = object.getJSONArray("result");
                jsonArray = results.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.w("TEST", jsonArray);
            Log.w("TEST", String.valueOf(jsonArray.length()));

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<HueUserData>>(){}.getType();
            historial_edo_usuarios = gson.fromJson(jsonArray, listType);

            for(HueUserData heu : historial_edo_usuarios) {
                Log.e("Test", String.valueOf(heu.id));
                Log.e("Test", String.valueOf(heu.latitud));
                Log.e("Test", String.valueOf(heu.longitud));
                Log.e("Test", String.valueOf(heu.nombre));
                Log.e("Test", String.valueOf(heu.email));

                LatLng markerm = new LatLng(Double.valueOf(heu.latitud), Double.valueOf(heu.longitud));
                googleMap.addMarker(
                        new MarkerOptions()
                                .position(markerm)
                                .title(heu.nombre)
                                .snippet(heu.email)
                );

                CameraPosition cameraPosition = new CameraPosition.Builder().target(markerm).zoom(13).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.setOnMarkerClickListener(markerClickListener);
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        }

        @Override
        public void error(String error) {
            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            Log.w("TEST", error);
        }
    };
}

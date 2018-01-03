package com.test.navigationdrawer1.MapTabs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import com.test.navigationdrawer1.R;
import com.test.navigationdrawer1.REST.IHttpResponseMethods;
import com.test.navigationdrawer1.REST.Models.HistorialEstadoUsuario;
import com.test.navigationdrawer1.REST.WebApi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleMapFragment extends Fragment {
    WebApi api;
    MapView mMapView;
    private GoogleMap googleMap;
    List<HistorialEstadoUsuario> historial_edo_usuarios;

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

        api = new WebApi(getActivity());
        api.responseMethods = queryHistorialEstadoUsuarios;
        api.QueryAllHistorialEstadoUsuarios();

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

            /*LatLng marker = new LatLng(Double.valueOf("19.506426"), Double.valueOf("-99.1253817"));
            googleMap.addMarker(
                    new MarkerOptions()
                            .position(marker)
                            .title("Persona 1")
                            .snippet("Persona 1")
            );
            LatLng marker1 = new LatLng(Double.valueOf("19.5062783"), Double.valueOf("-99.1228712"));
            googleMap.addMarker(
                    new MarkerOptions()
                            .position(marker1)
                            .title("Persona 2")
                            .snippet("Persona 2")
            );
            LatLng marker2 = new LatLng(Double.valueOf("19.5056593"), Double.valueOf("-99.1237612"));
            googleMap.addMarker(
                    new MarkerOptions()
                            .position(marker2)
                            .title("Persona 3")
                            .snippet("Persona 3")
            );*/

            Toast.makeText(getContext(), jsonResponse, Toast.LENGTH_LONG).show();
            Log.w("TEST", jsonResponse);

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<HistorialEstadoUsuario>>(){}.getType();
            historial_edo_usuarios = gson.fromJson(jsonResponse, listType);

            for(HistorialEstadoUsuario heu : historial_edo_usuarios) {
                Log.e("Test", String.valueOf(heu.id));
                Log.e("Test", String.valueOf(heu.idEdoUsrHue));
                Log.e("Test", String.valueOf(heu.idEventoHue));
                Log.e("Test", String.valueOf(heu.idUsrRegistroHue));
                Log.e("Test", String.valueOf(heu.latitud));
                Log.e("Test", String.valueOf(heu.longitud));

                LatLng markerm = new LatLng(Double.valueOf(heu.latitud), Double.valueOf(heu.longitud));
                googleMap.addMarker(
                        new MarkerOptions()
                                .position(markerm)
                                .title("Marker Title")
                                .snippet("Marker Description")
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

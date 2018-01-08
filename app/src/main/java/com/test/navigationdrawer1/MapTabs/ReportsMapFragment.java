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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.navigationdrawer1.R;
import com.test.navigationdrawer1.REST.IHttpResponseMethods;
import com.test.navigationdrawer1.REST.Models.HistorialEstadoUsuario;
import com.test.navigationdrawer1.REST.Models.ReporteIncidente;
import com.test.navigationdrawer1.REST.WebApi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsMapFragment extends Fragment {
    WebApi api;
    MapView mMapView;
    private GoogleMap googleMap;
    List<ReporteIncidente> reportes_incidente;

    public ReportsMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reports_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        api = new WebApi(getActivity());
        api.responseMethods = queryReporteIncidentes;
        api.QueryAllReporteIncidente();

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    IHttpResponseMethods queryReporteIncidentes = new IHttpResponseMethods() {
        @Override
        public void response(String jsonResponse) {
            //Toast.makeText(getContext(), jsonResponse, Toast.LENGTH_LONG).show();
            Log.w("TEST", jsonResponse);

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<ReporteIncidente>>(){}.getType();
            reportes_incidente = gson.fromJson(jsonResponse, listType);

            for(ReporteIncidente ri : reportes_incidente) {
                Log.e("Test", String.valueOf(ri.id));
                Log.e("Test", String.valueOf(ri.latitud));
                Log.e("Test", String.valueOf(ri.longitud));
                Log.e("Test", String.valueOf(ri.fecha));
                Log.e("Test", String.valueOf(ri.hora));
                Log.e("Test", String.valueOf(ri.descripcion));

                String title = ri.fecha + " " + ri.hora;
                String descp = ri.descripcion;

                LatLng markerm = new LatLng(Double.valueOf(ri.latitud), Double.valueOf(ri.longitud));
                googleMap.addMarker(
                        new MarkerOptions()
                                .position(markerm)
                                .title(title)
                                .snippet(descp)
                );

                CameraPosition cameraPosition = new CameraPosition.Builder().target(markerm).zoom(13).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                //googleMap.setOnMarkerClickListener(markerClickListener);
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

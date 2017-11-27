package com.test.navigationdrawer1.MapTabs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import com.test.navigationdrawer1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleMapFragment extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;

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

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(19.4978, -99.1269);
                LatLng sydney1 = new LatLng(19.50, -99.1269);
                LatLng sydney2 = new LatLng(19.48, -99.1269);
                LatLng sydney3 = new LatLng(19.4978, -99.13);
                LatLng sydney4 = new LatLng(19.4978, -99.11);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
                googleMap.addMarker(new MarkerOptions().position(sydney1).title("Marker Title").snippet("Marker Description"));
                googleMap.addMarker(new MarkerOptions().position(sydney2).title("Marker Title").snippet("Marker Description"));
                googleMap.addMarker(new MarkerOptions().position(sydney3).title("Marker Title").snippet("Marker Description"));
                googleMap.addMarker(new MarkerOptions().position(sydney4).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(13).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.setOnMarkerClickListener(markerClickListener);
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

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
}

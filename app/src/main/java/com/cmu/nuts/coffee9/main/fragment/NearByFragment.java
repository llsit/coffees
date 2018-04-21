package com.cmu.nuts.coffee9.main.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesWithFallbackProvider;


public class NearByFragment extends Fragment implements OnLocationUpdatedListener {


    public NearByFragment() {
        // Required empty public constructor
    }

    MapView mMapView;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private Activity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_near_by, container, false);
        activity = getActivity();
        mMapView = view.findViewById(R.id.mapView);
        try {
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume(); // needed to get the map to display immediately
        } catch (Exception e){
            Log.e("Google maps error", "The error is " + e.getMessage());
            e.printStackTrace();
        }

        try {
            MapsInitializer.initialize(activity.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                int REQUEST_CODE_ASK_PERMISSIONS = 123;
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }

                if (ActivityCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }
            }
        });
        return view;
    }

    private void setMaps(final double latitude, final double longitude){
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                int REQUEST_CODE_ASK_PERMISSIONS = 123;
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }

                if (ActivityCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }

                googleMap.setMyLocationEnabled(true);
                // For dropping a marker at a point on the Map
                LatLng cmu = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(cmu).title("Here").snippet("My location"));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(cmu).zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(SmartLocation.with(activity).location().state().locationServicesEnabled()) {
            LocationParams params = new LocationParams.Builder()
                    .setAccuracy(LocationAccuracy.HIGH)
                    .setInterval(2500)
                    .setDistance(10)
                    .build();
            SmartLocation.with(activity)
                    .location()
                    .config(params)
                    .start(this);
        } else {
            locationServiceUnavailable();
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        SmartLocation.with(activity)
                .location()
                .stop();
    }

    @Override
    public void onLocationUpdated(Location location) {
        setMaps(location.getLatitude(), location.getLongitude());
    }

    private void locationServiceUnavailable() {
        Toast.makeText(activity,"Location service unavailable, Please turn it on",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        SmartLocation.with(activity)
                .location()
                .start(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        SmartLocation.with(activity)
                .location()
                .stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SmartLocation.with(activity)
                .location()
                .stop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}

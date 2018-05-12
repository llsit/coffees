package com.cmu.nuts.coffee9.main.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Shop;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;


public class NearByFragment extends Fragment implements OnLocationUpdatedListener {


    public NearByFragment() {
        // Required empty public constructor
    }

    MapView mMapView;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private Activity activity;
    ArrayList<HashMap<String, String>> location = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map = new HashMap<String, String>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_near_by, container, false);
        activity = getActivity();
        mMapView = view.findViewById(R.id.mapView);
        final MapStyleOptions style;
        style = new MapStyleOptions("[" +
                "  {" +
                "    \"featureType\":\"poi.business\"," +
                "    \"elementType\":\"all\"," +
                "    \"stylers\":[" +
                "      {" +
                "        \"visibility\":\"off\"" +
                "      }" +
                "    ]" +
                "  }," +
                "  {" +
                "    \"featureType\":\"transit\"," +
                "    \"elementType\":\"all\"," +
                "    \"stylers\":[" +
                "      {" +
                "        \"visibility\":\"off\"" +
                "      }" +
                "    ]" +
                "  }" +
                "]");

        try {
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume(); // needed to get the map to display immediately
        } catch (Exception e) {
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
                googleMap.setMapStyle(style);
                int REQUEST_CODE_ASK_PERMISSIONS = 123;
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }

                if (ActivityCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }
            }
        });

        return view;
    }


    private void setMaps(final double latitude, final double longitude) {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                int REQUEST_CODE_ASK_PERMISSIONS = 123;
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }

                if (ActivityCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }

//                for (int i = 0; i < location.size(); i++) {
//                    System.out.println(location.get(i).get("latitude") + " " + location.get(i).get("latitude"));
//                    double Latitudes = Double.parseDouble(location.get(i).get("latitude"));
//                    double Longitudes = Double.parseDouble(location.get(i).get("longitude"));
//                    String name = location.get(i).get("LocationName");
//                    MarkerOptions marker = new MarkerOptions().position(new LatLng(Latitudes, Longitudes)).title(name);
//                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
//                    googleMap.addMarker(marker);
//                }


                googleMap.setMyLocationEnabled(true);
                // For dropping a marker at a point on the Map
                LatLng cmu = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(cmu).title("Here").snippet("My location"));
                // For zooming automatically to the location of the marker



                DatabaseReference mDatebase = FirebaseDatabase.getInstance().getReference(Shop.tag);
                mDatebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            Shop shop = item.getValue(Shop.class);
                            assert shop != null;
                            String locats = shop.getLocation();
                            String separated[] = locats.split("\\|");
                            String lat = null;
                            for (int i = 0; i < separated.length - 1; i++) {
                                lat = separated[i];
//                        System.out.println(lat);
                            }
                            String longs = null;
                            for (int i = 1; i < separated.length; i++) {
                                longs = separated[1];
//                        System.out.println(longs);
                            }
//                    Toast.makeText(getActivity(), locats, Toast.LENGTH_SHORT).show();
                            map.put("LocationName", shop.getName());
                            map.put("latitude", lat);
                            map.put("longitude", longs);
                            location.add(map);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                for (int i = 0; i < location.size(); i++) {
                    System.out.println(location.get(i).get("latitude") + " " + location.get(i).get("longitude"));
                }
                CameraPosition cameraPosition = new CameraPosition.Builder().target(cmu).zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (SmartLocation.with(activity).location().state().locationServicesEnabled()) {
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
        Toast.makeText(activity, "Location service unavailable, Please turn it on", Toast.LENGTH_SHORT).show();
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

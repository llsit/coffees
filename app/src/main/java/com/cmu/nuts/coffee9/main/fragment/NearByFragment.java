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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

    private MapView mMapView;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private Activity activity;
    private ArrayList<HashMap<String, String>> location;
    private HashMap<String, String> map;
    private Double Latitude = 0.00;
    private Double Longitude = 0.00;

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
                location = new ArrayList<HashMap<String, String>>();
                DatabaseReference mDatebase = FirebaseDatabase.getInstance().getReference(Shop.tag);
                mDatebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            map = new HashMap<String, String>();
                            Shop shop = item.getValue(Shop.class);
                            assert shop != null;
                            String locats = shop.getLocation();
                            String separated[] = locats.split("\\|");
                            String lat = null;
                            for (int i = 0; i < separated.length - 1; i++) {
                                lat = separated[i];
                            }
                            String longs = null;
                            for (int i = 1; i < separated.length; i++) {
                                longs = separated[i];
                            }
                            map.put("LocationName", shop.getName());
                            map.put("latitude", lat);
                            map.put("longitude", longs);
//                            Toast.makeText(getActivity(), map.get("latitude") + " " + map.get("longitude"), Toast.LENGTH_SHORT).show();
                            location.add(map);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                googleMap.setMyLocationEnabled(true);
                // For dropping a marker at a point on the Map
                LatLng cmu = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(cmu).title("Here").snippet("My location"));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(cmu).zoom(16).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                if (location.size() > 0) {
                    Toast.makeText(getContext(), "Not Empty",
                            Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < location.size(); i++) {
                        System.out.println(location.get(i).get("latitude") + " " + location.get(i).get("longitude"));
                        Toast.makeText(getContext(), location.get(i).get("latitude") + " " + location.get(i).get("longitude"),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Empty",
                            Toast.LENGTH_SHORT).show();
                }

//                for (int i = 0; i < location.size(); i++) {
//                    Latitude = Double.valueOf(location.get(i).get("latitude"));
//                    Longitude = Double.valueOf(location.get(i).get("longitude"));
//                    String name = location.get(i).get("LocationName");
//                    MarkerOptions marker = new MarkerOptions().position(new LatLng(Latitude, Longitude)).title(name);
//                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
//                    googleMap.addMarker(marker);
//                }

//                MarkerOptions marker = new MarkerOptions().position(new LatLng(Latitude, Longitude)).title("Test");
//                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
//                googleMap.addMarker(marker);

                // Location 1
                map = new HashMap<String, String>();
                map.put("LocationID", "1");
                map.put("Latitude", "18.804313");
                map.put("Longitude", "98.951609");
                map.put("LocationName", "Ladplakao 76");
                location.add(map);

                // Location 2
                map = new HashMap<String, String>();
                map.put("LocationID", "2");
                map.put("Latitude", "18.803346");
                map.put("Longitude", "98.952647");
                map.put("LocationName", "Ladplakao 70");
                location.add(map);

                // Location 3
                map = new HashMap<String, String>();
                map.put("LocationID", "3");
                map.put("Latitude", "18.804136");
                map.put("Longitude", "98.952535");
                map.put("LocationName", "Ladplakao 80");
                location.add(map);

                // *** Marker (Loop)
                for (int i = 0; i < location.size(); i++) {
                    Latitude = Double.valueOf(location.get(i).get("Latitude"));
                    Longitude = Double.valueOf(location.get(i).get("Longitude"));
                    String name = location.get(i).get("LocationName");
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(Latitude, Longitude)).title(name);
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
                    googleMap.addMarker(marker);
                }
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

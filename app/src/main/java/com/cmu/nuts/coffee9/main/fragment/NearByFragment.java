package com.cmu.nuts.coffee9.main.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.data_shop.DataShopActivity;
import com.cmu.nuts.coffee9.model.Shop;
import com.cmu.nuts.coffee9.utillity.PermissionUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.Executor;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

import static com.facebook.login.widget.ProfilePictureView.TAG;


public class NearByFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, LocationListener {


    public NearByFragment() {
        // Required empty public constructor
    }

    private GoogleMap googleMap;
    private Activity activity;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_near_by, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        activity = getActivity();

        return view;
    }

    private void setMaps(final double latitude, final double longitude) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Shop.tag);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Shop shop = item.getValue(Shop.class);
                    assert shop != null;
                    String locals = shop.getLocation();
                    String separated[] = locals.split("\\|");
                    String lat = "18.7990956";
                    String longs = "98.9621415";
                    if (separated.length >= 2) {
                        lat = separated[0];
                        longs = separated[1];
                    }
                    showLocation(lat, longs, shop.getName(), shop.getSid());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error while getting coffee shop's location cause by : ".concat(databaseError.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });

        // For dropping a marker at a point on the Map
        LatLng current = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(current).title("Here").snippet("Me"));
        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(current).zoom(16).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void showLocation(String lat, String longs, final String name, final String uid) {
        final LatLng latLng = new LatLng(Double.valueOf(lat), Double.valueOf(longs));
        MarkerOptions marker = new MarkerOptions().position(latLng).title(name).snippet("Coffee cafe'");
        BitmapDrawable icon = (BitmapDrawable) getResources().getDrawable(R.drawable.img_cafe_location);
        Bitmap bitmap = icon.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        googleMap.addMarker(marker);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(getActivity(), DataShopActivity.class);
                intent.putExtra("shopID", uid);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);
        enableMyLocation();
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));

        int REQUEST_CODE_ASK_PERMISSIONS = 123;
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }

        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }
        googleMap.setMyLocationEnabled(true);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(Objects.requireNonNull(getActivity()), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            setMaps(location.getLatitude(), location.getLongitude());
                        }
                    }
                });
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity) getContext(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (googleMap != null) {
            // Access to the location has been granted to the app.
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getChildFragmentManager(), "dialog");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        setMaps(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

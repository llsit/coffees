package com.cmu.nuts.coffee9.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cmu.nuts.coffee9.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewMapShopActivity extends AppCompatActivity {
    private MapView mapView_data;
    private GoogleMap googleMap;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map_shop);

        mapView_data = findViewById(R.id.mapView_data);
        bundle = savedInstanceState;

        String lat = getIntent().getStringExtra("Lat");
        String Lng = getIntent().getStringExtra("Lng");

        assert lat != null;
        assert Lng != null;
        setMaps(Double.valueOf(lat), Double.valueOf(Lng));

        ImageView map_display_back = findViewById(R.id.map_display_back);
        map_display_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setMaps(final double latitude, final double longitude) {
        try {
            mapView_data.onCreate(bundle);
            mapView_data.onResume(); // needed to get the map to display immediately
        } catch (Exception e) {
            Log.e("Google maps error", "The error is " + e.getMessage());
            e.printStackTrace();
        }

        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView_data.getMapAsync(new OnMapReadyCallback() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                int REQUEST_CODE_ASK_PERMISSIONS = 123;
                if (ActivityCompat.checkSelfPermission(ViewMapShopActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE_ASK_PERMISSIONS);
                    }
                    return;
                }

                if (ActivityCompat.checkSelfPermission(ViewMapShopActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }

                googleMap.setMyLocationEnabled(true);
                // For dropping a marker at a point on the Map
                LatLng cmu = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(cmu).title("Here").snippet(""));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(cmu).zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }
}

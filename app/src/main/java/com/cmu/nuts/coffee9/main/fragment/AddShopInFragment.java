package com.cmu.nuts.coffee9.main.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.AddShopActivity;
import com.cmu.nuts.coffee9.main.addDateTimeActivity;
import com.cmu.nuts.coffee9.model.Open_Hour;
import com.cmu.nuts.coffee9.model.Shop;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddShopInFragment extends Fragment implements OnLocationUpdatedListener {
    EditText nameshop, Addressshop, detail, location;
    TextView open_hour;
    Button btn_add;
    RadioGroup radio_price;
    RadioButton min, mid, max;
    MapView mMapView;
    private GoogleMap googleMap;
    private Activity activity;

    private String coffee_ID, name, addressshop, Detail, authorID, locat, open, prices, rating;

    private DatabaseReference mDatabase;
    FirebaseUser user;

    public AddShopInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_shop_in, container, false);
        location = view.findViewById(R.id.location);
        location.setEnabled(false);
        nameshop = view.findViewById(R.id.edt_name_shop);
        Addressshop = view.findViewById(R.id.edt_address_shop);
        detail = view.findViewById(R.id.edt_detail);
        open_hour = view.findViewById(R.id.edt_open_hour);
        radio_price = view.findViewById(R.id.rg);
        radio_price.check(R.id.rdo_min);
        open_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecttime();
            }
        });
        btn_add = view.findViewById(R.id.btn_add);
        mMapView = view.findViewById(R.id.add_shp_map_view);
        activity = getActivity();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        coffee_ID = mDatabase.push().getKey();

        addShop();
        setMap(savedInstanceState);
        return view;
    }

    private void addShop() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameshop.getText().toString();
                addressshop = Addressshop.getText().toString();
                Detail = detail.getText().toString();
                authorID = user.getUid();

                locat = location.getText().toString();
                open = "0";
                rating = "0";
                switch (radio_price.getCheckedRadioButtonId()) {
                    case R.id.rdo_min:
                        prices = activity.getString(R.string.txt_rang_0_100);
                        break;
                    case R.id.rdo_mid:
                        prices = activity.getString(R.string.txt_rang_101_200);
                        break;
                    case R.id.rdo_max:
                        prices = activity.getString(R.string.txt_rang_over_200);
                        break;
                }

                Shop shopData = new Shop(coffee_ID, name, addressshop, Detail, locat, open, prices, authorID);
                mDatabase.child("coffee_shop").child(coffee_ID).setValue(shopData);

//                DatabaseReference tDatebase = FirebaseDatabase.getInstance().getReference(Open_Hour.getTag());
//                String tid = tDatebase.push().getKey();
//                String tsid = coffee_ID;
////                String date = result.toString();
//                String timestart = arrayList.get(1);
//                String timeend = arrayList.get(2);
//                Open_Hour open = new Open_Hour(tsid, tid, "date", timestart, timeend);
//
//                tDatebase.child(tsid).child(tsid).setValue(open);

                Toast.makeText(getContext(), "Add Success", Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Bundle bundle = data.getExtras();

            ArrayList<Open_Hour> openHourArrayList = (ArrayList<Open_Hour>) data.getSerializableExtra("EXTRA_DATA");
            System.out.println(openHourArrayList);
//            openHourArrayList = (ArrayList<Open_Hour>)data.getSerializableExtra("MESSAGE");
        }
    }

    private void selecttime() {
        Intent i = new Intent(getContext(), addDateTimeActivity.class);
        i.putExtra("sid", coffee_ID);
        startActivity(i);
    }

    private void setMap(Bundle savedInstanceState) {
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
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                int REQUEST_CODE_ASK_PERMISSIONS = 123;
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE_ASK_PERMISSIONS);
                    }
                    return;
                }

                if (ActivityCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }

                googleMap.setMyLocationEnabled(true);
                // For dropping a marker at a point on the Map
                LatLng cmu = new LatLng(18.8037401, 98.9525114);
                googleMap.addMarker(new MarkerOptions().position(cmu).title("CMU").snippet("Computer Science"));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(cmu).zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setMaps(final double latitude, final double longitude) {
        location.setEnabled(true);
        location.setText(String.valueOf(latitude) + "|" + String.valueOf(longitude));
        location.setEnabled(false);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
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

    private void locationServiceUnavailable() {
        Toast.makeText(activity, "Location service unavailable, Please turn it on", Toast.LENGTH_SHORT).show();
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

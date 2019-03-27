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
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
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
import com.cmu.nuts.coffee9.main.addDateTimeActivity;
import com.cmu.nuts.coffee9.model.Open_Hour;
import com.cmu.nuts.coffee9.model.Shop;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddShopInFragment extends Fragment implements OnLocationUpdatedListener, LocationListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    EditText nameshop, Addressshop, detail, location;
    TextView open_hour;
    Button btn_add, btn_update;

    RadioGroup radio_price;
    MapView mMapView;
    private GoogleMap googleMap;
    private Activity activity;

    private String coffee_ID, name, addressshop, Detail, authorID, locat, open, prices, rating;
    ArrayList<Open_Hour> myobj = new ArrayList<>();

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
        btn_update = view.findViewById(R.id.btn_update);
        btn_update.setVisibility(View.GONE);

        addShop();

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

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));

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

                Shop shopData = new Shop(coffee_ID, name, addressshop, Detail, locat, open, prices, authorID, Open_Hour.getTag());
                mDatabase.child("coffee_shop").child(coffee_ID).setValue(shopData);

                if (myobj.size() > 0) {
                    for (int i = 0; i < myobj.size(); i++) {
                        mDatabase.child("coffee_shop").child(coffee_ID).child(Open_Hour.getTag()).child(myobj.get(i).getTid()).setValue(myobj.get(i));
                    }
                }
//                Toast.makeText(getContext(), "Add Success", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(Objects.requireNonNull(getView()), "Add Success", Snackbar.LENGTH_LONG);
                snackbar.show();

                Objects.requireNonNull(getActivity()).finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            myobj.clear();
            ArrayList<Open_Hour> openHourArrayList = (ArrayList<Open_Hour>) data.getSerializableExtra("EXTRA_DATA");
            myobj.addAll(openHourArrayList);
            if (!myobj.isEmpty())
                open_hour.setText(myobj.get(0).getDate() + myobj.get(0).getTimestart());
        }
    }


    private void selecttime() {
        Intent i = new Intent(getContext(), addDateTimeActivity.class);
        i.putExtra("sid", coffee_ID);
        i.putExtra("arrayListTime", myobj);
        startActivityForResult(i, 1);
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

                // For dropping a marker at a point on the Map
                LatLng position = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(position).title("Here").snippet("My location"));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    private void locationServiceUnavailable() {
        Toast.makeText(activity, "Location service unavailable, Please turn it on", Toast.LENGTH_LONG).show();
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
}

package com.cmu.nuts.coffee9.main.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
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

import butterknife.ButterKnife;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddShopFragment extends Fragment implements OnLocationUpdatedListener {

    EditText nameshop, Addressshop, detail, location, open_hour;
    Button btn_add;
    RadioGroup radio_price;
    RadioButton min, mid, max;
    MapView mMapView;
    private GoogleMap googleMap;
    private Activity activity;

    private String coffee_ID, name, addressshop, Detail, authorID, locat, open, prices, rating;

    private ArrayList<String> arrayList = new ArrayList<String>();
    private StringBuffer result = new StringBuffer();
    private DatabaseReference mDatabase;
    FirebaseUser user;

    public AddShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_shop, container, false);
        ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        Toolbar toolbar = view.findViewById(R.id.toolbarAddShop);
        toolbar.setTitle(getString(R.string.txt_add_coffee_shop));
        assert getActivity() != null;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        activity = getActivity();
        addShop(view);
        setMaps(view, savedInstanceState);
        return view;
    }

    @SuppressLint("WrongViewCast")
    private void addShop(final View view) {
        location = view.findViewById(R.id.location);
        location.setEnabled(false);
        nameshop = view.findViewById(R.id.edt_name_shop);
        Addressshop = view.findViewById(R.id.edt_address_shop);
        detail = view.findViewById(R.id.edt_detail);
        open_hour = view.findViewById(R.id.edt_open_hour);
        radio_price = view.findViewById(R.id.rdo_price);
        radio_price.check(R.id.rdo_min);
        open_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecttime();
            }
        });
        btn_add = view.findViewById(R.id.btn_add);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameshop.getText().toString();
                addressshop = Addressshop.getText().toString();
                Detail = detail.getText().toString();
                authorID = user.getUid();
                coffee_ID = mDatabase.push().getKey();
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

                DatabaseReference tDatebase = FirebaseDatabase.getInstance().getReference(Open_Hour.getTag());
                String tid = tDatebase.push().getKey();
                String tsid = coffee_ID;
                String date = result.toString();
                String timestart = arrayList.get(1);
                String timeend = arrayList.get(2);
                Open_Hour open = new Open_Hour(tsid, tid, date, timestart, timeend);

                tDatebase.child(tsid).child(tsid).setValue(open);

                Toast.makeText(getActivity(), "Add Success", Toast.LENGTH_SHORT).show();


                onBackPressed();
            }
        });
    }

    private void selecttime() {

        open_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();

                @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_addtime, null);
                builder.setView(view);

                final CheckBox day1 = view.findViewById(R.id.day1);
                final CheckBox day2 = view.findViewById(R.id.day2);
                final CheckBox day3 = view.findViewById(R.id.day3);
                final CheckBox day4 = view.findViewById(R.id.day4);
                final CheckBox day5 = view.findViewById(R.id.day5);
                final CheckBox day6 = view.findViewById(R.id.day6);
                final CheckBox day7 = view.findViewById(R.id.day7);
                final EditText hr = view.findViewById(R.id.hr);
                final EditText hr2 = view.findViewById(R.id.hr2);
                hr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                                hr.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
//                                result.append("[").append(selectedHour).append(selectedMinute).append("]");
                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                    }
                });

                hr2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                hr2.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
//                                result.append("[").append(selectedHour).append(selectedMinute).append("]");
                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                    }
                });

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Check username password

                        if (day1.isChecked()) {
                            result.append("Sunday");
//                            arrayList.add("Sunday");
                        }
                        if (day2.isChecked()) {
                            result.append("Monday");
//                            arrayList.add("Monday");
                        }
                        if (day3.isChecked()) {
                            result.append("Tuesday");
//                            arrayList.add("Tuesday");
                        }
                        if (day4.isChecked()) {
                            result.append("Wednesday");
//                            arrayList.add("Wednesday");
                        }
                        if (day5.isChecked()) {
                            result.append("Thursday");
//                            arrayList.add("Thursday");
                        }
                        if (day6.isChecked()) {
                            result.append("Friday");
//                            arrayList.add("Friday");
                        }
                        if (day7.isChecked()) {
                            result.append("Saturday");
//                            arrayList.add("Saturday");
                        }
                        String time_start = hr.getText().toString();
                        String time_end = hr2.getText().toString();
                        arrayList.add(String.valueOf(result));
                        arrayList.add(time_start);
                        arrayList.add(time_end);

                        open_hour.setText(result + time_start + time_end);
//                        Toast.makeText(DateTimePickerActivity.this, result.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
            }
        });
    }

    private void setMaps(View view, Bundle savedInstanceState) {
        mMapView = view.findViewById(R.id.add_shp_map_view);

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

    public void onBackPressed() {
        getActivity().onBackPressed();
    }
}

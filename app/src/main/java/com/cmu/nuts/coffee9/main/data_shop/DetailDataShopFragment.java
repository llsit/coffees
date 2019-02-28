package com.cmu.nuts.coffee9.main.data_shop;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.model.OpenTime;
import com.cmu.nuts.coffee9.model.Shop;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailDataShopFragment extends Fragment {

    private TextView name, address, detail, openTime, price;
    private RatingBar rating;
    private String shopID;
    private MapView mapView_data;
    private GoogleMap googleMap;
    private Bundle bundle;
    private View view_map;

    public DetailDataShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_data_shop, container, false);
        mapView_data = view.findViewById(R.id.mapView_data);
        bundle = savedInstanceState;
        view_map = view.findViewById(R.id.view_map);

        if (getArguments() != null) {
            shopID = getArguments().getString("shop_ID");
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.location_shop);
            detail = view.findViewById(R.id.detail);
            openTime = view.findViewById(R.id.datetime);
            price = view.findViewById(R.id.price);
            rating = view.findViewById(R.id.rating);
            getdatashop();
        }

        return view;
    }

    public void getdatashop() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Shop.tag).child(shopID);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Shop shop = dataSnapshot.getValue(Shop.class);
                assert shop != null;
                name.setText(shop.getName());
                address.setText(shop.getAddress());
                detail.setText(shop.getDetail());
                getTime(shop.getSid(), openTime);
                price.setText(shop.getPrice());
                rating.setRating(Integer.parseInt(shop.getRating()));
                String location = shop.getLocation();
                final String[] latlong = location.split("\\|");
                setMaps(Double.valueOf(latlong[0]), Double.valueOf(latlong[1]));
                view_map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Lat", latlong[0]);
                        bundle.putString("Lng", latlong[1]);
                        //set Fragmentclass Arguments

                        ViewMapShopFragment viewMapShopFragment = new ViewMapShopFragment();
                        FragmentManager manager_home = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                        FragmentTransaction hm = manager_home.beginTransaction();
                        hm.replace(R.id.view_map_shop, viewMapShopFragment);
                        hm.commit();

                        viewMapShopFragment.setArguments(bundle);
//                        Toast.makeText(getActivity(), "View map!",
//                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to load shop data!",
                        Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addListenerForSingleValueEvent(listener);
    }

    private void getTime(final String shopID, final TextView openTimeTv) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Open_Hour").child(shopID);
        final ArrayList<OpenTime> openTimes = new ArrayList<>();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OpenTime value = snapshot.getValue(OpenTime.class);
                    assert value != null;
                    openTimeTv.setText(value.getDate() + " " + value.getTimestart() + " - " + value.getTimeend());
                }

                if (!dataSnapshot.exists()) {
                    openTimeTv.setText("Monday,Tuesday,Wednesday,Thursday,Friday \n 08:00 - 16:00");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                openTimeTv.setText("Unknown");
                Toast.makeText(getActivity(), "Failed to load shop data!",
                        Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addListenerForSingleValueEvent(listener);
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
            MapsInitializer.initialize(getActivity().getApplicationContext());
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
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE_ASK_PERMISSIONS);
                    }
                    return;
                }

                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }

                googleMap.setMyLocationEnabled(true);
                // For dropping a marker at a point on the Map
                LatLng cmu = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(cmu).title("CMU").snippet("Computer Science"));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(cmu).zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

}

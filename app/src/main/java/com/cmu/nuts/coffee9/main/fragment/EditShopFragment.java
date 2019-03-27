package com.cmu.nuts.coffee9.main.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.addDateTimeActivity;
import com.cmu.nuts.coffee9.main.data_shop.EditDataShopActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import io.nlopez.smartlocation.OnLocationUpdatedListener;

public class EditShopFragment extends Fragment implements OnLocationUpdatedListener {
    private EditText nameshop, Addressshop, detail, location;
    private TextView open_hour;
    private Button btn_edit, btn_update;
    private RadioGroup radio_price;
    private MapView mMapView;
    private GoogleMap googleMap;

    private String coffee_ID, name, addressshop, Detail, authorID, locat, open, prices, rating;
    private ArrayList<Open_Hour> myOh = new ArrayList<>();
    private String name_shop;

    private DatabaseReference mDatabase;
    FirebaseUser user;
    Activity activity;


    public EditShopFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        btn_edit = view.findViewById(R.id.btn_add);
        btn_edit.setVisibility(View.GONE);
        btn_update = view.findViewById(R.id.btn_update);
        activity = getActivity();
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
        assert getArguments() != null;
        coffee_ID = getArguments().getString("shop_id");
        getDataShop();
        editDataShop();

        return view;
    }

    private void getDataShop() {
        mDatabase = FirebaseDatabase.getInstance().getReference(Shop.tag).child(coffee_ID);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Shop shops = dataSnapshot.getValue(Shop.class);
//                Toast.makeText(getContext(), "id = " + shops.getSid(), Toast.LENGTH_SHORT).show();
                    assert shops != null;
                    nameshop.setText(shops.getName());
                    Addressshop.setText(shops.getAddress());
                    detail.setText(shops.getDetail());
                    open_hour.setText(shops.getOpen_hour().trim());
                    String[] locat = shops.getLocation().split("\\|");
                    setMaps(Double.valueOf(locat[0]), Double.valueOf(locat[1]));
                    switch (shops.getPrice()) {
                        case "0-100":
                            radio_price.check(R.id.rdo_min);
                            break;
                        case "101-150":
                            radio_price.check(R.id.rdo_mid);
                            break;
                        case "151-200":
                            radio_price.check(R.id.rdo_max);
                            break;
                    }
                    name_shop = shops.getName();
                    location.setText(shops.getLocation());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error while getting coffee shop's location cause by : ".concat(databaseError.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
        mDatabase.child(Open_Hour.tag).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Open_Hour open_hour = data.getValue(Open_Hour.class);
                        myOh.add(open_hour);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error while getting coffee shop's location cause by : ".concat(databaseError.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selecttime() {
        Intent i = new Intent(getContext(), addDateTimeActivity.class);
        i.putExtra("sid", coffee_ID);
        i.putExtra("arrayListTime", myOh);
        startActivityForResult(i, 1);
    }

    private void editDataShop() {
        System.out.println(myOh);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameshop.getText().toString();
                addressshop = Addressshop.getText().toString();
                Detail = detail.getText().toString();
                authorID = user.getUid();
                locat = location.getText().toString();
//                rating = rating.getText().toString();
                rating = "12";
                switch (radio_price.getCheckedRadioButtonId()) {
                    case R.id.rdo_min:
                        prices = getString(R.string.txt_rang_0_100);
                        break;
                    case R.id.rdo_mid:
                        prices = getString(R.string.txt_rang_101_200);
                        break;
                    case R.id.rdo_max:
                        prices = getString(R.string.txt_rang_over_200);
                        break;
                }
                Shop shopData = new Shop(coffee_ID, name, addressshop, Detail, locat, rating, prices, authorID, Open_Hour.tag);
                mDatabase.child("coffee_shop").child(coffee_ID).setValue(shopData);

                for (int i = 0; i < myOh.size(); i++) {
                    mDatabase.child("coffee_shop").child(coffee_ID).child(Open_Hour.tag).child(myOh.get(i).getTid()).setValue(shopData);
                }
                Snackbar snackbar = Snackbar
                        .make(Objects.requireNonNull(getView()), "Update Success", Snackbar.LENGTH_LONG);
                snackbar.show();

                Objects.requireNonNull(getActivity()).finish();
            }
        });
    }

    private void setMaps(final double latitude, final double longitude) {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For dropping a marker at a point on the Map
                LatLng position = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(position).title("Here").snippet(name_shop));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    @Override
    public void onLocationUpdated(Location location) {

    }
}

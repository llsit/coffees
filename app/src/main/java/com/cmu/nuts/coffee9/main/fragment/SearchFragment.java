package com.cmu.nuts.coffee9.main.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.adapter.ShopRecyclerAdapter;
import com.cmu.nuts.coffee9.model.Shop;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;

public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    SearchView searchView;
    private Activity activity;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<Shop> shops;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String query = null;
    private ImageView filter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        searchView = view.findViewById(R.id.fragment_search_view);
        TextView addShop = view.findViewById(R.id.fragment_search_btn_add);
        activity = getActivity();
        recyclerView = view.findViewById(R.id.search_recycler_view);
        filter = view.findViewById(R.id.filter);
        database = FirebaseDatabase.getInstance();
        filters();


//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                databaseReference = database.getReference(Shop.tag);
//                if (query != null) {
//                    getShopDatabase(query);
//                }
//            }
//        });


        addShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddShopFragment addShop = new AddShopFragment();
                FragmentManager manager_addShop = getFragmentManager();
                assert manager_addShop != null;
                FragmentTransaction as = manager_addShop.beginTransaction();
                as.add(R.id.Fragment, addShop);
                as.addToBackStack(null);
                as.commit();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                databaseReference = database.getReference(Shop.tag);
                compareDatabase(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (shops != null && shops.size() > 0) {
                    onSearch(shops, newText);
                }
                return false;
            }
        });
        return view;
    }

    private void filters() {

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                LayoutInflater inflater = getLayoutInflater();

                @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_custom, null);
                builder.setView(view);

                final CheckBox price_max = view.findViewById(R.id.max);
                final CheckBox price_mid = view.findViewById(R.id.mid);
                final CheckBox price_min = view.findViewById(R.id.min);
                final CheckBox star5 = view.findViewById(R.id.rating5);
                final CheckBox star4 = view.findViewById(R.id.rating4);
                final CheckBox star3 = view.findViewById(R.id.rating3);
                final CheckBox star2 = view.findViewById(R.id.rating2);
                final CheckBox star1 = view.findViewById(R.id.rating1);
                final CheckBox now = view.findViewById(R.id.now);


                final ArrayList<String> arrayList = new ArrayList<String>();
                builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date currentTime = Calendar.getInstance().getTime();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("EEE:HH:mm");
                        String nows;
                        // Check username password
                        StringBuffer result = new StringBuffer();
                        if (star5.isChecked()) {
                            result.append("5");
                            arrayList.add("5");
                        }
                        if (star4.isChecked()) {
                            result.append("4");
                            arrayList.add("4");
                        }
                        if (star3.isChecked()) {
                            result.append("3");
                            arrayList.add("3");
                        }
                        if (star2.isChecked()) {
                            result.append("2");
                            arrayList.add("2");
                        }
                        if (star1.isChecked()) {
                            result.append("1");
                            arrayList.add("1");
                        }
                        if (price_max.isChecked()) {
                            result.append("151-200:");
                            arrayList.add("151-200");
                        }
                        if (price_mid.isChecked()) {
                            result.append("101-150:");
                            arrayList.add("101-150");
                        }
                        if (price_min.isChecked()) {
                            result.append("0-100");
                            arrayList.add("0-100");
                        }
                        if (now.isChecked()) {
                            nows = mdformat.format(currentTime);
                            result.append(nows);
                            arrayList.add(nows);
                        }
                        compareFilter(arrayList);
//                        Toast.makeText(getContext(), result.toString(),
//                                Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
//                Intent intent = new Intent(getActivity(), SearchFilterActivity.class);
//                startActivity(intent);
            }
        });

    }

    private void compareFilter(final ArrayList<String> values) {
        DatabaseReference fDatabase = FirebaseDatabase.getInstance().getReference(Shop.tag);
        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Shop shops = item.getValue(Shop.class);
                    for (int i = 0; i < values.size(); i++) {
                        assert shops != null;
                        if (shops.getPrice().toLowerCase().equals(values.get(i).toLowerCase())) {
                            getShopDatabase(shops.getSid());
//                            Log.d("Search", " price " + shops.getPrice() + " value = " + values.get(i) + " sid = " + shops.getSid());
//                            arrayShop.add(shops);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onSearch(List<Shop> list, String key) {
        List<Shop> new_shop = new ArrayList<>();
        for (int i = 1; i < list.size(); i++) {
            try {
                if (list.get(i).getName().contains(key)) {
                    new_shop.add(list.get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setRecyclerView(new_shop);
    }

    private void compareDatabase(final String query) {
        shops = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String values = snapshot.child("name").getValue(String.class);

                    assert values != null;
                    if (values.toLowerCase().contains(query.toLowerCase())) {
                        getShopDatabase(id);
//                        Log.d("Search", " name " + values + " id = " + id);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Shop", "Failed to get database", databaseError.toException());
//                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void getShopDatabase(String id) {
//        Log.d("Get", " id = " + id);
        shops = new ArrayList<>();
        DatabaseReference sDatabase = FirebaseDatabase.getInstance().getReference(Shop.tag).child(id);
        sDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Shop value = dataSnapshot.getValue(Shop.class);
                shops.add(value);

                setRecyclerView(shops);
//                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Shop", "Failed to get database", databaseError.toException());
//                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setRecyclerView(List<Shop> list) {
        ShopRecyclerAdapter recyclerAdapter = new ShopRecyclerAdapter(list, activity);
        RecyclerView.LayoutManager recycle = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(recycle);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }
}

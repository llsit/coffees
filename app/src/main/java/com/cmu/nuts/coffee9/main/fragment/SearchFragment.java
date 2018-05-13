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
import com.cmu.nuts.coffee9.model.Open_Hour;
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
    private CheckBox price_max, price_mid, price_min, star5, star4, star3, star2, star1, now;

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

                price_max = view.findViewById(R.id.max);
                price_mid = view.findViewById(R.id.mid);
                price_min = view.findViewById(R.id.min);
                star5 = view.findViewById(R.id.rating5);
                star4 = view.findViewById(R.id.rating4);
                star3 = view.findViewById(R.id.rating3);
                star2 = view.findViewById(R.id.rating2);
                star1 = view.findViewById(R.id.rating1);
                now = view.findViewById(R.id.now);

                builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkfilter();
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

    private void checkfilter() {
        final ArrayList<String> arrayList1 = new ArrayList<String>();
        final ArrayList<String> arrayList2 = new ArrayList<String>();
        final ArrayList<String> arrayList3 = new ArrayList<String>();
        final boolean[] status = {false};
        final boolean[] statuscheck = {false};

        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("EEEE|HH:mm");
        String nows;
        arrayList1.clear();
        arrayList2.clear();
        arrayList3.clear();
        if (star5.isChecked()) {
            arrayList3.add("5");
            statuscheck[0] = true;
        }
        if (star4.isChecked()) {
            arrayList3.add("4");
            statuscheck[0] = true;
        }
        if (star3.isChecked()) {
            arrayList3.add("3");
            statuscheck[0] = true;
        }
        if (star2.isChecked()) {
            arrayList3.add("2");
            statuscheck[0] = true;
        }
        if (star1.isChecked()) {
            arrayList3.add("1");
            statuscheck[0] = true;
        }
        if (price_max.isChecked()) {
            arrayList1.add("151-200");
            statuscheck[0] = true;
        }
        if (price_mid.isChecked()) {
            arrayList1.add("101-150");
            statuscheck[0] = true;
        }
        if (price_min.isChecked()) {
            arrayList1.add("0-100");
            statuscheck[0] = true;
        }
        if (now.isChecked()) {
            nows = mdformat.format(currentTime);
            arrayList2.add(nows);
            statuscheck[0] = true;
        }
        String day = null;
        int hrNow = 0;
        int minNow = 0;
        if (arrayList2.size() > 0) {
            statuscheck[0] = true;
            String[] arrStr = null;

            for (int i = 0; i < arrayList2.size(); i++) {
                String str = arrayList2.get(i);
                arrStr = str.split("\\|");
            }
            day = arrStr[0];
            String hrs = arrStr[1];
            String[] arrtime = hrs.split(":");
            hrNow = Integer.parseInt(arrtime[0]);
            minNow = Integer.parseInt(arrtime[1]);

        }
        final DatabaseReference shopDatabase = FirebaseDatabase.getInstance().getReference(Shop.tag);
        final String finalDay = day;
        final int finalHrNow = hrNow;
        final int finalMinNow = minNow;
        shopDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    final Shop shops = item.getValue(Shop.class);
                    if (arrayList1.size() > 0) {
                        status[0] = true;
                        for (int i = 0; i < arrayList1.size(); i++) {
                            assert shops != null;
                            if (shops.getPrice().equals(arrayList1.get(i))) {
                                String id = shops.getSid();
                                Distinct(id);
                            }
                        }

                    }
                    if (arrayList3.size() > 0) {
                        status[0] = true;
                        for (int i = 0; i < arrayList3.size(); i++) {
                            assert shops != null;
                            if (shops.getRating().equals(arrayList3.get(i))) {
                                String id = shops.getSid();
                                Distinct(id);
                            }
                        }

                    }

                    if (arrayList2.size() > 0) {
                        assert shops != null;
                        status[0] = true;
                        DatabaseReference tDatabase = FirebaseDatabase.getInstance().getReference(Open_Hour.getTag()).child(shops.getSid());
                        tDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot value : dataSnapshot.getChildren()) {
                                    Open_Hour open = value.getValue(Open_Hour.class);
                                    assert open != null;
                                    String start = open.getTimestart();
                                    String end = open.getTimeend();
                                    String[] arrStart = start.split(":");
                                    int hrStart = Integer.parseInt(arrStart[0]);
                                    int minStart = Integer.parseInt(arrStart[1]);
                                    String[] arrEnd = end.split(":");
                                    int hrEnd = Integer.parseInt(arrEnd[0]);
                                    int minEnd = Integer.parseInt(arrEnd[1]);

                                    assert finalDay != null;
                                    if (open.getDate().contains(finalDay)) {
//                                        Log.d("Search", "2 " + finalHrNow + " " + finalMinNow + " start = " + hrStart + " " + minStart + " End " + hrEnd + " " + minEnd);
                                        if ((finalHrNow >= hrStart) && (finalHrNow <= hrEnd)) {
//                                            Log.d("Search", "3 " + finalHrNow + " " + finalMinNow + " start = " + hrStart + " " + minStart + " End " + hrEnd + " " + minEnd);
                                            if (minEnd == 0) {
                                                minEnd = 60;
                                            }
                                            if ((finalMinNow >= minStart) && (finalMinNow <= minEnd)) {
//                                                Log.d("Search", "4 " + finalHrNow + " " + finalMinNow + " start = " + hrStart + " " + minStart + " End " + hrEnd + " " + minEnd);
                                                Distinct(open.getSid());
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (!statuscheck[0]) {
            recyclerView.setVisibility(View.GONE);
        } else {
            if (!status[0]) {
                recyclerView.setVisibility(View.VISIBLE);
                System.out.println("true full");
            } else {
                recyclerView.setVisibility(View.GONE);
                System.out.println("Empty");
            }
        }

//            DatabaseReference shopsDatabase = FirebaseDatabase.getInstance().getReference(Shop.tag);
//            final List<Shop> shops2 = new ArrayList<>();
//            shopsDatabase.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot item : dataSnapshot.getChildren()) {
//                        Shop shops = item.getValue(Shop.class);
//                        assert shops != null;
//                        shops2.add(shops);
//                        setRecyclerView(shops2);
////                        System.out.println(shops.getSid());
////                        getShopDatabase(shops.getSid());
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }

    }

    private void Distinct(String id) {
        final ArrayList<String> arrayListAll = new ArrayList<String>();
        arrayListAll.add(id);
        final ArrayList<String> arrayResult = new ArrayList<String>();
        for (int i = 0; i < arrayListAll.size(); i++) {
            int j;
            for (j = 0; j < i; j++)
                if (arrayListAll.get(i).equals(arrayListAll.get(j)))
                    break;

            if (i == j)
                arrayResult.add(arrayListAll.get(i));
        }

        if (arrayResult.size() > 0) {
            for (String a : arrayResult)
                getShopDatabase(a);
        } else {
            System.out.println("failed");

        }
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

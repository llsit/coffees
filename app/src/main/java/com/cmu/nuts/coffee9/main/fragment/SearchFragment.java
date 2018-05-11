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
//                        Date currentTime = Calendar.getInstance().getTime();
//                        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("EEE|HH:mm");
//                        String nows;
//                        // Check username password
//                        StringBuilder result = new StringBuilder();
//                        if (star5.isChecked()) {
//                            result.append("5");
//                            arrayList.add("5");
//                        }
//                        if (star4.isChecked()) {
//                            result.append("4");
//                            arrayList.add("4");
//                        }
//                        if (star3.isChecked()) {
//                            result.append("3");
//                            arrayList.add("3");
//                        }
//                        if (star2.isChecked()) {
//                            result.append("2");
//                            arrayList.add("2");
//                        }
//                        if (star1.isChecked()) {
//                            result.append("1");
//                            arrayList.add("1");
//                        }
//                        if (price_max.isChecked()) {
//                            result.append("151-200").append(" ");
//                            arrayList.add("151-200");
//                        }
//                        if (price_mid.isChecked()) {
//                            result.append("101-150").append(" ");
//                            arrayList.add("101-150");
//                        }
//                        if (price_min.isChecked()) {
//                            result.append("0-100");
//                            arrayList.add("0-100");
//                        }
//                        if (now.isChecked()) {
//                            nows = mdformat.format(currentTime);
//                            result.append(nows);
//                            arrayList.add(nows);
//                        }
////                        arrayList.add(result.toString());
//                        compareFilter(arrayList);
//                        for (String s : arrayList) {
//                            System.out.println(s);
//                        }
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

    private void checkfilter() {
        final String[] result = new String[1];
        final ArrayList<String> arrayList1 = new ArrayList<String>();
        final ArrayList<String> arrayList2 = new ArrayList<String>();

        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("EEEE|HH:mm");
        String nows;

//        if (star5.isChecked()) {
//            result[0] = "5";
//        }
//        if (star4.isChecked()) {
//            result[0] = "4";
//        }
//        if (star3.isChecked()) {
//            result[0] = "3";
//        }
//        if (star2.isChecked()) {
//            result[0] = "2";
//        }
//        if (star1.isChecked()) {
//            result[0] = "1";
//        }
        if (price_max.isChecked()) {
            arrayList1.add("151-200");

        }
        if (price_mid.isChecked()) {
            arrayList1.add("101-150");

        }
        if (price_min.isChecked()) {
            arrayList1.add("0-100");
        }
        if (now.isChecked()) {
            nows = mdformat.format(currentTime);
            arrayList2.add(nows);
        }
        String day = null;
        int hrNow = 0;
        int minNow = 0;
        if (arrayList2.size() > 0) {
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
                        for (int i = 0; i < arrayList1.size(); i++) {
                            assert shops != null;
                            if (shops.getPrice().equals(arrayList1.get(i))) {
                                String id = shops.getSid();
                                Distinct(id);
                            }
                        }
                    }

                    if (arrayList2.size() > 0) {
                        assert shops != null;
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


    }

    private void Distinct(String id) {
        final ArrayList<String> arrayListAll = new ArrayList<String>();
        arrayListAll.add(id);
        ArrayList<String> arrayResult = new ArrayList<String>();
        for (int i = 0; i < arrayListAll.size(); i++) {
            int j;
            for (j = 0; j < i; j++)
                if (arrayListAll.get(i) == arrayListAll.get(j))
                    break;

            if (i == j)
                arrayResult.add(arrayListAll.get(i));
        }
        for (String a : arrayResult)
            getShopDatabase(a);
//            System.out.println("id = " + a);
    }

    private void compareFilter(final ArrayList<String> values) {
        String[] arrStr = null;
        String day = null;
        for (int i = 0; i < values.size(); i++) {
            String str = values.get(i);
            arrStr = str.split("\\|");
        }
//        for (String a : arrStr)
//            System.out.println(a);
        if (arrStr.length > 1) {
            day = arrStr[0];
            String hrs = arrStr[1];
            String[] arrtime = hrs.split(":");
            //        System.out.println(day + hrs);
            for (String a : arrtime)
                System.out.println(a);
        }

        DatabaseReference fDatabase = FirebaseDatabase.getInstance().getReference(Shop.tag);
        final String finalDay = day;
        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    final Shop shops = item.getValue(Shop.class);
                    for (int i = 0; i < values.size(); i++) {
                        assert shops != null;
                        if (shops.getPrice().toLowerCase().equals(values.get(i).toLowerCase())) {
                            getShopDatabase(shops.getSid());
//                            Log.d("Search", " price " + shops.getPrice() + " value = " + values.get(i) + " sid = " + shops.getSid());
//                            arrayShop.add(shops);
                            DatabaseReference tDatabase = FirebaseDatabase.getInstance().getReference(Open_Hour.getTag()).child(shops.getSid());
                            tDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot value : dataSnapshot.getChildren()) {
                                        Open_Hour open = value.getValue(Open_Hour.class);
                                        if (finalDay != null) {
                                            if (finalDay.contains(open.getDate())) {
                                                Log.d("Search", " price " + shops.getPrice() + " sid = " + shops.getSid() + " date " + open.getDate());
                                            }
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            DatabaseReference tDatabase = FirebaseDatabase.getInstance().getReference(Open_Hour.getTag()).child(shops.getSid());
                            tDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot value : dataSnapshot.getChildren()) {
                                        Open_Hour open = value.getValue(Open_Hour.class);
                                        if (finalDay != null) {
                                            if (finalDay.contains(open.getDate())) {
                                                Log.d("Search", " price " + shops.getPrice() + " sid = " + shops.getSid() + " date " + open.getDate());
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

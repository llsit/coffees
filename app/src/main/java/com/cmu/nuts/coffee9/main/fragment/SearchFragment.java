package com.cmu.nuts.coffee9.main.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.cmu.nuts.coffee9.main.AddShopActivity;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import butterknife.ButterKnife;

public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    SearchView searchView;
    private Activity activity;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<Shop> shops = new ArrayList<>();
    private RecyclerView recyclerView;
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

        addShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddShopActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                databaseReference = database.getReference(Shop.tag);
                System.out.println(query);
                compareDatabase(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onSearch(shops, newText);
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
        final ArrayList<String> arrayList1 = new ArrayList<>();
        final ArrayList<String> arrayList2 = new ArrayList<>();
        final ArrayList<String> arrayList3 = new ArrayList<>();
        final boolean[] status = {false};

        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("EEEE|HH:mm");
        String nows;
        arrayList1.clear();
        arrayList2.clear();
        arrayList3.clear();
        if (star5.isChecked()) {
            arrayList3.add("5");
        }
        if (star4.isChecked()) {
            arrayList3.add("4");
        }
        if (star3.isChecked()) {
            arrayList3.add("3");
        }
        if (star2.isChecked()) {
            arrayList3.add("2");
        }
        if (star1.isChecked()) {
            arrayList3.add("1");
        }
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
        shops.clear();
        shopDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    final Shop shop = item.getValue(Shop.class);
                    if (arrayList1.size() > 0) {
                        status[0] = true;
                        for (int i = 0; i < arrayList1.size(); i++) {
                            assert shop != null;
                            if (shop.getPrice().equals(arrayList1.get(i))) {
                                shops.add(shop);
                            }
                        }
                    }
                    if (arrayList3.size() > 0) {
                        status[0] = true;
                        for (int i = 0; i < arrayList3.size(); i++) {
                            assert shop != null;
                            if (shop.getRating().equals(arrayList3.get(i))) {
                                shops.add(shop);
                            }
                        }
                    }

                    if (arrayList2.size() > 0) {
                        assert shops != null;
                        status[0] = true;
                        DatabaseReference tDatabase = FirebaseDatabase.getInstance().getReference(Open_Hour.getTag()).child(shop.getSid());
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
//                                                Distinct(open.getSid());
                                                System.out.println(shops);
                                                shops.add(shop);
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
                Distinct(shops);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void Distinct(List<Shop> shops) {
        final ArrayList<Shop> arrayListAll = new ArrayList<>(shops);
        final ArrayList<Shop> arrayResult = new ArrayList<>();
        for (int i = 0; i < shops.size(); i++) {
            int j;
            for (j = 0; j < i; j++)
                if (shops.get(i).equals(shops.get(j)))
                    break;
            if (i == j)
                arrayResult.add(arrayListAll.get(i));
        }
//        System.out.println(arrayResult);
        setRecyclerView(arrayResult);
    }

    private void onSearch(List<Shop> list, String key) {
        ArrayList<Shop> new_shop = new ArrayList<>();
        if (list.size() > 0) {
            for (int i = 1; i < list.size(); i++) {
                try {
                    if (list.get(i).getName().contains(key)) {
                        new_shop.add(list.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        setRecyclerView(new_shop);
    }

    private void compareDatabase(final String query) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Shop values = snapshot.getValue(Shop.class);
                    assert values != null;
                    if (values.getName().toLowerCase().contains(query.toLowerCase())) {
                        shops.add(values);
                    }
                }
                setRecyclerView(shops);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Shop", "Failed to get database", databaseError.toException());
            }
        });

    }

    private void setRecyclerView(ArrayList<Shop> list) {
        if (list.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            ShopRecyclerAdapter recyclerAdapter = new ShopRecyclerAdapter(list, activity);
            RecyclerView.LayoutManager recycle = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(recycle);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(recyclerAdapter);
        }
    }

}

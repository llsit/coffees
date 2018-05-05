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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.adapter.ShopRecyclerAdapter;
import com.cmu.nuts.coffee9.model.Shop;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                LayoutInflater inflater = getLayoutInflater();

                @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_custom, null);
                builder.setView(view);

//                final EditText username = (EditText) view.findViewById(R.id.username);
//                final EditText password = (EditText) view.findViewById(R.id.password);

                builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Check username password

                        Toast.makeText(getContext(), "Login Failed!",
                                Toast.LENGTH_SHORT).show();

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

        if (getArguments() != null) {
            ArrayList<String> strtext = getArguments().getStringArrayList("edttext");
            System.out.println(strtext);
            Toast.makeText(getActivity(), strtext.size(), Toast.LENGTH_LONG).show();
            for (String a : strtext) {
                System.out.println(a);
            }
//            assert values != null;
//            compareFilter(values);
        } else {
            System.out.println("Null");
        }


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

    private void compareFilter(ArrayList<String> values) {
        for (String a : values) {
            System.out.print(a);
        }
//        DatabaseReference fDatebase = FirebaseDatabase.getInstance().getReference(Shop.tag);

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
                        Log.d("Search", " name " + values + " id = " + id);
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
//        Log.d("Search", " id = " + id);
        DatabaseReference sDatabase = FirebaseDatabase.getInstance().getReference(Shop.tag).child(id);
        sDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Shop value = dataSnapshot.getValue(Shop.class);
                assert value != null;
                String sid = value.getSid();
                String name = value.getName();
                String address = value.getAddress();
                String detail = value.getDetail();
                String location = value.getLocation();
                String open_time = value.getOpen_hour();
                String price = value.getPrice();
                String uid = value.getUid();

                Shop shop = new Shop(sid, name, address, detail, location, open_time, price, uid);
                shops.add(shop);

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

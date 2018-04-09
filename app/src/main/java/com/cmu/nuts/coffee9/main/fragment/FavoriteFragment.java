package com.cmu.nuts.coffee9.main.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.adapter.FavoriteRecyclerAdapter;
import com.cmu.nuts.coffee9.model.Favorite;
import com.cmu.nuts.coffee9.model.Shop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    public FavoriteFragment() {
        // Required empty public constructor
    }

    private Activity activity;
    private DatabaseReference databaseReference, mDatabase;
    private FirebaseDatabase database;
    private List<Shop> shops;
    private RecyclerView recyclerView;
    String sid;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        activity = getActivity();
        recyclerView = view.findViewById(R.id.fav_recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.fav_swipe_refresh_layout);
        getFavDatabase();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFavDatabase();
            }
        });

        return view;
    }

    private void getFavDatabase() {


        String uid = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
//            Toast.makeText(getActivity(), uid, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "NULL", Toast.LENGTH_LONG).show();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference(Favorite.tag).child(uid);
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Favorite fav = item.getValue(Favorite.class);
                    assert fav != null;
                    String sid = fav.getSid();
//                    arr.add(sid);
//                    Toast.makeText(getActivity(), arr, Toast.LENGTH_LONG).show();
                    databaseReference = FirebaseDatabase.getInstance().getReference(Shop.tag).child(sid);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {
                            shops = new ArrayList<>();
                            shops.clear();
                            for (DataSnapshot item : dataSnapshot2.getChildren()) {

                                Shop value = item.getValue(Shop.class);

//                                String sid = value.getSid();
//                                String name = value.getName();
//                                String address = value.getAddress();
//                                String detail = value.getDetail();
//                                String location = value.getLocation();
//                                String open_time = value.getOpen_hour();
//                                String price = value.getPrice();
//                                String uid = value.getUid();
//
//                                Shop shop = new Shop(sid, name, address, detail, location, open_time, price, uid);
//                                shops.add(shop);
                            }
//                            setRecyclerView();
//                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Shop", "Failed to get database", databaseError.toException());
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Shop", "Failed to get database", databaseError.toException());
                swipeRefreshLayout.setRefreshing(false);
            }
        });

//        databaseReference = FirebaseDatabase.getInstance().getReference(Shop.tag).child(sid);
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot2) {
//                shops = new ArrayList<>();
//                for (DataSnapshot item : dataSnapshot2.getChildren()) {
//                    Shop value = item.getValue(Shop.class);
//
//                    String name1 = null;
//                    if (value != null) {
//                        name1 = value.getName();
//                        Toast.makeText(getActivity(), name1, Toast.LENGTH_LONG).show();
//                    }else{
//
//                        Toast.makeText(getActivity(), item.getKey(), Toast.LENGTH_LONG).show();
//                    }
//
//                    String sid = value.getSid();
//                    String name = value.getName();
//                    String address = value.getAddress();
//                    String detail = value.getDetail();
//                    String location = value.getLocation();
//                    String open_time = value.getOpen_hour();
//                    String price = value.getPrice();
//                    String uid = value.getUid();
//
//                    Shop shop = new Shop(sid, name, address, detail, location, open_time, price, uid);
//                    shops.add(shop);
//                }
//                setRecyclerView();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("Shop", "Failed to get database", databaseError.toException());
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    private void setRecyclerView() {
        FavoriteRecyclerAdapter recyclerAdapter = new FavoriteRecyclerAdapter(shops, activity);
        RecyclerView.LayoutManager recycle = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(recycle);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }
}

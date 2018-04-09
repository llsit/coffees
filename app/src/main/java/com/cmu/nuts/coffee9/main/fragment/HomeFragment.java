package com.cmu.nuts.coffee9.main.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private Activity activity;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();
        recyclerView = view.findViewById(R.id.home_recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.home_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getShopDatabase();
            }
        });
        getShopDatabase();
        return view;
    }

    private void getShopDatabase(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Shop.tag);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Shop> shops = new ArrayList<>();
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    Shop value = snapshot.getValue(Shop.class);
                    assert value != null;
                    shops.add(value);
                }
                setRecyclerView(shops);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Shop","Failed to get database", databaseError.toException());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setRecyclerView(List<Shop> shops){
        ShopRecyclerAdapter recyclerAdapter = new ShopRecyclerAdapter(shops, activity);
        RecyclerView.LayoutManager recycle = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(recycle);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }

}

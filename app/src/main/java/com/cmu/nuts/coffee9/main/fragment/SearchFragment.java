package com.cmu.nuts.coffee9.main.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

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

import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        searchView = view.findViewById(R.id.fragment_search_view);
        TextView addShop = view.findViewById(R.id.fragment_search_btn_add);
        activity = getActivity();
        recyclerView = view.findViewById(R.id.search_recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.search_swipe_refresh_layout);
        database = FirebaseDatabase.getInstance();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                databaseReference = database.getReference(Shop.tag);
                getShopDatabase();
            }
        });



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
                databaseReference.equalTo(query);
                getShopDatabase();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (shops != null && shops.size() > 0){
                    onSearch(shops, newText);
                }
                return false;
            }
        });
        return view;
    }

    private List<Shop> onSearch(List<Shop> list, String key){
        List<Shop> new_shop = new ArrayList<>();
        for (int i=1; i < list.size(); i++){
            try {
                if (list.get(i).getName().contains(key)){
                    new_shop.add(list.get(i));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        setRecyclerView(new_shop);
        return list;
    }

    private void getShopDatabase(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shops = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    Shop value = dataSnapshot1.getValue(Shop.class);
                    String sid = value.getSid();
                    String name = value.getName();
                    String address = value.getAddress();
                    String detail = value.getDetail();
                    String location = value.getLocation();
                    String open_time = value.getOpen_houre();
                    String price = value.getPrice();
                    String uid = value.getUid();

                    Shop shop = new Shop(sid, name, address, detail, location, open_time, price, uid);
                    shops.add(shop);
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

    private void setRecyclerView(List<Shop> list){
        ShopRecyclerAdapter recyclerAdapter = new ShopRecyclerAdapter(list, activity);
        RecyclerView.LayoutManager recycle = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(recycle);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }
}

package com.cmu.nuts.coffee9.main;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.adapter.ShopRecyclerAdapter;
import com.cmu.nuts.coffee9.main.fragment.FavoriteFragment;
import com.cmu.nuts.coffee9.main.fragment.HomeFragment;
import com.cmu.nuts.coffee9.main.fragment.NearByFragment;
import com.cmu.nuts.coffee9.main.fragment.ProfileFragment;
import com.cmu.nuts.coffee9.main.fragment.SearchFragment;
import com.cmu.nuts.coffee9.main.material.BottomNavigationViewHelper;
import com.cmu.nuts.coffee9.model.Shop;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class main extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    List<Shop> shops;
    RecyclerView recyclerView;
    private TextView mTitle;
    private FrameLayout fragmentContainer;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();

        BottomNavigationView mBottomNav = findViewById(R.id.buttom_nav);
        fragmentContainer = findViewById(R.id.myFragment);
        recyclerView = findViewById(R.id.main_recycler_view);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(Shop.tag);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shops = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    Shop value = dataSnapshot1.getValue(Shop.class);
                    Shop shop = new Shop();
                    String sid = value.getSid();
                    String name = value.getName();
                    shop.setSid(sid);
                    shop.setName(name);
                    shops.add(shop);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Shop","Failed to get database", databaseError.toException());
            }
        });

        HomeFragment home = new HomeFragment();
        FragmentManager manager_home = getSupportFragmentManager();
        FragmentTransaction hm = manager_home.beginTransaction();
        hm.replace(R.id.myFragment, home);
        hm.addToBackStack(null);
        hm.commit();


        ///////////////////////////////////////////////////////////////////////////////////////
        if (ab != null) {
            ab.setDisplayShowTitleEnabled(false);
        }
        mTitle = myToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.menu_home));
        BottomNavigationViewHelper.disableShiftMode(mBottomNav);
        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.fade_in_smooth);

        /////////////////////////////////////////////////////////////////////////////////////////
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // handle desired action here
                // One possibility of action is to replace the contents above the nav bar
                // return true if you want the item to be displayed as the selected item
                switch (item.getItemId()) {
                    case R.id.action_home:
                        mTitle.setText(getString(R.string.menu_home));
                        fragmentContainer.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        ShopRecyclerAdapter recyclerAdapter = new ShopRecyclerAdapter(shops, main.this);
                        RecyclerView.LayoutManager recyce = new LinearLayoutManager(main.this);
                        recyclerView.setLayoutManager(recyce);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(recyclerAdapter);
//                        HomeFragment home = new HomeFragment();
//                        FragmentManager manager_home = getSupportFragmentManager();
//                        FragmentTransaction hm = manager_home.beginTransaction();
//                        hm.setCustomAnimations(R.anim.slide_up_in_from_buttom, R.anim.slide_up_out);
//                        hm.replace(R.id.myFragment, home);
//                        hm.commit();
                        break;
                    case R.id.action_search:
                        mTitle.setText(getString(R.string.menu_search));
                        fragmentContainer.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        SearchFragment search = new SearchFragment();
                        FragmentManager manager_search = getSupportFragmentManager();
                        FragmentTransaction sh = manager_search.beginTransaction();
//                        sh.setCustomAnimations(R.anim.fade_in_smooth, R.anim.fade_out);
                        sh.replace(R.id.myFragment, search);
                        sh.commit();
                        break;
                    case R.id.action_nearby:
                        mTitle.setText(getString(R.string.menu_search_near));
                        fragmentContainer.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        NearByFragment nearby = new NearByFragment();
                        FragmentManager manager_nearby = getSupportFragmentManager();
                        FragmentTransaction nb = manager_nearby.beginTransaction();
                        nb.replace(R.id.myFragment, nearby);
                        nb.commit();
                        break;
                    case R.id.action_favorite:
                        mTitle.setText(getString(R.string.menu_favorite));
                        fragmentContainer.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        FavoriteFragment favorite = new FavoriteFragment();
                        FragmentManager manager_favorite = getSupportFragmentManager();
                        FragmentTransaction fv = manager_favorite.beginTransaction();
                        fv.replace(R.id.myFragment, favorite);
                        fv.commit();
                        break;
                    case R.id.action_profile:
                        mTitle.setText(getString(R.string.menu_profile));
                        fragmentContainer.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        ProfileFragment profile = new ProfileFragment();
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.replace(R.id.myFragment, profile);
                        ft.commit();
                        break;
                }

                return true;
            }
        });
    }

//    private int state = 0;
//    @Override
//    public void onBackPressed() {
//
////        switch (state){
////            case 0 : {
////                Toast.makeText(this, "Press back again to exit",Toast.LENGTH_SHORT).show();
////                state = 1;
////                Handler handler = new Handler();
////                Runnable run = new Runnable() {
////                    @Override
////                    public void run() {
////                        state = 0;
////                    }
////                };
////                handler.postDelayed(run, 2000);
////                break;
////            }
////            case 1 : {
////                finish();
////                break;
////            }
////        }
//    }
}


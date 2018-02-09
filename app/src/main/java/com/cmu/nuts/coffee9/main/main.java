package com.cmu.nuts.coffee9.main;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.fragment.FavoriteFragment;
import com.cmu.nuts.coffee9.main.fragment.HomeFragment;
import com.cmu.nuts.coffee9.main.fragment.NearByFragment;
import com.cmu.nuts.coffee9.main.fragment.ProfileFragment;
import com.cmu.nuts.coffee9.main.fragment.SearchFragment;
import com.cmu.nuts.coffee9.main.material.BottomNavigationViewHelper;


public class main extends AppCompatActivity {

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

        BottomNavigationView mBottomNav = findViewById(R.id.bottom_nav);
        fragmentContainer = findViewById(R.id.myFragment);

        HomeFragment home = new HomeFragment();
        FragmentManager manager_home = getSupportFragmentManager();
        FragmentTransaction hm = manager_home.beginTransaction();
        hm.replace(R.id.myFragment, home);
        hm.addToBackStack(null);
        hm.commit();

        if (ab != null) {
            ab.setDisplayShowTitleEnabled(false);
        }
        mTitle = myToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.menu_home));
        BottomNavigationViewHelper.disableShiftMode(mBottomNav);

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
                        HomeFragment home = new HomeFragment();
                        FragmentManager manager_home = getSupportFragmentManager();
                        FragmentTransaction hm = manager_home.beginTransaction();
                        hm.replace(R.id.myFragment, home);
                        hm.commit();
                        break;
                    case R.id.action_search:
                        mTitle.setText(getString(R.string.menu_search));
                        fragmentContainer.setVisibility(View.VISIBLE);
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
                        NearByFragment nearby = new NearByFragment();
                        FragmentManager manager_nearby = getSupportFragmentManager();
                        FragmentTransaction nb = manager_nearby.beginTransaction();
                        nb.replace(R.id.myFragment, nearby);
                        nb.commit();
                        break;
                    case R.id.action_favorite:
                        mTitle.setText(getString(R.string.menu_favorite));
                        fragmentContainer.setVisibility(View.VISIBLE);
                        FavoriteFragment favorite = new FavoriteFragment();
                        FragmentManager manager_favorite = getSupportFragmentManager();
                        FragmentTransaction fv = manager_favorite.beginTransaction();
                        fv.replace(R.id.myFragment, favorite);
                        fv.commit();
                        break;
                    case R.id.action_profile:
                        mTitle.setText(getString(R.string.menu_profile));
                        fragmentContainer.setVisibility(View.VISIBLE);
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
//        switch (state){
//            case 0 : {
//                Toast.makeText(this, "Press back again to exit",Toast.LENGTH_SHORT).show();
//                state = 1;
//                Handler handler = new Handler();
//                Runnable run = new Runnable() {
//                    @Override
//                    public void run() {
//                        state = 0;
//                    }
//                };
//                handler.postDelayed(run, 2000);
//                break;
//            }
//            case 1 : {
//                finish();
//                break;
//            }
//        }
//    }
}


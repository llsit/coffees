package com.example.nuts.coffee9;


import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;


public class main extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener,NearbyFragment.OnFragmentInteractionListener
    ,SearchFragment.OnFragmentInteractionListener,FavoriteFragment.OnFragmentInteractionListener,HomeFragment.OnFragmentInteractionListener{


    private TextView mTitle;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();

        BottomNavigationView mBottomNav = findViewById(R.id.buttom_nav);

        HomeFragment home = new HomeFragment();
        FragmentManager manager_home = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction hm = manager_home.beginTransaction();
        hm.replace(R.id.myFragment, home);
        hm.addToBackStack(null);
        hm.commit();

        //text.setText("Home");
        //ab.setTitle("Home");
        //myToolbar.setTitleMargin(450,1,1,1);
        if (ab != null) {
            ab.setDisplayShowTitleEnabled(false);
        }
        mTitle = myToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Home");
        BottomNavigationViewHelper.disableShiftMode(mBottomNav);

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // handle desired action here
                // One possibility of action is to replace the contents above the nav bar
                // return true if you want the item to be displayed as the selected item
                switch (item.getItemId()) {
                    case R.id.action_home:
                        mTitle.setText("Home");
                        HomeFragment home = new HomeFragment();
                        FragmentManager manager_home = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction hm = manager_home.beginTransaction();
                        hm.replace(R.id.myFragment, home);
                        hm.addToBackStack(null);
                        hm.commit();
                        break;
                    case R.id.action_search:
                        mTitle.setText("Search");
                        SearchFragment search = new SearchFragment();
                        FragmentManager manager_search = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction sh = manager_search.beginTransaction();
                        sh.replace(R.id.myFragment, search);
                        sh.addToBackStack(null);
                        sh.commit();
                        break;
                    case R.id.action_nearby:
                        mTitle.setText("Nearby");
                        NearbyFragment nearby = new NearbyFragment();
                        FragmentManager manager_nearby = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction nb = manager_nearby.beginTransaction();
                        nb.replace(R.id.myFragment, nearby);
                        nb.addToBackStack(null);
                        nb.commit();
                        break;
                    case R.id.action_favorite:
                        mTitle.setText("Favorite");
                        FavoriteFragment favorite = new FavoriteFragment();
                        FragmentManager manager_favorite = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fv = manager_favorite.beginTransaction();
                        fv.replace(R.id.myFragment, favorite);
                        fv.addToBackStack(null);
                        fv.commit();
                        break;
                    case R.id.action_profile:
                        mTitle.setText("Profile");
                        ProfileFragment profile = new ProfileFragment();
                        FragmentManager manager = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction ft = manager.beginTransaction();
                        ft.replace(R.id.myFragment, profile);
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}


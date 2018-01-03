package com.example.nuts.coffee9;


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
    private Toolbar myToolbar;
    private BottomNavigationView mBottomNav;
    private ActionBar ab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ab = getSupportActionBar();

        mBottomNav = findViewById(R.id.buttom_nav);

        HomeFragment home = new HomeFragment();
        FragmentManager manager_home = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction hm = manager_home.beginTransaction();
        hm.replace(R.id.myFragment, home);
        hm.addToBackStack(null);
        hm.commit();

        //text.setText("Home");
        //ab.setTitle("Home");
        //myToolbar.setTitleMargin(450,1,1,1);
        ab.setDisplayShowTitleEnabled(false);
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
                        //ab.setTitle("Home");
                        HomeFragment home = new HomeFragment();
                        FragmentManager manager_home = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction hm = manager_home.beginTransaction();
                        hm.replace(R.id.myFragment, home);
                        hm.addToBackStack(null);
                        hm.commit();
                        break;
                    case R.id.action_search:
                        //text.setText("Search");
                        //ab.setTitle("Search");
                        mTitle.setText("Search");
                        SearchFragment search = new SearchFragment();
                        FragmentManager manager_search = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction sh = manager_search.beginTransaction();
                        sh.replace(R.id.myFragment, search);
                        sh.addToBackStack(null);
                        sh.commit();
                        break;
                    case R.id.action_nearby:
                        //text.setText("Nearby");
                        //ab.setTitle("Nearby");
                        mTitle.setText("Nearby");
                        NearbyFragment nearby = new NearbyFragment();
                        FragmentManager manager_nearby = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction nb = manager_nearby.beginTransaction();
                        nb.replace(R.id.myFragment, nearby);
                        nb.addToBackStack(null);
                        nb.commit();
                        break;
                    case R.id.action_favorite:
                        //text.setText("Favorite");
                        //ab.setTitle("Favorite");
                        mTitle.setText("Favorite");
                        FavoriteFragment favorite = new FavoriteFragment();
                        FragmentManager manager_favorite = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fv = manager_favorite.beginTransaction();
                        fv.replace(R.id.myFragment, favorite);
                        fv.addToBackStack(null);
                        fv.commit();
                        break;
                    case R.id.action_profile:
                        //text.setText("Profile");
                        //ab.setTitle("Profile");
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


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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class main extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener,NearbyFragment.OnFragmentInteractionListener{

    private FirebaseAuth auth;
    private Button btn_logout;
    private TextView textView, text;
    private Toolbar myToolbar;
    private BottomNavigationView mBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        //btn_logout = findViewById(R.id.btn_logout);
        //textView = findViewById(R.id.textView);
        //text = findViewById(R.id.text);
        mBottomNav = findViewById(R.id.buttom_nav);

        auth = FirebaseAuth.getInstance();

        //FirebaseUser currentUser = auth.getCurrentUser();

        //textView.setText("Welcome " + currentUser.getEmail() + "ID = " + currentUser.getUid());

//        btn_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                auth.signOut();
//                Intent intent = new Intent(main.this, login.class);
//                startActivity(intent);
//                finish();
//
//            }
//        });

        //text.setText("Home");
        BottomNavigationViewHelper.disableShiftMode(mBottomNav);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // handle desired action here
                // One possibility of action is to replace the contents above the nav bar
                // return true if you want the item to be displayed as the selected item
                switch (item.getItemId()) {
                    case R.id.action_home:
                        //text.setText("Home");
                        //mBottomNav.setItemTextColor(ContextCompat.getColorStateList(main.this, R.color.colorAccent));
                        break;
                    case R.id.action_search:
                        //text.setText("Search");
                        //mBottomNav.setItemIconTintList(ContextCompat.getColorStateList(main.this, R.color.color_profile));
                        break;
                    case R.id.action_nearby:
                        //text.setText("Nearby");
                        NearbyFragment nearby = new NearbyFragment();
                        FragmentManager manager_nearby = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction nb = manager_nearby.beginTransaction();
                        nb.replace(R.id.myFragment, nearby);
                        nb.addToBackStack(null);
                        nb.commit();
                        break;
                    case R.id.action_favorite:
                        //text.setText("Favorite");
                        break;
                    case R.id.action_profile:
                        //text.setText("Profile");
                        //Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.myFragment);

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

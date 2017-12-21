package com.example.nuts.coffee9;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class main extends AppCompatActivity {

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
        text = findViewById(R.id.text);
        mBottomNav = findViewById(R.id.buttom_nav);

        /*auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();

        textView.setText("Welcome " + currentUser.getEmail() + "ID = " + currentUser.getUid());

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                auth.signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(main.this, login.class);
                startActivity(intent);
                finish();

            }
        });*/
        text.setText("Home");
        BottomNavigationViewHelper.disableShiftMode(mBottomNav);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // handle desired action here
                // One possibility of action is to replace the contents above the nav bar
                // return true if you want the item to be displayed as the selected item
                switch (item.getItemId()) {
                    case R.id.action_home:
                        text.setText("Home");
                        break;
                    case R.id.action_search:
                        text.setText("Search");
                        break;
                    case R.id.action_search_near:
                        text.setText("Nearby");
                        break;
                    case R.id.action_favorite:
                        text.setText("Favorite");
                        break;
                    case R.id.action_profile:
                        text.setText("Profile");
                        break;
                }
                return true;
            }
        });
    }



}

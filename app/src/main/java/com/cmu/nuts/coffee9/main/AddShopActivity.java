package com.cmu.nuts.coffee9.main;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.fragment.AddShopInFragment;

import java.util.Objects;


public class AddShopActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);

        Toolbar toolbar = findViewById(R.id.toolbarAddShop);
        toolbar.setTitle(getString(R.string.txt_add_coffee_shop));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        AddShopInFragment addShopInFragment = new AddShopInFragment();
        FragmentManager manager_home = getSupportFragmentManager();
        FragmentTransaction hm = manager_home.beginTransaction();
        hm.replace(R.id.myFragment2, addShopInFragment);
        hm.commit();
    }

    public void onBackPressed() {
        finish();
    }

}

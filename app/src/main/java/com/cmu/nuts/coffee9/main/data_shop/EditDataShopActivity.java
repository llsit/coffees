package com.cmu.nuts.coffee9.main.data_shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.fragment.EditShopFragment;

import java.util.Objects;

public class EditDataShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_shop);
        Toolbar toolbar = findViewById(R.id.toolbareditShop);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Edit Coffee Shop");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        String shop_id = intent.getStringExtra("shopid");

        Bundle bundle = new Bundle();
        bundle.putString("shop_id", shop_id);

        EditShopFragment editShopFragment = new EditShopFragment();
        editShopFragment.setArguments(bundle);
        FragmentManager manager_home = getSupportFragmentManager();
        FragmentTransaction hm = manager_home.beginTransaction();
        hm.replace(R.id.myFragment2, editShopFragment);
        hm.commit();
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_to_right);
    }

}

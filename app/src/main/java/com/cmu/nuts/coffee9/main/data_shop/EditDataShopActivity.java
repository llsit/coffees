package com.cmu.nuts.coffee9.main.data_shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.fragment.AddShopInFragment;
import com.cmu.nuts.coffee9.main.fragment.EditShopFragment;
import com.cmu.nuts.coffee9.model.Open_Hour;
import com.cmu.nuts.coffee9.model.Shop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EditDataShopActivity extends AppCompatActivity {

    private EditText nameshop;
    private EditText Addressshop;
    private EditText detail;
    private EditText location;
    private String rating;
    private Button btn_done;
    private RadioGroup radio_price;

    private String name;
    private String addressshop;
    private String Detail;
    private String authorID;
    private String locat;
    private String prices;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String shop_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_shop);
        Toolbar toolbar = findViewById(R.id.toolbar);

//        toolbar.setTitle(getString(R.string.txt_add_coffee_shop));
        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                onBackPressed();
//            }
//        });

        Intent intent = getIntent();
        shop_id = intent.getStringExtra("shopid");

        Bundle bundle = new Bundle();
        bundle.putString("shop_id", shop_id);

        EditShopFragment editShopFragment = new EditShopFragment();
        editShopFragment.setArguments(bundle);
        FragmentManager manager_home = getSupportFragmentManager();
        FragmentTransaction hm = manager_home.beginTransaction();
        hm.replace(R.id.myFragment2, editShopFragment);
        hm.commit();
    }


}

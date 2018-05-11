package com.cmu.nuts.coffee9.main.data_shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Shop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditDataShopActivity extends AppCompatActivity {

    private EditText nameshop;
    private EditText Addressshop;
    private EditText detail;
    private EditText location;
    private String rating;
    private Button btn_done;
    private RadioGroup radio_price;

    private String coffee_ID, name, addressshop, Detail, authorID, locat, prices;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String shop_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        shop_id = intent.getStringExtra("shopid");
        location = findViewById(R.id.location);
        nameshop = findViewById(R.id.edit_data_name_shop);
        Addressshop = findViewById(R.id.edit_data_address_shop);
        detail = findViewById(R.id.edit_data_detail);
//        rating = findViewById(R.id.edit_data_rating);
        radio_price = findViewById(R.id.edit_data_rdo_price);
        radio_price.check(R.id.rdo_min);
        btn_done = findViewById(R.id.edit_data_btn_done);

        getDataShop();
        editDataShop();
    }

    private void getDataShop() {
        mDatabase = FirebaseDatabase.getInstance().getReference(Shop.tag).child(shop_id);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot item : dataSnapshot.getChildren()){
                Shop shops = dataSnapshot.getValue(Shop.class);
                Toast.makeText(EditDataShopActivity.this, "id = " + shops.getSid(), Toast.LENGTH_SHORT).show();
                nameshop.setText(shops.getName());
                Addressshop.setText(shops.getAddress());
                detail.setText(shops.getDetail());
//                rating.setText(shops.getRating());
                radio_price.check(R.id.rdo_min);
                location.setText(shops.getLocation());
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void editDataShop() {

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameshop.getText().toString();
                addressshop = Addressshop.getText().toString();
                Detail = detail.getText().toString();
                authorID = user.getUid();
                locat = location.getText().toString();
//                rating = rating.getText().toString();
                rating = "12";
                switch (radio_price.getCheckedRadioButtonId()) {
                    case R.id.rdo_min:
                        prices = getString(R.string.txt_rang_0_100);
                        break;
                    case R.id.rdo_mid:
                        prices = getString(R.string.txt_rang_101_200);
                        break;
                    case R.id.rdo_max:
                        prices = getString(R.string.txt_rang_over_200);
                        break;
                }

                Shop shopData = new Shop(shop_id, name, addressshop, Detail, locat, rating, prices, authorID);
                mDatabase.child("coffee_shop").child(shop_id).setValue(shopData);

                Toast.makeText(EditDataShopActivity.this, "Edit Success", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}

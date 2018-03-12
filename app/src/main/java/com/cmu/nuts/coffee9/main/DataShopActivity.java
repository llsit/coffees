package com.cmu.nuts.coffee9.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;

import butterknife.BindView;
import butterknife.OnClick;

public class DataShopActivity extends AppCompatActivity {


    @BindView(R.id.data_shop_img_back) ImageView btn_back_data_shop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_data_shop_title);
        setSupportActionBar(toolbar);
    }


    @OnClick(R.id.data_shop_img_back) public void OnBack(){
        Toast.makeText(DataShopActivity.this, "Failed",
                Toast.LENGTH_SHORT).show();
        finish();
    }

}

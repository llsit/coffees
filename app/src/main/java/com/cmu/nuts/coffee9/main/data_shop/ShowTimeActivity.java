package com.cmu.nuts.coffee9.main.data_shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.Interface.AddTimeShopInterface;
import com.cmu.nuts.coffee9.main.adapter.AddTimeShopAdapter;
import com.cmu.nuts.coffee9.main.adapter.ShowTimeAdapter;
import com.cmu.nuts.coffee9.model.Open_Hour;

import java.util.ArrayList;
import java.util.Objects;

public class ShowTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_time);

        Toolbar toolbar = findViewById(R.id.toolbarShowTime);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Time Coffee Shop");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ArrayList<Open_Hour> open_hourArrayList = (ArrayList<Open_Hour>) getIntent().getExtras().getSerializable("openTimes");
        RecyclerView recyclerView_time = findViewById(R.id.recycler_time_list);

        ShowTimeAdapter showTimeAdapter = new ShowTimeAdapter(this, open_hourArrayList);
        RecyclerView.LayoutManager recycle = new LinearLayoutManager(this);
        recyclerView_time.setLayoutManager(recycle);
        recyclerView_time.setItemAnimator(new DefaultItemAnimator());
        recyclerView_time.setAdapter(showTimeAdapter);
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_to_right);
    }
}

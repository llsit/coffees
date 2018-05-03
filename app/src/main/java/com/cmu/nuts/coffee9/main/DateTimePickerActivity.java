package com.cmu.nuts.coffee9.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cmu.nuts.coffee9.R;

import java.util.ArrayList;

public class DateTimePickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ListView listView = findViewById(R.id.listViewAnimals);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ArrayList<String> date = new ArrayList<>();
            ArrayList<String> time = new ArrayList<>();
            time = bundle.getStringArrayList("arrlisttime");
            date = bundle.getStringArrayList("arrlistdate");
            if (time != null) {
                ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, date);
                listView.setAdapter(arrayAdapter);

                for (String a : time) {
                    System.out.println(a);
                }
            }
        }

        Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DateTimePickerActivity.this, addDateTimeActivity.class);
                startActivity(intent);
            }
        });

    }

}

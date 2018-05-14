package com.cmu.nuts.coffee9.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.cmu.nuts.coffee9.R;

import java.util.ArrayList;
import java.util.HashMap;

public class addDateTimeActivity extends AppCompatActivity {

    EditText open_hr, open_min, close_hr, close_min;
    CheckBox day1, day2, day3, day4, day5, day6, day7;
    Button add_time;
    HashMap<String, String> timess;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date_time);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        day1 = findViewById(R.id.day1);
        day2 = findViewById(R.id.day2);
        day3 = findViewById(R.id.day3);
        day4 = findViewById(R.id.day4);
        day5 = findViewById(R.id.day5);
        day6 = findViewById(R.id.day6);
        day7 = findViewById(R.id.day7);
        open_hr = findViewById(R.id.hr);
        open_min = findViewById(R.id.min);
        close_hr = findViewById(R.id.hr2);
        close_min = findViewById(R.id.min2);
        add_time = findViewById(R.id.add_time);
        final ArrayList<String> arrayTime = new ArrayList<>();
        final ArrayList<String> arrayDate = new ArrayList<String>();

        add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timess = new HashMap<>();
                StringBuilder result = new StringBuilder();
                if (day1.isChecked()) {
                    result.append("\nSunday");
                    arrayDate.add("Sunday");
                }
                if (day2.isChecked()) {
                    result.append("\nMonday");
                    arrayDate.add("Monday");
                }
                if (day3.isChecked()) {
                    result.append("\nTuesday");
                    arrayDate.add("Tuesday");
                }
                if (day4.isChecked()) {
                    result.append("\nWednesday");
                    arrayDate.add("Wednesday");
                }
                if (day5.isChecked()) {
                    result.append("\nThursday");
                    arrayDate.add("Thursday");
                }
                if (day6.isChecked()) {
                    result.append("\nFriday");
                    arrayDate.add("Friday");
                }
                if (day7.isChecked()) {
                    result.append("\nSaturday");
                    arrayDate.add("Saturday");
                }

                result.append(open_hr.getText()).append(":").append(open_min.getText()).append("\n");

                int open_hrs = Integer.parseInt(open_hr.getText().toString());
                int open_mins = Integer.parseInt(open_min.getText().toString());
                int close_hrs = Integer.parseInt(String.valueOf(close_hr.getText()));
                int close_mins = Integer.parseInt(String.valueOf(close_min.getText()));


                arrayTime.add(String.valueOf(open_hrs));
                arrayTime.add(String.valueOf(open_mins));
                arrayTime.add(String.valueOf(close_hrs));
                arrayTime.add(String.valueOf(close_mins));
                Intent intent = new Intent(addDateTimeActivity.this, DateTimePickerActivity.class);
                intent.putStringArrayListExtra("arrlisttime", arrayTime);
                intent.putStringArrayListExtra("arrlistdate", arrayDate);
                startActivity(intent);
            }
        });
    }
}

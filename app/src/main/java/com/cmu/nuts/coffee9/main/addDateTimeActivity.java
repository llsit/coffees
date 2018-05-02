package com.cmu.nuts.coffee9.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;

import java.util.ArrayList;

public class addDateTimeActivity extends AppCompatActivity {

    EditText open_hr, open_min, close_hr, close_min;
    CheckBox day1, day2, day3, day4, day5, day6, day7;
    Button add_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        day1 = findViewById(R.id.day1);
        day2 = findViewById(R.id.day2);
        day3 = findViewById(R.id.day3);
        day4 = findViewById(R.id.day4);
        day5 = findViewById(R.id.day5);
        day6 = findViewById(R.id.day6);
        day7 = findViewById(R.id.day7);
        add_time = findViewById(R.id.add_time);
        ArrayList arrayTime = new ArrayList();
        add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder result = new StringBuilder();
                if (day1.isChecked()) {
                    result.append("\nSunday");
                }
                if (day2.isChecked()) {
                    result.append("\nMonday");
                }
                if (day3.isChecked()) {
                    result.append("\nTuesday");
                }
                if (day4.isChecked()) {
                    result.append("\nWednesday");
                }
                if (day5.isChecked()) {
                    result.append("\nThursday");
                }
                if (day6.isChecked()) {
                    result.append("\nFriday");
                }
                if (day7.isChecked()) {
                    result.append("\nSaturday");
                }
                //Displaying the message on the toast
                Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

}

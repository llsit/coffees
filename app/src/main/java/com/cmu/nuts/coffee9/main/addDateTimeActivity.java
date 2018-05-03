package com.cmu.nuts.coffee9.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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

                int open_hrs = Integer.parseInt(String.valueOf(open_hr.getText()));
                int open_mins = Integer.parseInt(String.valueOf(open_min.getText()));
                int close_hrs = Integer.parseInt(String.valueOf(close_hr.getText()));
                int close_mins = Integer.parseInt(String.valueOf(close_min.getText()));

                if (open_hrs > 24 && open_hrs < 1) {
                    if (close_hrs > 24 && close_hrs < 1) {
                        close_hr.setError("Incorrect");
                        open_hr.setError("Incorrect");
                    } else {
                        open_hr.setError("Incorrect");
                    }
                    if (open_mins > 60 && open_mins < 0) {
                        open_min.setError("Incorrect");
                        open_hr.setError("Incorrect");
                    } else {
                        open_hr.setError("Incorrect");
                    }
                } else {
                    if (close_hrs > 24 && close_hrs < 1) {
                        close_hr.setError("Incorrect");
                    } else {
                        if (close_mins > 60 && close_mins < 0) {
                            close_hr.setError("Incorrect");
                            close_min.setError("Incorrect");
                        } else {
                            arrayTime.add(String.valueOf(open_hrs));
                            arrayTime.add(String.valueOf(open_mins));
                            arrayTime.add(String.valueOf(close_hrs));
                            arrayTime.add(String.valueOf(close_mins));
                        }
                    }
                }


                result.append(close_hr.getText()).append(":").append(close_min.getText());


                //Displaying the message on the toast
                for (int i = 0; i < arrayDate.size(); i++) {
                    System.out.println(arrayDate.get(i));
                }
                for (int i = 0; i < arrayTime.size(); i++) {
                    System.out.println(arrayTime.get(i));
                }

//                Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
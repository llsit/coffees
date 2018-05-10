package com.cmu.nuts.coffee9.main;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DateTimePickerActivity extends AppCompatActivity {

    ImageView back;
    Button add;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        listView = findViewById(R.id.listView);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add = findViewById(R.id.add);
        selectime();

    }

    private void showtime(ArrayList<String> arrayList) {
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }


    private void selectime() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(DateTimePickerActivity.this);
                LayoutInflater inflater = getLayoutInflater();

                @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_addtime, null);
                builder.setView(view);

                final StringBuffer result = new StringBuffer();
                final ArrayList<String> arrayList = new ArrayList<String>();

                final CheckBox day1 = view.findViewById(R.id.day1);
                final CheckBox day2 = view.findViewById(R.id.day2);
                final CheckBox day3 = view.findViewById(R.id.day3);
                final CheckBox day4 = view.findViewById(R.id.day4);
                final CheckBox day5 = view.findViewById(R.id.day5);
                final CheckBox day6 = view.findViewById(R.id.day6);
                final CheckBox day7 = view.findViewById(R.id.day7);
                final EditText hr = view.findViewById(R.id.hr);
                final EditText hr2 = view.findViewById(R.id.hr2);
                hr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(DateTimePickerActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                hr.setText(selectedHour + ":" + selectedMinute);
                                result.append("[").append(selectedHour).append(selectedMinute).append("}");
                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                    }
                });

                hr2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(DateTimePickerActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                hr2.setText(selectedHour + ":" + selectedMinute);
                                result.append("[").append(selectedHour).append(selectedMinute).append("]");
                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                    }
                });

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Check username password

                        if (day1.isChecked()) {
                            result.append("Sunday");
//                            arrayList.add("Sunday");
                        }
                        if (day2.isChecked()) {
                            result.append("Monday");
//                            arrayList.add("Monday");
                        }
                        if (day3.isChecked()) {
                            result.append("Tuesday");
//                            arrayList.add("Tuesday");
                        }
                        if (day4.isChecked()) {
                            result.append("Wednesday");
//                            arrayList.add("Wednesday");
                        }
                        if (day5.isChecked()) {
                            result.append("Thursday");
//                            arrayList.add("Thursday");
                        }
                        if (day6.isChecked()) {
                            result.append("Friday");
//                            arrayList.add("Friday");
                        }
                        if (day7.isChecked()) {
                            result.append("Saturday");
//                            arrayList.add("Saturday");
                        }
                        arrayList.add(String.valueOf(result));
                        showtime(arrayList);
                        Toast.makeText(DateTimePickerActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
            }
        });
    }

}

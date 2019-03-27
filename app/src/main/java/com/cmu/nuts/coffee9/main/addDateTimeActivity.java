package com.cmu.nuts.coffee9.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.Interface.AddTimeShopInterface;
import com.cmu.nuts.coffee9.main.adapter.AddTimeShopAdapter;
import com.cmu.nuts.coffee9.model.Open_Hour;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import static android.nfc.NfcAdapter.EXTRA_DATA;

public class addDateTimeActivity extends AppCompatActivity {
    public static final String EXTRA_DATA = "EXTRA_DATA";
    private ArrayList<Open_Hour> arrayList = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String shopId;
    private RecyclerView recyclerView;
    private AddTimeShopAdapter addTimeShopAdapter;
    private String dateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_date_time);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK, new Intent().putExtra(EXTRA_DATA, arrayList));
                finish();
            }
        });

        recyclerView = findViewById(R.id.time_shop);
        TextView clear_times = findViewById(R.id.clear_times);
        Intent i = getIntent();
        shopId = i.getStringExtra("sid");
        arrayList = (ArrayList<Open_Hour>) i.getExtras().getSerializable("arrayListTime");
        clear_times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                setAdapter();
            }
        });
        setAdapter();
        Button add_time = findViewById(R.id.add_time);
        add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View views) {
                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(Objects.requireNonNull(addDateTimeActivity.this));
                LayoutInflater inflater = getLayoutInflater();

                View view = inflater.inflate(R.layout.dialog_addtime, null);
                builder.setView(view);

                final RadioGroup radioGroup = view.findViewById(R.id.rdo_group);
                radioGroup.check(R.id.day1);

                final EditText hr = view.findViewById(R.id.hr);
                final EditText hr2 = view.findViewById(R.id.hr2);

                hr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(addDateTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                hr.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
//                                result.append("[").append(selectedHour).append(selectedMinute).append("]");
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
                        mTimePicker = new TimePickerDialog(addDateTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                hr2.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
//                                result.append("[").append(selectedHour).append(selectedMinute).append("]");
                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                    }
                });

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case R.id.day1:
                                dateStr = "Sunday";
                                break;
                            case R.id.day2:
                                dateStr = "Monday";
                                break;
                            case R.id.day3:
                                dateStr = "Tuesday";
                                break;
                            case R.id.day4:
                                dateStr = "Wednesday";
                                break;
                            case R.id.day5:
                                dateStr = "Thursday";
                                break;
                            case R.id.day6:
                                dateStr = "Friday";
                                break;
                            case R.id.day7:
                                dateStr = "Saturday";
                                break;
                            case R.id.day15:
                                dateStr = "Monday - Saturday";
                                break;
                            case R.id.day17:
                                dateStr = "Everyday";
                                break;
                            case R.id.day67:
                                dateStr = "Saturday - Sunday";
                                break;
                        }
                        String time_start = hr.getText().toString();
                        String time_end = hr2.getText().toString();
                        String key = database.getReference().push().getKey();
                        Open_Hour open_hour = new Open_Hour(shopId, key, dateStr, time_end, time_start);
                        arrayList.add(open_hour);
                        addTimeShopAdapter.notifyDataSetChanged();
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

    private void setAdapter() {
        addTimeShopAdapter = new AddTimeShopAdapter(this, arrayList, new AddTimeShopInterface() {
            @Override
            public void TimeOnRemove(View view, int postion) {
                arrayList.remove(postion);
                setAdapter();
            }
        });
        RecyclerView.LayoutManager recycle = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recycle);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(addTimeShopAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

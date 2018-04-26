package com.cmu.nuts.coffee9.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.cmu.nuts.coffee9.R;

import java.util.ArrayList;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        ArrayList<String> array = (ArrayList<String>) bundle.getStringArrayList("mylist");

        String pos = getIntent().getStringExtra("pos");

        TextView text = findViewById(R.id.test);


//        for (int i = 0; i < array.size(); i++) {
//            text.setText(text.getText() + array.get(i));
//        }

        assert array != null;
        text.setText(array.get(0));
    }

}

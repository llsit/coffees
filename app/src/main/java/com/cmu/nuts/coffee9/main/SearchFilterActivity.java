package com.cmu.nuts.coffee9.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;

public class SearchFilterActivity extends AppCompatActivity {

    private CheckBox price_max, price_mid, price_min;
    private Button searchs,cancel;
    StringBuffer result = new StringBuffer();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        price_max = findViewById(R.id.max);
        price_mid = findViewById(R.id.mid);
        price_min = findViewById(R.id.min);

        searchs = findViewById(R.id.search_filter);
        cancel = findViewById(R.id.cancel);

        if (price_max.isChecked()) {
            result.append(price_max.getText());
        } else if (price_mid.isChecked()) {
            result.append(price_mid.getText());
        } else if (price_min.isChecked()) {
            result.append(price_min.getText());
        }

        searchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchFilterActivity.this, result.toString(), Toast.LENGTH_LONG).show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.max:
                if (checked) ;
                    // Put some meat on the sandwich
                else
                    // Remove the meat
                    break;
            case R.id.mid:
                if (checked) ;
                    // Cheese me
                else
                    // I'm lactose intolerant
                    break;
                // TODO: Veggie sandwich
        }
    }

}

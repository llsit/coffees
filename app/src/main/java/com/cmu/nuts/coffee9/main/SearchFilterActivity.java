package com.cmu.nuts.coffee9.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.fragment.SearchFragment;

public class SearchFilterActivity extends AppCompatActivity {

    private CheckBox price_max, price_mid, price_min;
    private Button searchs, cancel;


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


        searchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer result = new StringBuffer();

                if (price_max.isChecked()) {
                    result.append("151-200:");
//                    arrayPrice.add("151-200");
                }
                if (price_mid.isChecked()) {
                    result.append("101-150:");
//                    arrayPrice.add("101-150");
                }
                if (price_min.isChecked()) {
                    result.append("0-100");
//                    arrayPrice.add("0-100");
                }
                SearchFragment fragobj = null;
                fragobj = new SearchFragment();
                Bundle bundle = new Bundle();
                bundle.putString("edttext", "From Activity");
                // set Fragmentclass Arguments

                fragobj.setArguments(bundle);
//                Toast.makeText(SearchFilterActivity.this, result, Toast.LENGTH_LONG).show();

                finish();
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

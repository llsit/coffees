package com.cmu.nuts.coffee9.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmu.nuts.coffee9.R;

import java.util.ArrayList;
import java.util.List;

public class DataShopActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    private ImageView back;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_shop);

        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        toolbar = findViewById(R.id.toolbar_data_shop);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        back = findViewById(R.id.data_shop_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String shop_ID = intent.getStringExtra("shopID");


        TextView text = findViewById(R.id.id_shopID);

        text.setText(shop_ID);

        String shopid = "123";
        Bundle bundle = new Bundle();
        bundle.putString("message", shopid);
        //set Fragmentclass Arguments
        DetailDataShopFragment fragobj = new DetailDataShopFragment();
        fragobj.setArguments(bundle);

//        Bundle bundle = new Bundle();
//        bundle.putString("shopID", "tab111111");
//        //set Fragmentclass Arguments
//        DetailDataShopFragment fragobj = new DetailDataShopFragment();
//        fragobj.setArguments(bundle);
        //Bundle bundle = getIntent().getBundleExtra("shopID");


    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new DetailDataShopFragment(), "Detail");
        adapter.addFragment(new ReviewDataShopFragment(), "Review");
        adapter.addFragment(new ImageDataShopFragment(), "Image");
        viewPager.setAdapter(adapter);
    }

    public class SectionsPageAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public SectionsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

}

package com.cmu.nuts.coffee9.main.data_shop;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.review.ReviewActivity;
import com.cmu.nuts.coffee9.utillity.ImageShare;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;
import io.github.kobakei.materialfabspeeddial.FabSpeedDialMenu;


public class DataShopActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    private ImageView backdata;

    private Toolbar toolbar;

    private String shop_ID;

    private MenuItem shared;

    private FloatingActionMenu fam;
    private FloatingActionButton fabReview, fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_shop);

        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.htab_viewpager);
        setupViewPager(mViewPager);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        backdata = findViewById(R.id.data_shop_back);

        backdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        shop_ID = intent.getStringExtra("shopID");


        final FabSpeedDial fab = findViewById(R.id.fab);
        FabSpeedDialMenu menu = new FabSpeedDialMenu(this);
        menu.add("Share Image").setIcon(R.drawable.ic_add_a_photo);
        menu.add("Review").setIcon(R.drawable.ic_edit_pen);
        fab.setMenu(menu);
        fab.addOnMenuItemClickListener(new FabSpeedDial.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(android.support.design.widget.FloatingActionButton fab, @Nullable TextView label, int itemId) {
                if (itemId == 1) {
                    ImagePicker.create(DataShopActivity.this).folderMode(true)
                            .toolbarFolderTitle("Folder").toolbarImageTitle("Tap to select")
                            .toolbarArrowColor(Color.WHITE).multi().limit(10)
                            .showCamera(true).imageDirectory("Camera").enableLog(true)
                            .start();
                } else if (itemId == 2) {
                    Intent intent = new Intent(DataShopActivity.this, ReviewActivity.class);
                    intent.putExtra("shopID", shop_ID);
                    startActivity(intent);
                }
//                Toast.makeText(DataShopActivity.this, "Click: " + itemId, Toast.LENGTH_SHORT).show();
            }

        });


    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            upload_photo(images);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upload_photo(List<Image> images) {

//        TextView text_view = findViewById(R.id.text_view);
        if (images == null) return;

//        StringBuilder stringBuffer = new StringBuilder();
        ImageShare imageManager = new ImageShare(DataShopActivity.class,shop_ID);
        for (int i = 0, l = images.size(); i < l; i++) {
//            stringBuffer.append(images.get(i).getPath());

            imageManager.uploadImage(shop_ID, Uri.fromFile(new File(images.get(i).getPath())));

        }
//        text_view.setText(stringBuffer.toString());

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

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        SectionsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new DetailDataShopFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("shop_ID", shop_ID);
                    fragment.setArguments(bundle);
                    break;
                case 1:
                    fragment = new ReviewDataShopFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("shop_ID", shop_ID);
                    fragment.setArguments(bundle2);
                    break;
                case 2:
                    fragment = new ImageDataShopFragment();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("shop_ID", shop_ID);
                    fragment.setArguments(bundle3);
                    break;
                default:
                    fragment = null;
                    break;
            }
            return fragment;
            //return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }


}

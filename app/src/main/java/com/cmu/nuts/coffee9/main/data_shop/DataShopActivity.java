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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.EditDataShopActivity;
import com.cmu.nuts.coffee9.main.review.ReviewActivity;
import com.cmu.nuts.coffee9.model.Share;
import com.cmu.nuts.coffee9.model.Shop;
import com.cmu.nuts.coffee9.utillity.ImageShare;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private String shop_ID;

    private MenuItem edit, del,share;

    private FirebaseUser auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_shop);

        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.htab_viewpager);
        setupViewPager(mViewPager);

        auth = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        shop_ID = intent.getStringExtra("shopID");

        final ImageView imageView = findViewById(R.id.image_header);
        DatabaseReference sDatabase = FirebaseDatabase.getInstance().getReference(Share.tag).child(shop_ID);
        sDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = null;
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Share shares = item.getValue(Share.class);
                    url = String.valueOf(Uri.parse(shares.getImg_url()));

                }
                Glide
                        .with(DataShopActivity.this)
                        .load(url)
                        .into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
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
            }

        });


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

        if (images == null) return;

        ImageShare imageManager = new ImageShare(DataShopActivity.class, shop_ID);
        for (int i = 0, l = images.size(); i < l; i++) {
            imageManager.uploadImage(shop_ID, Uri.fromFile(new File(images.get(i).getPath())));
        }
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
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting_data_shop, menu);
        edit = menu.findItem(R.id.edit_data_Shop);
        del = menu.findItem(R.id.delete_data_shop);
        share = menu.findItem(R.id.share);
        share.setVisible(true);

        DatabaseReference eDatabase = FirebaseDatabase.getInstance().getReference(Shop.tag).child(shop_ID);
        eDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Shop shops = dataSnapshot.getValue(Shop.class);
                    assert shops != null;
                    if (shops.getUid().equals(auth.getUid())) {
                        edit.setVisible(true);
                        del.setVisible(true);
                    } else {
                        edit.setVisible(false);
                        del.setVisible(false);
                    }
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.edit_data_Shop:
                // Code you want run when activity is clicked
                Intent intent = new Intent(DataShopActivity.this, EditDataShopActivity.class);
                intent.putExtra("shopid",shop_ID);
                startActivity(intent);
                return true;
            case R.id.delete_data_shop:
                // Code you want run when activity is clicked
                DatabaseReference delDatabase = FirebaseDatabase.getInstance().getReference(Shop.tag).child(shop_ID);
                delDatabase.child(shop_ID).removeValue();
                finish();
                return true;

            case R.id.share:
                // Code you want run when activity is clicked
                Toast.makeText(DataShopActivity.this, "done", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

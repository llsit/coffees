package com.cmu.nuts.coffee9.main.data_shop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.cmu.nuts.coffee9.main.review.ReviewActivity;
import com.cmu.nuts.coffee9.model.Open_Hour;
import com.cmu.nuts.coffee9.model.Review;
import com.cmu.nuts.coffee9.model.Share;
import com.cmu.nuts.coffee9.model.Shop;
import com.cmu.nuts.coffee9.utillity.ImageShare;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
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
    public static final String EXTRA_DATA = "EXTRA_DATA";

    private String shop_ID;

    private MenuItem edit;
    private MenuItem del;

    private FirebaseUser auth;
    ShareButton shareButton;
    ViewPager mViewPager;
    String name;
    String detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_shop);

        Log.d(TAG, "onCreate: Starting.");

//        SectionsPageAdapter mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        // Finding the facebook share button
        shareButton = findViewById(R.id.button);
        // Sharing the content to facebook

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.htab_viewpager);
        setupViewPager(mViewPager);

        auth = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        shop_ID = intent.getStringExtra("shopID");

        final ImageView imageView = findViewById(R.id.htab_image);
        DatabaseReference sDatabase = FirebaseDatabase.getInstance().getReference(Share.tag).child(shop_ID);
        sDatabase.limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = null;
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Share shares = item.getValue(Share.class);
                    assert shares != null;
                    url = String.valueOf(Uri.parse(shares.getImg_url()));
                }
                if (url != null) {
                    Glide
                            .with(DataShopActivity.this)
                            .load(url)
                            .into(imageView);
                }
//                tabs.setBackgroundColor(Integer.parseInt("#FFFFFF"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toolbar toolbar = findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        TabLayout tabLayout = findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        ImageView backdata = findViewById(R.id.data_shop_back);
        backdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_out_to_right);
            }
        });

        final FabSpeedDial fab = findViewById(R.id.fab);
        FabSpeedDialMenu menu = new FabSpeedDialMenu(this);
        menu.add(getResources().getString(R.string.txt_shared_image)).setIcon(R.drawable.ic_add_a_photo);
        menu.add(getResources().getString(R.string.txt_review)).setIcon(R.drawable.ic_edit_pen);
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
                    overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out);
                }
            }
        });
    }

    private void ShareDataShop() {
        DatabaseReference shDatabase = FirebaseDatabase.getInstance().getReference(Shop.tag).child(shop_ID);
        shDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Shop shops = dataSnapshot.getValue(Shop.class);
                assert shops != null;
                name = shops.getName();
                detail = shops.getDetail();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ShareLinkContent content = new ShareLinkContent.Builder()
                // Setting the title that will be shared
                .setContentTitle(name)
                // Setting the description that will be shared
                .setContentDescription(detail)
                // Setting the URL that will be shared
                .setContentUrl(Uri.parse("http://www.cs.science.cmu.ac.th/"))
                // Setting the image that will be shared
                .setImageUrl(Uri.parse("https://cdn-images-1.medium.com/fit/t/800/240/1*jZ3a6rYqrslI83KJFhdvFg.jpeg"))
                .build();
        shareButton.setShareContent(content);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            upload_photo(images);
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            System.out.println("delete");
        }
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
        adapter.addFragment(new DetailDataShopFragment(), getResources().getString(R.string.txt_detail));
        adapter.addFragment(new ReviewDataShopFragment(), getResources().getString(R.string.txt_review));
        adapter.addFragment(new ImageDataShopFragment(), getResources().getString(R.string.txt_image));
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
        MenuItem share = menu.findItem(R.id.share);
        share.setVisible(true);

        DatabaseReference eDatabase = FirebaseDatabase.getInstance().getReference(Shop.tag).child(shop_ID);
        eDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Shop shops = dataSnapshot.getValue(Shop.class);
                    assert shops != null;
                    if (shops.getUid().equals(auth.getUid())) {
                        edit.setVisible(true);
                        del.setVisible(true);
                    } else {
                        edit.setVisible(false);
                        del.setVisible(false);
                    }
                }
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
                intent.putExtra("shopid", shop_ID);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out);
                return true;
            case R.id.delete_data_shop:
                // Code you want run when activity is clicked
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this);
                builder.setMessage("คุณต้องการจะลบจริงหรือไม่?");
                builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteShop();
                    }
                });
                builder.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                    }
                });
                builder.show();

                return true;
            case R.id.share:
                // Code you want run when activity is clicked
                ShareDataShop();
                shareButton.performClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteShop() {
        DatabaseReference delDatabase = FirebaseDatabase.getInstance().getReference(Shop.tag);
        DatabaseReference RdelDatabase = FirebaseDatabase.getInstance().getReference(Review.tag);
        DatabaseReference SdelDatabase = FirebaseDatabase.getInstance().getReference(Share.tag);

        delDatabase.child(shop_ID).removeValue();
        SdelDatabase.child(shop_ID).removeValue();
        RdelDatabase.child(shop_ID).removeValue();
        Toast.makeText(this, "Delete Success", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, new Intent().putExtra(EXTRA_DATA, 1));
        finish();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupViewPager(mViewPager);
    }
}

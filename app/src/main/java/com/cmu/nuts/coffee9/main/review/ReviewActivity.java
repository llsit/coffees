package com.cmu.nuts.coffee9.main.review;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Review;
import com.cmu.nuts.coffee9.utillity.ImageReview;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewActivity extends AppCompatActivity {

    private MenuItem post;
    private ImageView back, imageView;
    private TextView descript;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ImageButton add_a_photo;

    private String uid, sid, detail, img_url, datetime, star;
    private String rid = mDatabase.push().getKey();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        Toolbar myToolbar = findViewById(R.id.toolbar_review);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        sid = intent.getStringExtra("shopID");
        rating();
        addImage();
    }

    private void addImage() {
        add_a_photo = findViewById(R.id.review_upload_image);
        add_a_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(ReviewActivity.this).folderMode(true)
                        .toolbarFolderTitle("Folder").toolbarImageTitle("Tap to select")
                        .toolbarArrowColor(Color.WHITE).multi().limit(10)
                        .showCamera(true).imageDirectory("Camera").enableLog(true)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
//            Image imageFile = ImagePicker.getFirstImageOrNull(data);
//            if (imageFile.getPath() != null) {
//                ImageReview imageManager = new ImageReview(ReviewActivity.class,rid);
//                imageManager.uploadImage(rid,Uri.fromFile(new File(imageFile.getPath())));
//            }
            upload_photo(images);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upload_photo(List<Image> images) {
        imageView = findViewById(R.id.show_photo);
        TextView text_view = findViewById(R.id.text_view);
        if (images == null) return;

        StringBuilder stringBuffer = new StringBuilder();
        ImageReview imageManager = new ImageReview(ReviewActivity.class, rid);
        for (int i = 0, l = images.size(); i < l; i++) {
            stringBuffer.append(images.get(i).getPath());

            imageManager.uploadImage(rid, Uri.fromFile(new File(images.get(i).getPath())));

        }
        text_view.setText(stringBuffer.toString());
//        Picasso.get()
//                .load(stringBuffer.toString())
//                .resize(50, 50)
//                .centerCrop()
//                .into(imageView);
    }


    @OnClick(R.id.review_back)
    public void onBack() {
        finish();
    }

    private void rating() {
        RatingBar mRatingBar = findViewById(R.id.stars_rating);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        star = "1";
                        break;
                    case 2:
                        star = "2";
                        break;
                    case 3:
                        star = "3";
                        break;
                    case 4:
                        star = "4";
                        break;
                    case 5:
                        star = "5";
                        break;
                    default:
                        star = "0";
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_review, menu);
        post = menu.findItem(R.id.post);
        post.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.post:
                addReview();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addReview() {
        descript = findViewById(R.id.edt_name_des);
        if (descript.getText().length() > 10) {
            datetime = DateFormat.getDateTimeInstance().format(new Date());
            uid = FirebaseAuth.getInstance().getUid();
            detail = descript.getText().toString();
            img_url = "null";
            Review review = new Review(rid, uid, sid, detail, img_url, datetime, star);
            mDatabase.child(Review.tag).child(sid).child(rid).setValue(review);
            Toast.makeText(this, "Your Review is now published" + sid, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            descript.setError("Your review is too shot");
        }
    }

}

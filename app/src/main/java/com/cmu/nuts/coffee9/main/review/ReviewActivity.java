package com.cmu.nuts.coffee9.main.review;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewActivity extends AppCompatActivity {

    private MenuItem post;
    private ImageView back;
    private TextView descript;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    String rid, uid, sid, detail, img_url, datetime,star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        Toolbar myToolbar = findViewById(R.id.toolbar_review);
        setSupportActionBar(myToolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        sid = intent.getStringExtra("shopID");
//        TextView usid = findViewById(R.id.sid);
//        if (sid != null) {
//            usid.setText(sid);
//        }
        rating();


    }

    @OnClick(R.id.review_back) public void onBack(){
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
        if (descript.getText().length() > 10){
            datetime = DateFormat.getDateTimeInstance().format(new Date());
            uid = FirebaseAuth.getInstance().getUid();
            detail = descript.getText().toString();
            rid = mDatabase.push().getKey();
            img_url = "null";
            Review review = new Review(rid, uid, sid, detail, img_url, datetime, star);
            mDatabase.child("review").child(sid).child(rid).setValue(review);
            Toast.makeText(this, "Your Review is now published" + sid, Toast.LENGTH_SHORT).show();
            finish();
        } else { descript.setError("Your review is too shot"); }
    }

}

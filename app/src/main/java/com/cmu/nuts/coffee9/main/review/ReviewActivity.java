package com.cmu.nuts.coffee9.main.review;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

        Toolbar mytoolbar = findViewById(R.id.toolbar_review);
        setSupportActionBar(mytoolbar);

        back = findViewById(R.id.review_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        rating();


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
                // Code you want run when activity is clicked
//                Intent intent = new Intent(Review.this, PreferencesActivity.class);
//                startActivity(intent);
                //Toast.makeText(this, "Add Review Success", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addReview() {

        descript = findViewById(R.id.edt_name_des);
        datetime = DateFormat.getDateTimeInstance().format(new Date());
        uid = FirebaseAuth.getInstance().getUid();
        detail = descript.getText().toString();
        rid = mDatabase.push().getKey();
        img_url = "null";
        Intent intent = getIntent();
        sid = intent.getParcelableExtra("shopID");


        Review review = new Review(rid, uid, sid, detail, img_url, datetime,star);
        mDatabase.child("review").child(rid).setValue(review);

        Toast.makeText(this, "Add Review Success", Toast.LENGTH_SHORT).show();

        finish();
    }

}

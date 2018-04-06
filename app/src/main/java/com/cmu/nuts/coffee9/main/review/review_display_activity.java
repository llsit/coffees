package com.cmu.nuts.coffee9.main.review;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class review_display_activity extends AppCompatActivity {

    private String shop_ID;
    private String review_ID;

    private TextView reviewer;
    private TextView datetime;
    private TextView description;
    private RatingBar star;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_display_activity);
        Toolbar toolbar = findViewById(R.id.toolbar_display_review);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        shop_ID = intent.getStringExtra("shopID");
        review_ID = intent.getStringExtra("reviewID");

        reviewer = findViewById(R.id.display_review_name_reviewer);
        datetime = findViewById(R.id.display_review_datetime);
        description = findViewById(R.id.display_review_description);
        star = findViewById(R.id.display_review_ratingBar);

//        reviewer.setText(shop_ID);
//        datetime.setText(review_ID);


        getDataReview();
    }

    private void getDataReview() {
        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference(Review.tag).child(shop_ID).child(review_ID);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Review review = dataSnapshot.getValue(Review.class);
//                reviewer.setText(review.);
                assert review != null;
                datetime.setText(review.getDatetime());
//                description.setText(review.getDetail());
//                star.setRating(Float.parseFloat(review.getStar()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Review Data", "Failed to get database", databaseError.toException());
            }
        });

    }

}

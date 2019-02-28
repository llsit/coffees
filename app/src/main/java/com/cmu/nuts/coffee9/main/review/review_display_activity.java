package com.cmu.nuts.coffee9.main.review;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.adapter.CommentRecyclerAdapter;
import com.cmu.nuts.coffee9.main.adapter.DataReviewRecyclerViewAdapter;
import com.cmu.nuts.coffee9.main.adapter.ImageGridAdapter;
import com.cmu.nuts.coffee9.main.fragment.ReviewRecyclerviewDisplay;
import com.cmu.nuts.coffee9.model.Comment;
import com.cmu.nuts.coffee9.model.Member;
import com.cmu.nuts.coffee9.model.Review;
import com.cmu.nuts.coffee9.model.Review_Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class review_display_activity extends AppCompatActivity {

    public static String shop_ID;
    public static String review_ID;
    private DatabaseReference mDatabase;
    //comment
    private EditText add_comment;
    private ImageButton send;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_display_activity);
        Toolbar toolbar = findViewById(R.id.toolbar_display_review);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        review_ID = intent.getStringExtra("reviewID");
        shop_ID = intent.getStringExtra("shopID");

        //review
        ImageView review_display_back = findViewById(R.id.review_display_back);
        review_display_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add_comment = findViewById(R.id.add_comment);
        send = findViewById(R.id.send);

        getReview();
        comment();

    }

    private void getReview() {
        ReviewRecyclerviewDisplay reviewRecyclerviewDisplay = new ReviewRecyclerviewDisplay();
        FragmentManager manager_home = getSupportFragmentManager();
        FragmentTransaction hm = manager_home.beginTransaction();
        hm.replace(R.id.myFragment, reviewRecyclerviewDisplay);
        hm.commit();
    }

    private void comment() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_comment.getText().length() > 3) {
                    String datetime = DateFormat.getDateTimeInstance().format(new Date());
                    String uid = FirebaseAuth.getInstance().getUid();
                    String detail = add_comment.getText().toString();
                    String cid = mDatabase.push().getKey();
                    String rid = shop_ID;

                    Comment comment = new Comment(cid, uid, rid, detail, datetime);
                    mDatabase.child(Comment.tag).child(rid).child(cid).setValue(comment);
                    Toast.makeText(review_display_activity.this, "Comment Complete", Toast.LENGTH_SHORT).show();
                    add_comment.setText("");
                    getReview();
                } else {
                    add_comment.setError("Your comment is too shot");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
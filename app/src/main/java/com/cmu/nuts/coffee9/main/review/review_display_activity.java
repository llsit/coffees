package com.cmu.nuts.coffee9.main.review;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.cmu.nuts.coffee9.model.Comment;
import com.cmu.nuts.coffee9.model.Member;
import com.cmu.nuts.coffee9.model.Review;
import com.cmu.nuts.coffee9.model.Review_Image;
import com.google.firebase.auth.FirebaseAuth;
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

    private String shop_ID;
    private String review_ID;
    private DatabaseReference mDatabase;
    //review
    private ImageView review_display_back;
    //comment
    private EditText add_comment;
    private ImageButton send;
    //display comment
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView RecyclerView;
//    private TextView data_shop_message;

    private ArrayList<Comment> commentArrayList = new ArrayList<>();
    private ArrayList<Review> reviewArrayList = new ArrayList<>();
    private ArrayList<String> arrayList = new ArrayList<>();

    private int status = 1;

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

        review_display_back = findViewById(R.id.review_display_back);

//        gridview = findViewById(R.id.review_shop_gridview);
//        gridview.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return event.getAction() == MotionEvent.ACTION_MOVE;
//            }
//
//        });
        review_display_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final BottomNavigationView BottomView = findViewById(R.id.bottom_nav_view);
//        BottomView.setVisibility(View.GONE);
//        ImageView comments = findViewById(R.id.comment_review);
//        comments.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (BottomView.isShown()) {
//                    BottomView.setVisibility(View.GONE);
//                } else {
//                    BottomView.setVisibility(View.VISIBLE);
//                }
//
//            }
//        });
        swipeRefreshLayout = findViewById(R.id.comment_swipe_refresh_layout);
        RecyclerView = findViewById(R.id.recycler_view);
//        data_shop_message = findViewById(R.id.comment);
        add_comment = findViewById(R.id.add_comment);
        send = findViewById(R.id.send);
        getDataReview();
//        comment();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentArrayList.clear();
                reviewArrayList.clear();
                arrayList.clear();
                getDataReview();
            }
        });

    }


    private void setRecyclerView(ArrayList<Review> reviewArrayList, ArrayList<Comment> commentArrayList, ArrayList<String> arrayList) {
        DataReviewRecyclerViewAdapter dataReviewRecyclerViewAdapter = new DataReviewRecyclerViewAdapter(reviewArrayList, commentArrayList, arrayList, this);
        RecyclerView.LayoutManager recycle = new LinearLayoutManager(review_display_activity.this);
        RecyclerView.setLayoutManager(recycle);
        RecyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.setAdapter(dataReviewRecyclerViewAdapter);
        RecyclerView.setVisibility(View.VISIBLE);
//        data_shop_message.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
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
                } else {
                    add_comment.setError("Your comment is too shot");
                }
            }
        });
    }

    private void getDataReview() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Review.tag).child(review_ID).child(shop_ID);

        final DatabaseReference imgDatabase = FirebaseDatabase.getInstance().getReference(Review_Image.tag);
        final DatabaseReference Commentdatabase = FirebaseDatabase.getInstance().getReference(Comment.tag);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Review review = dataSnapshot.getValue(Review.class);
                if (review != null) {
                    reviewArrayList.add(review);
                    Commentdatabase.child(review.getRid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Comment value = snapshot.getValue(Comment.class);
                                if (value != null && value.getCid() != null) {
                                    commentArrayList.add(new Comment(value.getCid(),
                                            value.getUid(),
                                            value.getRid(),
                                            value.getDetail(),
                                            value.getDatetime()
                                    ));
                                }
                            }
                            if (commentArrayList.size() == 0) {
                                RecyclerView.setVisibility(View.GONE);
                                swipeRefreshLayout.setRefreshing(false);
//                    data_shop_message.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Review Data", "Failed to get database", databaseError.toException());
                        }
                    });
                    imgDatabase.child(review.getRid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                Review_Image ri = item.getValue(Review_Image.class);
                                if (ri != null) {
                                    arrayList.add(ri.getImage_url());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Review Image", "Failed to get database", databaseError.toException());
                        }
                    });

                    setRecyclerView(reviewArrayList, commentArrayList, arrayList);
                } else {
                    Toast.makeText(review_display_activity.this, "NuLL NULL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Review Data", "Failed to get database", databaseError.toException());
            }
        });
    }

}
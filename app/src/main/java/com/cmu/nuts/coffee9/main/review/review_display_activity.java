package com.cmu.nuts.coffee9.main.review;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.adapter.CommentRecyclerAdapter;
import com.cmu.nuts.coffee9.model.Comment;
import com.cmu.nuts.coffee9.model.Member;
import com.cmu.nuts.coffee9.model.Review;
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

public class review_display_activity extends AppCompatActivity {

    private String shop_ID;
    private String review_ID;
    private DatabaseReference mDatabase;
    //review
    private TextView reviewer;
    private TextView datetime;
    private TextView description;
    private RatingBar star;
    private ImageView review_image;
    //comment
    private EditText add_comment;
    private ImageButton send;
    //display comment
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView RecyclerView;
    private TextView data_shop_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_display_activity);
        Toolbar toolbar = findViewById(R.id.toolbar_display_review);
        setSupportActionBar(toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        review_ID = intent.getStringExtra("reviewID");
        shop_ID = intent.getStringExtra("shopID");

        reviewer = findViewById(R.id.display_review_name_reviewer);
        datetime = findViewById(R.id.display_review_datetime);
        description = findViewById(R.id.display_review_description);
        star = findViewById(R.id.display_review_ratingBar);
        review_image = findViewById(R.id.display_review_image);
        getDataReview();
        comment();
        displayComment();
    }

    private void displayComment() {
        swipeRefreshLayout = findViewById(R.id.comment_swipe_refresh_layout);
        RecyclerView = findViewById(R.id.comment_recycler_view);
        data_shop_message = findViewById(R.id.comment);
        getCommentDatabase();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCommentDatabase();
            }
        });
    }

    private void getCommentDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Comment.tag).child(shop_ID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Comment> comments = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment value = snapshot.getValue(Comment.class);
                    if (value != null && value.getCid() != null) {
                        comments.add(new Comment(value.getCid(),
                                value.getUid(),
                                value.getRid(),
                                value.getDetail(),
                                value.getDatetime()
                        ));
                    }
                }
                if (comments.size() > 0) {
                    setRecyclerView(comments);
                } else {
                    RecyclerView.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    data_shop_message.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Review", "Failed to get database", databaseError.toException());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setRecyclerView(List<Comment> comments) {
        CommentRecyclerAdapter recyclerAdapter = new CommentRecyclerAdapter(comments, review_display_activity.this);
        RecyclerView.LayoutManager recycle = new LinearLayoutManager(review_display_activity.this);
        RecyclerView.setLayoutManager(recycle);
        RecyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.setAdapter(recyclerAdapter);
        RecyclerView.setVisibility(View.VISIBLE);
        data_shop_message.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }


    private void comment() {
        add_comment = findViewById(R.id.add_comment);
        send = findViewById(R.id.send);

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
                    hideSoftKeyboard(review_display_activity.this);
                    add_comment.setText("");
                } else {
                    add_comment.setError("Your comment is too shot");
                }
            }
        });
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void getDataReview() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Review.tag).child(review_ID).child(shop_ID);
        final DatabaseReference uDatabase = FirebaseDatabase.getInstance().getReference(Member.tag);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Review review = dataSnapshot.getValue(Review.class);
                if (review != null) {
                    datetime.setText(review.getDatetime());
                    description.setText(review.getDetail());
                    star.setRating(Float.parseFloat(review.getStar()));
                    uDatabase.child(review.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Member mem = dataSnapshot.getValue(Member.class);
                            if (mem != null) {
                                reviewer.setText(mem.getName());
                                Picasso.get()
                                        .load(mem.getPhotoUrl())
                                        .placeholder(R.drawable.img_user)
                                        .error(R.drawable.img_user)
                                        .resize(200,200)
                                        .centerInside()
                                        .into(review_image);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Review Data", "Failed to get database", databaseError.toException());
                        }
                    });
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

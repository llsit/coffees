package com.cmu.nuts.coffee9.main.review;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.adapter.CommentRecyclerAdapter;
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
    private ImageView image_review;
    private ImageView review_display_back;
    //comment
    private EditText add_comment;
    private ImageButton send;
    //display comment
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView RecyclerView;
    private TextView data_shop_message;

    private ListView listView;
    private int status = 0;

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
        image_review = findViewById(R.id.display_image_review);
        review_display_back = findViewById(R.id.review_display_back);
        listView = findViewById(R.id.list_image);
        review_display_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                    add_comment.setText("");
                } else {
                    add_comment.setError("Your comment is too shot");
                }
            }
        });
    }

    private void getDataReview() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Review.tag).child(review_ID).child(shop_ID);
        final DatabaseReference uDatabase = FirebaseDatabase.getInstance().getReference(Member.tag);
        final DatabaseReference imgDatabase = FirebaseDatabase.getInstance().getReference(Review_Image.tag);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Review review = dataSnapshot.getValue(Review.class);
                if (review != null) {
                    datetime.setText(review.getDatetime());
                    description.setText(review.getDetail());
                    star.setRating(Float.parseFloat(review.getStar()));
                    imgDatabase.child(review.getRid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<String> arrayList = new ArrayList<>();
                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                Review_Image ri = item.getValue(Review_Image.class);
                                if (ri != null) {
//                                    Picasso.get()
//                                            .load(ri.getImage_url())
//                                            .resize(200,200)
//                                            .centerInside()
//                                            .into(image_review);
//                                    Toast.makeText(review_display_activity.this, ri.getImgid(), Toast.LENGTH_SHORT).show();
                                    arrayList.add(ri.getImage_url());
                                }
                            }
                            setAdapter(listView, arrayList);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Review Image", "Failed to get database", databaseError.toException());
                        }
                    });
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
                                        .resize(200, 200)
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

    private void setAdapter(ListView listView, ArrayList<String> arrayList) {
        Toast.makeText(this, "Refreshing . .",
                Toast.LENGTH_LONG).show();
        listView.setAdapter(new ImageGridAdapter(this, arrayList, status));
    }

}
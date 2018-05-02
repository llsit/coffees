package com.cmu.nuts.coffee9.main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class reviewDisplayFragment extends Fragment {

    //review
    private TextView reviewer;
    private TextView datetime;
    private TextView description;
    private RatingBar star;
    private ImageView review_image;
    private ImageView image_review;
    private DatabaseReference mDatabase;
    //display comment
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView RecyclerView;
    private TextView data_shop_message;

    private ListView listView;
    private int status = 0;

    public reviewDisplayFragment() {
        // Required empty public constructor
    }

    String review_ID;
    String shop_ID;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_display, container, false);
        if (getArguments() != null){
            review_ID = getArguments().getString("reviewID");
//            shop_ID = getArguments().getString("shopID");
            Toast.makeText(getActivity(), review_ID + "   " + shop_ID, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
        }


        listView = view.findViewById(R.id.list_image);
        reviewer = view.findViewById(R.id.display_review_name_reviewer);
        datetime = view.findViewById(R.id.display_review_datetime);
        description = view.findViewById(R.id.display_review_description);
        star = view.findViewById(R.id.display_review_ratingBar);
        review_image = view.findViewById(R.id.display_review_image);
        image_review = view.findViewById(R.id.display_image_review);

//        getDataReview();
//        displayComment(view);

        return view;
    }

    private void displayComment(View view) {
        swipeRefreshLayout = view.findViewById(R.id.comment_swipe_refresh_layout);
        RecyclerView = view.findViewById(R.id.comment_recycler_view);
        data_shop_message = view.findViewById(R.id.comment);
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
        CommentRecyclerAdapter recyclerAdapter = new CommentRecyclerAdapter(comments, getContext());
        RecyclerView.LayoutManager recycle = new LinearLayoutManager(getContext());
        RecyclerView.setLayoutManager(recycle);
        RecyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.setAdapter(recyclerAdapter);
        RecyclerView.setVisibility(View.VISIBLE);
        data_shop_message.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
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
                    Toast.makeText(getActivity(), "NuLL NULL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Review Data", "Failed to get database", databaseError.toException());
            }
        });

    }

    private void setAdapter(ListView listView, ArrayList<String> arrayList) {
        Toast.makeText(getContext(), "Refreshing . .",
                Toast.LENGTH_LONG).show();
        listView.setAdapter(new ImageGridAdapter(getContext(), arrayList, status));
    }

}

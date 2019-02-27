package com.cmu.nuts.coffee9.main.fragment;


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
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.adapter.DataReviewRecyclerViewAdapter;
import com.cmu.nuts.coffee9.main.review.review_display_activity;
import com.cmu.nuts.coffee9.model.Comment;
import com.cmu.nuts.coffee9.model.Review;
import com.cmu.nuts.coffee9.model.Review_Image;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewRecyclerviewDisplay extends Fragment {
    private RecyclerView RecyclerView;
    private ArrayList<Comment> commentArrayList;
    private ArrayList<Review> reviewArrayList;
    String review_ID;
    String shop_ID;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ReviewRecyclerviewDisplay() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_recyclerview_display, container, false);
        RecyclerView = view.findViewById(R.id.recycler_view2019);
        review_ID = review_display_activity.review_ID;
        shop_ID = review_display_activity.shop_ID;
        reviewArrayList = new ArrayList<>();
        commentArrayList = new ArrayList<>();
        swipeRefreshLayout = view.findViewById(R.id.review_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentArrayList.clear();
                reviewArrayList.clear();
                getDataReview();
            }
        });

        getDataReview();
        return view;
    }

    private void getDataReview() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Review.tag).child(review_ID).child(shop_ID);
        final DatabaseReference Commentdatabase = FirebaseDatabase.getInstance().getReference(Comment.tag);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    final Review review = dataSnapshot.getValue(Review.class);

                    reviewArrayList.add(review);
                    assert review != null;
                    Commentdatabase.child(review.getRid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
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
                                setRecyclerView(reviewArrayList, commentArrayList);
                            } else {
                                commentArrayList.clear();
                                setRecyclerView(reviewArrayList, commentArrayList);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getContext(), "NuLL NULL", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "NuLL NULL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "NuLL NULL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerView(ArrayList<Review> reviewArrayList, ArrayList<Comment> commentArrayList) {
        DataReviewRecyclerViewAdapter dataReviewRecyclerViewAdapter = new DataReviewRecyclerViewAdapter(reviewArrayList, commentArrayList, getActivity());
        RecyclerView.LayoutManager recycle = new LinearLayoutManager(getContext());
        RecyclerView.setLayoutManager(recycle);
        RecyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.setAdapter(dataReviewRecyclerViewAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }
}

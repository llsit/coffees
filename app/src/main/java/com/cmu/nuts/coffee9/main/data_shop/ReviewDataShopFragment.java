package com.cmu.nuts.coffee9.main.data_shop;


import android.app.Activity;
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

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.adapter.ReviewRecyclerAdapter;
import com.cmu.nuts.coffee9.main.adapter.ShopRecyclerAdapter;
import com.cmu.nuts.coffee9.model.Review;
import com.cmu.nuts.coffee9.model.Shop;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewDataShopFragment extends Fragment {

    private List<Review> review;
    private Activity activity;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseReference databaseReference;

    public ReviewDataShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_data_shop, container, false);
        activity = getActivity();
        recyclerView = view.findViewById(R.id.review_recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.review_swipe_refresh_layout);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(Shop.tag);
        getReviewDatabase();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReviewDatabase();
            }
        });
        return view;
    }

    private void getReviewDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                review = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Review value = dataSnapshot1.getValue(Review.class);
                    assert value != null;
                    String sid = value.getSid();
                    String datetime = value.getDatetime();
                    String rid = value.getRid();
                    String detail = value.getDetail();
                    String uid = value.getUid();
                    String star = value.getStar();
                    String img_url = value.getImg_url();
                    Review reviews = new Review(rid, uid, sid, detail, img_url, datetime, star);
                    review.add(reviews);
                }
                setRecyclerView();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Review", "Failed to get database", databaseError.toException());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setRecyclerView() {
        ReviewRecyclerAdapter recyclerAdapter = new ReviewRecyclerAdapter(review, activity);
        RecyclerView.LayoutManager recycle = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(recycle);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }

}

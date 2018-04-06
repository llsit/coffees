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
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.adapter.ReviewRecyclerAdapter;
import com.cmu.nuts.coffee9.model.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewDataShopFragment extends Fragment {

    private static final String TAG = "REVIEW";
    private Activity activity;
    private String shopID;
    @BindView(R.id.review_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.review_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    public ReviewDataShopFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.data_shop_message)
    TextView data_shop_message;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_data_shop, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        if (getArguments() != null) {
            shopID = getArguments().getString("shop_ID");
        }
        if (shopID == null) {
            Toast.makeText(getContext(), "Something wrong, that all we know", Toast.LENGTH_SHORT).show();
            activity.finish();
        }
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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Review.tag).child(shopID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Review> reviews = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Review value = snapshot.getValue(Review.class);
                    if (value != null && value.getRid() != null) {
                        reviews.add(new Review(value.getSid(), value.getUid(), value.getRid(),
                                value.getDetail(), value.getImg_url(), value.getDatetime(),
                                String.valueOf(value.getStar())));
                    }
                }
                if (reviews.size() > 0) {
                    setRecyclerView(reviews);
                } else {
                    recyclerView.setVisibility(View.GONE);
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

    private void setRecyclerView(List<Review> reviews) {
        ReviewRecyclerAdapter recyclerAdapter = new ReviewRecyclerAdapter(reviews, activity);
        RecyclerView.LayoutManager recycle = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(recycle);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        data_shop_message.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

}

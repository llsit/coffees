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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewDataShopFragment extends Fragment {

    private static final String TAG = "REVIEW";
    // List<Review> review;
    private Activity activity;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseReference databaseReference;
    private String shopID;

    public ReviewDataShopFragment() {
        // Required empty public constructor
    }

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private Review review;
    @BindView(R.id.data_shop_message) TextView data_shop_message;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_data_shop, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        if (getArguments() != null) {
            shopID = getArguments().getString("shop_ID");
        } else {
            Toast.makeText(getContext(), "Something wrong, that all weknow", Toast.LENGTH_SHORT).show();
            activity.finish();
        }

        listView = view.findViewById(R.id.data_shop_list_view);
        arrayList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference(Review.tag).child(shopID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: "+ dataSnapshot.getChildrenCount());
                for (DataSnapshot reviewSnapshot: dataSnapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    if (Objects.requireNonNull(review).getRid() != null){
                        arrayList.add(review.getRid());
                    } else {
                        Toast.makeText(getContext(), "Something wrong,We can't get the review right now." + shopID, Toast.LENGTH_LONG).show();
                    }
                }

                if (arrayList.isEmpty()){
                    data_shop_message.setVisibility(View.VISIBLE);
                } else {
                    data_shop_message.setVisibility(View.GONE);
                    arrayAdapter = new ArrayAdapter<>(activity, R.layout.item_review_list, R.id.show, arrayList);
                    listView.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error: , " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }


    private void getReviewDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //review = new ArrayList<>();
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
                    //review.add(reviews);
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
        //ReviewRecyclerAdapter recyclerAdapter = new ReviewRecyclerAdapter(review, activity);
        RecyclerView.LayoutManager recycle = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(recycle);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(recyclerAdapter);
    }

}

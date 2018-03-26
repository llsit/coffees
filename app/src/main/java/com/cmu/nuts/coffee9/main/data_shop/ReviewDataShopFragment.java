package com.cmu.nuts.coffee9.main.data_shop;


import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.adapter.ReviewRecyclerAdapter;
import com.cmu.nuts.coffee9.model.Review;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_list_item_1;
import static java.lang.String.valueOf;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewDataShopFragment extends Fragment {

    // List<Review> review;
    private Activity activity;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseReference databaseReference;

    public ReviewDataShopFragment() {
        // Required empty public constructor
    }

    //private ListView mListView;
    //private ArrayList<String> mMeetings = new ArrayList<>();

    private ListView listView;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private Review review;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_data_shop, container, false);
        activity = getActivity();
//        recyclerView = view.findViewById(R.id.review_recycler_view);
//        swipeRefreshLayout = view.findViewById(R.id.review_swipe_refresh_layout);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(Review.tag);
//        getReviewDatabase();
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getReviewDatabase();
//            }
//        });
        listView = view.findViewById(R.id.listview);
        review = new Review();
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.item_review_list, arrayList);


        //textViewPersons = (TextView) findViewById(R.id.textViewPersons);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot item : dataSnapshot.getChildren()) {

                    review = item.getValue(Review.class);
                    arrayList.add(review.getDetail() + " Wow ");
                }
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        databaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Toast.makeText(getContext(), "TEXT", Toast.LENGTH_LONG).show();
//
//                for (DataSnapshot item : dataSnapshot.getChildren()) {
//
//                    Review value = dataSnapshot.getValue(Review.class);
//
//                    arrayList.add(value.getDetail());
//
//
//
//                    //arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
//                    //listView.setAdapter(arrayAdapter);
//                }
//                arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arrayList);
//                listView.setAdapter(arrayAdapter);
//
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                Toast.makeText(getContext(),  "TEXT", Toast.LENGTH_LONG).show();
//                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mMeetings);
//                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
//
//                    Review review = childSnapShot.getValue(Review.class);
//                    mMeetings.add(String.valueOf(review));
//
////                    String date = (String) childSnapShot.child("address").getValue();
////                    String datetime = (String) childSnapShot.child("datetime").getValue();
////                    String name = (String) childSnapShot.child("name").getValue();
////
////                    mMeetings.add(date);
////                    mMeetings.add(datetime);
////                    mMeetings.add(name);
//
//
//                }
//
//                mListView.setAdapter(arrayAdapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//        });


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

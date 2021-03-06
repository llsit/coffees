package com.cmu.nuts.coffee9.main.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmu.nuts.coffee9.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListShopFragment extends Fragment {


    public ListShopFragment() {
        // Required empty public constructor
    }

    private RecyclerView mlist;
    FirebaseDatabase database;
    DatabaseReference myref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_shop, container, false);
        mlist = view.findViewById(R.id.messages_list);
        mlist.setHasFixedSize(true);
        mlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        database = FirebaseDatabase.getInstance();
        myref = database.getReference("coffee_shop");

        return view;
    }

}

package com.cmu.nuts.coffee9.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Shop;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailDataShopFragment extends Fragment {

    private TextView text;

    private String shopID;
    private ValueEventListener valueEventListener;

    private DatabaseReference databaseReference;
    public DetailDataShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_data_shop, container, false);

        if (getArguments() != null){
            shopID = getArguments().getString("shop_ID");
            text = view.findViewById(R.id.location_shop);
//
            getdatashop(shopID);

        }

//


        return view;
    }

    public void getdatashop(String shopID){

        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Shop.tag).child(shopID);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Shop shop = dataSnapshot.getValue(Shop.class);
                assert shop != null;
                text.setText(getActivity().getString(R.string.name_data_shop).concat(shop.getName()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to load shop data!",
                        Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addListenerForSingleValueEvent(listener);
        valueEventListener = listener;
    }

}

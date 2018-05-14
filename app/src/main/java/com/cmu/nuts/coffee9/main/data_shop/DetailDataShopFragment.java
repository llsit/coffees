package com.cmu.nuts.coffee9.main.data_shop;


import android.os.Bundle;
import android.support.annotation.NonNull;
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

    private TextView name,address,detail,rating,price;

    private String shopID;

    public DetailDataShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_data_shop, container, false);

        if (getArguments() != null){
            shopID = getArguments().getString("shop_ID");
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.location_shop);
            detail = view.findViewById(R.id.detail);
            rating = view.findViewById(R.id.rating);
            price = view.findViewById(R.id.price);
            getdatashop();
        }
        return view;
    }

    public void getdatashop(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Shop.tag).child(shopID);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Shop shop = dataSnapshot.getValue(Shop.class);
                assert shop != null;
                name.setText(shop.getName());
                address.setText(shop.getAddress());
                detail.setText(shop.getDetail());
                rating.setText(shop.getRating());
                price.setText(shop.getPrice());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to load shop data!",
                        Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addListenerForSingleValueEvent(listener);
    }

}

package com.cmu.nuts.coffee9.main.data_shop;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.adapter.ImageGridAdapter;
import com.cmu.nuts.coffee9.model.Share;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageDataShopFragment extends Fragment {

    public static String[] ShareImages;
    ArrayList<String> n = new ArrayList<String>();

    private GridView gridView;
    private DatabaseReference mDatabase;
    private String shopID;

    public ImageDataShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_data_shop, container, false);

        gridView = view.findViewById(R.id.image_data_shop_gridview);


        if (getArguments() != null) {
            shopID = getArguments().getString("shop_ID");

        }

        mDatabase = FirebaseDatabase.getInstance().getReference(Share.tag);

        mDatabase.child(shopID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long counts = dataSnapshot.getChildrenCount();
                int i = 0;
                ShareImages = new String[(int) counts];
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Share shares = item.getValue(Share.class);
                    assert shares != null;
//                    ShareImages[i] = shares.getImg_url();
                    n.add(shares.getImg_url());
                    Toast.makeText(getActivity(), shares.getImg_url(),
                            Toast.LENGTH_SHORT).show();
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        if (ShareImages != null) {
//            gridView.setAdapter(
//                    new ImageGridAdapter(
//                            getActivity(),
//                            ShareImages
//                    )
//            );
//        }

        gridView.setAdapter(
                new ImageGridAdapter(
                        getActivity(),
                        n
                )
        );

        return view;
    }

}

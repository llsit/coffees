package com.cmu.nuts.coffee9.main.data_shop;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.adapter.ImageGridAdapter;
import com.cmu.nuts.coffee9.model.Share;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageDataShopFragment extends Fragment {

    private String shopID;
    private DatabaseReference mDatabase;
    private GridView gridView;

    public ImageDataShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_data_shop, container, false);

        gridView = view.findViewById(R.id.image_data_shop_gridview);
        Button all = view.findViewById(R.id.AllButtonSelector);
        Button byMe = view.findViewById(R.id.yourButtonSelector);

        if (getArguments() != null) {
            shopID = getArguments().getString("shop_ID");
        }
        mDatabase = FirebaseDatabase.getInstance().getReference(Share.tag);
        getImage();
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
        byMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageByMe();
            }
        });

        return view;
    }

    private void getImageByMe() {
        final FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child(shopID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> arrayList = new ArrayList<>();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Share shares = item.getValue(Share.class);
                        assert shares != null;
                        assert auth != null;
                        if (shares.getUid().equals(auth.getUid())) {
                            arrayList.add(shares.getImg_url());
                        }
                    }
                    setAdapter(gridView, arrayList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getImage() {
        mDatabase.child(shopID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> arrayList = new ArrayList<>();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Share shares = item.getValue(Share.class);
                        assert shares != null;
                        arrayList.add(shares.getImg_url());
                    }
                    setAdapter(gridView, arrayList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setAdapter(GridView gridView, final ArrayList<String> arrayList) {
        gridView.setAdapter(new ImageGridAdapter(getActivity(), arrayList, 1));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), FullImageActivity.class);
                intent.putExtra("mylist", arrayList);
                intent.putExtra("shopid", shopID);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out);
            }
        });
    }

}

package com.cmu.nuts.coffee9.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmu.nuts.coffee9.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailDataShopFragment extends Fragment {

    private TextView text;

    private String shopID;

    public DetailDataShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_data_shop, container, false);

        if (getArguments() != null){
            shopID = getArguments().getString("message");
            text = view.findViewById(R.id.id_shop);
//
            text.setText(shopID);
        }

//


        return view;
    }

}

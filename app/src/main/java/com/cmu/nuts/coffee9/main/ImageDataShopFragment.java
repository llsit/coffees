package com.cmu.nuts.coffee9.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmu.nuts.coffee9.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageDataShopFragment extends Fragment {


    public ImageDataShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_data_shop, container, false);
    }

}

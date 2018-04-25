package com.cmu.nuts.coffee9.main.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cmu.nuts.coffee9.R;

import java.util.ArrayList;

public class ImageGridAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;

    private ArrayList<String> imageUrls;

    public ImageGridAdapter(Context context, ArrayList<String> imageUrls) {
        super(context, R.layout.item_image, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_image, parent, false);
        }

        Glide
                .with(context)
                .load(imageUrls.get(position))
                .into((ImageView) convertView);

        return convertView;
    }
}

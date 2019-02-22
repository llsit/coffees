package com.cmu.nuts.coffee9.main.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.cmu.nuts.coffee9.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageGridAdapter extends ArrayAdapter {
    private LayoutInflater inflater;
    private int status;

    private ArrayList<String> imageUrls;

    public ImageGridAdapter(Context context, ArrayList<String> imageUrls, int status) {
        super(context, R.layout.item_image, imageUrls);

        this.imageUrls = imageUrls;
        this.status = status;

        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_image, parent, false);
        }
        ImageView img_del = convertView.findViewById(R.id.image_del);
        img_del.setVisibility(View.GONE);
        ImageView img = convertView.findViewById(R.id.Image_show);
        img.setPadding(30, 20, 30, 20);
        if (status == 1) { //ImageDataShopFragment.java
            Picasso.get()
                    .load(imageUrls.get(position))
                    .placeholder(R.drawable.img_preview)
                    .into(img);
        } else {
            Picasso.get()
                    .load(imageUrls.get(position))
                    .placeholder(R.drawable.img_preview)
                    .fit().centerInside()
                    .into(img);
        }

        return convertView;
    }
}

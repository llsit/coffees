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
    private Context context;
    private LayoutInflater inflater;
    private int status;

    private ArrayList<String> imageUrls;

    public ImageGridAdapter(Context context, ArrayList<String> imageUrls, int status) {
        super(context, R.layout.item_image, imageUrls);

        this.context = context;
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
        if (status == 1){ //ImageDataShopFragment.java
            Picasso.get()
                    .load(imageUrls.get(position))
                    .placeholder(R.drawable.img_preview)
                    .fit().centerCrop()
                    .error(R.drawable.img_preview)
                    .into((ImageView) convertView);
        }else{
            Picasso.get()
                    .load(imageUrls.get(position))
                    .placeholder(R.drawable.img_preview)
                    .fit().centerInside()
                    .error(R.drawable.img_preview)
                    .into((ImageView) convertView);
        }


        return convertView;
    }
}

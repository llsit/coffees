package com.cmu.nuts.coffee9.main.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Review;
import com.esafirm.imagepicker.model.Image;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ImageSelectedAdapter extends RecyclerView.Adapter<ImageSelectedAdapter.ImageHolder> {
    private List<Image> images;
    private Context context;

    public ImageSelectedAdapter(List<Image> image, Context context) {
        images = image;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageSelectedAdapter.ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSelectedAdapter.ImageHolder holder, int position) {
        Picasso.get().load(Uri.fromFile(new File(images.get(position).getPath()))).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView imageDel;

        ImageHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.Image_show);
            imageDel = itemView.findViewById(R.id.image_del);
        }
    }
}

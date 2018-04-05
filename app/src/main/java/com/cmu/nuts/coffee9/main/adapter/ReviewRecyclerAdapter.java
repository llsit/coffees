package com.cmu.nuts.coffee9.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Review;

import java.util.List;

/**
 * Created by nuts on 3/23/2018.
 **/

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewHolder> {
    private List<Review> reviews;
    private Context context;

    public ReviewRecyclerAdapter(List<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Review review = reviews.get(position);

        holder.tv_sid.setText(review.getSid());
        holder.tv_rid.setText(review.getRid());
        holder.tv_detail.setText(review.getDetail());
        holder.tv_datetime.setText(review.getDatetime());
        holder.tv_star.setText(String.valueOf(review.getStar()));
        holder.tv_userid.setText(review.getUid());
        holder.tv_url.setText(review.getImg_url());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open new Activity that show Review content
                Toast.makeText(context,  position +" name : " + review.getSid(), Toast.LENGTH_LONG).show();
//                Intent i = new Intent(v.getContext(), DataShopActivity.class);
//                String shopID;
//                shopID = review.getSid();
//                i.putExtra("shopID", shopID);
//                v.getContext().startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        int arr = 0;
        try {
            if (reviews.size() == 0) {
                arr = 0;
            } else {
                arr = reviews.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    class ReviewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tv_rid;
        TextView tv_sid;
        TextView tv_datetime;
        TextView tv_detail;
        TextView tv_star;
        TextView tv_userid;
        TextView tv_url;

        @SuppressLint("WrongViewCast")
        ReviewHolder(View itemView) {
            super(itemView);
            tv_sid = itemView.findViewById(R.id.item_review_sid);
            tv_datetime = itemView.findViewById(R.id.item_review_datetime);
            tv_detail = itemView.findViewById(R.id.item_review_description);
            tv_star = itemView.findViewById(R.id.item_review_ratingBar2);
            tv_userid = itemView.findViewById(R.id.item_review_uid);
            tv_rid = itemView.findViewById(R.id.item_review_rid);
            tv_url = itemView.findViewById(R.id.url);
            cardView = itemView.findViewById(R.id.item_review_cardview);
        }
    }
}

package com.cmu.nuts.coffee9.main.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.review.review_display_activity;
import com.cmu.nuts.coffee9.model.Comment;
import com.cmu.nuts.coffee9.model.Member;
import com.cmu.nuts.coffee9.model.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

/**
 * Created by nuts on 3/23/2018.
 **/

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewHolder> {
    private List<Review> reviews;
    private Activity activity;

    public ReviewRecyclerAdapter(List<Review> reviews, Activity activity) {
        this.reviews = reviews;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_review, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Review review = reviews.get(position);
        holder.tv_sid.setText(review.getSid());
        holder.tv_rid.setText(review.getRid());
        holder.tv_detail.setText(review.getDetail());
        holder.tv_datetime.setText(review.getDatetime());
        holder.tv_star.setRating(Float.parseFloat(review.getStar()));
        holder.tv_uid.setText(review.getUid());
        holder.tv_url.setText(review.getImg_url());
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(Member.tag).child(review.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member member = dataSnapshot.getValue(Member.class);
                assert member != null;
                holder.tv_name.setText(member.getName());
                Picasso.get()
                        .load(member.getPhotoUrl())
                        .placeholder(R.drawable.img_user)
                        .error(R.drawable.img_user)
                        .resize(200, 200)
                        .centerInside()
                        .into(holder.tv_image);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Shop", "Failed to get database", error.toException());
            }
        });
        DatabaseReference countReference = FirebaseDatabase.getInstance().getReference(Comment.tag).child(review.getSid());
        countReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                int counts = Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()));
//                System.out.println(counts);
                holder.tv_count.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Shop", "Failed to get database", databaseError.toException());
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewID = review.getRid();
                String shopID = review.getSid();

                Intent i = new Intent(v.getContext(), review_display_activity.class);
                i.putExtra("reviewID", reviewID);
                i.putExtra("shopID", shopID);
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out);
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
        RatingBar tv_star;
        TextView tv_uid;
        TextView tv_url;
        TextView tv_name;
        ImageView tv_image;
        TextView tv_count;

        @SuppressLint("WrongViewCast")
        ReviewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_review_card_view);
            tv_sid = itemView.findViewById(R.id.item_review_sid);
            tv_datetime = itemView.findViewById(R.id.item_review_datetime);
            tv_detail = itemView.findViewById(R.id.item_review_description);
            tv_star = itemView.findViewById(R.id.item_review_ratingBar);
            tv_uid = itemView.findViewById(R.id.item_review_uid);
            tv_rid = itemView.findViewById(R.id.item_review_rid);
            tv_url = itemView.findViewById(R.id.item_shop_review_url);
            tv_name = itemView.findViewById(R.id.item_review_name_reviewer);
            tv_image = itemView.findViewById(R.id.item_review_image);
            tv_count = itemView.findViewById(R.id.item_review_count);
        }
    }
}

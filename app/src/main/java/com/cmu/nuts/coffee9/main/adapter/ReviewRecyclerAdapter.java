package com.cmu.nuts.coffee9.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.review.review_display_activity;
import com.cmu.nuts.coffee9.model.Member;
import com.cmu.nuts.coffee9.model.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nuts on 3/23/2018.
 **/

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewHolder> {
    private List<Review> reviews;
    private Context context;
    private DatabaseReference mDatabase;

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
    public void onBindViewHolder(final ReviewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Review review = reviews.get(position);
        holder.tv_sid.setText(review.getSid());
        holder.tv_rid.setText(review.getRid());
        holder.tv_detail.setText(review.getDetail());
        holder.tv_datetime.setText(review.getDatetime());
        holder.tv_star.setRating(Float.parseFloat(review.getStar()));
        holder.tv_uid.setText(review.getUid());
        holder.tv_url.setText(review.getImg_url());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Member").child(review.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member member = dataSnapshot.getValue(Member.class);
                assert member != null;
                holder.tv_name.setText(member.getName());
                Picasso.get()
                        .load(member.getPhotoUrl())
                        .placeholder(R.drawable.img_user)
                        .error(R.drawable.img_user)
                        .resize(200,200)
                        .centerInside()
                        .into(holder.tv_image);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Shop", "Failed to get database", error.toException());
            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Comment").child(review.getRid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                    Comment com = dataSnapshot.getValue(Comment.class);
                int counts = Math.toIntExact(dataSnapshot.getChildrenCount());
                    holder.tv_count.setText(counts);
                    Toast.makeText(context, dataSnapshot.getKey() + ":" + dataSnapshot.getChildrenCount(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Shop", "Failed to get database", databaseError.toException());
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open new Activity that show Review content
                Toast.makeText(context, position + " name : " + review.getSid(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(v.getContext(), review_display_activity.class);
                String reviewID;
                String shopID;
                reviewID = review.getRid();
                shopID = review.getSid();
                i.putExtra("reviewID", reviewID);
                i.putExtra("shopID",shopID);
                v.getContext().startActivity(i);
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

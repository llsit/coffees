package com.cmu.nuts.coffee9.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Comment;
import com.cmu.nuts.coffee9.model.Member;
import com.cmu.nuts.coffee9.model.Review;
import com.cmu.nuts.coffee9.model.Review_Image;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DataReviewRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Review> reviews;
    private ArrayList<Comment> comments;
    private ArrayList<String> arrayList;
    private Context context;
    private int ITEM_HEADER = 0;
    private int ITEM_COMMENT = 1;

    public DataReviewRecyclerViewAdapter(ArrayList<Review> reviewArrayList, ArrayList<Comment> commentArrayList, Context context) {
        this.reviews = reviewArrayList;
        this.comments = commentArrayList;
        this.context = context;
        arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_review_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == ITEM_COMMENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
            return new CommentHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            Review review = reviews.get(position);
            headerViewHolder.tv_datetime.setText(review.getDatetime());
            headerViewHolder.tv_detail.setText(review.getDetail());
            headerViewHolder.tv_star.setRating(Float.valueOf(review.getStar()));
            final DatabaseReference uDatabase = FirebaseDatabase.getInstance().getReference(Member.tag).child(review.getUid());
            uDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Member mem = dataSnapshot.getValue(Member.class);
                    if (mem != null) {
                        headerViewHolder.tv_name.setText(mem.getName());
                        Picasso.get()
                                .load(mem.getPhotoUrl())
                                .placeholder(R.drawable.img_user)
                                .error(R.drawable.img_user)
                                .into(headerViewHolder.tv_person);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("Review Image", "Failed to get database", databaseError.toException());
                }
            });
            final DatabaseReference imgDatabase = FirebaseDatabase.getInstance().getReference(Review_Image.tag);
            imgDatabase.child(review.getRid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            Review_Image ri = item.getValue(Review_Image.class);
                            if (ri != null) {
                                arrayList.add(ri.getImage_url());
                            }
                        }
                        ImageGridAdapter imageGridAdapter = new ImageGridAdapter(context, arrayList, 2);
                        headerViewHolder.gridView.setAdapter(imageGridAdapter);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("Review Image", "Failed to get database", databaseError.toException());
                }
            });
        } else if (holder instanceof CommentHolder) {
            final CommentHolder commentHolder = (CommentHolder) holder;
            Comment comment = comments.get(position - 1);
            commentHolder.tv_datetime.setText(comment.getDatetime());
            commentHolder.tv_detail.setText(comment.getDetail());
            final DatabaseReference uDatabase = FirebaseDatabase.getInstance().getReference(Member.tag).child(comment.getUid());
            uDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Member mem = dataSnapshot.getValue(Member.class);
                    if (mem != null) {
                        commentHolder.tv_name.setText(mem.getName());
                        Picasso.get()
                                .load(mem.getPhotoUrl())
                                .placeholder(R.drawable.img_user)
                                .error(R.drawable.img_user)
                                .into(commentHolder.tv_image);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("Review Image", "Failed to get database", databaseError.toException());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_HEADER;
        } else if (comments.size() > 0) {
            return ITEM_COMMENT;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (comments.size() > 0) {
            return comments.size() + 1;
        } else {
            return 1;
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView tv_person;
        TextView tv_name;
        TextView tv_datetime;
        TextView tv_detail;
        RatingBar tv_star;
        GridView gridView;

        @SuppressLint("WrongViewCast")
        HeaderViewHolder(View itemView) {
            super(itemView);
            tv_person = itemView.findViewById(R.id.display_review_image);
            tv_datetime = itemView.findViewById(R.id.display_review_datetime);
            tv_detail = itemView.findViewById(R.id.display_review_description);
            tv_star = itemView.findViewById(R.id.display_review_ratingBar);
            tv_name = itemView.findViewById(R.id.display_review_name_reviewer);
            gridView = itemView.findViewById(R.id.review_shop_gridview);
        }
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        TextView tv_datetime;
        TextView tv_detail;
        TextView tv_name;
        ImageView tv_image;

        @SuppressLint("WrongViewCast")
        CommentHolder(View itemView) {
            super(itemView);
            tv_datetime = itemView.findViewById(R.id.item_comment_datetime);
            tv_detail = itemView.findViewById(R.id.item_comment_description);
            tv_name = itemView.findViewById(R.id.item_comment_name);
            tv_image = itemView.findViewById(R.id.item_comment_image);
        }
    }
}

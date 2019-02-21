package com.cmu.nuts.coffee9.main.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Comment;
import com.cmu.nuts.coffee9.model.Member;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentHolder> {
    private ArrayList<Comment> comments;
    private Context context;
    private DatabaseReference mDatabase;

    public CommentRecyclerAdapter(ArrayList<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Comment comment = comments.get(position);

        holder.tv_detail.setText(comment.getDetail());
        holder.tv_datetime.setText(comment.getDatetime());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Member").child(comment.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member mem = dataSnapshot.getValue(Member.class);
                assert mem != null;
                holder.tv_name.setText(mem.getName());
                Picasso.get()
                        .load(mem.getPhotoUrl())
                        .placeholder(R.drawable.img_user)
                        .error(R.drawable.img_user)
                        .resize(200, 200)
                        .centerInside()
                        .into(holder.tv_image);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("comment", "Failed to get database", error.toException());
            }
        });

    }

    @Override
    public int getItemCount() {
        int arr = 0;
        try {
            if (comments.size() == 0) {
                arr = 0;
            } else {
                arr = comments.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    class CommentHolder extends RecyclerView.ViewHolder {

        //        TextView tv_rid;
//        TextView tv_cid;
        TextView tv_datetime;
        TextView tv_detail;
        //        TextView tv_uid;
        TextView tv_name;
        ImageView tv_image;

        @SuppressLint("WrongViewCast")
        CommentHolder(View itemView) {
            super(itemView);
//            tv_cid = itemView.findViewById(R.id.item_review_sid);
            tv_datetime = itemView.findViewById(R.id.item_comment_datetime);
            tv_detail = itemView.findViewById(R.id.item_comment_description);
//            tv_uid = itemView.findViewById(R.id.item_review_uid);
//            tv_rid = itemView.findViewById(R.id.item_review_rid);
            tv_name = itemView.findViewById(R.id.item_comment_name);
            tv_image = itemView.findViewById(R.id.item_comment_image);

        }
    }
}

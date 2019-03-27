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
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.data_shop.DataShopActivity;
import com.cmu.nuts.coffee9.model.Favorite;
import com.cmu.nuts.coffee9.model.Share;
import com.cmu.nuts.coffee9.model.Shop;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.UUID;

import static java.security.AccessController.getContext;

/**
 * Created by Jaylers on 07-Feb-18.
 **/

public class ShopRecyclerAdapter extends RecyclerView.Adapter<ShopRecyclerAdapter.ShopHolder> {

    private List<Shop> shops;
    private Activity activity;
    private DatabaseReference mDatabase;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public ShopRecyclerAdapter(List<Shop> shops, Activity activity) {
        this.shops = shops;
        this.activity = activity;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_shop, parent, false);
        return new ShopHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShopHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Shop shop = shops.get(position);

        holder.tv_sid.setText(shop.getSid());
        holder.tv_name.setText(shop.getName());
        holder.tv_detail.setText(shop.getDetail());
        holder.tv_rating.setText(shop.getRating());
        holder.tv_price.setText(shop.getPrice());
        holder.tv_address.setText(shop.getAddress());
        holder.tv_uid.setText(shop.getUid());
        holder.tv_location.setText(shop.getLocation());
        holder.tv_ratingBar.setRating(Integer.parseInt(shop.getRating()));

        holder.tv_love.setVisibility(View.GONE);
        DatabaseReference fDatabase = FirebaseDatabase.getInstance().getReference(Favorite.tag).child(user.getUid());
        fDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Favorite fav = item.getValue(Favorite.class);
                    assert fav != null;
                    if (fav.getSid().equals(shop.getSid())) {
                        holder.tv_not_love.setVisibility(View.GONE);
                        holder.tv_love.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.tv_not_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fid = mDatabase.push().getKey();
                String uid = user.getUid();
                String sid = shop.getSid();
                System.out.println(fid);
                Favorite fav = new Favorite(fid, uid, sid);
                mDatabase.child(Favorite.tag).child(uid).child(fid).setValue(fav);
                holder.tv_not_love.setVisibility(View.GONE);
                holder.tv_love.setVisibility(View.VISIBLE);
                Toast.makeText(activity, "love it", Toast.LENGTH_LONG).show();
            }
        });

        holder.tv_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Favorite.tag).child(user.getUid());

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            Favorite fav = item.getValue(Favorite.class);
                            if (fav != null) {
                                if (shop.getSid().equals(fav.getSid())) {
                                    mDatabase.child(fav.getFid()).removeValue();
                                    holder.tv_not_love.setVisibility(View.VISIBLE);
                                    holder.tv_love.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Shop", "Failed to get database", databaseError.toException());
                    }
                });
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open new Activity that show Shop content
                Intent i = new Intent(v.getContext(), DataShopActivity.class);
                String shopID;
                shopID = shop.getSid();
                i.putExtra("shopID", shopID);
                activity.startActivityForResult(i, 1);
            }
        });

        DatabaseReference iDatebase = FirebaseDatabase.getInstance().getReference(Share.tag).child(shop.getSid());
        iDatebase.limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Share shares = dataSnapshot.getValue(Share.class);
                if (shares != null && shares.getImg_url() != null) {
                    Picasso.get().load(shares.getImg_url()).placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_email).into(holder.tv_image);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        int arr = 0;
        try {
            if (shops.size() == 0) {
                arr = 0;
            } else {
                arr = shops.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    class ShopHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tv_sid;
        TextView tv_name;
        TextView tv_detail;
        TextView tv_rating;
        TextView tv_price;
        TextView tv_address;
        TextView tv_uid;
        TextView tv_location;
        ImageView tv_image;
        ImageView tv_love;
        ImageView tv_not_love;
        RatingBar tv_ratingBar;

        ShopHolder(View itemView) {
            super(itemView);
            tv_sid = itemView.findViewById(R.id.item_shop_sid);
            tv_name = itemView.findViewById(R.id.item_shop_name);
            tv_detail = itemView.findViewById(R.id.item_shop_detail);
            tv_rating = itemView.findViewById(R.id.item_shop_rating);
            tv_price = itemView.findViewById(R.id.item_shop_price);
            tv_address = itemView.findViewById(R.id.item_shop_address);
            tv_uid = itemView.findViewById(R.id.item_shop_uid);
            tv_location = itemView.findViewById(R.id.item_shop_location);
            cardView = itemView.findViewById(R.id.item_shop_card_view);
            tv_image = itemView.findViewById(R.id.item_shop_img);
            tv_love = itemView.findViewById(R.id.fav);
            tv_not_love = itemView.findViewById(R.id.not_fav);
            tv_ratingBar = itemView.findViewById(R.id.item_shop_ratingbar);
        }
    }
}

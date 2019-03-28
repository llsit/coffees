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
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.data_shop.DataShopActivity;
import com.cmu.nuts.coffee9.main.fragment.FavoriteFragment;
import com.cmu.nuts.coffee9.model.Favorite;
import com.cmu.nuts.coffee9.model.Shop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.FavHolder> {

    private List<Shop> shops;
    private Activity activity;
    private FavoriteFragment favoriteFragment;

    public FavoriteRecyclerAdapter(List<Shop> shops, Activity activity, FavoriteFragment favoriteFragment) {
        this.shops = shops;
        this.activity = activity;
        this.favoriteFragment = favoriteFragment;
    }

    @NonNull
    @Override
    public FavHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_fav, parent, false);
        return new FavHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Shop shop = shops.get(position);
        holder.tv_name.setText(shop.getName());
        holder.tv_detail.setText(shop.getDetail());
        holder.tv_rating.setRating(Integer.parseInt(shop.getRating()));
        holder.tv_price.setText(shop.getPrice());

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                String uid = user.getUid();
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Favorite.tag).child(uid);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            Favorite fav = item.getValue(Favorite.class);
                            if (fav != null) {
                                if (shop.getSid().equals(fav.getSid())) {
                                    mDatabase.child(fav.getFid()).removeValue();
                                    favoriteFragment.refresh();
                                    Toast.makeText(activity, "Done", Toast.LENGTH_LONG).show();
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
//                Toast.makeText(activity, position + " name : " + shop.getSid(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(v.getContext(), DataShopActivity.class);
                String shopID;
                shopID = shop.getSid();
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

    class FavHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tv_name;
        TextView tv_detail;
        RatingBar tv_rating;
        TextView tv_price;
        ImageButton close;

        FavHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.item_fav_name);
            tv_detail = itemView.findViewById(R.id.item_fav_detail);
            tv_price = itemView.findViewById(R.id.item_fav_price);
            tv_rating = itemView.findViewById(R.id.item_fav_ratingbar);
            cardView = itemView.findViewById(R.id.item_fav_card_view);
            close = itemView.findViewById(R.id.close);
        }
    }
}

package com.cmu.nuts.coffee9.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.util.List;

/**
 * Created by Jaylers on 07-Feb-18.
 **/

public class ShopRecyclerAdapter extends RecyclerView.Adapter<ShopRecyclerAdapter.ShopHolder> {

    private List<Shop> shops;
    private Context context;
    private DatabaseReference mDatabase, fDatabase;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public ShopRecyclerAdapter(List<Shop> shops, Context context) {
        this.shops = shops;
        this.context = context;
    }

    @Override
    public ShopHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop, parent, false);
        return new ShopHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShopHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Shop shop = shops.get(position);

        holder.tv_sid.setText(shop.getSid());
        holder.tv_name.setText(shop.getName());
        holder.tv_detail.setText(shop.getDetail());
        holder.tv_open_time.setText(shop.getOpen_hour());
        holder.tv_price.setText(shop.getPrice());
        holder.tv_address.setText(shop.getAddress());
        holder.tv_uid.setText(shop.getUid());
        holder.tv_location.setText(shop.getLocation());


        fDatabase = FirebaseDatabase.getInstance().getReference(Favorite.tag).child(user.getUid());
        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Favorite fav = item.getValue(Favorite.class);
                    if (fav.getSid().equals(shop.getSid())) {
                        holder.tv_fav.setVisibility(View.GONE);
                    } else {
                        holder.tv_fav.setOnFavoriteChangeListener(
                                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                                    @Override
                                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                                        user = FirebaseAuth.getInstance().getCurrentUser();
                                        mDatabase = FirebaseDatabase.getInstance().getReference();
                                        String fid = mDatabase.push().getKey();
                                        String uid = user.getUid();
                                        String sid = shop.getSid();
                                        if (favorite) {
                                            Favorite fav = new Favorite(fid, uid, sid);
                                            mDatabase.child("Favorite").child(uid).child(fid).setValue(fav);
                                            Toast.makeText(context, "love it", Toast.LENGTH_LONG).show();
                                        } else {
                                            mDatabase.child(Favorite.tag).child(uid).child(fid).removeValue();
//                            Toast.makeText(context, "not love", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open new Activity that show Shop content
//                Toast.makeText(context, position + " name : " + shop.getSid(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(v.getContext(), DataShopActivity.class);
                String shopID;
                shopID = shop.getSid();
                i.putExtra("shopID", shopID);
                v.getContext().startActivity(i);

            }
        });
    }

    private String getImage(String sid) {
        final String[] url = {null};
        DatabaseReference iDatebase = FirebaseDatabase.getInstance().getReference(Share.tag).child(sid);
        iDatebase.limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Share shares = dataSnapshot.getValue(Share.class);
                assert shares != null;
                url[0] = shares.getImg_url();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return url[0];
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
        TextView tv_open_time;
        TextView tv_price;
        TextView tv_address;
        TextView tv_uid;
        TextView tv_location;
        MaterialFavoriteButton tv_fav;
        ImageView tv_image;

        ShopHolder(View itemView) {
            super(itemView);
            tv_sid = itemView.findViewById(R.id.item_shop_sid);
            tv_name = itemView.findViewById(R.id.item_shop_name);
            tv_detail = itemView.findViewById(R.id.item_shop_detail);
            tv_open_time = itemView.findViewById(R.id.item_shop_open_time);
            tv_price = itemView.findViewById(R.id.item_shop_price);
            tv_address = itemView.findViewById(R.id.item_shop_address);
            tv_uid = itemView.findViewById(R.id.item_shop_uid);
            tv_location = itemView.findViewById(R.id.item_shop_location);
            cardView = itemView.findViewById(R.id.item_shop_card_view);
            tv_fav = itemView.findViewById(R.id.love);
            tv_image = itemView.findViewById(R.id.item_shop_img);
        }
    }
}

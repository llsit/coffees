package com.cmu.nuts.coffee9.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.DataShopActivity;
import com.cmu.nuts.coffee9.main.DetailDataShopFragment;
import com.cmu.nuts.coffee9.model.Shop;

import java.util.List;

/**
 * Created by Jaylers on 07-Feb-18.
 */

public class ShopRecyclerAdapter extends RecyclerView.Adapter<ShopRecyclerAdapter.ShopHolder> {

    private List<Shop> shops;
    private Context context;

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
    public void onBindViewHolder(ShopHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Shop shop = shops.get(position);

        holder.tv_sid.setText(shop.getSid());
        holder.tv_name.setText(shop.getName());
        holder.tv_detail.setText(shop.getDetail());
        holder.tv_open_time.setText(shop.getOpen_houre());
        holder.tv_price.setText(shop.getPrice());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open new Activity that show Shop content
                Toast.makeText(context, position + " name : " + shop.getSid(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(v.getContext(), DataShopActivity.class);
                String shopID;
                shopID = shop.getSid();
                i.putExtra("shopID", shopID);
                v.getContext().startActivity(i);



//                Intent intent = new Intent(getActivity().getBaseContext(),
//                        TargetActivity.class);
//                intent.putExtra("message", message);
//                getActivity().startActivity(intent);
//                Intent i = new Intent(context..this, DataShopActivity.class);
//                //                String userID = null;
////                userID = shop.getSid();
////                i.putExtra("userID", userID);
//                context.startActivity(i);
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
        TextView tv_open_time;
        TextView tv_price;
        TextView tv_address;
        TextView tv_uid;
        TextView tv_location;

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
        }
    }
}

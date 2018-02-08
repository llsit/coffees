package com.cmu.nuts.coffee9.main.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Shop;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaylers on 07-Feb-18.
 */

public class ShopRecyclerAdapter extends RecyclerView.Adapter<ShopRecyclerAdapter.ShopHolder>{

    List<Shop> shops;
    Context context;

    public ShopRecyclerAdapter(List<Shop> shops, Context context){
        this.shops = shops;
        this.context = context;
    }

    @Override
    public ShopHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop, parent, false);
        return new ShopHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopHolder holder, int position) {
        Shop shop = shops.get(position);
        holder.tv_sid.setText(shop.getSid());
        holder.tv_name.setText(shop.getName());
    }

    @Override
    public int getItemCount() {
        int arr = 0;
        try {
            if (shops.size() == 0){
                arr = 0;
            } else {
                arr = shops.size();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return arr;
    }

    class ShopHolder extends RecyclerView.ViewHolder{
        TextView tv_sid;
        TextView tv_name;

        public ShopHolder(View itemView){
            super(itemView);
            tv_sid = itemView.findViewById(R.id.item_shop_sid);
            tv_name = itemView.findViewById(R.id.item_shop_name);
        }
    }
}

package com.cmu.nuts.coffee9.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Open_Hour;

import java.util.ArrayList;

public class AddTimeShopAdapter extends RecyclerView.Adapter<AddTimeShopAdapter.AddTimeHolder> {
    private Context context;
    private ArrayList<Open_Hour> openHourArrayList;

    public AddTimeShopAdapter(Context context, ArrayList<Open_Hour> openHourArrayList) {
        this.context = context;
        this.openHourArrayList = openHourArrayList;
    }

    @NonNull
    @Override

    public AddTimeShopAdapter.AddTimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time_shop, parent, false);
        return new AddTimeShopAdapter.AddTimeHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AddTimeShopAdapter.AddTimeHolder holder, int position) {
//        System.out.println(openHourArrayList.get(position).getDate() + " " + openHourArrayList.get(position).getTimestart() + "-" + openHourArrayList.get(position).getTimeend());
        holder.time.setText(openHourArrayList.get(position).getDate() + " " + openHourArrayList.get(position).getTimestart() + "-" + openHourArrayList.get(position).getTimeend());
    }

    @Override
    public int getItemCount() {
        return openHourArrayList.size();
    }


    class AddTimeHolder extends RecyclerView.ViewHolder {
        TextView time;

        AddTimeHolder(View view) {
            super(view);
            time = view.findViewById(R.id.textView_time);
        }
    }
}

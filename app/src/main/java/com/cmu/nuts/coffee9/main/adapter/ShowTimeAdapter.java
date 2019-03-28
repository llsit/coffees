package com.cmu.nuts.coffee9.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Open_Hour;

import java.util.ArrayList;

public class ShowTimeAdapter extends RecyclerView.Adapter<ShowTimeAdapter.ShowTimeHolder> {
    private Context context;
    private ArrayList<Open_Hour> openHourArrayList;

    public ShowTimeAdapter(Context context, ArrayList<Open_Hour> openHourArrayList) {
        this.context = context;
        this.openHourArrayList = openHourArrayList;
    }

    @NonNull
    @Override
    public ShowTimeAdapter.ShowTimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time_shop, viewGroup, false);
        return new ShowTimeAdapter.ShowTimeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowTimeAdapter.ShowTimeHolder showTimeHolder, int i) {
        showTimeHolder.time.setText(openHourArrayList.get(i).getDate() + " " + openHourArrayList.get(i).getTimestart() + "-" + openHourArrayList.get(i).getTimeend());
        showTimeHolder.imageView_del_time.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        if (openHourArrayList.isEmpty()) {
            return 0;
        } else {
            return openHourArrayList.size();
        }
    }

    class ShowTimeHolder extends RecyclerView.ViewHolder {
        TextView time;
        ImageView imageView_del_time;

        ShowTimeHolder(View view) {
            super(view);
            time = view.findViewById(R.id.textView_time);
            imageView_del_time = view.findViewById(R.id.imageView_del_time);
        }
    }
}

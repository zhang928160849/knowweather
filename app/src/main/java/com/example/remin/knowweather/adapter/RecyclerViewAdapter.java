package com.example.remin.knowweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.remin.knowweather.Item;
import com.example.remin.knowweather.R;
import com.example.remin.knowweather.db.County;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by remin.
 * Created Time 2017/8/24 ${Time}
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<County> counties;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView cityName;
        Button deleteButton;
        public ViewHolder(View itemView) {
            super(itemView);
            this.cityName = (TextView) itemView.findViewById(R.id.city_name);
            this.deleteButton = (Button) itemView.findViewById(R.id.delete_button);
        }
    }

    public RecyclerViewAdapter(ArrayList<County> counties) {
        this.counties = counties;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        County county = counties.get(position);
        holder.deleteButton.setVisibility(View.VISIBLE);
        holder.cityName.setText(counties.get(position).getCountyName());
//        Item item = mItems.get(position);
//        holder.textView.setText(item.getItemName());
//        holder.imageView.setImageResource(item.getItemId());
    }

    @Override
    public int getItemCount()
    {
        return counties.size();
    }
}

package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterSpotlightItemListRecyclerView extends RecyclerView.Adapter<AdapterSpotlightItemListRecyclerView.MyViewHolder> {
    Context context;
    List<String> itemname,price;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public MyViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterSpotlightItemListRecyclerView(Context context, List<String> itemname, List<String> price) {
        this.context = context;
        this.itemname = itemname;
        this.price = price;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterSpotlightItemListRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                int viewType) {


        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_spotlight_items, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Toast.makeText(context, "called", Toast.LENGTH_SHORT).show();

        return itemname.size();
    }
}

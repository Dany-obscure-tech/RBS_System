package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.dotcom.rbs_system.R;

import java.util.List;

/**
 * Created by Mahadi on 3/11/2018.
 */

public class AdapterCategoryProductsItemRecyclerView extends RecyclerView.Adapter<AdapterCategoryProductsItemRecyclerView.MyViewHolder> {

    Context context;
    List<String> itemname;
    List<String> price;
    List<String> itemImage;

    public AdapterCategoryProductsItemRecyclerView(Context context, List<String> itemname, List<String> price, List<String> itemImage) {
        this.context = context;
        this.itemname = itemname;
        this.price = price;
        this.itemImage = itemImage;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_spotlight_items, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.name.setText(itemname.get(position));
    }

    @Override
    public int getItemCount() {
        return itemname.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView priceTV;
        ImageView image;


        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name_contact);
            priceTV = (TextView) itemView.findViewById(R.id.ph_number);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}

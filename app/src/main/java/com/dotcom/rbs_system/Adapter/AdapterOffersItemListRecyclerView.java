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

public class AdapterOffersItemListRecyclerView extends RecyclerView.Adapter<AdapterOffersItemListRecyclerView.MyViewHolder> {

    Context context;
    List<String> itemname;
    List<String> price;
    List<String> itemImage;
    List<String> offer_status;
    List<String> offer_product_price;
    List<String> product_offer_msg;

    public AdapterOffersItemListRecyclerView(Context context, List<String> itemname, List<String> price, List<String> itemImage, List<String> offer_status, List<String> offer_product_price, List<String> product_offer_msg) {
        this.context = context;
        this.itemname = itemname;
        this.price = price;
        this.itemImage = itemImage;
        this.offer_status = offer_status;
        this.offer_product_price = offer_product_price;
        this.product_offer_msg = product_offer_msg;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.recyclerview_offers_items, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
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
        TextView offer_status;
        TextView offer_product_price;
        TextView product_offer_msg;

        //TODo check and remove problems
        //TODO dany is activity pa kam rehta ha
        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name_contact);
            priceTV = (TextView) itemView.findViewById(R.id.ph_number);
            offer_status = (TextView) itemView.findViewById(R.id.offer_status);
            offer_product_price = (TextView) itemView.findViewById(R.id.offer_product_price);
            product_offer_msg = (TextView) itemView.findViewById(R.id.product_offer_msg);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}

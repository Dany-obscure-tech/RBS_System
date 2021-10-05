package com.dotcom.rbs_system.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.RBS_productdetails;
import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterShopProductsShowcaseListRecyclerView extends RecyclerView.Adapter<AdapterShopProductsShowcaseListRecyclerView.MyViewHolder> {

    Context context;
    List<String> product_name;
    List<String> category;
    List<String> product_price;
    List<String> product_no_of_offers;
    List<String> image;
    List<String> key_idList;


    public AdapterShopProductsShowcaseListRecyclerView(Context context, List<String> product_name, List<String> category, List<String> image, List<String> product_price, List<String> product_no_of_offers, List<String> key_idList) {
        this.context = context;
        this.product_name = product_name;
        this.category = category;
        this.product_price = product_price;
        this.product_no_of_offers = product_no_of_offers;
        this.image = image;
        this.key_idList = key_idList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.recyclerview_shop_products_items, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.product_name.setText(product_name.get(position));
        holder.product_price.setText(product_price.get(position));
        holder.category.setText(category.get(position));
        holder.currency_textView.setText(Currency.getInstance().getCurrency());
        holder.product_no_of_offers.setText(product_no_of_offers.get(position));
        Picasso.get().load(image.get(position)).into(holder.image);

        holder.shop_items_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RBS_productdetails.class);
                intent.putExtra("PRODUCT_ID", key_idList.get(position));
                intent.putExtra("CATEGORY", category.get(position));
                ((Activity)context).startActivityForResult(intent,1212);
            }
        });


    }

    @Override
    public int getItemCount() {
        return product_name.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView product_name;
        TextView category;
        TextView product_price;
        TextView product_no_of_offers;
        TextView currency_textView;
        ImageView image;
        CardView shop_items_cardView;


        public MyViewHolder(View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.product_name);
            category = itemView.findViewById(R.id.category);
            product_price = itemView.findViewById(R.id.product_price);
            currency_textView = itemView.findViewById(R.id.currency_textView);
            product_no_of_offers = itemView.findViewById(R.id.product_no_of_offers);
            image = itemView.findViewById(R.id.image);
            shop_items_cardView = itemView.findViewById(R.id.shop_items_cardView);
        }
    }
}

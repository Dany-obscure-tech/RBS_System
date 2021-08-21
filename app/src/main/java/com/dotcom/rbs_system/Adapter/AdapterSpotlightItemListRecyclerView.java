package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.BuyLocal_productdetails;
import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mahadi on 3/11/2018.
 */

public class AdapterSpotlightItemListRecyclerView extends RecyclerView.Adapter<AdapterSpotlightItemListRecyclerView.MyViewHolder> {

    Context context;
    List<String> itemname;
    List<String> price;
    List<String> itemImage;
    List<String> key_idList;
    List<String> categoryList;
    List<String> shopkeeperList;

    public AdapterSpotlightItemListRecyclerView(Context context, List<String> itemname, List<String> price, List<String> itemImage, List<String> key_idList, List<String> categoryList, List<String> shopkeeperList) {
        this.context = context;
        this.itemname = itemname;
        this.price = price;
        this.itemImage = itemImage;
        this.key_idList = key_idList;
        this.categoryList = categoryList;
        this.shopkeeperList = shopkeeperList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.recyclerview_spotlight_items, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.name.setText(itemname.get(position));
        holder.priceTV.setText(Currency.getInstance().getCurrency() + " " + price.get(position));
        Picasso.get().load(itemImage.get(position)).into(holder.image);

        holder.spotlight_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BuyLocal_productdetails.class);
                intent.putExtra("PRODUCT_ID", key_idList.get(position));
                intent.putExtra("CATEGORY", categoryList.get(position));
                intent.putExtra("SHOPKEEPER_ID", shopkeeperList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemname.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView priceTV;
        ImageView image;
        CardView spotlight_cardView;


        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name_contact);
            priceTV = (TextView) itemView.findViewById(R.id.ph_number);
            image = (ImageView) itemView.findViewById(R.id.image);
            spotlight_cardView = (CardView) itemView.findViewById(R.id.spotlight_cardView);
        }
    }
}

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
import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mahadi on 3/11/2018.
 */

public class AdapterCategoryProductsItemRecyclerView extends RecyclerView.Adapter<AdapterCategoryProductsItemRecyclerView.MyViewHolder> {

    Context context;
    List<String> itemname;
    List<String> price;
    List<String> itemImage;
    List<String> itemkeyid;
    List<String> shopkeeperKeyId;
    List<String> itemCategory;

    public AdapterCategoryProductsItemRecyclerView(Context context, List<String> itemname, List<String> price, List<String> itemImage, List<String> itemkeyid, List<String> shopkeeperKeyId, List<String> itemCategory) {
        this.context = context;
        this.itemname = itemname;
        this.price = price;
        this.itemImage = itemImage;
        this.itemkeyid = itemkeyid;
        this.shopkeeperKeyId = shopkeeperKeyId;
        this.itemCategory = itemCategory;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_spotlight_items, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.name.setText(itemname.get(position));
        holder.priceTV.setText(price.get(position));
        Picasso.get().load(itemImage.get(position)).into(holder.image);

        holder.spotlight_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BuyLocal_productdetails.class);
                intent.putExtra("PRODUCT_ID",itemkeyid.get(position));
                intent.putExtra("CATEGORY",itemCategory.get(position));
                intent.putExtra("SHOPKEEPER_ID",shopkeeperKeyId.get(position));

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

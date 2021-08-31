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

public class AdapterShopProductsRecyclerView extends RecyclerView.Adapter<AdapterShopProductsRecyclerView.MyViewHolder> {

    Context context;
    List<String> itemname;
    List<String> price;
    List<String> itemImage;
    List<String> productID;
    List<String> productCategory;
    String shopkeeperID;

    public AdapterShopProductsRecyclerView(Context context, List<String> itemname, List<String> price, List<String> itemImage, List<String> productID, List<String> productCategory, String shopkeeperID) {
        this.context = context;
        this.itemname = itemname;
        this.price = price;
        this.itemImage = itemImage;
        this.productID = productID;
        this.productCategory = productCategory;
        this.shopkeeperID = shopkeeperID;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.recyclerview_spotlight_items, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.name.setText(itemname.get(position));
        holder.priceTV.setText(price.get(position));
        holder.currency.setText(Currency.getInstance().getCurrency());
        Picasso.get().load(itemImage.get(position)).into(holder.image);

        holder.spotlight_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BuyLocal_productdetails.class);
                intent.putExtra("PRODUCT_ID", productID.get(position));
                intent.putExtra("CATEGORY", productCategory.get(position));
                intent.putExtra("SHOPKEEPER_ID", shopkeeperID);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemname.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView spotlight_cardView;
        TextView name;
        TextView priceTV;
        TextView currency;
        ImageView image;

        //TODO is kay item ko center karna ha


        public MyViewHolder(View itemView) {
            super(itemView);

            spotlight_cardView = itemView.findViewById(R.id.spotlight_cardView);
            name = itemView.findViewById(R.id.name_contact);
            priceTV = itemView.findViewById(R.id.ph_number);
            currency = itemView.findViewById(R.id.currency);
            image = itemView.findViewById(R.id.image);
        }
    }
}

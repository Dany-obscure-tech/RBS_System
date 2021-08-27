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

public class AdapterOffersItemListRecyclerView extends RecyclerView.Adapter<AdapterOffersItemListRecyclerView.MyViewHolder> {

    Context context;
    List<String> itemname;
    List<String> price;
    List<String> itemImage;
    List<String> offer_status;
    List<String> offer_product_price;
    List<String> product_offer_msg;
    List<String> product_keyId;
    List<String> shopkeeperID;
    List<String> itemCategory;

    public AdapterOffersItemListRecyclerView(Context context, List<String> itemname, List<String> price, List<String> itemImage, List<String> offer_status, List<String> offer_product_price, List<String> product_offer_msg, List<String> product_keyId, List<String> shopkeeperID, List<String> itemCategory) {
        this.context = context;
        this.itemname = itemname;
        this.price = price;
        this.itemImage = itemImage;
        this.offer_status = offer_status;
        this.offer_product_price = offer_product_price;
        this.product_offer_msg = product_offer_msg;
        this.product_keyId = product_keyId;
        this.shopkeeperID = shopkeeperID;
        this.itemCategory = itemCategory;
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

        holder.item_name.setText(itemname.get(position));
        holder.item_price.setText(price.get(position));
        holder.offer_status.setText(offer_status.get(position));
        holder.offer_product_price.setText(offer_product_price.get(position));
        holder.currency_textView.setText(Currency.getInstance().getCurrency());

        Picasso.get().load(itemImage.get(position)).into(holder.image);

        holder.conversationItem_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BuyLocal_productdetails.class);

                intent.putExtra("PRODUCT_ID",product_keyId.get(position));
                intent.putExtra("CATEGORY",itemCategory.get(position));
                intent.putExtra("SHOPKEEPER_ID",shopkeeperID.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemname.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView conversationItem_cardView;
        TextView item_name;
        TextView item_price;
        TextView offer_status;
        TextView offer_product_price;
        TextView currency_textView;
        TextView product_offer_msg;
        ImageView image;

        //TODo check and remove problems
        //TODO dany is activity pa kam rehta ha
        public MyViewHolder(View itemView) {
            super(itemView);

            conversationItem_cardView = (CardView) itemView.findViewById(R.id.conversationItem_cardView);

            item_name = (TextView) itemView.findViewById(R.id.item_name);
            item_price = (TextView) itemView.findViewById(R.id.item_price);
            offer_status = (TextView) itemView.findViewById(R.id.offer_status);
            offer_product_price = (TextView) itemView.findViewById(R.id.offer_product_price);
            product_offer_msg = (TextView) itemView.findViewById(R.id.product_offer_msg);
            currency_textView = (TextView) itemView.findViewById(R.id.currency_textView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    
}

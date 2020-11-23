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

public class AdapterProductsOffersListRecyclerView extends  RecyclerView.Adapter<AdapterProductsOffersListRecyclerView.MyViewHolder> {

    Context context;
    List<String> product_name;
    List<String> currency;
    List<String> offered_price;
    List<String> product_orignal_price;
    List<String> product_offer_msg;
    List<String> product_image;
    List<String> profileImage;

    public AdapterProductsOffersListRecyclerView(Context context, List<String> product_name, List<String> currency, List<String> product_offer_msg, List<String> offered_price, List<String> product_orignal_price,List<String> product_image,List<String> profileImage) {
        this.context = context;
        this.product_name = product_name;
        this.currency = currency;
        this.offered_price = offered_price;
        this.product_orignal_price = product_orignal_price;
        this.product_offer_msg = product_offer_msg;
        this.product_image = product_image;
        this.profileImage = profileImage;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.recyclerview_shop_products_offers_items, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.product_name.setText(product_name.get(position));
//        holder.priceTV.setText(Currency.getInstance().getCurrency() + " " + price.get(position));
//        Picasso.get().load(itemImage.get(position)).into(holder.image);
//
//        holder.image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, BuyLocal_productdetails.class);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return product_name.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView product_name;
        TextView currency;
        TextView offered_price;
        TextView product_orignal_price;
        TextView product_offer_msg;
        ImageView product_image;
        ImageView profileImage;


        public MyViewHolder(View itemView) {
            super(itemView);

            product_name = (TextView) itemView.findViewById(R.id.product_name);
            currency = (TextView) itemView.findViewById(R.id.currency);
            offered_price = (TextView) itemView.findViewById(R.id.offered_price);
            product_orignal_price = (TextView) itemView.findViewById(R.id.product_orignal_price);
            product_offer_msg = (TextView) itemView.findViewById(R.id.product_offer_msg);
            product_image = (ImageView) itemView.findViewById(R.id.product_image);
            profileImage = (ImageView) itemView.findViewById(R.id.profileImage);
        }
    }
}

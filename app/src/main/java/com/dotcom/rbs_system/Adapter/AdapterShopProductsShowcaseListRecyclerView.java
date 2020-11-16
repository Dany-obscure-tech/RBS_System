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

public class AdapterShopProductsShowcaseListRecyclerView extends  RecyclerView.Adapter<AdapterShopProductsShowcaseListRecyclerView.MyViewHolder> {

    Context context;
    List<String> product_name;
    List<String> category;
    List<String> product_price;
    List<String> product_no_of_offers;
    List<String> image;

    public AdapterShopProductsShowcaseListRecyclerView(Context context, List<String> product_name, List<String> category, List<String> image, List<String> product_price, List<String> product_no_of_offers) {
        this.context = context;
        this.product_name = product_name;
        this.category = category;
        this.product_price = product_price;
        this.product_no_of_offers = product_no_of_offers;
        this.image = image;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.recyclerview_shop_products_items, parent, false);
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
        TextView category;
        TextView product_price;
        TextView product_no_of_offers;
        ImageView image;


        public MyViewHolder(View itemView) {
            super(itemView);

            product_name = (TextView) itemView.findViewById(R.id.product_name);
            category = (TextView) itemView.findViewById(R.id.category);
            product_price = (TextView) itemView.findViewById(R.id.product_price);
            product_no_of_offers = (TextView) itemView.findViewById(R.id.product_no_of_offers);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}

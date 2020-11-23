package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;

import java.util.List;

public class AdapterProductsOffersReceivedListRecyclerView extends  RecyclerView.Adapter<AdapterProductsOffersReceivedListRecyclerView.MyViewHolder> {

    Context context;
    List<String> customer_name;
    List<String> currency;
    List<String> offered_price;
    List<String> product_offer_msg;
    List<String> customerImage;
    List<String> date_textView;

    public AdapterProductsOffersReceivedListRecyclerView(Context context, List<String> customer_name, List<String> currency, List<String> product_offer_msg, List<String> offered_price, List<String> customerImage,List<String> date_textView) {
        this.context = context;
        this.customer_name = customer_name;
        this.currency = currency;
        this.offered_price = offered_price;
        this.product_offer_msg = product_offer_msg;
        this.customerImage = customerImage;
        this.date_textView = date_textView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.recyclerview_shop_products_received_offers_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.customer_name.setText(customer_name.get(position));
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
        return customer_name.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView customer_name;
        TextView currency;
        TextView offered_price;
        TextView product_offer_msg;
        Button accept_offer_btn;
        TextView date_textView;
        ImageView customerImage;


        public MyViewHolder(View itemView) {
            super(itemView);

            customer_name = (TextView) itemView.findViewById(R.id.customer_name);
            currency = (TextView) itemView.findViewById(R.id.currency);
            offered_price = (TextView) itemView.findViewById(R.id.offered_price);
            accept_offer_btn = (Button) itemView.findViewById(R.id.accept_offer_btn);
            date_textView = (TextView) itemView.findViewById(R.id.date_textView);
            product_offer_msg = (TextView) itemView.findViewById(R.id.product_offer_msg);
            customerImage = (ImageView) itemView.findViewById(R.id.customerImage);
        }
    }
}

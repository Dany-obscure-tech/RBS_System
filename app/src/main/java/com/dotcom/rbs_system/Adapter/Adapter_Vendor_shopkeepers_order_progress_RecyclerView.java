package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Vendor_shopkeepers_order_progress_RecyclerView extends RecyclerView.Adapter<Adapter_Vendor_shopkeepers_order_progress_RecyclerView.MyViewHolder> {
    Context context;
    List<String> accessory_name, rate_price, order_qty, image_url;

    public Adapter_Vendor_shopkeepers_order_progress_RecyclerView(Context context, List<String> accessory_name, List<String> rate_price, List<String> order_qty, List<String> image_url) {
        this.context = context;
        this.accessory_name = accessory_name;
        this.rate_price = rate_price;
        this.order_qty = order_qty;
        this.image_url = image_url;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.vendor_shopkeepers_order_progress_list_item, parent, false);
        Adapter_Vendor_shopkeepers_order_progress_RecyclerView.MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.accessory_name.setText(accessory_name.get(position));
        holder.rate_currency.setText(Currency.getInstance().getCurrency());
        holder.rate_price.setText(rate_price.get(position));
        holder.order_qty.setText(order_qty.get(position));
        Picasso.get().load(image_url.get(position)).into(holder.placeorder_item_pic_imageView);
    }

    @Override
    public int getItemCount() {

        return accessory_name.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView accessory_name, rate_currency, rate_price, order_qty;
        ImageView placeorder_item_pic_imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            accessory_name = itemView.findViewById(R.id.stockName_textView);
            rate_currency = itemView.findViewById(R.id.rate_currency);
            rate_price = itemView.findViewById(R.id.rate_price);
            order_qty = itemView.findViewById(R.id.order_qty);
            placeorder_item_pic_imageView = itemView.findViewById(R.id.placeorder_item_pic_imageView);

        }
    }
}

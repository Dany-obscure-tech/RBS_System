package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Buylocal_category_products;
import com.dotcom.rbs_system.R;
import com.dotcom.rbs_system.VendorShopkeepersShopReceipts;
import com.dotcom.rbs_system.Vendor_shopkeepers_orders_progress;

import java.util.List;

public class Adapter_Vendor_order_list_RecyclerView extends RecyclerView.Adapter<Adapter_Vendor_order_list_RecyclerView.MyViewHolder> {
    Context context;
    List<String> order_no_vendor;
    List<String> shop_name;
    List<String> price_currency;
    List<String> vendor_order_price;
    List<String> paid_currency;
    List<String> vendor_order_paid;
    List<String> balance_currency;
    List<String> vendor_order_status;

    public Adapter_Vendor_order_list_RecyclerView(Context context, List<String> order_no_vendor, List<String>shop_name, List<String> price_currency, List<String> vendor_order_price, List<String> paid_currency, List<String> vendor_order_paid,List<String> balance_currency,List<String> vendor_order_status) {
        this.context = context;
        this.order_no_vendor = order_no_vendor;
        this.shop_name = shop_name;
        this.price_currency = price_currency;
        this.vendor_order_price = vendor_order_price;
        this.paid_currency = paid_currency;
        this.vendor_order_paid = vendor_order_paid;
        this.balance_currency = balance_currency;
        this.vendor_order_status = vendor_order_status;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.vendor_orders_list_item, parent, false);
        Adapter_Vendor_order_list_RecyclerView.MyViewHolder myViewHolder = new Adapter_Vendor_order_list_RecyclerView.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.shop_name.setText(shop_name.get(position));
        holder.order_no_vendor.setText(order_no_vendor.get(position));
        holder.order_no_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Vendor_shopkeepers_orders_progress.class);
                intent.putExtra("Shop_Name",shop_name.get(position));
                context.startActivity(intent);
            }
        });
        holder.shop_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VendorShopkeepersShopReceipts.class);
                intent.putExtra("Shop_Name",shop_name.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return shop_name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView order_no_vendor,shop_name,price_currency,vendor_order_price,paid_currency,vendor_order_paid,balance_currency,vendor_order_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            order_no_vendor = (TextView) itemView.findViewById(R.id.order_no_vendor);
            shop_name = (TextView) itemView.findViewById(R.id.shop_name);
            price_currency = (TextView) itemView.findViewById(R.id.price_currency);
            vendor_order_price = (TextView) itemView.findViewById(R.id.vendor_order_price);
            paid_currency = (TextView) itemView.findViewById(R.id.paid_currency);
            vendor_order_paid = (TextView) itemView.findViewById(R.id.vendor_order_paid);
            balance_currency = (TextView) itemView.findViewById(R.id.balance_currency);
            vendor_order_status = (TextView) itemView.findViewById(R.id.vendor_order_status);


        }
    }
}

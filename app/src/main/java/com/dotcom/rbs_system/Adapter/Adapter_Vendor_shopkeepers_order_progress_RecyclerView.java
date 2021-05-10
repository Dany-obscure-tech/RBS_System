package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;

import java.util.List;

public class Adapter_Vendor_shopkeepers_order_progress_RecyclerView extends RecyclerView.Adapter<Adapter_Vendor_shopkeepers_order_progress_RecyclerView.MyViewHolder> {
    Context context;
    List<String> sr_no_order,accessory_name,rate_currency,rate_price,order_qty;

    public Adapter_Vendor_shopkeepers_order_progress_RecyclerView(Context context, List<String> sr_no_order, List<String> accessory_name, List<String> rate_currency, List<String> rate_price, List<String> order_qty) {
        this.context = context;
        this.sr_no_order = sr_no_order;
        this.accessory_name = accessory_name;
        this.rate_currency = rate_currency;
        this.rate_price = rate_price;
        this.order_qty = order_qty;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.vendor_shopkeepers_order_progress_list_item, parent, false);
        Adapter_Vendor_shopkeepers_order_progress_RecyclerView.MyViewHolder myViewHolder = new Adapter_Vendor_shopkeepers_order_progress_RecyclerView.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.accessory_name.setText(accessory_name.get(position));
    }

    @Override
    public int getItemCount() {

        return accessory_name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sr_no_order,accessory_name,rate_currency,rate_price,order_qty,total_price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sr_no_order = (TextView) itemView.findViewById(R.id.sr_no_order);
            accessory_name = (TextView) itemView.findViewById(R.id.stockName_textView);
            rate_currency = (TextView) itemView.findViewById(R.id.rate_currency);
            rate_price = (TextView) itemView.findViewById(R.id.rate_price);
            order_qty = (TextView) itemView.findViewById(R.id.order_qty);

        }
    }
}

package com.dotcom.rbs_system.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Customer_history;
import com.dotcom.rbs_system.R;
import com.dotcom.rbs_system.Shopkeeper_details;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterItemHistoryListRecyclerView extends RecyclerView.Adapter<AdapterItemHistoryListRecyclerView.ViewHolder> {
    Context context;
    List<String> status_textView;
    List<String> dateList;
    List<String> shopkeeper_status;
    List<String> shopkeeper_name_textview;
    List<String> customer_status;
    List<String> customer_name_textview;
    List<String> shopkeeperImage_imageView_list;
    List<String> customerImage_imageView_list;
    List<String> shopkeeper_key_id;
    List<String> customer_key_id;

    public AdapterItemHistoryListRecyclerView(Context context, List<String> status_textView, List<String> shopkeeper_status, List<String> shopkeeper_name_textview, List<String> customer_status, List<String> customer_name_textview, List<String> shopkeeperImage_imageView_list, List<String> customerImage_imageView_list, List<String> dateList, List<String> shopkeeper_key_id, List<String> customer_key_id) {
        this.context = context;
        this.status_textView = status_textView;
        this.shopkeeper_status = shopkeeper_status;
        this.shopkeeper_name_textview = shopkeeper_name_textview;
        this.customer_status = customer_status;
        this.customer_name_textview = customer_name_textview;
        this.shopkeeperImage_imageView_list = shopkeeperImage_imageView_list;
        this.customerImage_imageView_list = customerImage_imageView_list;
        this.dateList = dateList;
        this.shopkeeper_key_id = shopkeeper_key_id;
        this.customer_key_id = customer_key_id;
    }

    @NonNull
    @Override
    public AdapterItemHistoryListRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_itemhistorylist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItemHistoryListRecyclerView.ViewHolder holder, final int position) {

        holder.status_textView.setText(status_textView.get(position));
        holder.shopkeeper_name_textview.setText(shopkeeper_name_textview.get(position));
        holder.shopkeeper_status.setText(shopkeeper_status.get(position));
        holder.customer_status.setText(customer_status.get(position));
        holder.customer_name_textview.setText(customer_name_textview.get(position));
        holder.date_textView.setText(dateList.get(position));
        Picasso.get().load(shopkeeperImage_imageView_list.get(position)).into(holder.shopkeeperImage_imageView);
        Picasso.get().load(customerImage_imageView_list.get(position)).into(holder.customerImage_imageView);
        holder.shopkeeper_area_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Shopkeeper_details.class);
                intent.putExtra("SHOPKEEPER_ID",shopkeeper_key_id.get(position));
                context.startActivity(intent);
            }
        });
        holder.customer_area_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Customer_history.class);
                intent.putExtra("CUSTOMER_ID",customer_key_id.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView date_textView, status_textView, shopkeeper_status, shopkeeper_name_textview, customer_status, customer_name_textview;
        ImageView shopkeeperImage_imageView, customerImage_imageView;
        LinearLayout shopkeeper_area_linearlayout, customer_area_linearlayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date_textView = itemView.findViewById(R.id.date_textView);
            status_textView = itemView.findViewById(R.id.status_textView);
            shopkeeper_status = itemView.findViewById(R.id.shopkeeper_status);
            shopkeeper_name_textview = itemView.findViewById(R.id.shopkeeper_name_textview);
            customer_status = itemView.findViewById(R.id.customer_status);
            customer_name_textview = itemView.findViewById(R.id.customer_name_textview);
            shopkeeperImage_imageView = itemView.findViewById(R.id.shopkeeperImage_imageView);
            customerImage_imageView = itemView.findViewById(R.id.customerImage_imageView);
            shopkeeper_area_linearlayout = itemView.findViewById(R.id.shopkeeper_area_linearlayout);
            customer_area_linearlayout = itemView.findViewById(R.id.customer_area_linearlayout);

        }
    }
}

package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;
import com.dotcom.rbs_system.Shopkeeper_details;

import java.util.List;

public class AdapterBuylocalCustomerHistoryListRecyclerView extends RecyclerView.Adapter<AdapterBuylocalCustomerHistoryListRecyclerView.ViewHolder> {
    Context context;
    List<String> dateList, item_category_textview, itemImageView, itemKeyId, item_name_textview, serial_no_textview, shopkeeperImage_imageView_list, shopkeeper_key_id, shopkeeper_name_textview;

    public AdapterBuylocalCustomerHistoryListRecyclerView(Context context, List<String> dateList, List<String> item_category_textview, List<String> itemImageView, List<String> itemKeyId, List<String> item_name_textview, List<String> serial_no_textview, List<String> shopkeeperImage_imageView_list, List<String> shopkeeper_key_id, List<String> shopkeeper_name_textview) {
        this.context = context;
        this.dateList = dateList;
        this.item_category_textview = item_category_textview;
        this.itemImageView = itemImageView;
        this.itemKeyId = itemKeyId;
        this.item_name_textview = item_name_textview;
        this.serial_no_textview = serial_no_textview;
        this.shopkeeperImage_imageView_list = shopkeeperImage_imageView_list;
        this.shopkeeper_key_id = shopkeeper_key_id;
        this.shopkeeper_name_textview = shopkeeper_name_textview;
    }

    @NonNull
    @Override
    public AdapterBuylocalCustomerHistoryListRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_buylocal_customerhistorylist_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBuylocalCustomerHistoryListRecyclerView.ViewHolder holder, final int position) {
//        holder.shopkeeper_name_textview.setText(shopkeeper_name_textview.get(position));
        holder.item_name_textview.setText(item_name_textview.get(position));
//        holder.serial_no_textview.setText(serial_no_textview.get(position));
//        holder.date_textView.setText(dateList.get(position));
//        Picasso.get().load(shopkeeperImage_imageView_list.get(position)).into(holder.shopkeeperImage_imageView);
//        Picasso.get().load(itemImageView.get(position)).into(holder.itemImage_imageView);
        holder.shopkeeper_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Shopkeeper_details.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return item_name_textview.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout shopkeeper_linearlayout, item_detail_linearlayout;
        TextView date_textView, shopkeeper_name_textview, item_name_textview, serial_no_textview;
        ImageView shopkeeperImage_imageView, itemImage_imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date_textView = itemView.findViewById(R.id.date_textView);
            shopkeeper_name_textview = itemView.findViewById(R.id.shopkeeper_name_textview);
            item_name_textview = itemView.findViewById(R.id.item_name_textview);
            serial_no_textview = itemView.findViewById(R.id.serial_no_textview);
            shopkeeperImage_imageView = itemView.findViewById(R.id.shopkeeperImage_imageView);
            itemImage_imageView = itemView.findViewById(R.id.itemImage_imageView);
            shopkeeper_linearlayout = itemView.findViewById(R.id.shopkeeper_linearlayout);
            item_detail_linearlayout = itemView.findViewById(R.id.item_detail_linearlayout);

        }
    }
}

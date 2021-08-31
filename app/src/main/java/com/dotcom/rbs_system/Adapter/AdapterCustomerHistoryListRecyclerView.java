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

import com.dotcom.rbs_system.Item_history;
import com.dotcom.rbs_system.R;
import com.dotcom.rbs_system.Shopkeeper_details;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCustomerHistoryListRecyclerView extends RecyclerView.Adapter<AdapterCustomerHistoryListRecyclerView.ViewHolder> {
    Context context;
    List<String> dateList, item_category_textview, itemImageView, itemKeyId, item_name_textview, serial_no_textview, status_textView, shopkeeperImage_imageView_list, shopkeeper_key_id, shopkeeper_name_textview;

    public AdapterCustomerHistoryListRecyclerView(Context context, List<String> dateList, List<String> item_category_textview, List<String> itemImageView, List<String> itemKeyId, List<String> item_name_textview, List<String> serial_no_textview, List<String> status_textView, List<String> shopkeeperImage_imageView_list, List<String> shopkeeper_key_id, List<String> shopkeeper_name_textview) {
        this.context = context;
        this.dateList = dateList;
        this.item_category_textview = item_category_textview;
        this.itemImageView = itemImageView;
        this.itemKeyId = itemKeyId;
        this.item_name_textview = item_name_textview;
        this.serial_no_textview = serial_no_textview;
        this.status_textView = status_textView;
        this.shopkeeperImage_imageView_list = shopkeeperImage_imageView_list;
        this.shopkeeper_key_id = shopkeeper_key_id;
        this.shopkeeper_name_textview = shopkeeper_name_textview;
    }

    @NonNull
    @Override
    public AdapterCustomerHistoryListRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_customerhistorylist_item, parent, false));

    }
    @Override
    public void onBindViewHolder(@NonNull AdapterCustomerHistoryListRecyclerView.ViewHolder holder, final int position) {
        holder.shopkeeper_name_textview.setText(shopkeeper_name_textview.get(position));
        holder.item_name_textview.setText(item_name_textview.get(position));
        holder.status_textView.setText(status_textView.get(position));
        holder.serial_no_textview.setText(serial_no_textview.get(position));
        holder.date_textView.setText(dateList.get(position));
        Picasso.get().load(shopkeeperImage_imageView_list.get(position)).into(holder.shopkeeperImage_imageView);
        Picasso.get().load(itemImageView.get(position)).into(holder.itemImage_imageView);
        holder.shopkeeper_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Shopkeeper_details.class);
                intent.putExtra("SHOPKEEPER_ID",shopkeeper_key_id.get(position));
                context.startActivity(intent);
            }
        });
        holder.item_detail_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Item_history.class);
                intent.putExtra("ITEM_ID",itemKeyId.get(position));
                intent.putExtra("ITEM_CATEGORY",item_category_textview.get(position));
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return shopkeeper_name_textview.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout shopkeeper_linearlayout, item_detail_linearlayout;
        TextView date_textView, shopkeeper_name_textview, item_name_textview, serial_no_textview, status_textView;
        ImageView shopkeeperImage_imageView, itemImage_imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date_textView = itemView.findViewById(R.id.date_textView);
            shopkeeper_name_textview = itemView.findViewById(R.id.shopkeeper_name_textview);
            item_name_textview = itemView.findViewById(R.id.item_name_textview);
            status_textView = itemView.findViewById(R.id.status_textView);
            serial_no_textview = itemView.findViewById(R.id.serial_no_textview);
            shopkeeperImage_imageView = itemView.findViewById(R.id.shopkeeperImage_imageView);
            itemImage_imageView = itemView.findViewById(R.id.itemImage_imageView);
            shopkeeper_linearlayout = itemView.findViewById(R.id.shopkeeper_linearlayout);
            item_detail_linearlayout = itemView.findViewById(R.id.item_detail_linearlayout);

        }
    }
}

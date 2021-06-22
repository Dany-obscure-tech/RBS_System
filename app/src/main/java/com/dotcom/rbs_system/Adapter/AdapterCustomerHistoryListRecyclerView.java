package com.dotcom.rbs_system.Adapter;

import android.app.Activity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCustomerHistoryListRecyclerView extends RecyclerView.Adapter<AdapterCustomerHistoryListRecyclerView.ViewHolder> {
    Context context;
    List<String> customer_key_id,shopkeeper_name_textview, item_name_textview, dateList,serial_no_textview,status_textView,shopkeeperImage_imageView_list,customerImage_imageView_list,shopkeeper_key_id;
    Activity activity;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Listed_faults");

    public AdapterCustomerHistoryListRecyclerView(Context context, List<String> shopkeeper_name_textview, List<String> item_name_textview,List<String> serial_no_textview,List<String> status_textView, List<String> shopkeeperImage_imageView_list,List<String> customerImage_imageView_list, List<String> dateList,List<String> shopkeeper_key_id,List<String> customer_key_id) {
        this.context = context;
        this.shopkeeper_name_textview = shopkeeper_name_textview;
        this.item_name_textview = item_name_textview;
        this.serial_no_textview = serial_no_textview;
        this.status_textView = status_textView;
        this.shopkeeperImage_imageView_list = shopkeeperImage_imageView_list;
        this.customerImage_imageView_list = customerImage_imageView_list;
        this.dateList = dateList;
        this.shopkeeper_key_id = shopkeeper_key_id;
        this.customer_key_id = customer_key_id;

    }

    @NonNull
    @Override
    public AdapterCustomerHistoryListRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_customerhistorylist_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCustomerHistoryListRecyclerView.ViewHolder holder, final int position) {
        holder.shopkeeper_name_textview.setText(shopkeeper_name_textview.get(position));
        holder.item_name_textview.setText(item_name_textview.get(position));
        holder.status_textView.setText(status_textView.get(position));
        holder.serial_no_textview.setText(serial_no_textview.get(position));
        holder.date_textView.setText(dateList.get(position));
        Picasso.get().load(shopkeeperImage_imageView_list.get(position)).into(holder.shopkeeperImage_imageView);
        Picasso.get().load(customerImage_imageView_list.get(position)).into(holder.customerImage_imageView);
        holder.shopkeeper_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Shopkeeper_details.class);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return shopkeeper_name_textview.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

            LinearLayout shopkeeper_linearlayout;
            TextView date_textView,shopkeeper_name_textview, item_name_textview,serial_no_textview,status_textView;
            ImageView shopkeeperImage_imageView,customerImage_imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date_textView = (TextView)itemView.findViewById(R.id.date_textView);
            shopkeeper_name_textview = (TextView)itemView.findViewById(R.id.shopkeeper_name_textview);
            item_name_textview = (TextView)itemView.findViewById(R.id.item_name_textview);
            status_textView = (TextView)itemView.findViewById(R.id.status_textView);
            serial_no_textview = (TextView)itemView.findViewById(R.id.serial_no_textview);
            shopkeeper_linearlayout = (LinearLayout) itemView.findViewById(R.id.shopkeeper_linearlayout);
            shopkeeperImage_imageView = (ImageView) itemView.findViewById(R.id.shopkeeperImage_imageView);
            customerImage_imageView = (ImageView) itemView.findViewById(R.id.customerImage_imageView);

        }
    }
}

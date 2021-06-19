package com.dotcom.rbs_system.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;
import com.dotcom.rbs_system.Shopkeeper_details;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterCustomerHistoryListRecyclerView extends RecyclerView.Adapter<AdapterCustomerHistoryListRecyclerView.ViewHolder> {
    Context context;
    List<String> shopkeeper_name_textview, item_name_textview, dateList,serial_no_textview,status_textView;
    Activity activity;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Listed_faults");

    public AdapterCustomerHistoryListRecyclerView(Context context, List<String> shopkeeper_name_textview, List<String> item_name_textview,List<String> serial_no_textview,List<String> status_textView, List<String> dateList) {
        this.context = context;
        this.shopkeeper_name_textview = shopkeeper_name_textview;
        this.item_name_textview = item_name_textview;
        this.serial_no_textview = serial_no_textview;
        this.status_textView = status_textView;
        this.dateList = dateList;

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date_textView = (TextView)itemView.findViewById(R.id.date_textView);
            shopkeeper_name_textview = (TextView)itemView.findViewById(R.id.shopkeeper_name_textview);
            item_name_textview = (TextView)itemView.findViewById(R.id.item_name_textview);
            status_textView = (TextView)itemView.findViewById(R.id.status_textView);
            serial_no_textview = (TextView)itemView.findViewById(R.id.serial_no_textview);
            shopkeeper_linearlayout = (LinearLayout) itemView.findViewById(R.id.shopkeeper_linearlayout);

        }
    }
}

package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;
import com.dotcom.rbs_system.VendorShopkeepersShopReceipts;
import com.dotcom.rbs_system.Vendor_shopkeepers_orders_progress;

import java.util.List;

public class Adapter_Vendor_users_list_RecyclerView extends RecyclerView.Adapter<Adapter_Vendor_users_list_RecyclerView.MyViewHolder> {
    Context context;
    List<String> user_category;
    List<String> user_name;
    List<String> user_phone_number;
    List<String> user_email;

    public Adapter_Vendor_users_list_RecyclerView(Context context, List<String> user_category, List<String> user_name, List<String> user_phone_number, List<String> user_email) {
        this.context = context;
        this.user_category = user_category;
        this.user_name = user_name;
        this.user_phone_number = user_phone_number;
        this.user_email = user_email;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.vendor_users_list_item, parent, false);
        Adapter_Vendor_users_list_RecyclerView.MyViewHolder myViewHolder = new Adapter_Vendor_users_list_RecyclerView.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.user_name.setText(user_name.get(position));
        holder.user_name.setText(user_name.get(position));
    }

    @Override
    public int getItemCount() {

        return user_name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView user_category, user_name, user_phone_number, user_email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            user_category = (TextView) itemView.findViewById(R.id.user_category);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_phone_number = (TextView) itemView.findViewById(R.id.user_phone_number);
            user_email = (TextView) itemView.findViewById(R.id.user_email);

        }
    }
}

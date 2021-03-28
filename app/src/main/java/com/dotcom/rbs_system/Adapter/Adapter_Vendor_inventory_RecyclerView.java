package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;

import java.util.List;

public class Adapter_Vendor_inventory_RecyclerView extends RecyclerView.Adapter<Adapter_Vendor_inventory_RecyclerView.MyViewHolder> {
    Context context;
    List<String> s_no_vendor_inventory;
    List<String> accessory_name;
    List<String> accesory_vendor_inventory;
    List<String> price_vendor_inventory;
    List<String> quantity_vendor_inventory;
    List<String> image_vendor_inventory;

    public Adapter_Vendor_inventory_RecyclerView(Context context, List<String> s_no_vendor_inventory,List<String>accessory_name, List<String> accesory_vendor_inventory, List<String> price_vendor_inventory,List<String> quantity_vendor_inventory,List<String> image_vendor_inventory) {
        this.context = context;
        this.s_no_vendor_inventory = s_no_vendor_inventory;
        this.accessory_name = accessory_name;
        this.accesory_vendor_inventory = accesory_vendor_inventory;
        this.price_vendor_inventory = price_vendor_inventory;
        this.quantity_vendor_inventory = quantity_vendor_inventory;
        this.image_vendor_inventory = image_vendor_inventory;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.vendormainscreen_item, parent, false);
        Adapter_Vendor_inventory_RecyclerView.MyViewHolder myViewHolder = new Adapter_Vendor_inventory_RecyclerView.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.accesory_vendor_inventory.setText(accesory_vendor_inventory.get(position));
    }

    @Override
    public int getItemCount() {

        return accesory_vendor_inventory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image_vendor_inventory;
        TextView s_no_vendor_inventory,accessory_name,accesory_vendor_inventory,price_vendor_inventory,quantity_vendor_inventory;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            s_no_vendor_inventory = (TextView) itemView.findViewById(R.id.s_no_vendor_inventory);
            accessory_name = (TextView) itemView.findViewById(R.id.accessory_name);
            accesory_vendor_inventory = (TextView) itemView.findViewById(R.id.accesory_vendor_inventory);
            price_vendor_inventory = (TextView) itemView.findViewById(R.id.price_vendor_inventory);
            quantity_vendor_inventory = (TextView) itemView.findViewById(R.id.quantity_vendor_inventory);
            image_vendor_inventory = (ImageView) itemView.findViewById(R.id.image_vendor_inventory);


        }
    }
}

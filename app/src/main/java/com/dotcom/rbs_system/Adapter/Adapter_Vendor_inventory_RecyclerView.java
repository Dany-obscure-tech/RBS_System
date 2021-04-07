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
    List<String> stockSNo_list;
    List<String> stockName_list;
    List<String> stockCategory_list;
    List<String> stockPrice_list;
    List<String> stockQuantity_list;
    List<String> stockImageUrl_list;

    public Adapter_Vendor_inventory_RecyclerView(Context context, List<String> stockSNo_list, List<String> stockName_list, List<String> stockCategory_list, List<String> stockPrice_list, List<String> quantity_vendor_inventory, List<String> stockImageUrl_list) {
        this.context = context;
        this.stockSNo_list = stockSNo_list;
        this.stockName_list = stockName_list;
        this.stockCategory_list = stockCategory_list;
        this.stockPrice_list = stockPrice_list;
        this.stockQuantity_list = quantity_vendor_inventory;
        this.stockImageUrl_list = stockImageUrl_list;
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
        holder.stockCategory_textView.setText(stockCategory_list.get(position));
    }

    @Override
    public int getItemCount() {

        return stockCategory_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView stockImage_imageView;
        TextView stockSNo_textView, stockName_textView, stockCategory_textView, stockPrice_textView, stockQuantity_textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            stockSNo_textView = (TextView) itemView.findViewById(R.id.stockSNo_textView);
            stockName_textView = (TextView) itemView.findViewById(R.id.stockName_textView);
            stockCategory_textView = (TextView) itemView.findViewById(R.id.stockCategory_textView);
            stockPrice_textView = (TextView) itemView.findViewById(R.id.stockPrice_textView);
            stockQuantity_textView = (TextView) itemView.findViewById(R.id.stockQuantity_textView);
            
            stockImage_imageView = (ImageView) itemView.findViewById(R.id.stockImage_imageView);


        }
    }
}

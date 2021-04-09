package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_RBS_Vendor_inventory_RecyclerView extends RecyclerView.Adapter<Adapter_RBS_Vendor_inventory_RecyclerView.MyViewHolder> {
    Context context;
    List<String> vendor_stock_category_textView;
    List<String> vendor_stockName_textView;
    List<String> vendor_stock_currency_textview;
    List<String> vendor_stock_price_textview;
    List<String> vendor_stock_quantity_textView;
    List<String> vendor_stock_imageView;
    List<String> vendor_stock_selection_checkbox;

    public Adapter_RBS_Vendor_inventory_RecyclerView(Context context, List<String> vendor_stock_category_textView, List<String> vendor_stockName_textView, List<String> vendor_stock_currency_textview, List<String> vendor_stock_price_textview, List<String> vendor_stock_quantity_textView, List<String> vendor_stock_imageView,List<String> vendor_stock_selection_checkbox) {
        this.context = context;
        this.vendor_stock_category_textView = vendor_stock_category_textView;
        this.vendor_stockName_textView = vendor_stockName_textView;
        this.vendor_stock_currency_textview = vendor_stock_currency_textview;
        this.vendor_stock_price_textview = vendor_stock_price_textview;
        this.vendor_stock_quantity_textView = vendor_stock_quantity_textView;
        this.vendor_stock_imageView = vendor_stock_imageView;
        this.vendor_stock_selection_checkbox = vendor_stock_selection_checkbox;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.rbs_vendormainscreen_item, parent, false);
        Adapter_RBS_Vendor_inventory_RecyclerView.MyViewHolder myViewHolder = new Adapter_RBS_Vendor_inventory_RecyclerView.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.vendor_stockName_textView.setText(vendor_stockName_textView.get(position));
        Picasso.get().load(vendor_stock_imageView.get(position)).into(holder.vendor_stock_imageView);
    }

    @Override
    public int getItemCount() {

        return vendor_stockName_textView.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView vendor_stock_imageView;
        CheckBox vendor_stock_selection_checkbox;
        TextView vendor_stock_category_textView, vendor_stockName_textView, vendor_stock_currency_textview, vendor_stock_price_textview, vendor_stock_quantity_textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vendor_stock_category_textView = (TextView) itemView.findViewById(R.id.vendor_stock_category_textView);
            vendor_stockName_textView = (TextView) itemView.findViewById(R.id.vendor_stockName_textView);
            vendor_stock_currency_textview = (TextView) itemView.findViewById(R.id.stockCategory_textView);
            vendor_stock_price_textview = (TextView) itemView.findViewById(R.id.vendor_stock_price_textview);
            vendor_stock_quantity_textView = (TextView) itemView.findViewById(R.id.vendor_stock_quantity_textView);
            vendor_stock_imageView = (ImageView) itemView.findViewById(R.id.vendor_stock_imageView);
            vendor_stock_selection_checkbox = (CheckBox) itemView.findViewById(R.id.vendor_stock_selection_checkbox);


        }
    }
}

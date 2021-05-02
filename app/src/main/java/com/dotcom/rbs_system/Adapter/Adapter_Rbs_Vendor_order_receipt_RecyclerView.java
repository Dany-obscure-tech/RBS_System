package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;

import java.util.List;

public class Adapter_Rbs_Vendor_order_receipt_RecyclerView extends RecyclerView.Adapter<Adapter_Rbs_Vendor_order_receipt_RecyclerView.MyViewHolder> {
    Context context;
    List<String> placeorder_item_name_textView, placeorder_item_category_textView, placeorder_item_currency_textview, place_order_price_textview, place_order_quantity_textView, requiredQuantity_editText;


    public Adapter_Rbs_Vendor_order_receipt_RecyclerView(Context context, List<String> placeorder_item_name_textView, List<String> placeorder_item_category_textView, List<String> placeorder_item_currency_textview, List<String> place_order_price_textview, List<String> place_order_quantity_textView, List<String> requiredQuantity_editText) {
        this.context = context;
        this.placeorder_item_name_textView = placeorder_item_name_textView;
        this.placeorder_item_category_textView = placeorder_item_category_textView;
        this.placeorder_item_currency_textview = placeorder_item_currency_textview;
        this.place_order_price_textview = place_order_price_textview;
        this.place_order_quantity_textView = place_order_quantity_textView;
        this.requiredQuantity_editText = requiredQuantity_editText;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.rbs_vendor_order_receipt_item, parent, false);
        Adapter_Rbs_Vendor_order_receipt_RecyclerView.MyViewHolder myViewHolder = new Adapter_Rbs_Vendor_order_receipt_RecyclerView.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.placeorder_item_name_textView.setText(placeorder_item_name_textView.get(position));

    }

    @Override
    public int getItemCount() {

        return placeorder_item_name_textView.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView placeorder_item_name_textView, placeorder_item_category_textView, placeorder_item_currency_textview, place_order_price_textview, place_order_quantity_textView;
        EditText requiredQuantity_editText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            placeorder_item_name_textView = (TextView) itemView.findViewById(R.id.placeorder_item_name_textView);
            placeorder_item_category_textView = (TextView) itemView.findViewById(R.id.placeorder_item_category_textView);
            placeorder_item_currency_textview = (TextView) itemView.findViewById(R.id.placeorder_item_currency_textview);
            place_order_price_textview = (TextView) itemView.findViewById(R.id.place_order_price_textview);
            place_order_quantity_textView = (TextView) itemView.findViewById(R.id.place_order_quantity_textView);
            requiredQuantity_editText = (EditText) itemView.findViewById(R.id.requiredQuantity_editText);

        }
    }
}

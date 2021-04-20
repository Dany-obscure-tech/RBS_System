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

public class Adapter_RBS_Vendor_placeorder_RecyclerView extends RecyclerView.Adapter<Adapter_RBS_Vendor_placeorder_RecyclerView.MyViewHolder> {
    Context context;
    List<String> placeorder_item_name_textView;
    List<String> placeorder_item_category_textView;
    List<String> placeorder_item_currency_textview;
    List<String> place_order_price_edittext;
    List<String> place_order_quantity_editText;
    List<String> placeorder_item_pic_imageView;


    public Adapter_RBS_Vendor_placeorder_RecyclerView(Context context, List<String> placeorder_item_name_textView, List<String> placeorder_item_category_textView, List<String> placeorder_item_currency_textview, List<String> place_order_price_edittext, List<String> place_order_quantity_editText, List<String> placeorder_item_pic_imageView) {
        this.context = context;
        this.placeorder_item_name_textView = placeorder_item_name_textView;
        this.placeorder_item_category_textView = placeorder_item_category_textView;
        this.placeorder_item_currency_textview = placeorder_item_currency_textview;
        this.place_order_price_edittext = place_order_price_edittext;
        this.place_order_quantity_editText = place_order_quantity_editText;
        this.placeorder_item_pic_imageView = placeorder_item_pic_imageView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.rbs_vendor_placeorder_item, parent, false);
        Adapter_RBS_Vendor_placeorder_RecyclerView.MyViewHolder myViewHolder = new Adapter_RBS_Vendor_placeorder_RecyclerView.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.placeorder_item_name_textView.setText(placeorder_item_name_textView.get(position));
        holder.placeorder_item_category_textView.setText(placeorder_item_category_textView.get(position));
        holder.placeorder_item_currency_textview.setText(placeorder_item_currency_textview.get(position));
        holder.place_order_price_edittext.setText(place_order_price_edittext.get(position));
        holder.place_order_quantity_editText.setText(place_order_quantity_editText.get(position));
        Picasso.get().load(placeorder_item_pic_imageView.get(position)).into(holder.placeorder_item_pic_imageView);
    }

    @Override
    public int getItemCount() {

        return placeorder_item_name_textView.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView placeorder_item_pic_imageView;
        TextView placeorder_item_name_textView, placeorder_item_category_textView, placeorder_item_currency_textview, place_order_price_edittext, place_order_quantity_editText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            placeorder_item_name_textView = (TextView) itemView.findViewById(R.id.placeorder_item_name_textView);
            placeorder_item_category_textView = (TextView) itemView.findViewById(R.id.placeorder_item_category_textView);
            placeorder_item_currency_textview = (TextView) itemView.findViewById(R.id.placeorder_item_currency_textview);
            place_order_price_edittext = (TextView) itemView.findViewById(R.id.place_order_price_edittext);
            place_order_quantity_editText = (TextView) itemView.findViewById(R.id.place_order_quantity_editText);
            placeorder_item_pic_imageView = (ImageView) itemView.findViewById(R.id.placeorder_item_pic_imageView);


        }
    }
}

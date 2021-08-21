package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_RBS_Vendor_inventory_RecyclerView extends RecyclerView.Adapter<Adapter_RBS_Vendor_inventory_RecyclerView.MyViewHolder> {
    Context context;
    List<String> vendor_stock_category_textView;
    List<String> vendor_stockName_textView;
    List<String> vendor_stock_price_textview;
    List<String> vendor_stock_quantity_textView;
    List<String> vendor_stock_imageView;
    List<Integer> vendor_stock_selection_checkbox;


    public Adapter_RBS_Vendor_inventory_RecyclerView(Context context, List<String> vendor_stock_category_textView, List<String> vendor_stockName_textView, List<String> vendor_stock_price_textview, List<String> vendor_stock_quantity_textView, List<String> vendor_stock_imageView) {
        this.context = context;
        this.vendor_stock_category_textView = vendor_stock_category_textView;
        this.vendor_stockName_textView = vendor_stockName_textView;
        this.vendor_stock_price_textview = vendor_stock_price_textview;
        this.vendor_stock_quantity_textView = vendor_stock_quantity_textView;
        this.vendor_stock_imageView = vendor_stock_imageView;
        vendor_stock_selection_checkbox = new ArrayList<>();
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.vendor_stock_category_textView.setText(vendor_stock_category_textView.get(position));
        holder.vendor_stockName_textView.setText(vendor_stockName_textView.get(position));
        holder.vendor_stock_currency_textview.setText(Currency.getInstance().getCurrency());
        holder.vendor_stock_price_textview.setText(vendor_stock_price_textview.get(position));
        holder.vendor_stock_quantity_textView.setText(vendor_stock_quantity_textView.get(position));
        Picasso.get().load(vendor_stock_imageView.get(position)).into(holder.vendor_stock_imageView);

        holder.vendor_stock_selection_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    vendor_stock_selection_checkbox.add(position);
                    Toast.makeText(context, String.valueOf(vendor_stock_selection_checkbox.size()), Toast.LENGTH_SHORT).show();
                }
                if (!compoundButton.isChecked()) {

                    for (int i = 0; i < vendor_stock_selection_checkbox.size(); i++) {
                        if (vendor_stock_selection_checkbox.get(i) == position) {
                            vendor_stock_selection_checkbox.remove(i);
                        }
                    }
                    Toast.makeText(context, String.valueOf(vendor_stock_selection_checkbox.size()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return vendor_stockName_textView.size();
    }

    public List<Integer> getSelectedItemPositions() {
        return vendor_stock_selection_checkbox;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView vendor_stock_imageView;
        CheckBox vendor_stock_selection_checkbox;
        TextView vendor_stock_category_textView, vendor_stockName_textView, vendor_stock_currency_textview, vendor_stock_price_textview, vendor_stock_quantity_textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vendor_stock_category_textView = (TextView) itemView.findViewById(R.id.vendor_stock_category_textView);
            vendor_stockName_textView = (TextView) itemView.findViewById(R.id.vendor_stockName_textView);
            vendor_stock_currency_textview = (TextView) itemView.findViewById(R.id.vendor_stock_currency_textview);
            vendor_stock_price_textview = (TextView) itemView.findViewById(R.id.vendor_stock_price_textview);
            vendor_stock_quantity_textView = (TextView) itemView.findViewById(R.id.vendor_stock_quantity_textView);
            vendor_stock_imageView = (ImageView) itemView.findViewById(R.id.vendor_stock_imageView);
            vendor_stock_selection_checkbox = (CheckBox) itemView.findViewById(R.id.vendor_stock_selection_checkbox);

            //TODO is ma next activity ma data pass ho raha jub back awo wapis is activity pa aur again jawo to data double ho jata picli list clear nhi ho rahe

        }
    }
}

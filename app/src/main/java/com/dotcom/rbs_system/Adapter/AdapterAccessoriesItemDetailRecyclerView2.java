package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;

import java.util.List;

public class AdapterAccessoriesItemDetailRecyclerView2 extends RecyclerView.Adapter<AdapterAccessoriesItemDetailRecyclerView2.ViewHolder> {
    Context context;
    List<String> itemCategoryList,ItemNameList,itemPriceList,itemQuantityList;

    public AdapterAccessoriesItemDetailRecyclerView2(Context context, List<String> itemCategoryList, List<String> ItemNameList, List<String> itemPriceList, List<String> itemQuantityList) {
        this.context = context;
        this.itemCategoryList = itemCategoryList;
        this.ItemNameList =ItemNameList;
        this.itemPriceList = itemPriceList;
        this.itemQuantityList = itemQuantityList;
    }

    @NonNull
    @Override
    public AdapterAccessoriesItemDetailRecyclerView2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_accessories_stock_check_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAccessoriesItemDetailRecyclerView2.ViewHolder holder, final int position) {
        holder.category_name_textView.setText(itemCategoryList.get(position));
        holder.item_name_textView.setText(ItemNameList.get(position));
        holder.priceUnit_textView.setText(itemPriceList.get(position));
        holder.quantity_textView.setText(itemQuantityList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemQuantityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView category_name_textView,item_name_textView,priceUnit_textView,quantity_textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            category_name_textView = (TextView)itemView.findViewById(R.id.category_name_textView);
            item_name_textView = (TextView)itemView.findViewById(R.id.item_name_textView);
            priceUnit_textView = (TextView)itemView.findViewById(R.id.priceUnit_textView);
            quantity_textView = (TextView)itemView.findViewById(R.id.quantity_textView);
        }
    }
}

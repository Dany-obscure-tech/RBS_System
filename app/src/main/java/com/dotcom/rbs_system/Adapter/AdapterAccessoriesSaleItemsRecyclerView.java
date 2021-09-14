package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.R;

import java.util.List;

public class AdapterAccessoriesSaleItemsRecyclerView extends RecyclerView.Adapter<AdapterAccessoriesSaleItemsRecyclerView.ViewHolder> {
    String currency = Currency.getInstance().getCurrency();
    Context context;
    List<String> accessorykeyIdList, accessoryCategoryList, accessoryItemNameList, accessoryQtyList, accessoryUnitPriceList, accessoryTotalPriceList,accessoryOldUnitPriceList;

    public AdapterAccessoriesSaleItemsRecyclerView(Context context, List<String> accessorykeyIdList, List<String> accessoryCategoryList, List<String> accessoryItemNameList, List<String> accessoryQtyList, List<String> accessoryUnitPriceList, List<String> accessoryTotalPriceList, List<String> accessoryOldUnitPriceList) {
        this.context = context;
        this.accessorykeyIdList = accessorykeyIdList;
        this.accessoryCategoryList = accessoryCategoryList;
        this.accessoryItemNameList = accessoryItemNameList;
        this.accessoryQtyList = accessoryQtyList;
        this.accessoryUnitPriceList = accessoryUnitPriceList;
        this.accessoryTotalPriceList = accessoryTotalPriceList;
        this.accessoryOldUnitPriceList = accessoryOldUnitPriceList;
    }

    @NonNull
    @Override
    public AdapterAccessoriesSaleItemsRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_accessory_sale_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAccessoriesSaleItemsRecyclerView.ViewHolder holder, final int position) {
        holder.category_textView.setText(accessoryCategoryList.get(position));
        holder.item_name_textView.setText(accessoryItemNameList.get(position));
        holder.unitPrice_textView.setText(currency + accessoryUnitPriceList.get(position));
        holder.totalPrice_textView.setText(accessoryTotalPriceList.get(position));
        holder.quantity_textView.setText(accessoryQtyList.get(position));

        holder.remove_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessoryCategoryList.remove(position);
                accessoryItemNameList.remove(position);
                accessoryQtyList.remove(position);
                accessoryUnitPriceList.remove(position);
                accessoryTotalPriceList.remove(position);
                accessorykeyIdList.remove(position);
                accessoryOldUnitPriceList.remove(position);

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return accessoryItemNameList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView category_textView, item_name_textView, unitPrice_textView, totalPrice_textView, quantity_textView, remove_textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            category_textView = (TextView) itemView.findViewById(R.id.category_textView);
            item_name_textView = (TextView) itemView.findViewById(R.id.item_name_textView);
            unitPrice_textView = (TextView) itemView.findViewById(R.id.unitPrice_textView);
            totalPrice_textView = (TextView) itemView.findViewById(R.id.totalPrice_textView);
            quantity_textView = (TextView) itemView.findViewById(R.id.quantity_textView);

            remove_textView = (TextView) itemView.findViewById(R.id.remove_textView);
        }
    }
}

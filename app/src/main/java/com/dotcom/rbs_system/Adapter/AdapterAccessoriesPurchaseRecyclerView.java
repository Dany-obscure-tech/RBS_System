package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;

import java.util.List;

public class AdapterAccessoriesPurchaseRecyclerView extends RecyclerView.Adapter<AdapterAccessoriesPurchaseRecyclerView.ViewHolder> {
    Context context;
    List<String> purchaseItemNameList,purchaseCategoryList,purchasePriceUnitList,purchaseQuantityList;

    public AdapterAccessoriesPurchaseRecyclerView(Context context,List<String> purchaseItemNameList,List<String> purchaseCategoryList,List<String> purchasePriceUnitList,List<String> purchaseQuantityList) {
        this.context = context;
        this.purchaseItemNameList =purchaseItemNameList;
        this.purchaseCategoryList = purchaseCategoryList;
        this.purchasePriceUnitList = purchasePriceUnitList;
        this.purchaseQuantityList = purchaseQuantityList;
    }

    @NonNull
    @Override
    public AdapterAccessoriesPurchaseRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_accessories_purchase_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAccessoriesPurchaseRecyclerView.ViewHolder holder, final int position) {
        holder.itemName_textView.setText(purchaseItemNameList.get(position));
        holder.category_textView.setText(purchaseCategoryList.get(position));
        holder.priceUnit_textView.setText(purchasePriceUnitList.get(position));
        holder.quantity_textView.setText(purchaseQuantityList.get(position));

        holder.remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseItemNameList.remove(position);
                purchaseCategoryList.remove(position);
                purchasePriceUnitList.remove(position);
                purchaseQuantityList.remove(position);

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return purchaseItemNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName_textView,category_textView,priceUnit_textView,quantity_textView;
        Button remove_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName_textView = (TextView)itemView.findViewById(R.id.company_name_textView);
            category_textView = (TextView)itemView.findViewById(R.id.category_textView);
            priceUnit_textView = (TextView)itemView.findViewById(R.id.priceUnit_textView);
            quantity_textView = (TextView)itemView.findViewById(R.id.quantity_textView);

            remove_btn = (Button) itemView.findViewById(R.id.remove_btn);
        }
    }
}

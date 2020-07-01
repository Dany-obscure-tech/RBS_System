package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;

import java.util.List;

public class AdapterAccessoriesItemsRecyclerView extends RecyclerView.Adapter<AdapterAccessoriesItemsRecyclerView.ViewHolder> {
    Context context;
    List<String> accessorySrNoList,accessoryNameList, accessoryQtyList, accessoryUnitPriceList, accessoryTotalPriceList;

    public AdapterAccessoriesItemsRecyclerView(Context context, List<String>accessorySrNoList,List<String> accessoryNameList, List<String> accessoryQtyList, List<String> accessoryUnitPriceList, List<String> accessoryTotalPriceList) {
        this.context = context;
        this.accessorySrNoList = accessorySrNoList;
        this.accessoryNameList = accessoryNameList;
        this.accessoryQtyList = accessoryQtyList;
        this.accessoryUnitPriceList = accessoryUnitPriceList;
        this.accessoryTotalPriceList = accessoryTotalPriceList;
    }

    @NonNull
    @Override
    public AdapterAccessoriesItemsRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_accessory_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAccessoriesItemsRecyclerView.ViewHolder holder, final int position) {
        holder.accessoryName_textView.setText(accessoryNameList.get(position));
        holder.unitPrice_textView.setText(accessoryQtyList.get(position));
        holder.totalPrice_textView.setText(accessoryUnitPriceList.get(position));
        holder.quantity_textView.setText(accessoryTotalPriceList.get(position));
        holder.serialNo_textView.setText(accessorySrNoList.get(position));

        holder.remove_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessoryNameList.remove(position);
                accessoryQtyList.remove(position);
                accessoryUnitPriceList.remove(position);
                accessoryTotalPriceList.remove(position);
                accessorySrNoList.remove(accessorySrNoList.size()-1);

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return accessoryNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView accessoryName_textView, unitPrice_textView, totalPrice_textView,quantity_textView,remove_textView,serialNo_textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            accessoryName_textView = (TextView)itemView.findViewById(R.id.accessoryName_textView);
            unitPrice_textView = (TextView)itemView.findViewById(R.id.unitPrice_textView);
            totalPrice_textView = (TextView)itemView.findViewById(R.id.totalPrice_textView);
            quantity_textView = (TextView)itemView.findViewById(R.id.quantity_textView);
            serialNo_textView = (TextView)itemView.findViewById(R.id.serialNo_textView);

            remove_textView = (TextView) itemView.findViewById(R.id.remove_textView);
        }
    }
}

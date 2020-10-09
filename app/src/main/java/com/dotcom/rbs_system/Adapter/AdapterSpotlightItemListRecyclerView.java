package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;

import java.util.List;

public class AdapterSpotlightItemListRecyclerView extends RecyclerView.Adapter<AdapterSpotlightItemListRecyclerView.ViewHolder> {
    Context context;
    List<String> item_name, item_price;

    public AdapterSpotlightItemListRecyclerView(Context context,List<String> item_name,List<String> item_price) {
        this.context=context;
        this.item_name=item_name;
        this.item_price=item_price;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_spotlight_items,parent,false);
        AdapterSpotlightItemListRecyclerView.ViewHolder viewHolder = new AdapterSpotlightItemListRecyclerView.ViewHolder(view);
//        statement_recyclerView = (RecyclerView) view.findViewById(R.id.statement_recyclerView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Toast.makeText(context, "called", Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return item_name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView transaction,statement_description,statement_balance,statement_date;
        ImageView statement_status;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);



        }
    }
}

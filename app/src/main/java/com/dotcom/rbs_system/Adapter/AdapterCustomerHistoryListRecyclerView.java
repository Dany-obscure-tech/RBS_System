package com.dotcom.rbs_system.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterCustomerHistoryListRecyclerView extends RecyclerView.Adapter<AdapterCustomerHistoryListRecyclerView.ViewHolder> {
    Context context;
    List<String> itemNameList, rbsList, dateList;
    Activity activity;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Listed_faults");

    public AdapterCustomerHistoryListRecyclerView(Context context, List<String> itemNameList, List<String> rbsList, List<String> dateList) {
        this.context = context;
        this.itemNameList = itemNameList;
        this.rbsList = rbsList;
        this.dateList = dateList;

    }

    @NonNull
    @Override
    public AdapterCustomerHistoryListRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_itemhistorylist_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCustomerHistoryListRecyclerView.ViewHolder holder, final int position) {
        holder.customerName_textView.setText(itemNameList.get(position));
        holder.rbs_textView.setText(rbsList.get(position));
        holder.date_textView.setText(dateList.get(position));


    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date_textView,customerName_textView, rbs_textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date_textView = (TextView)itemView.findViewById(R.id.date_textView);
            customerName_textView = (TextView)itemView.findViewById(R.id.customerName_textView);
            rbs_textView = (TextView)itemView.findViewById(R.id.rbs_textView);

        }
    }
}

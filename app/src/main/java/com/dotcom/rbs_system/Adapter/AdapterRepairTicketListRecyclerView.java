package com.dotcom.rbs_system.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;
import com.dotcom.rbs_system.Repair_Ticket;
import com.dotcom.rbs_system.Repair_details;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.List;

public class AdapterRepairTicketListRecyclerView extends RecyclerView.Adapter<AdapterRepairTicketListRecyclerView.ViewHolder> {
    Context context;
    List<String> customerNameList, itemNameList, ticketNoList;
    Activity activity;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Listed_faults");

    public AdapterRepairTicketListRecyclerView(Context context, List<String> customerNameList, List<String> itemNameList, List<String> ticketNoList) {
        this.context = context;
        this.customerNameList = customerNameList;
        Collections.reverse(this.customerNameList);
        this.itemNameList = itemNameList;
        Collections.reverse(this.itemNameList);
        this.ticketNoList = ticketNoList;
        Collections.reverse(this.ticketNoList);
        activity = (Activity)context;
    }

    @NonNull
    @Override
    public AdapterRepairTicketListRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_repairticket_repairticketlist_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRepairTicketListRecyclerView.ViewHolder holder, final int position) {
        holder.customerName_textView.setText(customerNameList.get(position));
        holder.itemName_textView.setText(itemNameList.get(position));
        holder.ticketNo_textView.setText(ticketNoList.get(position));

        holder.leftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Repair_details.class);
                intent.putExtra("REPAIR_ID",ticketNoList.get(position));
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return ticketNoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView ticketNo_textView,customerName_textView,itemName_textView;
        LinearLayout leftLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ticketNo_textView = (TextView)itemView.findViewById(R.id.ticketNo_textView);
            customerName_textView = (TextView)itemView.findViewById(R.id.customerName_textView);
            itemName_textView = (TextView)itemView.findViewById(R.id.itemName_textView);

            leftLayout = (LinearLayout) itemView.findViewById(R.id.leftLayout);

        }
    }
}

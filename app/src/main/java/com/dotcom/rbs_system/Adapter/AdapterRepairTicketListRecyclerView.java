package com.dotcom.rbs_system.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Progress_dialog;
import com.dotcom.rbs_system.R;
import com.dotcom.rbs_system.Repair_ticket_details;

import java.util.Collections;
import java.util.List;

public class AdapterRepairTicketListRecyclerView extends RecyclerView.Adapter<AdapterRepairTicketListRecyclerView.ViewHolder> {
    Context context;
    List<String> customerNameList, customerIdList, itemNameList, itemSerialNoList, repairKeyIDList, dateList, pendingStatusList,ticketNoList;
    Activity activity;

    public AdapterRepairTicketListRecyclerView(Context context, List<String> customerNameList, List<String> customerIdList, List<String> itemNameList, List<String> itemSerialNoList, List<String> repairKeyIDList, List<String> dateList, List<String> pendingStatusList, List<String> ticketNoList) {

        this.context = context;

        this.customerNameList = customerNameList;
        Collections.reverse(this.customerNameList);
        this.customerIdList = customerIdList;
        Collections.reverse(this.customerIdList);
        this.itemNameList = itemNameList;
        Collections.reverse(this.itemNameList);
        this.itemSerialNoList = itemSerialNoList;
        Collections.reverse(this.itemSerialNoList);
        this.repairKeyIDList = repairKeyIDList;
        Collections.reverse(this.repairKeyIDList);
        this.dateList = dateList;
        Collections.reverse(this.dateList);
        this.pendingStatusList = pendingStatusList;
        Collections.reverse(this.pendingStatusList);
        this.ticketNoList = ticketNoList;
        Collections.reverse(this.ticketNoList);

        activity = (Activity) context;

    }

    @NonNull
    @Override
    public AdapterRepairTicketListRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_repairticket_repairticketlist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRepairTicketListRecyclerView.ViewHolder holder, int position) {
        holder.customerName_textView.setText(customerNameList.get(position));
        holder.customerID_textView.setText(customerIdList.get(position));
        holder.itemName_textView.setText(itemNameList.get(position));
        holder.item_serialNo_textView.setText(itemSerialNoList.get(position));
        holder.date_textView.setText(dateList.get(position));

        if (pendingStatusList != null) {
            switch (pendingStatusList.get(position)) {
                case "Pending Changes":
                    holder.ticketNo_textView.setText(ticketNoList.get(position) + "\n(Pending Changes)");
                    break;
                case "Confirmed":
                    holder.ticketNo_textView.setText(ticketNoList.get(position) + "\n(Confirmed)");
                    break;
                case "Canceled":
                    holder.ticketNo_textView.setText(ticketNoList.get(position) + "\n(Canceled)");
                    break;
                case "clear":
                    holder.ticketNo_textView.setText(ticketNoList.get(position));
                    break;
            }
        } else {
            holder.ticketNo_textView.setText(ticketNoList.get(position));
        }


        holder.leftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Repair_ticket_details.class);
                intent.putExtra("REPAIR_ID", repairKeyIDList.get(position));
                intent.putExtra("STATUS", pendingStatusList.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return repairKeyIDList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        Progress_dialog pd;

        TextView ticketNo_textView, customerName_textView, itemName_textView;
        TextView customerID_textView, item_serialNo_textView, date_textView;
        LinearLayout leftLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pd = new Progress_dialog();

            ticketNo_textView = itemView.findViewById(R.id.ticketNo_textView);
            customerName_textView = itemView.findViewById(R.id.customerName_textView);
            customerID_textView = itemView.findViewById(R.id.customerID_textView);
            itemName_textView = itemView.findViewById(R.id.item_category_textView);
            item_serialNo_textView = itemView.findViewById(R.id.item_serialNo_textView);
            date_textView = itemView.findViewById(R.id.date_textView);

            leftLayout = itemView.findViewById(R.id.leftLayout);

        }
    }
}

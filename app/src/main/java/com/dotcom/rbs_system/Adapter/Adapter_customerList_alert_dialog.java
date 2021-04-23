package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;

import java.util.List;

public class Adapter_customerList_alert_dialog extends RecyclerView.Adapter<Adapter_customerList_alert_dialog.ViewHolder> {

    Context context;
    List<String> exisitngCustomerNamesList, exisitngCustomerIDList, exisitngCustomerKeyIDList, existingCustomerEmailList, existingCustomerPhnoList, existingCustomerImageUrlList;

    public Adapter_customerList_alert_dialog(Context context, List<String> exisitngCustomerNamesList, List<String> exisitngCustomerIDList, List<String> exisitngCustomerKeyIDList, List<String> existingCustomerEmailList, List<String> existingCustomerPhnoList, List<String> existingCustomerImageUrlList) {
        this.context = context;
        this.exisitngCustomerNamesList = exisitngCustomerNamesList;
        this.exisitngCustomerIDList = exisitngCustomerIDList;
        this.exisitngCustomerKeyIDList = exisitngCustomerKeyIDList;
        this.existingCustomerEmailList = existingCustomerEmailList;
        this.existingCustomerPhnoList = existingCustomerPhnoList;
        this.existingCustomerImageUrlList = existingCustomerImageUrlList;
    }

    @NonNull
    @Override
    public Adapter_customerList_alert_dialog.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Adapter_customerList_alert_dialog.ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_customerlist_alert_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_customerList_alert_dialog.ViewHolder holder, int position) {
        holder.customerName_textView.setText(exisitngCustomerNamesList.get(position));
//        holder.customerID_textView.setText(exisitngCustomerIDList.get(position));
//        holder.customerEmail_textView.setText(existingCustomerEmailList.get(position));
//        holder.customerPhno_textView.setText(existingCustomerPhnoList.get(position));
//
//        Picasso.get().load(existingCustomerImageUrlList.get(position)).into(holder.customerImage_imageView);
    }

    @Override
    public int getItemCount() {
        return exisitngCustomerNamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName_textView, customerID_textView, customerEmail_textView, customerPhno_textView;
        ImageView customerImage_imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName_textView = (TextView)itemView.findViewById(R.id.customerName_textView);
            customerID_textView = (TextView)itemView.findViewById(R.id.customerID_textView);
            customerEmail_textView = (TextView)itemView.findViewById(R.id.customerEmail_textView);
            customerPhno_textView = (TextView)itemView.findViewById(R.id.customerPhno_textView);
            customerImage_imageView = (ImageView) itemView.findViewById(R.id.customerImage_imageView);

        }
    }
}

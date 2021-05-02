package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;
import com.dotcom.rbs_system.Rbs_vendor_specific_order;
import com.dotcom.rbs_system.VendorShopkeepersShopReceipts;
import com.dotcom.rbs_system.Vendor_shopkeepers_orders_progress;

import java.util.List;

public class Adapter_RBS_Vendor_orders_list_RecyclerView extends RecyclerView.Adapter<Adapter_RBS_Vendor_orders_list_RecyclerView.MyViewHolder> {
    Context context;
    List<String> invoice_no_textview;
    List<String> vendor_name_textview;
    List<String> amount_currency_textview;
    List<String> amount_textview;
    List<String> paid_currency_textview;
    List<String> paid_textview;
    List<String> date_textView;
    List<String> balance_currency_textview;
    List<String> balance_textview;
    List<String> order_status_textview;

    public Adapter_RBS_Vendor_orders_list_RecyclerView(Context context, List<String> invoice_no_textview, List<String>vendor_name_textview, List<String> amount_currency_textview, List<String> amount_textview, List<String> paid_currency_textview, List<String> paid_textview, List<String> date_textView, List<String> balance_currency_textview,List<String> balance_textview,List<String>order_status_textview) {
        this.context = context;
        this.invoice_no_textview = invoice_no_textview;
        this.vendor_name_textview = vendor_name_textview;
        this.amount_currency_textview = amount_currency_textview;
        this.amount_textview = amount_textview;
        this.paid_currency_textview = paid_currency_textview;
        this.paid_textview = paid_textview;
        this.date_textView = date_textView;
        this.balance_currency_textview = balance_currency_textview;
        this.balance_textview = balance_textview;
        this.order_status_textview = order_status_textview;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.rbs_vendor_orders_list_item, parent, false);
        Adapter_RBS_Vendor_orders_list_RecyclerView.MyViewHolder myViewHolder = new Adapter_RBS_Vendor_orders_list_RecyclerView.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.order_status_textview.setText(order_status_textview.get(position));
        holder.vendor_name_textview.setText(vendor_name_textview.get(position));
        holder.invoice_no_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Rbs_vendor_specific_order.class);
                intent.putExtra("Invoice_no",invoice_no_textview.get(position));
                intent.putExtra("Vendor_name",vendor_name_textview.get(position));
                intent.putExtra("Date",date_textView.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return vendor_name_textview.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView invoice_no_textview,vendor_name_textview,amount_currency_textview,amount_textview,paid_currency_textview,paid_textview,date_textView,balance_currency_textview,balance_textview,order_status_textview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            invoice_no_textview = (TextView) itemView.findViewById(R.id.invoice_no_textview);
            vendor_name_textview = (TextView) itemView.findViewById(R.id.vendor_name_textview);
            amount_currency_textview = (TextView) itemView.findViewById(R.id.amount_currency_textview);
            amount_textview = (TextView) itemView.findViewById(R.id.amount_textview);
            paid_currency_textview = (TextView) itemView.findViewById(R.id.paid_currency_textview);
            paid_textview = (TextView) itemView.findViewById(R.id.paid_textview);
            date_textView = (TextView) itemView.findViewById(R.id.date_textView);
            balance_currency_textview = (TextView) itemView.findViewById(R.id.balance_currency_textview);
            balance_textview = (TextView) itemView.findViewById(R.id.balance_textview);
            order_status_textview = (TextView) itemView.findViewById(R.id.order_status_textview);

        }
    }
}

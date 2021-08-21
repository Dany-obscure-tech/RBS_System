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

public class Adapter_Vendor_shopkeeper_invoices_RecyclerView extends RecyclerView.Adapter<Adapter_Vendor_shopkeeper_invoices_RecyclerView.MyViewHolder> {
    Context context;
    List<String> shopkeeper_invoice_no;
    List<String> amount_currency;
    List<String> amount_price;
    List<String> invoice_date;

    public Adapter_Vendor_shopkeeper_invoices_RecyclerView(Context context, List<String> shopkeeper_invoice_no, List<String> amount_currency, List<String> amount_price, List<String> invoice_date) {
        this.context = context;
        this.shopkeeper_invoice_no = shopkeeper_invoice_no;
        this.amount_currency = amount_currency;
        this.amount_price = amount_price;
        this.invoice_date = invoice_date;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.vendor_shopkeeper_invoice_item, parent, false);
        Adapter_Vendor_shopkeeper_invoices_RecyclerView.MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.shopkeeper_invoice_no.setText(shopkeeper_invoice_no.get(position));
    }

    @Override
    public int getItemCount() {

            return shopkeeper_invoice_no.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView shopkeeper_invoice_no, amount_currency, amount_price, invoice_date, status_textview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            shopkeeper_invoice_no = itemView.findViewById(R.id.shopkeeper_invoice_no);
            amount_currency = itemView.findViewById(R.id.amount_currency);
            amount_price = itemView.findViewById(R.id.amount_price);
            invoice_date = itemView.findViewById(R.id.invoice_date);
            status_textview = itemView.findViewById(R.id.status_textview);


        }
    }
}

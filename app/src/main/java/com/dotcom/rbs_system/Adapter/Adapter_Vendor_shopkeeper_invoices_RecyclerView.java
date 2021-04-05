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
import com.dotcom.rbs_system.VendorShopkeepersShopReceipts;

import java.util.List;

public class Adapter_Vendor_shopkeeper_invoices_RecyclerView extends RecyclerView.Adapter<Adapter_Vendor_shopkeeper_invoices_RecyclerView.MyViewHolder> {
    Context context;
    List<String> shopkeeper_invoice_no;
    List<String> amount_currency;
    List<String> amount_price;
    List<String> invoice_date;
    List<String> balance_currency;
    List<String> shopkeeper_balance;
    List<String> paid_currency;
    List<String> shopkeeper_paid;

    public Adapter_Vendor_shopkeeper_invoices_RecyclerView(Context context, List<String> shopkeeper_invoice_no, List<String>amount_currency, List<String> amount_price, List<String> invoice_date, List<String> balance_currency, List<String> shopkeeper_balance, List<String> paid_currency, List<String> shopkeeper_paid) {
        this.context = context;
        this.shopkeeper_invoice_no = shopkeeper_invoice_no;
        this.amount_currency = amount_currency;
        this.amount_price = amount_price;
        this.invoice_date = invoice_date;
        this.balance_currency = balance_currency;
        this.shopkeeper_balance = shopkeeper_balance;
        this.paid_currency = paid_currency;
        this.shopkeeper_paid = shopkeeper_paid;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.vendor_shopkeeper_invoice_item, parent, false);
        Adapter_Vendor_shopkeeper_invoices_RecyclerView.MyViewHolder myViewHolder = new Adapter_Vendor_shopkeeper_invoices_RecyclerView.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.shopkeeper_paid.setText(shopkeeper_paid.get(position));
    }

    @Override
    public int getItemCount() {

        return shopkeeper_paid.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView shopkeeper_invoice_no,amount_currency,amount_price,invoice_date,balance_currency,shopkeeper_balance,paid_currency,shopkeeper_paid;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            shopkeeper_invoice_no = (TextView) itemView.findViewById(R.id.shopkeeper_invoice_no);
            amount_currency = (TextView) itemView.findViewById(R.id.amount_currency);
            amount_price = (TextView) itemView.findViewById(R.id.amount_price);
            invoice_date = (TextView) itemView.findViewById(R.id.invoice_date);
            balance_currency = (TextView) itemView.findViewById(R.id.balance_currency);
            shopkeeper_balance = (TextView) itemView.findViewById(R.id.shopkeeper_balance);
            paid_currency = (TextView) itemView.findViewById(R.id.paid_currency);
            shopkeeper_paid = (TextView) itemView.findViewById(R.id.shopkeeper_paid);


        }
    }
}

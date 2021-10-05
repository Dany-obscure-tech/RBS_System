package com.dotcom.rbs_system.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.R;
import com.dotcom.rbs_system.VendorShopkeepersShopReceipts;
import com.dotcom.rbs_system.Vendor_shopkeepers_orders_progress;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Vendor_ShopkeeperDetails_order_list_RecyclerView extends RecyclerView.Adapter<Adapter_Vendor_ShopkeeperDetails_order_list_RecyclerView.MyViewHolder> {
    Context context;
    List<String> order_no_vendor;
    List<String> date;
    List<String> totalAmount;
    List<String> vendor_order_status;
    List<String> shopKeeper_keyID;
    List<String> invoiceKeyID;

    public Adapter_Vendor_ShopkeeperDetails_order_list_RecyclerView(Context context, List<String> order_no_vendor, List<String> date, List<String> totalAmount, List<String> vendor_order_status, List<String> shopKeeper_keyID, List<String> invoiceKeyID) {
        this.context = context;
        this.order_no_vendor = order_no_vendor;
        this.date = date;
        this.totalAmount = totalAmount;
        this.vendor_order_status = vendor_order_status;
        this.shopKeeper_keyID = shopKeeper_keyID;
        this.invoiceKeyID = invoiceKeyID;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.vendor_shopkeeperdetails_orders_list_item, parent, false);
        Adapter_Vendor_ShopkeeperDetails_order_list_RecyclerView.MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.date.setText(date.get(position));
        holder.totalBalance.setText(totalAmount.get(position));
        holder.balance_currency.setText(Currency.getInstance().getCurrency());
        holder.vendor_order_status.setText(vendor_order_status.get(position));

        holder.order_no_vendor.setText(order_no_vendor.get(position));
        holder.searchForItem_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Vendor_shopkeepers_orders_progress.class);

                intent.putExtra("User_ID", shopKeeper_keyID.get(position));
                intent.putExtra("Invoice_keyID", invoiceKeyID.get(position));
                intent.putExtra("status", vendor_order_status.get(position));
                intent.putExtra("userType", "vendor");
                ((Activity)context).startActivityForResult(intent,111);
            }
        });
    }

    @Override
    public int getItemCount() {

        return shopKeeper_keyID.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView searchForItem_cardView;
        TextView order_no_vendor, date, balance_currency, vendor_order_status, totalBalance;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            searchForItem_cardView = itemView.findViewById(R.id.searchForItem_cardView);
            order_no_vendor = itemView.findViewById(R.id.order_no_vendor);
            date = itemView.findViewById(R.id.date);
            totalBalance = itemView.findViewById(R.id.totalBalance);
            balance_currency = itemView.findViewById(R.id.balance_currency);
            vendor_order_status = itemView.findViewById(R.id.vendor_order_status);

        }
    }
}

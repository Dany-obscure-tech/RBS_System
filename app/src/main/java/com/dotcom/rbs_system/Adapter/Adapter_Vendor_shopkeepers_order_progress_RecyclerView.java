package com.dotcom.rbs_system.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Vendor_shopkeepers_order_progress_RecyclerView extends RecyclerView.Adapter<Adapter_Vendor_shopkeepers_order_progress_RecyclerView.MyViewHolder> {
    Dialog quantityEditAlert;
    TextView editAccessoryName_textview,addSave_textview,addCancel_textview;
    EditText alertAccessoryQuantity_editText;
    DatabaseReference accessoriesOrdersList;
    DatabaseReference vendorOrderRef, shopkeeperVendorOrderRef;

    Context context;
    List<String> accessory_name, rate_price, order_qty, image_url;
    String shopKeeperID, invoiceID,status,check;


    public Adapter_Vendor_shopkeepers_order_progress_RecyclerView(Context context, List<String> accessory_name, List<String> rate_price, List<String> order_qty, List<String> image_url,String shopKeeperID,String invoiceID,String status,String check) {
        this.context = context;
        this.accessory_name = accessory_name;
        this.rate_price = rate_price;
        this.order_qty = order_qty;
        this.image_url = image_url;
        this.shopKeeperID = shopKeeperID;
        this.invoiceID = invoiceID;
        this.status = status;
        this.check = check;

        vendorOrderRef = FirebaseDatabase.getInstance().getReference("Vendor_order/" + FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+invoiceID);
        shopkeeperVendorOrderRef = FirebaseDatabase.getInstance().getReference("Shopkeeper_vendor_order/" + shopKeeperID+"/"+invoiceID);
        accessoriesOrdersList = FirebaseDatabase.getInstance().getReference("Accessories_orders/" + invoiceID);

        quantityEditAlert = new Dialog(context);
        quantityEditAlert.setContentView(R.layout.alert_accessory_quantity_edit);
        editAccessoryName_textview = quantityEditAlert.findViewById(R.id.editAccessoryName_textview);
        addSave_textview = quantityEditAlert.findViewById(R.id.addSave_textview);
        addCancel_textview = quantityEditAlert.findViewById(R.id.addCancel_textview);
        alertAccessoryQuantity_editText = quantityEditAlert.findViewById(R.id.alertAccessoryQuantity_editText);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.vendor_shopkeepers_order_progress_list_item, parent, false);
        Adapter_Vendor_shopkeepers_order_progress_RecyclerView.MyViewHolder myViewHolder = new MyViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.accessory_name.setText(accessory_name.get(position));
        holder.rate_currency.setText(Currency.getInstance().getCurrency());
        holder.rate_price.setText(rate_price.get(position));
        holder.order_qty.setText(order_qty.get(position));
        Picasso.get().load(image_url.get(position)).into(holder.placeorder_item_pic_imageView);

        if (status.equals("Ready")){
            if (check.equals("vendor")){
                holder.itemAccessory_cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        quantityEditAlert.show();
                        editAccessoryName_textview.setText(accessory_name.get(position));
                        alertAccessoryQuantity_editText.setText(order_qty.get(position));
                        addSave_textview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                float totalBalance = 0.0f;
                                accessoriesOrdersList.child("items_list").child("item_"+String.valueOf(position+1)).child("item_quantity").setValue(alertAccessoryQuantity_editText.getText().toString());
                                order_qty.set(position,alertAccessoryQuantity_editText.getText().toString());
                                for (int i = 0;i<getItemCount();i++){
                                    totalBalance = totalBalance+((Float.parseFloat(rate_price.get(i)))*(Float.parseFloat(order_qty.get(i))));
                                    System.out.println(totalBalance);
                                    System.out.println(rate_price.get(i));
                                    System.out.println(order_qty.get(i));
                                    System.out.println("/////////////////");
                                }
                                accessoriesOrdersList.child("totalBalance").setValue(String.valueOf(totalBalance));
                                ((Activity)context).recreate();
                            }
                        });
                    }
                });

            }
        }

    }

    @Override
    public int getItemCount() {

        return accessory_name.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView accessory_name, rate_currency, rate_price, order_qty;
        ImageView placeorder_item_pic_imageView;
        CardView itemAccessory_cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            accessory_name = itemView.findViewById(R.id.stockName_textView);
            rate_currency = itemView.findViewById(R.id.rate_currency);
            rate_price = itemView.findViewById(R.id.rate_price);
            order_qty = itemView.findViewById(R.id.order_qty);
            placeorder_item_pic_imageView = itemView.findViewById(R.id.placeorder_item_pic_imageView);
            itemAccessory_cardView = itemView.findViewById(R.id.itemAccessory_cardView);

        }
    }
}

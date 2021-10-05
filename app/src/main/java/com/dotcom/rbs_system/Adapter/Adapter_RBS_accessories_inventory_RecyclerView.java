package com.dotcom.rbs_system.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Adapter_RBS_accessories_inventory_RecyclerView extends RecyclerView.Adapter<Adapter_RBS_accessories_inventory_RecyclerView.MyViewHolder> {
    DatabaseReference stockRef = FirebaseDatabase.getInstance().getReference("ShopKeeper_accessories/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

    Context context;
    List<String> accessory_name_textView;
    List<String> accessory_inventory_Category_textView;
    List<String> stockPrice_list;
    List<String> accessory_inventory_Quantity_textView;
    List<String> edit_stock_textview_list;
    List<String> stockkeyId_list;

    Dialog dialog_box;

    public Adapter_RBS_accessories_inventory_RecyclerView(Context context, List<String> accessory_name_textView, List<String> accessory_inventory_Category_textView, List<String> stockPrice_list, List<String> accessory_inventory_Quantity_textView, List<String> stockkeyId_list, List<String> edit_stock_textview_list) {
        this.context = context;
        this.accessory_name_textView = accessory_name_textView;
        this.accessory_inventory_Category_textView = accessory_inventory_Category_textView;
        this.stockPrice_list = stockPrice_list;
        this.accessory_inventory_Quantity_textView = accessory_inventory_Quantity_textView;
        this.stockkeyId_list = stockkeyId_list;
        this.edit_stock_textview_list = edit_stock_textview_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.shopkeeper_accesory_inventory_item, parent, false);
        Adapter_RBS_accessories_inventory_RecyclerView.MyViewHolder myViewHolder = new Adapter_RBS_accessories_inventory_RecyclerView.MyViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.accessory_name_textView.setText(accessory_name_textView.get(position));
        holder.accesory_inventory_Category_textView.setText(accessory_inventory_Category_textView.get(position));
        holder.stockPrice_textView.setText(stockPrice_list.get(position));
        holder.accessory_inventory_Quantity_textView.setText(accessory_inventory_Quantity_textView.get(position));

        holder.currency_textView.setText(Currency.getInstance().getCurrency());

        holder.edit_stock_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_box = new Dialog(context);
                dialog_box.setContentView(R.layout.alert_rbs_accessory_stock_edit);

                holder.item_name_editText = dialog_box.findViewById(R.id.item_name_editText);
                holder.category_editText = dialog_box.findViewById(R.id.username_editText);
                holder.remove_btn = dialog_box.findViewById(R.id.remove_btn);
                holder.save_btn = dialog_box.findViewById(R.id.save_btn);
                holder.cancel_btn = dialog_box.findViewById(R.id.cancel_btn);
                holder.item_price_editText = dialog_box.findViewById(R.id.item_price_editText);
                holder.item_quantity_edittext = dialog_box.findViewById(R.id.item_quantity_edittext);

                holder.category_editText.setText(accessory_inventory_Category_textView.get(position));

                holder.item_name_editText.setText(accessory_name_textView.get(position));
                holder.item_price_editText.setText(stockPrice_list.get(position));
                holder.item_quantity_edittext.setText(accessory_inventory_Quantity_textView.get(position));


                holder.save_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean valid = true;

                        if (holder.item_price_editText.getText().toString().isEmpty()) {
                            holder.item_price_editText.setError("Please enter price");
                            valid = false;

                        } else {
                            if (Float.parseFloat(holder.item_price_editText.getText().toString()) == 0) {
                                holder.item_price_editText.setError("Please enter valid price");
                            } else {
                                holder.item_price_editText.setText(holder.item_price_editText.getText().toString().replaceFirst("^0+(?!$)", ""));
                                if (holder.item_price_editText.getText().toString().startsWith(".")) {
                                    holder.item_price_editText.setText("0" + holder.item_price_editText.getText().toString());
                                }
                            }
                        }

                        if (holder.item_quantity_edittext.getText().toString().isEmpty()) {
                            holder.item_quantity_edittext.setError("Please enter price");
                            valid = false;

                        } else {
                            if (Float.parseFloat(holder.item_quantity_edittext.getText().toString()) == 0) {
                                holder.item_quantity_edittext.setError("Please enter valid price");
                            } else {
                                holder.item_quantity_edittext.setText(holder.item_quantity_edittext.getText().toString().replaceFirst("^0+(?!$)", ""));
                                if (holder.item_quantity_edittext.getText().toString().startsWith(".")) {
                                    holder.item_quantity_edittext.setText("0" + holder.item_quantity_edittext.getText().toString());
                                }
                            }
                        }

                        if (holder.item_quantity_edittext.getText().toString().equals("0")) {
                            holder.item_quantity_edittext.setError("Please enter item quantity");
                            valid = false;
                        }

                        if (holder.item_name_editText.getText().toString().isEmpty()) {
                            holder.item_name_editText.setError("Please enter item name");
                            valid = false;
                        }

                        if (valid) {
                            stockRef.child(accessory_inventory_Category_textView.get(position)).child(stockkeyId_list.get(position)).child("Price").setValue(holder.item_price_editText.getText().toString());
                            stockRef.child(accessory_inventory_Category_textView.get(position)).child(stockkeyId_list.get(position)).child("Quantity").setValue(holder.item_quantity_edittext.getText().toString());
                            stockRef.child(accessory_inventory_Category_textView.get(position)).child(stockkeyId_list.get(position)).child("Name").setValue(holder.item_name_editText.getText().toString());


                            dialog_box.dismiss();
                            ((Activity) context).recreate();
                        }

                    }
                });

                holder.cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_box.dismiss();
                    }
                });

                holder.remove_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stockRef.child(accessory_inventory_Category_textView.get(position)).child(stockkeyId_list.get(position)).removeValue();

                        dialog_box.dismiss();
                        ((Activity) context).recreate();
                    }
                });




                dialog_box.show();

            }

        });
    }


    @Override
    public int getItemCount() {

        return accessory_inventory_Category_textView.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView accessory_name_textView, accesory_inventory_Category_textView, stockPrice_textView, accessory_inventory_Quantity_textView;
        TextView currency_textView;
        TextView edit_stock_textview;
        TextView category_editText;
        EditText item_price_editText, item_quantity_edittext,item_name_editText;
        TextView save_btn, cancel_btn, remove_btn;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            accessory_name_textView = itemView.findViewById(R.id.accessory_name_textView);
            accesory_inventory_Category_textView = itemView.findViewById(R.id.accesory_inventory_Category_textView);
            stockPrice_textView = itemView.findViewById(R.id.stockPrice_textView);
            accessory_inventory_Quantity_textView = itemView.findViewById(R.id.accessory_inventory_Quantity_textView);
            currency_textView = itemView.findViewById(R.id.currency_textView);
            edit_stock_textview = itemView.findViewById(R.id.edit_stock_textview);
        }
    }
}

package com.dotcom.rbs_system.Adapter;

import android.annotation.SuppressLint;
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
import com.dotcom.rbs_system.Classes.VendorStockDetails;
import com.dotcom.rbs_system.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Vendor_inventory_RecyclerView extends RecyclerView.Adapter<Adapter_Vendor_inventory_RecyclerView.MyViewHolder> {
    Context context;
    List<String> stockName_list;
    List<String> stockCategory_list;
    List<String> stockPrice_list;
    List<String> stockQuantity_list;
    List<String> stockImageUrl_list;
    List<String> edit_stock_textview_list;
    List<String> stockkeyId_list;

    Dialog dialog_box;

    DatabaseReference vendorStockRef = FirebaseDatabase.getInstance().getReference("Vendor_stock/" + FirebaseAuth.getInstance().getCurrentUser().getUid());


    public Adapter_Vendor_inventory_RecyclerView(Context context, List<String> stockName_list, List<String> stockCategory_list, List<String> stockPrice_list, List<String> quantity_vendor_inventory, List<String> stockImageUrl_list, List<String> stockkeyId_list, List<String> edit_stock_textview_list) {
        this.context = context;
        this.stockName_list = stockName_list;
        this.stockCategory_list = stockCategory_list;
        this.stockPrice_list = stockPrice_list;
        this.stockQuantity_list = quantity_vendor_inventory;
        this.stockImageUrl_list = stockImageUrl_list;
        this.stockkeyId_list = stockkeyId_list;
        this.edit_stock_textview_list = edit_stock_textview_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.vendormainscreen_item, parent, false);
        Adapter_Vendor_inventory_RecyclerView.MyViewHolder myViewHolder = new Adapter_Vendor_inventory_RecyclerView.MyViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.stockName_textView.setText(stockName_list.get(position));
        holder.stockCategory_textView.setText(stockCategory_list.get(position));
        holder.stockPrice_textView.setText(stockPrice_list.get(position));
        holder.stockQuantity_textView.setText(stockQuantity_list.get(position));

        holder.currency_textView.setText(Currency.getInstance().getCurrency());

        Picasso.get().load(stockImageUrl_list.get(position)).into(holder.stockImage_imageView);

        holder.shop_items_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_box = new Dialog(context);
                dialog_box.setContentView(R.layout.alert_vendor_stock_edit);

                holder.item_name_editText = dialog_box.findViewById(R.id.item_name_editText);
                holder.category_editText = dialog_box.findViewById(R.id.username_editText);
                holder.change_picture_btn = dialog_box.findViewById(R.id.change_picture_btn);
                holder.remove_btn = dialog_box.findViewById(R.id.remove_btn);
                holder.save_btn = dialog_box.findViewById(R.id.save_btn);
                holder.cancel_btn = dialog_box.findViewById(R.id.cancel_btn);
                holder.item_price_editText = dialog_box.findViewById(R.id.item_price_editText);
                holder.item_quantity_edittext = dialog_box.findViewById(R.id.item_quantity_edittext);
                holder.stock_pic = dialog_box.findViewById(R.id.stock_pic);
                holder.stock_pic.setImageDrawable(holder.stockImage_imageView.getDrawable());

                holder.item_name_editText.setText(stockName_list.get(position));
                holder.category_editText.setText(stockCategory_list.get(position));

                holder.item_price_editText.setText(stockPrice_list.get(position));
                holder.item_quantity_edittext.setText(stockQuantity_list.get(position));

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
                            vendorStockRef.child(stockCategory_list.get(position)).child(stockkeyId_list.get(position)).child("Price").setValue(holder.item_price_editText.getText().toString());
                            vendorStockRef.child(stockCategory_list.get(position)).child(stockkeyId_list.get(position)).child("Quantity").setValue(holder.item_quantity_edittext.getText().toString());
                            vendorStockRef.child(stockCategory_list.get(position)).child(stockkeyId_list.get(position)).child("Name").setValue(holder.item_name_editText.getText().toString());


                            dialog_box.dismiss();
                            ((Activity) context).recreate();
                        }

                    }
                });

                holder.change_picture_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_box.dismiss();
                        VendorStockDetails.getInstance().setCategory(stockCategory_list.get(position));
                        VendorStockDetails.getInstance().setKeyId(stockkeyId_list.get(position));
                        ImagePicker.Companion.with((Activity) context)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(333);
                    }
                });

                holder.cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_box.dismiss();
                    }
                });


                dialog_box.show();

            }

        });
    }


    @Override
    public int getItemCount() {

        return stockCategory_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CardView shop_items_cardView;
        ImageView stockImage_imageView;
        TextView stockName_textView, stockCategory_textView, stockPrice_textView, stockQuantity_textView;
        TextView currency_textView;
        TextView edit_stock_textview;
        TextView category_editText;
        EditText item_price_editText, item_quantity_edittext,item_name_editText;
        ImageView stock_pic;
        TextView change_picture_btn, save_btn, cancel_btn, remove_btn;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            stockName_textView = itemView.findViewById(R.id.stockName_textView);
            stockCategory_textView = itemView.findViewById(R.id.stockCategory_textView);
            stockPrice_textView = itemView.findViewById(R.id.stockPrice_textView);
            stockQuantity_textView = itemView.findViewById(R.id.stockQuantity_textView);
            currency_textView = itemView.findViewById(R.id.currency_textView);
            stockImage_imageView = itemView.findViewById(R.id.stockImage_imageView);
            shop_items_cardView = itemView.findViewById(R.id.shop_items_cardView);
            edit_stock_textview = itemView.findViewById(R.id.edit_stock_textview);

        }
    }
}

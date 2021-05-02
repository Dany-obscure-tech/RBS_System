package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_RBS_Vendor_placeorder_RecyclerView extends RecyclerView.Adapter<Adapter_RBS_Vendor_placeorder_RecyclerView.MyViewHolder> {
    Context context;
    List<String> placeorder_item_name_textView;
    List<String> placeorder_item_category_textView;
    List<String> place_order_price_edittext;
    List<String> place_order_quantity_editText;
    List<String> placeorder_item_pic_imageView;
    TextView totalBalance_textView;

    List<Boolean> validateList;

    float totalBalance = 0.0f, previousValue = 0.0f;

    public Adapter_RBS_Vendor_placeorder_RecyclerView(Context context, List<String> placeorder_item_name_textView, List<String> placeorder_item_category_textView, List<String> place_order_price_edittext, List<String> place_order_quantity_editText, List<String> placeorder_item_pic_imageView, TextView totalBalance_textView) {
        this.context = context;

        this.placeorder_item_name_textView = placeorder_item_name_textView;
        this.placeorder_item_category_textView = placeorder_item_category_textView;
        this.place_order_price_edittext = place_order_price_edittext;
        this.place_order_quantity_editText = place_order_quantity_editText;
        this.placeorder_item_pic_imageView = placeorder_item_pic_imageView;

        this.totalBalance_textView = totalBalance_textView;

        validateList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.rbs_vendor_placeorder_item, parent, false);
        Adapter_RBS_Vendor_placeorder_RecyclerView.MyViewHolder myViewHolder = new Adapter_RBS_Vendor_placeorder_RecyclerView.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        validateList.add(false);

        holder.placeorder_item_name_textView.setText(placeorder_item_name_textView.get(position));
        holder.placeorder_item_category_textView.setText(placeorder_item_category_textView.get(position));
        holder.placeorder_item_currency_textview.setText(Currency.getInstance().getCurrency());
        holder.place_order_price_textview.setText(place_order_price_edittext.get(position));
        holder.place_order_quantity_textView.setText(place_order_quantity_editText.get(position));
        Picasso.get().load(placeorder_item_pic_imageView.get(position)).into(holder.placeorder_item_pic_imageView);

        holder.requiredQuantity_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.requiredQuantity_editText.getText().toString().isEmpty()){
                    previousValue = 0.0f;
                }else {
                    previousValue = (Float.parseFloat(holder.requiredQuantity_editText.getText().toString()))*(Float.parseFloat(holder.place_order_price_textview.getText().toString()));
                }



            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (stockPrice_list.get(position).equals(item_price_editText.getText().toString())){
//                    item_price_editText.setError("Please enter new value!");
//                    validate = false;
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (holder.requiredQuantity_editText.getText().toString().isEmpty()){
                    holder.requiredQuantity_editText.setError("Please enter value!");
                    totalBalance = totalBalance - previousValue;
                    totalBalance_textView.setText(String.valueOf(totalBalance));
                    validateList.set(position,false);
                }else {
                    if (Float.parseFloat(holder.requiredQuantity_editText.getText().toString())==0.0f){
                        totalBalance = totalBalance - previousValue;
                        validateList.set(position,false);
                        holder.requiredQuantity_editText.setText("");
                    }else {
                        totalBalance = totalBalance - previousValue;
                        totalBalance= totalBalance + (Float.parseFloat(holder.requiredQuantity_editText.getText().toString())*Float.parseFloat(holder.place_order_price_textview.getText().toString()));
                        totalBalance_textView.setText(String.valueOf(totalBalance));
                        if (Float.parseFloat(holder.requiredQuantity_editText.getText().toString())>Float.parseFloat(holder.place_order_quantity_textView.getText().toString())){
                            holder.requiredQuantity_editText.setError("Quantity exceeds!");
                            validateList.set(position,false);
                        }else {
                            validateList.set(position,true);
                        }

                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return placeorder_item_name_textView.size();
    }

    public List<Boolean> getValidateList(){
        return validateList;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        
        ImageView placeorder_item_pic_imageView;
        TextView placeorder_item_name_textView, placeorder_item_category_textView, placeorder_item_currency_textview, place_order_price_textview, place_order_quantity_textView;
        EditText requiredQuantity_editText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            placeorder_item_name_textView = (TextView) itemView.findViewById(R.id.placeorder_item_name_textView);
            placeorder_item_category_textView = (TextView) itemView.findViewById(R.id.placeorder_item_category_textView);
            placeorder_item_currency_textview = (TextView) itemView.findViewById(R.id.placeorder_item_currency_textview);
            place_order_price_textview = (TextView) itemView.findViewById(R.id.place_order_price_textview);
            place_order_quantity_textView = (TextView) itemView.findViewById(R.id.place_order_quantity_textView);

            requiredQuantity_editText = (EditText) itemView.findViewById(R.id.requiredQuantity_editText);

            placeorder_item_pic_imageView = (ImageView) itemView.findViewById(R.id.placeorder_item_pic_imageView);

        }

    }

}

package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.AdapterOffersItemListRecyclerView;
import com.dotcom.rbs_system.Adapter.Adapter_Rbs_Vendor_order_receipt_RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Rbs_vendor_specific_order extends AppCompatActivity {
    ImageButton back_btn;
    TextView shopkeeper_name_textView,shopkeeper_phone_no_textView,shopkeeper_email_textView,shopkeeper_address_textView;
    String invoice_no_string,vendor_name_string,date_string;
    TextView vendor_name,date_TextView,invoice_no_textView;
    RecyclerView shopkeeper_order_details_recyclerview;
    List<String> placeorder_item_name_list, placeorder_item_category_list, placeorder_item_currency_textview, place_order_price_textview, place_order_quantity_textView, requiredQuantity_editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rbs_vendor_specific_order);
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        initialization();
        onclicklistners();
    }
///////////////////////////////////////////////////////////////////////////////////////////////////
    private void initialization() {
        vendor_name=(TextView)findViewById(R.id.vendor_name);
        date_TextView=(TextView)findViewById(R.id.date_TextView);
        invoice_no_textView=(TextView)findViewById(R.id.invoice_no_textView);
        shopkeeper_order_details_recyclerview=(RecyclerView)findViewById(R.id.shopkeeper_order_details_recyclerview);
        shopkeeper_name_textView=(TextView)findViewById(R.id.shopkeeper_name_textView);
        shopkeeper_phone_no_textView=(TextView)findViewById(R.id.shopkeeper_phone_no_textView);
        shopkeeper_email_textView=(TextView)findViewById(R.id.shopkeeper_email_textView);
        shopkeeper_address_textView=(TextView)findViewById(R.id.shopkeeper_address_textView);
        invoice_no_string = getIntent().getStringExtra("Invoice_no");
        vendor_name_string = getIntent().getStringExtra("Vendor_name");
        date_string = getIntent().getStringExtra("Date");
        vendor_name.setText(vendor_name_string);
        invoice_no_textView.setText(invoice_no_string);
        date_TextView.setText(date_string);

        placeorder_item_name_list = new ArrayList<String>();
        placeorder_item_category_list = new ArrayList<String>();
        placeorder_item_currency_textview = new ArrayList<String>();
        place_order_price_textview = new ArrayList<String>();
        place_order_quantity_textView = new ArrayList<String>();
        requiredQuantity_editText = new ArrayList<String>();
        placeorder_item_name_list.add("Samsung Note 8 Screen");
        placeorder_item_category_list.add("Mobile");
        placeorder_item_currency_textview.add("$");
        place_order_price_textview.add("8000");
        place_order_quantity_textView.add("2");
        requiredQuantity_editText.add("5");
        Adapter_Rbs_Vendor_order_receipt_RecyclerView adapter_rbs_vendor_order_receipt_recyclerView=new Adapter_Rbs_Vendor_order_receipt_RecyclerView(Rbs_vendor_specific_order.this,placeorder_item_name_list,null,null,null,null,null);
        shopkeeper_order_details_recyclerview.setLayoutManager(new GridLayoutManager(Rbs_vendor_specific_order.this,1));
        shopkeeper_order_details_recyclerview.setAdapter(adapter_rbs_vendor_order_receipt_recyclerView);

    }
///////////////////////////////////////////////////////////////////////////////////////////////////
    private void onclicklistners() {
        back_btn_listner();
    }

    private void back_btn_listner() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
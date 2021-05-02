package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.Adapter_RBS_Vendor_inventory_RecyclerView;
import com.dotcom.rbs_system.Adapter.Adapter_RBS_Vendor_placeorder_RecyclerView;
import com.dotcom.rbs_system.Classes.RBSVendorSelectedStock;

import java.util.ArrayList;
import java.util.List;

public class Rbs_vendor_order_receipt extends AppCompatActivity {
    RBSVendorSelectedStock rbsVendorSelectedStock;
    Adapter_RBS_Vendor_placeorder_RecyclerView adapter_rbs_vendor_placeorder_recyclerView;
    RecyclerView shopkeeper_invoice_details_recyclerview;

    TextView totalBalance_textView,balance_currency_textView;
    TextView confirm_order_btn;

    ImageButton back_btn;
    List<String> placeorder_item_name_list,placeorder_item_category_list,place_order_price_list,place_order_quantity_list,placeorder_item_pic_list;

    Boolean validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rbs_vendor_order_receipt);
        Initialization();
        Onclicklistners();

    }
///////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Initialization() {
        totalBalance_textView = (TextView)findViewById(R.id.totalBalance_textView);
        balance_currency_textView = (TextView)findViewById(R.id.balance_currency_textView);

        confirm_order_btn = (TextView)findViewById(R.id.confirm_order_btn);

        rbsVendorSelectedStock = RBSVendorSelectedStock.getInstance();

        placeorder_item_name_list = rbsVendorSelectedStock.getVendor_stockName_textView();
        placeorder_item_category_list = rbsVendorSelectedStock.getVendor_stock_category_textView();
        place_order_price_list = rbsVendorSelectedStock.getVendor_stock_price_textview();
        place_order_quantity_list = rbsVendorSelectedStock.getVendor_stock_quantity_textView();
        placeorder_item_pic_list = rbsVendorSelectedStock.getVendor_stock_imageView();

        back_btn = (ImageButton) findViewById(R.id.back_btn);

        adapter_rbs_vendor_placeorder_recyclerView = new Adapter_RBS_Vendor_placeorder_RecyclerView(Rbs_vendor_order_receipt.this, placeorder_item_name_list, placeorder_item_category_list, place_order_price_list, place_order_quantity_list, placeorder_item_pic_list,totalBalance_textView);

        shopkeeper_invoice_details_recyclerview = (RecyclerView) findViewById(R.id.shopkeeper_invoice_details_recyclerview);
        shopkeeper_invoice_details_recyclerview.setLayoutManager(new GridLayoutManager(Rbs_vendor_order_receipt.this, 1));
        shopkeeper_invoice_details_recyclerview.setAdapter(adapter_rbs_vendor_placeorder_recyclerView);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Onclicklistners() {
        back_btn_listner();
        confirmButtonListener();
    }

    private void confirmButtonListener() {
        confirm_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate = true;
                for (int i = 0;i<adapter_rbs_vendor_placeorder_recyclerView.getValidateList().size();i++){
                    if (!adapter_rbs_vendor_placeorder_recyclerView.getValidateList().get(i)){
                        validate = false;
                    }
                }
                if (!validate){
                    Toast.makeText(Rbs_vendor_order_receipt.this, "Please provide valid quantities", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.dotcom.rbs_system.Adapter.Adapter_RBS_Vendor_inventory_RecyclerView;
import com.dotcom.rbs_system.Adapter.Adapter_RBS_Vendor_placeorder_RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Rbs_vendor_order extends AppCompatActivity {

    RecyclerView shopkeeper_invoice_details_recyclerview;
    ImageButton back_btn;
    List<String> placeorder_item_name_list,placeorder_item_category_list,placeorder_item_currency_list,place_order_price_list,place_order_quantity_list,placeorder_item_pic_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rbs_vendor_order);
        Initialization();
        Onclicklistners();

        Adapter_RBS_Vendor_placeorder_RecyclerView adapter_rbs_vendor_placeorder_recyclerView = new Adapter_RBS_Vendor_placeorder_RecyclerView(Rbs_vendor_order.this, placeorder_item_name_list, placeorder_item_category_list, placeorder_item_currency_list, place_order_price_list, place_order_quantity_list, placeorder_item_pic_list);
        shopkeeper_invoice_details_recyclerview.setAdapter(adapter_rbs_vendor_placeorder_recyclerView);

    }
///////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Initialization() {
        placeorder_item_name_list = new ArrayList<>();
        placeorder_item_category_list = new ArrayList<>();
        placeorder_item_currency_list = new ArrayList<>();
        place_order_price_list = new ArrayList<>();
        place_order_quantity_list = new ArrayList<>();
        placeorder_item_pic_list = new ArrayList<>();
        placeorder_item_name_list.add("Samsung C7 Screens");
        placeorder_item_category_list.add("Mobile");
        placeorder_item_currency_list.add("$");
        place_order_price_list.add("10500");
        place_order_quantity_list.add("10");

        placeorder_item_pic_list.add("https://samsungmobilespecs.com/wp-content/uploads/2018/03/Samsung-Galaxy-C7-Price-Specs-featured-581x571.jpg");

        shopkeeper_invoice_details_recyclerview = (RecyclerView) findViewById(R.id.shopkeeper_invoice_details_recyclerview);
        back_btn = (ImageButton) findViewById(R.id.back_btn);
        shopkeeper_invoice_details_recyclerview.setLayoutManager(new GridLayoutManager(Rbs_vendor_order.this, 1));
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Onclicklistners() {
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
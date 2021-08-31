package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.Adapter_RBS_accessories_inventory_RecyclerView;
import com.dotcom.rbs_system.Adapter.Adapter_Vendor_inventory_RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Accessory extends AppCompatActivity {

    RecyclerView shopkeeper_accessories_stock_recyclerview;
    TextView add_accessory_textview;
    ImageButton back_btn;
    CardView order_accessory_textview,sale_accessory_textview;
    Adapter_RBS_accessories_inventory_RecyclerView adapter_rbs_accessories_inventory_recyclerView;
    List<String> accessory_name_textView, accessory_inventory_Category_textView, stockPrice_list, accessory_inventory_Quantity_textView, stockkeyId_list, edit_stock_textview_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory);

        initialization();
        onclicklistners();


    }

    private void onclicklistners() {
        order_accessory_textview_listner();
        sale_accessory_textview_listner();
        add_accessory_textview_listner();
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

    private void add_accessory_textview_listner() {
        add_accessory_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Accessory.this, Accessory_add.class);
                startActivity(intent);
            }
        });
    }

    private void sale_accessory_textview_listner() {
        sale_accessory_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Accessory.this, Accessory_sale.class);
                startActivity(intent);
            }
        });
    }

    private void order_accessory_textview_listner() {
        order_accessory_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Accessory.this, RBS_Vendors.class);
                startActivity(intent);
            }
        });
    }

    private void initialization() {
        accessory_name_textView = new ArrayList<>();
        stockPrice_list = new ArrayList<>();
        accessory_inventory_Quantity_textView = new ArrayList<>();
        stockkeyId_list = new ArrayList<>();
        edit_stock_textview_list = new ArrayList<>();
        accessory_inventory_Category_textView = new ArrayList<>();
        accessory_inventory_Category_textView.add("Laptop");
        accessory_name_textView.add("Asus ROG");
        stockPrice_list.add("1500");
        accessory_inventory_Quantity_textView.add("10");
        stockkeyId_list.add("ID");
        //TODO id ko check kar ley
        shopkeeper_accessories_stock_recyclerview = findViewById(R.id.shopkeeper_accessories_stock_recyclerview);
        shopkeeper_accessories_stock_recyclerview.setLayoutManager(new GridLayoutManager(Accessory.this, 1));
        order_accessory_textview = findViewById(R.id.order_accessory_textview);
        sale_accessory_textview = findViewById(R.id.sale_accessory_textview);
        add_accessory_textview = findViewById(R.id.add_accessory_textview);
        back_btn = findViewById(R.id.back_btn);
        adapter_rbs_accessories_inventory_recyclerView = new Adapter_RBS_accessories_inventory_RecyclerView(Accessory.this, accessory_name_textView, accessory_inventory_Category_textView, stockPrice_list, accessory_inventory_Quantity_textView, stockkeyId_list, null);
        shopkeeper_accessories_stock_recyclerview.setAdapter(adapter_rbs_accessories_inventory_recyclerView);
    }
}
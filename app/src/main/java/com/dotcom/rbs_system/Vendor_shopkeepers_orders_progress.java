package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.Adapter_Vendor_order_list_RecyclerView;
import com.dotcom.rbs_system.Adapter.Adapter_Vendor_shopkeepers_order_progress_RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Vendor_shopkeepers_orders_progress extends AppCompatActivity {
    String shopname_getting;
    TextView shop_name;
    ImageButton back_btn;
    RecyclerView shopkeeper_invoice_details_recyclerview;
    List<String> accessory_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_shopkeepers_orders_progress);
        Initialization();
        Onclicklistners();

        shop_name.setText(shopname_getting);
    }

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

    private void Initialization() {
        shopname_getting = getIntent().getStringExtra("Shop_Name");
        shopkeeper_invoice_details_recyclerview=(RecyclerView)findViewById(R.id.shopkeeper_invoice_details_recyclerview);
        shop_name=(TextView)findViewById(R.id.shop_name);
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        accessory_name=new ArrayList<>();
        accessory_name.add("Samsung Note 8 Screen");
        accessory_name.add("Samsung Note 7 Screen");
        accessory_name.add("Samsung Note 10 Screen");
        Adapter_Vendor_shopkeepers_order_progress_RecyclerView adapter_vendor_shopkeepers_order_progress_recyclerView=new Adapter_Vendor_shopkeepers_order_progress_RecyclerView(Vendor_shopkeepers_orders_progress.this,null,accessory_name,null,null,null,null);

        shopkeeper_invoice_details_recyclerview.setLayoutManager(new GridLayoutManager(Vendor_shopkeepers_orders_progress.this,1));
        shopkeeper_invoice_details_recyclerview.setAdapter(adapter_vendor_shopkeepers_order_progress_recyclerView);


    }
}
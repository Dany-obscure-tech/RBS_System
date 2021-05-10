package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.Adapter_Vendor_shopkeepers_order_progress_RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Vendor_shopkeepers_orders_progress extends AppCompatActivity {
    String shopname_getting;
    TextView shop_name;
    ImageButton back_btn;
    RecyclerView order_invoice_recyclerview;
    List<String> sr_no_order,accessory_name,rate_currency,rate_price,order_qty,total_price;

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
        order_invoice_recyclerview =(RecyclerView)findViewById(R.id.order_invoice_recyclerview);
        shop_name=(TextView)findViewById(R.id.shop_name);
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        sr_no_order=new ArrayList<>();
        accessory_name=new ArrayList<>();
        rate_currency=new ArrayList<>();
        rate_price=new ArrayList<>();
        order_qty=new ArrayList<>();
        total_price=new ArrayList<>();
        accessory_name.add("Itech Computers");
        accessory_name.add("Phi traders");
        accessory_name.add("Phi Horizon Company");
        Adapter_Vendor_shopkeepers_order_progress_RecyclerView adapter_vendor_shopkeepers_order_progress_recyclerView=new Adapter_Vendor_shopkeepers_order_progress_RecyclerView(Vendor_shopkeepers_orders_progress.this,null,accessory_name,null,null,null);

        order_invoice_recyclerview.setLayoutManager(new GridLayoutManager(Vendor_shopkeepers_orders_progress.this,1));
        order_invoice_recyclerview.setAdapter(adapter_vendor_shopkeepers_order_progress_recyclerView);


    }
}
package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.Adapter_Vendor_order_list_RecyclerView;
import com.dotcom.rbs_system.Adapter.Adapter_Vendor_shopkeeper_invoices_RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VendorShopkeepersShopReceipts extends AppCompatActivity {
    String shopname_getting;
    TextView vendor_shop_name;
    ImageButton back_btn;
    RecyclerView shopkeeper_invoices_recyclerview;
    List<String> shopkeeper_invoice_no,amount_currency,amount_price,invoice_date,balance_currency,shopkeeper_balance,paid_currency,shopkeeper_paid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_shopkeepers_shop_receipts);
        Initialization();
        Onclicklistners();

        vendor_shop_name.setText(shopname_getting);


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
        vendor_shop_name=(TextView)findViewById(R.id.vendor_shop_name);
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        shopkeeper_invoices_recyclerview=(RecyclerView) findViewById(R.id.shopkeeper_invoices_recyclerview);
        shopkeeper_invoice_no=new ArrayList<>();
        amount_currency=new ArrayList<>();
        amount_price=new ArrayList<>();
        invoice_date=new ArrayList<>();
        balance_currency=new ArrayList<>();
        shopkeeper_balance=new ArrayList<>();
        paid_currency=new ArrayList<>();
        shopkeeper_balance=new ArrayList<>();
        shopkeeper_paid=new ArrayList<>();
        shopkeeper_paid.add("ITECH Computers");
        shopkeeper_paid.add("Forex Trading");
        Adapter_Vendor_shopkeeper_invoices_RecyclerView adapter_vendor_shopkeeper_invoices_recyclerView=new Adapter_Vendor_shopkeeper_invoices_RecyclerView(VendorShopkeepersShopReceipts.this,null,null,null,null,null,null,null,shopkeeper_paid);

        shopkeeper_invoices_recyclerview.setLayoutManager(new GridLayoutManager(VendorShopkeepersShopReceipts.this,1));
        shopkeeper_invoices_recyclerview.setAdapter(adapter_vendor_shopkeeper_invoices_recyclerView);


    }
}
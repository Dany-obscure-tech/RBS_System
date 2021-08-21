package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.Adapter_Vendor_shopkeepers_order_progress_RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Vendor_shopkeepers_orders_progress extends AppCompatActivity {
    String shopKeeperID,invoiceID;
    TextView shop_name_textView,phone_no_textview,email_textView,address_textView;
    ImageButton back_btn;
    RecyclerView order_invoice_recyclerview;
    List<String> accessory_name,rate_price,order_qty,image_url;
    DatabaseReference invoiceRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_shopkeepers_orders_progress);
        Initialization();
        InitialProcess();
        Onclicklistners();

    }
    /////////////////////////////////////////////////////////////////
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

    /////////////////////////////////////////////////////////////////

    private void Initialization() {
        shopKeeperID = getIntent().getStringExtra("Shopkeeper_ID");
        invoiceID = getIntent().getStringExtra("Invoice_keyID");

        invoiceRef = FirebaseDatabase.getInstance().getReference("Accessories_orders/"+invoiceID);

        order_invoice_recyclerview =(RecyclerView)findViewById(R.id.order_invoice_recyclerview);
        shop_name_textView =(TextView)findViewById(R.id.shop_name_textView);
        phone_no_textview =(TextView)findViewById(R.id.phone_no_textview);
        email_textView =(TextView)findViewById(R.id.email_textView);
        address_textView =(TextView)findViewById(R.id.address_textView);
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        accessory_name=new ArrayList<>();
        rate_price=new ArrayList<>();
        order_qty=new ArrayList<>();
        image_url=new ArrayList<>();

    }

    //////////////////////////////////////////////////////////////////////


    private void InitialProcess() {
        datafetch();
    }

    private void datafetch() {
        invoiceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shop_name_textView.setText(snapshot.child("shopkeeper_name").getValue().toString());
                phone_no_textview.setText(snapshot.child("shopkeeper_phoneno").getValue().toString());
                email_textView.setText(snapshot.child("shopkeeper_email").getValue().toString());
                address_textView.setText(snapshot.child("shopkeeper_address").getValue().toString());

                for (DataSnapshot snapshot1: snapshot.child("items_list").getChildren()){
                    accessory_name.add(snapshot1.child("item_name").getValue().toString());
                    rate_price.add(snapshot1.child("item_unitPrice").getValue().toString());
                    order_qty.add(snapshot1.child("item_quantity").getValue().toString());
                    image_url.add(snapshot1.child("item_imageUrl").getValue().toString());
                }

                Adapter_Vendor_shopkeepers_order_progress_RecyclerView adapter_vendor_shopkeepers_order_progress_recyclerView=new Adapter_Vendor_shopkeepers_order_progress_RecyclerView(Vendor_shopkeepers_orders_progress.this,accessory_name,rate_price,order_qty,image_url);
                order_invoice_recyclerview.setLayoutManager(new GridLayoutManager(Vendor_shopkeepers_orders_progress.this,1));
                order_invoice_recyclerview.setAdapter(adapter_vendor_shopkeepers_order_progress_recyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
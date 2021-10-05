package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.Adapter_Vendor_ShopkeeperDetails_order_list_RecyclerView;
import com.dotcom.rbs_system.Adapter.Adapter_Vendor_order_list_RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VendorShopkeepersShopReceipts extends AppCompatActivity {
    String shopKeyID;
    TextView shop_name_textView;
    TextView shop_phno_textView;
    TextView shop_email_textView;
    TextView shop_address_textView;
    ImageButton back_btn;
    RecyclerView orders_list_recyclerview;
    Adapter_Vendor_ShopkeeperDetails_order_list_RecyclerView adapter_Vendor_ShopkeeperDetails_order_list_RecyclerView;
    DatabaseReference Vendor_orderRef,shopkeeperDetailsRef;

    List<String> order_no_vendor;
    List<String> date;
    List<String> totalAmount;
    List<String> vendor_order_status;
    List<String> shopKeeper_keyID;
    List<String> invoiceKeyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_shopkeepers_shop_receipts);
        Initialization();
        Onclicklistners();

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
        shopKeyID = getIntent().getStringExtra("Shop_keyID");
        Vendor_orderRef = FirebaseDatabase.getInstance().getReference("Vendor_order/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        shopkeeperDetailsRef = FirebaseDatabase.getInstance().getReference("Users_data/" + shopKeyID + "/Shopkeeper_details");

        back_btn = findViewById(R.id.back_btn);

        shop_name_textView = findViewById(R.id.shop_name_textView);
        shop_phno_textView = findViewById(R.id.shop_phno_textView);
        shop_email_textView = findViewById(R.id.shop_email_textView);
        shop_address_textView = findViewById(R.id.shop_address_textView);

        orders_list_recyclerview = findViewById(R.id.orders_list_recyclerview);

        order_no_vendor = new ArrayList<>();
        date = new ArrayList<>();
        totalAmount = new ArrayList<>();
        vendor_order_status = new ArrayList<>();
        shopKeeper_keyID = new ArrayList<>();
        invoiceKeyID = new ArrayList<>();

        initialProcess();

    }

    private void initialProcess() {
        datafetch();
    }

    private void datafetch() {
        shopkeeperDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shop_name_textView.setText(snapshot.child("shop_name").getValue().toString());
                shop_phno_textView.setText(snapshot.child("shop_phno").getValue().toString());
                shop_email_textView.setText(snapshot.child("shop_email").getValue().toString());
                shop_address_textView.setText(snapshot.child("shop_address").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Vendor_orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    if (snapshot1.child("shopkeeper_keyID").getValue().toString().equals(shopKeyID)){
                        order_no_vendor.add(snapshot1.child("invoice_no").getValue().toString());
                        date.add(snapshot1.child("date").getValue().toString());
                        totalAmount.add(snapshot1.child("totalBalance").getValue().toString());
                        vendor_order_status.add(snapshot1.child("status").getValue().toString());
                        shopKeeper_keyID.add(snapshot1.child("shopkeeper_keyID").getValue().toString());
                        invoiceKeyID.add(snapshot1.child("invoice_keyId").getValue().toString());
                    }

                }

                adapter_Vendor_ShopkeeperDetails_order_list_RecyclerView = new Adapter_Vendor_ShopkeeperDetails_order_list_RecyclerView(VendorShopkeepersShopReceipts.this, order_no_vendor, date, totalAmount, vendor_order_status, shopKeeper_keyID, invoiceKeyID);
                orders_list_recyclerview.setLayoutManager(new GridLayoutManager(VendorShopkeepersShopReceipts.this, 1));
                orders_list_recyclerview.setAdapter(adapter_Vendor_ShopkeeperDetails_order_list_RecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
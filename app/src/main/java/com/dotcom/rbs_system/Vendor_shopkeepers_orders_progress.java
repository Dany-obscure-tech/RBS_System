package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.Adapter_Vendor_shopkeepers_order_progress_RecyclerView;
import com.dotcom.rbs_system.Classes.Currency;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Vendor_shopkeepers_orders_progress extends AppCompatActivity {
    String userID, invoiceID,status,check;
    TextView shop_name_textView, phone_no_textview, email_textView, address_textView;
    TextView total_price_TextView, total_price_currency_TextView;
    TextView balance_currency,balance_price_TextView,status_textView;
    ImageButton back_btn;
    RecyclerView order_invoice_recyclerview;
    List<String> accessory_name, rate_price, order_qty, image_url;
    List<String> accessory_category,accessory_keyId;
    List<Integer> oldQuantityList;
    TextView readyOrder_textView,confirm_order_btn,cancel_order_btn;

    EditText discount_editText;

    DatabaseReference accessoriesOrdersList, vendorStockRef;
    DatabaseReference vendorOrderRef, shopkeeperVendorOrderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_shopkeepers_orders_progress);
        Initialization();
        InitialProcess();
        MidProcess();
        Onclicklistners();

    }

    private void Initialization() {
        userID = getIntent().getStringExtra("User_ID");
        invoiceID = getIntent().getStringExtra("Invoice_keyID");
        status = getIntent().getStringExtra("status");
        check = getIntent().getStringExtra("userType");

        if (check.equals("vendor")){
            vendorOrderRef = FirebaseDatabase.getInstance().getReference("Vendor_order/" + FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+invoiceID);
            shopkeeperVendorOrderRef = FirebaseDatabase.getInstance().getReference("Shopkeeper_vendor_order/" + userID +"/"+invoiceID);
            vendorStockRef = FirebaseDatabase.getInstance().getReference("Vendor_stock/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        }else if (check.equals("shopkeeper")){
            vendorOrderRef = FirebaseDatabase.getInstance().getReference("Vendor_order/" + userID+"/"+invoiceID);
            shopkeeperVendorOrderRef = FirebaseDatabase.getInstance().getReference("Shopkeeper_vendor_order/" +  FirebaseAuth.getInstance().getCurrentUser().getUid() +"/"+invoiceID);
        }
        accessoriesOrdersList = FirebaseDatabase.getInstance().getReference("Accessories_orders/" + invoiceID);

        order_invoice_recyclerview = findViewById(R.id.order_invoice_recyclerview);
        shop_name_textView = findViewById(R.id.shop_name_textView);
        phone_no_textview = findViewById(R.id.phone_no_textview);
        email_textView = findViewById(R.id.email_textView);
        address_textView = findViewById(R.id.address_textView);
        back_btn = findViewById(R.id.back_btn);
        readyOrder_textView = findViewById(R.id.readyOrder_textView);
        confirm_order_btn = findViewById(R.id.confirm_order_btn);
        cancel_order_btn = findViewById(R.id.cancel_order_btn);
        total_price_TextView = findViewById(R.id.total_price_TextView);
        balance_price_TextView = findViewById(R.id.balance_price_TextView);
        status_textView = findViewById(R.id.status_textView);
        status_textView.setText(status);
        total_price_currency_TextView = findViewById(R.id.total_price_currency_TextView);
        balance_currency = findViewById(R.id.balance_currency);
        discount_editText = findViewById(R.id.discount_editText);
        total_price_currency_TextView.setText(Currency.getInstance().getCurrency());
        balance_currency.setText(Currency.getInstance().getCurrency());
        accessory_name = new ArrayList<>();
        rate_price = new ArrayList<>();
        order_qty = new ArrayList<>();
        image_url = new ArrayList<>();
        accessory_category = new ArrayList<>();
        accessory_keyId = new ArrayList<>();

        oldQuantityList = new ArrayList<>();

        if (status.equals("Pending")){
            if (check.equals("vendor")){
                confirm_order_btn.setVisibility(View.GONE);
                cancel_order_btn.setVisibility(View.VISIBLE);
            }else {
                readyOrder_textView.setVisibility(View.GONE);
                confirm_order_btn.setVisibility(View.GONE);
                cancel_order_btn.setVisibility(View.VISIBLE);
            }

        } else if (status.equals("Ready")){
            if (check.equals("vendor")) {
                readyOrder_textView.setVisibility(View.GONE);
                cancel_order_btn.setVisibility(View.VISIBLE);
            }else {
                readyOrder_textView.setVisibility(View.GONE);
                confirm_order_btn.setVisibility(View.GONE);
                cancel_order_btn.setVisibility(View.GONE);
            }
        } else if (status.equals("Complete")){
            readyOrder_textView.setVisibility(View.GONE);
            confirm_order_btn.setVisibility(View.GONE);
            cancel_order_btn.setVisibility(View.GONE);
        }

    }

    //////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    private void Onclicklistners() {
        back_btn_listner();
        readyOrder_textView_listener();
        confirm_order_btn_listener();
        cancel_order_btn_listener();
    }

    private void cancel_order_btn_listener() {
        cancel_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vendorOrderRef.removeValue();
                shopkeeperVendorOrderRef.removeValue();
                accessoriesOrdersList.removeValue();

                Intent intent = new Intent();
                setResult(111,intent);
                finish();

            }
        });
    }

    private void confirm_order_btn_listener() {
        confirm_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!discount_editText.getText().toString().isEmpty()){
                    accessoriesOrdersList.child("discount").setValue(Float.parseFloat(discount_editText.getText().toString()));
                }
                vendorOrderRef.child("totalBalance").setValue(Float.parseFloat(balance_price_TextView.getText().toString()));
                vendorOrderRef.child("status").setValue("Complete");

                shopkeeperVendorOrderRef.child("totalBalance").setValue(Float.parseFloat(balance_price_TextView.getText().toString()));
                shopkeeperVendorOrderRef.child("status").setValue("Complete");

                accessoriesOrdersList.child("totalBalance").setValue(Float.parseFloat(balance_price_TextView.getText().toString()));
                accessoriesOrdersList.child("status").setValue("Complete");

                vendorStockRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        System.out.println(snapshot.toString());
                        for (int i = 0;i<accessory_name.size();i++){
                            String value = snapshot.child(accessory_category.get(i)).child(accessory_keyId.get(i)).child("Quantity").getValue().toString();

                            oldQuantityList.add(Integer.valueOf(value));

                        }

                        for (int i = 0;i < accessory_name.size();i++){
                            int value = oldQuantityList.get(i)-Integer.parseInt(order_qty.get(i));
                            if (value<0){
                                value = 0;
                            }
                            vendorStockRef.child(accessory_category.get(i)).child(accessory_keyId.get(i)).child("Quantity").setValue(value);

                        }

                        Intent intent = new Intent();
                        setResult(111,intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    private void readyOrder_textView_listener() {
        readyOrder_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vendorOrderRef.child("status").setValue("Ready");
                shopkeeperVendorOrderRef.child("status").setValue("Ready");
                accessoriesOrdersList.child("status").setValue("Ready");

                Intent intent = new Intent();
                setResult(111,intent);
                finish();
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

    /////////////////////////////////////////////////////////////////

    private void InitialProcess() {
        datafetch();
    }

    private void datafetch() {
        accessoriesOrdersList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (check.equals("vendor")){
                    shop_name_textView.setText(snapshot.child("shopkeeper_name").getValue().toString());
                    phone_no_textview.setText(snapshot.child("shopkeeper_phoneno").getValue().toString());
                    email_textView.setText(snapshot.child("shopkeeper_email").getValue().toString());
                    address_textView.setText(snapshot.child("shopkeeper_address").getValue().toString());
                }else if (check.equals("shopkeeper")){
                    shop_name_textView.setText(snapshot.child("vendor_name").getValue().toString());
                    phone_no_textview.setText(snapshot.child("vendor_phoneno").getValue().toString());
                    email_textView.setText(snapshot.child("vendor_email").getValue().toString());
                    address_textView.setText(snapshot.child("vendor_address").getValue().toString());
                }

                total_price_TextView.setText(snapshot.child("totalBalance").getValue().toString());
                balance_price_TextView.setText(snapshot.child("totalBalance").getValue().toString());

                for (DataSnapshot snapshot1 : snapshot.child("items_list").getChildren()) {
                    accessory_name.add(snapshot1.child("item_name").getValue().toString());
                    rate_price.add(snapshot1.child("item_unitPrice").getValue().toString());
                    order_qty.add(snapshot1.child("item_quantity").getValue().toString());
                    image_url.add(snapshot1.child("item_imageUrl").getValue().toString());
                    accessory_keyId.add(snapshot1.child("item_keyId").getValue().toString());
                    accessory_category.add(snapshot1.child("item_category").getValue().toString());

                }

                Adapter_Vendor_shopkeepers_order_progress_RecyclerView adapter_vendor_shopkeepers_order_progress_recyclerView = new Adapter_Vendor_shopkeepers_order_progress_RecyclerView(Vendor_shopkeepers_orders_progress.this, accessory_name, rate_price, order_qty, image_url, userID, invoiceID,status,check);
                order_invoice_recyclerview.setLayoutManager(new GridLayoutManager(Vendor_shopkeepers_orders_progress.this, 1));
                order_invoice_recyclerview.setAdapter(adapter_vendor_shopkeepers_order_progress_recyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /////////////////////////////////////////////////////////////////

    private void MidProcess() {
        applyDiscount();
    }

    private void applyDiscount() {

        discount_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!discount_editText.getText().toString().isEmpty()){
                    if (Float.parseFloat(discount_editText.getText().toString())==0){
                        discount_editText.removeTextChangedListener(this);
                        discount_editText.setText("");
                        discount_editText.setSelection(discount_editText.getText().toString().length());
                        applyDiscount();
                    }else {
                        float discountValue=0.0f,totalValue;
                        discountValue = Float.parseFloat(discount_editText.getText().toString());
                        discount_editText.removeTextChangedListener(this);

                        if (discountValue>=100){
                            discountValue=100.0f;
                            discount_editText.setText("100");
                        }
                        totalValue = Float.parseFloat(total_price_TextView.getText().toString());
                        balance_price_TextView.setText(String.valueOf((totalValue-(discountValue/100)*totalValue)));
                        applyDiscount();
                    }
                }else {
                    discount_editText.removeTextChangedListener(this);
                    discount_editText.setText("");
                    discount_editText.setSelection(discount_editText.getText().toString().length());
                    applyDiscount();
                }

            }
        });
    }

}
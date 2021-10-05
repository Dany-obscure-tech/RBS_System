package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.Adapter_Vendor_shopkeepers_order_progress_RecyclerView;
import com.dotcom.rbs_system.Classes.AccessoryList;
import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.Classes.InvoiceNumberGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Vendor_shopkeepers_orders_progress extends AppCompatActivity {
    String userID, invoiceID, invoiceNo, status, check;
    TextView shop_name_textView, phone_no_textview, email_textView, address_textView;
    TextView total_price_TextView, total_price_currency_TextView;
    TextView balance_currency, balance_price_TextView,remainingBalance_price_TextView, status_textView;
    TextView message_textView;
    ImageButton back_btn;
    RecyclerView order_invoice_recyclerview;
    List<String> accessory_name, rate_price, order_qty, image_url;
    List<String> accessory_category, accessory_keyId;
    List<Integer> oldQuantityList;
    TextView readyOrder_textView, confirm_order_btn, cancel_order_btn;
    TextView invoiceNo_TextView;
    LinearLayout entry_area,paidEntry_area;


    List<String> shopkeeperOldAccessory_keyId;

    List<Integer> shopKeeperNewQuantityList;
    List<String> shopkeeperNewAccessory_keyId;
    List<String> shopkeeperNewAccessory_category;
    List<Integer> shopkeeperNewAccessory_quantity;
    List<String> shopkeeperNewAccessory_name;
    List<String> shopkeeperNewAccessory_price;

    EditText discount_editText;
    EditText paid_editText;

    DatabaseReference accessoriesOrdersList, vendorStockRef, shopkeeperStockRef;
    DatabaseReference vendorOrderRef, shopkeeperVendorOrderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_shopkeepers_orders_progress);
        Initialization();
        try {
            generateInvoiceNo();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        InitialProcess();
        textWatchers();
        Onclicklistners();

    }

    private void Initialization() {
        userID = getIntent().getStringExtra("User_ID");
        invoiceID = getIntent().getStringExtra("Invoice_keyID");
        invoiceNo = getIntent().getStringExtra("Invoice_no");
        status = getIntent().getStringExtra("status");
        check = getIntent().getStringExtra("userType");

        if (check.equals("vendor")) {
            vendorOrderRef = FirebaseDatabase.getInstance().getReference("Vendor_order/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + invoiceID);
            shopkeeperVendorOrderRef = FirebaseDatabase.getInstance().getReference("Shopkeeper_vendor_order/" + userID + "/" + invoiceID);
            shopkeeperStockRef = FirebaseDatabase.getInstance().getReference("ShopKeeper_accessories/" + userID);

            vendorStockRef = FirebaseDatabase.getInstance().getReference("Vendor_stock/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        } else if (check.equals("shopkeeper")) {
            vendorOrderRef = FirebaseDatabase.getInstance().getReference("Vendor_order/" + userID + "/" + invoiceID);
            shopkeeperVendorOrderRef = FirebaseDatabase.getInstance().getReference("Shopkeeper_vendor_order/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + invoiceID);
        }
        accessoriesOrdersList = FirebaseDatabase.getInstance().getReference("Accessories_orders/" + invoiceID);

        entry_area = findViewById(R.id.entry_area);
        paidEntry_area = findViewById(R.id.paidEntry_area);

        order_invoice_recyclerview = findViewById(R.id.order_invoice_recyclerview);
        shop_name_textView = findViewById(R.id.shop_name_textView);
        phone_no_textview = findViewById(R.id.phone_no_textview);
        email_textView = findViewById(R.id.email_textView);
        address_textView = findViewById(R.id.address_textView);
        invoiceNo_TextView = (TextView) findViewById(R.id.invoiceNo_TextView);
        back_btn = findViewById(R.id.back_btn);
        readyOrder_textView = findViewById(R.id.readyOrder_textView);
        confirm_order_btn = findViewById(R.id.confirm_order_btn);
        cancel_order_btn = findViewById(R.id.cancel_order_btn);
        total_price_TextView = findViewById(R.id.total_price_TextView);
        balance_price_TextView = findViewById(R.id.balance_price_TextView);
        remainingBalance_price_TextView = findViewById(R.id.remainingBalance_price_TextView);
        message_textView = findViewById(R.id.message_textView);
        status_textView = findViewById(R.id.status_textView);
        status_textView.setText(status);
        total_price_currency_TextView = findViewById(R.id.total_price_currency_TextView);
        balance_currency = findViewById(R.id.balance_currency);
        discount_editText = findViewById(R.id.discount_editText);
        paid_editText = findViewById(R.id.paid_editText);
        total_price_currency_TextView.setText(Currency.getInstance().getCurrency());
        balance_currency.setText(Currency.getInstance().getCurrency());
        accessory_name = new ArrayList<>();
        rate_price = new ArrayList<>();
        order_qty = new ArrayList<>();
        image_url = new ArrayList<>();
        accessory_category = new ArrayList<>();
        accessory_keyId = new ArrayList<>();

        shopkeeperOldAccessory_keyId = new ArrayList<>();

        shopKeeperNewQuantityList = new ArrayList<>();
        shopkeeperNewAccessory_keyId = new ArrayList<>();
        shopkeeperNewAccessory_category = new ArrayList<>();
        shopkeeperNewAccessory_quantity = new ArrayList<>();
        shopkeeperNewAccessory_name = new ArrayList<>();
        shopkeeperNewAccessory_price = new ArrayList<>();

        oldQuantityList = new ArrayList<>();

        if (status.equals("Pending")) {
            if (check.equals("vendor")) {
                confirm_order_btn.setVisibility(View.GONE);
                cancel_order_btn.setVisibility(View.VISIBLE);
                entry_area.setVisibility(View.GONE);
                paidEntry_area.setVisibility(View.GONE);
            } else {
                readyOrder_textView.setVisibility(View.GONE);
                confirm_order_btn.setVisibility(View.GONE);
                cancel_order_btn.setVisibility(View.VISIBLE);
                entry_area.setVisibility(View.GONE);
                paidEntry_area.setVisibility(View.GONE);
            }

        } else if (status.equals("Ready")) {
            if (check.equals("vendor")) {
                readyOrder_textView.setVisibility(View.GONE);
                cancel_order_btn.setVisibility(View.VISIBLE);
            } else {
                readyOrder_textView.setVisibility(View.GONE);
                confirm_order_btn.setVisibility(View.GONE);
                cancel_order_btn.setVisibility(View.GONE);
                entry_area.setVisibility(View.GONE);
                paidEntry_area.setVisibility(View.GONE);
            }
        } else if (status.equals("Complete")) {
            discount_editText.setEnabled(false);
            paid_editText.setEnabled(false);
            readyOrder_textView.setVisibility(View.GONE);
            confirm_order_btn.setVisibility(View.GONE);
            cancel_order_btn.setVisibility(View.GONE);
        }
        else if (status.equals("Canceled")) {
            paidEntry_area.setVisibility(View.GONE);
            entry_area.setVisibility(View.GONE);
            paid_editText.setEnabled(false);
            readyOrder_textView.setVisibility(View.GONE);
            confirm_order_btn.setVisibility(View.GONE);
            cancel_order_btn.setVisibility(View.GONE);
        }

    }

    private void generateInvoiceNo() throws ParseException {
        invoiceNo_TextView.setText(new InvoiceNumberGenerator().generateNumber());
    }

    //////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    private void Onclicklistners() {
        back_btn_listner();
        readyOrder_textView_listener();
        confirm_order_btn_listener();
        cancel_order_btn_listener();
        message_textView_btn_listener();
    }

    private void message_textView_btn_listener() {
        message_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Vendor_shopkeepers_orders_progress.this, Vendor_messaging.class);

                if (check.equals("vendor")) {
                    intent.putExtra("SHOPKEEPER_ID", userID);
                    intent.putExtra("VENDOR_ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } else if (check.equals("shopkeeper")) {
                    intent.putExtra("SHOPKEEPER_ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    intent.putExtra("VENDOR_ID", userID);
                }
                intent.putExtra("INVOICE_KEYID", invoiceID);
                intent.putExtra("INVOICE_NO", invoiceNo);
                startActivity(intent);
            }
        });
    }

    private void cancel_order_btn_listener() {
        cancel_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vendorOrderRef.child("status").setValue("Canceled");
                shopkeeperVendorOrderRef.child("status").setValue("Canceled");
                accessoriesOrdersList.child("status").setValue("Canceled");
                accessoriesOrdersList.child("remainingBalance").setValue("NA");
                accessoriesOrdersList.child("discount").setValue("NA");
                accessoriesOrdersList.child("paid").setValue("NA");

                Intent intent = new Intent();
                setResult(111, intent);
                finish();

            }
        });
    }

    private void confirm_order_btn_listener() {
        confirm_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    vendorOrderRef.child("totalBalance").setValue(Float.parseFloat(balance_price_TextView.getText().toString()));
                    vendorOrderRef.child("remainingBalance").setValue(Float.parseFloat(remainingBalance_price_TextView.getText().toString()));
                    vendorOrderRef.child("status").setValue("Complete");

                    shopkeeperVendorOrderRef.child("totalBalance").setValue(Float.parseFloat(balance_price_TextView.getText().toString()));
                    shopkeeperVendorOrderRef.child("remainingBalance").setValue(Float.parseFloat(remainingBalance_price_TextView.getText().toString()));
                    shopkeeperVendorOrderRef.child("status").setValue("Complete");

                    accessoriesOrdersList.child("totalBalance").setValue(Float.parseFloat(balance_price_TextView.getText().toString()));
                    accessoriesOrdersList.child("status").setValue("Complete");
                    accessoriesOrdersList.child("remainingBalance").setValue(Float.parseFloat(remainingBalance_price_TextView.getText().toString()));
                    accessoriesOrdersList.child("discount").setValue(Float.parseFloat(discount_editText.getText().toString()));
                    accessoriesOrdersList.child("paid").setValue(Float.parseFloat(paid_editText.getText().toString()));

                    vendorStockRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (int i = 0; i < accessory_name.size(); i++) {
                                String value = snapshot.child(accessory_category.get(i)).child(accessory_keyId.get(i)).child("Quantity").getValue().toString();

                                oldQuantityList.add(Integer.valueOf(value));

                            }

                            for (int i = 0; i < accessory_name.size(); i++) {
                                int value = oldQuantityList.get(i) - Integer.parseInt(order_qty.get(i));
                                if (value < 0) {
                                    value = 0;
                                }
                                vendorStockRef.child(accessory_category.get(i)).child(accessory_keyId.get(i)).child("Quantity").setValue(value);

                            }

                            shopkeeperStockRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            for (int i = 0; i < accessory_keyId.size(); i++) {
                                                if (snapshot1.child(accessory_keyId.get(i)).exists()) {
                                                    int qty = (Integer.parseInt(snapshot1.child(accessory_keyId.get(i)).child("Quantity").getValue().toString())) + Integer.parseInt(order_qty.get(i));
                                                    shopkeeperNewAccessory_quantity.add(qty);
                                                    shopkeeperOldAccessory_keyId.add(accessory_keyId.get(i));

                                                }

                                            }
                                        }
                                    }

                                    for (int i = 0; i < accessory_keyId.size(); i++) {
                                        shopkeeperNewAccessory_keyId.add(accessory_keyId.get(i));
                                        shopkeeperNewAccessory_category.add(accessory_category.get(i));
                                        shopkeeperNewAccessory_name.add(accessory_name.get(i));
                                        shopkeeperNewAccessory_price.add(rate_price.get(i));
                                        shopkeeperNewAccessory_quantity.add(Integer.valueOf(order_qty.get(i)));

                                    }

                                    for (int i = 0; i < accessory_keyId.size(); i++) {
                                        for (int j = 0; j < shopkeeperOldAccessory_keyId.size(); j++){
                                            if (accessory_keyId.get(i).equals(shopkeeperOldAccessory_keyId.get(j))){
                                                shopkeeperNewAccessory_quantity.set(i,shopkeeperNewAccessory_quantity.get(j));
                                            }
                                        }
                                    }

                                    for (int i = 0; i < shopkeeperNewAccessory_keyId.size(); i++) {
                                        shopkeeperStockRef.child(shopkeeperNewAccessory_category.get(i)).child(shopkeeperNewAccessory_keyId.get(i)).child("Quantity").setValue(shopkeeperNewAccessory_quantity.get(i));
                                        shopkeeperStockRef.child(shopkeeperNewAccessory_category.get(i)).child(shopkeeperNewAccessory_keyId.get(i)).child("Category").setValue(shopkeeperNewAccessory_category.get(i));
                                        shopkeeperStockRef.child(shopkeeperNewAccessory_category.get(i)).child(shopkeeperNewAccessory_keyId.get(i)).child("Name").setValue(shopkeeperNewAccessory_name.get(i));
                                        shopkeeperStockRef.child(shopkeeperNewAccessory_category.get(i)).child(shopkeeperNewAccessory_keyId.get(i)).child("Price").setValue(shopkeeperNewAccessory_price.get(i));
                                    }

                                    Intent intent = new Intent(Vendor_shopkeepers_orders_progress.this,Invoice_preview_Vendor_order.class);

                                    Date date = Calendar.getInstance().getTime();
                                    String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);
                                    intent.putExtra("Date",currentDateString);
                                    intent.putExtra("Customer_Name",shop_name_textView.getText().toString());
                                    intent.putExtra("Customer_Email",email_textView.getText().toString());
                                    intent.putExtra("Customer_Ph_No",phone_no_textview.getText().toString());
                                    intent.putExtra("Item_Price_Currency",Currency.getInstance().getCurrency());
                                    intent.putExtra("Paid_Amount",paid_editText.getText().toString());
                                    intent.putExtra("Invoice_No",invoiceNo_TextView.getText().toString());
                                    intent.putExtra("Invoice_Type",status_textView.getText().toString());
                                    startActivityForResult(intent,123);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });
    }

    private boolean validate() {
        boolean valid = true;

        if (confirm_order_btn.getText().toString().equals("Apply")){

            if (paid_editText.getText().toString().isEmpty()) {
                paid_editText.setError("Please enter price");
                valid = false;

            }else {
                if (Float.parseFloat(paid_editText.getText().toString())==0){
                    paid_editText.setError("Please enter valid price");
                }else {
                    paid_editText.setText(paid_editText.getText().toString().replaceFirst("^0+(?!$)",""));
                    if (paid_editText.getText().toString().startsWith(".")){
                        paid_editText.setText("0"+paid_editText.getText().toString());
                    }
                }
            }

            if (discount_editText.getText().toString().isEmpty()) {
                discount_editText.setError("Please enter discount");
                valid = false;

            }else {
                if (Float.parseFloat(discount_editText.getText().toString())>=100){
                    discount_editText.setText("100");
                    Float discount = Float.valueOf(discount_editText.getText().toString());
                    Float totalBalance = Float.valueOf(total_price_TextView.getText().toString());
                    Float appliedDiscount = totalBalance-((discount/100)*totalBalance);
                    balance_price_TextView.setText(String.valueOf(appliedDiscount));
                }else{
                    discount_editText.setText(discount_editText.getText().toString().replaceFirst("^0+(?!$)",""));
                    if (discount_editText.getText().toString().startsWith(".")){
                        discount_editText.setText("0"+discount_editText.getText().toString());
                    }
                    Float discount = Float.valueOf(discount_editText.getText().toString());
                    Float totalBalance = Float.valueOf(total_price_TextView.getText().toString());
                    Float appliedDiscount = totalBalance-((discount/100)*totalBalance);
                    balance_price_TextView.setText(String.valueOf(appliedDiscount));
                }
            }
        }


        if (valid){
            if (confirm_order_btn.getText().equals("Confirm Order")){
                valid = true;
            }else if(confirm_order_btn.getText().equals("Apply")){
                Float paidAmount = Float.valueOf(paid_editText.getText().toString());
                Float appliedDiscount = Float.valueOf(balance_price_TextView.getText().toString());
                Float remainingBalance = appliedDiscount-paidAmount;

                remainingBalance_price_TextView.setText(String.valueOf(remainingBalance));
                if (remainingBalance<0||paidAmount<=0){
                    paid_editText.setError("Enter Valid Amount!");
                }else {
                    confirm_order_btn.setText("Confirm Order");
                    confirm_order_btn.setBackground(getResources().getDrawable(R.drawable.profile_screen_header));
                }
                valid = false;

            }
        }

        System.out.println(valid);
        return valid;
    }

    private void readyOrder_textView_listener() {
        readyOrder_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vendorOrderRef.child("status").setValue("Ready");
                shopkeeperVendorOrderRef.child("status").setValue("Ready");
                accessoriesOrdersList.child("status").setValue("Ready");

                Intent intent = new Intent();
                setResult(111, intent);
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
                if (check.equals("vendor")) {
                    shop_name_textView.setText(snapshot.child("shopkeeper_name").getValue().toString());
                    phone_no_textview.setText(snapshot.child("shopkeeper_phoneno").getValue().toString());
                    email_textView.setText(snapshot.child("shopkeeper_email").getValue().toString());
                    address_textView.setText(snapshot.child("shopkeeper_address").getValue().toString());
                } else if (check.equals("shopkeeper")) {
                    shop_name_textView.setText(snapshot.child("vendor_name").getValue().toString());
                    phone_no_textview.setText(snapshot.child("vendor_phoneno").getValue().toString());
                    email_textView.setText(snapshot.child("vendor_email").getValue().toString());
                    address_textView.setText(snapshot.child("vendor_address").getValue().toString());
                }

                total_price_TextView.setText(snapshot.child("totalBalance").getValue().toString());
                balance_price_TextView.setText(snapshot.child("totalBalance").getValue().toString());
                invoiceNo_TextView.setText(snapshot.child("invoice_no").getValue().toString());
                if (snapshot.child("remainingBalance").exists()){
                    remainingBalance_price_TextView.setText(snapshot.child("remainingBalance").getValue().toString());
                }
                if (snapshot.child("discount").exists()){
                    discount_editText.setText(snapshot.child("discount").getValue().toString());
                }
                if (snapshot.child("paid").exists()){
                    paid_editText.setText(snapshot.child("paid").getValue().toString());
                }

                for (DataSnapshot snapshot1 : snapshot.child("items_list").getChildren()) {
                    accessory_name.add(snapshot1.child("item_name").getValue().toString());
                    rate_price.add(snapshot1.child("item_unitPrice").getValue().toString());
                    order_qty.add(snapshot1.child("item_quantity").getValue().toString());
                    accessory_category.add(snapshot1.child("item_category").getValue().toString());
                    image_url.add(snapshot1.child("item_imageUrl").getValue().toString());
                    accessory_keyId.add(snapshot1.child("item_keyId").getValue().toString());

                }

                AccessoryList.getInstance().setAccessoryNameList(accessory_name);
                AccessoryList.getInstance().setAccessoryQuantityList(order_qty);
                Adapter_Vendor_shopkeepers_order_progress_RecyclerView adapter_vendor_shopkeepers_order_progress_recyclerView = new Adapter_Vendor_shopkeepers_order_progress_RecyclerView(Vendor_shopkeepers_orders_progress.this, accessory_name, rate_price, order_qty, image_url, userID, invoiceID, status, check);
                order_invoice_recyclerview.setLayoutManager(new GridLayoutManager(Vendor_shopkeepers_orders_progress.this, 1));
                order_invoice_recyclerview.setAdapter(adapter_vendor_shopkeepers_order_progress_recyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /////////////////////////////////////////////////////////////////

    private void textWatchers() {
        paid_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println("paid textwatcher");
                confirm_order_btn.setText("Apply");
                confirm_order_btn.setBackground(getResources().getDrawable(R.drawable.buylocal_green_button));
            }
        });

        discount_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println("discount textwatcher");
                confirm_order_btn.setText("Apply");
                confirm_order_btn.setBackground(getResources().getDrawable(R.drawable.buylocal_green_button));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==123){
            Intent intent = new Intent();
            setResult(111, intent);
            finish();
        }
    }
}
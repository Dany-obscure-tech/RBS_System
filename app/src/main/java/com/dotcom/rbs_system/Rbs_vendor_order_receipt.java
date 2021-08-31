package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.Adapter_RBS_Vendor_placeorder_RecyclerView;
import com.dotcom.rbs_system.Classes.RBSVendorSelectedStock;
import com.dotcom.rbs_system.Classes.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Rbs_vendor_order_receipt extends AppCompatActivity {
    Date date;

    RBSVendorSelectedStock rbsVendorSelectedStock;
    Adapter_RBS_Vendor_placeorder_RecyclerView adapter_rbs_vendor_placeorder_recyclerView;
    RecyclerView shopkeeper_invoice_details_recyclerview;

    TextView date_textView;
    TextView totalBalance_textView, balance_currency_textView;
    TextView confirm_order_btn;

    ImageButton back_btn;
    List<String> placeorder_item_name_list, placeorder_item_category_list, place_order_price_list, place_order_quantity_list, placeorder_item_pic_list, placeorder_item_keyId_list;

    Boolean validate;
    DatabaseReference vendorOrderRef, shopkeeperVendorOrderRef, accessoriesOrdersList;

    String selectedVendorID,profileImage;

    TextView vendor_name_textView, vendor_phno_textView, vendor_email_textView, vendor_address_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rbs_vendor_order_receipt);
        Initialization();
        Onclicklistners();

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Initialization() {

        date_textView = findViewById(R.id.date_textView);
        date = Calendar.getInstance().getTime();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);
        date_textView.setText(currentDateString);
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = formatter.parse(date.getDay() + "-" + date.getMonth() + "-" + date.getYear());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        vendor_name_textView = findViewById(R.id.vendor_name_textView);
        vendor_phno_textView = findViewById(R.id.vendor_phno_textView);
        vendor_email_textView = findViewById(R.id.vendor_email_textView);
        vendor_address_textView = findViewById(R.id.vendor_address_textView);

        vendor_name_textView.setText(getIntent().getStringExtra("VENDOR_NAME"));
        vendor_email_textView.setText(getIntent().getStringExtra("VENDOR_EMAIL"));
        vendor_address_textView.setText(getIntent().getStringExtra("VENDOR_ADDRESS"));
        vendor_phno_textView.setText(getIntent().getStringExtra("VENDOR_PHNO"));
        profileImage = getIntent().getStringExtra("VENDOR_IMAGEURL");
        selectedVendorID = getIntent().getStringExtra("VENDOR_keyID");

        vendorOrderRef = FirebaseDatabase.getInstance().getReference("Vendor_order/" + selectedVendorID);
        shopkeeperVendorOrderRef = FirebaseDatabase.getInstance().getReference("Shopkeeper_vendor_order/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        accessoriesOrdersList = FirebaseDatabase.getInstance().getReference("Accessories_orders");


        totalBalance_textView = findViewById(R.id.totalBalance_textView);
        balance_currency_textView = findViewById(R.id.balance_currency_textView);

        confirm_order_btn = findViewById(R.id.confirm_order_btn);

        rbsVendorSelectedStock = RBSVendorSelectedStock.getInstance();

        placeorder_item_name_list = rbsVendorSelectedStock.getVendor_stockName_textView();
        placeorder_item_category_list = rbsVendorSelectedStock.getVendor_stock_category_textView();
        place_order_price_list = rbsVendorSelectedStock.getVendor_stock_price_textview();
        place_order_quantity_list = rbsVendorSelectedStock.getVendor_stock_quantity_textView();
        placeorder_item_pic_list = rbsVendorSelectedStock.getVendor_stock_imageView();
        placeorder_item_keyId_list = rbsVendorSelectedStock.getVendor_stock_keyID_list();

        back_btn = findViewById(R.id.back_btn);

        adapter_rbs_vendor_placeorder_recyclerView = new Adapter_RBS_Vendor_placeorder_RecyclerView(Rbs_vendor_order_receipt.this, placeorder_item_name_list, placeorder_item_category_list, place_order_price_list, place_order_quantity_list, placeorder_item_pic_list,placeorder_item_keyId_list, totalBalance_textView);

        shopkeeper_invoice_details_recyclerview = findViewById(R.id.order_invoice_recyclerview);
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
                for (int i = 0; i < adapter_rbs_vendor_placeorder_recyclerView.getValidateList().size(); i++) {
                    if (!adapter_rbs_vendor_placeorder_recyclerView.getValidateList().get(i)) {
                        validate = false;
                    }
                }
                if (!validate) {
                    Toast.makeText(Rbs_vendor_order_receipt.this, "Please provide valid quantities", Toast.LENGTH_SHORT).show();
                } else {
                    String key = accessoriesOrdersList.push().getKey();

                    accessoriesOrdersList.child(key).child("vendor_name").setValue(vendor_name_textView.getText().toString());
                    accessoriesOrdersList.child(key).child("vendor_imageUrl").setValue(profileImage);
                    accessoriesOrdersList.child(key).child("vendor_phoneno").setValue(vendor_phno_textView.getText().toString());
                    accessoriesOrdersList.child(key).child("vendor_email").setValue(vendor_email_textView.getText().toString());
                    accessoriesOrdersList.child(key).child("vendor_address").setValue(vendor_address_textView.getText().toString());
                    accessoriesOrdersList.child(key).child("vendor_keyID").setValue(selectedVendorID);

                    accessoriesOrdersList.child(key).child("shopkeeper_name").setValue(UserDetails.getInstance().getShopName());
                    accessoriesOrdersList.child(key).child("shopkeeper_imageUrl").setValue(UserDetails.getInstance().getShopLogo());
                    accessoriesOrdersList.child(key).child("shopkeeper_phoneno").setValue(UserDetails.getInstance().getShopPhno());
                    accessoriesOrdersList.child(key).child("shopkeeper_email").setValue(UserDetails.getInstance().getShopEmail());
                    accessoriesOrdersList.child(key).child("shopkeeper_address").setValue(UserDetails.getInstance().getAddress());
                    accessoriesOrdersList.child(key).child("shopkeeper_keyID").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    accessoriesOrdersList.child(key).child("invoice_no").setValue(key);
                    accessoriesOrdersList.child(key).child("status").setValue("Pending");
                    accessoriesOrdersList.child(key).child("date").setValue(date_textView.getText().toString());

                    accessoriesOrdersList.child(key).child("totalBalance").setValue(totalBalance_textView.getText().toString());


                    for (int i = 0; i < placeorder_item_name_list.size(); i++) {
                        accessoriesOrdersList.child(key).child("items_list").child("item_" + (i + 1)).child("item_name").setValue(placeorder_item_name_list.get(i));
                        accessoriesOrdersList.child(key).child("items_list").child("item_" + (i + 1)).child("item_category").setValue(placeorder_item_category_list.get(i));
                        accessoriesOrdersList.child(key).child("items_list").child("item_" + (i + 1)).child("item_unitPrice").setValue(place_order_price_list.get(i));
                        accessoriesOrdersList.child(key).child("items_list").child("item_" + (i + 1)).child("item_quantity").setValue(place_order_quantity_list.get(i));
                        accessoriesOrdersList.child(key).child("items_list").child("item_" + (i + 1)).child("item_imageUrl").setValue(placeorder_item_pic_list.get(i));
                        accessoriesOrdersList.child(key).child("items_list").child("item_" + (i + 1)).child("item_keyId").setValue(placeorder_item_keyId_list.get(i));

                    }

                    ////////////////////////////////////////////////////////////////////////////////

                    shopkeeperVendorOrderRef.child(key).child("vendor_name").setValue(vendor_name_textView.getText().toString());
                    shopkeeperVendorOrderRef.child(key).child("vendor_imageUrl").setValue(profileImage);
                    shopkeeperVendorOrderRef.child(key).child("invoice_no").setValue(key);
                    shopkeeperVendorOrderRef.child(key).child("totalBalance").setValue(totalBalance_textView.getText().toString());
                    shopkeeperVendorOrderRef.child(key).child("date").setValue(date_textView.getText().toString());
                    shopkeeperVendorOrderRef.child(key).child("status").setValue("Pending");
                    shopkeeperVendorOrderRef.child(key).child("invoice_keyId").setValue(key);
                    shopkeeperVendorOrderRef.child(key).child("vendor_keyID").setValue(selectedVendorID);

                    ////////////////////////////////////////////////////////////////////////////////


                    vendorOrderRef.child(key).child("shopkeeper_name").setValue(UserDetails.getInstance().getShopName());
                    vendorOrderRef.child(key).child("shopkeeper_imageUrl").setValue(UserDetails.getInstance().getShopLogo());
                    vendorOrderRef.child(key).child("invoice_no").setValue(key);
                    vendorOrderRef.child(key).child("totalBalance").setValue(totalBalance_textView.getText().toString());
                    vendorOrderRef.child(key).child("date").setValue(date_textView.getText().toString());
                    vendorOrderRef.child(key).child("status").setValue("Pending");
                    vendorOrderRef.child(key).child("invoice_keyId").setValue(key);
                    vendorOrderRef.child(key).child("shopkeeper_keyID").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    finish();                }
            }
        });
    }

    private void back_btn_listner() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RBSVendorSelectedStock.getInstance().clearData();
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        RBSVendorSelectedStock.getInstance().clearData();
        return super.onKeyDown(keyCode, event);
    }
}
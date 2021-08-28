package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.Adapter_RBS_Vendor_inventory_RecyclerView;
import com.dotcom.rbs_system.Classes.RBSVendorSelectedStock;
import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class RBS_Vendors extends AppCompatActivity {
    RBSVendorSelectedStock rbsVendorSelectedStock;

    DatabaseReference vendorNameListRef, vendorStockRef;

    RecyclerView rbs_vendor_products_recyclerview;
    Adapter_RBS_Vendor_inventory_RecyclerView adapter_rbs_vendor_inventory_recyclerView;

    LinearLayout searchForVendors, vendor_details_linearLayout;

    ImageView store_banner_imageView, profileImage_imageView;

    ImageButton back_btn;

    String selectedVendorID, profileImage;

    TextView vendor_name_textView, confirm_order_btn;
    TextView vendor_address_textView, vendor_phone_textView, vendor_email_textView;

    List<String> vendors_name_list, vendors_phno_list, vendors_id_list, vendors_address_list, vendors_email_list, vendors_profileImage_list, vendors_banner_list;
    List<String> vendor_stock_category_list, vendor_stockName_list, vendor_stock_price_list, vendor_stock_quantity_list, vendor_stock_imageView_list, vendor_stock_keyID_list;

    private ArrayList<SampleSearchModel> setting_vendors_name_data() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i = 0; i < vendors_name_list.size(); i++) {
            items.add(new SampleSearchModel(vendors_name_list.get(i) + "\n" + vendors_phno_list.get(i), vendors_id_list.get(i), vendors_name_list.get(i), vendors_phno_list.get(i), vendors_address_list.get(i), vendors_email_list.get(i), vendors_profileImage_list.get(i), vendors_banner_list.get(i)));
        }

        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rbs_vendors);
        initialization();
        intialProcesses();
        onclicklistners();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    private void initialization() {
        rbsVendorSelectedStock = RBSVendorSelectedStock.getInstance();
        rbsVendorSelectedStock.listInitialization();

        vendorNameListRef = FirebaseDatabase.getInstance().getReference("Vendor_list");
        vendorStockRef = FirebaseDatabase.getInstance().getReference("Vendor_stock");

        rbs_vendor_products_recyclerview = findViewById(R.id.rbs_vendor_products_recyclerview);
        back_btn = findViewById(R.id.back_btn);
        rbs_vendor_products_recyclerview.setLayoutManager(new GridLayoutManager(RBS_Vendors.this, 1));

        searchForVendors = findViewById(R.id.searchForVendors);
        vendor_details_linearLayout = findViewById(R.id.vendor_details_linearLayout);

        store_banner_imageView = findViewById(R.id.store_banner_imageView);
        profileImage_imageView = findViewById(R.id.profileImage_imageView);

        vendor_name_textView = findViewById(R.id.vendor_name_textView);
        vendor_phone_textView = findViewById(R.id.vendor_phone_textView);
        vendor_address_textView = findViewById(R.id.customer_address_textView);
        vendor_email_textView = findViewById(R.id.vendor_email_textView);

        confirm_order_btn = findViewById(R.id.confirm_order_btn);
        vendor_stock_category_list = new ArrayList<>();
        vendor_stockName_list = new ArrayList<>();
        vendor_stock_price_list = new ArrayList<>();
        vendor_stock_quantity_list = new ArrayList<>();
        vendor_stock_imageView_list = new ArrayList<>();
        vendor_stock_keyID_list = new ArrayList<>();

        vendors_name_list = new ArrayList<>();
        vendors_phno_list = new ArrayList<>();
        vendors_id_list = new ArrayList<>();
        vendors_address_list = new ArrayList<>();
        vendors_email_list = new ArrayList<>();
        vendors_profileImage_list = new ArrayList<>();
        vendors_banner_list = new ArrayList<>();

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private void intialProcesses() {
        vendorNameListFetch();
    }

    private void vendorNameListFetch() {
        vendorNameListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    vendors_name_list.add(snapshot1.child("Name").getValue().toString());
                    vendors_phno_list.add(snapshot1.child("Phone_no").getValue().toString());
                    vendors_address_list.add(snapshot1.child("address").getValue().toString());
                    ///TODO yaha pa app crash ho rahe ha, RBS options ka 5th button wali activity jo accessory ma a gaye ha
                    vendors_email_list.add(snapshot1.child("email").getValue().toString());
                    vendors_profileImage_list.add(snapshot1.child("profile_image_url").getValue().toString());
                    vendors_banner_list.add(snapshot1.child("banner").getValue().toString());
                    vendors_id_list.add(snapshot1.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private void onclicklistners() {
        searchForVendors_listner();
        confirm_order_btn_listner();
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

    private void confirm_order_btn_listner() {
        confirm_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RBS_Vendors.this, Rbs_vendor_order_receipt.class);

                for (int i = 0; i < adapter_rbs_vendor_inventory_recyclerView.getSelectedItemPositions().size(); i++) {

                    rbsVendorSelectedStock.getVendor_stockName_textView().add(vendor_stockName_list.get(adapter_rbs_vendor_inventory_recyclerView.getSelectedItemPositions().get(i)));
                    rbsVendorSelectedStock.getVendor_stock_category_textView().add(vendor_stock_category_list.get(adapter_rbs_vendor_inventory_recyclerView.getSelectedItemPositions().get(i)));
                    rbsVendorSelectedStock.getVendor_stock_price_textview().add(vendor_stock_price_list.get(adapter_rbs_vendor_inventory_recyclerView.getSelectedItemPositions().get(i)));
                    rbsVendorSelectedStock.getVendor_stock_quantity_textView().add(vendor_stock_quantity_list.get(adapter_rbs_vendor_inventory_recyclerView.getSelectedItemPositions().get(i)));
                    rbsVendorSelectedStock.getVendor_stock_imageView().add(vendor_stock_imageView_list.get(adapter_rbs_vendor_inventory_recyclerView.getSelectedItemPositions().get(i)));
                    rbsVendorSelectedStock.getVendor_stock_keyID_list().add(vendor_stock_keyID_list.get(adapter_rbs_vendor_inventory_recyclerView.getSelectedItemPositions().get(i)));
                }
                if (rbsVendorSelectedStock.getVendor_stockName_textView().size() != 0) {
                    intent.putExtra("VENDOR_NAME", vendor_name_textView.getText().toString());
                    intent.putExtra("VENDOR_EMAIL", vendor_email_textView.getText().toString());
                    intent.putExtra("VENDOR_ADDRESS", vendor_address_textView.getText().toString());
                    intent.putExtra("VENDOR_IMAGEURL", profileImage);
                    intent.putExtra("VENDOR_PHNO", vendor_phone_textView.getText().toString());
                    intent.putExtra("VENDOR_keyID", selectedVendorID);
                    startActivity(intent);
                } else {
                    Toast.makeText(RBS_Vendors.this, "Please select items!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void searchForVendors_listner() {
        searchForVendors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(RBS_Vendors.this, "Search results...",
                        "Search for voucher number.", null, setting_vendors_name_data(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                vendor_name_textView.setText(item.getName());
                                vendor_name_textView.setTextColor(getResources().getColor(R.color.textBlue));
                                vendor_phone_textView.setText(item.getVal1());
                                vendor_address_textView.setText(item.getVal2());
                                vendor_email_textView.setText(item.getVal3());
                                Picasso.get().load(item.getVal4()).into(profileImage_imageView);
                                profileImage = item.getVal4();
                                profileImage_imageView.setVisibility(View.VISIBLE);
                                Picasso.get().load(item.getVal5()).into(store_banner_imageView);
                                vendor_details_linearLayout.setVisibility(View.VISIBLE);
                                confirm_order_btn.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                                fetchVendorStock(item.getId());
                            }
                        }).show();

            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private void fetchVendorStock(String id) {
        vendorStockRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                selectedVendorID = id;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                        vendor_stock_category_list.add(snapshot1.getKey());
                        vendor_stockName_list.add(snapshot2.child("Name").getValue().toString());
                        vendor_stock_price_list.add(snapshot2.child("Price").getValue().toString());
                        vendor_stock_quantity_list.add(snapshot2.child("Quantity").getValue().toString());
                        vendor_stock_imageView_list.add(snapshot2.child("Image_url").getValue().toString());
                        vendor_stock_keyID_list.add(snapshot2.getKey());

                    }

                }
                adapter_rbs_vendor_inventory_recyclerView = new Adapter_RBS_Vendor_inventory_RecyclerView(RBS_Vendors.this, vendor_stock_category_list, vendor_stockName_list, vendor_stock_price_list, vendor_stock_quantity_list, vendor_stock_imageView_list);
                rbs_vendor_products_recyclerview.setAdapter(adapter_rbs_vendor_inventory_recyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
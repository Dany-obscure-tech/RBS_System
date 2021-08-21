package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterCustomerHistoryListRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterCustomerIDImagesRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Customer_history extends AppCompatActivity {

    String customerKeyID;
    String selected_country_code;
    EditText editTextCarrierNumber;
    List<String> shopkeeper_name_textview, item_name_textview, item_category_textview, status_textView, shopkeeperImage_imageView_list, dateList, itemKeyId, itemImageView, shopkeeper_key_id, serial_no_textview, customerIDimageUrlList;

    RecyclerView customer_ID_Image_recyclerView;
    ImageView edit_image_image_view;

    RelativeLayout alert_background_relativelayout;

    LinearLayout header_profile;

    TextView edit_textview;

    SimpleDateFormat sfd;

    TextView customerDetailsToggle_textView;
    TextView customerName_textView, customerID_textView, phno_textView, customer_address_textView, customer_email_textView;

    DatabaseReference customerRef;
    DatabaseReference customerHistoryRef;
    Query orderQuery;

    RecyclerView customerHistoryRecyclerView;
    AdapterCustomerHistoryListRecyclerView adapterCustomerHistoryListRecyclerView;

    AdapterCustomerIDImagesRecyclerView adapterCustomerIDImagesRecyclerView;

    Progreess_dialog pd1, pd2;

    Boolean toggleCheck = true;

    ImageButton back_btn;

    CountryCodePicker ccp;

    EditText ac_address, ac_email;

    TextView save_btn_textview, cancel_btn_textview;

    Dialog edit_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_history);

        Initialization();
        InitialDataFetch();
        ClickListeners();
        //TODO validatefields ka function nahi laga howa yaha pa
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Initialization() {

        edit_dialog = new Dialog(this);
        edit_dialog.setContentView(R.layout.edit_dialog_customer);

        ccp = edit_dialog.findViewById(R.id.ccp);
        editTextCarrierNumber = edit_dialog.findViewById(R.id.editText_carrierNumber);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);

        customerName_textView = findViewById(R.id.customerName_textView);
        customerID_textView = findViewById(R.id.customerID_textView);
        phno_textView = findViewById(R.id.phno_textView);
        customer_address_textView = findViewById(R.id.customer_address_textView);
        customer_email_textView = findViewById(R.id.customer_email_textView);
        back_btn = findViewById(R.id.back_btn);
        edit_textview = findViewById(R.id.edit_textview);
        header_profile = findViewById(R.id.header_profile);
        alert_background_relativelayout = findViewById(R.id.alert_background_relativelayout);
        edit_image_image_view = findViewById(R.id.edit_image_image_view);


        ac_address = edit_dialog.findViewById(R.id.ac_address);
        ac_email = edit_dialog.findViewById(R.id.ac_email);
        cancel_btn_textview = edit_dialog.findViewById(R.id.cancel_btn_textview);
        save_btn_textview = edit_dialog.findViewById(R.id.save_btn_textview);

        customerKeyID = getIntent().getStringExtra("CUSTOMER_ID");
        customerIDimageUrlList = new ArrayList<>();

        dateList = new ArrayList<>();
        itemImageView = new ArrayList<>();
        itemKeyId = new ArrayList<>();
        item_name_textview = new ArrayList<>();
        item_category_textview = new ArrayList<>();
        serial_no_textview = new ArrayList<>();
        status_textView = new ArrayList<>();
        shopkeeperImage_imageView_list = new ArrayList<>();
        shopkeeper_key_id = new ArrayList<>();
        shopkeeper_name_textview = new ArrayList<>();

        sfd = new SimpleDateFormat("dd-MM-yyyy");

        customerHistoryRef = FirebaseDatabase.getInstance().getReference("Customer_history/" + customerKeyID);

        orderQuery = customerHistoryRef.orderByChild("Timestamp");

        customerHistoryRecyclerView = findViewById(R.id.customerHistoryRecyclerView);
        customerHistoryRecyclerView.setLayoutManager(new GridLayoutManager(Customer_history.this, 1));


        customerDetailsToggle_textView = findViewById(R.id.customerDetailsToggle_textView);

        customerRef = FirebaseDatabase.getInstance().getReference("Customer_list");

        customer_ID_Image_recyclerView = findViewById(R.id.customer_ID_Image_recyclerView);
        customer_ID_Image_recyclerView.setLayoutManager(new GridLayoutManager(Customer_history.this, 2));


        pd1 = new Progreess_dialog();
        pd2 = new Progreess_dialog();

        Date date = Calendar.getInstance().getTime();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);

        //TODo ye commented code remove karna ha
//        String key = customerHistoryRef.push().getKey();
//        customerHistoryRef.child(key).child("Item_name").setValue("HP Omen");
//        customerHistoryRef.child(key).child("Item_serialno").setValue("HP/112233");
//        customerHistoryRef.child(key).child("Shopkeeper_name").setValue("ITech Computers");
//        customerHistoryRef.child(key).child("RBS").setValue("Buy");
//        customerHistoryRef.child(key).child("Timestamp").setValue(date.getTime());
//        customerHistoryRef.child(key).child("Date").setValue(currentDateString);
//
//        key = customerHistoryRef.push().getKey();
//        customerHistoryRef.child(key).child("Item_name").setValue("HP Omen");
//        customerHistoryRef.child(key).child("Item_serialno").setValue("HP/112233");
//        customerHistoryRef.child(key).child("Shopkeeper_name").setValue("ITech Computers");
//        customerHistoryRef.child(key).child("RBS").setValue("Sale");
//        customerHistoryRef.child(key).child("Timestamp").setValue(date.getTime());
//        customerHistoryRef.child(key).child("Date").setValue(currentDateString);
//
//        key = customerHistoryRef.push().getKey();
//        customerHistoryRef.child(key).child("Item_name").setValue("HP Omen");
//        customerHistoryRef.child(key).child("Item_serialno").setValue("HP/112233");
//        customerHistoryRef.child(key).child("Shopkeeper_name").setValue("ITech Computers");
//        customerHistoryRef.child(key).child("RBS").setValue("Buy");
//        customerHistoryRef.child(key).child("Timestamp").setValue(date.getTime());
//        customerHistoryRef.child(key).child("Date").setValue(currentDateString);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialDataFetch() {
        gettingHistoryList();
        getCustomer(customerKeyID);
    }

    private void gettingHistoryList() {
        pd1.showProgressBar(Customer_history.this);

        pd1.dismissProgressBar(Customer_history.this);
        customerHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        dateList.add(dataSnapshot1.child("Date").getValue().toString());
                        item_category_textview.add(dataSnapshot1.child("Item_keyCategory").getValue().toString());
                        itemImageView.add(dataSnapshot1.child("Item_image").getValue().toString());
                        itemKeyId.add(dataSnapshot1.child("Item_keyId").getValue().toString());
                        item_name_textview.add(dataSnapshot1.child("Item_name").getValue().toString());
                        serial_no_textview.add(dataSnapshot1.child("Item_serialno").getValue().toString());
                        status_textView.add(dataSnapshot1.child("RBS").getValue().toString());
                        shopkeeperImage_imageView_list.add(dataSnapshot1.child("Shopkeeper_image").getValue().toString());
                        shopkeeper_key_id.add(dataSnapshot1.child("Shopkeeper_keyId").getValue().toString());
                        shopkeeper_name_textview.add(dataSnapshot1.child("Shopkeeper_name").getValue().toString());

                    }
                    Collections.reverse(dateList);
                    Collections.reverse(itemImageView);
                    Collections.reverse(itemKeyId);
                    Collections.reverse(item_name_textview);
                    Collections.reverse(item_category_textview);
                    Collections.reverse(serial_no_textview);
                    Collections.reverse(status_textView);
                    Collections.reverse(shopkeeperImage_imageView_list);
                    Collections.reverse(shopkeeper_key_id);
                    Collections.reverse(shopkeeper_name_textview);


                } else {
                    pd1.dismissProgressBar(Customer_history.this);
                }

                adapterCustomerHistoryListRecyclerView = new AdapterCustomerHistoryListRecyclerView(Customer_history.this, dateList, item_category_textview, itemImageView, itemKeyId, item_name_textview, serial_no_textview, status_textView, shopkeeperImage_imageView_list, shopkeeper_key_id, shopkeeper_name_textview);
                customerHistoryRecyclerView.setAdapter(adapterCustomerHistoryListRecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getCustomer(String customerKeyID) {
        pd2.showProgressBar(Customer_history.this);
        customerRef.child(customerKeyID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    customerName_textView.setText(dataSnapshot.child("Name").getValue().toString());
                    customerID_textView.setText(dataSnapshot.child("ID").getValue().toString());
                    phno_textView.setText(dataSnapshot.child("Phone_no").getValue().toString());
                    customer_address_textView.setText(dataSnapshot.child("Address").getValue().toString());
                    customer_email_textView.setText(dataSnapshot.child("Email").getValue().toString());

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("ID_Image_urls").getChildren()) {
                        customerIDimageUrlList.add(dataSnapshot1.getValue().toString());
                    }

                    adapterCustomerIDImagesRecyclerView = new AdapterCustomerIDImagesRecyclerView(Customer_history.this, customerIDimageUrlList, alert_background_relativelayout, edit_image_image_view);
                    customer_ID_Image_recyclerView.setAdapter(adapterCustomerIDImagesRecyclerView);
                    pd2.dismissProgressBar(Customer_history.this);
                } else {
                    pd2.dismissProgressBar(Customer_history.this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void ClickListeners() {
        customerDetailsToggle();
        editbtn();
        cancelbtn();
        savebtn();
        backbtn();
    }

    private void editbtn() {
        edit_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextCarrierNumber.setText(phno_textView.getText().toString());
                ac_address.setText(customer_address_textView.getText().toString());
                ac_email.setText(customer_email_textView.getText().toString());
                edit_dialog.show();
            }
        });
    }

    private void savebtn() {
        save_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validatefields()) {
                    edit_data();
                }
            }
        });
    }

    private boolean validatefields() {
        boolean valid = true;
        //TODO yaha par validations sahi sa nhi lage howi email ki aur address ki
        if (!ccp.isValidFullNumber()) {
            editTextCarrierNumber.setError("Please enter valid number");
            valid = false;
        }
        return valid;
    }

    private void edit_data() {


        if (ccp.isValidFullNumber()) {
            customerRef.child(customerKeyID).child("Phone_no").setValue(ccp.getFullNumberWithPlus());
        }
        if (!ac_address.getText().toString().equals("")) {
            customerRef.child(customerKeyID).child("Address").setValue(ac_address.getText().toString());
        }
        if (!ac_email.getText().toString().equals("")) {
            customerRef.child(customerKeyID).child("Email").setValue(ac_email.getText().toString());
        }
        Toast.makeText(this, "Data save Successfully", Toast.LENGTH_SHORT).show();
        edit_dialog.dismiss();
        recreate();

    }

    private void cancelbtn() {
        cancel_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_dialog.dismiss();
            }
        });
    }

    private void customerDetailsToggle() {
        customerDetailsToggle_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toggleCheck) {
                    header_profile.setVisibility(View.VISIBLE);
                    customerDetailsToggle_textView.setText("Hide Details");
                    toggleCheck = true;
                } else {
                    header_profile.setVisibility(View.GONE);
                    customerDetailsToggle_textView.setText("Show Item Details");
                    toggleCheck = false;
                }
            }
        });
    }

    private void backbtn() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onCountryPickerClick(View view) {
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
                selected_country_code = ccp.getFullNumberWithPlus();
            }
        });
    }
}

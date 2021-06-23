package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Customer_history extends AppCompatActivity {

    String customerKeyID;
    List<String> shopkeeper_name_textview, item_name_textview,status_textView,shopkeeperImage_imageView_list,customerImage_imageView_list ,dateList,shopkeeper_key_id,customer_key_id,serial_no_textview,imageUrlList;

    RecyclerView customer_ID_Image_recyclerView;
    ImageView edit_image_image_view;

    RelativeLayout alert_background_relativelayout;

    LinearLayout header_profile;

    TextView edit_textview;

    SimpleDateFormat sfd;

    TextView customerDetailsToggle_textView;;
    TextView customerName_textView, customerID_textView, phno_textView, address_textView, email_textView;

    DatabaseReference customerRef;
    DatabaseReference customerHistoryRef;
    Query orderQuery;

    RecyclerView customerHistoryRecyclerView;
    AdapterCustomerHistoryListRecyclerView adapterCustomerHistoryListRecyclerView;

    AdapterCustomerIDImagesRecyclerView adapterCustomerIDImagesRecyclerView;

    LinearLayout customerDetails;

    Progreess_dialog pd1,pd2;

    Boolean toggleCheck = true;

    ImageButton Back_btn;

    EditText ac_phoneno,ac_address,ac_email;

    TextView save_btn_textview,cancel_btn_textview;

    Dialog edit_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_history);

        Initialization();
        InitialDataFetch();
        ClickListeners();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Initialization() {
        customerName_textView = (TextView)findViewById(R.id.customerName_textView);
        customerID_textView = (TextView)findViewById(R.id.customerID_textView);
        phno_textView = (TextView)findViewById(R.id.phno_textView);
        address_textView = (TextView)findViewById(R.id.vendor_address_textView);
        email_textView = (TextView)findViewById(R.id.post_code_textView);
        Back_btn=(ImageButton)findViewById(R.id.Back_btn);
        edit_textview=(TextView) findViewById(R.id.edit_textview);
        header_profile=(LinearLayout) findViewById(R.id.header_profile);
        alert_background_relativelayout=(RelativeLayout) findViewById(R.id.alert_background_relativelayout);
        edit_image_image_view=(ImageView) findViewById(R.id.edit_image_image_view);

        edit_dialog = new Dialog(this);
        edit_dialog.setContentView(R.layout.edit_dialog_customer);

        ac_phoneno = (EditText) edit_dialog.findViewById(R.id.ac_phoneno);
        ac_address = (EditText) edit_dialog.findViewById(R.id.ac_address);
        ac_email = (EditText) edit_dialog.findViewById(R.id.ac_email);
        cancel_btn_textview = (TextView) edit_dialog.findViewById(R.id.cancel_btn_textview);
        save_btn_textview = (TextView) edit_dialog.findViewById(R.id.save_btn_textview);

        customerKeyID = getIntent().getStringExtra("CUSTOMER_ID");

        shopkeeper_name_textview = new ArrayList<>();
        item_name_textview = new ArrayList<>();
        status_textView = new ArrayList<>();
        shopkeeperImage_imageView_list = new ArrayList<>();
        customerImage_imageView_list = new ArrayList<>();
        serial_no_textview = new ArrayList<>();
        dateList = new ArrayList<>();
        shopkeeper_key_id = new ArrayList<>();
        customer_key_id = new ArrayList<>();
        imageUrlList = new ArrayList<>();

        shopkeeperImage_imageView_list.add("https://firebasestorage.googleapis.com/v0/b/rbssystem.appspot.com/o/Item_Images%2F-MbWKj3Ju2hQXGjGdMxj%2Fimage_1?alt=media&token=dc41aeb4-9fbe-42bb-9c8a-5aedf66cc509");
        shopkeeperImage_imageView_list.add("https://firebasestorage.googleapis.com/v0/b/rbssystem.appspot.com/o/Item_Images%2F-MbWKj3Ju2hQXGjGdMxj%2Fimage_1?alt=media&token=dc41aeb4-9fbe-42bb-9c8a-5aedf66cc509");
        shopkeeperImage_imageView_list.add("https://firebasestorage.googleapis.com/v0/b/rbssystem.appspot.com/o/Item_Images%2F-MbWKj3Ju2hQXGjGdMxj%2Fimage_1?alt=media&token=dc41aeb4-9fbe-42bb-9c8a-5aedf66cc509");
        shopkeeperImage_imageView_list.add("https://firebasestorage.googleapis.com/v0/b/rbssystem.appspot.com/o/Item_Images%2F-MbWKj3Ju2hQXGjGdMxj%2Fimage_1?alt=media&token=dc41aeb4-9fbe-42bb-9c8a-5aedf66cc509");
        customerImage_imageView_list.add("https://firebasestorage.googleapis.com/v0/b/rbssystem.appspot.com/o/Item_Images%2F-MbWKj3Ju2hQXGjGdMxj%2Fimage_1?alt=media&token=dc41aeb4-9fbe-42bb-9c8a-5aedf66cc509");
        customerImage_imageView_list.add("https://firebasestorage.googleapis.com/v0/b/rbssystem.appspot.com/o/Item_Images%2F-MbWKj3Ju2hQXGjGdMxj%2Fimage_1?alt=media&token=dc41aeb4-9fbe-42bb-9c8a-5aedf66cc509");
        customerImage_imageView_list.add("https://firebasestorage.googleapis.com/v0/b/rbssystem.appspot.com/o/Item_Images%2F-MbWKj3Ju2hQXGjGdMxj%2Fimage_1?alt=media&token=dc41aeb4-9fbe-42bb-9c8a-5aedf66cc509");
        customerImage_imageView_list.add("https://firebasestorage.googleapis.com/v0/b/rbssystem.appspot.com/o/Item_Images%2F-MbWKj3Ju2hQXGjGdMxj%2Fimage_1?alt=media&token=dc41aeb4-9fbe-42bb-9c8a-5aedf66cc509");


        sfd = new SimpleDateFormat("dd-MM-yyyy");

        customerHistoryRef = FirebaseDatabase.getInstance().getReference("Customer_history/"+ customerKeyID);

        orderQuery = customerHistoryRef.orderByChild("Timestamp");

        customerHistoryRecyclerView = (RecyclerView)findViewById(R.id.customerHistoryRecyclerView);
        customerHistoryRecyclerView.setLayoutManager(new GridLayoutManager(Customer_history.this,1));


        customerDetailsToggle_textView = (TextView)findViewById(R.id.customerDetailsToggle_textView);

        customerDetails = (LinearLayout)findViewById(R.id.customerDetails);

        customerRef = FirebaseDatabase.getInstance().getReference("Customer_list");

        customer_ID_Image_recyclerView = (RecyclerView)findViewById(R.id.customer_ID_Image_recyclerView);
        customer_ID_Image_recyclerView.setLayoutManager(new GridLayoutManager(Customer_history.this,2));



        pd1 = new Progreess_dialog();
        pd2 = new Progreess_dialog();

        Date date = Calendar.getInstance().getTime();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);

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


                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        shopkeeper_name_textview.add(dataSnapshot1.child("Shopkeeper_name").getValue().toString());
                        item_name_textview.add(dataSnapshot1.child("Item_name").getValue().toString());
                        status_textView.add(dataSnapshot1.child("RBS").getValue().toString());
                        serial_no_textview.add(dataSnapshot1.child("Item_serialno").getValue().toString());
                        dateList.add(dataSnapshot1.child("Date").getValue().toString());

                    }
                    Collections.reverse(shopkeeper_name_textview);
                    Collections.reverse(item_name_textview);
                    Collections.reverse(status_textView);
                    Collections.reverse(serial_no_textview);
                    Collections.reverse(dateList);


                }else {
                    pd1.dismissProgressBar(Customer_history.this);
                }

                adapterCustomerHistoryListRecyclerView = new AdapterCustomerHistoryListRecyclerView(Customer_history.this,shopkeeper_name_textview,item_name_textview,serial_no_textview,status_textView,shopkeeperImage_imageView_list,customerImage_imageView_list,dateList,shopkeeper_key_id,customer_key_id);
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

                if (dataSnapshot.exists()){
                    customerName_textView.setText(dataSnapshot.child("Name").getValue().toString());
                    customerID_textView.setText(dataSnapshot.child("ID").getValue().toString());
                    phno_textView.setText(dataSnapshot.child("Phone_no").getValue().toString());
                    address_textView.setText(dataSnapshot.child("Address").getValue().toString());
                    email_textView.setText(dataSnapshot.child("Email").getValue().toString());

                    for(DataSnapshot dataSnapshot1: dataSnapshot.child("ID_Image_urls").getChildren()){
                        imageUrlList.add(dataSnapshot1.getValue().toString());
                    }

                    adapterCustomerIDImagesRecyclerView = new AdapterCustomerIDImagesRecyclerView(Customer_history.this,imageUrlList,alert_background_relativelayout,edit_image_image_view);
                    customer_ID_Image_recyclerView.setAdapter(adapterCustomerIDImagesRecyclerView);
                    pd2.dismissProgressBar(Customer_history.this);
                }else {
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
                edit_dialog.show();
            }
        });
    }

    private void savebtn() {
        save_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_data();
            }
        });
    }

    private void edit_data() {
        if (!ac_phoneno.getText().toString().equals("")){
            customerRef.child(customerKeyID).child("Phone_no").setValue(ac_phoneno.getText().toString());
        }
        if (!ac_address.getText().toString().equals("")){
            customerRef.child(customerKeyID).child("Address").setValue(ac_address.getText().toString());
        }
        if (!ac_email.getText().toString().equals("")){
            customerRef.child(customerKeyID).child("Email").setValue(ac_email.getText().toString());
        }
        Toast.makeText(this, "Data save Successfully", Toast.LENGTH_SHORT).show();
        edit_dialog.dismiss();
        recreate();

    }

    private void cancelbtn(){
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
                if (!toggleCheck){
                    header_profile.setVisibility(View.VISIBLE);
                    customerDetailsToggle_textView.setText("Hide Details");
                    toggleCheck = true;
                }else {
                    header_profile.setVisibility(View.GONE);
                    customerDetailsToggle_textView.setText("Show Item Details");
                    toggleCheck = false;
                }
            }
        });
    }

    private void backbtn() {
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}

package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.AdapterCustomerHistoryListRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer_history extends AppCompatActivity {

    String customerKeyID;
    List<String> itemNameList, rbsList, dateList;

    SimpleDateFormat sfd;

    TextView customerDetailsToggle_textView;;
    TextView customerName_textView, customerID_textView, phno_textView, address_textView, email_textView;

    DatabaseReference customerRef;
    DatabaseReference customerHistoryRef;
    Query orderQuery;

    RecyclerView customerHistoryRecyclerView;
    AdapterCustomerHistoryListRecyclerView adapterCustomerHistoryListRecyclerView;

    LinearLayout customerDetails;

    Progreess_dialog pd1,pd2;

    Boolean toggleCheck = true;

    ImageButton Back_btn;

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
        address_textView = (TextView)findViewById(R.id.address_textView);
        email_textView = (TextView)findViewById(R.id.email_textView);
        Back_btn=(ImageButton)findViewById(R.id.Back_btn);

        customerKeyID = getIntent().getStringExtra("CUSTOMER_ID");

        itemNameList = new ArrayList<>();
        rbsList = new ArrayList<>();
        dateList = new ArrayList<>();

        sfd = new SimpleDateFormat("dd-MM-yyyy");

        customerHistoryRef = FirebaseDatabase.getInstance().getReference("Customer_history/"+ customerKeyID);

        orderQuery = customerHistoryRef.orderByChild("Timestamp");

        customerHistoryRecyclerView = (RecyclerView)findViewById(R.id.customerHistoryRecyclerView);
        customerHistoryRecyclerView.setLayoutManager(new GridLayoutManager(Customer_history.this,1));

        customerDetailsToggle_textView = (TextView)findViewById(R.id.customerDetailsToggle_textView);

        customerDetails = (LinearLayout)findViewById(R.id.customerDetails);

        customerRef = FirebaseDatabase.getInstance().getReference("Customer_list");

        pd1 = new Progreess_dialog();
        pd2 = new Progreess_dialog();
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialDataFetch() {
        gettingHistoryList();
        getCustomer(customerKeyID);
    }

    private void gettingHistoryList() {
        pd1.showProgressBar(Customer_history.this);
        orderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        itemNameList.add(dataSnapshot1.child("Item_name").getValue().toString());
                        rbsList.add(dataSnapshot1.child("RBS").getValue().toString());
                        dateList.add(dataSnapshot1.child("Date").getValue().toString());

                    }
                    Collections.reverse(itemNameList);
                    Collections.reverse(rbsList);
                    Collections.reverse(dateList);

                    adapterCustomerHistoryListRecyclerView = new AdapterCustomerHistoryListRecyclerView(Customer_history.this,itemNameList,rbsList,dateList);
                    customerHistoryRecyclerView.setAdapter(adapterCustomerHistoryListRecyclerView);
                    pd1.dismissProgressBar(Customer_history.this);
                }else {
                    pd1.dismissProgressBar(Customer_history.this);
                }
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
        backbtn();
    }

    private void customerDetailsToggle() {
        customerDetailsToggle_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toggleCheck){
                    customerDetails.setVisibility(View.VISIBLE);
                    customerDetailsToggle_textView.setText("Hide detailss");
                    customerDetailsToggle_textView.setTextColor(getResources().getColor(R.color.textGrey));
                    customerDetailsToggle_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));

                    toggleCheck = true;
                }else {
                    customerDetails.setVisibility(View.GONE);
                    customerDetailsToggle_textView.setText("Show Item Details");
                    customerDetailsToggle_textView.setTextColor(getResources().getColor(R.color.textBlue));
                    customerDetailsToggle_textView.setBackground(getResources().getDrawable(R.drawable.main_button));
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

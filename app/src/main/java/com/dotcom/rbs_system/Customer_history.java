package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    EditText ac_phoneno,ac_address,ac_email;

    Button edit_btn,cancel_btn,save_btn;

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
        edit_btn=(Button)findViewById(R.id.edit_btn);

        edit_dialog = new Dialog(this);
        edit_dialog.setContentView(R.layout.edit_dialog_customer);

        ac_phoneno = (EditText) edit_dialog.findViewById(R.id.ac_phoneno);
        ac_address = (EditText) edit_dialog.findViewById(R.id.ac_address);
        ac_email = (EditText) edit_dialog.findViewById(R.id.ac_email);
        cancel_btn = (Button) edit_dialog.findViewById(R.id.cancel_btn);
        save_btn = (Button) edit_dialog.findViewById(R.id.save_btn);

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
        editbtn();
        cancelbtn();
        savebtn();
        backbtn();
    }

    private void editbtn() {
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_dialog.show();
            }
        });
    }

    private void savebtn() {
        save_btn.setOnClickListener(new View.OnClickListener() {
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
        cancel_btn.setOnClickListener(new View.OnClickListener() {
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

package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterRepairsFaultListRecyclerView;
import com.dotcom.rbs_system.Classes.Repair_details_edit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Repair_details extends AppCompatActivity {
    LinearLayout itemDetails,customerDetails;

    Repair_details_edit repair_details_edit_obj;

    TextView ticketNo_textView,agreedPrice_TextView,date_TextView,paidAmount_TextView,balanceAmount_TextView,specialConditions_TextView;
    TextView itemName_textView,serialNo_textView,category_textView,condition_textView,notes_textView;
    TextView customerName_textView,id_textView,phno_textView,dob_textView,address_textView,email_textView;

    Button edit_btn;

    DatabaseReference repairRef,customerRef,itemRef;

    String repairID,itemKeyID,customerKeyID;
    String firebaseUserID;
    List<String> faultNameList,faultPriceList,faultKeyIDList;

    long timestamp;

    Progreess_dialog pd1,pd2,pd3;

    RecyclerView faultList_recyclerView;
    AdapterRepairsFaultListRecyclerView adapterRepairsFaultListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_details);

        Initialization();
        InitialDataFetch();
        OnClickListeners();

    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Initialization() {

        itemDetails = (LinearLayout) findViewById(R.id.itemDetails);
        customerDetails = (LinearLayout) findViewById(R.id.customerDetails);

        repair_details_edit_obj = Repair_details_edit.getInstance();

        ticketNo_textView = (TextView)findViewById(R.id.ticketNo_textView);
        agreedPrice_TextView = (TextView)findViewById(R.id.agreedPrice_TextView);
        date_TextView = (TextView)findViewById(R.id.date_TextView);
        paidAmount_TextView = (TextView)findViewById(R.id.paidAmount_TextView);
        balanceAmount_TextView = (TextView)findViewById(R.id.balanceAmount_TextView);
        specialConditions_TextView = (TextView)findViewById(R.id.specialConditions_TextView);

        itemName_textView = (TextView)findViewById(R.id.itemName_textView);
        serialNo_textView = (TextView)findViewById(R.id.serialNo_textView);
        category_textView = (TextView)findViewById(R.id.category_textView);
        condition_textView = (TextView)findViewById(R.id.condition_textView);
        notes_textView = (TextView)findViewById(R.id.notes_textView);

        customerName_textView = (TextView)findViewById(R.id.customerName_textView);
        id_textView = (TextView)findViewById(R.id.id_textView);
        phno_textView = (TextView)findViewById(R.id.phno_textView);
        dob_textView = (TextView)findViewById(R.id.dob_textView);
        address_textView = (TextView)findViewById(R.id.address_textView);
        email_textView = (TextView)findViewById(R.id.email_textView);

        edit_btn = (Button) findViewById(R.id.edit_btn);

        faultList_recyclerView = (RecyclerView) findViewById(R.id.faultList_recyclerView);
        faultList_recyclerView.setLayoutManager(new GridLayoutManager(Repair_details.this,1));

        pd1 = new Progreess_dialog();
        pd2 = new Progreess_dialog();
        pd3 = new Progreess_dialog();

        repairID = getIntent().getStringExtra("REPAIR_ID");
        firebaseUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        faultNameList = new ArrayList<>();
        faultPriceList = new ArrayList<>();
        faultKeyIDList = new ArrayList<>();

        repairRef = FirebaseDatabase.getInstance().getReference("Repairs_list/"+firebaseUserID+"/"+repairID);
        customerRef = FirebaseDatabase.getInstance().getReference("Customer_list");
        itemRef = FirebaseDatabase.getInstance().getReference("Items");

    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void InitialDataFetch() {
        getDetails();
    }

    private void getDetails() {
        pd1.showProgressBar(Repair_details.this);
        repairRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ticketNo_textView.setText(dataSnapshot.child("Ticket_no").getValue().toString());
                agreedPrice_TextView.setText(dataSnapshot.child("Agreed_price").getValue().toString());
                date_TextView.setText(dataSnapshot.child("Date").getValue().toString());
                paidAmount_TextView.setText(dataSnapshot.child("Paid_amount").getValue().toString());
                balanceAmount_TextView.setText(dataSnapshot.child("Balance_amount").getValue().toString());
                specialConditions_TextView.setText(dataSnapshot.child("Special_condition").getValue().toString());
                timestamp = Long.parseLong(dataSnapshot.child("Timestamp").getValue().toString());
                for (DataSnapshot dataSnapshot1:dataSnapshot.child("Faults").getChildren()){
                    faultNameList.add(dataSnapshot1.child("Fault_name").getValue().toString());
                    faultPriceList.add(dataSnapshot1.child("Fault_price").getValue().toString());
                    faultKeyIDList.add(dataSnapshot1.child("Fault_key").getValue().toString());
                }
                adapterRepairsFaultListRecyclerView = new AdapterRepairsFaultListRecyclerView(Repair_details.this,faultNameList,faultPriceList,null);
                faultList_recyclerView.setAdapter(adapterRepairsFaultListRecyclerView);

                getCustomer(dataSnapshot.child("Customer_keyID").getValue().toString());
                getItem(dataSnapshot.child("Item_category").getValue().toString(),dataSnapshot.child("Item_keyID").getValue().toString());

                pd1.dismissProgressBar(Repair_details.this);

            }

            private void getCustomer(String customer_keyID) {
                pd2.showProgressBar(Repair_details.this);
                customerKeyID = customer_keyID;
                customerRef.child(customer_keyID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        customerName_textView.setText(dataSnapshot.child("Name").getValue().toString());
                        id_textView.setText(dataSnapshot.child("ID").getValue().toString());
                        phno_textView.setText(dataSnapshot.child("Phone_no").getValue().toString());
                        dob_textView.setText(dataSnapshot.child("DOB").getValue().toString());
                        address_textView.setText(dataSnapshot.child("Address").getValue().toString());
                        email_textView.setText(dataSnapshot.child("Email").getValue().toString());

                        pd2.dismissProgressBar(Repair_details.this);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        pd2.dismissProgressBar(Repair_details.this);
                    }

                });
            }

            private void getItem(String item_category, String item_keyID) {
                pd3.showProgressBar(Repair_details.this);
                itemKeyID=item_keyID;
                itemRef.child(item_category).child(item_keyID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        itemName_textView.setText(dataSnapshot.child("Item_name").getValue().toString());
                        serialNo_textView.setText(dataSnapshot.child("Item_id").getValue().toString());
                        category_textView.setText(dataSnapshot.child("Category").getValue().toString());
                        condition_textView.setText(dataSnapshot.child("Condition").getValue().toString());
                        notes_textView.setText(dataSnapshot.child("Notes").getValue().toString());

                        pd3.dismissProgressBar(Repair_details.this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        pd3.dismissProgressBar(Repair_details.this);
                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd1.dismissProgressBar(Repair_details.this);
            }
        });
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void OnClickListeners() {
        editRepairDetails();
        historyActivity();
        backButton();
    }

    private void editRepairDetails() {
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                repair_details_edit_obj.setItemKeyID(itemKeyID);
                repair_details_edit_obj.setItemName_textView(itemName_textView.getText().toString());
                repair_details_edit_obj.setSerialNo_textView(serialNo_textView.getText().toString());
                repair_details_edit_obj.setCategory_textView(category_textView.getText().toString());
                repair_details_edit_obj.setCondition_textView(condition_textView.getText().toString());
                repair_details_edit_obj.setNotes_textView(notes_textView.getText().toString());

                repair_details_edit_obj.setCustomerKeyID(customerKeyID);
                repair_details_edit_obj.setCustomerName_textView(customerName_textView.getText().toString());
                repair_details_edit_obj.setId_textView(id_textView.getText().toString());
                repair_details_edit_obj.setPhno_textView(phno_textView.getText().toString());
                repair_details_edit_obj.setDob_textView(dob_textView.getText().toString());
                repair_details_edit_obj.setAddress_textView(address_textView.getText().toString());
                repair_details_edit_obj.setEmail_textView(email_textView.getText().toString());

                repair_details_edit_obj.setTicketNo_TextView(ticketNo_textView.getText().toString());
                repair_details_edit_obj.setAgreedPrice_TextView(agreedPrice_TextView.getText().toString());
                repair_details_edit_obj.setDate_TextView(date_TextView.getText().toString());
                repair_details_edit_obj.setPaidAmount_TextView(paidAmount_TextView.getText().toString());
                repair_details_edit_obj.setBalanceAmount_TextView(balanceAmount_TextView.getText().toString());
                repair_details_edit_obj.setSpecialConditions_TextView(specialConditions_TextView.getText().toString());

                repair_details_edit_obj.setFaultNameList(faultNameList);
                repair_details_edit_obj.setFaultPriceList(faultPriceList);
                repair_details_edit_obj.setFaultKeyIDList(faultKeyIDList);

                repair_details_edit_obj.setTimestamp(timestamp);

                Intent intent = new Intent(Repair_details.this,Repairs.class);
                intent.putExtra("EDIT_CHECK",true);
                startActivity(intent);
            }
        });
    }

    private void historyActivity() {
        itemDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Repair_details.this,Item_history.class);
                intent.putExtra("ITEM_ID",itemKeyID);
                intent.putExtra("ITEM_CATEGORY",category_textView.getText().toString());
                startActivity(intent);
            }
        });

        customerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Repair_details.this,Customer_history.class);
                intent.putExtra("CUSTOMER_ID",customerKeyID);
                startActivity(intent);
            }
        });
    }

    private void backButton() {
        ImageButton Back_btn;
        Back_btn=(ImageButton)findViewById(R.id.Back_btn);
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}

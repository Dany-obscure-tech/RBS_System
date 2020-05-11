package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterRepairsFaultListRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Repair_details extends AppCompatActivity {
    TextView ticketNo_textView,agreedPrice_TextView,date_TextView,paidAmount_TextView,balanceAmount_TextView,specialConditions_TextView;
    TextView itemName_textView,serialNo_textView,category_textView,condition_textView,notes_textView;
    TextView customerName_textView,id_textView,phno_textView,dob_textView,address_textView,email_textView;

    DatabaseReference repairRef,customerRef,itemRef;

    String repairID;
    String firebaseUserID;
    List<String> faultNameList,faultPriceList;

    Progreess_dialog pd;

    RecyclerView faultList_recyclerView;
    AdapterRepairsFaultListRecyclerView adapterRepairsFaultListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_details);

        Initialization();
        InitialDataFetch();

    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialization() {
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

        faultList_recyclerView = (RecyclerView) findViewById(R.id.faultList_recyclerView);
        faultList_recyclerView.setLayoutManager(new GridLayoutManager(Repair_details.this,1));

        pd = new Progreess_dialog();

        repairID = getIntent().getStringExtra("REPAIR_ID");
        firebaseUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        faultNameList = new ArrayList<>();
        faultPriceList = new ArrayList<>();

        repairRef = FirebaseDatabase.getInstance().getReference("Repairs_list/"+firebaseUserID+"/"+repairID);
        customerRef = FirebaseDatabase.getInstance().getReference("Customer_list");
        itemRef = FirebaseDatabase.getInstance().getReference("Items");

    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialDataFetch() {
        getDetails();
    }

    private void getDetails() {
        pd.showProgressBar(Repair_details.this);
        repairRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ticketNo_textView.setText(dataSnapshot.child("Ticket_no").getValue().toString());
                agreedPrice_TextView.setText(dataSnapshot.child("Agreed_price").getValue().toString());
                date_TextView.setText(dataSnapshot.child("Date").getValue().toString());
                paidAmount_TextView.setText(dataSnapshot.child("Paid_amount").getValue().toString());
                balanceAmount_TextView.setText(dataSnapshot.child("Balance_amount").getValue().toString());
                specialConditions_TextView.setText(dataSnapshot.child("Special_conditiomn").getValue().toString());

                for (DataSnapshot dataSnapshot1:dataSnapshot.child("Faults").getChildren()){
                    faultNameList.add(dataSnapshot1.child("Fault_name").getValue().toString());
                    faultPriceList.add(dataSnapshot1.child("Fault_price").getValue().toString());
                }
                adapterRepairsFaultListRecyclerView = new AdapterRepairsFaultListRecyclerView(Repair_details.this,faultNameList,faultPriceList,null);
                faultList_recyclerView.setAdapter(adapterRepairsFaultListRecyclerView);

                getCustomer(dataSnapshot.child("Customer_keyID").getValue().toString());
                getItem(dataSnapshot.child("Item_category").getValue().toString(),dataSnapshot.child("Item_keyID").getValue().toString());

                pd.dismissProgressBar(Repair_details.this);
            }

            private void getCustomer(String customer_keyID) {
                customerRef.child(customer_keyID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        customerName_textView.setText(dataSnapshot.child("Name").getValue().toString());
                        id_textView.setText(dataSnapshot.child("ID").getValue().toString());
                        phno_textView.setText(dataSnapshot.child("Phone_no").getValue().toString());
                        dob_textView.setText(dataSnapshot.child("DOB").getValue().toString());
                        address_textView.setText(dataSnapshot.child("Address").getValue().toString());
                        email_textView.setText(dataSnapshot.child("Email").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
            }

            private void getItem(String item_category, String item_keyID) {
                itemRef.child(item_category).child(item_keyID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        itemName_textView.setText(dataSnapshot.child("Item_name").getValue().toString());
                        serialNo_textView.setText(dataSnapshot.child("Item_id").getValue().toString());
                        category_textView.setText(dataSnapshot.child("Category").getValue().toString());
                        condition_textView.setText(dataSnapshot.child("Condition").getValue().toString());
                        notes_textView.setText(dataSnapshot.child("Notes").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismissProgressBar(Repair_details.this);
            }
        });
    }


}

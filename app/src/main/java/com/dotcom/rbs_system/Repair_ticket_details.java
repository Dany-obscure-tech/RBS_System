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
import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.Classes.Repair_details_edit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Repair_ticket_details extends AppCompatActivity {
    String currency = Currency.getInstance().getCurrency();
    LinearLayout pendingAgreedPrice_linearLayout;

    Repair_details_edit repair_details_edit_obj;

    TextView ticketNo_textView, amount_TextView, date_TextView, specialConditions_TextView;
    TextView itemName_textView, serialNo_textView;
    TextView customerName_textView, id_textView, phno_textView, email_textView;
    TextView pendingFaults_textView, pendingAmount_TextView;
    TextView edit_textView;
    TextView confirmTicket_textView, cancleTicket_textView;

    DatabaseReference repairRef, repairTicketRef;

    String repairID, repairTicketStatus, itemKeyID, customerKeyID;
    String firebaseUserID;
    String agreedAmount_str, pendingAmount_str;

    List<String> faultNameList, faultPriceList, faultKeyIDList;
    List<String> pendingFaultNameList, pendingFaultPriceList, pendingFaultKeyIDList;
    List<String> dateList;

    ImageButton gmail_btn, sms_btn, print_btn;
    Button btn_done;

    long timestamp;

    Progreess_dialog pd1, pd2, pd3, pd4;


    RecyclerView faultList_recyclerView, pendingFaultList_recyclerView;
    AdapterRepairsFaultListRecyclerView adapterRepairsFaultListRecyclerView, adapterRepairsPendingFaultListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_ticket_details);

        Initialization();
        InitialDataFetch();
        OnClickListeners();

        //TODO edit ticket ma jo already faults list a rahe, usko bhi hm delete kar sakey, but ye is ma nhi ha

    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialization() {

        pendingAgreedPrice_linearLayout = findViewById(R.id.pendingAgreedPrice_linearLayout);

        repair_details_edit_obj = Repair_details_edit.getInstance();

        ticketNo_textView = findViewById(R.id.ticketNo_textView);
        amount_TextView = findViewById(R.id.amount_TextView);
        date_TextView = findViewById(R.id.date_TextView);
        specialConditions_TextView = findViewById(R.id.specialConditions_TextView);

        itemName_textView = findViewById(R.id.itemName_textView);
        serialNo_textView = findViewById(R.id.serialNo_textView);

        customerName_textView = findViewById(R.id.customerName_textView);
        id_textView = findViewById(R.id.id_textView);
        phno_textView = findViewById(R.id.phno_textView);
        email_textView = findViewById(R.id.customer_email_textView);
        pendingFaults_textView = findViewById(R.id.pendingFaults_textView);
        pendingAmount_TextView = findViewById(R.id.pendingAmount_TextView);

        edit_textView = findViewById(R.id.edit_textView);

        confirmTicket_textView = findViewById(R.id.confirmTicket_textView);
        cancleTicket_textView = findViewById(R.id.cancleTicket_textView);

        faultList_recyclerView = findViewById(R.id.faultList_recyclerView);
        faultList_recyclerView.setLayoutManager(new GridLayoutManager(Repair_ticket_details.this, 1));
        pendingFaultList_recyclerView = findViewById(R.id.pendingFaultList_recyclerView);
        pendingFaultList_recyclerView.setLayoutManager(new GridLayoutManager(Repair_ticket_details.this, 1));

        pd1 = new Progreess_dialog();
        pd2 = new Progreess_dialog();
        pd3 = new Progreess_dialog();
        pd4 = new Progreess_dialog();

        repairID = getIntent().getStringExtra("REPAIR_ID");
        repairTicketStatus = getIntent().getStringExtra("STATUS");
        if (repairTicketStatus.equals("Confirmed") || repairTicketStatus.equals("Canceled")) {
            edit_textView.setVisibility(View.GONE);
            confirmTicket_textView.setVisibility(View.GONE);
            cancleTicket_textView.setVisibility(View.GONE);

        }

        firebaseUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        faultNameList = new ArrayList<>();
        faultPriceList = new ArrayList<>();
        faultKeyIDList = new ArrayList<>();
        pendingFaultNameList = new ArrayList<>();
        pendingFaultPriceList = new ArrayList<>();
        pendingFaultKeyIDList = new ArrayList<>();
        dateList = new ArrayList<>();

        repairRef = FirebaseDatabase.getInstance().getReference("Repairs_list/" + firebaseUserID + "/" + repairID);
        repairTicketRef = FirebaseDatabase.getInstance().getReference("Repairs_ticket_list/" + firebaseUserID + "/" + repairID);

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void InitialDataFetch() {
        getDetails();
    }

    private void getDetails() {
        pd1.showProgressBar(Repair_ticket_details.this);
        repairRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerName_textView.setText(dataSnapshot.child("Customer_name").getValue().toString());
                id_textView.setText(dataSnapshot.child("Customer_id").getValue().toString());
                phno_textView.setText(dataSnapshot.child("Customer_phno").getValue().toString());
                email_textView.setText(dataSnapshot.child("Customer_email").getValue().toString());

                itemName_textView.setText(dataSnapshot.child("Item_name").getValue().toString());
                serialNo_textView.setText(dataSnapshot.child("Item_serial_no").getValue().toString());

                ticketNo_textView.setText(dataSnapshot.child("Ticket_no").getValue().toString());
                amount_TextView.setText(currency + dataSnapshot.child("Amount").getValue().toString());
                agreedAmount_str = dataSnapshot.child("Amount").getValue().toString();
                date_TextView.setText(dataSnapshot.child("Date").getValue().toString());
                specialConditions_TextView.setText(dataSnapshot.child("Special_condition").getValue().toString());
                timestamp = Long.parseLong(dataSnapshot.child("Timestamp").getValue().toString());

                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Faults").getChildren()) {
                    faultNameList.add(dataSnapshot1.child("Fault_name").getValue().toString());
                    faultPriceList.add(dataSnapshot1.child("Fault_price").getValue().toString());
                    faultKeyIDList.add(dataSnapshot1.child("Fault_key").getValue().toString());
                }
                adapterRepairsFaultListRecyclerView = new AdapterRepairsFaultListRecyclerView(Repair_ticket_details.this, faultNameList, faultPriceList, null, null, null, null, null, null, false);
                faultList_recyclerView.setAdapter(adapterRepairsFaultListRecyclerView);

                if (dataSnapshot.child("Pending_Faults").exists()) {
                    pendingFaults_textView.setVisibility(View.VISIBLE);
                    pendingFaultList_recyclerView.setVisibility(View.VISIBLE);

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Pending_Faults").getChildren()) {
                        pendingFaultNameList.add(dataSnapshot1.child("Fault_name").getValue().toString());
                        pendingFaultPriceList.add(dataSnapshot1.child("Fault_price").getValue().toString());
                        pendingFaultKeyIDList.add(dataSnapshot1.child("Fault_key").getValue().toString());
                    }
                    adapterRepairsPendingFaultListRecyclerView = new AdapterRepairsFaultListRecyclerView(Repair_ticket_details.this, pendingFaultNameList, pendingFaultPriceList, null, null, null, null, null, null, false);
                    pendingFaultList_recyclerView.setAdapter(adapterRepairsPendingFaultListRecyclerView);
                }

                if (dataSnapshot.child("Pending_Amount").exists()) {
                    repair_details_edit_obj.setPendingAmount_TextView(dataSnapshot.child("Pending_Amount").getValue().toString());
                    pendingAmount_TextView.setText(currency + dataSnapshot.child("Pending_Amount").getValue().toString());
                    pendingAmount_str = dataSnapshot.child("Pending_Amount").getValue().toString();
                    pendingAgreedPrice_linearLayout.setVisibility(View.VISIBLE);
                } else {
                    repair_details_edit_obj.setPendingAmount_TextView(agreedAmount_str);
                }


                pd1.dismissProgressBar(Repair_ticket_details.this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd1.dismissProgressBar(Repair_ticket_details.this);
            }
        });
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void OnClickListeners() {
        editRepairDetails();
        confirmTicket();
        cancleTicket();
        backButton();
    }


    private void editRepairDetails() {
        edit_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                repair_details_edit_obj.setItemKeyID(itemKeyID);
                repair_details_edit_obj.setItemName_textView(itemName_textView.getText().toString());
                repair_details_edit_obj.setSerialNo_textView(serialNo_textView.getText().toString());

                repair_details_edit_obj.setCustomerKeyID(customerKeyID);
                repair_details_edit_obj.setCustomerName_textView(customerName_textView.getText().toString());
                repair_details_edit_obj.setId_textView(id_textView.getText().toString());
                repair_details_edit_obj.setPhno_textView(phno_textView.getText().toString());

                repair_details_edit_obj.setEmail_textView(email_textView.getText().toString());

                repair_details_edit_obj.setTicketNo_TextView(ticketNo_textView.getText().toString());
                repair_details_edit_obj.setAmount_TextView(agreedAmount_str);
                repair_details_edit_obj.setDate_TextView(date_TextView.getText().toString());
                repair_details_edit_obj.setSpecialConditions_TextView(specialConditions_TextView.getText().toString());

                repair_details_edit_obj.setFaultNameList(faultNameList);
                repair_details_edit_obj.setFaultPriceList(faultPriceList);
                repair_details_edit_obj.setFaultKeyIDList(faultKeyIDList);

                repair_details_edit_obj.setPendingFaultNameList(pendingFaultNameList);
                repair_details_edit_obj.setPendingFaultPriceList(pendingFaultPriceList);
                repair_details_edit_obj.setPendingFaultKeyIDList(pendingFaultKeyIDList);

                repair_details_edit_obj.setTimestamp(timestamp);

                Intent intent = new Intent(Repair_ticket_details.this, AddRepairTicket.class);
                intent.putExtra("EDIT_CHECK", true);
                startActivity(intent);
            }
        });
    }

    private void confirmTicket() {
        confirmTicket_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pendingFaultNameList.size() == 0) {

                    repairTicketRef.child("Status").setValue("Confirmed");
                    //TODO yaha pa alert lgana ha print, whatsapp aur email wala

                } else {
                    Toast.makeText(Repair_ticket_details.this, "Changes pending!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cancleTicket() {
        cancleTicket_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repairTicketRef.child("Status").setValue("Canceled");
                finish();
            }
        });
    }

    private void backButton() {
        ImageButton back_btn;
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
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

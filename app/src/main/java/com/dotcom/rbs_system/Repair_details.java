package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Repair_details extends AppCompatActivity {
    String currency = Currency.getInstance().getCurrency();
    CardView item_cardView,customer_cardView;
    LinearLayout pendingAgreedPrice_linearLayout,changesConfirmation_linearLayout;

    Repair_details_edit repair_details_edit_obj;

    TextView ticketNo_textView,agreedPrice_TextView,date_TextView,paidAmount_TextView,balanceAmount_TextView,specialConditions_TextView;
    TextView itemName_textView,serialNo_textView;
    TextView customerName_textView,id_textView,phno_textView,email_textView;
    TextView pendingFaults_textView,pendingAgreedPrice_TextView;
    TextView edit_textView;
    TextView confirmChanges_textView,cancleChanges_textView;
    TextView confirmTicket_textView,cancleTicket_textView;

    DatabaseReference repairRef,repairTicketRef;

    String repairID,itemKeyID,customerKeyID;
    String firebaseUserID;
    String paidAmount_str,agreedAmount_str,pendingAmount_str,balanceAmount_str;

    List<String> faultNameList,faultPriceList,faultKeyIDList;
    List<String> pendingFaultNameList,pendingFaultPriceList,pendingFaultKeyIDList;
    List<String> dateList;

    ImageButton gmail_btn,sms_btn,print_btn;
    Button btn_done;

    Query orderQuery;


    long timestamp;

    Progreess_dialog pd1,pd2,pd3,pd4;
    Dialog sendingdialog;


    RecyclerView faultList_recyclerView,pendingFaultList_recyclerView;
    AdapterRepairsFaultListRecyclerView adapterRepairsFaultListRecyclerView,adapterRepairsPendingFaultListRecyclerView;

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

        item_cardView = (CardView) findViewById(R.id.item_cardView);
        customer_cardView = (CardView) findViewById(R.id.customer_cardView);
        pendingAgreedPrice_linearLayout = (LinearLayout) findViewById(R.id.pendingAgreedPrice_linearLayout);
        changesConfirmation_linearLayout = (LinearLayout) findViewById(R.id.changesConfirmation_linearLayout);

        repair_details_edit_obj = Repair_details_edit.getInstance();

        ticketNo_textView = (TextView)findViewById(R.id.ticketNo_textView);
        agreedPrice_TextView = (TextView)findViewById(R.id.agreedPrice_TextView);
        date_TextView = (TextView)findViewById(R.id.date_TextView);
        paidAmount_TextView = (TextView)findViewById(R.id.paidAmount_TextView);
        balanceAmount_TextView = (TextView)findViewById(R.id.balanceAmount_TextView);
        specialConditions_TextView = (TextView)findViewById(R.id.specialConditions_TextView);

        itemName_textView = (TextView)findViewById(R.id.itemName_textView);
        serialNo_textView = (TextView)findViewById(R.id.serialNo_textView);

        customerName_textView = (TextView)findViewById(R.id.customerName_textView);
        id_textView = (TextView)findViewById(R.id.id_textView);
        phno_textView = (TextView)findViewById(R.id.phno_textView);
        email_textView = (TextView)findViewById(R.id.email_textView);
        pendingFaults_textView = (TextView)findViewById(R.id.pendingFaults_textView);
        pendingAgreedPrice_TextView = (TextView)findViewById(R.id.pendingAgreedPrice_TextView);

        edit_textView = (TextView) findViewById(R.id.edit_textView);
        confirmChanges_textView = (TextView) findViewById(R.id.confirmChanges_textView);
        cancleChanges_textView = (TextView) findViewById(R.id.cancleChanges_textView);

        confirmTicket_textView = (TextView) findViewById(R.id.confirmTicket_textView);
        cancleTicket_textView = (TextView) findViewById(R.id.cancleTicket_textView);

        faultList_recyclerView = (RecyclerView) findViewById(R.id.faultList_recyclerView);
        faultList_recyclerView.setLayoutManager(new GridLayoutManager(Repair_details.this,1));
        pendingFaultList_recyclerView = (RecyclerView) findViewById(R.id.pendingFaultList_recyclerView);
        pendingFaultList_recyclerView.setLayoutManager(new GridLayoutManager(Repair_details.this,1));

        pd1 = new Progreess_dialog();
        pd2 = new Progreess_dialog();
        pd3 = new Progreess_dialog();
        pd4 = new Progreess_dialog();

        sendingdialog = new Dialog(this);
        sendingdialog.setContentView(R.layout.dialoge_items);
        sendingdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });

        gmail_btn = (ImageButton) sendingdialog.findViewById(R.id.gmail_btn);
        print_btn = (ImageButton) sendingdialog.findViewById(R.id.print_btn);
        sms_btn = (ImageButton) sendingdialog.findViewById(R.id.sms_btn);
        btn_done = (Button) sendingdialog.findViewById(R.id.btn_done);

        repairID = getIntent().getStringExtra("REPAIR_ID");
        firebaseUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        faultNameList = new ArrayList<>();
        faultPriceList = new ArrayList<>();
        faultKeyIDList = new ArrayList<>();
        pendingFaultNameList = new ArrayList<>();
        pendingFaultPriceList = new ArrayList<>();
        pendingFaultKeyIDList = new ArrayList<>();
        dateList = new ArrayList<>();

        repairRef = FirebaseDatabase.getInstance().getReference("Repairs_list/"+firebaseUserID+"/"+repairID);
        repairTicketRef = FirebaseDatabase.getInstance().getReference("Repairs_ticket_list/"+firebaseUserID+"/"+repairID);

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
                customerName_textView.setText(dataSnapshot.child("Customer_name").getValue().toString());
                id_textView.setText(dataSnapshot.child("Customer_id").getValue().toString());
                phno_textView.setText(dataSnapshot.child("Customer_phno").getValue().toString());
                email_textView.setText(dataSnapshot.child("Customer_email").getValue().toString());

                itemName_textView.setText(dataSnapshot.child("Item_name").getValue().toString());
                serialNo_textView.setText(dataSnapshot.child("Item_serial_no").getValue().toString());

                ticketNo_textView.setText(dataSnapshot.child("Ticket_no").getValue().toString());
                agreedPrice_TextView.setText(currency+dataSnapshot.child("Agreed_price").getValue().toString());
                agreedAmount_str=dataSnapshot.child("Agreed_price").getValue().toString();
                date_TextView.setText(dataSnapshot.child("Date").getValue().toString());
                paidAmount_TextView.setText(currency+dataSnapshot.child("Paid_amount").getValue().toString());
                paidAmount_str=dataSnapshot.child("Paid_amount").getValue().toString();
                balanceAmount_TextView.setText(currency+dataSnapshot.child("Balance_amount").getValue().toString());
                balanceAmount_str=dataSnapshot.child("Balance_amount").getValue().toString();
                specialConditions_TextView.setText(dataSnapshot.child("Special_condition").getValue().toString());
                timestamp = Long.parseLong(dataSnapshot.child("Timestamp").getValue().toString());

                for (DataSnapshot dataSnapshot1:dataSnapshot.child("Faults").getChildren()){
                    faultNameList.add(dataSnapshot1.child("Fault_name").getValue().toString());
                    faultPriceList.add(dataSnapshot1.child("Fault_price").getValue().toString());
                    faultKeyIDList.add(dataSnapshot1.child("Fault_key").getValue().toString());
                }
                adapterRepairsFaultListRecyclerView = new AdapterRepairsFaultListRecyclerView(Repair_details.this,faultNameList,faultPriceList,null,null,null,null,null,null,false);
                faultList_recyclerView.setAdapter(adapterRepairsFaultListRecyclerView);

                if (dataSnapshot.child("Pending_Faults").exists()){
//                    changesConfirmation_linearLayout.setVisibility(View.VISIBLE);
                    pendingFaults_textView.setVisibility(View.VISIBLE);
                    pendingFaultList_recyclerView.setVisibility(View.VISIBLE);

                    for (DataSnapshot dataSnapshot1:dataSnapshot.child("Pending_Faults").getChildren()){
                        pendingFaultNameList.add(dataSnapshot1.child("Fault_name").getValue().toString());
                        pendingFaultPriceList.add(dataSnapshot1.child("Fault_price").getValue().toString());
                        pendingFaultKeyIDList.add(dataSnapshot1.child("Fault_key").getValue().toString());
                    }
                    adapterRepairsPendingFaultListRecyclerView = new AdapterRepairsFaultListRecyclerView(Repair_details.this,pendingFaultNameList,pendingFaultPriceList,null,null,null,null,null,null,false);
                    pendingFaultList_recyclerView.setAdapter(adapterRepairsPendingFaultListRecyclerView);
                }

                if (dataSnapshot.child("Pending_Price").exists()){
                    repair_details_edit_obj.setPendingPrice(dataSnapshot.child("Pending_Price").getValue().toString());
                    pendingAgreedPrice_TextView.setText(currency+dataSnapshot.child("Pending_Price").getValue().toString());
                    pendingAmount_str = dataSnapshot.child("Pending_Price").getValue().toString();
                    pendingAgreedPrice_linearLayout.setVisibility(View.VISIBLE);

                }else {
                    repair_details_edit_obj.setPendingPrice(agreedAmount_str);
                }


                pd1.dismissProgressBar(Repair_details.this);

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
        confirmChanges();
        cancleChanges();

        confirmTicket();
        cancleTicket();

        historyActivity();
        sending_dialog();
        backButton();
    }

    private void sending_dialog() {
        print_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Repair_details.this, "YEs working", Toast.LENGTH_SHORT).show();
            }
        });

        gmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_SEND);
                it.setType("message/rfc822");
                startActivity(Intent.createChooser(it,"Choose Mail App"));
            }
        });
        sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "0323"));
                intent.putExtra("sms_body", "Hi how are you");
                startActivity(intent);
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendingdialog.dismiss();
                finish();
            }
        });
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
                repair_details_edit_obj.setAgreedPrice_TextView(agreedAmount_str);
                repair_details_edit_obj.setDate_TextView(date_TextView.getText().toString());
                repair_details_edit_obj.setPaidAmount_TextView(paidAmount_str);
                repair_details_edit_obj.setBalanceAmount_TextView(balanceAmount_str);
                repair_details_edit_obj.setSpecialConditions_TextView(specialConditions_TextView.getText().toString());

                repair_details_edit_obj.setFaultNameList(faultNameList);
                repair_details_edit_obj.setFaultPriceList(faultPriceList);
                repair_details_edit_obj.setFaultKeyIDList(faultKeyIDList);

                repair_details_edit_obj.setPendingFaultNameList(pendingFaultNameList);
                repair_details_edit_obj.setPendingFaultPriceList(pendingFaultPriceList);
                repair_details_edit_obj.setPendingFaultKeyIDList(pendingFaultKeyIDList);

                repair_details_edit_obj.setTimestamp(timestamp);

                Intent intent = new Intent(Repair_details.this, AddRepairTicket.class);
                intent.putExtra("EDIT_CHECK",true);
                startActivity(intent);
            }
        });
    }

    private void confirmChanges() {
        confirmChanges_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pendingFaultNameList.size()!=0){
                    repairRef.child("Pending_Faults").removeValue();
                    for (int i = 0;i<pendingFaultNameList.size();i++){
                        repairRef.child("Faults").child("Fault_"+String.valueOf(faultNameList.size()+1)).child("Fault_name").setValue(pendingFaultNameList.get(i));
                        repairRef.child("Faults").child("Fault_"+String.valueOf(faultNameList.size()+1)).child("Fault_price").setValue(pendingFaultPriceList.get(i));
                        repairRef.child("Faults").child("Fault_"+String.valueOf(faultNameList.size()+1)).child("Fault_key").setValue(pendingFaultKeyIDList.get(i));
                    }

                    repairRef.child("Pending_Price").removeValue();
                    repairRef.child("Agreed_price").setValue(pendingAmount_str);
                    repairRef.child("Balance_amount").setValue(String.valueOf(Float.parseFloat(pendingAmount_str)-Float.parseFloat(paidAmount_str)));

                    repairTicketRef.child("Status").setValue("clear");

                    Intent intent = new Intent(Repair_details.this,Repair_details.class);
                    intent.putExtra("REPAIR_ID",repairID);
                    finish();
                    startActivity(intent);
                }else {
                    Toast.makeText(Repair_details.this, "No changes", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void cancleChanges() {
        cancleChanges_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                repairRef.child("Pending_Faults").removeValue();
                repairRef.child("Pending_Price").removeValue();
                repairTicketRef.child("Status").setValue("clear");

                Intent intent = new Intent(Repair_details.this,Repair_details.class);
                intent.putExtra("REPAIR_ID",repairID);
                finish();
                startActivity(intent);
            }
        });
    }

    private void confirmTicket() {
        confirmTicket_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pendingFaultNameList.size()==0){

                    repairTicketRef.removeValue();
                    repairRef.removeValue();
                    sendingdialog.show();

                }else {
                    Toast.makeText(Repair_details.this, "Changes pending!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cancleTicket() {
        cancleTicket_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repairTicketRef.removeValue();
                repairRef.removeValue();
                finish();
            }
        });
    }

    private void historyActivity() {
        item_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Repair_details.this,Item_history.class);
                startActivity(intent);
            }
        });

        customer_cardView.setOnClickListener(new View.OnClickListener() {
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

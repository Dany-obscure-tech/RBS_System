package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.AdapterRepairTicketListRecyclerView;
import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Repair_Ticket extends AppCompatActivity {
    DatabaseReference repairTicketRef;

    RecyclerView repairTicketList_recyclerView;
    AdapterRepairTicketListRecyclerView adapterRepairTicketListRecyclerView;

    TextView repairTicketAdd_textView;
    CardView searchForTicket_cardview;

    ImageButton back_btn;

    List<String> customerNameList, customerIdList, itemNameList, itemSerialNoList, repairKeyIDList, dateList,ticketNoList;
    List<String> customerKeyIDList, itemKeyIDList;
    List<String> pendingStatusList;

    String firebaseUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    Progress_dialog pd;

    private ArrayList<SampleSearchModel> createTicketNoData() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        Collections.reverse(repairKeyIDList);
        Collections.reverse(ticketNoList);
        for (int i = 0; i < repairKeyIDList.size(); i++) {
            if (pendingStatusList.get(i).equals("clear")) {
                items.add(new SampleSearchModel(ticketNoList.get(i), repairKeyIDList.get(i), null, pendingStatusList.get(i), null, null, null, null));
            } else {
                items.add(new SampleSearchModel(ticketNoList.get(i) + "\n" + pendingStatusList.get(i), repairKeyIDList.get(i), null, pendingStatusList.get(i), null, null, null, null));
            }

        }
        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_ticket);

        Initialize();
        startDataFetching();
        dataProcessing();
        ClickEvents();
    }

    private void Initialize() {
        repairTicketRef = FirebaseDatabase.getInstance().getReference("Repairs_ticket_list/" + firebaseUID);

        repairTicketAdd_textView = findViewById(R.id.repairTicketAdd_textView);
        searchForTicket_cardview = findViewById(R.id.searchForTicket_cardview);

        repairTicketList_recyclerView = findViewById(R.id.repairTicketList_recyclerView);
        repairTicketList_recyclerView.setLayoutManager(new GridLayoutManager(Repair_Ticket.this, 1));

        back_btn = findViewById(R.id.back_btn);

        customerNameList = new ArrayList<>();
        customerIdList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        itemSerialNoList = new ArrayList<>();
        repairKeyIDList = new ArrayList<>();
        ticketNoList = new ArrayList<>();
        dateList = new ArrayList<>();
        customerKeyIDList = new ArrayList<>();
        itemKeyIDList = new ArrayList<>();
        pendingStatusList = new ArrayList<>();

        pd = new Progress_dialog();

    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void startDataFetching() {

        gettingRepairTicketList();
    }

    private void gettingRepairTicketList() {
        pd.showProgressBar(Repair_Ticket.this);
        repairTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        customerNameList.add(dataSnapshot1.child("Customer_name").getValue().toString());
                        customerIdList.add(dataSnapshot1.child("Customer_id").getValue().toString());
                        itemNameList.add(dataSnapshot1.child("Item_name").getValue().toString());
                        itemSerialNoList.add(dataSnapshot1.child("Item_serial_no").getValue().toString());
                        repairKeyIDList.add(dataSnapshot1.getKey());
                        ticketNoList.add(dataSnapshot1.child("Ticket_no").getValue().toString());
                        dateList.add(dataSnapshot1.child("Date").getValue().toString());
                        pendingStatusList.add(dataSnapshot1.child("Status").getValue().toString());

                        customerKeyIDList.add(dataSnapshot1.child("Customer_id").getValue().toString());
                        itemKeyIDList.add("to be removed");
                    }

                    adapterRepairTicketListRecyclerView = new AdapterRepairTicketListRecyclerView(Repair_Ticket.this, customerNameList, customerIdList, itemNameList, itemSerialNoList, repairKeyIDList, dateList, pendingStatusList,ticketNoList);
                    repairTicketList_recyclerView.setAdapter(adapterRepairTicketListRecyclerView);
                    pd.dismissProgressBar(Repair_Ticket.this);
                } else {
                    pd.dismissProgressBar(Repair_Ticket.this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismissProgressBar(Repair_Ticket.this);
            }

        });
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void dataProcessing() {
        repairTicketRecyclerView();
    }

    private void repairTicketRecyclerView() {

    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void ClickEvents() {
//        repairTicketSearch();
        addRepairTicket();
        backButton();
        searchForTicket_cardview_listner();
    }

    private void searchForTicket_cardview_listner() {
        searchForTicket_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SimpleSearchDialogCompat(Repair_Ticket.this, "Search results...",
                        "Search for ticket number.", null, createTicketNoData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                dialog.dismiss();
                                Intent intent = new Intent(Repair_Ticket.this, Repair_ticket_details.class);
                                intent.putExtra("REPAIR_ID", item.getId());
                                intent.putExtra("STATUS", item.getVal1());
                                startActivity(intent);
                            }
                        }).show();
            }
        });
    }


    private void addRepairTicket() {
        repairTicketAdd_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Repair_Ticket.this, AddRepairTicket.class);
                intent.putExtra("EDIT_CHECK", false);
                startActivity(intent);
            }
        });
    }

    private void backButton() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}

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

    TextView searchForTicket_textView;
    TextView repairTicketAdd_textView;

    ImageButton Back_btn;

    List<String> customerNameList,itemNameList,ticketNoList;
    List<String> customerKeyIDList,itemKeyIDList,repairKeyIDList;
    List<String> pendingStatusList;

    String firebaseUID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

    Progress_dialoge pd;

    private ArrayList<SampleSearchModel> createTicketNoData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        Collections.reverse(ticketNoList);
        for (int i=0;i<ticketNoList.size();i++){
            if (pendingStatusList.get(i).equals("pending")){
                items.add(new SampleSearchModel(ticketNoList.get(i)+"\n(Pending)",repairKeyIDList.get(i),null,null,null,null,null,null));
            }else {
                items.add(new SampleSearchModel(ticketNoList.get(i),repairKeyIDList.get(i),null,null,null,null,null,null));
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
        repairTicketRef = FirebaseDatabase.getInstance().getReference("Repairs_ticket_list/"+firebaseUID);

        searchForTicket_textView = (TextView)findViewById(R.id.searchForTicket_textView);
        repairTicketAdd_textView =(TextView) findViewById(R.id.repairTicketAdd_textView);

        repairTicketList_recyclerView = (RecyclerView)findViewById(R.id.repairTicketList_recyclerView);
        repairTicketList_recyclerView.setLayoutManager(new GridLayoutManager(Repair_Ticket.this,1));

        Back_btn=(ImageButton)findViewById(R.id.Back_btn);

        customerNameList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        ticketNoList = new ArrayList<>();
        customerKeyIDList = new ArrayList<>();
        itemKeyIDList = new ArrayList<>();
        repairKeyIDList = new ArrayList<>();
        pendingStatusList = new ArrayList<>();

        pd = new Progress_dialoge();

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
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                        customerNameList.add(dataSnapshot1.child("Customer_name").getValue().toString());
                        itemNameList.add(dataSnapshot1.child("Item_name").getValue().toString());
                        ticketNoList.add(dataSnapshot1.child("Ticket_no").getValue().toString());
                        pendingStatusList.add(dataSnapshot1.child("Status").getValue().toString());

                        customerKeyIDList.add(dataSnapshot1.child("Customer_keyID").getValue().toString());
                        itemKeyIDList.add(dataSnapshot1.child("Item_keyID").getValue().toString());
                        repairKeyIDList.add(dataSnapshot1.child("Repair_key_id").getValue().toString());
                    }

                    adapterRepairTicketListRecyclerView = new AdapterRepairTicketListRecyclerView(Repair_Ticket.this,customerNameList,itemNameList,ticketNoList,pendingStatusList);
                    repairTicketList_recyclerView.setAdapter(adapterRepairTicketListRecyclerView);
                    pd.dismissProgressBar(Repair_Ticket.this);
                }else {
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
        repairTicketSearch();
        addRepairTicket();
        backButton();
    }

    private void repairTicketSearch() {
        searchForTicket_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Repair_Ticket.this, "Search results...",
                        "Search for ticket number.", null, createTicketNoData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                dialog.dismiss();
                                Intent intent = new Intent(Repair_Ticket.this,Repair_details.class);
                                intent.putExtra("REPAIR_ID",item.getId());
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
                Intent intent = new Intent(Repair_Ticket.this,Repairs.class);
                intent.putExtra("EDIT_CHECK",false);
                startActivity(intent);
            }
        });
    }

    private void backButton() {
        Back_btn.setOnClickListener(new View.OnClickListener() {
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

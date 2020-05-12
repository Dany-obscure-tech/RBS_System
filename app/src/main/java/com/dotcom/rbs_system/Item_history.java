package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterItemHistoryListRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterRepairTicketListRecyclerView;
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

public class Item_history extends AppCompatActivity {
    String itemID;
    List<String> customerNameList, rbsList, dateList;

    SimpleDateFormat sfd;

    DatabaseReference itemHistoryRef;
    Query orderQuery;

    RecyclerView itemHistoryRecyclerView;
    AdapterItemHistoryListRecyclerView adapterItemHistoryListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_history);

        Initialization();
        InitialDataFetch();


    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialization() {
        itemID = getIntent().getStringExtra("ITEM_ID");
        customerNameList = new ArrayList<>();
        rbsList = new ArrayList<>();
        dateList = new ArrayList<>();

        sfd = new SimpleDateFormat("dd-MM-yyyy");

        itemHistoryRef = FirebaseDatabase.getInstance().getReference("Item_history/"+itemID);

        orderQuery = itemHistoryRef.orderByChild("Timestamp");

        itemHistoryRecyclerView = (RecyclerView)findViewById(R.id.itemHistoryRecyclerView);
        itemHistoryRecyclerView.setLayoutManager(new GridLayoutManager(Item_history.this,1));
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialDataFetch() {
        gettingHistoryList();
    }

    private void gettingHistoryList() {
        orderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        customerNameList.add(dataSnapshot1.child("Customer_name").getValue().toString());
                        rbsList.add(dataSnapshot1.child("RBS").getValue().toString());
                        dateList.add(dataSnapshot1.child("Date").getValue().toString());

                    }
                    Collections.reverse(customerNameList);
                    Collections.reverse(rbsList);
                    Collections.reverse(dateList);

                    adapterItemHistoryListRecyclerView = new AdapterItemHistoryListRecyclerView(Item_history.this,customerNameList,rbsList,dateList);
                    itemHistoryRecyclerView.setAdapter(adapterItemHistoryListRecyclerView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

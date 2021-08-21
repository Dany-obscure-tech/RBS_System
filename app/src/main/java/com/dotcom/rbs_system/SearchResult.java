package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterSpotlightItemListRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchResult extends AppCompatActivity {
    int x = 0;

    DatabaseReference searchingListRef, itemsRef;

    List<String> searchItemsList, filteredList, itemname;
    List<String> filteredItemname, key_idList, categoryList, shopkeeperList, price, itemImage;

    String searchMe, findMe;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Initialize();
        InitialProcess();
        //TODO search activity ko online karna ha

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Initialize() {
        key_idList = new ArrayList<>();
        categoryList = new ArrayList<>();
        shopkeeperList = new ArrayList<>();
        filteredItemname = new ArrayList<>();
        price = new ArrayList<>();
        itemImage = new ArrayList<>();

        itemname = new ArrayList<>();

        itemsRef = FirebaseDatabase.getInstance().getReference("Search/Name_to_items");
        searchingListRef = FirebaseDatabase.getInstance().getReference("Search/Name_list");

        searchItemsList = new ArrayList<>();
        filteredList = new ArrayList<>();

        findMe = getIntent().getStringExtra("SEARCHED");

        recyclerView = findViewById(R.id.spotlightRecyclerView);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialProcess() {
        downloadSearchItemList();
    }

    private void downloadSearchItemList() {
        searchingListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    searchItemsList.add(snapshot1.getValue().toString());

                }
                filterList();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterList() {
        for (int j = 0; j < searchItemsList.size(); j++) {
            searchMe = searchItemsList.get(j);

            int searchMeLength = searchMe.length();
            int findMeLength = findMe.length();
            for (int i = 0;
                 i <= (searchMeLength - findMeLength);
                 i++) {
                if (searchMe.regionMatches(true, i, findMe, 0, findMeLength)) {
                    filteredList.add(searchMe);
                    break;
                }
            }
        }
        getFilteredItems();
    }

    private void getFilteredItems() {
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child(filteredList.get(x)).getChildren()) {
                        Toast.makeText(SearchResult.this, filteredList.get(x), Toast.LENGTH_SHORT).show();
                        filteredItemname.add(String.valueOf(dataSnapshot1.child("item_name").getValue()));
                        price.add(String.valueOf(dataSnapshot1.child("Price").getValue()));
                        itemImage.add(String.valueOf(dataSnapshot1.child("Image").getValue()));
                        key_idList.add(String.valueOf(dataSnapshot1.child("key_id").getValue()));
                        categoryList.add(String.valueOf(dataSnapshot1.child("Category").getValue()));
                        shopkeeperList.add(String.valueOf(dataSnapshot1.child("shopkeeper").getValue()));
                        x++;
                    }

                    AdapterSpotlightItemListRecyclerView viewAdapter = new AdapterSpotlightItemListRecyclerView(SearchResult.this, filteredItemname, price, itemImage, key_idList, categoryList, shopkeeperList);
                    recyclerView.setLayoutManager(new GridLayoutManager(SearchResult.this, 2));
                    recyclerView.setAdapter(viewAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
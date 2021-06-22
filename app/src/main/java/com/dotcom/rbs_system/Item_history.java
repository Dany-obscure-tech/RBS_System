package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterItemHistoryListRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Item_history extends AppCompatActivity {
    String itemID, itemCategory;
    List<String> dateList,status_list, shopkeeper_type_list,shopkeeper_name_list, customer_type_list,customer_name_list;
    List<String> itemImageUrl_list;

    DatabaseReference itemRef;
    DatabaseReference itemHistoryRef;
    Query orderQuery;

    RecyclerView itemHistoryRecyclerView;
    AdapterItemHistoryListRecyclerView adapterItemHistoryListRecyclerView;

    TextView itemDetailsToggle_textView, edit_btn_textview;
    TextView itemName_textView, serialNo_textView, category_textView, condition_textView,item_description_textview;

    RelativeLayout itemDetails_relativelayout;

    Progreess_dialog pd1, pd2;

    Boolean toggleCheck = true;

    ImageButton back_btn;

    Button save_btn, cancel_btn;

    RatingBar ratingBar;

    EditText notes_editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_history);

        Initialization();
        InitialDataFetch();
        ClickListeners();


    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Initialization() {
        itemName_textView = (TextView) findViewById(R.id.item_category_textView);
        item_description_textview = (TextView) findViewById(R.id.item_description_textview);
        serialNo_textView = (TextView) findViewById(R.id.serialNo_textView);
        category_textView = (TextView) findViewById(R.id.category_textView);
        condition_textView = (TextView) findViewById(R.id.condition_textView);
        back_btn = (ImageButton) findViewById(R.id.back_btn);
        edit_btn_textview = (TextView) findViewById(R.id.edit_btn_textview);


        itemID = getIntent().getStringExtra("ITEM_ID");
        itemCategory = getIntent().getStringExtra("ITEM_CATEGORY");

        Toast.makeText(this, itemID+itemCategory, Toast.LENGTH_SHORT).show();


        dateList = new ArrayList<>();
        status_list = new ArrayList<>();
        shopkeeper_type_list = new ArrayList<>();
        shopkeeper_name_list = new ArrayList<>();
        customer_type_list = new ArrayList<>();
        customer_name_list = new ArrayList<>();
        itemImageUrl_list = new ArrayList<>();

        itemHistoryRef = FirebaseDatabase.getInstance().getReference("Item_history/" + itemID);

        Date date = Calendar.getInstance().getTime();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);

//        String key = itemHistoryRef.push().getKey();
//        itemHistoryRef.child(key).child("Shopkeeper_name").setValue("ITech Computers");
//        itemHistoryRef.child(key).child("RBS").setValue("Stocked");
//        itemHistoryRef.child(key).child("Timestamp").setValue(date.getTime());
//        itemHistoryRef.child(key).child("Date").setValue(currentDateString);
//
//        key = itemHistoryRef.push().getKey();
//        itemHistoryRef.child(key).child("Customer_name").setValue("Customer 2");
//        itemHistoryRef.child(key).child("Shopkeeper_name").setValue("ITech Computers");
//        itemHistoryRef.child(key).child("Customer_type").setValue("Buyer");
//        itemHistoryRef.child(key).child("Shopkeeper_type").setValue("Seller");
//        itemHistoryRef.child(key).child("RBS").setValue("Trade");
//        itemHistoryRef.child(key).child("Timestamp").setValue(date.getTime());
//        itemHistoryRef.child(key).child("Date").setValue(currentDateString);
//
//        key = itemHistoryRef.push().getKey();
//        itemHistoryRef.child(key).child("Customer_name").setValue("Customer 2");
//        itemHistoryRef.child(key).child("Shopkeeper_name").setValue("ITech Computers");
//        itemHistoryRef.child(key).child("Customer_type").setValue("Seller");
//        itemHistoryRef.child(key).child("Shopkeeper_type").setValue("Buyer");
//        itemHistoryRef.child(key).child("RBS").setValue("Trade");
//        itemHistoryRef.child(key).child("Timestamp").setValue(date.getTime());
//        itemHistoryRef.child(key).child("Date").setValue(currentDateString);
//
//        key = itemHistoryRef.push().getKey();
//        itemHistoryRef.child(key).child("Shopkeeper_name").setValue("ITech Computers");
//        itemHistoryRef.child(key).child("RBS").setValue("Out of stock");
//        itemHistoryRef.child(key).child("Timestamp").setValue(date.getTime());
//        itemHistoryRef.child(key).child("Date").setValue(currentDateString);


        orderQuery = itemHistoryRef.orderByChild("Timestamp");

        itemHistoryRecyclerView = (RecyclerView) findViewById(R.id.itemHistoryRecyclerView);
        itemHistoryRecyclerView.setLayoutManager(new GridLayoutManager(Item_history.this, 1));

        itemDetailsToggle_textView = (TextView) findViewById(R.id.itemDetailsToggle_textView);

        itemDetails_relativelayout = (RelativeLayout) findViewById(R.id.itemDetails_relativelayout);

        itemRef = FirebaseDatabase.getInstance().getReference("Items");

        pd1 = new Progreess_dialog();
        pd2 = new Progreess_dialog();

    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialDataFetch() {
        gettingHistoryList();
        getItem(itemCategory, itemID);
    }

    private void gettingHistoryList() {
        itemHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd1.showProgressBar(Item_history.this);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        dateList.add(dataSnapshot1.child("Date").getValue().toString());
                        status_list.add(dataSnapshot1.child("RBS").getValue().toString());
                        shopkeeper_name_list.add(dataSnapshot1.child("Shopkeeper_name").getValue().toString());

                        if (dataSnapshot1.child("Customer_name").exists()){
                            shopkeeper_type_list.add(dataSnapshot1.child("Shopkeeper_type").getValue().toString());
                            customer_type_list.add(dataSnapshot1.child("Customer_type").getValue().toString());
                            customer_name_list.add(dataSnapshot1.child("Customer_name").getValue().toString());
                        }else {
                            shopkeeper_type_list.add("NA");
                            customer_type_list.add("NA");
                            customer_name_list.add("NA");
                        }

                        Collections.reverse(dateList);
                        Collections.reverse(status_list);
                        Collections.reverse(shopkeeper_name_list);
                        Collections.reverse(shopkeeper_type_list);
                        Collections.reverse(customer_type_list);
                        Collections.reverse(customer_name_list);

                        adapterItemHistoryListRecyclerView = new AdapterItemHistoryListRecyclerView(Item_history.this,status_list, shopkeeper_type_list,shopkeeper_name_list, customer_type_list,customer_name_list,dateList);
                        itemHistoryRecyclerView.setAdapter(adapterItemHistoryListRecyclerView);

                    }

                    pd1.dismissProgressBar(Item_history.this);
                } else {
                    pd1.dismissProgressBar(Item_history.this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd1.dismissProgressBar(Item_history.this);
            }
        });
    }

    private void getItem(String item_category, String item_keyID) {
        itemRef.child(item_category).child(item_keyID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd2.showProgressBar(Item_history.this);
                if (dataSnapshot.exists()) {
                    itemName_textView.setText(dataSnapshot.child("Item_name").getValue().toString());
                    serialNo_textView.setText(dataSnapshot.child("Item_id").getValue().toString());
                    category_textView.setText(dataSnapshot.child("Category").getValue().toString());
                    condition_textView.setText(dataSnapshot.child("Condition").getValue().toString());
                    item_description_textview.setText(dataSnapshot.child("Description").getValue().toString());

                    for (DataSnapshot dataSnapshot1:dataSnapshot.child("Image_urls").getChildren()){
                        itemImageUrl_list.add(dataSnapshot1.getValue().toString());
                        //todo add this list to slider (Shahzaib)
                    }

                    pd2.dismissProgressBar(Item_history.this);
                } else {
                    pd2.dismissProgressBar(Item_history.this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd2.dismissProgressBar(Item_history.this);
            }
        });
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void ClickListeners() {
        itemDetails_relativelayoutToggle();
        back_btn();
    }



    private void edit_data(String item_category, String item_keyID) {
        if (!ratingBar.equals("0")) {
            itemRef.child(item_category).child(item_keyID).child("Condition").setValue(ratingBar.getRating());
        }
        if (!notes_editText.getText().toString().equals("")) {
            itemRef.child(item_category).child(item_keyID).child("Notes").setValue(notes_editText.getText().toString());
        }
        recreate();

    }

    private void back_btn() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void itemDetails_relativelayoutToggle() {
        itemDetailsToggle_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toggleCheck) {
                    itemDetails_relativelayout.setVisibility(View.VISIBLE);
                    itemDetailsToggle_textView.setText("Hide details");

                    toggleCheck = true;
                } else {
                    itemDetails_relativelayout.setVisibility(View.GONE);
                    itemDetailsToggle_textView.setText("Show Item Details");
                    toggleCheck = false;
                }
            }
        });
    }
}

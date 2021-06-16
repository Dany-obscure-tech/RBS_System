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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.AdapterItemHistoryListRecyclerView;
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
    String itemID, itemCategory;
    List<String> dateList,status_list,shopkeeper_list,shopkeeper_name_list,customer_status_list,customer_name_list;

    SimpleDateFormat sfd;

    DatabaseReference itemRef;
    DatabaseReference itemHistoryRef;
    Query orderQuery;

    RecyclerView itemHistoryRecyclerView;
    AdapterItemHistoryListRecyclerView adapterItemHistoryListRecyclerView;

    TextView itemDetailsToggle_textView, edit_btn_textview;
    TextView itemName_textView, serialNo_textView, category_textView, condition_textView;

    RelativeLayout itemDetails_relativelayout;

    Progreess_dialog pd1, pd2;

    Boolean toggleCheck = true;

    ImageButton back_btn;

    Button save_btn, cancel_btn;

    Dialog edit_dialog;

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
        serialNo_textView = (TextView) findViewById(R.id.serialNo_textView);
        category_textView = (TextView) findViewById(R.id.category_textView);
        condition_textView = (TextView) findViewById(R.id.condition_textView);
        back_btn = (ImageButton) findViewById(R.id.back_btn);
        edit_btn_textview = (TextView) findViewById(R.id.edit_btn_textview);

        edit_dialog = new Dialog(this);
        edit_dialog.setContentView(R.layout.edit_dialog_item);

        ratingBar = (RatingBar) edit_dialog.findViewById(R.id.ratingBar);
        notes_editText = (EditText) edit_dialog.findViewById(R.id.notes_editText);
        save_btn = (Button) edit_dialog.findViewById(R.id.save_btn);
        cancel_btn = (Button) edit_dialog.findViewById(R.id.cancel_btn);


        itemID = getIntent().getStringExtra("ITEM_ID");
        itemCategory = getIntent().getStringExtra("ITEM_CATEGORY");


        dateList = new ArrayList<>();
        status_list = new ArrayList<>();
        shopkeeper_list = new ArrayList<>();
        shopkeeper_name_list = new ArrayList<>();
        customer_status_list = new ArrayList<>();
        customer_name_list = new ArrayList<>();


        dateList.add("6/16/2021");
        status_list.add("Trading");
        shopkeeper_list.add("Seller");
        shopkeeper_name_list.add("Itech Computers");
        customer_status_list.add("Buyer");
        customer_name_list.add("Shahzaib");

        sfd = new SimpleDateFormat("dd-MM-yyyy");

        itemHistoryRef = FirebaseDatabase.getInstance().getReference("Item_history/" + itemID);

        orderQuery = itemHistoryRef.orderByChild("Timestamp");

        itemHistoryRecyclerView = (RecyclerView) findViewById(R.id.itemHistoryRecyclerView);
        itemHistoryRecyclerView.setLayoutManager(new GridLayoutManager(Item_history.this, 1));

        itemDetailsToggle_textView = (TextView) findViewById(R.id.itemDetailsToggle_textView);

        itemDetails_relativelayout = (RelativeLayout) findViewById(R.id.itemDetails_relativelayout);

        itemRef = FirebaseDatabase.getInstance().getReference("Items");

        pd1 = new Progreess_dialog();
        pd2 = new Progreess_dialog();


        adapterItemHistoryListRecyclerView = new AdapterItemHistoryListRecyclerView(Item_history.this,status_list,shopkeeper_list,shopkeeper_name_list,customer_status_list,customer_name_list,dateList);
        itemHistoryRecyclerView.setAdapter(adapterItemHistoryListRecyclerView);
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialDataFetch() {
        gettingHistoryList();
        getItem(itemCategory, itemID);
    }

    private void gettingHistoryList() {
        orderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd1.showProgressBar(Item_history.this);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        dateList.add(dataSnapshot1.child("Date").getValue().toString());

                    }
                    Collections.reverse(dateList);
//
//                    adapterItemHistoryListRecyclerView = new AdapterItemHistoryListRecyclerView(Item_history.this,status_list, dateList);
//                    itemHistoryRecyclerView.setAdapter(adapterItemHistoryListRecyclerView);
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
        editbtn();
        cancelbtn();
        savebtn();
        back_btn();
    }

    private void savebtn() {
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_data(itemCategory, itemID);
            }
        });
    }

    private void edit_data(String item_category, String item_keyID) {
        if (!ratingBar.equals("0")) {
            itemRef.child(item_category).child(item_keyID).child("Condition").setValue(ratingBar.getRating());
        }
        if (!notes_editText.getText().toString().equals("")) {
            itemRef.child(item_category).child(item_keyID).child("Notes").setValue(notes_editText.getText().toString());
        }
        edit_dialog.dismiss();
        recreate();

    }

    private void cancelbtn() {
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_dialog.dismiss();
            }
        });
    }

    private void editbtn() {
        edit_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_dialog.show();

            }
        });
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
                    itemDetailsToggle_textView.setText("Hide detailss");
                    itemDetailsToggle_textView.setTextColor(getResources().getColor(R.color.textGrey));
                    itemDetailsToggle_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));

                    toggleCheck = true;
                } else {
                    itemDetails_relativelayout.setVisibility(View.GONE);
                    itemDetailsToggle_textView.setText("Show Item Details");
                    itemDetailsToggle_textView.setTextColor(getResources().getColor(R.color.textBlue));
                    itemDetailsToggle_textView.setBackground(getResources().getDrawable(R.drawable.main_button));
                    toggleCheck = false;
                }
            }
        });
    }
}

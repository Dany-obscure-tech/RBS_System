package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Buy extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ImageButton Back_btn,sms_btn,gmail_btn;
    Button date_btn,customer_add_btn,searchForCustomer_btn, item_add_btn,searchForItem_btn;
    TextView date_text;
    String firebaseAuthUID;
    List<String> exisitngCustomerList,exisitngCustomerIDList,exisitngItemsList;
    DatabaseReference existingCustomersRef,existingItemsRef;

    Progreess_dialog pd;

//    list creation for customer search view
    private ArrayList<SampleSearchModel> createSampleData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<exisitngCustomerList.size();i++){
//            items.add(new SampleSearchModel(exisitngCustomerList.get(i)+" ("+exisitngCustomerIDList.get(i)+")"));
        }

        return items;
    }

//    list creation for item search view
    private ArrayList<SampleSearchModel> createSampleData2(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<exisitngItemsList.size();i++){
//            items.add(new SampleSearchModel(exisitngItemsList.get(i)));
        }

        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        initialize();

        fetchingExisitingCustomers();
        fetchingExisitingItems();

        onClickListeners();

    }

    private void initialize() {
        exisitngCustomerList = new ArrayList<>();
        exisitngCustomerIDList = new ArrayList<>();
        exisitngItemsList = new ArrayList<>();

        searchForCustomer_btn = (Button)findViewById(R.id.searchForCustomer_btn);
        searchForItem_btn = (Button)findViewById(R.id.searchForItem_btn);

        firebaseAuthUID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
        existingCustomersRef = FirebaseDatabase.getInstance().getReference("Users_databases/"+firebaseAuthUID+"/Customer_list");
        existingItemsRef = FirebaseDatabase.getInstance().getReference("Items");

        Back_btn=(ImageButton)findViewById(R.id.Back_btn);
        sms_btn=(ImageButton)findViewById(R.id.sms_btn);
        customer_add_btn=(Button) findViewById(R.id.customer_add_btn);
        item_add_btn =(Button) findViewById(R.id.item_add_btn);
        date_btn=(Button)findViewById(R.id.date_btn);
        date_text=(TextView)findViewById(R.id.date_text);
        gmail_btn=(ImageButton) findViewById(R.id.gmail_btn);

        pd=new Progreess_dialog();
    }

    private void fetchingExisitingCustomers() {
        existingCustomersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    exisitngCustomerList.add(String.valueOf(String.valueOf(dataSnapshot1.child("Name").getValue())));
                    exisitngCustomerIDList.add(String.valueOf(String.valueOf(dataSnapshot1.child("ID").getValue())));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchingExisitingItems() {
        existingItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                        exisitngItemsList.add(String.valueOf(dataSnapshot2.child("Item_name").getValue()));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date_text.setText(currentDateString);

    }


    private void onClickListeners() {

        sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "0323"));
                intent.putExtra("sms_body", "Hi how are you");
                startActivity(intent);
            }
        });



        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchForCustomer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Buy.this, "Search...",
                        "What are you looking for...?", null, createSampleData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                searchForCustomer_btn.setText(item.getTitle());
                                searchForCustomer_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                searchForCustomer_btn.setTextColor(getResources().getColor(R.color.textGrey));

                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        searchForItem_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Buy.this, "Search...",
                        "What are you looking for...?", null, createSampleData2(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                searchForItem_btn.setText(item.getTitle());
                                searchForItem_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                searchForItem_btn.setTextColor(getResources().getColor(R.color.textGrey));

                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        // Firebase config

        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker=new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"date picker");

            }
        });

        customer_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buy.this,Customer_details.class);
                startActivity(intent);
            }
        });

        item_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buy.this,Item_detail.class);
                startActivity(intent);
            }
        });

        gmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Buy.this, "yes", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(Intent.ACTION_SEND);
                it.setType("message/rfc822");
                startActivity(Intent.createChooser(it,"Choose Mail App"));
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}
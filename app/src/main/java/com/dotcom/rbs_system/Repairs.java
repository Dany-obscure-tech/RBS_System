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
import android.widget.LinearLayout;
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

public class Repairs extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ImageButton Back_btn;
    Button date_btn,customer_add_btn,searchForCustomer_btn,addItem2_btn,searchForItem_btn,item_add_btn;
    TextView date_text;
    List<String> exisitngCustomerList,exisitngCustomerIDList,exisitngItemsList,exisitngItemsIDList,exisitngItemsCategoryList;
    DatabaseReference existingCustomersRef,existingItemsRef,selectedItemRef;
    String firebaseAuthUID;
    ImageButton gmail_btn;

    private ArrayList<SampleSearchModel> createSampleData2(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<exisitngItemsList.size();i++){
            items.add(new SampleSearchModel(exisitngItemsList.get(i)+"\n("+exisitngItemsIDList.get(i)+")",exisitngItemsIDList.get(i),exisitngItemsList.get(i),exisitngItemsCategoryList.get(i)));
        }

        return items;
    }

    private ArrayList<SampleSearchModel> createSampleData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<exisitngCustomerList.size();i++){
//            items.add(new SampleSearchModel(exisitngCustomerList.get(i),exisitngCustomerIDList.get(i)));
        }

        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairs);

        initialize();

        fetchingExisitingCustomers();
        fetchingExisitingItems();

        onClickListeners();

    }

    private void initialize() {
        exisitngCustomerList = new ArrayList<>();
        exisitngCustomerIDList = new ArrayList<>();
        exisitngItemsList = new ArrayList<>();
        exisitngItemsIDList = new ArrayList<>();
        exisitngItemsCategoryList = new ArrayList<>();

        date_btn=(Button)findViewById(R.id.date_btn);
        Back_btn=(ImageButton)findViewById(R.id.Back_btn);
        date_text=(TextView)findViewById(R.id.date_text);
        gmail_btn=(ImageButton) findViewById(R.id.gmail_btn);
        customer_add_btn=(Button) findViewById(R.id.customer_add_btn);
        searchForCustomer_btn = (Button)findViewById(R.id.searchForCustomer_btn);
        item_add_btn =(Button) findViewById(R.id.item_add_btn);

        /////Firebase config
        firebaseAuthUID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
        existingCustomersRef = FirebaseDatabase.getInstance().getReference("Users_databases/"+firebaseAuthUID+"/Customer_list");
        existingItemsRef = FirebaseDatabase.getInstance().getReference("Items");
        searchForItem_btn = (Button)findViewById(R.id.searchForItem_btn);
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
                        exisitngItemsIDList.add(String.valueOf(dataSnapshot2.child("key_id").getValue()));
                        exisitngItemsCategoryList.add(String.valueOf(dataSnapshot2.child("Category").getValue()));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void onClickListeners() {
        searchForCustomer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Repairs.this, "Search...",
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
                new SimpleSearchDialogCompat(Repairs.this, "Search...",
                        "What are you looking for...?", null, createSampleData2(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   final SampleSearchModel item, int position) {
                                searchForItem_btn.setText(item.getName());
                                searchForItem_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                searchForItem_btn.setTextColor(getResources().getColor(R.color.textGrey));

                                selectedItemRef = FirebaseDatabase.getInstance().getReference("Items/"+item.getCategory()+"/"+item.getId());
                                selectedItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Toast.makeText(Repairs.this, String.valueOf(dataSnapshot.child("Condition")), Toast.LENGTH_SHORT).show();
                                        // todo
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                dialog.dismiss();

                            }
                        }).show();
            }
        });


        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                Intent intent = new Intent(Repairs.this,Customer_details.class);
                startActivity(intent);
            }
        });
        item_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Repairs.this,Item_detail.class);
                startActivity(intent);
            }
        });

        gmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Repairs.this, "yes", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(Intent.ACTION_SEND);
                it.setType("message/rfc822");
                startActivity(Intent.createChooser(it,"Choose Mail App"));
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
}

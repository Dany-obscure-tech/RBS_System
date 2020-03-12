package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Repairs extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    LinearLayout itemDetails,customerDetails;
    ImageButton Back_btn;
    Button date_btn,customer_add_btn,searchForCustomer_btn,submit_btn,searchForItem_btn,item_add_btn;
    TextView category_textView,condition_textView,notes_textView,phno_textView,dob_textView,address_textView,email_textView;
    TextView date_text;
    List<String> exisitngCustomerList,exisitngCustomerIDList,exisitngCustomerKeyIDList,exisitngItemsList,exisitngItemsIDList,exisitngItemsKeyIDList;
    List<String> exisitngItemsCategoryList,existingItemsConditionsList,existingItemsNotesList,existingCustomerPhnoList,existingCustomerDobList,existingCustomerAddressList,existingCustomerEmailList;
    DatabaseReference existingCustomersRef,existingItemsRef,selectedItemRef;
    String firebaseAuthUID;
    ImageButton gmail_btn,sms_btn,print_btn;
    DatabaseReference reference;
    TextView date_textView;
    EditText listedFaults_editText,listedPrice_editText,agreed_price_editText,paidAmount_editText,balance_amount_editText,special_condition_editText;
    String customerKeyID, itemKeyID;

    private ArrayList<SampleSearchModel> createItemsData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<exisitngItemsList.size();i++){
            items.add(new SampleSearchModel(exisitngItemsList.get(i)+"\n("+exisitngItemsIDList.get(i)+")",exisitngItemsIDList.get(i),exisitngItemsList.get(i),exisitngItemsCategoryList.get(i),existingItemsConditionsList.get(i),existingItemsNotesList.get(i),null,exisitngCustomerKeyIDList.get(i)));
        }

        return items;
    }

    private ArrayList<SampleSearchModel> createCustomerData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<exisitngCustomerList.size();i++){
            items.add(new SampleSearchModel(exisitngCustomerList.get(i)+"\n("+exisitngCustomerIDList.get(i)+")",exisitngCustomerIDList.get(i),exisitngCustomerList.get(i),existingCustomerPhnoList.get(i),existingCustomerDobList.get(i),existingCustomerAddressList.get(i),existingCustomerEmailList.get(i),exisitngCustomerKeyIDList.get(i)));
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

        date_textView = (TextView)findViewById(R.id.date_textView);
        listedFaults_editText = (EditText)findViewById(R.id.listedFaults_editText);
        listedPrice_editText = (EditText)findViewById(R.id.listedPrice_editText);
        agreed_price_editText = (EditText)findViewById(R.id.agreed_price_editText);
        paidAmount_editText = (EditText)findViewById(R.id.paidAmount_editText);
        balance_amount_editText = (EditText)findViewById(R.id.balance_amount_editText);
        special_condition_editText = (EditText)findViewById(R.id.special_condition_editText);

        reference = FirebaseDatabase.getInstance().getReference();

        itemDetails = (LinearLayout)findViewById(R.id.itemDetails);
        customerDetails = (LinearLayout)findViewById(R.id.customerDetails);

        exisitngCustomerList = new ArrayList<>();
        exisitngCustomerIDList = new ArrayList<>();
        exisitngCustomerKeyIDList = new ArrayList<>();
        exisitngItemsList = new ArrayList<>();
        exisitngItemsIDList = new ArrayList<>();
        exisitngItemsKeyIDList = new ArrayList<>();

        exisitngItemsCategoryList = new ArrayList<>();
        existingItemsConditionsList= new ArrayList<>();
        existingItemsNotesList= new ArrayList<>();
        existingCustomerPhnoList= new ArrayList<>();
        existingCustomerDobList= new ArrayList<>();
        existingCustomerAddressList= new ArrayList<>();
        existingCustomerEmailList= new ArrayList<>();

        submit_btn=(Button)findViewById(R.id.submit_btn);
        date_btn=(Button)findViewById(R.id.date_btn);
        Back_btn=(ImageButton)findViewById(R.id.Back_btn);
        date_text=(TextView)findViewById(R.id.date_text);
        gmail_btn=(ImageButton) findViewById(R.id.gmail_btn);
        print_btn=(ImageButton) findViewById(R.id.print_btn);
        customer_add_btn=(Button) findViewById(R.id.customer_add_btn);
        searchForCustomer_btn = (Button)findViewById(R.id.searchForCustomer_btn);
        item_add_btn =(Button) findViewById(R.id.item_add_btn);
        sms_btn=(ImageButton)findViewById(R.id.sms_btn);

        category_textView=(TextView)findViewById(R.id.category_textView);
        condition_textView=(TextView)findViewById(R.id.condition_textView);
        notes_textView=(TextView)findViewById(R.id.notes_textView);
        phno_textView=(TextView)findViewById(R.id.phno_textView);
        dob_textView=(TextView)findViewById(R.id.dob_textView);
        address_textView=(TextView)findViewById(R.id.address_textView);
        email_textView=(TextView)findViewById(R.id.email_textView);

        /////Firebase config
        firebaseAuthUID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
        existingCustomersRef = FirebaseDatabase.getInstance().getReference("Customer_list");
        existingItemsRef = FirebaseDatabase.getInstance().getReference("Items");
        searchForItem_btn = (Button)findViewById(R.id.searchForItem_btn);
    }

    private void fetchingExisitingCustomers() {
        existingCustomersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    exisitngCustomerList.add(String.valueOf(dataSnapshot1.child("Name").getValue()));
                    exisitngCustomerIDList.add(String.valueOf(dataSnapshot1.child("ID").getValue()));
                    existingCustomerPhnoList.add(String.valueOf(dataSnapshot1.child("Phone_no").getValue()));
                    existingCustomerDobList.add(String.valueOf(dataSnapshot1.child("DOB").getValue()));
                    existingCustomerAddressList.add(String.valueOf(dataSnapshot1.child("Address").getValue()));
                    existingCustomerEmailList.add(String.valueOf(dataSnapshot1.child("Email").getValue()));
                    exisitngCustomerKeyIDList.add(String.valueOf(dataSnapshot1.child("key_id").getValue()));
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
                        exisitngItemsIDList.add(String.valueOf(dataSnapshot2.child("Item_id").getValue()));
                        exisitngItemsCategoryList.add(String.valueOf(dataSnapshot2.child("Category").getValue()));
                        existingItemsConditionsList.add(String.valueOf(dataSnapshot2.child("Condition").getValue()));
                        existingItemsNotesList.add(String.valueOf(dataSnapshot2.child("Notes").getValue()));
                        exisitngItemsKeyIDList.add(String.valueOf(dataSnapshot2.child("key_id").getValue()));
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
                        "What are you looking for...?", null, createCustomerData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                searchForCustomer_btn.setText(item.getTitle());
                                phno_textView.setText(item.getVal1());
                                dob_textView.setText(item.getVal2());
                                address_textView.setText(item.getVal3());
                                email_textView.setText(item.getVal4());
                                customerKeyID = item.getVal5();
                                searchForCustomer_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                searchForCustomer_btn.setTextColor(getResources().getColor(R.color.textGrey));
                                customerDetails.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        searchForItem_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Repairs.this, "Search...",
                        "What are you looking for...?", null, createItemsData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   final SampleSearchModel item, int position) {
                                searchForItem_btn.setText(item.getTitle());
                                category_textView.setText(item.getVal1());
                                condition_textView.setText(item.getVal2());
                                notes_textView.setText(item.getVal3());
                                itemKeyID = item.getVal5();
                                searchForItem_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                searchForItem_btn.setTextColor(getResources().getColor(R.color.textGrey));

                                selectedItemRef = FirebaseDatabase.getInstance().getReference("Items/"+item.getVal1()+"/"+item.getId());
                                selectedItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                itemDetails.setVisibility(View.VISIBLE);
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
        sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "0323"));
                intent.putExtra("sms_body", "Hi how are you");
                startActivity(intent);
            }
        });

        print_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsSubmit();
            }
        });

    }

    private void detailsSubmit() {
        String key = reference.push().getKey();
        reference.child("Repairs_list").child(key).child("Customer_keyID").setValue(customerKeyID);
        reference.child("Repairs_list").child(key).child("Item_keyID").setValue(itemKeyID);

        reference.child("Repairs_list").child(key).child("Listed_faults").setValue(listedFaults_editText.getText().toString());
        reference.child("Repairs_list").child(key).child("Listed_price").setValue(listedPrice_editText.getText().toString());
        reference.child("Repairs_list").child(key).child("Agreed_price").setValue(agreed_price_editText.getText().toString());

        reference.child("Repairs_list").child(key).child("Date").setValue(date_textView.getText().toString());
        reference.child("Repairs_list").child(key).child("Paid_amount").setValue(paidAmount_editText.getText().toString());
        reference.child("Repairs_list").child(key).child("Balance_amount").setValue(balance_amount_editText.getText().toString());
        reference.child("Repairs_list").child(key).child("Special_conditiomn").setValue(special_condition_editText.getText().toString());
        reference.child("Repairs_list").child(key).child("key_id").setValue(key);
        reference.child("Repairs_list").child(key).child("added_by").setValue(firebaseAuthUID);


        finish();

    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date_textView.setText(currentDateString);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}

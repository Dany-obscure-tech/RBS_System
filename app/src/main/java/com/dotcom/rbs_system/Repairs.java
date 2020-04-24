package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterRepairsFaultListRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterSettingsFaultListRecyclerView;
import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.InetAddress;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Repairs extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Progress_dialoge pd;
    Boolean item_btn,customer;

    LinearLayout itemDetails,customerDetails,toggling_linear;
    ImageButton Back_btn;
    Button date_btn,customer_add_btn,searchForCustomer_btn,submit_btn,searchForItem_btn,item_add_btn;
    TextView category_textView,condition_textView,notes_textView,phno_textView,dob_textView,address_textView,email_textView;
    TextView date_text,balanceAmount_TextView;
    List<String> exisitngCustomerList,exisitngCustomerIDList,exisitngCustomerKeyIDList,exisitngItemsList,exisitngItemsIDList,exisitngItemsKeyIDList;
    List<String> exisitngItemsCategoryList,existingItemsConditionsList,existingItemsNotesList,existingCustomerPhnoList,existingCustomerDobList,existingCustomerAddressList,existingCustomerEmailList;
    DatabaseReference existingCustomersRef,existingItemsRef;
    String firebaseAuthUID;
    ImageButton gmail_btn,sms_btn,print_btn;
    DatabaseReference reference;
    TextView date_textView;
    EditText agreed_price_editText,paidAmount_editText,special_condition_editText;
    String customerKeyID, itemKeyID;
    Button addFaults_btn;
    Button btn_done;

    Dialog sendingdialog;

    RecyclerView faultList_recyclerView;
    AdapterRepairsFaultListRecyclerView adapterRepairsFaultListRecyclerView;

    DatabaseReference faultListRef;

    List<String> faultNameList, faultPriceList, faultKeyIDList;
    List<String> tempFaultNameList, tempFaultPriceList, tempFaultKeyIDList;

    private ArrayList<SampleSearchModel> createItemsData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<exisitngItemsList.size();i++){
            items.add(new SampleSearchModel(exisitngItemsList.get(i)+"\n("+exisitngItemsIDList.get(i)+")",exisitngItemsIDList.get(i),exisitngItemsList.get(i),exisitngItemsCategoryList.get(i),existingItemsConditionsList.get(i),existingItemsNotesList.get(i),null,exisitngItemsKeyIDList.get(i)));

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

    private ArrayList<SampleSearchModel> createFaultListData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<faultNameList.size();i++){
            items.add(new SampleSearchModel(faultNameList.get(i)+"\n("+faultPriceList.get(i)+")",faultKeyIDList.get(i),faultNameList.get(i),faultPriceList.get(i),null,null,null,null));

        }

        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairs);

        initialize();
        balanceAmount();
        getFaultsList();
        fetchingExisitingCustomers();
        fetchingExisitingItems();
        onClickListeners();
        item_btn=false;
        customer=false;


    }



    private void initialize() {

        pd = new Progress_dialoge();
        sendingdialog = new Dialog(this);
        sendingdialog.setContentView(R.layout.dialoge_items);
        //TODo
        gmail_btn = (ImageButton) sendingdialog.findViewById(R.id.gmail_btn);
        print_btn = (ImageButton) sendingdialog.findViewById(R.id.print_btn);
        sms_btn = (ImageButton) sendingdialog.findViewById(R.id.sms_btn);
        btn_done = (Button) sendingdialog.findViewById(R.id.btn_done);


        faultNameList = new ArrayList<>();
        faultPriceList = new ArrayList<>();
        faultKeyIDList = new ArrayList<>();

        tempFaultNameList = new ArrayList<>();
        tempFaultPriceList = new ArrayList<>();
        tempFaultKeyIDList = new ArrayList<>();

        faultListRef = FirebaseDatabase.getInstance().getReference("Listed_faults");

        addFaults_btn = (Button)findViewById(R.id.addFaults_btn);

        adapterRepairsFaultListRecyclerView = new AdapterRepairsFaultListRecyclerView(Repairs.this,tempFaultNameList,tempFaultPriceList,tempFaultKeyIDList);
        faultList_recyclerView = (RecyclerView) findViewById(R.id.faultList_recyclerView);
        faultList_recyclerView.setAdapter(adapterRepairsFaultListRecyclerView);
        faultList_recyclerView.setLayoutManager(new GridLayoutManager(Repairs.this,1));

        date_textView = (TextView)findViewById(R.id.date_textView);
        agreed_price_editText = (EditText)findViewById(R.id.agreed_price_editText);
        paidAmount_editText = (EditText)findViewById(R.id.paidAmount_editText);
        balanceAmount_TextView = (TextView) findViewById(R.id.balanceAmount_TextView);
        special_condition_editText = (EditText)findViewById(R.id.special_condition_editText);

        reference = FirebaseDatabase.getInstance().getReference();

        itemDetails = (LinearLayout)findViewById(R.id.itemDetails);
        customerDetails = (LinearLayout)findViewById(R.id.customerDetails);
        toggling_linear = (LinearLayout)findViewById(R.id.toggling_linear);

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
        date_text=(TextView)findViewById(R.id.date_of_birth_text);

        customer_add_btn=(Button) findViewById(R.id.customer_add_btn);
        searchForCustomer_btn = (Button)findViewById(R.id.searchForCustomer_btn);
        item_add_btn =(Button) findViewById(R.id.item_add_btn);


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
//                pd.dismissProgressBar(Repairs.this);
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

//                pd.dismissProgressBar(Repairs.this);
            }
        });

    }

    private void getFaultsList() {
        faultListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    faultNameList.add(String.valueOf(dataSnapshot1.child("Fault_name").getValue()));
                    faultPriceList.add(String.valueOf(dataSnapshot1.child("Fault_price").getValue()));
                    faultKeyIDList.add(String.valueOf(dataSnapshot1.child("key_id").getValue()));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                pd.dismissProgressBar(Repairs.this);

            }
        });
    }

    private void onClickListeners() {
        addFaults_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Repairs.this, "Search...",
                        "What are you looking for...?", null, createFaultListData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                tempFaultNameList.add(item.getName());
                                tempFaultKeyIDList.add(item.getId());
                                tempFaultPriceList.add(item.getVal1());

                                adapterRepairsFaultListRecyclerView.notifyDataSetChanged();

                                dialog.dismiss();
                            }
                        }).show();
            }
        });

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
                                customer=true;
                                if (item_btn==true&&customer==true){
                                    toggling_linear.setVisibility(View.VISIBLE);
                                }
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
                                itemDetails.setVisibility(View.VISIBLE);
                                item_btn=true;
                                if (item_btn==true&&customer==true){
                                    toggling_linear.setVisibility(View.VISIBLE);
                                }
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
        print_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Repairs.this, "YEs working", Toast.LENGTH_SHORT).show();
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


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields() == true){
                    detailsSubmit();
                }
            }
        });

    }

    private boolean validateFields() {
        boolean valid = true;

        if (searchForItem_btn.getText().toString().equals("Search for item")) {
            Toast.makeText(this, "Please select item", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (searchForCustomer_btn.getText().toString().equals("Search for customer")) {
            Toast.makeText(this, "Please select customer", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (agreed_price_editText.getText().toString().isEmpty()) {
            agreed_price_editText.setError("Please enter agreed price");
            valid = false;
        }
        if (date_textView.getText().toString().equals("Select date")) {
            Toast.makeText(this, "Select date", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (paidAmount_editText.getText().toString().isEmpty()) {
            paidAmount_editText.setError("Please enter paid amount");
            valid = false;
        }
        if (special_condition_editText.getText().toString().isEmpty()) {
            special_condition_editText.setError("Please enter special condition");
            valid = false;
        }
        if (tempFaultNameList.size()==0){
            Toast.makeText(this, "Please enter faults", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void detailsSubmit() {
        pd.showProgressBar(Repairs.this);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Repairs.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            String key = reference.push().getKey();
            reference.child("Repairs_list").child(key).child("Customer_keyID").setValue(customerKeyID);
            reference.child("Repairs_list").child(key).child("Item_keyID").setValue(itemKeyID);
            reference.child("Repairs_list").child(key).child("Agreed_price").setValue(agreed_price_editText.getText().toString());
            reference.child("Repairs_list").child(key).child("Date").setValue(date_textView.getText().toString());
            reference.child("Repairs_list").child(key).child("Paid_amount").setValue(paidAmount_editText.getText().toString());
            reference.child("Repairs_list").child(key).child("Balance_amount").setValue(balanceAmount_TextView.getText().toString());
            reference.child("Repairs_list").child(key).child("Special_conditiomn").setValue(special_condition_editText.getText().toString());
            reference.child("Repairs_list").child(key).child("key_id").setValue(key);
            reference.child("Repairs_list").child(key).child("added_by").setValue(firebaseAuthUID);

            for (int i = 0;i<tempFaultNameList.size();i++){
                reference.child("Repairs_list").child(key).child("Faults").child("Fault_"+String.valueOf(i+1)).child("Fault_name").setValue(tempFaultNameList.get(i));
                reference.child("Repairs_list").child(key).child("Faults").child("Fault_"+String.valueOf(i+1)).child("Fault_price").setValue(tempFaultPriceList.get(i));
                reference.child("Repairs_list").child(key).child("Faults").child("Fault_"+String.valueOf(i+1)).child("Fault_key").setValue(tempFaultKeyIDList.get(i));
            }
            //TODO

            Toast.makeText(this, "Submit Successfully", Toast.LENGTH_SHORT).show();
            pd.dismissProgressBar(Repairs.this);
            sendingdialog.show();

        }
        else
        {Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
         }

    }

    private void balanceAmount() {
        agreed_price_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (agreed_price_editText.getText().toString().isEmpty()){
                    balanceAmount_TextView.setText("NA");
                }else {
                    balanceAmount_TextView.setText(agreed_price_editText.getText().toString());
                    paidAmount_editText.setText("");
                }
            }
        });

        paidAmount_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                pd.dismissProgressBar(Repairs.this);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                pd.dismissProgressBar(Repairs.this);

            }

            @Override
            public void afterTextChanged(Editable s) {
//                pd.showProgressBar(Repairs.this);
                if(balanceAmount_TextView.getText().toString().equals("NA")){

                }else {
                    if (paidAmount_editText.getText().toString().equals("")){

                    }else {
                        int agreedprice = Integer.parseInt(agreed_price_editText.getText().toString());
                        int paidprice = Integer.parseInt(paidAmount_editText.getText().toString());

                        if (paidprice>agreedprice){
                            String sss = String.valueOf(agreedprice);
                            paidAmount_editText.setText(sss);
                            balanceAmount_TextView.setText("0");
                        }else {
                            String sss = String.valueOf(agreedprice-paidprice);
                            balanceAmount_TextView.setText(sss);
                        }
                    }

                }
//                pd.dismissProgressBar(Repairs.this);

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
        date_textView.setText(currentDateString);
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        recreate();
//    }

}

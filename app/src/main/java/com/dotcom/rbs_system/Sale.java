package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterRepairTicketListRecyclerView;
import com.dotcom.rbs_system.Classes.Exchanged_itemdata;
import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Sale extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Exchanged_itemdata exchangeObj = Exchanged_itemdata.getInstance();

    private static final int ITEM_ACTIVITY_REQUEST_CODE = 0;
    private static final int CUSTOMER_ACTIVITY_REQUEST_CODE = 0;

    TextView searchForVoucher_textView,Transaction_textview;
    Progress_dialoge pd;

    DatabaseReference reference,itemHistoryRef;
    DatabaseReference existingCustomersRef,existingItemsRef,existingVoucherRef;
    CheckBox cash_checkbox,voucher_checkbox;
    Query orderQuery;


    ImageButton Back_btn,sms_btn,gmail_btn,print_btn;
    Button date_btn,exchange_btn,customer_add_btn,item_add_btn,submit_btn;
    Button exchangeItemRemove_btn,btn_done;

    Dialog sendingdialog;
    LinearLayout toggling_linear;
    Boolean item_btn,customer;


    TextView searchForItem_textView,searchForCustomer_textView;
    TextView category_textView,condition_textView,notes_textView,phno_textView,dob_textView,address_textView,email_textView,suggest_price_TextView;
    TextView date_textView;
    TextView last_active_textView;
    TextView exchangeItemName_textView,exchangeItemCategory_textView,exchangeItemCondition_textView,exchangeItemAgreedPrice_textView,exchangeItemNotes_textView;

    String firebaseAuthUID,itemID;
    String customerKeyID, itemKeyID,customerName,itemCategory,itemName;

    List<String> exisitngCustomerList,exisitngCustomerIDList,exisitngCustomerKeyIDList,exisitngItemsList,exisitngItemsIDList,exisitngItemsKeyIDList;
    List<String> exisitngItemsCategoryList,existingItemsPriceList,existingItemsConditionsList,existingItemsNotesList,existingCustomerPhnoList,existingCustomerDobList,existingCustomerAddressList,existingCustomerEmailList;
    List<String> voucher_number_list,Voucher_amount_list;
    List<String> dateList,lastActiveDatelist;

    LinearLayout itemDetails,customerDetails,exchangeItemDetails;

    EditText sale_price_editText,cash_editText,paid_editText;

    Date date;

    Progress_dialoge pd1,pd2,pd3;

    private ArrayList<SampleSearchModel> createItemsData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<exisitngItemsList.size();i++){
            items.add(new SampleSearchModel(exisitngItemsList.get(i)+"\n("+exisitngItemsIDList.get(i)+")",exisitngItemsIDList.get(i),exisitngItemsList.get(i),exisitngItemsCategoryList.get(i),existingItemsConditionsList.get(i),existingItemsNotesList.get(i),lastActiveDatelist.get(i),exisitngItemsKeyIDList.get(i)));

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

    private ArrayList<SampleSearchModel> getting_voucher_data(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<Voucher_amount_list.size();i++){
            items.add(new SampleSearchModel(voucher_number_list.get(i)+"\n("+Voucher_amount_list.get(i)+")",null,null,null,null,null,null,null));
        }

        return items;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        initialize();
        exchangeFromBuyCheck();
        fetchingExisitingCustomers();
        fetchingExisitingItems();
        gettingVoucherList();
        onClickListenes();
        historyActivity();
    }

    private void initialize() {
        pd = new Progress_dialoge();
        item_btn=false;
        customer=false;

        pd1 = new Progress_dialoge();
        pd2 = new Progress_dialoge();
        pd3 = new Progress_dialoge();

        reference = FirebaseDatabase.getInstance().getReference();

        itemDetails = (LinearLayout)findViewById(R.id.itemDetails);
        customerDetails = (LinearLayout)findViewById(R.id.customerDetails);
        exchangeItemDetails = (LinearLayout)findViewById(R.id.exchangeItemDetails);
        toggling_linear = (LinearLayout)findViewById(R.id.toggling_linear);

        cash_checkbox=(CheckBox)findViewById(R.id.cash_checkbox);
        Transaction_textview=(TextView) findViewById(R.id.Transaction_textview);
        voucher_checkbox=(CheckBox)findViewById(R.id.voucher_checkbox);

        exisitngCustomerList = new ArrayList<>();
        exisitngCustomerIDList = new ArrayList<>();
        exisitngCustomerKeyIDList = new ArrayList<>();
        exisitngItemsList = new ArrayList<>();
        exisitngItemsIDList = new ArrayList<>();
        exisitngItemsKeyIDList = new ArrayList<>();

        exisitngItemsCategoryList = new ArrayList<>();
        existingItemsConditionsList= new ArrayList<>();
        existingItemsNotesList= new ArrayList<>();
        existingItemsPriceList= new ArrayList<>();
        existingCustomerPhnoList= new ArrayList<>();
        existingCustomerDobList= new ArrayList<>();
        existingCustomerAddressList= new ArrayList<>();
        existingCustomerEmailList= new ArrayList<>();
        voucher_number_list= new ArrayList<>();
        Voucher_amount_list= new ArrayList<>();

        sale_price_editText = (EditText) findViewById(R.id.sale_price_editText);
        cash_editText = (EditText) findViewById(R.id.cash_editText);
        paid_editText = (EditText) findViewById(R.id.paid_editText);

        suggest_price_TextView=(TextView)findViewById(R.id.suggest_price_TextView);
        searchForVoucher_textView=(TextView)findViewById(R.id.searchForVoucher_textView);
        category_textView=(TextView)findViewById(R.id.category_textView);
        condition_textView=(TextView)findViewById(R.id.condition_textView);
        notes_textView=(TextView)findViewById(R.id.notes_textView);
        phno_textView=(TextView)findViewById(R.id.phno_textView);
        dob_textView=(TextView)findViewById(R.id.dob_textView);
        address_textView=(TextView)findViewById(R.id.address_textView);
        email_textView=(TextView)findViewById(R.id.email_textView);
        exchangeItemName_textView=(TextView)findViewById(R.id.exchangeItemName_textView);
        exchangeItemCategory_textView=(TextView)findViewById(R.id.exchangeItemCategory_textView);
        exchangeItemCondition_textView=(TextView)findViewById(R.id.exchangeItemCondition_textView);
        exchangeItemAgreedPrice_textView=(TextView)findViewById(R.id.exchangeItemAgreedPrice_textView);
        exchangeItemNotes_textView=(TextView)findViewById(R.id.exchangeItemNotes_textView);
        searchForCustomer_textView = (TextView) findViewById(R.id.searchForCustomer_textView);
        searchForItem_textView = (TextView) findViewById(R.id.searchForItem_textView);
        last_active_textView =(TextView)findViewById(R.id.last_active_textView);

        Back_btn=(ImageButton)findViewById(R.id.Back_btn);
        submit_btn = (Button)findViewById(R.id.submit_btn);
        date_btn=(Button)findViewById(R.id.date_btn);
        exchange_btn=(Button)findViewById(R.id.exchange_btn);
        date_textView =(TextView)findViewById(R.id.date_textView);
        customer_add_btn=(Button) findViewById(R.id.customer_add_btn);
        item_add_btn=(Button) findViewById(R.id.item_add_btn);
        exchangeItemRemove_btn = (Button)findViewById(R.id.exchangeItemRemove_btn);

        sendingdialog = new Dialog(this);
        sendingdialog.setContentView(R.layout.dialoge_items);
        sendingdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });

        gmail_btn = (ImageButton) sendingdialog.findViewById(R.id.gmail_btn);
        print_btn = (ImageButton) sendingdialog.findViewById(R.id.print_btn);
        sms_btn = (ImageButton) sendingdialog.findViewById(R.id.sms_btn);
        btn_done = (Button) sendingdialog.findViewById(R.id.btn_done);

        exisitngCustomerList = new ArrayList<>();
        exisitngCustomerIDList = new ArrayList<>();
        dateList = new ArrayList<>();
        lastActiveDatelist = new ArrayList<>();


        /////Firebase config
        firebaseAuthUID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
        existingCustomersRef = FirebaseDatabase.getInstance().getReference("Customer_list");
        existingItemsRef = FirebaseDatabase.getInstance().getReference("Items");
        existingVoucherRef = FirebaseDatabase.getInstance().getReference("Voucher_List");
        existingVoucherRef = FirebaseDatabase.getInstance().getReference("Voucher_List/"+FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

        ///////

        date=Calendar.getInstance().getTime();
        String currentDateString= DateFormat.getDateInstance(DateFormat.FULL).format(date);
        date_textView.setText(currentDateString);

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = (Date)formatter.parse(date.getDay()+"-"+date.getMonth()+"-"+date.getYear());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void exchangeFromBuyCheck() {
        if (exchangeObj.getExchangeFromBuyCheck()){
            searchForCustomer_textView.setText(exchangeObj.getCustomerButtonText());
            phno_textView.setText(exchangeObj.getPhNo());
            dob_textView.setText(exchangeObj.getDob());
            address_textView.setText(exchangeObj.getAddress());
            email_textView.setText(exchangeObj.getEmail());
            customerKeyID = exchangeObj.getCustomer_keyID();
            searchForCustomer_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
            searchForCustomer_textView.setTextColor(getResources().getColor(R.color.textGrey));
            customerDetails.setVisibility(View.VISIBLE);

            exchangeItemName_textView.setText(exchangeObj.getName());
            exchangeItemCategory_textView.setText(exchangeObj.getCategory());
            exchangeItemCondition_textView.setText(exchangeObj.getCondition());
            exchangeItemAgreedPrice_textView.setText(exchangeObj.getPurchase_price());
            exchangeItemNotes_textView.setText(exchangeObj.getNotes());

            exchangeItemDetails.setVisibility(View.VISIBLE);
            customerName = exchangeObj.getName();
            customer=true;
        }
    }

    private void fetchingExisitingCustomers() {
        pd3.showProgressBar(Sale.this);
        existingCustomersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        exisitngCustomerList.add(String.valueOf(dataSnapshot1.child("Name").getValue()));
                        exisitngCustomerIDList.add(String.valueOf(dataSnapshot1.child("ID").getValue()));
                        existingCustomerPhnoList.add(String.valueOf(dataSnapshot1.child("Phone_no").getValue()));
                        existingCustomerDobList.add(String.valueOf(dataSnapshot1.child("DOB").getValue()));
                        existingCustomerAddressList.add(String.valueOf(dataSnapshot1.child("Address").getValue()));
                        existingCustomerEmailList.add(String.valueOf(dataSnapshot1.child("Email").getValue()));
                        exisitngCustomerKeyIDList.add(String.valueOf(dataSnapshot1.child("key_id").getValue()));

                        pd3.dismissProgressBar(Sale.this);
                    }
                }else {
                    pd3.dismissProgressBar(Sale.this);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd3.dismissProgressBar(Sale.this);
            }
        });

    }

    private void fetchingExisitingItems() {
        pd2.showProgressBar(Sale.this);
        existingItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int i = 0;
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                            exisitngItemsList.add(String.valueOf(dataSnapshot2.child("Item_name").getValue()));
                            exisitngItemsIDList.add(String.valueOf(dataSnapshot2.child("Item_id").getValue()));
                            exisitngItemsCategoryList.add(String.valueOf(dataSnapshot2.child("Category").getValue()));
                            existingItemsConditionsList.add(String.valueOf(dataSnapshot2.child("Condition").getValue()));
                            existingItemsNotesList.add(String.valueOf(dataSnapshot2.child("Notes").getValue()));
                            existingItemsPriceList.add(String.valueOf(dataSnapshot2.child("Price").getValue()));
                            exisitngItemsKeyIDList.add(String.valueOf(dataSnapshot2.child("key_id").getValue()));
                            gettingHistoryList(String.valueOf(dataSnapshot2.child("key_id").getValue()));

                        }
                    }
                    pd2.dismissProgressBar(Sale.this);
                }else {
                    pd2.dismissProgressBar(Sale.this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd2.dismissProgressBar(Sale.this);

            }
        });

    }

    private void gettingHistoryList(String itemID) {
        itemHistoryRef = FirebaseDatabase.getInstance().getReference("Item_history/"+itemID);

        orderQuery = itemHistoryRef.orderByChild("Timestamp");
        orderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    dateList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        dateList.add(dataSnapshot1.child("Date").getValue().toString());

                    }
                    Collections.reverse(dateList);
                    lastActiveDatelist.add(dateList.get(0));


                }else {
                    lastActiveDatelist.add("NA");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void gettingVoucherList() {
        pd.showProgressBar(Sale.this);
        existingVoucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        voucher_number_list.add(dataSnapshot1.child("Voucher_number").getValue().toString());
                        Voucher_amount_list.add(dataSnapshot1.child("Voucher_amount").getValue().toString());

                    }
                    pd.dismissProgressBar(Sale.this);
                }else {
                    pd.dismissProgressBar(Sale.this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismissProgressBar(Sale.this);
            }

        });
    }

    private void onClickListenes() {

        cash_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cash_checkbox.isChecked()){
                    cash_editText.setVisibility(View.VISIBLE);

                }
                if (!cash_checkbox.isChecked()){
                    cash_editText.setVisibility(View.GONE);

                }
            }
        });

        voucher_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voucher_checkbox.isChecked()){
                    searchForVoucher_textView.setVisibility(View.VISIBLE);

                }
                if (!voucher_checkbox.isChecked()){
                    searchForVoucher_textView.setVisibility(View.GONE);
                }
            }
        });


        searchForVoucher_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Sale.this, "Search results...",
                        "Search for voucher number.", null, getting_voucher_data(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                searchForVoucher_textView.setText(item.getTitle());
                                searchForVoucher_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                                searchForVoucher_textView.setTextColor(getResources().getColor(R.color.textGrey));
                                dialog.dismiss();
                            }
                        }).show();
            }
        });


        searchForCustomer_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Sale.this, "Search...",
                        "What are you looking for...?", null, createCustomerData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                searchForCustomer_textView.setText(item.getTitle());
                                phno_textView.setText(item.getVal1());
                                customerName = item.getName();
                                dob_textView.setText(item.getVal2());
                                address_textView.setText(item.getVal3());
                                email_textView.setText(item.getVal4());
                                customerKeyID = item.getVal5();
                                searchForCustomer_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                                searchForCustomer_textView.setTextColor(getResources().getColor(R.color.textGrey));
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

        searchForItem_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Sale.this, "Search...",
                        "What are you looking for...?", null, createItemsData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   final SampleSearchModel item, int position) {
                                itemName = item.getName();
                                itemID = item.getId();
                                searchForItem_textView.setText(item.getTitle());
                                category_textView.setText(item.getVal1());
                                itemCategory = item.getVal1();
                                condition_textView.setText(item.getVal2());
                                notes_textView.setText(item.getVal3());
                                last_active_textView.setText(item.getVal4());
                                itemKeyID = item.getVal5();
                                searchForItem_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                                searchForItem_textView.setTextColor(getResources().getColor(R.color.textGrey));
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

        item_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sale.this,Item_detail.class);
                startActivityForResult(intent,ITEM_ACTIVITY_REQUEST_CODE);
            }
        });

        exchange_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchForCustomer_textView.getText().toString().equals("Search for customer")) {
                    exchangeObj.setCustomer_keyID(customerKeyID);
                    exchangeObj.setCustomerButtonText(searchForCustomer_textView.getText().toString());
                    exchangeObj.setPhNo(phno_textView.getText().toString());
                    exchangeObj.setDob(dob_textView.getText().toString());
                    exchangeObj.setAddress(address_textView.getText().toString());
                    exchangeObj.setEmail(email_textView.getText().toString());
                }
                Intent intent=new Intent(Sale.this,Buy.class);
                exchangeObj.setExchangeCheck(true);
                startActivityForResult(intent,1);
            }
        });
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker=new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"date picker");

            }
        });
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        customer_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sale.this,Customer_details.class);
                startActivityForResult(intent,CUSTOMER_ACTIVITY_REQUEST_CODE);
            }
        });

        print_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Sale.this, "YEs working", Toast.LENGTH_SHORT).show();
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
                if (validateFields() == true)
                    detailsSubmit();

            }
        });

        exchangeItemRemove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchangeItemDetails.setVisibility(View.GONE);
                exchangeObj.setExchangeCheck(false);
                exchangeItemName_textView.setText("Item name");
                exchangeObj.setExchangeFromBuyCheck(false);
            }
        });

    }

    private boolean validateFields() {
        boolean valid = true;

        if (searchForItem_textView.getText().toString().equals("SEARCH FOR ITEM")) {
            Toast.makeText(this, "Please select item", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (searchForCustomer_textView.getText().toString().equals("SEARCH FOR CUSTOMER")) {
            Toast.makeText(this, "Please select customer", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (sale_price_editText.getText().toString().isEmpty()) {
            sale_price_editText.setError("Please enter sale price");
            valid = false;
        }

        if (date_textView.getText().toString().equals("Select date")) {
            Toast.makeText(this, "Select date", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (cash_checkbox.isChecked()) {
            if (cash_editText.getText().toString().isEmpty()) {
                cash_editText.setError("Please enter cash");
                valid = false;
            }
        }

        if (voucher_checkbox.isChecked()){
            if (searchForVoucher_textView.getText().toString().equals("SEARCH FOR VOUCHER")) {
                Toast.makeText(this, "Select voucher number", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }

        if (!cash_checkbox.isChecked()&&!voucher_checkbox.isChecked()){
            Transaction_textview.setError("Select atleast one Transaction Method");
            Toast.makeText(this, "Select atleast one Transaction Method", Toast.LENGTH_SHORT).show();
            valid=false;
        }

        if (paid_editText.getText().toString().isEmpty()) {
            paid_editText.setError("Please enter paid amount");
            valid = false;
        }

        return valid;
    }

    private void detailsSubmit() {

        pd1.showProgressBar(Sale.this);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Sale.this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            String key = reference.push().getKey();


            reference.child("Sale_list").child(key).child("Customer_keyID").setValue(customerKeyID);
            reference.child("Sale_list").child(key).child("Item_keyID").setValue(itemKeyID);
            reference.child("Sale_list").child(key).child("Sale_price").setValue(sale_price_editText.getText().toString());
            reference.child("Sale_list").child(key).child("Date").setValue(date_textView.getText().toString());

            reference.child("Sale_list").child(key).child("Paid").setValue(paid_editText.getText().toString());


            reference.child("Sale_list").child(key).child("key_id").setValue(key);
            reference.child("Sale_list").child(key).child("added_by").setValue(firebaseAuthUID);

            if (cash_checkbox.isChecked()) {
                if (!cash_editText.getText().toString().isEmpty()) {
                    reference.child("Sale_list").child(key).child("Cash").setValue(cash_editText.getText().toString());
                }
            }

            //TODO
            //Problem is here of sending \n in the database with text

            if (voucher_checkbox.isChecked()) {
                if (!searchForVoucher_textView.getText().toString().equals("SEARCH FOR VOUCHER")) {
                    reference.child("Sale_list").child(key).child("Voucher_number").setValue(searchForVoucher_textView.getText().toString());
                }
            }

            reference.child("Item_history").child(itemKeyID).child(key).child("Item").setValue(itemKeyID);
            reference.child("Item_history").child(itemKeyID).child(key).child("Customer_name").setValue(customerName);
            reference.child("Item_history").child(itemKeyID).child(key).child("RBS").setValue("Sale");
            reference.child("Item_history").child(itemKeyID).child(key).child("Timestamp").setValue(date.getTime());
            reference.child("Item_history").child(itemKeyID).child(key).child("Date").setValue(date_textView.getText().toString());

            reference.child("Customer_history").child(customerKeyID).child(key).child("Item_name").setValue(itemName);
            reference.child("Customer_history").child(customerKeyID).child(key).child("Customer").setValue(customerKeyID);
            reference.child("Customer_history").child(customerKeyID).child(key).child("RBS").setValue("Sale");
            reference.child("Customer_history").child(customerKeyID).child(key).child("Timestamp").setValue(date.getTime());
            reference.child("Customer_history").child(customerKeyID).child(key).child("Date").setValue(date_textView.getText().toString());

            String check = exchangeItemName_textView.getText().toString();

            if (!exchangeItemName_textView.getText().toString().equals("Item name")){
                Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
                    String key2 = reference.push().getKey();
                    reference.child("Buy_list").child(key2).child("Customer_keyID").setValue(exchangeObj.getCustomer_keyID());
                    reference.child("Buy_list").child(key2).child("Item_keyID").setValue(exchangeObj.getItem_keyID());

                    reference.child("Buy_list").child(key2).child("Purchase_price").setValue(exchangeObj.getPurchase_price());
                    reference.child("Buy_list").child(key2).child("Quantity").setValue(exchangeObj.getQuantity());
                    reference.child("Buy_list").child(key2).child("Date").setValue(exchangeObj.getDate());
                    reference.child("Buy_list").child(key2).child("Cash").setValue(exchangeObj.getCash());
                    reference.child("Buy_list").child(key2).child("Voucher").setValue(exchangeObj.getVoucher());
                    reference.child("Buy_list").child(key2).child("Paid").setValue(exchangeObj.getPaid());

                    reference.child("Buy_list").child(key2).child("key_id").setValue(key2);
                    reference.child("Buy_list").child(key2).child("added_by").setValue(firebaseAuthUID);

                    reference.child("Item_history").child(exchangeObj.getItem_keyID()).child(key2).child("Item").setValue(exchangeObj.getItem_keyID());
                    reference.child("Item_history").child(exchangeObj.getItem_keyID()).child(key2).child("Customer_name").setValue(customerName);
                    reference.child("Item_history").child(exchangeObj.getItem_keyID()).child(key2).child("RBS").setValue("Buy");
                    reference.child("Item_history").child(exchangeObj.getItem_keyID()).child(key2).child("Timestamp").setValue(exchangeObj.getTimestamp());
                    reference.child("Item_history").child(exchangeObj.getItem_keyID()).child(key2).child("Date").setValue(exchangeObj.getDate());

                    reference.child("Customer_history").child(customerKeyID).child(key2).child("Item_name").setValue(exchangeObj.getName());
                    reference.child("Customer_history").child(customerKeyID).child(key2).child("Customer").setValue(customerKeyID);
                    reference.child("Customer_history").child(customerKeyID).child(key2).child("RBS").setValue("Buy");
                    reference.child("Customer_history").child(customerKeyID).child(key2).child("Timestamp").setValue(exchangeObj.getTimestamp());
                    reference.child("Customer_history").child(customerKeyID).child(key2).child("Date").setValue(exchangeObj.getDate());



            }

            pd1.dismissProgressBar(Sale.this);
            sendingdialog.show();

        }
        else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
            pd1.dismissProgressBar(Sale.this);
        }

    }

    private void historyActivity() {
        itemDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sale.this,Item_history.class);
                intent.putExtra("ITEM_ID",itemKeyID);
                intent.putExtra("ITEM_CATEGORY",itemCategory);
                startActivity(intent);
            }
        });

        customerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sale.this,Customer_history.class);
                intent.putExtra("CUSTOMER_ID",customerKeyID);
                startActivity(intent);
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

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = (Date)formatter.parse(dayOfMonth+"-"+month+"-"+year);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        date_textView.setText(currentDateString);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ITEM_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                // get String data from Intent
                String itemname_returnString = data.getStringExtra("Item_name");
                String itemid_returnString = data.getStringExtra("Item_id");
                String itemcategory_returnString = data.getStringExtra("Item_category");
                String itemkeyid_returnString = data.getStringExtra("Item_keyid");
                String returnString = data.getStringExtra("Last_Active");
                // set text view with string
                last_active_textView.setText(returnString);
                itemDetails.setVisibility(View.VISIBLE);

                itemKeyID = itemkeyid_returnString;
                itemCategory = itemcategory_returnString;

                searchForItem_textView.setText(itemname_returnString+"\n"+itemid_returnString);
                searchForItem_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                searchForItem_textView.setTextColor(getResources().getColor(R.color.textGrey));
            }
        }
        if (requestCode == CUSTOMER_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_FIRST_USER) { // Activity.RESULT_OK
                // get String data from Intent
                String title_returnString = data.getStringExtra("AC_title");
                String id_returnString = data.getStringExtra("AC_id");
                String key_id_returnString = data.getStringExtra("AC_key_id");
                String phone_no_returnString = data.getStringExtra("AC_phone_no");
                String email_returnString = data.getStringExtra("AC_email");
                // set text view with string

                customerKeyID = key_id_returnString;
                phno_textView.setText(phone_no_returnString);
                email_textView.setText(email_returnString);
                customerDetails.setVisibility(View.VISIBLE);

                searchForCustomer_textView.setText(title_returnString+"\n"+id_returnString);
                searchForCustomer_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                searchForCustomer_textView.setTextColor(getResources().getColor(R.color.textGrey));
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchingExisitingCustomers();
        fetchingExisitingItems();
    }
}

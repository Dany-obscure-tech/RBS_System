package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.Classes.Exchanged_itemdata;
import com.dotcom.rbs_system.Model.SampleSearchModel;
//import com.dotcom.rbs_system.asynch.AsyncTcpEscPosPrint;
//import com.dotcom.rbs_system.asynch.AsyncEscPosPrinter;
import com.dotcom.rbs_system.async.AsyncEscPosPrinter;
import com.dotcom.rbs_system.async.AsyncTcpEscPosPrint;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

public class Buy extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int ITEM_ACTIVITY_REQUEST_CODE = 0;

    AsyncTask asyncTask = null;
    AsyncEscPosPrinter printer;

    private static final int CUSTOMER_ACTIVITY_REQUEST_CODE = 0;

    Exchanged_itemdata exchanged_itemdata = Exchanged_itemdata.getInstance();

    CheckBox cash_checkbox,voucher_checkbox;

    TextView voucher_number,voucher_number_textview,Transaction_textview;

    String voucher_key,itemID;

    Progress_dialoge pd,pd2,pd3;

    LinearLayout toggling_linear;

    Boolean item_btn,customer;

    DatabaseReference reference,itemHistoryRef;

    DatabaseReference existingCustomersRef,existingItemsRef;

    Query orderQuery;

    ImageButton back_btn,sms_btn,gmail_btn,print_btn;

    Button date_btn,customer_add_btn,item_add_btn,submit_btn;

    Button exchange_btn, btn_done;

    TextView searchForItem_textView,searchForCustomer_textView;

    TextView date_textView,forExchange_textView;

    TextView category_textView,condition_textView,notes_textView,phno_textView,dob_textView,address_textView,email_textView,suggest_price_TextView;

    TextView last_active_textView;

    TextView itemName_textView,itemID_textView,itemPriceCurrency_textView,itemPrice_textView;

    ImageView itemImage_imageView;

    String firebaseAuthUID;

    String customerKeyID, itemKeyID,customerName,itemCategory,itemName;

    String voucherNo;

    List<String> exisitngCustomerList,exisitngCustomerIDList,exisitngCustomerKeyIDList,exisitngItemsList,exisitngItemsIDList,exisitngItemsKeyIDList;
    List<String> exisitngItemsCategoryList,existingItemsConditionsList,existingItemsPriceList,existingItemsNotesList,existingCustomerPhnoList,existingCustomerDobList,existingCustomerAddressList,existingCustomerEmailList;
    List<String> dateList,lastActiveDatelist;

    LinearLayout itemDetails,customerDetails,print_linearLayout;

    EditText purchase_price_editText,cash_editText,voucher_editText,paid_editText;

    EditText ipAddress;

    EditText portAddress;

    Dialog sendingdialog;

    Date date;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        initialize();
        exchangeCustomerCheck();
        fetchingExisitingCustomers();
        fetchingExisitingItems();
        onClickListeners();
        historyActivity();

    }

    private void initialize() {

        reference = FirebaseDatabase.getInstance().getReference();
        voucher_key = reference.push().getKey().toString();

        item_btn=false;
        customer=false;

        pd = new Progress_dialoge();
        pd2 = new Progress_dialoge();
        pd3 = new Progress_dialoge();

        sendingdialog = new Dialog(this);
        sendingdialog.setContentView(R.layout.dialoge_items);
        sendingdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference();
        voucherNo = reference.push().getKey();

        itemDetails = (LinearLayout)findViewById(R.id.itemDetails);
        customerDetails = (LinearLayout)findViewById(R.id.customerDetails);
        cash_checkbox=(CheckBox)findViewById(R.id.cash_checkbox);
        voucher_checkbox=(CheckBox)findViewById(R.id.voucher_checkbox);

        exisitngCustomerList = new ArrayList<>();
        exisitngCustomerIDList = new ArrayList<>();
        exisitngCustomerKeyIDList = new ArrayList<>();
        exisitngItemsList = new ArrayList<>();
        exisitngItemsIDList = new ArrayList<>();
        exisitngItemsKeyIDList = new ArrayList<>();

        exisitngItemsCategoryList = new ArrayList<>();
        existingItemsConditionsList= new ArrayList<>();
        existingItemsPriceList= new ArrayList<>();
        existingItemsNotesList= new ArrayList<>();
        existingCustomerPhnoList= new ArrayList<>();
        existingCustomerDobList= new ArrayList<>();
        existingCustomerAddressList= new ArrayList<>();
        existingCustomerEmailList= new ArrayList<>();

        dateList = new ArrayList<>();
        lastActiveDatelist = new ArrayList<>();

        category_textView=(TextView)findViewById(R.id.category_textView);
        voucher_number=(TextView)findViewById(R.id.voucher_number);
        Transaction_textview=(TextView)findViewById(R.id.Transaction_textview);
        voucher_number_textview=(TextView)findViewById(R.id.voucher_number_textview);
        voucher_number.setText(voucher_key.toString());

        condition_textView=(TextView)findViewById(R.id.condition_textView);
        notes_textView=(TextView)findViewById(R.id.notes_textView);
        phno_textView=(TextView)findViewById(R.id.phno_textView);
        dob_textView=(TextView)findViewById(R.id.dob_textView);
        address_textView=(TextView)findViewById(R.id.address_textView);
        email_textView=(TextView)findViewById(R.id.email_textView);
        date_textView =(TextView)findViewById(R.id.date_textView);
        forExchange_textView =(TextView)findViewById(R.id.forExchange_textView);
        last_active_textView =(TextView)findViewById(R.id.last_active_textView);
        suggest_price_TextView = (TextView) findViewById(R.id.suggest_price_TextView);

        itemName_textView = (TextView) findViewById(R.id.itemName_textView);
        itemID_textView = (TextView) findViewById(R.id.itemID_textView);
        itemPriceCurrency_textView = (TextView) findViewById(R.id.itemPriceCurrency_textView);
        itemPriceCurrency_textView.setText(Currency.getInstance().getCurrency());
        itemPrice_textView = (TextView) findViewById(R.id.itemPrice_textView);

        itemImage_imageView = (ImageView) findViewById(R.id.itemImage_imageView);

        purchase_price_editText = (EditText)findViewById(R.id.purchase_price_editText);
        cash_editText = (EditText)findViewById(R.id.cash_editText);
        voucher_editText = (EditText)findViewById(R.id.voucher_editText);
        paid_editText = (EditText)findViewById(R.id.paid_editText);

        searchForCustomer_textView = (TextView) findViewById(R.id.searchForCustomer_textView);
        searchForItem_textView = (TextView) findViewById(R.id.searchForItem_textView);
        submit_btn = (Button)findViewById(R.id.submit_btn);
        toggling_linear = (LinearLayout)findViewById(R.id.toggling_linear);

        firebaseAuthUID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
        existingCustomersRef = FirebaseDatabase.getInstance().getReference("Customer_list");
        existingItemsRef = FirebaseDatabase.getInstance().getReference("Items");

        back_btn =(ImageButton)findViewById(R.id.back_btn);
        customer_add_btn=(Button) findViewById(R.id.customer_add_btn);
        item_add_btn =(Button) findViewById(R.id.item_add_btn);
        date_btn=(Button)findViewById(R.id.date_btn);
        exchange_btn=(Button)findViewById(R.id.exchange_btn);
        print_linearLayout = (LinearLayout) sendingdialog.findViewById(R.id.print_linearLayout);

        ipAddress = (EditText) sendingdialog.findViewById(R.id.edittext_tcp_ip);
        portAddress = (EditText) sendingdialog.findViewById(R.id.edittext_tcp_port);

        gmail_btn = (ImageButton) sendingdialog.findViewById(R.id.gmail_btn);
        print_btn = (ImageButton) sendingdialog.findViewById(R.id.print_btn);
        sms_btn = (ImageButton) sendingdialog.findViewById(R.id.sms_btn);
        btn_done = (Button) sendingdialog.findViewById(R.id.btn_done);

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

    private void exchangeCustomerCheck() {
        if (!exchanged_itemdata.getCustomerButtonText().equals("Search for customer")&&!exchanged_itemdata.getCustomerButtonText().isEmpty()) {
            searchForCustomer_textView.setText(exchanged_itemdata.getCustomerButtonText());
            phno_textView.setText(exchanged_itemdata.getPhNo());
            dob_textView.setText(exchanged_itemdata.getDob());
            address_textView.setText(exchanged_itemdata.getAddress());
            email_textView.setText(exchanged_itemdata.getEmail());
            customerKeyID = exchanged_itemdata.getCustomer_keyID();
            searchForCustomer_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
            searchForCustomer_textView.setTextColor(getResources().getColor(R.color.textGrey));
            customerDetails.setVisibility(View.VISIBLE);
            customer=true;
        }
        if (exchanged_itemdata.getExchangeCheck()){
            exchange_btn.setVisibility(View.GONE);
            forExchange_textView.setVisibility(View.GONE);
        }
    }

    private void fetchingExisitingCustomers() {
        pd2.showProgressBar(Buy.this);
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

                    }
                    pd2.dismissProgressBar(Buy.this);
                }else {
                    pd2.dismissProgressBar(Buy.this);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd2.dismissProgressBar(Buy.this);
            }
        });

    }

    private void fetchingExisitingItems() {
        pd3.showProgressBar(Buy.this);
        existingItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                            exisitngItemsList.add(String.valueOf(dataSnapshot2.child("Item_name").getValue()));
                            exisitngItemsIDList.add(String.valueOf(dataSnapshot2.child("Item_id").getValue()));
                            exisitngItemsCategoryList.add(String.valueOf(dataSnapshot2.child("Category").getValue()));
                            existingItemsConditionsList.add(String.valueOf(dataSnapshot2.child("Condition").getValue()));
                            existingItemsPriceList.add(String.valueOf(dataSnapshot2.child("Price").getValue()));
                            existingItemsNotesList.add(String.valueOf(dataSnapshot2.child("Notes").getValue()));
                            exisitngItemsKeyIDList.add(String.valueOf(dataSnapshot2.child("key_id").getValue()));
                            gettingHistoryList(String.valueOf(dataSnapshot2.child("key_id").getValue()));
                        }
                    }
                    pd3.dismissProgressBar(Buy.this);
                }else {
                    pd3.dismissProgressBar(Buy.this);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd3.dismissProgressBar(Buy.this);
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

    private void onClickListeners() {

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exchanged_itemdata.getExchangeCheck()){
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("result",0);

                    setResult(RESULT_OK,resultIntent);
                    exchanged_itemdata.setExchangeCheck(false);
                    finish();
                }else {
                    finish();
                }
            }
        });

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
                    voucher_number.setVisibility(View.VISIBLE);
                    voucher_number_textview.setVisibility(View.VISIBLE);
                    voucher_editText.setVisibility(View.VISIBLE);

                }
                if (!voucher_checkbox.isChecked()){
                    voucher_number.setVisibility(View.GONE);
                    voucher_number_textview.setVisibility(View.GONE);
                    voucher_editText.setVisibility(View.GONE);

                }
            }
        });

        searchForCustomer_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Buy.this, "Search...",
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
                new SimpleSearchDialogCompat(Buy.this, "Search...",
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
                startActivityForResult(intent,CUSTOMER_ACTIVITY_REQUEST_CODE);
            }
        });

        item_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buy.this,Item_detail.class);
                startActivityForResult(intent,ITEM_ACTIVITY_REQUEST_CODE);
            }
        });

        print_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Buy.this, "YEs working", Toast.LENGTH_SHORT).show();
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
                if(validateFields() == true){
                    detailsSubmit();
                }
            }
        });

        exchange_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()==true){
                    exchanged_itemdata.setExchangeFromBuyCheck(true);
                    detailsSubmit();
//                    Toast.makeText(Buy.this, "Yes working", Toast.LENGTH_SHORT).show();
                }
            }
        });

        print_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printTcp();
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

        if (purchase_price_editText.getText().toString().isEmpty()) {
            purchase_price_editText.setError("Please enter suggested price");
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
            if (voucher_editText.getText().toString().isEmpty()) {
                voucher_editText.setError("Please enter voucher");
                valid = false;
            }
        }

        if (!cash_checkbox.isChecked()&&!voucher_checkbox.isChecked()){
            Toast.makeText(this, "Select atleast one Transaction Method", Toast.LENGTH_SHORT).show();
            valid=false;
        }

        if (paid_editText.getText().toString().isEmpty()) {
            paid_editText.setError("Please enter paid amount");
            valid = false;
        }

        return valid;
    }

    private void   detailsSubmit() {

        if (exchanged_itemdata.getExchangeCheck()||exchanged_itemdata.getExchangeFromBuyCheck()){

            exchanged_itemdata.setCustomer_keyID(customerKeyID);
            exchanged_itemdata.setCustomerButtonText(searchForCustomer_textView.getText().toString());
            exchanged_itemdata.setPhNo(phno_textView.getText().toString());
            exchanged_itemdata.setDob(dob_textView.getText().toString());
            exchanged_itemdata.setAddress(address_textView.getText().toString());
            exchanged_itemdata.setEmail(email_textView.getText().toString());

            exchanged_itemdata.setItem_keyID(itemKeyID);
            exchanged_itemdata.setPurchase_price(purchase_price_editText.getText().toString());
            exchanged_itemdata.setDate(date_textView.getText().toString());
            exchanged_itemdata.setCash(cash_editText.getText().toString());
            exchanged_itemdata.setVoucher(voucher_editText.getText().toString());
            exchanged_itemdata.setPaid(paid_editText.getText().toString());

            exchanged_itemdata.setName(itemName);
            exchanged_itemdata.setCondition(condition_textView.getText().toString());
            exchanged_itemdata.setCategory(category_textView.getText().toString());
            exchanged_itemdata.setNotes(notes_textView.getText().toString());
            exchanged_itemdata.setTimestamp(date.getTime());


            if (exchanged_itemdata.getExchangeFromBuyCheck()){
                Intent intent = new Intent(Buy.this,Sale.class);
                startActivity(intent);
                finish();
            }else {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result",1);

                setResult(RESULT_OK,resultIntent);
                finish();
            }


        }else {
            pd.showProgressBar(Buy.this);

            boolean connected = false;
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Buy.this.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                //we are connected to a network
                connected = true;

                String key = reference.push().getKey();
                reference.child("Buy_list").child(key).child("Customer_keyID").setValue(customerKeyID);
                reference.child("Buy_list").child(key).child("Item_keyID").setValue(itemKeyID);

                reference.child("Buy_list").child(key).child("Purchase_price").setValue(purchase_price_editText.getText().toString());
                reference.child("Buy_list").child(key).child("Date").setValue(date_textView.getText().toString());

                reference.child("Buy_list").child(key).child("Paid").setValue(paid_editText.getText().toString());

                reference.child("Buy_list").child(key).child("key_id").setValue(key);
                reference.child("Buy_list").child(key).child("added_by").setValue(firebaseAuthUID);

                reference.child("Item_history").child(itemKeyID).child(key).child("Item").setValue(itemKeyID);
                reference.child("Item_history").child(itemKeyID).child(key).child("Customer_name").setValue(customerName);
                reference.child("Item_history").child(itemKeyID).child(key).child("RBS").setValue("Buy");
                reference.child("Item_history").child(itemKeyID).child(key).child("Timestamp").setValue(date.getTime());
                reference.child("Item_history").child(itemKeyID).child(key).child("Date").setValue(date_textView.getText().toString());

                reference.child("Customer_history").child(customerKeyID).child(key).child("Item_name").setValue(itemName);
                reference.child("Customer_history").child(customerKeyID).child(key).child("Customer").setValue(customerKeyID);
                reference.child("Customer_history").child(customerKeyID).child(key).child("RBS").setValue("Buy");
                reference.child("Customer_history").child(customerKeyID).child(key).child("Timestamp").setValue(date.getTime());
                reference.child("Customer_history").child(customerKeyID).child(key).child("Date").setValue(date_textView.getText().toString());

                if (cash_checkbox.isChecked()) {
                    if (!cash_editText.getText().toString().isEmpty()) {
                        reference.child("Buy_list").child(key).child("Cash").setValue(cash_editText.getText().toString());
                    }
                }

                if (voucher_checkbox.isChecked()) {
                    if (!voucher_editText.getText().toString().isEmpty()) {
                        reference.child("Voucher_List").child(firebaseAuthUID).child(voucher_number.getText().toString()).child("Voucher_number").setValue(voucher_number.getText().toString());
                        reference.child("Voucher_List").child(firebaseAuthUID).child(voucher_number.getText().toString()).child("Voucher_amount").setValue(voucher_editText.getText().toString());
                    }
                }
                pd.dismissProgressBar(Buy.this);
                sendingdialog.show();
            }else{
                Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
                pd.dismissProgressBar(Buy.this);
            }

            connected = false;
        }

    }

    private void historyActivity() {
        itemDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buy.this,Item_history.class);
                intent.putExtra("ITEM_ID",itemKeyID);
                intent.putExtra("ITEM_CATEGORY",itemCategory);
                startActivity(intent);
            }
        });

        customerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buy.this,Customer_history.class);
                intent.putExtra("CUSTOMER_ID",customerKeyID);
                startActivity(intent);
            }
        });
    }

//    public void printTcp() {
//
//        try {
//            // this.printIt(new TcpConnection(ipAddress.getText().toString(), Integer.parseInt(portAddress.getText().toString())));
//            new AsyncTcpEscPosPrint(this)
//                    .execute(this.getAsyncEscPosPrinter(new TcpConnection(ipAddress.getText().toString(), Integer.parseInt(portAddress.getText().toString()))));
//        } catch (NumberFormatException e) {
//            new AlertDialog.Builder(this)
//                    .setTitle("Invalid TCP port address")
//                    .setMessage("Port field must be a number.")
//                    .show();
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Asynchronous printing
//     */
//    @SuppressLint("SimpleDateFormat")
//    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {
//        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
//        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
//        return printer.setTextToPrint(
//                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
//                        "[L]\n" +
//                        "[C]<u><font size='big'>ORDER N°045</font></u>\n" +
//                        "[L]\n" +
//                        "[C]<u type='double'>" + format.format(new Date()) + "</u>\n" +
//                        "[C]\n" +
//                        "[C]================================\n" +
//                        "[L]\n" +
//                        "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99e\n" +
//                        "[L]  + Size : S\n" +
//                        "[L]\n" +
//                        "[L]<b>AWESOME HAT</b>[R]24.99e\n" +
//                        "[L]  + Size : 57/58\n" +
//                        "[L]\n" +
//                        "[C]--------------------------------\n" +
//                        "[R]TOTAL PRICE :[R]34.98e\n" +
//                        "[R]TAX :[R]4.23e\n" +
//                        "[L]\n" +
//                        "[C]================================\n" +
//                        "[L]\n" +
//                        "[L]<u><font color='bg-black' size='tall'>Customer :</font></u>\n" +
//                        "[L]Raymond DUPONT\n" +
//                        "[L]5 rue des girafes\n" +
//                        "[L]31547 PERPETES\n" +
//                        "[L]Tel : +33801201456\n" +
//                        "\n" +
//                        "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
//                        "[L]\n" +
//                        "[C]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>\n"
//        );
//    }

    public void printTcp() {
        if (asyncTask==null){
            try {
                // this.printIt(new TcpConnection(ipAddress.getText().toString(), Integer.parseInt(portAddress.getText().toString())));
                asyncTask = new AsyncTcpEscPosPrint(this).execute(this.getAsyncEscPosPrinter(new TcpConnection(ipAddress.getText().toString(), Integer.parseInt(portAddress.getText().toString()))));
            } catch (NumberFormatException e) {
                new AlertDialog.Builder(this)
                        .setTitle("Invalid TCP port address")
                        .setMessage("Port field must be a number.")
                        .show();
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
            asyncTask = null;

            try {
                // this.printIt(new TcpConnection(ipAddress.getText().toString(), Integer.parseInt(portAddress.getText().toString())));
                asyncTask = new AsyncTcpEscPosPrint(this).execute(this.getAsyncEscPosPrinter2(printer));
            } catch (NumberFormatException e) {
                new AlertDialog.Builder(this)
                        .setTitle("Invalid TCP port address")
                        .setMessage("Port field must be a number.")
                        .show();
                e.printStackTrace();
            }
        }

    }

    /**
     * Asynchronous printing
     */
    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {
        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        return printer.setTextToPrint(
                printingData()
        );
    }


    /**
     * Asynchronous printing
     */
    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter2(AsyncEscPosPrinter printer) {
        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        return printer.setTextToPrint(
                printingData()
        );
    }

    public String printingData() {
        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        return "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                "[L]\n" +
                "[C]<u type='double'>" + format.format(new Date()) + "</u>\n" +
                "[C]\n" +
                "[C]================================\n" +
                "[L]\n" +
                "[L]<b>"+searchForItem_textView.getText().toString()+"</b>\n" +
                "[C]--------------------------------\n" +
                "[R]TOTAL PRICE :[R]"+purchase_price_editText.getText().toString()+Currency.getInstance().getCurrency()+ "\n" +
                "[L]\n" +
                "[C]================================\n" +
                "[L]\n" +
                "[L]<u><font color='bg-black' size='tall'>Customer :</font></u>\n" +
                "[L]"+searchForCustomer_textView.getText().toString()+"\n" +
                "[L]Phno : "+phno_textView.getText().toString()+"\n" +
                "\n" +
                "[L]\n";
    }


    @Override
    public void onBackPressed() {
        if (exchanged_itemdata.getExchangeCheck()){
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result",0);

            setResult(RESULT_OK,resultIntent);
            exchanged_itemdata.setExchangeCheck(false);
            exchanged_itemdata.setExchangeFromBuyCheck(false);
            finish();
        }else {
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchingExisitingCustomers();
        fetchingExisitingItems();
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
                String itemprice_returnString = data.getStringExtra("Item_price");
                String itemimage_returnString = data.getStringExtra("Item_image");
                String returnString = data.getStringExtra("Last_Active");
                // set text view with string
                last_active_textView.setText(returnString);
                itemDetails.setVisibility(View.VISIBLE);

                itemKeyID = itemkeyid_returnString;
                itemCategory = itemcategory_returnString;

                itemName_textView.setText(itemname_returnString);
                itemID_textView.setText(itemid_returnString);
                itemName_textView.setTextColor(getResources().getColor(R.color.gradientDarkBlue));
                itemPrice_textView.setText(itemprice_returnString);

                itemID_textView.setVisibility(View.VISIBLE);
                itemPriceCurrency_textView.setVisibility(View.VISIBLE);
                itemPrice_textView.setVisibility(View.VISIBLE);
                itemImage_imageView.setVisibility(View.VISIBLE);

                searchForItem_textView.setText(itemname_returnString+"\n"+itemid_returnString);
                Picasso.get().load(itemimage_returnString).into(itemImage_imageView);
//                Toast.makeText(this, itemimage_returnString, Toast.LENGTH_SHORT).show();
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
}
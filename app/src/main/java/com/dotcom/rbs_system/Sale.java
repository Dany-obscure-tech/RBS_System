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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Classes.Exchanged_itemdata;
import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Sale extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Exchanged_itemdata exchangeObj = Exchanged_itemdata.getInstance();

    DatabaseReference reference;
    DatabaseReference existingCustomersRef,existingItemsRef;

    ImageButton Back_btn,sms_btn,gmail_btn,print_btn;
    Button date_btn,exchange_btn,customer_add_btn,searchForCustomer_btn,item_add_btn,searchForItem_btn,submit_btn;
    Button exchangeItemRemove_btn,btn_done;

    Dialog sendingdialog;
    LinearLayout toggling_linear;
    Boolean item_btn,customer;

    TextView category_textView,condition_textView,notes_textView,phno_textView,dob_textView,address_textView,email_textView,suggest_price_TextView;
    TextView date_textView;
    TextView exchangeItemName_textView,exchangeItemCategory_textView,exchangeItemCondition_textView,exchangeItemAgreedPrice_textView,exchangeItemNotes_textView;

    String firebaseAuthUID;
    String customerKeyID, itemKeyID,customerName,itemCategory,itemName;

    List<String> exisitngCustomerList,exisitngCustomerIDList,exisitngCustomerKeyIDList,exisitngItemsList,exisitngItemsIDList,exisitngItemsKeyIDList;
    List<String> exisitngItemsCategoryList,existingItemsPriceList,existingItemsConditionsList,existingItemsNotesList,existingCustomerPhnoList,existingCustomerDobList,existingCustomerAddressList,existingCustomerEmailList;


    LinearLayout itemDetails,customerDetails,exchangeItemDetails;

    EditText sale_price_editText,quantity_editText,cash_editText,paid_editText;

    Date date;

    Progress_dialoge pd1,pd2,pd3;

    private ArrayList<SampleSearchModel> createItemsData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<exisitngItemsList.size();i++){
            items.add(new SampleSearchModel(exisitngItemsList.get(i)+"\n("+exisitngItemsIDList.get(i)+")",exisitngItemsIDList.get(i),exisitngItemsList.get(i),exisitngItemsCategoryList.get(i),existingItemsConditionsList.get(i),existingItemsNotesList.get(i),existingItemsPriceList.get(i),exisitngItemsKeyIDList.get(i)));
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
        setContentView(R.layout.activity_sale);

        initialize();
        exchangeFromBuyCheck();
        fetchingExisitingCustomers();
        fetchingExisitingItems();
        onClickListenes();
        historyActivity();
    }

    private void initialize() {
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

        sale_price_editText = (EditText) findViewById(R.id.sale_price_editText);
        quantity_editText = (EditText) findViewById(R.id.quantity_editText);
        cash_editText = (EditText) findViewById(R.id.cash_editText);
        paid_editText = (EditText) findViewById(R.id.paid_editText);

        suggest_price_TextView=(TextView)findViewById(R.id.suggest_price_TextView);
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

        Back_btn=(ImageButton)findViewById(R.id.Back_btn);
        submit_btn = (Button)findViewById(R.id.submit_btn);
        searchForItem_btn = (Button)findViewById(R.id.searchForItem_btn);
        date_btn=(Button)findViewById(R.id.date_btn);
        exchange_btn=(Button)findViewById(R.id.exchange_btn);
        date_textView =(TextView)findViewById(R.id.date_textView);
        customer_add_btn=(Button) findViewById(R.id.customer_add_btn);
        item_add_btn=(Button) findViewById(R.id.item_add_btn);
        searchForCustomer_btn = (Button)findViewById(R.id.searchForCustomer_btn);
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


        /////Firebase config
        firebaseAuthUID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
        existingCustomersRef = FirebaseDatabase.getInstance().getReference("Customer_list");
        existingItemsRef = FirebaseDatabase.getInstance().getReference("Items");
        ///////
    }

    private void exchangeFromBuyCheck() {
        if (exchangeObj.getExchangeFromBuyCheck()){
            searchForCustomer_btn.setText(exchangeObj.getCustomerButtonText());
            phno_textView.setText(exchangeObj.getPhNo());
            dob_textView.setText(exchangeObj.getDob());
            address_textView.setText(exchangeObj.getAddress());
            email_textView.setText(exchangeObj.getEmail());
            customerKeyID = exchangeObj.getCustomer_keyID();
            searchForCustomer_btn.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
            searchForCustomer_btn.setTextColor(getResources().getColor(R.color.textGrey));
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
        existingCustomersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd3.showProgressBar(Sale.this);
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

            }
        });

    }

    private void fetchingExisitingItems() {
        existingItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd2.showProgressBar(Sale.this);
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
                            i++;
                        }
                    }
                    pd2.dismissProgressBar(Sale.this);
                }else {
                    pd2.dismissProgressBar(Sale.this);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void onClickListenes() {

        searchForCustomer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Sale.this, "Search...",
                        "What are you looking for...?", null, createCustomerData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                searchForCustomer_btn.setText(item.getTitle());
                                phno_textView.setText(item.getVal1());
                                customerName = item.getName();
                                dob_textView.setText(item.getVal2());
                                address_textView.setText(item.getVal3());
                                email_textView.setText(item.getVal4());
                                customerKeyID = item.getVal5();
                                searchForCustomer_btn.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
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
                new SimpleSearchDialogCompat(Sale.this, "Search...",
                        "What are you looking for...?", null, createItemsData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                searchForItem_btn.setText(item.getTitle());
                                itemCategory = item.getVal1();
                                itemName = item.getName();
                                category_textView.setText(item.getVal1());
                                condition_textView.setText(item.getVal2());
                                notes_textView.setText(item.getVal3());
                                suggest_price_TextView.setText(item.getVal4());
                                itemKeyID = item.getVal5();
                                searchForItem_btn.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
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

        item_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sale.this,Item_detail.class);
                startActivity(intent);
            }
        });

        exchange_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchForCustomer_btn.getText().toString().equals("Search for customer")) {
                    exchangeObj.setCustomer_keyID(customerKeyID);
                    exchangeObj.setCustomerButtonText(searchForCustomer_btn.getText().toString());
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
                startActivity(intent);
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

        if (searchForItem_btn.getText().toString().equals("Search for item")) {
            Toast.makeText(this, "Please select item", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (searchForCustomer_btn.getText().toString().equals("Search for customer")) {
            Toast.makeText(this, "Please select customer", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (sale_price_editText.getText().toString().isEmpty()) {
            sale_price_editText.setError("Please enter sale price");
            valid = false;
        }
        if (quantity_editText.getText().toString().isEmpty()) {
            quantity_editText.setError("Please enter quantity");
            valid = false;
        }
        if (date_textView.getText().toString().equals("Select date")) {
            Toast.makeText(this, "Select date", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (cash_editText.getText().toString().isEmpty()) {
            cash_editText.setError("Please enter cash");
            valid = false;
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
            reference.child("Sale_list").child(key).child("Quantity").setValue(quantity_editText.getText().toString());
            reference.child("Sale_list").child(key).child("Date").setValue(date_textView.getText().toString());
            reference.child("Sale_list").child(key).child("Cash").setValue(cash_editText.getText().toString());
            reference.child("Sale_list").child(key).child("Paid").setValue(paid_editText.getText().toString());


            reference.child("Sale_list").child(key).child("key_id").setValue(key);
            reference.child("Sale_list").child(key).child("added_by").setValue(firebaseAuthUID);

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

        if (data.getIntExtra("result",-1)==1){
            exchangeItemName_textView.setText(exchangeObj.getName());
            exchangeItemCategory_textView.setText(exchangeObj.getCategory());
            exchangeItemCondition_textView.setText(exchangeObj.getCondition());
            exchangeItemAgreedPrice_textView.setText(exchangeObj.getPurchase_price());
            exchangeItemNotes_textView.setText(exchangeObj.getNotes());

            exchangeItemDetails.setVisibility(View.VISIBLE);
        }else {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchingExisitingCustomers();
        fetchingExisitingItems();

    }
}

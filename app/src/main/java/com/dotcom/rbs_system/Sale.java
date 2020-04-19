package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Sale extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Exchanged_itemdata exchangeObj = Exchanged_itemdata.getInstance();

    DatabaseReference reference;
    DatabaseReference existingCustomersRef,existingItemsRef;

    ImageButton Back_btn,sms_btn,gmail_btn;
    Button date_btn,exchange_btn,customer_add_btn,searchForCustomer_btn,item_add_btn,searchForItem_btn,submit_btn;
    Button exchangeItemRemove_btn;

    Progress_dialoge pd;

    TextView category_textView,condition_textView,notes_textView,phno_textView,dob_textView,address_textView,email_textView,suggest_price_TextView;
    TextView date_textView;
    TextView exchangeItemName_textView,exchangeItemCategory_textView,exchangeItemCondition_textView,exchangeItemAgreedPrice_textView,exchangeItemNotes_textView;

    String firebaseAuthUID;
    String customerKeyID, itemKeyID;

    List<String> exisitngCustomerList,exisitngCustomerIDList,exisitngCustomerKeyIDList,exisitngItemsList,exisitngItemsIDList,exisitngItemsKeyIDList;
    List<String> exisitngItemsCategoryList,existingItemsPriceList,existingItemsConditionsList,existingItemsNotesList,existingCustomerPhnoList,existingCustomerDobList,existingCustomerAddressList,existingCustomerEmailList;


    LinearLayout itemDetails,customerDetails,exchangeItemDetails;

    EditText sale_price_editText,quantity_editText,cash_editText,paid_editText;


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

    }

    private void initialize() {

        pd = new Progress_dialoge();

        reference = FirebaseDatabase.getInstance().getReference();

        itemDetails = (LinearLayout)findViewById(R.id.itemDetails);
        customerDetails = (LinearLayout)findViewById(R.id.customerDetails);
        exchangeItemDetails = (LinearLayout)findViewById(R.id.exchangeItemDetails);

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

        sms_btn=(ImageButton)findViewById(R.id.sms_btn);
        gmail_btn=(ImageButton) findViewById(R.id.gmail_btn);
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
        }
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
                                dob_textView.setText(item.getVal2());
                                address_textView.setText(item.getVal3());
                                email_textView.setText(item.getVal4());
                                customerKeyID = item.getVal5();
                                searchForCustomer_btn.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
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
                new SimpleSearchDialogCompat(Sale.this, "Search...",
                        "What are you looking for...?", null, createItemsData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                searchForItem_btn.setText(item.getTitle());
                                category_textView.setText(item.getVal1());
                                condition_textView.setText(item.getVal2());
                                notes_textView.setText(item.getVal3());
                                suggest_price_TextView.setText(item.getVal4());
                                itemKeyID = item.getVal5();
                                searchForItem_btn.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                                searchForItem_btn.setTextColor(getResources().getColor(R.color.textGrey));
                                itemDetails.setVisibility(View.VISIBLE);
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

        gmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Sale.this, "yes", Toast.LENGTH_SHORT).show();
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

        pd.showProgressBar(Sale.this);

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
            pd.dismissProgressBar(Sale.this);
            finish();

        }
        else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
        }

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
}

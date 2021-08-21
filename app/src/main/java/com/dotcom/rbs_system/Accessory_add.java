package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterAccessoriesItemsRecyclerView;
import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Accessory_add extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    String currency,invoiceNo,vendorName,vendorPhno;

    Date date;

    Button vendor_add_btn, alertCategoryAdd_btn,alertAddCategoryEnter_btn,alertAddCategoryCancel_btn,date_btn,alertAddAccessoryEnter_btn,alertAddAccessoryCancel_btn,addAccessory_btn,submit_btn;

    ImageButton Back_btn;

    TextView searchForVendor_textView, date_textView,alertAccessoryTotalPrice_textView,invoiceNo_TextView,alertCategory_textView,totalPrice_TextView;

    EditText alertAddCategoryName_editText,alertAccessoryName_editText,alertAccessoryQuantity_editText,alertAccessoryUnitPrice_editText,vendorInvoiceRef_editText;

    List<String> exisitngVendorNameList,exisitngVendorPhNoList,accesoriesCategoriesList,accessorySrNoList,accessoryNameList,accessoryQtyList,accessoryUnitPriceList,accessoryTotalPriceList;

    DatabaseReference existingCustomersRef,existingAccessoryCategories,accessoriesCategoryRef,AccessoryInvoicesRef,AccessoryItemsRef;

    Dialog categoryAddAlert,accessoryAddAlet;

    RecyclerView accessoryItemList_recyclerView;

    AdapterAccessoriesItemsRecyclerView adapterAccessoriesItemsRecyclerView;

    private static final int VENDOR_ACTIVITY_REQUEST_CODE = 0;

    private ArrayList<SampleSearchModel> createVendorData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i = 0; i< exisitngVendorNameList.size(); i++){
            items.add(new SampleSearchModel(exisitngVendorNameList.get(i)+"\nPh#: "+ exisitngVendorPhNoList.get(i), exisitngVendorPhNoList.get(i), exisitngVendorNameList.get(i),null,null,null,null,null));
        }

        return items;
    }

    private ArrayList<SampleSearchModel> createAccessoriesCategoryData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<accesoriesCategoriesList.size();i++){
            items.add(new SampleSearchModel(accesoriesCategoriesList.get(i),null,null,null,null,null,null,null));
        }

        return items;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory_add);

        Initialization();
        InitialProcess();
        Processes();
        ClickListeners();
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialization() {
        currency = Currency.getInstance().getCurrency();

        categoryAddAlert = new Dialog(this);
        accessoryAddAlet = new Dialog(this);
        categoryAddAlert.setContentView(R.layout.alert_add_category);
        accessoryAddAlet.setContentView(R.layout.alert_add_accesory_item);

        existingCustomersRef = FirebaseDatabase.getInstance().getReference("Vendor_list");
        existingAccessoryCategories = FirebaseDatabase.getInstance().getReference("Vendor_list");
        accessoriesCategoryRef = FirebaseDatabase.getInstance().getReference("Accessories_categories");
        AccessoryInvoicesRef = FirebaseDatabase.getInstance().getReference("Accessories_invoices/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        AccessoryItemsRef = FirebaseDatabase.getInstance().getReference("Accessories_items");

        vendor_add_btn = (Button)findViewById(R.id.vendor_add_btn);
        alertCategoryAdd_btn = (Button)findViewById(R.id.alertCategoryAdd_btn);
        alertAddCategoryEnter_btn  = (Button) categoryAddAlert.findViewById(R.id.alertAddCategoryEnter_btn);
        alertAddCategoryCancel_btn = (Button) categoryAddAlert.findViewById(R.id.alertAddCategoryCancel_btn);
        date_btn = (Button) findViewById(R.id.date_textview);
        alertAddAccessoryEnter_btn = (Button) accessoryAddAlet.findViewById(R.id.alertAddAccessoryEnter_btn);
        alertAddAccessoryCancel_btn = (Button) accessoryAddAlet.findViewById(R.id.alertAddAccessoryCancel_btn);
        addAccessory_btn = (Button)findViewById(R.id.addAccessory_btn);
        submit_btn = (Button)findViewById(R.id.submit_textview);
        Back_btn = (ImageButton)findViewById(R.id.Back_btn);

        alertAddCategoryName_editText = (EditText) categoryAddAlert.findViewById(R.id.alertAddCategoryName_editText);
        alertAccessoryName_editText = (EditText) accessoryAddAlet.findViewById(R.id.alertAccessoryName_editText);
        alertAccessoryQuantity_editText = (EditText) accessoryAddAlet.findViewById(R.id.alertAccessoryQuantity_editText);
        alertAccessoryUnitPrice_editText = (EditText) accessoryAddAlet.findViewById(R.id.alertAccessoryUnitPrice_editText);
        vendorInvoiceRef_editText = (EditText)findViewById(R.id.vendorInvoiceRef_editText);

        alertCategory_textView = (TextView) findViewById(R.id.alertCategory_textView);
        totalPrice_TextView = (TextView) findViewById(R.id.alertCategory_textView);
        searchForVendor_textView = (TextView)findViewById(R.id.searchForVendor_textView);
        date_textView = (TextView) findViewById(R.id.date_textView);
        invoiceNo_TextView = (TextView) findViewById(R.id.invoiceNo_TextView);
        alertAccessoryTotalPrice_textView = (TextView) accessoryAddAlet.findViewById(R.id.alertAccessoryTotalPrice_textView);

        date=Calendar.getInstance().getTime();
        String currentDateString= DateFormat.getDateInstance(DateFormat.FULL).format(date);
        date_textView.setText(currentDateString);
        date_textView.setText(currentDateString);

        exisitngVendorNameList = new ArrayList<>();
        exisitngVendorPhNoList = new ArrayList<>();
        accesoriesCategoriesList = new ArrayList<>();
        accessorySrNoList = new ArrayList<>();
        accessoryNameList = new ArrayList<>();
        accessoryQtyList = new ArrayList<>();
        accessoryUnitPriceList = new ArrayList<>();
        accessoryTotalPriceList = new ArrayList<>();

        adapterAccessoriesItemsRecyclerView = new AdapterAccessoriesItemsRecyclerView(Accessory_add.this,accessorySrNoList,accessoryNameList,accessoryQtyList,accessoryUnitPriceList,accessoryTotalPriceList);

        accessoryItemList_recyclerView = (RecyclerView)findViewById(R.id.accessoryItemList_recyclerView);
        accessoryItemList_recyclerView.setLayoutManager(new GridLayoutManager(Accessory_add.this,1));
        accessoryItemList_recyclerView.setAdapter(adapterAccessoriesItemsRecyclerView);

    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialProcess() {
        generatingInvoiceNo();
        fetchingExistingAccessoryCategories();
        fetchingExistingVendors();
    }

    private void generatingInvoiceNo() {
        invoiceNo = AccessoryInvoicesRef.push().getKey();
        invoiceNo_TextView.setText(invoiceNo);
    }

    private void fetchingExistingAccessoryCategories() {
        accessoriesCategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        accesoriesCategoriesList.add(String.valueOf(dataSnapshot1.getValue()));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchingExistingVendors() {
        existingCustomersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        exisitngVendorNameList.add(String.valueOf(dataSnapshot1.child("Name").getValue()));
                        exisitngVendorPhNoList.add(String.valueOf(dataSnapshot1.child("Phone_no").getValue()));

                    }
                }else {
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Processes() {
        AccessoryTotalPriceCalculation();
    }

    private void AccessoryTotalPriceCalculation() {
        currency = Currency.getInstance().getCurrency();

        alertAccessoryQuantity_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (alertAccessoryQuantity_editText.getText().toString().equals("")){
                    alertAccessoryTotalPrice_textView.setText("NA");
                }else {
                    if (!alertAccessoryUnitPrice_editText.getText().toString().equals("")){
                        float quantity = Float.parseFloat(alertAccessoryQuantity_editText.getText().toString());
                        float unitPrice = Float.parseFloat(alertAccessoryUnitPrice_editText.getText().toString());

                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                        alertAccessoryTotalPrice_textView.setText(currency+String.valueOf(Double.valueOf(twoDForm.format(quantity*unitPrice))));
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        alertAccessoryUnitPrice_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (alertAccessoryUnitPrice_editText.getText().toString().equals("")){
                    alertAccessoryTotalPrice_textView.setText("NA");
                }else {
                    if (!alertAccessoryQuantity_editText.getText().toString().equals("")){
                        float quantity = Float.parseFloat(alertAccessoryQuantity_editText.getText().toString());
                        float unitPrice = Float.parseFloat(alertAccessoryUnitPrice_editText.getText().toString());

                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                        alertAccessoryTotalPrice_textView.setText(currency+String.valueOf(Double.valueOf(twoDForm.format(quantity*unitPrice))));
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean validateAccessoryAlertFields() {
        boolean valid = true;
        if (alertAccessoryName_editText.getText().toString().isEmpty()){
            alertAccessoryName_editText.setError("Enter Name");
            valid = false;
        }

        if (alertAccessoryQuantity_editText.getText().toString().equals("")){
            alertAccessoryQuantity_editText.setError("Enter valid quantity");
            valid = false;

        }else {
            if (Integer.parseInt(alertAccessoryQuantity_editText.getText().toString())==0){
                alertAccessoryQuantity_editText.setError("Enter valid quantity");
                valid = false;
            }
        }

        if (alertAccessoryUnitPrice_editText.getText().toString().equals("")){
            alertAccessoryUnitPrice_editText.setError("Enter valid quantity");
            valid = false;
        }else {
            if (Float.parseFloat(alertAccessoryUnitPrice_editText.getText().toString())==0){
                alertAccessoryUnitPrice_editText.setError("Enter valid unit price");
                valid = false;
            }
        }



        return valid;
    }

    private boolean validate() {
        boolean valid = true;

        if (searchForVendor_textView.getText().toString().equals("SEARCH FOR VENDOR")){
            Toast.makeText(this, "Select Vendor!", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (alertCategory_textView.getText().toString().equals("SELECT CATEGORY")){
            Toast.makeText(this, "Select Category!", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (vendorInvoiceRef_editText.getText().toString().isEmpty()){
            vendorInvoiceRef_editText.setError("Enter Invoice Ref");
            valid = false;
        }

        if (accessoryNameList.size()==0){
            Toast.makeText(this, "Enter Accessories!", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void ClickListeners() {
        backbtn();
        detailsSubmit();
        closeAddAccessoryAlert();
        addAccessoryToList();
        selectDate();
        searchCategory();
        alertAddCategoryAdd();
        alertAddCategoryCancel();
        addCategoryAlert();
        selectVendor();
        addVendor();
        alertAddAccessory();
    }

    private void backbtn() {
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void detailsSubmit() {
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){

                    String s = alertAddCategoryName_editText.getText().toString();
                    String key = accessoriesCategoryRef.push().getKey();
                    accessoriesCategoryRef.child(key).setValue(s);

                    AccessoryInvoicesRef.child(invoiceNo).child("Invoice_no").setValue(invoiceNo);
                    AccessoryInvoicesRef.child(invoiceNo).child("Invoice_date").setValue(date_textView.getText().toString());
                    AccessoryInvoicesRef.child(invoiceNo).child("Vendor_name").setValue(vendorName);
                    AccessoryInvoicesRef.child(invoiceNo).child("Vendor_phNo").setValue(vendorPhno);
                    AccessoryInvoicesRef.child(invoiceNo).child("catagory").setValue(alertCategory_textView.getText().toString());



                    for (int i = 0;i<accessoryNameList.size();i++){
                        String key2 = AccessoryItemsRef.push().getKey();

                        AccessoryInvoicesRef.child(invoiceNo).child("Accessory_items").child("Item_"+(i+1)).child("name").setValue(accessoryNameList.get(i));
                        AccessoryInvoicesRef.child(invoiceNo).child("Accessory_items").child("Item_"+(i+1)).child("quantity").setValue(accessoryQtyList.get(i));
                        AccessoryInvoicesRef.child(invoiceNo).child("Accessory_items").child("Item_"+(i+1)).child("unit_price").setValue(accessoryUnitPriceList.get(i).replace("£",""));
                        AccessoryInvoicesRef.child(invoiceNo).child("Accessory_items").child("Item_"+(i+1)).child("total_price").setValue(accessoryTotalPriceList.get(i).replace("£",""));

                        AccessoryItemsRef.child(alertCategory_textView.getText().toString()).child(key2).setValue(accessoryNameList.get(i));
                    }



                    finish();
                }
            }
        });
    }

    private void closeAddAccessoryAlert() {
        alertAddAccessoryCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessoryAddAlet.dismiss();
            }
        });
    }

    private void addAccessoryToList() {
        alertAddAccessoryEnter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAccessoryAlertFields()){
                    accessorySrNoList.add(String.valueOf(accessorySrNoList.size()+1));
                    accessoryNameList.add(alertAccessoryName_editText.getText().toString());
                    accessoryQtyList.add(alertAccessoryQuantity_editText.getText().toString());
                    accessoryUnitPriceList.add(alertAccessoryUnitPrice_editText.getText().toString());
                    accessoryTotalPriceList.add(alertAccessoryTotalPrice_textView.getText().toString());

                    adapterAccessoriesItemsRecyclerView.notifyDataSetChanged();
                    accessoryAddAlet.dismiss();
                }
            }
        });


    }

    private void alertAddAccessory() {
        addAccessory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessoryAddAlet.show();
            }
        });
    }

    private void selectDate() {
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker=new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"date picker");

            }
        });
    }

    private void searchCategory() {
        alertCategory_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessory_add.this, "Search...",
                        "What are you looking for...?", null, createAccessoriesCategoryData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                alertCategory_textView.setText(item.getTitle());
                                alertCategory_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                                alertCategory_textView.setTextColor(getResources().getColor(R.color.textGrey));

                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void alertAddCategoryAdd() {
        alertAddCategoryEnter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = alertAddCategoryName_editText.getText().toString();

                Boolean valid = true;
                if (accesoriesCategoriesList.contains(s)){
                    alertAddCategoryName_editText.setError("Category already exists");
                    valid = false;
                }
                if (valid==true){
                    alertCategory_textView.setText(s);
                    accesoriesCategoriesList.add(s);

                    alertCategory_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                    alertCategory_textView.setTextColor(getResources().getColor(R.color.textGrey));
                    categoryAddAlert.dismiss();
                }
            }
        });
    }

    private void alertAddCategoryCancel() {
        alertAddCategoryCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryAddAlert.dismiss();
            }
        });
    }

    private void addCategoryAlert() {
        alertCategoryAdd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryAddAlert.setCancelable(false);
                categoryAddAlert.show();
            }
        });
    }

    private void selectVendor() {
        searchForVendor_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessory_add.this, "Search...",
                        "What are you looking for...?", null, createVendorData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                searchForVendor_textView.setText(item.getTitle());
                                vendorName = item.getName();
                                vendorPhno = item.getId();
                                searchForVendor_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                                searchForVendor_textView.setTextColor(getResources().getColor(R.color.textGrey));
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void addVendor() {
        vendor_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Accessory_add.this, RBS_accesories_vendor_detail.class);
                startActivityForResult(intent,VENDOR_ACTIVITY_REQUEST_CODE);
            }
        });
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VENDOR_ACTIVITY_REQUEST_CODE) {

            if (resultCode == 1) { // Activity.RESULT_OK
                
                // get String data from Intent
                String title_returnString = data.getStringExtra("AC_title");
                String key_id_returnString = data.getStringExtra("AC_key_id");
                String phone_no_returnString = data.getStringExtra("AC_phone_no");
                // set text view with string

                searchForVendor_textView.setText(title_returnString+"\n("+phone_no_returnString+")");
//                customerDetails.setVisibility(View.VISIBLE);

                searchForVendor_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                searchForVendor_textView.setTextColor(getResources().getColor(R.color.textGrey));
            }
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

}
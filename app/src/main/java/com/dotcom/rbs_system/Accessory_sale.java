package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
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

import com.dotcom.rbs_system.Adapter.AdapterAccessoriesSaleItemsRecyclerView;
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
import java.util.LinkedHashSet;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Accessory_sale extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    TextView invoiceNo_TextView;

    String invoiceNo;

    DatabaseReference AccessorySaleInvoicesRef;
    String category_name,currency;
    int container=100;
    Button saleAccessory_btn,date_btn,alertSaleAccessoryCancel_btn,alertAddAccessoryEnter_btn,submit_btn;
    ImageButton Back_btn;
    EditText customer_name_editText,customer_phone_no_editText,alertAccessoryQuantity_editText,alertAccessoryUnitPrice_editText,paid_editText;
    //    Date date;
    TextView date_textView,select_category_textView,select_itemname_textView,alertAccessoryTotalPrice_textView,category_textView,item_name_textView,balance_TextView;
    Dialog accessorySaleAlert;
    RecyclerView accessoryItemList_recyclerView1;
    DatabaseReference existing_categories;
    List<String> accessories_categories_raw_list,accessories_categories_filter_list;
    List<String> accessories_item_name_list,category_data,accessories_item_quantity_list,accessories_item_total_price_list,accessories_item_unit_price_list;
    List<String> accessories_category_list;
    List<String> accessorySrNoList,accessoryCategoryList,accessoryItemNameList,accessoryQtyList,accessoryUnitPriceList,accessoryTotalPriceList;
    List<String> new_item_name_list;
    AdapterAccessoriesSaleItemsRecyclerView adapterAccessoriesSaleItemsRecyclerView;

    private ArrayList<SampleSearchModel> selectCategoryData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i = 0; i< accessories_categories_filter_list.size(); i++){
            items.add(new SampleSearchModel(accessories_categories_filter_list.get(i), null,null,null,null,null,null,null));
        }

        return items;
    }

    private ArrayList<SampleSearchModel> selectItemNameData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i = 0; i< new_item_name_list.size(); i++){
            items.add(new SampleSearchModel(new_item_name_list.get(i), null,null,null,null,null,null,null));
        }

        return items;
    }


////////////////////////////////ON CREATE/////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory_sale);
        Initialization();
        ClickListeners();
        fetching_item_name();

    }


/////////////////////////////////////////////////////
    private void Initialization() {

        invoiceNo_TextView = (TextView) findViewById(R.id.invoiceNo_TextView);

        AccessorySaleInvoicesRef = FirebaseDatabase.getInstance().getReference("Accessories_Sale_invoices/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        Date date;

        date_textView = (TextView) findViewById(R.id.date_textView);

        currency = Currency.getInstance().getCurrency();
        existing_categories = FirebaseDatabase.getInstance().getReference("Accessories_invoices/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        accessories_categories_raw_list = new ArrayList<>();
        accessories_categories_filter_list = new ArrayList<>();
        accessories_item_name_list = new ArrayList<>();
        category_data = new ArrayList<>();
        accessories_item_quantity_list = new ArrayList<>();
        accessories_item_total_price_list = new ArrayList<>();
        accessories_item_unit_price_list = new ArrayList<>();
        accessories_category_list = new ArrayList<>();
        new_item_name_list = new ArrayList<>();
        accessorySrNoList = new ArrayList<>();
        accessoryCategoryList = new ArrayList<>();
        accessoryItemNameList = new ArrayList<>();
        accessoryQtyList = new ArrayList<>();
        accessoryUnitPriceList = new ArrayList<>();
        accessoryTotalPriceList = new ArrayList<>();
        accessorySaleAlert = new Dialog(this);
        accessorySaleAlert.setContentView(R.layout.alert_sale_accesory_item);

        Back_btn = (ImageButton) findViewById(R.id.Back_btn);
        customer_name_editText = (EditText) findViewById(R.id.customer_name_editText);
        customer_phone_no_editText = (EditText) findViewById(R.id.customer_phone_no_editText);
        date_btn = (Button) findViewById(R.id.date_textview);
        submit_btn = (Button) findViewById(R.id.submit_textview);
        alertAccessoryQuantity_editText=(EditText)accessorySaleAlert.findViewById(R.id.alertAccessoryQuantity_editText);
        alertAccessoryUnitPrice_editText=(EditText)accessorySaleAlert.findViewById(R.id.alertAccessoryUnitPrice_editText);
        paid_editText=(EditText)findViewById(R.id.paid_editText);
        saleAccessory_btn = (Button) findViewById(R.id.saleAccessory_btn);
        alertSaleAccessoryCancel_btn = (Button) accessorySaleAlert.findViewById(R.id.alertSaleAccessoryCancel_btn);
        alertAddAccessoryEnter_btn = (Button) accessorySaleAlert.findViewById(R.id.alertAddAccessoryEnter_btn);
        select_category_textView = (TextView) accessorySaleAlert.findViewById(R.id.select_category_textView);
        select_itemname_textView = (TextView) accessorySaleAlert.findViewById(R.id.select_itemname_textView);
        alertAccessoryTotalPrice_textView=(TextView)accessorySaleAlert.findViewById(R.id.alertAccessoryTotalPrice_textView);
        category_textView=(TextView)accessorySaleAlert.findViewById(R.id.category_textView);
        item_name_textView=(TextView)accessorySaleAlert.findViewById(R.id.item_name_textView);
        balance_TextView=(TextView)findViewById(R.id.balance_TextView);
        adapterAccessoriesSaleItemsRecyclerView = new AdapterAccessoriesSaleItemsRecyclerView(Accessory_sale.this,accessorySrNoList,accessoryCategoryList,accessoryItemNameList,accessoryQtyList,accessoryUnitPriceList,accessoryTotalPriceList);
        accessoryItemList_recyclerView1 = (RecyclerView)findViewById(R.id.accessoryItemList_recyclerView1);
        accessoryItemList_recyclerView1.setLayoutManager(new GridLayoutManager(Accessory_sale.this,1));
        accessoryItemList_recyclerView1.setAdapter(adapterAccessoriesSaleItemsRecyclerView);

        date=Calendar.getInstance().getTime();
        String currentDateString= DateFormat.getDateInstance(DateFormat.FULL).format(date);
        date_textView.setText(currentDateString);
        date_textView.setText(currentDateString);



        generatingInvoiceNo();
    }

/////////////////////////////////////////////////////

    private boolean validate() {
        boolean valid = true;


        return valid;
    }

    private void generatingInvoiceNo() {
        invoiceNo = AccessorySaleInvoicesRef.push().getKey();
        invoiceNo_TextView.setText(invoiceNo);
    }

/////////////////////////////////////////////////////

    private void ClickListeners() {

//        selectdate();

        backbtn();
        enterbtn();
        alertSaleAccessory();
        alertCancel();
        select_sale_category();
        select_item_name();
        AccessoryTotalPriceCalculation();
//        balancewatcher();
        addAccessoryToList();
        selectDate();
        detailsSubmit();


    }

    private boolean validate_fields() {
        boolean valid = true;
        if (customer_name_editText.getText().toString().isEmpty()){
            customer_name_editText.setError("Please enter name");
            valid = false;
        }
        if (customer_phone_no_editText.getText().toString().isEmpty()){
            customer_phone_no_editText.setError("Please enter password");
            valid=false;
        }
        if (date_textView.getText().toString().equals("Select date")){
            date_textView.setError("Please enter date");
            valid=false;
        }
        if (paid_editText.getText().toString().isEmpty()){
            paid_editText.setError("Please enter paid amount");
            valid=false;
        }

        return valid;
    }

    private void detailsSubmit() {
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate_fields()) {
                    if (validate()) {

                        AccessorySaleInvoicesRef.child(invoiceNo).child("Invoice_no").setValue(invoiceNo);
                        AccessorySaleInvoicesRef.child(invoiceNo).child("Invoice_date").setValue(date_textView.getText().toString());
                        AccessorySaleInvoicesRef.child(invoiceNo).child("Customer_name").setValue(customer_name_editText.getText().toString());
                        AccessorySaleInvoicesRef.child(invoiceNo).child("Customer_phNo").setValue(customer_phone_no_editText.getText().toString());
                        AccessorySaleInvoicesRef.child(invoiceNo).child("Paid").setValue(paid_editText.getText().toString());
//                        AccessorySaleInvoicesRef.child(invoiceNo).child("Balance").setValue(balance_TextView.getText().toString());

                        for (int i = 0; i < accessoryItemNameList.size(); i++) {
                            AccessorySaleInvoicesRef.child(invoiceNo).child("Accessory_items").child("Item_" + (i + 1)).child("name").setValue(accessoryItemNameList.get(i));
                            AccessorySaleInvoicesRef.child(invoiceNo).child("Accessory_items").child("Item_" + (i + 1)).child("category").setValue(accessoryCategoryList.get(i));
                            AccessorySaleInvoicesRef.child(invoiceNo).child("Accessory_items").child("Item_" + (i + 1)).child("quantity").setValue(accessoryQtyList.get(i));
                            AccessorySaleInvoicesRef.child(invoiceNo).child("Accessory_items").child("Item_" + (i + 1)).child("unit_price").setValue(accessoryUnitPriceList.get(i).replace("£", ""));
                            AccessorySaleInvoicesRef.child(invoiceNo).child("Accessory_items").child("Item_" + (i + 1)).child("total_price").setValue(accessoryTotalPriceList.get(i).replace("£", ""));

                        }

                        finish();
                    }
                }
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

    private void addAccessoryToList() {
        alertAddAccessoryEnter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateSaleAlertFields()) {
                    accessorySrNoList.add(String.valueOf(accessorySrNoList.size() + 1));
                    accessoryCategoryList.add(select_category_textView.getText().toString());
                    accessoryItemNameList.add(select_itemname_textView.getText().toString());
                    accessoryQtyList.add(alertAccessoryQuantity_editText.getText().toString());
                    accessoryUnitPriceList.add(alertAccessoryUnitPrice_editText.getText().toString());
                    accessoryTotalPriceList.add(alertAccessoryTotalPrice_textView.getText().toString());
                    adapterAccessoriesSaleItemsRecyclerView.notifyDataSetChanged();

                    accessorySaleAlert.dismiss();
                }
            }
        });
    }

    private void enterbtn() {
        alertAddAccessoryEnter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateSaleAlertFields()){
                    accessorySaleAlert.dismiss();
            }
            }
        });
    }

    private boolean validateSaleAlertFields() {
        boolean valid=true;

        if (select_category_textView.getText().toString().equals("SELECT CATEGORY")){
            Toast.makeText(this, "Select category", Toast.LENGTH_SHORT).show();
            valid=false;
        }
        if (select_itemname_textView.getText().toString().equals("SELECT ITEM NAME")){
            Toast.makeText(this, "Select item", Toast.LENGTH_SHORT).show();
            valid=false;
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

    private void balancewatcher() {
        paid_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (paid_editText.getText().toString().equals("Enter paid amount")){
                    balance_TextView.setText("NA");
                }
                else {
                    if (!paid_editText.getText().toString().equals("Enter paid amount")){
                        //TODO
                        for (int i=0;i<accessoryTotalPriceList.size();i++){
                            Toast.makeText(Accessory_sale.this, "YEs", Toast.LENGTH_SHORT).show();
//                            container= container+(Integer.parseInt(accessoryTotalPriceList.get(i)));
                        }
                        float container1 = container;
                        float paid = Float.parseFloat(paid_editText.getText().toString());

                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                        balance_TextView.setText(currency+String.valueOf(Double.valueOf(twoDForm.format(paid-container1))));

                    }
                    if (paid_editText.getText().toString().equals("")){
                        
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void AccessoryTotalPriceCalculation() {

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

    private void select_item_name() {
        select_itemname_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessory_sale.this, "Search...",
                        "What are you looking for...?", null, selectItemNameData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                select_itemname_textView.setText(item.getTitle());
//                                vendorName = item.getName();
//                                vendorPhno = item.getId();
                                select_itemname_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                                select_itemname_textView.setTextColor(getResources().getColor(R.color.textGrey));
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void select_sale_category() {
        select_category_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessory_sale.this, "Search...",
                        "What are you looking for...?", null, selectCategoryData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                select_category_textView.setText(item.getTitle());
//                                vendorName = item.getName();
//                                vendorPhno = item.getId();
                                select_category_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                                select_category_textView.setTextColor(getResources().getColor(R.color.textGrey));
                                dialog.dismiss();
                                category_name= item.getTitle();
                                new_item_name_list.clear();
                                set_item_name();
                            }
                        }).show();

            }
        });
    }

    private void set_item_name() {

        for (int i=0;i<accessories_category_list.size();i++){
            if (String.valueOf(accessories_category_list.get(i)).equals(category_name)){
                new_item_name_list.add(accessories_item_name_list.get(i));
            }
        }
    }

    private void alertCancel() {
        alertSaleAccessoryCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessorySaleAlert.dismiss();
            }
        });
    }

    private void alertSaleAccessory() {
        saleAccessory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessorySaleAlert.show();
                fetching_categories();

            }
        });

    }

    private void fetching_categories() {
        existing_categories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    accessories_categories_raw_list.add(String.valueOf(dataSnapshot1.child("catagory").getValue()));
                }
                accessories_categories_filter_list = new ArrayList<String>(new LinkedHashSet<String>(accessories_categories_raw_list));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetching_item_name() {
        existing_categories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    for (DataSnapshot dataSnapshot2:dataSnapshot1.child("Accessory_items").getChildren()){
                        accessories_category_list.add(String.valueOf(dataSnapshot1.child("catagory").getValue()));
                        accessories_item_name_list.add(String.valueOf(dataSnapshot2.child("name").getValue()));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void backbtn() {
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

}
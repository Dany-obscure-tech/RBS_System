package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.hbb20.CountryCodePicker;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Accessory_add extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String currency, invoiceNo, vendorName, vendorPhno;

    EditText alertvendorName_editText;

    String selected_country_code;

    EditText editTextCarrierNumber;

    DatabaseReference reference;

    CountryCodePicker ccp;

    String key;

    Date date;

    TextView alertAddVendor_save_textview, alertAddVendorAccessoryCancel_textview, ac_title;

    CardView search_vendor_cardview, search_category_cardview;

    TextView date_textview, submit_textview, addaccessory_textview, alertCategoryAdd_textview, vendor_add_textview, alertAddCategoryCancel_textview, alertAddCategoryEnter_textview, alertAddAccessoryCancel_textview, alertAddAccessoryEnter_textview;

    ImageButton back_btn;

    TextView searchForVendor_textView, date_textView, alertAccessoryTotalPrice_textView, alertAccessoryTotalPrice_currency_textView, invoiceNo_TextView, alertCategory_textView, totalPrice_TextView;

    EditText alertAddCategoryName_editText, alertAccessoryName_editText, alertAccessoryQuantity_editText, alertAccessoryUnitPrice_editText, vendorInvoiceRef_editText;

    List<String> exisitngVendorNameList, exisitngVendorPhNoList, accesoriesCategoriesList, accessorySrNoList, accessoryNameList, accessoryQtyList, accessoryUnitPriceList, accessoryTotalPriceList;

    DatabaseReference existingCustomersRef, existingAccessoryCategories, accessoriesCategoryRef, AccessoryInvoicesRef, AccessoryItemsRef;

    Dialog categoryAddAlert, accessoryAddAlert, vendorAddAlert;

    RecyclerView accessoryItemList_recyclerView;

    AdapterAccessoriesItemsRecyclerView adapterAccessoriesItemsRecyclerView;

    private static final int VENDOR_ACTIVITY_REQUEST_CODE = 0;

    private ArrayList<SampleSearchModel> createVendorData() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i = 0; i < exisitngVendorNameList.size(); i++) {
            items.add(new SampleSearchModel(exisitngVendorNameList.get(i) + "\nPh#: " + exisitngVendorPhNoList.get(i), exisitngVendorPhNoList.get(i), exisitngVendorNameList.get(i), null, null, null, null, null));
        }

        return items;
    }

    private ArrayList<SampleSearchModel> createAccessoriesCategoryData() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i = 0; i < accesoriesCategoriesList.size(); i++) {
            items.add(new SampleSearchModel(accesoriesCategoriesList.get(i), null, null, null, null, null, null, null));
        }

        return items;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory_add);
        //TODO accessory add aur accessory sale ko check karna ha kay data sahi sa ja raha ha kay nhi, aur validation bhi check karna ha
        Initialization();
        InitialProcess();
        Processes();
        ClickListeners();
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialization() {
        currency = Currency.getInstance().getCurrency();
        reference = FirebaseDatabase.getInstance().getReference();
        categoryAddAlert = new Dialog(this);
        accessoryAddAlert = new Dialog(this);
        vendorAddAlert = new Dialog(this);
        categoryAddAlert.setContentView(R.layout.alert_add_category);
        accessoryAddAlert.setContentView(R.layout.alert_add_accesory_item);
        vendorAddAlert.setContentView(R.layout.alert_add_vendor_item);

        alertvendorName_editText = vendorAddAlert.findViewById(R.id.alertvendorName_editText);
        ccp = vendorAddAlert.findViewById(R.id.ccp);
        editTextCarrierNumber = vendorAddAlert.findViewById(R.id.editTextCarrierNumber);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);

        alertAddVendor_save_textview = vendorAddAlert.findViewById(R.id.alertAddVendor_save_textview);
        alertAddVendorAccessoryCancel_textview = vendorAddAlert.findViewById(R.id.alertAddVendorAccessoryCancel_textview);

        existingCustomersRef = FirebaseDatabase.getInstance().getReference("Vendor_list");
        existingAccessoryCategories = FirebaseDatabase.getInstance().getReference("Vendor_list");
        accessoriesCategoryRef = FirebaseDatabase.getInstance().getReference("Accessories_categories");
        AccessoryInvoicesRef = FirebaseDatabase.getInstance().getReference("Accessories_invoices/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        AccessoryItemsRef = FirebaseDatabase.getInstance().getReference("Accessories_items");

        vendor_add_textview = findViewById(R.id.vendor_add_textview);
        alertCategoryAdd_textview = findViewById(R.id.alertCategoryAdd_textview);
        alertAddCategoryEnter_textview = categoryAddAlert.findViewById(R.id.alertAddCategoryEnter_textview);
        alertAddCategoryCancel_textview = categoryAddAlert.findViewById(R.id.alertAddCategoryCancel_textview);
        date_textview = findViewById(R.id.date_textview);
        alertAddAccessoryEnter_textview = accessoryAddAlert.findViewById(R.id.alertAddAccessoryEnter_textview);
        alertAddAccessoryCancel_textview = accessoryAddAlert.findViewById(R.id.alertAddAccessoryCancel_textview);
        addaccessory_textview = findViewById(R.id.addaccessory_textview);
        submit_textview = findViewById(R.id.submit_textview);
        back_btn = findViewById(R.id.back_btn);

        alertAddCategoryName_editText = categoryAddAlert.findViewById(R.id.alertAddCategoryName_editText);
        alertAccessoryName_editText = accessoryAddAlert.findViewById(R.id.alertAccessoryName_editText);
        alertAccessoryQuantity_editText = accessoryAddAlert.findViewById(R.id.alertAccessoryQuantity_editText);
        alertAccessoryUnitPrice_editText = accessoryAddAlert.findViewById(R.id.alertAccessoryUnitPrice_editText);
        vendorInvoiceRef_editText = findViewById(R.id.vendorInvoiceRef_editText);

        alertCategory_textView = findViewById(R.id.alertCategory_textView);
        search_category_cardview = findViewById(R.id.search_category_cardview);
        totalPrice_TextView = findViewById(R.id.alertCategory_textView);
        searchForVendor_textView = findViewById(R.id.searchForVendor_textView);
        search_vendor_cardview = findViewById(R.id.search_vendor_cardview);
        date_textView = findViewById(R.id.date_textView);
        invoiceNo_TextView = findViewById(R.id.invoiceNo_TextView);
        alertAccessoryTotalPrice_textView = accessoryAddAlert.findViewById(R.id.alertAccessoryTotalPrice_textView);
        alertAccessoryTotalPrice_currency_textView = accessoryAddAlert.findViewById(R.id.alertAccessoryTotalPrice_currency_textView);

        date = Calendar.getInstance().getTime();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);
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

        adapterAccessoriesItemsRecyclerView = new AdapterAccessoriesItemsRecyclerView(Accessory_add.this, accessorySrNoList, accessoryNameList, accessoryQtyList, accessoryUnitPriceList, accessoryTotalPriceList);

        accessoryItemList_recyclerView = (RecyclerView) findViewById(R.id.accessoryItemList_recyclerView);
        accessoryItemList_recyclerView.setLayoutManager(new GridLayoutManager(Accessory_add.this, 1));
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
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
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

                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        exisitngVendorNameList.add(String.valueOf(dataSnapshot1.child("Name").getValue()));
                        exisitngVendorPhNoList.add(String.valueOf(dataSnapshot1.child("Phone_no").getValue()));

                    }
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

                if (alertAccessoryQuantity_editText.getText().toString().equals("")) {
                    alertAccessoryTotalPrice_textView.setText("NA");
                } else {
                    if (!alertAccessoryUnitPrice_editText.getText().toString().equals("")) {
                        float quantity = Float.parseFloat(alertAccessoryQuantity_editText.getText().toString());
                        float unitPrice = Float.parseFloat(alertAccessoryUnitPrice_editText.getText().toString());

                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                        alertAccessoryTotalPrice_textView.setText(currency + Double.valueOf(twoDForm.format(quantity * unitPrice)));
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

                if (alertAccessoryUnitPrice_editText.getText().toString().equals("")) {
                    alertAccessoryTotalPrice_textView.setText("NA");
                } else {
                    if (!alertAccessoryQuantity_editText.getText().toString().equals("")) {
                        float quantity = Float.parseFloat(alertAccessoryQuantity_editText.getText().toString());
                        float unitPrice = Float.parseFloat(alertAccessoryUnitPrice_editText.getText().toString());

                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                        alertAccessoryTotalPrice_textView.setText(currency + Double.valueOf(twoDForm.format(quantity * unitPrice)));
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
        if (alertAccessoryName_editText.getText().toString().isEmpty()) {
            alertAccessoryName_editText.setError("Enter Name");
            valid = false;
        }

        if (alertAccessoryQuantity_editText.getText().toString().equals("")) {
            alertAccessoryQuantity_editText.setError("Enter valid quantity");
            valid = false;

        } else {
            if (Integer.parseInt(alertAccessoryQuantity_editText.getText().toString()) == 0) {
                alertAccessoryQuantity_editText.setError("Enter valid quantity");
                valid = false;
            }
        }

        if (alertAccessoryUnitPrice_editText.getText().toString().equals("")) {
            alertAccessoryUnitPrice_editText.setError("Enter valid quantity");
            valid = false;
        } else {
            if (Float.parseFloat(alertAccessoryUnitPrice_editText.getText().toString()) == 0) {
                alertAccessoryUnitPrice_editText.setError("Enter valid unit price");
                valid = false;
            }
        }


        return valid;
    }

    private boolean validate() {
        boolean valid = true;

        if (searchForVendor_textView.getText().toString().equals("Search vendors...")) {
            Toast.makeText(this, "Select Vendor!", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (alertCategory_textView.getText().toString().equals("Select category...")) {
            Toast.makeText(this, "Select Category!", Toast.LENGTH_SHORT).show();
            valid = false;
        }


        if (vendorInvoiceRef_editText.getText().toString().equals("")) {
            vendorInvoiceRef_editText.setError("Enter Invoice Ref");
            valid = false;
        }

        if (accessoryNameList.size() == 0) {
            Toast.makeText(this, "Enter Accessory Items!", Toast.LENGTH_SHORT).show();
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
        alertAddVendor_save_textview_listner();
        alertAddVendorAccessoryCancel_textview_listner();
    }

    private void alertAddVendorAccessoryCancel_textview_listner() {
        alertAddVendorAccessoryCancel_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorAddAlert.dismiss();
            }
        });
    }

    private void alertAddVendor_save_textview_listner() {
        alertAddVendor_save_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateVendorFields()) {
                    vendordetailsSubmit();
                }
            }
        });
    }

    private void vendordetailsSubmit() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Accessory_add.this.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            key = reference.push().getKey();
            reference.child("Vendor_list").child(key).child("Name").setValue(alertvendorName_editText.getText().toString());
            reference.child("Vendor_list").child(key).child("Phone_no").setValue(String.valueOf(ccp.getFullNumberWithPlus()));

            searchForVendor_textView.setText(alertvendorName_editText.getText().toString() + "\n(" + ccp.getFullNumberWithPlus() + ")");
            searchForVendor_textView.setTextColor(getResources().getColor(R.color.textBlue));
            vendorAddAlert.dismiss();

        } else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
        }
    }

    private boolean validateVendorFields() {
        boolean valid = true;

        if (alertvendorName_editText.getText().toString().isEmpty()) {
            alertvendorName_editText.setError("Please enter your name");
            valid = false;
        }
        if (alertvendorName_editText.length() > 32) {
            alertvendorName_editText.setError("Name character limit is 32");
            valid = false;
        }

        if (!ccp.isValidFullNumber()) {
            editTextCarrierNumber.setError("Please enter valid number");
            valid = false;
        }

        return valid;
    }

    private void backbtn() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void detailsSubmit() {
        submit_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    String s = alertAddCategoryName_editText.getText().toString();
                    String key = accessoriesCategoryRef.push().getKey();
                    accessoriesCategoryRef.child(key).setValue(s);

                    AccessoryInvoicesRef.child(invoiceNo).child("Invoice_no").setValue(invoiceNo);
                    AccessoryInvoicesRef.child(invoiceNo).child("Invoice_date").setValue(date_textView.getText().toString());
                    AccessoryInvoicesRef.child(invoiceNo).child("Vendor_name").setValue(vendorName);
                    AccessoryInvoicesRef.child(invoiceNo).child("Vendor_phNo").setValue(vendorPhno);
                    AccessoryInvoicesRef.child(invoiceNo).child("catagory").setValue(alertCategory_textView.getText().toString());


                    for (int i = 0; i < accessoryNameList.size(); i++) {
                        String key2 = AccessoryItemsRef.push().getKey();

                        AccessoryInvoicesRef.child(invoiceNo).child("Accessory_items").child("Item_" + (i + 1)).child("name").setValue(accessoryNameList.get(i));
                        AccessoryInvoicesRef.child(invoiceNo).child("Accessory_items").child("Item_" + (i + 1)).child("quantity").setValue(accessoryQtyList.get(i));
                        AccessoryInvoicesRef.child(invoiceNo).child("Accessory_items").child("Item_" + (i + 1)).child("unit_price").setValue(accessoryUnitPriceList.get(i).replace("£", ""));
                        AccessoryInvoicesRef.child(invoiceNo).child("Accessory_items").child("Item_" + (i + 1)).child("total_price").setValue(accessoryTotalPriceList.get(i).replace("£", ""));

                        AccessoryItemsRef.child(alertCategory_textView.getText().toString()).child(key2).setValue(accessoryNameList.get(i));
                    }


                    finish();
                }
            }
        });
    }

    private void closeAddAccessoryAlert() {
        alertAddAccessoryCancel_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessoryAddAlert.dismiss();
            }
        });
    }

    private void addAccessoryToList() {
        alertAddAccessoryEnter_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAccessoryAlertFields()) {
                    accessorySrNoList.add(String.valueOf(accessorySrNoList.size() + 1));
                    accessoryNameList.add(alertAccessoryName_editText.getText().toString());
                    accessoryQtyList.add(alertAccessoryQuantity_editText.getText().toString());
                    accessoryUnitPriceList.add(alertAccessoryUnitPrice_editText.getText().toString());
                    accessoryTotalPriceList.add(alertAccessoryTotalPrice_textView.getText().toString());

                    adapterAccessoriesItemsRecyclerView.notifyDataSetChanged();
                    accessoryAddAlert.dismiss();
                }
            }
        });


    }

    private void alertAddAccessory() {
        addaccessory_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessoryAddAlert.show();
            }
        });
    }

    private void selectDate() {
        date_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(), "date picker");

            }
        });
    }

    private void searchCategory() {
        search_category_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessory_add.this, "Search...",
                        "What are you looking for...?", null, createAccessoriesCategoryData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                alertCategory_textView.setText(item.getTitle());
                                alertCategory_textView.setTextColor(getResources().getColor(R.color.textBlue));

                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void alertAddCategoryAdd() {
        alertAddCategoryEnter_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate_category()) {

                    String s = alertAddCategoryName_editText.getText().toString();
                    alertCategory_textView.setText(s);
                    accesoriesCategoriesList.add(s);
                    alertCategory_textView.setTextColor(getResources().getColor(R.color.textBlue));
                    categoryAddAlert.dismiss();
                }

            }
        });
    }

    private boolean validate_category() {
        boolean valid = true;

        String s = alertAddCategoryName_editText.getText().toString();
        if (alertAddCategoryName_editText.getText().toString().equals("")) {
            alertAddCategoryName_editText.setError("Enter category name");
            valid = false;
        }

        if (accesoriesCategoriesList.contains(s)) {
            alertAddCategoryName_editText.setError("Category already exists");
            valid = false;
        }

        return valid;

    }

    private void alertAddCategoryCancel() {
        alertAddCategoryCancel_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryAddAlert.dismiss();
            }
        });
    }

    private void addCategoryAlert() {
        alertCategoryAdd_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryAddAlert.setCancelable(false);
                categoryAddAlert.show();
            }
        });
    }

    private void selectVendor() {
        search_vendor_cardview.setOnClickListener(new View.OnClickListener() {
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
                                searchForVendor_textView.setTextColor(getResources().getColor(R.color.textBlue));
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void addVendor() {
        vendor_add_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorAddAlert.show();
//                Intent intent = new Intent(Accessory_add.this, RBS_accesories_vendor_detail.class);
//                startActivityForResult(intent, VENDOR_ACTIVITY_REQUEST_CODE);
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

                //TODO "key_id_returnString" ye string use nhi howi
                String key_id_returnString = data.getStringExtra("AC_key_id");
                String phone_no_returnString = data.getStringExtra("AC_phone_no");
                // set text view with string

                searchForVendor_textView.setText(title_returnString + "\n(" + phone_no_returnString + ")");
//                customerDetails.setVisibility(View.VISIBLE);

                searchForVendor_textView.setTextColor(getResources().getColor(R.color.textBlue));
            }
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date_textView.setText(currentDateString);
    }

    public void onCountryPickerClick(View view) {
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
                selected_country_code = ccp.getFullNumberWithPlus();
            }
        });

    }
}
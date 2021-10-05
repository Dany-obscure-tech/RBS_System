package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterAccessoriesSaleItemsRecyclerView;
import com.dotcom.rbs_system.Classes.AccessoryList;
import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.Classes.InvoiceNumberGenerator;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Accessory_sale extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView invoiceNo_TextView, invoice_date_textView, saleAccessory_textview, submit_textview, alertAddAccessoryEnter_textview, alertSaleAccessoryCancel_textview;

    String keyID,invoiceNo;

    String selected_country_code;

    EditText editTextCarrierNumber;

    CountryCodePicker ccp;

    CardView select_category_cardview, select_itemname_cardview;

    DatabaseReference AccessorySaleInvoicesRef;
    String category_name, currency;
    int container = 100;
    ImageButton back_btn;
    EditText customer_name_editText, alertAccessoryQuantity_editText, alertAccessoryUnitPrice_editText, paid_editText;
    //    Date date;
    TextView select_category_textView, select_itemname_textView, alertAccessoryTotalPrice_textView, category_textView, item_name_textView;
    String select_itemKeyid;
    String select_oldItemQuantity;
    Dialog accessorySaleAlert;
    RecyclerView accessoryItemList_recyclerView1;
    DatabaseReference shopkeeperAccessoriesRef;

    List<String> accessories_categories_raw_list;
    List<String> accessories_item_name_raw_list;
    List<String> accessories_item_quantity_raw_list;
    List<String> accessories_item_unit_price_raw_list;
    List<String> accessories_item_keyID_raw_list;


    List<String> accessories_item_name_list, accessories_item_quantity_list, accessories_item_total_price_list, accessories_item_unit_price_list;
    List<String> accessories_category_list;
    List<String> accessorykeyIdList, accessoryCategoryList, accessoryItemNameList, accessoryQtyList, accessoryUnitPriceList, accessoryTotalPriceList;
    List<String> accessoryOldUnitPriceList;

    List<String> new_item_name_list;
    List<String> new_item_price_list;
    List<String> new_item_qty_list;
    List<String> new_item_keyID_list;
    AdapterAccessoriesSaleItemsRecyclerView adapterAccessoriesSaleItemsRecyclerView;

    private ArrayList<SampleSearchModel> selectCategoryData() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i = 0; i < accessories_categories_raw_list.size(); i++) {
            items.add(new SampleSearchModel(accessories_categories_raw_list.get(i), null, null, null, null, null, null, null));
        }

        return items;
    }

    private ArrayList<SampleSearchModel> selectItemNameData() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i = 0; i < new_item_name_list.size(); i++) {
            items.add(new SampleSearchModel(new_item_name_list.get(i) + "\n" + "Unit price: " + new_item_price_list.get(i) + "\n" + "Quantity: " + new_item_qty_list.get(i), new_item_keyID_list.get(i), new_item_name_list.get(i), new_item_price_list.get(i), new_item_qty_list.get(i), null, null, null));
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
        fetching_categories();
        fetching_item_name();

    }


    /////////////////////////////////////////////////////
    private void Initialization() {

        invoiceNo_TextView = (TextView) findViewById(R.id.invoiceNo_TextView);

        ccp = findViewById(R.id.ccp);
        editTextCarrierNumber = findViewById(R.id.editText_carrierNumber);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);

        AccessorySaleInvoicesRef = FirebaseDatabase.getInstance().getReference("Accessories_Sale_invoices/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        Date date;

        currency = Currency.getInstance().getCurrency();
        shopkeeperAccessoriesRef = FirebaseDatabase.getInstance().getReference("ShopKeeper_accessories/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        accessories_categories_raw_list = new ArrayList<>();
        accessories_item_name_raw_list = new ArrayList<>();
        accessories_item_quantity_raw_list = new ArrayList<>();
        accessories_item_unit_price_raw_list = new ArrayList<>();
        accessories_item_keyID_raw_list = new ArrayList<>();

        accessories_item_name_list = new ArrayList<>();
        accessories_item_quantity_list = new ArrayList<>();
        accessories_item_total_price_list = new ArrayList<>();
        accessories_item_unit_price_list = new ArrayList<>();
        accessories_category_list = new ArrayList<>();

        new_item_name_list = new ArrayList<>();
        new_item_price_list = new ArrayList<>();
        new_item_qty_list = new ArrayList<>();
        new_item_keyID_list = new ArrayList<>();

        accessorykeyIdList = new ArrayList<>();
        accessoryOldUnitPriceList = new ArrayList<>();
        accessoryCategoryList = new ArrayList<>();
        accessoryItemNameList = new ArrayList<>();
        accessoryQtyList = new ArrayList<>();
        accessoryUnitPriceList = new ArrayList<>();
        accessoryTotalPriceList = new ArrayList<>();
        accessorySaleAlert = new Dialog(this);
        accessorySaleAlert.setContentView(R.layout.alert_sale_accesory_item);

        back_btn = (ImageButton) findViewById(R.id.back_btn);
        customer_name_editText = (EditText) findViewById(R.id.customer_name_editText);
        invoice_date_textView = findViewById(R.id.invoice_date_textView);
        submit_textview = findViewById(R.id.submit_textview);
        alertAccessoryQuantity_editText = (EditText) accessorySaleAlert.findViewById(R.id.alertAccessoryQuantity_editText);
        alertAccessoryUnitPrice_editText = (EditText) accessorySaleAlert.findViewById(R.id.alertAccessoryUnitPrice_editText);
        paid_editText = (EditText) findViewById(R.id.paid_editText);
        saleAccessory_textview = findViewById(R.id.saleAccessory_textview);
        alertSaleAccessoryCancel_textview = accessorySaleAlert.findViewById(R.id.alertSaleAccessoryCancel_textview);
        alertAddAccessoryEnter_textview = accessorySaleAlert.findViewById(R.id.alertAddAccessoryEnter_textview);
        select_category_textView = (TextView) accessorySaleAlert.findViewById(R.id.select_category_textView);
        select_category_cardview = accessorySaleAlert.findViewById(R.id.select_category_cardview);
        select_itemname_textView = (TextView) accessorySaleAlert.findViewById(R.id.select_itemname_textView);
        select_itemname_cardview = accessorySaleAlert.findViewById(R.id.select_itemname_cardview);
        alertAccessoryTotalPrice_textView = (TextView) accessorySaleAlert.findViewById(R.id.alertAccessoryTotalPrice_textView);
        category_textView = (TextView) accessorySaleAlert.findViewById(R.id.category_textView);
        item_name_textView = (TextView) accessorySaleAlert.findViewById(R.id.item_name_textView);

        adapterAccessoriesSaleItemsRecyclerView = new AdapterAccessoriesSaleItemsRecyclerView(Accessory_sale.this, accessorykeyIdList, accessoryCategoryList, accessoryItemNameList, accessoryQtyList, accessoryUnitPriceList, accessoryTotalPriceList, accessoryOldUnitPriceList);
        accessoryItemList_recyclerView1 = (RecyclerView) findViewById(R.id.accessoryItemList_recyclerView1);
        accessoryItemList_recyclerView1.setLayoutManager(new GridLayoutManager(Accessory_sale.this, 1));
        accessoryItemList_recyclerView1.setAdapter(adapterAccessoriesSaleItemsRecyclerView);

        date = Calendar.getInstance().getTime();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);
        invoice_date_textView.setText(currentDateString);

        try {
            generatingInvoiceNo();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

/////////////////////////////////////////////////////

    private void generatingInvoiceNo() throws ParseException {
        keyID = AccessorySaleInvoicesRef.push().getKey();
        invoiceNo = new InvoiceNumberGenerator().generateNumber();
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
        addAccessoryToList();
        selectDate();
        detailsSubmit();

    }

    private boolean validate_fields() {
        boolean valid = true;
        if (customer_name_editText.getText().toString().isEmpty()) {
            customer_name_editText.setError("Please enter name");
            valid = false;
        }
        if (!ccp.isValidFullNumber()) {
            editTextCarrierNumber.setError("Please enter valid number");
            valid = false;
        }
        if (invoice_date_textView.getText().toString().equals("Select date")) {
            invoice_date_textView.setError("Please enter date");
            valid = false;
        }

        if (paid_editText.getText().toString().isEmpty()) {
            paid_editText.setError("Please enter price");
            valid = false;

        } else {
            if (Float.parseFloat(paid_editText.getText().toString()) == 0) {
                paid_editText.setError("Please enter valid price");
            } else {
                paid_editText.setText(paid_editText.getText().toString().replaceFirst("^0+(?!$)", ""));
                if (paid_editText.getText().toString().startsWith(".")) {
                    paid_editText.setText("0" + paid_editText.getText().toString());
                }
            }
        }
        return valid;
    }

    private void detailsSubmit() {
        submit_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate_fields()) {

                    AccessorySaleInvoicesRef.child(keyID).child("Invoice_no").setValue(invoiceNo);
                    AccessorySaleInvoicesRef.child(keyID).child("Invoice_date").setValue(invoice_date_textView.getText().toString());
                    AccessorySaleInvoicesRef.child(keyID).child("Customer_name").setValue(customer_name_editText.getText().toString());
                    AccessorySaleInvoicesRef.child(keyID).child("Customer_phNo").setValue(ccp.getFullNumberWithPlus());
                    AccessorySaleInvoicesRef.child(keyID).child("Paid Amount").setValue(paid_editText.getText().toString());

                    for (int i = 0; i < accessoryItemNameList.size(); i++) {
                        AccessorySaleInvoicesRef.child(keyID).child("Accessory_items").child("Item_" + (i + 1)).child("name").setValue(accessoryItemNameList.get(i));
                        AccessorySaleInvoicesRef.child(keyID).child("Accessory_items").child("Item_" + (i + 1)).child("category").setValue(accessoryCategoryList.get(i));
                        AccessorySaleInvoicesRef.child(keyID).child("Accessory_items").child("Item_" + (i + 1)).child("quantity").setValue(accessoryQtyList.get(i));
                        AccessorySaleInvoicesRef.child(keyID).child("Accessory_items").child("Item_" + (i + 1)).child("unit_price").setValue(accessoryUnitPriceList.get(i).replace("£", ""));
                        AccessorySaleInvoicesRef.child(keyID).child("Accessory_items").child("Item_" + (i + 1)).child("total_price").setValue(accessoryTotalPriceList.get(i).replace("£", ""));

                        if (Integer.parseInt(accessoryOldUnitPriceList.get(i)) - Integer.parseInt(accessoryQtyList.get(i)) <= 0) {
                            shopkeeperAccessoriesRef.child(accessoryCategoryList.get(i)).child(accessorykeyIdList.get(i)).child("Quantity").setValue("0");
                        } else {
                            shopkeeperAccessoriesRef.child(accessoryCategoryList.get(i)).child(accessorykeyIdList.get(i)).child("Quantity").setValue(Integer.parseInt(accessoryOldUnitPriceList.get(i)) - Integer.parseInt(accessoryQtyList.get(i)));
                        }

                    }

                    Intent intent = new Intent(Accessory_sale.this,Invoice_preview_accessory_sale.class);
                    intent.putExtra("Date",invoice_date_textView.getText().toString());
                    intent.putExtra("Invoice_Type","ACCESSORIES_SOLD");
                    intent.putExtra("Customer_Name",customer_name_editText.getText().toString());
                    intent.putExtra("Customer_Ph_No",ccp.getFullNumberWithPlus());
                    intent.putExtra("Invoice_No",invoiceNo);
                    intent.putExtra("Amount",paid_editText.getText().toString());
                    AccessoryList.getInstance().setAccessoryNameList(accessoryItemNameList);
                    AccessoryList.getInstance().setAccessoryQuantityList(accessoryQtyList);
                    startActivity(intent);
                    finish();

                }
            }
        });
    }

    private void selectDate() {
        invoice_date_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(), "date picker");

            }
        });
    }

    private void addAccessoryToList() {
        alertAddAccessoryEnter_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateSaleAlertFields()) {
                    accessorykeyIdList.add(select_itemKeyid);
                    accessoryOldUnitPriceList.add(select_oldItemQuantity);
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
        alertAddAccessoryEnter_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateSaleAlertFields()) {
                    accessorySaleAlert.dismiss();
                }
            }
        });
    }

    private boolean validateSaleAlertFields() {
        boolean valid = true;

        if (select_category_textView.getText().toString().equals("Select category")) {
            Toast.makeText(this, "Select category", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (select_itemname_textView.getText().toString().equals("Select item name")) {
            Toast.makeText(this, "Select item", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (alertAccessoryQuantity_editText.getText().toString().isEmpty()) {
            alertAccessoryQuantity_editText.setError("Please enter price");
            valid = false;

        } else {
            if (Float.parseFloat(alertAccessoryQuantity_editText.getText().toString()) == 0) {
                alertAccessoryQuantity_editText.setError("Please enter valid price");
            } else {
                alertAccessoryQuantity_editText.setText(alertAccessoryQuantity_editText.getText().toString().replaceFirst("^0+(?!$)", ""));
                if (alertAccessoryQuantity_editText.getText().toString().startsWith(".")) {
                    alertAccessoryQuantity_editText.setText("0" + alertAccessoryQuantity_editText.getText().toString());
                }
            }
        }

        if (alertAccessoryQuantity_editText.getText().toString().equals("0")) {
            alertAccessoryQuantity_editText.setError("Please enter item quantity");
            valid = false;
        }

        if (alertAccessoryUnitPrice_editText.getText().toString().isEmpty()) {
            alertAccessoryUnitPrice_editText.setError("Please enter price");
            valid = false;

        } else {
            if (Float.parseFloat(alertAccessoryUnitPrice_editText.getText().toString()) == 0) {
                alertAccessoryUnitPrice_editText.setError("Please enter valid price");
            } else {
                alertAccessoryUnitPrice_editText.setText(alertAccessoryUnitPrice_editText.getText().toString().replaceFirst("^0+(?!$)", ""));
                if (alertAccessoryUnitPrice_editText.getText().toString().startsWith(".")) {
                    alertAccessoryUnitPrice_editText.setText("0" + alertAccessoryUnitPrice_editText.getText().toString());
                }
            }
        }


        return valid;
    }

    private void AccessoryTotalPriceCalculation() {

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

    private void select_item_name() {
        select_itemname_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessory_sale.this, "Search...",
                        "What are you looking for...?", null, selectItemNameData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                select_itemKeyid = String.valueOf(item.getId());
                                select_oldItemQuantity = String.valueOf(item.getVal2());

                                select_itemname_textView.setText(item.getName());
                                select_itemname_textView.setTextColor(getResources().getColor(R.color.textBlue));
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void select_sale_category() {
        select_category_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessory_sale.this, "Search...",
                        "What are you looking for...?", null, selectCategoryData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                select_category_textView.setText(item.getTitle());
                                select_category_textView.setTextColor(getResources().getColor(R.color.textBlue));
                                dialog.dismiss();
                                select_itemname_textView.setText("Select item name");
                                select_itemname_textView.setTextColor(getResources().getColor(R.color.textGrey));
                                category_name = item.getTitle();
                                new_item_name_list.clear();
                                new_item_price_list.clear();
                                new_item_qty_list.clear();
                                new_item_keyID_list.clear();

                                set_item_name();
                            }
                        }).show();

            }
        });
    }

    private void set_item_name() {

        for (int i = 0; i < accessories_categories_raw_list.size(); i++) {
            if (String.valueOf(accessories_categories_raw_list.get(i)).equals(category_name)) {
                new_item_name_list.add(accessories_item_name_raw_list.get(i));
                new_item_price_list.add(accessories_item_unit_price_raw_list.get(i));
                new_item_qty_list.add(accessories_item_quantity_raw_list.get(i));
                new_item_keyID_list.add(accessories_item_keyID_raw_list.get(i));
            }
        }
    }

    private void alertCancel() {
        alertSaleAccessoryCancel_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessorySaleAlert.dismiss();
            }
        });
    }

    private void alertSaleAccessory() {
        saleAccessory_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessorySaleAlert.show();

            }
        });

    }

    private void fetching_categories() {
        System.out.println("called");

        shopkeeperAccessoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                        accessories_categories_raw_list.add(dataSnapshot2.child("Category").getValue().toString());
                        accessories_item_name_raw_list.add(dataSnapshot2.child("Name").getValue().toString());
                        accessories_item_quantity_raw_list.add(dataSnapshot2.child("Quantity").getValue().toString());
                        accessories_item_unit_price_raw_list.add(dataSnapshot2.child("Price").getValue().toString());
                        accessories_item_keyID_raw_list.add(dataSnapshot2.getKey().toString());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetching_item_name() {
        shopkeeperAccessoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Accessory_items").getChildren()) {
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
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        invoice_date_textView.setText(currentDateString);
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
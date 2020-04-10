package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterAccessoriesPurchaseRecyclerView;
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

public class Accessories extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{


    Button stock_entry_btn,sale_btn,purchase_btn;
    LinearLayout upperLayout,lowerLayout,purchaseLayout;

    RecyclerView purchase_recyclerView;
    AdapterAccessoriesPurchaseRecyclerView adapterAccessoriesPurchaseRecyclerView;

    DatabaseReference categoryRef,reference,accessoriesCategoryRef,stock_check_ref;
    List<String> categoryList,accessoriesCategoryList;
    Button selectCategory_btn,submit_btn,purchaseSubmit_btn,category_search_btn,selectvoucher_no_btn;
    Button selectCategory_btn2,searchForItem_btn,submit_btn2;
    Button purchaseAddItem_btn;
    RatingBar ratingBar;

    Button select_company_name_btn;

    ImageButton Back_btn;

    List<String> exisitngItemsList,exisitngItemsIDList,exisitngItemsKeyIDList,exisitngItemsCategoryList,existingItemsConditionsList;
    List<String> accesoriesCategoriesList;

    List<String> accessoriesCompanyNameList,accessoriesInvoicenoList,accessoriesItemCategoryList,accessoriesItemNameList,accessoriesItemPriceUnitList,accessoriesItemQuantityList;
    List<String> accessoriesCompanyNameListduplicate;

    List<String> purchaseItemNameList,purchaseCategoryList,purchasePriceUnitList,purchaseQuantityList;

    EditText itemName_editText,itemId_editText,purchase_price_editText;

    Dialog purchaseItemAddAlert;
    Dialog purchaseCategoryAddAlert;

    DatabaseReference existingItemsRef,accesoriesCategories;
    private String itemKeyID;
    private EditText suggest_price_editText, balance_editText,paid_editText;

    String firebaseAuthUID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());


    EditText companyName_editText,paidAmount_editText,invoiceNo_editText;
    TextView purchaseDate_textView;

    Button purchaseAlertCategory_btn,purchaseAlertCategoryAdd_btn,purchaseAlertenter_btn,purchaseAlertCancel_btn,date_btn;

    EditText priceUnit_editText,quantity_editText,alertItemName_editText;

    EditText alertAddCategoryName_editText;
    Button alertAddCategoryEnter_btn,alertAddCategoryCancel_btn;

    private ArrayList<SampleSearchModel> createCategoryData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<accessoriesCategoryList.size();i++){
            items.add(new SampleSearchModel(accessoriesCategoryList.get(i),null,null,null,null,null,null,null));
        }

        return items;
    }

    private ArrayList<SampleSearchModel> accessoriesItemInvoiceData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<accessoriesInvoicenoList.size();i++){
            items.add(new SampleSearchModel(accessoriesInvoicenoList.get(i),null,null,null,null,null,null,null));
        }

        return items;
    }

    private ArrayList<SampleSearchModel> accessoriesCompanyData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<accessoriesCompanyNameListduplicate.size();i++){
            items.add(new SampleSearchModel(accessoriesCompanyNameListduplicate.get(i),null,null,null,null,null,null,null));
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

    private ArrayList<SampleSearchModel> createItemsData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<exisitngItemsList.size();i++){
            items.add(new SampleSearchModel(exisitngItemsList.get(i)+"\n("+exisitngItemsIDList.get(i)+")",exisitngItemsIDList.get(i),exisitngItemsList.get(i),exisitngItemsCategoryList.get(i),existingItemsConditionsList.get(i),null,null,exisitngItemsKeyIDList.get(i)));
        }

        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories);

        initialize();

        layoutToggle();

        getCategoryList();

        getAccessoriesCategoryList();

        getting_stock_check();

        onClickListeners();
    }


    private void initialize() {


        purchaseItemAddAlert = new Dialog(this);
        purchaseItemAddAlert.setContentView(R.layout.alert_purchase_add_item);

        purchaseCategoryAddAlert = new Dialog(this);
        purchaseCategoryAddAlert.setContentView(R.layout.alert_purchase_add_category);

        purchaseAlertCategoryAdd_btn = (Button)purchaseItemAddAlert.findViewById(R.id.purchaseAlertCategoryAdd_btn);
        purchaseAlertCancel_btn = (Button)purchaseItemAddAlert.findViewById(R.id.purchaseAlertCancel_btn);
        purchaseAlertenter_btn = (Button)purchaseItemAddAlert.findViewById(R.id.purchaseAlertenter_btn);

        alertItemName_editText = (EditText) purchaseItemAddAlert.findViewById(R.id.alertItemName_editText);
        purchaseAlertCategory_btn = (Button)purchaseItemAddAlert.findViewById(R.id.purchaseAlertCategory_btn);
        quantity_editText = (EditText) purchaseItemAddAlert.findViewById(R.id.quantity_editText);
        priceUnit_editText = (EditText) purchaseItemAddAlert.findViewById(R.id.priceUnit_editText);

        alertAddCategoryName_editText = (EditText) purchaseCategoryAddAlert.findViewById(R.id.alertAddCategoryName_editText);
        alertAddCategoryEnter_btn  = (Button) purchaseCategoryAddAlert.findViewById(R.id.alertAddCategoryEnter_btn);
        alertAddCategoryCancel_btn = (Button) purchaseCategoryAddAlert.findViewById(R.id.alertAddCategoryCancel_btn);

        purchaseItemNameList = new ArrayList<>();
        purchaseCategoryList = new ArrayList<>();
        purchasePriceUnitList = new ArrayList<>();
        purchaseQuantityList = new ArrayList<>();
        accessoriesCompanyNameList = new ArrayList<>();
        accessoriesCompanyNameListduplicate = new ArrayList<>();
        accessoriesInvoicenoList = new ArrayList<>();
        accessoriesItemCategoryList = new ArrayList<>();
        accessoriesItemNameList = new ArrayList<>();
        accessoriesItemPriceUnitList = new ArrayList<>();
        accessoriesItemQuantityList = new ArrayList<>();




        purchase_recyclerView = (RecyclerView)findViewById(R.id.purchase_recyclerView);
        adapterAccessoriesPurchaseRecyclerView = new AdapterAccessoriesPurchaseRecyclerView(Accessories.this,purchaseItemNameList,purchaseCategoryList,purchasePriceUnitList,purchaseQuantityList);
        purchase_recyclerView.setAdapter(adapterAccessoriesPurchaseRecyclerView);
        purchase_recyclerView.setLayoutManager(new GridLayoutManager(this,1));


        Back_btn=(ImageButton)findViewById(R.id.Back_btn);

        reference = FirebaseDatabase.getInstance().getReference();
        categoryRef = FirebaseDatabase.getInstance().getReference("Categories");
        accessoriesCategoryRef = FirebaseDatabase.getInstance().getReference("Accessories_categories");
        stock_check_ref = FirebaseDatabase.getInstance().getReference("Accessories_purchase_list");
        existingItemsRef = FirebaseDatabase.getInstance().getReference("Accessories");
        accesoriesCategories = FirebaseDatabase.getInstance().getReference("Accessories_categories");


        stock_entry_btn = (Button)findViewById(R.id.stock_entry_btn);
        sale_btn = (Button)findViewById(R.id.sale_btn);
        purchase_btn = (Button)findViewById(R.id.purchase_btn);

        upperLayout = (LinearLayout) findViewById(R.id.upperLayout);
        lowerLayout = (LinearLayout) findViewById(R.id.lowerLayout);
        purchaseLayout = (LinearLayout) findViewById(R.id.purchaseLayout);

        selectCategory_btn = (Button)findViewById(R.id.selectCategory_btn);
        select_company_name_btn = (Button)findViewById(R.id.select_company_name_btn);
        category_search_btn = (Button)findViewById(R.id.category_search_btn);
        selectvoucher_no_btn = (Button)findViewById(R.id.selectvoucher_no_btn);
        selectCategory_btn2 = (Button)findViewById(R.id.selectCategory_btn2);
        searchForItem_btn = (Button)findViewById(R.id.searchForItem_btn);

        suggest_price_editText = (EditText)findViewById(R.id.suggest_price_editText);
        paid_editText = (EditText)findViewById(R.id.paid_editText);
        balance_editText = (EditText)findViewById(R.id.balance_editText);

        itemName_editText = (EditText)findViewById(R.id.itemName_editText);
        itemId_editText = (EditText)findViewById(R.id.itemId_editText);
        purchase_price_editText = (EditText)findViewById(R.id.purchase_price_editText);

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        submit_btn = (Button)findViewById(R.id.submit_btn);
        submit_btn2 = (Button)findViewById(R.id.submit_btn2);
        purchaseSubmit_btn = (Button)findViewById(R.id.purchaseSubmit_btn);

        purchaseAddItem_btn = (Button)findViewById(R.id.purchaseAddItem_btn);


        categoryList = new ArrayList<>();
        exisitngItemsList = new ArrayList<>();
        exisitngItemsIDList = new ArrayList<>();
        exisitngItemsKeyIDList = new ArrayList<>();
        exisitngItemsCategoryList = new ArrayList<>();
        existingItemsConditionsList = new ArrayList<>();
        accessoriesCategoryList = new ArrayList<>();
        accesoriesCategoriesList = new ArrayList<>();


        companyName_editText  = (EditText)findViewById(R.id.companyName_editText);
        paidAmount_editText = (EditText)findViewById(R.id.paidAmount_editText);
        invoiceNo_editText = (EditText)findViewById(R.id.invoiceNo_editText);
        purchaseDate_textView = (TextView) findViewById(R.id.purchaseDate_textView);

        date_btn = (Button) findViewById(R.id.date_btn);


    }

    private void getting_stock_check() {
        stock_check_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    DataSnapshot itemsdatasnapshot= snapshot.child("Items");
                    for (DataSnapshot snapshot1:itemsdatasnapshot.getChildren()){
                        accessoriesCompanyNameList.add(String.valueOf(snapshot.child("Company_name").getValue()));
                        accessoriesInvoicenoList.add(String.valueOf(snapshot.child("Invoice_no").getValue()));
                        accessoriesItemCategoryList.add(String.valueOf(snapshot1.child("Item_category").getValue())) ;
                        accessoriesItemNameList.add(String.valueOf(snapshot1.child("Item_name").getValue())) ;
                        accessoriesItemPriceUnitList.add(String.valueOf(snapshot1.child("Item_price_unit").getValue()));
                        accessoriesItemQuantityList.add(String.valueOf(snapshot1.child("Item_quantity").getValue())) ;
                    }
                }
                String value=null;
                Boolean check=true;
                for (int j=0;j<accessoriesCompanyNameList.size();j++){
//                    check=true;
                    if (accessoriesCompanyNameListduplicate.size()==0){
                        value=accessoriesCompanyNameList.get(j);
                        accessoriesCompanyNameListduplicate.add(value);
                    }
                    else {
                        value=accessoriesCompanyNameList.get(j);
                        for (int i=0;i<=accessoriesCompanyNameListduplicate.size();i++){
                            if (i<accessoriesCompanyNameListduplicate.size()){
                                if (accessoriesCompanyNameListduplicate.get(i).equals(value)){

                                    check=false;
                                }
                            }
                            else  if (i==accessoriesCompanyNameListduplicate.size()){
                                Toast.makeText(Accessories.this, value, Toast.LENGTH_SHORT).show();

                                if (check==true){
                                    accessoriesCompanyNameListduplicate.add("hi");
                                }
                            }

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getAccessoriesCategoryList() {
        accessoriesCategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    accessoriesCategoryList.add(String.valueOf(dataSnapshot1.getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getCategoryList() {
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    categoryList.add(String.valueOf(dataSnapshot1.getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        accessoriesCategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i =0;
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    accesoriesCategoriesList.add(String.valueOf(dataSnapshot1.getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void detailsSubmit() {
        final String key = reference.push().getKey();
        reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("Category").setValue(selectCategory_btn.getText().toString());
        reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("Item_id").setValue(itemId_editText.getText().toString());
        reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("added_by").setValue(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("Item_name").setValue(itemName_editText.getText().toString());
        reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("Condition").setValue(ratingBar.getRating());
        reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("key_id").setValue(key);



        finish();

    }

    private void detailsSubmit2() {

        String key = reference.push().getKey();

        reference.child("Accessories_sale").child(key).child("Item_keyID").setValue(itemKeyID);
        reference.child("Accessories_sale").child(key).child("Suggested_price").setValue(suggest_price_editText.getText().toString());
        reference.child("Accessories_sale").child(key).child("Balance").setValue(balance_editText.getText().toString());
        reference.child("Accessories_sale").child(key).child("Paid").setValue(paid_editText.getText().toString());
        reference.child("Accessories_sale").child(key).child("key_id").setValue(key);
        reference.child("Accessories_sale").child(key).child("added_by").setValue(firebaseAuthUID);


        finish();
    }

    private void detailsSubmitPurchaseAlert() {

        purchaseItemNameList.add(alertItemName_editText.getText().toString());
        purchaseCategoryList.add(purchaseAlertCategory_btn.getText().toString());
        purchasePriceUnitList.add(priceUnit_editText.getText().toString());
        purchaseQuantityList.add(quantity_editText.getText().toString());
        adapterAccessoriesPurchaseRecyclerView.notifyDataSetChanged();
        purchaseItemAddAlert.dismiss();

    }

    private void detailsSubmitPurchase() {

        String key = reference.push().getKey();
        reference.child("Accessories_purchase_list").child(key).child("Company_name").setValue(companyName_editText.getText().toString());
        reference.child("Accessories_purchase_list").child(key).child("Invoice_date").setValue(purchaseDate_textView.getText().toString());
        reference.child("Accessories_purchase_list").child(key).child("Paid_amount").setValue(paidAmount_editText.getText().toString());
        reference.child("Accessories_purchase_list").child(key).child("Invoice_no").setValue(invoiceNo_editText.getText().toString());

        for (int i = 0;i<purchaseItemNameList.size();i++){
            reference.child("Accessories_purchase_list").child(key).child("Items").child("Item_"+String.valueOf(i+1)).child("Item_name").setValue(purchaseItemNameList.get(i));
            reference.child("Accessories_purchase_list").child(key).child("Items").child("Item_"+String.valueOf(i+1)).child("Item_category").setValue(purchaseCategoryList.get(i));
            reference.child("Accessories_purchase_list").child(key).child("Items").child("Item_"+String.valueOf(i+1)).child("Item_price_unit").setValue(purchasePriceUnitList.get(i));
            reference.child("Accessories_purchase_list").child(key).child("Items").child("Item_"+String.valueOf(i+1)).child("Item_quantity").setValue(purchaseQuantityList.get(i));
        }

        reference.child("Accessories_purchase_list").child(key).child("added_by").setValue(firebaseAuthUID);

        finish();

    }

    private void layoutToggle() {
        stock_entry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseLayout.setVisibility(View.GONE);
                upperLayout.setVisibility(View.VISIBLE);
                lowerLayout.setVisibility(View.GONE);

            }
        });

        sale_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseLayout.setVisibility(View.GONE);
                upperLayout.setVisibility(View.GONE);
                lowerLayout.setVisibility(View.VISIBLE);

            }
        });

        purchase_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseLayout.setVisibility(View.VISIBLE);
                upperLayout.setVisibility(View.GONE);
                lowerLayout.setVisibility(View.GONE);
            }
        });

    }

    private void fetchingExisitingItems(String category) {

        exisitngItemsList.clear();
        exisitngItemsIDList.clear();
        exisitngItemsCategoryList.clear();
        existingItemsConditionsList.clear();
        exisitngItemsKeyIDList.clear();

        existingItemsRef.child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        exisitngItemsList.add(String.valueOf(dataSnapshot1.child("Item_name").getValue()));
                        exisitngItemsIDList.add(String.valueOf(dataSnapshot1.child("Item_id").getValue()));
                        exisitngItemsCategoryList.add(String.valueOf(dataSnapshot1.child("Category").getValue()));
                        existingItemsConditionsList.add(String.valueOf(dataSnapshot1.child("Condition").getValue()));
                        exisitngItemsKeyIDList.add(String.valueOf(dataSnapshot1.child("key_id").getValue()));
                }

                searchForItem_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SimpleSearchDialogCompat(Accessories.this, "Search...",
                                "What are you looking for...?", null, createItemsData(),
                                new SearchResultListener<SampleSearchModel>() {
                                    @Override
                                    public void onSelected(BaseSearchDialogCompat dialog,
                                                           SampleSearchModel item, int position) {
                                        searchForItem_btn.setText(item.getTitle());
                                        searchForItem_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                        searchForItem_btn.setTextColor(getResources().getColor(R.color.textGrey));

                                        itemKeyID = item.getVal5();

                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void onClickListeners() {

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

        purchaseAlertCategory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessories.this, "Search...",
                        "What are you looking for...?", null, createAccessoriesCategoryData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                purchaseAlertCategory_btn.setText(item.getTitle());
                                purchaseAlertCategory_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                purchaseAlertCategory_btn.setTextColor(getResources().getColor(R.color.textGrey));

                                dialog.dismiss();
                            }
                        }).show();
            }
        });


        selectCategory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessories.this, "Search...",
                        "What are you looking for...?", null, createCategoryData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                selectCategory_btn.setText(item.getTitle());
                                selectCategory_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                selectCategory_btn.setTextColor(getResources().getColor(R.color.textGrey));

                                dialog.dismiss();
                            }
                        }).show();
                // hello
            }
        });

        selectCategory_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessories.this, "Search...",
                        "What are you looking for...?", null, createCategoryData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                selectCategory_btn2.setText(item.getTitle());
                                selectCategory_btn2.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                selectCategory_btn2.setTextColor(getResources().getColor(R.color.textGrey));

                                fetchingExisitingItems(item.getTitle());

                                dialog.dismiss();
                            }
                        }).show();
                // hello
            }
        });
        category_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessories.this, "Search...",
                        "What are you looking for...?", null, createCategoryData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                category_search_btn.setText(item.getTitle());
                                category_search_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                category_search_btn.setTextColor(getResources().getColor(R.color.textGrey));

                                fetchingExisitingItems(item.getTitle());

                                dialog.dismiss();
                            }
                        }).show();
                // hello

            }
        });
        selectvoucher_no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessories.this, "Search...",
                        "What are you looking for...?", null, accessoriesItemInvoiceData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                selectvoucher_no_btn.setText(item.getTitle());
                                selectvoucher_no_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                selectvoucher_no_btn.setTextColor(getResources().getColor(R.color.textGrey));

                                fetchingExisitingItems(item.getTitle());

                                dialog.dismiss();
                            }
                        }).show();
                // hello
            }
        });

        select_company_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessories.this, "Search...",
                        "What are you looking for...?", null, accessoriesCompanyData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                selectvoucher_no_btn.setText(item.getTitle());
                                selectvoucher_no_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                selectvoucher_no_btn.setTextColor(getResources().getColor(R.color.textGrey));

                                fetchingExisitingItems(item.getTitle());

                                dialog.dismiss();
                            }
                        }).show();
                // hello
            }
        });


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields() == true)
                    detailsSubmit();
            }
        });

        submit_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields2() == true)
                    detailsSubmit2();
            }
        });

        purchaseAddItem_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                purchaseItemAddAlert.setCancelable(false);
                purchaseItemAddAlert.show();
            }
        });


        purchaseAlertCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseItemAddAlert.dismiss();
            }
        });

        purchaseAlertenter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePurchaseAlertFields() == true)
                    detailsSubmitPurchaseAlert();
            }
        });

        purchaseSubmit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePurchaseFields() == true)
                    detailsSubmitPurchase();
            }
        });

        purchaseAlertCategoryAdd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseCategoryAddAlert.setCancelable(false);
                purchaseCategoryAddAlert.show();
            }
        });

        alertAddCategoryEnter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = alertAddCategoryName_editText.getText().toString();
                Boolean valid = true;
                for (int i = 0;i<accesoriesCategoriesList.size();i++){

                    if (s.equals(accesoriesCategoriesList.get(i))){
                        alertAddCategoryName_editText.setError("Category already exists");
                        valid = false;
                    }
                }
                if (valid==true){
                    String key = accessoriesCategoryRef.push().getKey();
                    accessoriesCategoryRef.child(key).setValue(s);
                    finish();
                }
            }
        });

        alertAddCategoryCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseCategoryAddAlert.dismiss();
            }
        });


    }

    private boolean validateFields() {
        boolean valid = true;

        if (selectCategory_btn.getText().toString().equals("Select Category")) {
            Toast.makeText(this, "Please select category", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (itemName_editText.getText().toString().isEmpty()) {
            itemName_editText.setError("Please enter item name");
            valid = false;
        }

        if (itemId_editText.getText().toString().isEmpty()) {
            itemId_editText.setError("Please enter item id");
            valid = false;
        }

        if (purchase_price_editText.getText().toString().isEmpty()) {
            purchase_price_editText.setError("Please enter item id");
            valid = false;
        }

        if (ratingBar.getRating()==0){
            Toast.makeText(this, "Please select rating", Toast.LENGTH_SHORT).show();
            valid=false;
        }


        return valid;
    }

    private boolean validateFields2() {
        boolean valid = true;

        if (selectCategory_btn2.getText().toString().equals("Select Category")) {
            Toast.makeText(this, "Please select category", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (searchForItem_btn.getText().toString().equals("Search for item")) {
            Toast.makeText(this, "Please select item", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (suggest_price_editText.getText().toString().isEmpty()) {
            suggest_price_editText.setError("Please enter item name");
            valid = false;
        }

        if (paid_editText.getText().toString().isEmpty()) {
            paid_editText.setError("Please enter item id");
            valid = false;
        }

        if (balance_editText.getText().toString().isEmpty()) {
            balance_editText.setError("Please enter item id");
            valid = false;
        }
        return valid;
    }

    private boolean validatePurchaseAlertFields() {
        boolean valid = true;

        if (purchaseAlertCategory_btn.getText().toString().equals("Select Category")) {
            Toast.makeText(this, "Please select category", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (alertItemName_editText.getText().toString().isEmpty()) {
            alertItemName_editText.setError("Please enter item name");
            valid = false;
        }
        if (priceUnit_editText.getText().toString().isEmpty()) {
            priceUnit_editText.setError("Please enter price unit");
            valid = false;
        }
        if (quantity_editText.getText().toString().isEmpty()) {
            quantity_editText.setError("Please enter quantity");
            valid = false;
        }

        return valid;
    }

    private boolean validatePurchaseFields() {
        Boolean valid = true;

        if (purchaseDate_textView.getText().toString().equals("Select date")) {
            Toast.makeText(this, "Select date", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (companyName_editText.getText().toString().isEmpty()) {
            companyName_editText.setError("Please enter company name");
            valid = false;
        }
        if (paidAmount_editText.getText().toString().isEmpty()) {
            paidAmount_editText.setError("Please enter paid amount");
            valid = false;
        }
        if (invoiceNo_editText.getText().toString().isEmpty()) {
            invoiceNo_editText.setError("Please enter invoice no");
            valid = false;
        }
        if (purchaseItemNameList.size()==0){
            Toast.makeText(this, "Please enter Items", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        purchaseDate_textView.setText(currentDateString);
    }
}

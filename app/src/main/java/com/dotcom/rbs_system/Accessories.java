package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterAccessoriesItemDetailRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterAccessoriesItemDetailRecyclerView1;
import com.dotcom.rbs_system.Adapter.AdapterAccessoriesItemDetailRecyclerView2;
import com.dotcom.rbs_system.Adapter.AdapterAccessoriesPurchaseRecyclerView;
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
import java.util.LinkedHashSet;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class
Accessories extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    ImageButton stockEntrysms_btn,stockEntrygmail_btn,stockEntryprint_btn;
    ImageButton salePrint_btn,saleEmail_btn,saleSms_btn;
    ImageButton purchasePrint_btn,purchaseEmail_btn,purchaseSms_btn;
    ImageButton stockCheckPrint_btn,stockCheckEmail_btn,stockCheckSms_btn;
    ImageButton gmail_btn,sms_btn,print_btn;

    //switch of sending dialog
    int switch_container=0;
    int switch_container_second=0;
    //switch of sending dialog

    Date date;

    Button btn_done;
    Button category_search_btn_submit_btn,select_Invoice_no_btn_submit_btn,select_company_name_btn_submit_btn;

    // Progress dialog
    Progress_dialoge pd;
    Dialog sendingdialog;

    Button stock_entry_btn,sale_btn,purchase_btn,Stock_check_btn;
    LinearLayout upperLayout,lowerLayout,purchaseLayout,stock_check_Layout;

    RecyclerView purchase_recyclerView,item_details_recyclerview,item_details_recyclerview1,item_details_recyclerview2;
    AdapterAccessoriesPurchaseRecyclerView adapterAccessoriesPurchaseRecyclerView;
    AdapterAccessoriesItemDetailRecyclerView adapterAccessoriesItemDetailRecyclerView;
    AdapterAccessoriesItemDetailRecyclerView1 adapterAccessoriesItemDetailRecyclerView1;
    AdapterAccessoriesItemDetailRecyclerView2 adapterAccessoriesItemDetailRecyclerView2;

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
    List<String> accessoriesCompanyNameListfilter,accessoriesInvoicenoListfilter,accessoriesItemCategoryListfilter;

    List<String> accessoriesCompanyNameListsearch,accessoriesInvoicenoListsearch,accessoriesItemCategoryListsearch,accessoriesItemNameListsearch,accessoriesItemPriceUnitListsearch,accessoriesItemQuantityListsearch;
    List<String> accessoriesCompanyNameListsearch1,accessoriesInvoicenoListsearch1,accessoriesItemCategoryListsearch1,accessoriesItemNameListsearch1,accessoriesItemPriceUnitListsearch1,accessoriesItemQuantityListsearch1;
    List<String> accessoriesCompanyNameListsearch2,accessoriesInvoicenoListsearch2,accessoriesItemCategoryListsearch2,accessoriesItemNameListsearch2,accessoriesItemPriceUnitListsearch2,accessoriesItemQuantityListsearch2;

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
        for (int i=0;i<accessoriesItemCategoryListfilter.size();i++){
            items.add(new SampleSearchModel(accessoriesItemCategoryListfilter.get(i),null,null,null,null,null,null,null));
        }

        return items;
    }

    private ArrayList<SampleSearchModel> accessoriesItemInvoiceData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<accessoriesInvoicenoListfilter.size();i++){
            items.add(new SampleSearchModel(accessoriesInvoicenoListfilter.get(i),null,null,null,null,null,null,null));
        }

        return items;
    }

    private ArrayList<SampleSearchModel> accessoriesCompanyData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i = 0; i< accessoriesCompanyNameListfilter.size(); i++){
            items.add(new SampleSearchModel(accessoriesCompanyNameListfilter.get(i),null,null,null,null,null,null,null));
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

        pd = new Progress_dialoge();
        sendingdialog = new Dialog(this);
        sendingdialog.setContentView(R.layout.dialoge_items);
        gmail_btn = (ImageButton) sendingdialog.findViewById(R.id.gmail_btn);
        print_btn = (ImageButton) sendingdialog.findViewById(R.id.print_btn);
        sms_btn = (ImageButton) sendingdialog.findViewById(R.id.sms_btn);
        btn_done = (Button) sendingdialog.findViewById(R.id.btn_done);

        category_search_btn_submit_btn=(Button)findViewById(R.id.category_search_btn_submit_btn);
        select_Invoice_no_btn_submit_btn=(Button)findViewById(R.id.select_Invoice_no_btn_submit_btn);
        select_company_name_btn_submit_btn=(Button)findViewById(R.id.select_company_name_btn_submit_btn);

        purchaseItemAddAlert = new Dialog(this);
        purchaseItemAddAlert.setContentView(R.layout.alert_purchase_add_item);

        purchaseCategoryAddAlert = new Dialog(this);
        purchaseCategoryAddAlert.setContentView(R.layout.alert_add_category);

        purchaseAlertCategoryAdd_btn = (Button)purchaseItemAddAlert.findViewById(R.id.alertCategoryAdd_btn);
        purchaseAlertCancel_btn = (Button)purchaseItemAddAlert.findViewById(R.id.purchaseAlertCancel_btn);
        purchaseAlertenter_btn = (Button)purchaseItemAddAlert.findViewById(R.id.purchaseAlertenter_btn);

        alertItemName_editText = (EditText) purchaseItemAddAlert.findViewById(R.id.alertItemName_editText);
        purchaseAlertCategory_btn = (Button)purchaseItemAddAlert.findViewById(R.id.alertCategory_btn);
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
        accessoriesInvoicenoList = new ArrayList<>();
        accessoriesItemCategoryList = new ArrayList<>();
        accessoriesItemNameList = new ArrayList<>();
        accessoriesItemPriceUnitList = new ArrayList<>();
        accessoriesItemQuantityList = new ArrayList<>();

        accessoriesCompanyNameListsearch = new ArrayList<>();
        accessoriesInvoicenoListsearch = new ArrayList<>();
        accessoriesItemCategoryListsearch = new ArrayList<>();
        accessoriesItemNameListsearch = new ArrayList<>();
        accessoriesItemPriceUnitListsearch = new ArrayList<>();
        accessoriesItemQuantityListsearch = new ArrayList<>();

        accessoriesCompanyNameListsearch1 = new ArrayList<>();
        accessoriesInvoicenoListsearch1 = new ArrayList<>();
        accessoriesItemCategoryListsearch1 = new ArrayList<>();
        accessoriesItemNameListsearch1 = new ArrayList<>();
        accessoriesItemPriceUnitListsearch1 = new ArrayList<>();
        accessoriesItemQuantityListsearch1 = new ArrayList<>();

        accessoriesCompanyNameListsearch2 = new ArrayList<>();
        accessoriesInvoicenoListsearch2 = new ArrayList<>();
        accessoriesItemCategoryListsearch2 = new ArrayList<>();
        accessoriesItemNameListsearch2 = new ArrayList<>();
        accessoriesItemPriceUnitListsearch2 = new ArrayList<>();
        accessoriesItemQuantityListsearch2 = new ArrayList<>();

        purchase_recyclerView = (RecyclerView)findViewById(R.id.purchase_recyclerView);
        item_details_recyclerview = (RecyclerView)findViewById(R.id.item_details_recyclerview);
        item_details_recyclerview1 = (RecyclerView)findViewById(R.id.item_details_recyclerview1);
        item_details_recyclerview2 = (RecyclerView)findViewById(R.id.item_details_recyclerview2);
        adapterAccessoriesPurchaseRecyclerView = new AdapterAccessoriesPurchaseRecyclerView(Accessories.this,purchaseItemNameList,purchaseCategoryList,purchasePriceUnitList,purchaseQuantityList);
        adapterAccessoriesItemDetailRecyclerView = new AdapterAccessoriesItemDetailRecyclerView(Accessories.this,accessoriesItemCategoryListsearch,accessoriesItemNameListsearch,accessoriesItemPriceUnitListsearch,accessoriesItemQuantityListsearch);
        adapterAccessoriesItemDetailRecyclerView1 = new AdapterAccessoriesItemDetailRecyclerView1(Accessories.this,accessoriesItemCategoryListsearch1,accessoriesItemNameListsearch1,accessoriesItemPriceUnitListsearch1,accessoriesItemQuantityListsearch1);
        adapterAccessoriesItemDetailRecyclerView2 = new AdapterAccessoriesItemDetailRecyclerView2(Accessories.this,accessoriesItemCategoryListsearch2,accessoriesItemNameListsearch2,accessoriesItemPriceUnitListsearch2,accessoriesItemQuantityListsearch2);

        purchase_recyclerView.setAdapter(adapterAccessoriesPurchaseRecyclerView);
        item_details_recyclerview.setAdapter(adapterAccessoriesItemDetailRecyclerView);
        item_details_recyclerview1.setAdapter(adapterAccessoriesItemDetailRecyclerView1);
        item_details_recyclerview2.setAdapter(adapterAccessoriesItemDetailRecyclerView2);

        purchase_recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        item_details_recyclerview.setLayoutManager(new GridLayoutManager(this,1));
        item_details_recyclerview1.setLayoutManager(new GridLayoutManager(this,1));
        item_details_recyclerview2.setLayoutManager(new GridLayoutManager(this,1));

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
        Stock_check_btn = (Button)findViewById(R.id.Stock_check_btn);

        upperLayout = (LinearLayout) findViewById(R.id.upperLayout);
        lowerLayout = (LinearLayout) findViewById(R.id.lowerLayout);
        purchaseLayout = (LinearLayout) findViewById(R.id.purchaseLayout);
        stock_check_Layout = (LinearLayout) findViewById(R.id.stock_check_Layout);

        selectCategory_btn = (Button)findViewById(R.id.selectCategory_btn);
        select_company_name_btn = (Button)findViewById(R.id.select_company_name_btn);
        category_search_btn = (Button)findViewById(R.id.category_search_btn);
        selectvoucher_no_btn = (Button)findViewById(R.id.select_Invoice_no_btn);
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
        purchaseDate_textView = (TextView) findViewById(R.id.date_textView);

        date_btn = (Button) findViewById(R.id.date_btn);

        date=Calendar.getInstance().getTime();
        String currentDateString= DateFormat.getDateInstance(DateFormat.FULL).format(date);
        purchaseDate_textView.setText(currentDateString);

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = (Date)formatter.parse(date.getDay()+"-"+date.getMonth()+"-"+date.getYear());
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void getting_stock_check() {
        pd.showProgressBar(Accessories.this);
        stock_check_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                accessoriesCompanyNameList.clear();
                accessoriesInvoicenoList.clear();
                accessoriesItemCategoryList.clear();
                accessoriesItemNameList.clear();
                accessoriesItemPriceUnitList.clear();
                accessoriesItemQuantityList.clear();

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
                accessoriesItemCategoryListfilter = new ArrayList<String>(new LinkedHashSet<String>(accessoriesItemCategoryList));
                accessoriesInvoicenoListfilter = new ArrayList<String>(new LinkedHashSet<String>(accessoriesInvoicenoList));
                accessoriesCompanyNameListfilter = new ArrayList<String>(new LinkedHashSet<String>(accessoriesCompanyNameList));
                pd.dismissProgressBar(Accessories.this);

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
        switch_container=1;
        pd.showProgressBar(Accessories.this);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Accessories.this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            final String key = reference.push().getKey();
            reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("Category").setValue(selectCategory_btn.getText().toString());
            reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("Item_id").setValue(itemId_editText.getText().toString());
            reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("added_by").setValue(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
            reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("Item_name").setValue(itemName_editText.getText().toString());
            reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("Condition").setValue(ratingBar.getRating());
            reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("key_id").setValue(key);

            pd.dismissProgressBar(Accessories.this);
            sendingdialog.show();
        }
        else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
        }


    }

    private void detailsSubmit2() {
        switch_container=2;
        pd.showProgressBar(Accessories.this);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Accessories.this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            String key = reference.push().getKey();

            reference.child("Accessories_sale").child(key).child("Item_keyID").setValue(itemKeyID);
            reference.child("Accessories_sale").child(key).child("Suggested_price").setValue(suggest_price_editText.getText().toString());
            reference.child("Accessories_sale").child(key).child("Balance").setValue(balance_editText.getText().toString());
            reference.child("Accessories_sale").child(key).child("Paid").setValue(paid_editText.getText().toString());
            reference.child("Accessories_sale").child(key).child("key_id").setValue(key);
            reference.child("Accessories_sale").child(key).child("added_by").setValue(firebaseAuthUID);
            pd.dismissProgressBar(Accessories.this);
            sendingdialog.show();

        }
        else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
        }


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
        switch_container=3;

        pd.showProgressBar(Accessories.this);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Accessories.this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
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
            pd.dismissProgressBar(Accessories.this);
            sendingdialog.show();
        }
        else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
        }



    }

    private void layoutToggle() {
        stock_entry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseLayout.setVisibility(View.GONE);
                upperLayout.setVisibility(View.VISIBLE);
                lowerLayout.setVisibility(View.GONE);
                stock_check_Layout.setVisibility(View.GONE);

            }
        });

        sale_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseLayout.setVisibility(View.GONE);
                upperLayout.setVisibility(View.GONE);
                lowerLayout.setVisibility(View.VISIBLE);
                stock_check_Layout.setVisibility(View.GONE);

            }
        });

        purchase_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseLayout.setVisibility(View.VISIBLE);
                upperLayout.setVisibility(View.GONE);
                lowerLayout.setVisibility(View.GONE);
                stock_check_Layout.setVisibility(View.GONE);
            }
        });
        Stock_check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stock_check_Layout.setVisibility(View.VISIBLE);
                purchaseLayout.setVisibility(View.GONE);
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

        category_search_btn_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_container=4;
                switch_container_second=1;
                sendingdialog.show();

            }
        });

        select_Invoice_no_btn_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_container=4;
                switch_container_second=2;
            }
        });

        select_company_name_btn_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_container=4;
                switch_container_second=3;
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

                                searchForItem_btn.setText("Search for item");
                                searchForItem_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                                searchForItem_btn.setTextColor(getResources().getColor(R.color.colorGrey));

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
                item_details_recyclerview.setVisibility(View.VISIBLE);
                item_details_recyclerview1.setVisibility(View.GONE);
                item_details_recyclerview2.setVisibility(View.GONE);
                new SimpleSearchDialogCompat(Accessories.this, "Search...",
                        "What are you looking for...?", null, createCategoryData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                category_search_btn.setText(item.getTitle());
                                category_search_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                category_search_btn.setTextColor(getResources().getColor(R.color.textGrey));
                                accessoriesItemCategoryListsearch.clear();
                                accessoriesInvoicenoListsearch.clear();
                                accessoriesCompanyNameListsearch.clear();
                                accessoriesItemNameListsearch.clear();
                                accessoriesItemPriceUnitListsearch.clear();
                                accessoriesItemQuantityListsearch.clear();

                                fetchingExisitingItems(item.getTitle());
                                dialog.dismiss();
                                for (int k=0;k<accessoriesItemCategoryList.size();k++){
                                    if (item.getTitle().equals(accessoriesItemCategoryList.get(k))){
                                        accessoriesItemCategoryListsearch.add(accessoriesItemCategoryList.get(k));
                                        accessoriesInvoicenoListsearch.add(accessoriesInvoicenoList.get(k));
                                        accessoriesCompanyNameListsearch.add(accessoriesCompanyNameList.get(k));
                                        accessoriesItemNameListsearch.add(accessoriesItemNameList.get(k));
                                        accessoriesItemPriceUnitListsearch.add(accessoriesItemPriceUnitList.get(k));
                                        accessoriesItemQuantityListsearch.add(accessoriesItemQuantityList.get(k));
                                        adapterAccessoriesItemDetailRecyclerView.notifyDataSetChanged();
                                    }
                                }

                            }
                        }).show();


            }
        });
        selectvoucher_no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_details_recyclerview.setVisibility(View.GONE);
                item_details_recyclerview1.setVisibility(View.VISIBLE);
                item_details_recyclerview2.setVisibility(View.GONE);
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
                                accessoriesItemCategoryListsearch1.clear();
                                accessoriesInvoicenoListsearch1.clear();
                                accessoriesCompanyNameListsearch1.clear();
                                accessoriesItemNameListsearch1.clear();
                                accessoriesItemPriceUnitListsearch1.clear();
                                accessoriesItemQuantityListsearch1.clear();

                                for (int k=0;k<accessoriesInvoicenoList.size();k++){
                                    if (item.getTitle().equals(accessoriesInvoicenoList.get(k))){
                                        accessoriesItemCategoryListsearch1.add(accessoriesItemCategoryList.get(k));
                                        accessoriesInvoicenoListsearch1.add(accessoriesInvoicenoList.get(k));
                                        accessoriesCompanyNameListsearch1.add(accessoriesCompanyNameList.get(k));
                                        accessoriesItemNameListsearch1.add(accessoriesItemNameList.get(k));
                                        accessoriesItemPriceUnitListsearch1.add(accessoriesItemPriceUnitList.get(k));
                                        accessoriesItemQuantityListsearch1.add(accessoriesItemQuantityList.get(k));
                                        adapterAccessoriesItemDetailRecyclerView1.notifyDataSetChanged();
//                                        Toast.makeText(Accessories.this, String.valueOf(accessoriesItemCategoryListsearch.size()), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        }).show();
                // hello
            }
        });

        select_company_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_details_recyclerview.setVisibility(View.GONE);
                item_details_recyclerview1.setVisibility(View.GONE);
                item_details_recyclerview2.setVisibility(View.VISIBLE);
                new SimpleSearchDialogCompat(Accessories.this, "Search...",
                        "What are you looking for...?", null, accessoriesCompanyData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                select_company_name_btn.setText(item.getTitle());
                                select_company_name_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                select_company_name_btn.setTextColor(getResources().getColor(R.color.textGrey));

                                fetchingExisitingItems(item.getTitle());

                                dialog.dismiss();
                                accessoriesItemCategoryListsearch2.clear();
                                accessoriesInvoicenoListsearch2.clear();
                                accessoriesCompanyNameListsearch2.clear();
                                accessoriesItemNameListsearch2.clear();
                                accessoriesItemPriceUnitListsearch2.clear();
                                accessoriesItemQuantityListsearch2.clear();

                                for (int k=0;k<accessoriesCompanyNameList.size();k++){
                                    if (item.getTitle().equals(accessoriesCompanyNameList.get(k))){
                                        accessoriesItemCategoryListsearch2.add(accessoriesItemCategoryList.get(k));
                                        accessoriesInvoicenoListsearch2.add(accessoriesInvoicenoList.get(k));
                                        accessoriesCompanyNameListsearch2.add(accessoriesCompanyNameList.get(k));
                                        accessoriesItemNameListsearch2.add(accessoriesItemNameList.get(k));
                                        accessoriesItemPriceUnitListsearch2.add(accessoriesItemPriceUnitList.get(k));
                                        accessoriesItemQuantityListsearch2.add(accessoriesItemQuantityList.get(k));
                                        adapterAccessoriesItemDetailRecyclerView2.notifyDataSetChanged();
//                                        Toast.makeText(Accessories.this, String.valueOf(accessoriesItemCategoryListsearch.size()), Toast.LENGTH_SHORT).show();
                                    }
                                }
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

        print_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Accessories.this, "YEs working", Toast.LENGTH_SHORT).show();
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
                if (switch_container==1){
                    //Stock Entry
                    intent.putExtra("sms_body", "Category : "+selectCategory_btn.getText().toString()+"\n"+"Item name : "+itemName_editText.getText().toString()+"\n"+"Item Serial-NO/IMEI-NO : "+itemId_editText.getText().toString()+"\n"+"Condition : "+ratingBar.getRating()+"\n"+"Purchased Price : "+purchase_price_editText.getText().toString());
                    startActivity(intent);

                }
                else if (switch_container==2){
                    //Sale
                    intent.putExtra("sms_body", "Category : "+selectCategory_btn2.getText().toString()+"\n"+"Item name : "+searchForItem_btn.getText().toString()+"\n"+"Suggested price : "+suggest_price_editText.getText().toString()+"\n"+"Paid : "+paid_editText.getText().toString()+"\n"+"Balance : "+balance_editText.getText().toString());
                    startActivity(intent);
                }
                else if (switch_container==3){
                    //Purchase
                    intent.putExtra("sms_body", "Company name : "+companyName_editText.getText().toString()+"\n"+"Invoice Date : "+purchaseDate_textView.getText().toString()+"\n"+"Paid Amount : "+paidAmount_editText.getText().toString()+"\n"+"Invoice No : "+invoiceNo_editText.getText().toString()+"\n"+"Purchase Item details"+"\n"+"---------------"+"\n"+"Item name : "+alertItemName_editText.getText().toString()+"\n"+"Item Category : "+alertItemName_editText.getText().toString()+"\n"+"Price unit : "+priceUnit_editText.getText().toString()+"\n"+"Quantity : "+quantity_editText.getText().toString()+"\n"+"---------------");
                    startActivity(intent);
                }
                else{
                    //Stock check
                    intent.putExtra("sms_body", "Hi how are you");
                    startActivity(intent);

                }

            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendingdialog.dismiss();
                finish();
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

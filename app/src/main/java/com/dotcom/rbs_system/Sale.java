package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.Adapter_customerList_alert_dialog;
import com.dotcom.rbs_system.Adapter.Adapter_itemList_alert_dialog;
import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.Classes.Exchanged_itemdata;
import com.dotcom.rbs_system.Classes.RBSCustomerDetails;
import com.dotcom.rbs_system.Classes.RBSItemDetails;
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
    RBSItemDetails rbsItemDetails;
    RBSCustomerDetails rbsCustomerDetails;

    Boolean isScrolling = false, itemDatafullyloaded = false;
    Boolean isCustomerScrolling = false, customerDatafullyloaded = false;

    ProgressBar alert_rbs_itemlist_progressBar,alert_rbs_customerlist_progressBar;

    int currentItems, totalItems, scrollOutItems;
    int currentCustomer, totalCustomer, scrollOutCustomer;

    CardView searchForItem_cardView,searchForCustomer_cardView;

    Dialog itemList_alert_dialog,customerList_alert_dialog;
    RecyclerView itemList_recyclerView,customerList_recyclerView;
    Adapter_itemList_alert_dialog adapter_itemList_alert_dialog;
    Adapter_customerList_alert_dialog adapter_customerList_alert_dialog;

    Exchanged_itemdata exchangeObj = Exchanged_itemdata.getInstance();

    private static final int ITEM_ACTIVITY_REQUEST_CODE = 0;
    private static final int CUSTOMER_ACTIVITY_REQUEST_CODE = 0;

    TextView searchForVoucher_textView, transaction_textview;
    TextView customerName_textView,customerEmail_textView,customerPhno_textView;

    Progress_dialoge pd;

    DatabaseReference reference,itemHistoryRef;
    DatabaseReference existingCustomersRef,existingItemsRef,existingVoucherRef;
    CheckBox cash_checkbox,voucher_checkbox;
    Query orderQuery;

    TextView itemLastActive_textView,submit_textView;

    ImageButton back_btn,sms_btn,gmail_btn,print_btn;
    Button btn_done;

    Dialog sendingdialog;
    LinearLayout toggling_linear,itemLastActive_linearLayout;
    Boolean item_btn,customer;

    ImageView itemImage_imageView,customerImage_imageView;

    TextView itemName_textView,itemID_textView,itemPriceCurrency_textView,itemPrice_textView,customer_add_textView,item_add_textView;

    TextView suggest_price_TextView;
    TextView date_textView,datebtn_textView;

    String firebaseAuthUID,itemID;
    String customerKeyID, itemKeyID,customerName,itemCategory,itemName;

    List<String> exisitngCustomerList,exisitngCustomerIDList,exisitngCustomerKeyIDList,existingCustomerPhnoList,existingCustomerDobList,existingCustomerAddressList,existingCustomerEmailList,existingCustomerImageUrlList;
    List<String> lessExisitngCustomerList,lessExisitngCustomerIDList,lessExistingCustomerPhnoList,lessExistingCustomerEmailList,lessExisitngCustomerKeyIDList,lessExistingCustomerImageUrlList;
    List<String> exisitngItemsNamesList, exisitngItemsSerialNoList,exisitngItemsKeyIDList,existingItemsPriceList,existingItemsLastActiveList,existingItemsImageUrlList;
    List<String> lessExisitngItemsNamesList, lessExisitngItemsSerialNoList,lessExisitngItemsKeyIDList,lessExistingItemsPriceList,lessExistingItemsLastActiveList,lessExistingItemsImageUrlList;
    List<String> voucher_number_list,Voucher_amount_list;
    List<String> dateList,lastActiveDatelist;

    LinearLayout itemDetails,customerDetails;

    EditText sale_price_editText,cash_editText,paid_editText;

    Date date;

    Progress_dialoge pd1,pd2,pd3;

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
        fetchingExisitingCustomers();
        fetchingExisitingItems();
        gettingVoucherList();
        onClickListenes();
        historyActivity();
    }

    private void initialize() {
        rbsItemDetails = RBSItemDetails.getInstance();
        rbsCustomerDetails = RBSCustomerDetails.getInstance();

        rbsItemDetails.clearData();
        rbsCustomerDetails.clearData();

        pd = new Progress_dialoge();
        item_btn=false;
        customer=false;

        pd1 = new Progress_dialoge();
        pd2 = new Progress_dialoge();
        pd3 = new Progress_dialoge();

        ////

        itemList_alert_dialog = new Dialog(this);
        itemList_alert_dialog.setContentView(R.layout.alert_rbs_itemlist);

        itemList_recyclerView = (RecyclerView)itemList_alert_dialog.findViewById(R.id.itemList_recyclerView);
        itemList_recyclerView.setLayoutManager(new GridLayoutManager(Sale.this,1));

        alert_rbs_itemlist_progressBar = (ProgressBar)itemList_alert_dialog.findViewById(R.id.alert_rbs_itemlist_progressBar);

        searchForItem_cardView = (CardView)findViewById(R.id.searchForItem_cardView);
        searchForCustomer_cardView = (CardView)findViewById(R.id.searchForCustomer_cardView);

        reference = FirebaseDatabase.getInstance().getReference();

        itemDetails = (LinearLayout)findViewById(R.id.itemDetails);
        customerDetails = (LinearLayout)findViewById(R.id.customerDetails);
        toggling_linear = (LinearLayout)findViewById(R.id.toggling_linear);

        cash_checkbox=(CheckBox)findViewById(R.id.cash_checkbox);
        voucher_checkbox=(CheckBox)findViewById(R.id.voucher_checkbox);

        exisitngItemsNamesList = new ArrayList<>();
        exisitngItemsSerialNoList = new ArrayList<>();
        exisitngItemsKeyIDList = new ArrayList<>();
        existingItemsPriceList= new ArrayList<>();
        existingItemsLastActiveList= new ArrayList<>();
        existingItemsImageUrlList= new ArrayList<>();

        lessExisitngItemsNamesList = new ArrayList<>();
        lessExisitngItemsSerialNoList = new ArrayList<>();
        lessExisitngItemsKeyIDList = new ArrayList<>();
        lessExistingItemsPriceList= new ArrayList<>();
        lessExistingItemsLastActiveList= new ArrayList<>();
        lessExistingItemsImageUrlList= new ArrayList<>();

        lessExisitngCustomerList = new ArrayList<>();
        lessExisitngCustomerIDList = new ArrayList<>();
        lessExistingCustomerPhnoList = new ArrayList<>();
        lessExistingCustomerEmailList = new ArrayList<>();
        lessExisitngCustomerKeyIDList = new ArrayList<>();
        lessExistingCustomerImageUrlList = new ArrayList<>();

        exisitngCustomerList = new ArrayList<>();
        exisitngCustomerIDList = new ArrayList<>();
        exisitngCustomerKeyIDList = new ArrayList<>();
        existingCustomerPhnoList= new ArrayList<>();
        existingCustomerDobList= new ArrayList<>();
        existingCustomerAddressList= new ArrayList<>();
        existingCustomerEmailList= new ArrayList<>();
        existingCustomerImageUrlList= new ArrayList<>();


        voucher_number_list= new ArrayList<>();
        Voucher_amount_list= new ArrayList<>();

        sale_price_editText = (EditText) findViewById(R.id.sale_price_editText);
        cash_editText = (EditText) findViewById(R.id.cash_editText);
        paid_editText = (EditText) findViewById(R.id.paid_editText);

        itemLastActive_linearLayout = (LinearLayout)findViewById(R.id.itemLastActive_linearLayout);

        itemName_textView = (TextView) findViewById(R.id.itemName_textView);
        itemID_textView = (TextView) findViewById(R.id.itemID_textView);
        itemPriceCurrency_textView = (TextView) findViewById(R.id.itemPriceCurrency_textView);
        itemPriceCurrency_textView.setText(Currency.getInstance().getCurrency());
        itemPrice_textView = (TextView) findViewById(R.id.itemPrice_textView);

        customerName_textView=(TextView)findViewById(R.id.customerName_textView);
        customerEmail_textView=(TextView)findViewById(R.id.customerEmail_textView);
        customerPhno_textView=(TextView)findViewById(R.id.customerPhno_textView);

        itemLastActive_textView = (TextView) findViewById(R.id.itemLastActive_textView);

        itemLastActive_textView = (TextView) findViewById(R.id.itemLastActive_textView);

        customer_add_textView =(TextView) findViewById(R.id.customer_add_textView);

        itemImage_imageView = (ImageView) findViewById(R.id.itemImage_imageView);
        customerImage_imageView = (ImageView) findViewById(R.id.customerImage_imageView);

        //////

        customerList_alert_dialog= new Dialog(this);
        customerList_alert_dialog.setContentView(R.layout.alert_rbs_customerlist);

        // TODO: 07-May-21  
        customerList_recyclerView = (RecyclerView)customerList_alert_dialog.findViewById(R.id.customerList_recyclerView);
        customerList_recyclerView.setLayoutManager(new GridLayoutManager(Sale.this,1));

        alert_rbs_customerlist_progressBar = (ProgressBar)customerList_alert_dialog.findViewById(R.id.alert_rbs_customerlist_progressBar);

        //////

        ////////////////////////
        suggest_price_TextView=(TextView)findViewById(R.id.suggest_price_TextView);
        searchForVoucher_textView=(TextView)findViewById(R.id.searchForVoucher_textView);
        transaction_textview=(TextView)findViewById(R.id.transaction_textview);
        item_add_textView =(TextView) findViewById(R.id.item_add_textView);


        back_btn =(ImageButton)findViewById(R.id.back_btn);
        submit_textView = (TextView) findViewById(R.id.submit_textView);
        datebtn_textView =(TextView) findViewById(R.id.datebtn_textView);
        date_textView =(TextView)findViewById(R.id.date_textView);

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

        dateList = new ArrayList<>();
        lastActiveDatelist = new ArrayList<>();


        /////Firebase config
        firebaseAuthUID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
        existingCustomersRef = FirebaseDatabase.getInstance().getReference("Customer_list");
        existingItemsRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/"+FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
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
                        if (dataSnapshot1.child("profile_image").exists()){

                        }else {
                            existingCustomerImageUrlList.add(String.valueOf(dataSnapshot1.child("ID_Image_urls").child("image_1").getValue()));
                        }

                        pd3.dismissProgressBar(Sale.this);
                    }

                    if (exisitngCustomerList.size()>3){
                        for (int i = 0 ; i<3;i++){
                            lessExisitngCustomerList.add(exisitngCustomerList.get(i));
                            lessExisitngCustomerIDList.add(exisitngCustomerIDList.get(i));
                            lessExistingCustomerPhnoList.add(existingCustomerPhnoList.get(i));
                            lessExistingCustomerEmailList.add(existingCustomerEmailList.get(i));
                            lessExisitngCustomerKeyIDList.add(exisitngCustomerKeyIDList.get(i));
                            lessExistingCustomerImageUrlList.add(existingCustomerImageUrlList.get(i));
                        }
                    }else {
                        for (int i = 0 ; i<exisitngCustomerList.size();i++){
                            lessExisitngCustomerList.add(exisitngCustomerList.get(i));
                            lessExisitngCustomerIDList.add(exisitngCustomerIDList.get(i));
                            lessExistingCustomerPhnoList.add(existingCustomerPhnoList.get(i));
                            lessExistingCustomerEmailList.add(existingCustomerEmailList.get(i));
                            lessExisitngCustomerKeyIDList.add(exisitngCustomerKeyIDList.get(i));
                            lessExistingCustomerImageUrlList.add(existingCustomerImageUrlList.get(i));
                        }
                    }

                    adapter_customerList_alert_dialog = new Adapter_customerList_alert_dialog(Sale.this,lessExisitngCustomerList,lessExisitngCustomerKeyIDList,lessExisitngCustomerIDList,lessExistingCustomerPhnoList,lessExistingCustomerEmailList,lessExistingCustomerImageUrlList,customerName_textView,customerEmail_textView,customerPhno_textView,customerImage_imageView,customerList_alert_dialog);
                    customerList_recyclerView.setAdapter(adapter_customerList_alert_dialog);
                    onCustomerRecyclerViewScrollListner();

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

                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                            exisitngItemsNamesList.add(String.valueOf(dataSnapshot2.child("Item_name").getValue()));
                            exisitngItemsSerialNoList.add(String.valueOf(dataSnapshot2.child("Item_id").getValue()));
                            if (dataSnapshot2.child("Last_active").exists()){
                                existingItemsLastActiveList.add(String.valueOf(dataSnapshot2.child("Last_active").getValue()));
                            }else {
                                existingItemsLastActiveList.add("NA");
                            }
                            existingItemsImageUrlList.add(String.valueOf(dataSnapshot2.child("Image").getValue()));
                            existingItemsPriceList.add(String.valueOf(dataSnapshot2.child("Price").getValue()));
                            exisitngItemsKeyIDList.add(String.valueOf(dataSnapshot2.getKey().toString()));
                            gettingHistoryList(String.valueOf(dataSnapshot2.getKey().toString()));

                        }
                    }
                    if (exisitngItemsNamesList.size()>3){
                        for (int i = 0 ; i<3;i++){
                            lessExisitngItemsNamesList.add(exisitngItemsNamesList.get(i));
                            lessExisitngItemsSerialNoList.add(exisitngItemsSerialNoList.get(i));
                            lessExistingItemsLastActiveList.add(existingItemsLastActiveList.get(i));
                            lessExistingItemsImageUrlList.add(existingItemsImageUrlList.get(i));
                            lessExistingItemsPriceList.add(existingItemsPriceList.get(i));
                            lessExisitngItemsKeyIDList.add(exisitngItemsKeyIDList.get(i));
                        }
                    }else {
                        for (int i = 0 ; i<exisitngItemsNamesList.size();i++){
                            lessExisitngItemsNamesList.add(exisitngItemsNamesList.get(i));
                            lessExisitngItemsSerialNoList.add(exisitngItemsSerialNoList.get(i));
                            lessExistingItemsLastActiveList.add(existingItemsLastActiveList.get(i));
                            lessExistingItemsImageUrlList.add(existingItemsImageUrlList.get(i));
                            lessExistingItemsPriceList.add(existingItemsPriceList.get(i));
                            lessExisitngItemsKeyIDList.add(exisitngItemsKeyIDList.get(i));
                        }
                    }
                    adapter_itemList_alert_dialog = new Adapter_itemList_alert_dialog(Sale.this,lessExisitngItemsNamesList,lessExisitngItemsSerialNoList,lessExisitngItemsKeyIDList,lessExistingItemsPriceList,lessExistingItemsLastActiveList,lessExistingItemsImageUrlList,itemName_textView,itemID_textView,itemPriceCurrency_textView,itemPrice_textView,itemLastActive_textView,itemImage_imageView,itemList_alert_dialog);
                    itemList_recyclerView.setAdapter(adapter_itemList_alert_dialog);
                    onScrollListner();
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
        searchForItem_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemList_alert_dialog.show();
            }
        });

        searchForCustomer_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerList_alert_dialog.show();
            }
        });

        customer_add_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sale.this,Customer_details.class);
                startActivityForResult(intent,CUSTOMER_ACTIVITY_REQUEST_CODE);
            }
        });

        item_add_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sale.this,Item_detail.class);
                startActivityForResult(intent,ITEM_ACTIVITY_REQUEST_CODE);
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
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        datebtn_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker=new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"date picker");

            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        submit_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields() == true)
                    detailsSubmit();

            }
        });


    }

    private void onScrollListner(){
        itemList_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = itemList_recyclerView.getLayoutManager().getChildCount();
                totalItems = itemList_recyclerView.getLayoutManager().getItemCount();
                scrollOutItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (isScrolling && (currentItems+scrollOutItems == totalItems)){
                    isScrolling = false;
                    fetchDataforRecyclerView();
                }
            }
        });
    }

    private void onCustomerRecyclerViewScrollListner(){
        customerList_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isCustomerScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentCustomer = customerList_recyclerView.getLayoutManager().getChildCount();
                totalCustomer = customerList_recyclerView.getLayoutManager().getItemCount();
                scrollOutCustomer = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (isCustomerScrolling && (currentCustomer+scrollOutCustomer == totalCustomer)){
                    isCustomerScrolling = false;
                    fetchDataforCustomerRecyclerView();
                }
            }
        });
    }

    private void fetchDataforRecyclerView() {
        if (itemDatafullyloaded){

        }else {
            alert_rbs_itemlist_progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int size = lessExisitngItemsNamesList.size()+2;
                    for (int i = lessExisitngItemsNamesList.size() ; i<size;i++){
                        if (i<exisitngItemsNamesList.size()){
                            lessExisitngItemsNamesList.add(exisitngItemsNamesList.get(i));
                            lessExisitngItemsSerialNoList.add(exisitngItemsSerialNoList.get(i));
                            lessExistingItemsLastActiveList.add(existingItemsLastActiveList.get(i));
                            lessExistingItemsImageUrlList.add(existingItemsImageUrlList.get(i));
                            lessExistingItemsPriceList.add(existingItemsPriceList.get(i));
                            lessExisitngItemsKeyIDList.add(exisitngItemsKeyIDList.get(i));
                        }

                    }
                    adapter_itemList_alert_dialog.notifyDataSetChanged();
                    alert_rbs_itemlist_progressBar.setVisibility(View.GONE);
                    if (lessExisitngItemsNamesList.size()==exisitngItemsNamesList.size()){
                        itemDatafullyloaded = true;
                    }
                }
            },3000);
        }

    }

    private void fetchDataforCustomerRecyclerView() {
        if (customerDatafullyloaded){

        }else {
            alert_rbs_customerlist_progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int size = lessExisitngCustomerList.size()+2;
                    for (int i = lessExisitngCustomerList.size(); i<size;i++){
                        if (i<exisitngCustomerList.size()){
                            System.out.println(String.valueOf(i));
                            lessExisitngCustomerList.add(exisitngCustomerList.get(i));
                            lessExisitngCustomerIDList.add(exisitngCustomerIDList.get(i));
                            lessExistingCustomerPhnoList.add(existingCustomerPhnoList.get(i));
                            lessExistingCustomerEmailList.add(existingCustomerEmailList.get(i));
                            lessExisitngCustomerKeyIDList.add(exisitngCustomerKeyIDList.get(i));
                            lessExistingCustomerImageUrlList.add(existingCustomerImageUrlList.get(i));
                        }

                    }
                    adapter_customerList_alert_dialog.notifyDataSetChanged();
                    alert_rbs_customerlist_progressBar.setVisibility(View.GONE);
                    if (lessExisitngCustomerList.size()==exisitngCustomerList.size()){
                        customerDatafullyloaded = true;
                    }

                }
            },3000);
        }

    }


    private boolean validateFields() {
        boolean valid = true;
        if (customerName_textView.getText().toString().equals("Search for customer ...")){
            Toast.makeText(this, "Please select Customer!", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (itemName_textView.getText().toString().equals("Search for item ...")){
            Toast.makeText(this, "Please select Item!", Toast.LENGTH_SHORT).show();
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
            transaction_textview.setError("Select atleast one Transaction Method");
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

            rbsItemDetails.uploadNewItemDetails(Sale.this);
            rbsCustomerDetails.uploadCustomerDetails(Sale.this);

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
//        itemDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Sale.this,Item_history.class);
//                intent.putExtra("ITEM_ID",itemKeyID);
//                intent.putExtra("ITEM_CATEGORY",itemCategory);
//                startActivity(intent);
//            }
//        });
//
//        customerDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Sale.this,Customer_history.class);
//                intent.putExtra("CUSTOMER_ID",customerKeyID);
//                startActivity(intent);
//            }
//        });
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
                Toast.makeText(this, String.valueOf(rbsItemDetails.getImageUrlList().size()), Toast.LENGTH_SHORT).show();
                String itemname_returnString = data.getStringExtra("Item_name");
                String itemid_returnString = data.getStringExtra("Item_id");
                String itemcategory_returnString = data.getStringExtra("Item_category");
                String itemkeyid_returnString = data.getStringExtra("Item_keyid");
                String itemprice_returnString = data.getStringExtra("Item_price");
                String returnString = data.getStringExtra("Last_Active");
                // set text view with string

                itemKeyID = itemkeyid_returnString;
                itemCategory = itemcategory_returnString;

                itemName_textView.setText(itemname_returnString);
                itemID_textView.setText(itemid_returnString);
                itemName_textView.setTextColor(getResources().getColor(R.color.gradientDarkBlue));
                itemPrice_textView.setText(itemprice_returnString);
                itemLastActive_textView.setText(returnString);
                itemImage_imageView.setImageURI(rbsItemDetails.getImageUrlList().get(0));

                itemID_textView.setVisibility(View.VISIBLE);
                itemPriceCurrency_textView.setVisibility(View.VISIBLE);
                itemPrice_textView.setVisibility(View.VISIBLE);
                itemImage_imageView.setVisibility(View.VISIBLE);
                itemLastActive_linearLayout.setVisibility(View.VISIBLE);


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

                customerName_textView.setText(title_returnString);
                customerName_textView.setTextColor(getResources().getColor(R.color.gradientDarkBlue));
                customerImage_imageView.setImageURI(rbsCustomerDetails.getImageUrlList().get(0));
                customerImage_imageView.setVisibility(View.VISIBLE);
                customerEmail_textView.setText(email_returnString);
                customerEmail_textView.setVisibility(View.VISIBLE);
                customerPhno_textView.setText(phone_no_returnString);
                customerPhno_textView.setVisibility(View.VISIBLE);


            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


}

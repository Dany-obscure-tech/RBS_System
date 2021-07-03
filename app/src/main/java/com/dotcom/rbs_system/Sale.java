package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.dotcom.rbs_system.Adapter.Adapter_customerList_alert_dialog;
import com.dotcom.rbs_system.Adapter.Adapter_itemList_alert_dialog;
import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.Classes.Customer_history_class;
import com.dotcom.rbs_system.Classes.RBSCustomerDetails;
import com.dotcom.rbs_system.Classes.RBSItemDetails;
import com.dotcom.rbs_system.Classes.UniquePushID;
import com.dotcom.rbs_system.Classes.UserDetails;
import com.dotcom.rbs_system.Model.SampleSearchModel;
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

public class Sale extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    RBSItemDetails rbsItemDetails;
    RBSCustomerDetails rbsCustomerDetails;

    Handler handler = new Handler();
    Handler handler2 = new Handler();

    AsyncTask asyncTask = null;

    String searchItem, findItem;
    String searchCustomer, findCustomer;

    AsyncEscPosPrinter printer;

    Boolean isScrolling = false, itemDatafullyloaded = false;
    Boolean isCustomerScrolling = false, customerDatafullyloaded = false;

    ProgressBar alert_rbs_itemlist_progressBar, alert_rbs_customerlist_progressBar;

    int currentItems, totalItems, scrollOutItems;
    int currentCustomer, totalCustomer, scrollOutCustomer;

    CardView searchForItem_cardView, searchForCustomer_cardView;

    Dialog itemList_alert_dialog, customerList_alert_dialog;
    RecyclerView itemList_recyclerView, customerList_recyclerView;
    Adapter_itemList_alert_dialog adapter_itemList_alert_dialog;
    Adapter_customerList_alert_dialog adapter_customerList_alert_dialog;

    private static final int ITEM_ACTIVITY_REQUEST_CODE = 0;
    private static final int CUSTOMER_ACTIVITY_REQUEST_CODE = 0;

    TextView searchForVoucher_textView, transaction_textview;
    TextView customerName_textView, customerEmail_textView, customerPhno_textView, customerID_textView;
    TextView viewCustomerDetails_textView;

    TextView email_title_textview, id_title_textview, phone_no_title_textview;

    Progress_dialoge pd;

    DatabaseReference reference, itemHistoryRef;
    DatabaseReference existingCustomersRef, existingItemsRef, existingVoucherRef;
    CheckBox cash_checkbox, voucher_checkbox;
    Query orderQuery;

    TextView itemLastActive_textView, submit_textView;

    EditText searchItem_editText, searchCustomer_editText;

    ImageButton back_btn, sms_btn, gmail_btn, print_btn;
    ImageButton searchItem_imageBtn;
    Button btn_done, test;

    Dialog sendingdialog;
    LinearLayout toggling_linear, itemLastActive_linearLayout, customerID_linearLayout;
    Boolean item_btn, customer;

    ImageView itemImage_imageView, customerImage_imageView;

    TextView itemName_textView, itemID_textView, itemPriceCurrency_textView, itemPrice_textView, customer_add_textView, item_add_textView;
    TextView viewItemDetails_textView;

    TextView suggest_price_TextView;
    TextView date_textView, datebtn_textView;

    String firebaseAuthUID, itemID;
    String customerKeyID, itemKeyID, customerName, itemCategory, itemName;

    List<String> exisitngCustomerList, exisitngCustomerIDList, exisitngCustomerKeyIDList, existingCustomerPhnoList, existingCustomerDobList, existingCustomerAddressList, existingCustomerEmailList, existingCustomerImageUrlList;
    List<String> lessExisitngCustomerList, lessExisitngCustomerIDList, lessExistingCustomerPhnoList, lessExistingCustomerDobList, lessExistingCustomerAddressList, lessExistingCustomerEmailList, lessExisitngCustomerKeyIDList, lessExistingCustomerImageUrlList;
    List<String> filteredExisitngCustomerList, filteredExisitngCustomerIDList, filteredExisitngCustomerKeyIDList, filteredExistingCustomerPhnoList, filteredExistingCustomerDobList, filteredExistingCustomerAddressList, filteredExistingCustomerEmailList, filteredExistingCustomerImageUrlList;
    List<String> exisitngItemsNamesList, exisitngItemsSerialNoList, exisitngItemsKeyIDList, existingItemsPriceList, existingItemsCategoryList, existingItemsLastActiveList, existingItemsImageUrlList;
    List<String> filteredExisitngItemsNamesList, filteredExisitngItemsSerialNoList, filteredExisitngItemsKeyIDList, filteredExistingItemsPriceList, filteredExistingItemsCategoryList, filteredExistingItemsLastActiveList, filteredExistingItemsImageUrlList;
    List<String> lessExisitngItemsNamesList, lessExisitngItemsSerialNoList, lessExisitngItemsKeyIDList, lessExistingItemsPriceList, lessExistingItemsCategoryList, lessExistingItemsLastActiveList, lessExistingItemsImageUrlList;
    List<String> voucher_number_list, Voucher_amount_list;
    List<String> dateList, lastActiveDatelist;

    LinearLayout itemDetails, customerDetails;

    EditText sale_price_editText, cash_editText, paid_editText;

    Date date;

    Progress_dialoge pd1, pd2, pd3;

    private ArrayList<SampleSearchModel> getting_voucher_data() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i = 0; i < Voucher_amount_list.size(); i++) {
            items.add(new SampleSearchModel(voucher_number_list.get(i) + "\n(" + Voucher_amount_list.get(i) + ")", null, null, null, null, null, null, null));
        }

        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        initialize();
        checkForItemSell();
        fetchingExisitingCustomers();
        fetchingExisitingItems();
        searchItem();
        searchCustomers();
        gettingVoucherList();
        onClickListenes();
    }

    private void initialize() {
        rbsItemDetails = RBSItemDetails.getInstance();
        rbsCustomerDetails = RBSCustomerDetails.getInstance();

        rbsItemDetails.clearData();
        rbsCustomerDetails.clearData();

        pd = new Progress_dialoge();
        item_btn = false;
        customer = false;

        pd1 = new Progress_dialoge();
        pd2 = new Progress_dialoge();
        pd3 = new Progress_dialoge();

        ////

        itemList_alert_dialog = new Dialog(this);
        itemList_alert_dialog.setContentView(R.layout.alert_rbs_itemlist);

        searchItem_editText = (EditText) itemList_alert_dialog.findViewById(R.id.searchItem_editText);

        itemList_recyclerView = (RecyclerView) itemList_alert_dialog.findViewById(R.id.itemList_recyclerView);
        itemList_recyclerView.setLayoutManager(new GridLayoutManager(Sale.this, 1));

        alert_rbs_itemlist_progressBar = (ProgressBar) itemList_alert_dialog.findViewById(R.id.alert_rbs_itemlist_progressBar);

        searchForItem_cardView = (CardView) findViewById(R.id.searchForItem_cardView);
        searchForCustomer_cardView = (CardView) findViewById(R.id.searchForCustomer_cardView);

        reference = FirebaseDatabase.getInstance().getReference();

        itemDetails = (LinearLayout) findViewById(R.id.itemDetails);
        customerDetails = (LinearLayout) findViewById(R.id.customerDetails);
        toggling_linear = (LinearLayout) findViewById(R.id.toggling_linear);

        cash_checkbox = (CheckBox) findViewById(R.id.cash_checkbox);
        voucher_checkbox = (CheckBox) findViewById(R.id.voucher_checkbox);

        exisitngItemsNamesList = new ArrayList<>();
        exisitngItemsSerialNoList = new ArrayList<>();
        exisitngItemsKeyIDList = new ArrayList<>();
        existingItemsPriceList = new ArrayList<>();
        existingItemsCategoryList = new ArrayList<>();
        existingItemsLastActiveList = new ArrayList<>();
        existingItemsImageUrlList = new ArrayList<>();

        filteredExisitngItemsNamesList = new ArrayList<>();
        filteredExisitngItemsSerialNoList = new ArrayList<>();
        filteredExisitngItemsKeyIDList = new ArrayList<>();
        filteredExistingItemsPriceList = new ArrayList<>();
        filteredExistingItemsCategoryList = new ArrayList<>();
        filteredExistingItemsLastActiveList = new ArrayList<>();
        filteredExistingItemsImageUrlList = new ArrayList<>();

        exisitngCustomerList = new ArrayList<>();
        exisitngCustomerIDList = new ArrayList<>();
        exisitngCustomerKeyIDList = new ArrayList<>();
        existingCustomerPhnoList = new ArrayList<>();
        existingCustomerDobList = new ArrayList<>();
        existingCustomerAddressList = new ArrayList<>();
        existingCustomerEmailList = new ArrayList<>();
        existingCustomerImageUrlList = new ArrayList<>();

        lessExisitngItemsNamesList = new ArrayList<>();
        lessExisitngItemsSerialNoList = new ArrayList<>();
        lessExisitngItemsKeyIDList = new ArrayList<>();
        lessExistingItemsPriceList = new ArrayList<>();
        lessExistingItemsCategoryList = new ArrayList<>();
        lessExistingItemsLastActiveList = new ArrayList<>();
        lessExistingItemsImageUrlList = new ArrayList<>();

        lessExisitngCustomerList = new ArrayList<>();
        lessExisitngCustomerIDList = new ArrayList<>();
        lessExisitngCustomerKeyIDList = new ArrayList<>();
        lessExistingCustomerPhnoList = new ArrayList<>();
        lessExistingCustomerDobList = new ArrayList<>();
        lessExistingCustomerAddressList = new ArrayList<>();
        lessExistingCustomerEmailList = new ArrayList<>();
        lessExistingCustomerImageUrlList = new ArrayList<>();


        filteredExisitngCustomerList = new ArrayList<>();
        filteredExisitngCustomerIDList = new ArrayList<>();
        filteredExisitngCustomerKeyIDList = new ArrayList<>();
        filteredExistingCustomerPhnoList = new ArrayList<>();
        filteredExistingCustomerDobList = new ArrayList<>();
        filteredExistingCustomerAddressList = new ArrayList<>();
        filteredExistingCustomerEmailList = new ArrayList<>();
        filteredExistingCustomerImageUrlList = new ArrayList<>();

        voucher_number_list = new ArrayList<>();
        Voucher_amount_list = new ArrayList<>();

        sale_price_editText = (EditText) findViewById(R.id.sale_price_editText);
        cash_editText = (EditText) findViewById(R.id.cash_editText);
        paid_editText = (EditText) findViewById(R.id.paid_editText);

        itemLastActive_linearLayout = (LinearLayout) findViewById(R.id.itemLastActive_linearLayout);

        customerID_linearLayout = (LinearLayout) findViewById(R.id.customerID_linearLayout);

        itemName_textView = (TextView) findViewById(R.id.itemName_textView);
        itemID_textView = (TextView) findViewById(R.id.itemID_textView);
        itemPriceCurrency_textView = (TextView) findViewById(R.id.itemPriceCurrency_textView);
        itemPriceCurrency_textView.setText(Currency.getInstance().getCurrency());
        itemPrice_textView = (TextView) findViewById(R.id.itemPrice_textView);
        viewItemDetails_textView = (TextView) findViewById(R.id.viewItemDetails_textView);

        customerName_textView = (TextView) findViewById(R.id.customerName_textView);
        customerEmail_textView = (TextView) findViewById(R.id.customerEmail_textView);
        customerID_textView = (TextView) findViewById(R.id.customerID_textView);
        customerPhno_textView = (TextView) findViewById(R.id.customerPhno_textView);
        viewCustomerDetails_textView = (TextView) findViewById(R.id.viewCustomerDetails_textView);

        itemLastActive_textView = (TextView) findViewById(R.id.itemLastActive_textView);

        itemLastActive_textView = (TextView) findViewById(R.id.itemLastActive_textView);

        customer_add_textView = (TextView) findViewById(R.id.customer_add_textView);

        itemImage_imageView = (ImageView) findViewById(R.id.itemImage_imageView);
        customerImage_imageView = (ImageView) findViewById(R.id.customerImage_imageView);

        //////

        customerList_alert_dialog = new Dialog(this);
        customerList_alert_dialog.setContentView(R.layout.alert_rbs_customerlist);

        searchCustomer_editText = (EditText) customerList_alert_dialog.findViewById(R.id.searchCustomer_editText);

        customerList_recyclerView = (RecyclerView) customerList_alert_dialog.findViewById(R.id.customerList_recyclerView);
        customerList_recyclerView.setLayoutManager(new GridLayoutManager(Sale.this, 1));

        alert_rbs_customerlist_progressBar = (ProgressBar) customerList_alert_dialog.findViewById(R.id.alert_rbs_customerlist_progressBar);

        //////

        ////////////////////////
        suggest_price_TextView = (TextView) findViewById(R.id.suggest_price_TextView);
        searchForVoucher_textView = (TextView) findViewById(R.id.searchForVoucher_textView);
        transaction_textview = (TextView) findViewById(R.id.transaction_textview);
        item_add_textView = (TextView) findViewById(R.id.item_add_textView);

        email_title_textview = (TextView) findViewById(R.id.email_title_textview);
        id_title_textview = (TextView) findViewById(R.id.id_title_textview);
        phone_no_title_textview = (TextView) findViewById(R.id.phone_no_title_textview);


        submit_textView = (TextView) findViewById(R.id.submit_textView);
        datebtn_textView = (TextView) findViewById(R.id.datebtn_textView);
        date_textView = (TextView) findViewById(R.id.date_textView);

        sendingdialog = new Dialog(this);
        sendingdialog.setContentView(R.layout.dialoge_items);
        sendingdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });

        searchItem_imageBtn = (ImageButton) findViewById(R.id.searchItem_imageBtn);
        back_btn = (ImageButton) findViewById(R.id.back_btn);
        gmail_btn = (ImageButton) sendingdialog.findViewById(R.id.gmail_btn);
        print_btn = (ImageButton) sendingdialog.findViewById(R.id.print_btn);
        sms_btn = (ImageButton) sendingdialog.findViewById(R.id.sms_btn);

        btn_done = (Button) sendingdialog.findViewById(R.id.btn_done);
        test = (Button) findViewById(R.id.test);


        dateList = new ArrayList<>();
        lastActiveDatelist = new ArrayList<>();


        /////Firebase config
        firebaseAuthUID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
        existingCustomersRef = FirebaseDatabase.getInstance().getReference("Customer_list");
        existingItemsRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/" + FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        existingVoucherRef = FirebaseDatabase.getInstance().getReference("Voucher_List");
        existingVoucherRef = FirebaseDatabase.getInstance().getReference("Voucher_List/" + FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

        ///////

        date = Calendar.getInstance().getTime();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);
        date_textView.setText(currentDateString);

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = (Date) formatter.parse(date.getDay() + "-" + date.getMonth() + "-" + date.getYear());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void printTcp() {
        if (asyncTask == null) {
            try {
                // this.printIt(new TcpConnection(ipAddress.getText().toString(), Integer.parseInt(portAddress.getText().toString())));
                asyncTask = new AsyncTcpEscPosPrint(this).execute(this.getAsyncEscPosPrinter(new TcpConnection("192.168.1.123", 9100)));
            } catch (NumberFormatException e) {
                new AlertDialog.Builder(this)
                        .setTitle("Invalid TCP port address")
                        .setMessage("Port field must be a number.")
                        .show();
                e.printStackTrace();
            }
        } else {
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
//        return "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
//                "[L]\n" +
//                "[C]<u type='double'>" + format.format(new Date()) + "</u>\n" +
//                "[C]\n" +
//                "[C]================================\n" +
//                "[L]\n" +
//                "[L]<b>"+"</b>\n" +
//                "[C]--------------------------------\n" +
//                "[R]TOTAL PRICE :[R]"+purchase_price_editText.getText().toString()+Currency.getInstance().getCurrency()+ "\n" +
//                "[L]\n" +
//                "[C]================================\n" +
//                "[L]\n" +
//                "[L]<u><font color='bg-black' size='tall'>Customer :</font></u>\n" +
//                "[L]"+"\n" +
//                "[L]Phno : "+"\n" +
//                "\n" +
//                "[L]\n";

        return "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                "[L]\n" +
                "[L]Invoice Printed"+
                "[C]<u type='double'>" + format.format(new Date()) + "</u>\n" +
                "[C]\n" +
                "[L]Invoice no: 12345\n"+
                "[L]\n"+
                "[C]<font color='bg-black'>================================</font>\n" +
                "[L]\n"+
                "[C]<font color='black' size='wide'> CUSTOMER </font>\n" +
                "[L]\n"+
                "[L]Name:"+customerName_textView.getText().toString()+"\n"+
                "[L]\n" +
                "[L]Email:"+customerEmail_textView.getText().toString()+"\n"+
                "[L]\n" +
                "[L]ID:"+customerID_textView.getText().toString()+"\n"+
                "[L]\n" +
                "[L]Phone no:"+customerID_textView.getText().toString()+"\n"+
                "[C]--------------------------------\n" +
                "[L]\n"+
                "[C]<font color='black' size='wide'> ITEM </font>\n" +
                "[L]\n" +
                "[L]Name:"+itemName_textView.getText().toString()+"\n"+
                "[L]\n" +
                "[L]Serial no:"+itemID_textView.getText().toString()+"\n"+
                "[L]\n"+
                "[C]================================\n" +
                "[L]\n"+
                "[L]Sale Price: [R]"+Currency.getInstance().getCurrency()+sale_price_editText.getText().toString()+"\n"+
                "[L]\n" +
                "[L]Paid Amount: [R]"+Currency.getInstance().getCurrency()+paid_editText.getText().toString()+"\n"+
                "[L]\n" +
                "[L]Sale Date: [R]"+date_textView.getText().toString()+"\n"+
                "[C]\n" +
                "[C]<font color='bg-black'> -------------------------------- </font>\n" +
                "[C]\n" +
                "[L]<font color='black' size='tall'> Shopkeeper Terms & Conditions </font>\n" +
                "[C]\n" +
                "[L]1.This is the first condition. 2. This is the second condition\n"+
                "[C]\n" +
                "[L]<font color='black' size='tall'> Buy Local Terms & Conditions </font>\n" +
                "[C]\n" +
                "[L]1.This is the first condition. 2. This is the second condition\n"+
                "[L]\n"+
                "[L]\n"+
                "[L]\n";

    }

    private void checkForItemSell() {
        if (getIntent().getStringExtra("ITEM_SELL_CHECK").equals("TRUE")) {

            String itemname_returnString = getIntent().getStringExtra("Item_name");
            String itemid_returnString = getIntent().getStringExtra("Item_id");
            String itemcategory_returnString = getIntent().getStringExtra("Item_category");
            String itemkeyid_returnString = getIntent().getStringExtra("Item_keyid");
            String itemprice_returnString = getIntent().getStringExtra("Item_price");
            String itemlstactive_returnString = getIntent().getStringExtra("Last_Active");
            String itemlimage_returnString = getIntent().getStringExtra("Item_image");
            // set text view with string

            itemKeyID = itemkeyid_returnString;
            itemCategory = itemcategory_returnString;
            itemName_textView.setText(itemname_returnString);
            itemID_textView.setText(itemid_returnString);
            itemPrice_textView.setText(itemprice_returnString);
            itemLastActive_textView.setText(itemlstactive_returnString);
            Picasso.get().load(itemlimage_returnString).into(itemImage_imageView);

            rbsItemDetails.setKey(itemkeyid_returnString);
            rbsItemDetails.setItemCategory(itemcategory_returnString);
            rbsItemDetails.setItemName(itemname_returnString);
            rbsItemDetails.setItemID(itemid_returnString);
            rbsItemDetails.setItemPrice(itemprice_returnString);
            //todo
//            rbsItemDetails.setFirstImageUri(Uri.parse(itemlimage_returnString));

            itemName_textView.setTextColor(getResources().getColor(R.color.gradientDarkBlue));
            itemID_textView.setVisibility(View.VISIBLE);
            itemPriceCurrency_textView.setVisibility(View.VISIBLE);
            itemPrice_textView.setVisibility(View.VISIBLE);
            itemImage_imageView.setVisibility(View.VISIBLE);
            itemLastActive_linearLayout.setVisibility(View.VISIBLE);
            itemLastActive_textView.setVisibility(View.VISIBLE);
            viewItemDetails_textView.setVisibility(View.VISIBLE);

            rbsItemDetails.setCheck("Sale existing item");
        }
    }

    private void fetchingExisitingCustomers() {
        pd3.showProgressBar(Sale.this);
        existingCustomersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        exisitngCustomerList.add(String.valueOf(dataSnapshot1.child("Name").getValue()));
                        exisitngCustomerIDList.add(String.valueOf(dataSnapshot1.child("ID").getValue()));
                        existingCustomerPhnoList.add(String.valueOf(dataSnapshot1.child("Phone_no").getValue()));
                        existingCustomerDobList.add(String.valueOf(dataSnapshot1.child("DOB").getValue()));
                        existingCustomerAddressList.add(String.valueOf(dataSnapshot1.child("Address").getValue()));
                        existingCustomerEmailList.add(String.valueOf(dataSnapshot1.child("Email").getValue()));
                        exisitngCustomerKeyIDList.add(String.valueOf(dataSnapshot1.child("key_id").getValue()));
                        if (dataSnapshot1.child("profile_image").exists()) {

                        } else {
                            existingCustomerImageUrlList.add(String.valueOf(dataSnapshot1.child("ID_Image_urls").child("image_1").getValue()));
                        }

                        pd3.dismissProgressBar(Sale.this);
                    }
//i changed the value 3 to 10 here
                    if (exisitngCustomerList.size() > 10) {
                        for (int i = 0; i < 10; i++) {
                            lessExisitngCustomerList.add(exisitngCustomerList.get(i));
                            lessExisitngCustomerIDList.add(exisitngCustomerIDList.get(i));
                            lessExistingCustomerPhnoList.add(existingCustomerPhnoList.get(i));
                            lessExistingCustomerDobList.add(existingCustomerDobList.get(i));
                            lessExistingCustomerAddressList.add(existingCustomerAddressList.get(i));
                            lessExistingCustomerEmailList.add(existingCustomerEmailList.get(i));
                            lessExisitngCustomerKeyIDList.add(exisitngCustomerKeyIDList.get(i));
                            lessExistingCustomerImageUrlList.add(existingCustomerImageUrlList.get(i));
                        }
                    } else {
                        for (int i = 0; i < exisitngCustomerList.size(); i++) {
                            lessExisitngCustomerList.add(exisitngCustomerList.get(i));
                            lessExisitngCustomerIDList.add(exisitngCustomerIDList.get(i));
                            lessExistingCustomerPhnoList.add(existingCustomerPhnoList.get(i));
                            lessExistingCustomerDobList.add(existingCustomerDobList.get(i));
                            lessExistingCustomerAddressList.add(existingCustomerAddressList.get(i));
                            lessExistingCustomerEmailList.add(existingCustomerEmailList.get(i));
                            lessExisitngCustomerKeyIDList.add(exisitngCustomerKeyIDList.get(i));
                            lessExistingCustomerImageUrlList.add(existingCustomerImageUrlList.get(i));
                        }
                    }

                    adapter_customerList_alert_dialog = new Adapter_customerList_alert_dialog(Sale.this, lessExisitngCustomerList, lessExisitngCustomerKeyIDList, lessExisitngCustomerIDList, lessExistingCustomerPhnoList, lessExistingCustomerEmailList, lessExistingCustomerImageUrlList, customerName_textView, customerEmail_textView, customerID_textView, customerPhno_textView, customerImage_imageView, customerList_alert_dialog, viewCustomerDetails_textView, customerID_linearLayout);
                    customerList_recyclerView.setAdapter(adapter_customerList_alert_dialog);
                    onCustomerRecyclerViewScrollListner(exisitngCustomerList, exisitngCustomerIDList, existingCustomerPhnoList, existingCustomerDobList, existingCustomerAddressList, existingCustomerEmailList, exisitngCustomerKeyIDList, existingCustomerImageUrlList);

                } else {
                    pd3.dismissProgressBar(Sale.this);
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd3.dismissProgressBar(Sale.this);
            }
        });

    }

    private void searchCustomers() {
        searchCustomer_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentItems = 0;
                totalItems = 0;
                scrollOutItems = 0;
                handler.removeCallbacksAndMessages(null);

                lessExisitngCustomerList.clear();
                lessExisitngCustomerIDList.clear();
                lessExisitngCustomerKeyIDList.clear();
                lessExistingCustomerPhnoList.clear();
                lessExistingCustomerDobList.clear();
                lessExistingCustomerAddressList.clear();
                lessExistingCustomerEmailList.clear();
                lessExistingCustomerImageUrlList.clear();

                filteredExisitngCustomerList.clear();
                filteredExisitngCustomerIDList.clear();
                filteredExisitngCustomerKeyIDList.clear();
                filteredExistingCustomerPhnoList.clear();
                filteredExistingCustomerDobList.clear();
                filteredExistingCustomerAddressList.clear();
                filteredExistingCustomerEmailList.clear();
                filteredExistingCustomerImageUrlList.clear();

                findCustomer = searchCustomer_editText.getText().toString();
                for (int j = 0; j < exisitngCustomerList.size(); j++) {
                    searchCustomer = exisitngCustomerList.get(j) + " " + exisitngCustomerIDList.get(j);

                    int searchMeLength = searchCustomer.length();
                    int findMeLength = findCustomer.length();
//                    boolean foundIt = false;
                    for (int i = 0;
                         i <= (searchMeLength - findMeLength);
                         i++) {
                        if (searchCustomer.regionMatches(true, i, findCustomer, 0, findMeLength)) {

                            filteredExisitngCustomerList.add(exisitngCustomerList.get(j));
                            filteredExisitngCustomerIDList.add(exisitngCustomerIDList.get(j));
                            filteredExisitngCustomerKeyIDList.add(exisitngCustomerKeyIDList.get(j));
                            filteredExistingCustomerPhnoList.add(existingCustomerPhnoList.get(j));
                            filteredExistingCustomerDobList.add(existingCustomerDobList.get(j));
                            filteredExistingCustomerAddressList.add(existingCustomerAddressList.get(j));
                            filteredExistingCustomerEmailList.add(existingCustomerEmailList.get(j));
                            filteredExistingCustomerImageUrlList.add(existingCustomerImageUrlList.get(j));


                            break;
                        }
                    }
                }
                getFilteredCustomers();
            }
        });
    }

    private void getFilteredCustomers() {
        if (filteredExisitngCustomerList.size() > 10) {
            for (int i = 0; i < 10; i++) {
                lessExisitngCustomerList.add(filteredExisitngCustomerList.get(i));
                lessExisitngCustomerIDList.add(filteredExisitngCustomerIDList.get(i));
                lessExisitngCustomerKeyIDList.add(filteredExisitngCustomerKeyIDList.get(i));
                lessExistingCustomerPhnoList.add(filteredExistingCustomerPhnoList.get(i));
                lessExistingCustomerDobList.add(filteredExistingCustomerDobList.get(i));
                lessExistingCustomerAddressList.add(filteredExistingCustomerAddressList.get(i));
                lessExistingCustomerEmailList.add(filteredExistingCustomerEmailList.get(i));
                lessExistingCustomerImageUrlList.add(filteredExistingCustomerImageUrlList.get(i));
            }
        } else {
            for (int i = 0; i < filteredExisitngCustomerList.size(); i++) {
                lessExisitngCustomerList.add(filteredExisitngCustomerList.get(i));
                lessExisitngCustomerIDList.add(filteredExisitngCustomerIDList.get(i));
                lessExisitngCustomerKeyIDList.add(filteredExisitngCustomerKeyIDList.get(i));
                lessExistingCustomerPhnoList.add(filteredExistingCustomerPhnoList.get(i));
                lessExistingCustomerDobList.add(filteredExistingCustomerDobList.get(i));
                lessExistingCustomerAddressList.add(filteredExistingCustomerAddressList.get(i));
                lessExistingCustomerEmailList.add(filteredExistingCustomerEmailList.get(i));
                lessExistingCustomerImageUrlList.add(filteredExistingCustomerImageUrlList.get(i));
            }
        }

        adapter_customerList_alert_dialog = new Adapter_customerList_alert_dialog(Sale.this, lessExisitngCustomerList, lessExisitngCustomerKeyIDList, lessExisitngCustomerIDList, lessExistingCustomerPhnoList, lessExistingCustomerEmailList, lessExistingCustomerImageUrlList, customerName_textView, customerEmail_textView, customerID_textView, customerPhno_textView, customerImage_imageView, customerList_alert_dialog, viewCustomerDetails_textView, customerID_linearLayout);
        customerList_recyclerView.setAdapter(adapter_customerList_alert_dialog);
        onScrollCustomerListner(filteredExisitngCustomerList, filteredExisitngCustomerIDList, filteredExisitngCustomerKeyIDList, filteredExistingCustomerPhnoList, filteredExistingCustomerDobList, filteredExistingCustomerAddressList, filteredExistingCustomerEmailList, filteredExistingCustomerImageUrlList);

    }

    private void fetchingExisitingItems() {
        pd2.showProgressBar(Sale.this);
        existingItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            exisitngItemsNamesList.add(String.valueOf(dataSnapshot2.child("Item_name").getValue()));
                            exisitngItemsSerialNoList.add(String.valueOf(dataSnapshot2.child("Serial_no").getValue()));
                            if (dataSnapshot2.child("Last_active").exists()) {
                                existingItemsLastActiveList.add(String.valueOf(dataSnapshot2.child("Last_active").getValue()));
                            } else {
                                existingItemsLastActiveList.add("NA");
                            }
                            existingItemsImageUrlList.add(String.valueOf(dataSnapshot2.child("Image").getValue()));
                            existingItemsPriceList.add(String.valueOf(dataSnapshot2.child("Price").getValue()));
                            existingItemsCategoryList.add(String.valueOf(dataSnapshot2.child("Category").getValue()));
                            exisitngItemsKeyIDList.add(String.valueOf(dataSnapshot2.getKey().toString()));
                            gettingHistoryList(String.valueOf(dataSnapshot2.getKey().toString()));

                        }
                    }
                    /////I changed the 3 value to 10 here
                    if (exisitngItemsNamesList.size() > 10) {
                        for (int i = 0; i < 10; i++) {
                            lessExisitngItemsNamesList.add(exisitngItemsNamesList.get(i));
                            lessExisitngItemsSerialNoList.add(exisitngItemsSerialNoList.get(i));
                            lessExistingItemsLastActiveList.add(existingItemsLastActiveList.get(i));
                            lessExistingItemsImageUrlList.add(existingItemsImageUrlList.get(i));
                            lessExistingItemsPriceList.add(existingItemsPriceList.get(i));
                            lessExistingItemsCategoryList.add(existingItemsCategoryList.get(i));
                            lessExisitngItemsKeyIDList.add(exisitngItemsKeyIDList.get(i));
                        }
                    } else {
                        for (int i = 0; i < exisitngItemsNamesList.size(); i++) {
                            lessExisitngItemsNamesList.add(exisitngItemsNamesList.get(i));
                            lessExisitngItemsSerialNoList.add(exisitngItemsSerialNoList.get(i));
                            lessExistingItemsLastActiveList.add(existingItemsLastActiveList.get(i));
                            lessExistingItemsImageUrlList.add(existingItemsImageUrlList.get(i));
                            lessExistingItemsPriceList.add(existingItemsPriceList.get(i));
                            lessExistingItemsCategoryList.add(existingItemsCategoryList.get(i));
                            lessExisitngItemsKeyIDList.add(exisitngItemsKeyIDList.get(i));
                        }
                    }
                    adapter_itemList_alert_dialog = new Adapter_itemList_alert_dialog(Sale.this, lessExisitngItemsNamesList, lessExisitngItemsSerialNoList, lessExisitngItemsKeyIDList, lessExistingItemsPriceList, lessExistingItemsCategoryList, lessExistingItemsLastActiveList, lessExistingItemsImageUrlList, itemName_textView, itemID_textView, itemPriceCurrency_textView, itemPrice_textView, itemLastActive_textView, itemImage_imageView, viewItemDetails_textView, itemList_alert_dialog);
                    itemList_recyclerView.setAdapter(adapter_itemList_alert_dialog);
                    onScrollListner(exisitngItemsNamesList, exisitngItemsSerialNoList, existingItemsLastActiveList, existingItemsImageUrlList, existingItemsPriceList, existingItemsCategoryList, exisitngItemsKeyIDList);
                    pd2.dismissProgressBar(Sale.this);
                } else {

                    pd2.dismissProgressBar(Sale.this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd2.dismissProgressBar(Sale.this);

            }
        });

    }

    private void searchItem() {
        searchItem_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentItems = 0;
                totalItems = 0;
                scrollOutItems = 0;
                handler.removeCallbacksAndMessages(null);

                lessExisitngItemsNamesList.clear();
                lessExisitngItemsSerialNoList.clear();
                lessExistingItemsLastActiveList.clear();
                lessExistingItemsImageUrlList.clear();
                lessExistingItemsPriceList.clear();
                lessExistingItemsCategoryList.clear();
                lessExisitngItemsKeyIDList.clear();

                filteredExisitngItemsNamesList.clear();
                filteredExisitngItemsSerialNoList.clear();
                filteredExistingItemsLastActiveList.clear();
                filteredExistingItemsImageUrlList.clear();
                filteredExistingItemsPriceList.clear();
                filteredExistingItemsCategoryList.clear();
                filteredExisitngItemsKeyIDList.clear();

                findItem = searchItem_editText.getText().toString();
                for (int j = 0; j < exisitngItemsNamesList.size(); j++) {
                    searchItem = exisitngItemsNamesList.get(j) + " " + exisitngItemsSerialNoList.get(j);

                    int searchMeLength = searchItem.length();
                    int findMeLength = findItem.length();
//                    boolean foundIt = false;
                    for (int i = 0;
                         i <= (searchMeLength - findMeLength);
                         i++) {
                        if (searchItem.regionMatches(true, i, findItem, 0, findMeLength)) {

                            filteredExisitngItemsNamesList.add(exisitngItemsNamesList.get(j));
                            filteredExisitngItemsSerialNoList.add(exisitngItemsSerialNoList.get(j));
                            filteredExistingItemsLastActiveList.add(existingItemsLastActiveList.get(j));
                            filteredExistingItemsImageUrlList.add(existingItemsImageUrlList.get(j));
                            filteredExistingItemsPriceList.add(existingItemsPriceList.get(j));
                            filteredExistingItemsCategoryList.add(existingItemsCategoryList.get(j));
                            filteredExisitngItemsKeyIDList.add(exisitngItemsKeyIDList.get(j));

                            adapter_itemList_alert_dialog.notifyDataSetChanged();

                            break;
                        }
                    }
                }
                getFilteredItems();
            }
        });
    }

    private void getFilteredItems() {
        if (filteredExisitngItemsNamesList.size() > 10) {
            for (int i = 0; i < 10; i++) {
                lessExisitngItemsNamesList.add(filteredExisitngItemsNamesList.get(i));
                lessExisitngItemsSerialNoList.add(filteredExisitngItemsSerialNoList.get(i));
                lessExistingItemsLastActiveList.add(filteredExistingItemsLastActiveList.get(i));
                lessExistingItemsImageUrlList.add(filteredExistingItemsImageUrlList.get(i));
                lessExistingItemsPriceList.add(filteredExistingItemsPriceList.get(i));
                lessExistingItemsCategoryList.add(filteredExistingItemsCategoryList.get(i));
                lessExisitngItemsKeyIDList.add(filteredExisitngItemsKeyIDList.get(i));
            }
        } else {
            for (int i = 0; i < filteredExisitngItemsNamesList.size(); i++) {
                lessExisitngItemsNamesList.add(filteredExisitngItemsNamesList.get(i));
                lessExisitngItemsSerialNoList.add(filteredExisitngItemsSerialNoList.get(i));
                lessExistingItemsLastActiveList.add(filteredExistingItemsLastActiveList.get(i));
                lessExistingItemsImageUrlList.add(filteredExistingItemsImageUrlList.get(i));
                lessExistingItemsPriceList.add(filteredExistingItemsPriceList.get(i));
                lessExistingItemsCategoryList.add(filteredExistingItemsCategoryList.get(i));
                lessExisitngItemsKeyIDList.add(filteredExisitngItemsKeyIDList.get(i));
            }
        }

        adapter_itemList_alert_dialog = new Adapter_itemList_alert_dialog(Sale.this, lessExisitngItemsNamesList, lessExisitngItemsSerialNoList, lessExisitngItemsKeyIDList, lessExistingItemsPriceList, lessExistingItemsCategoryList, lessExistingItemsLastActiveList, lessExistingItemsImageUrlList, itemName_textView, itemID_textView, itemPriceCurrency_textView, itemPrice_textView, itemLastActive_textView, itemImage_imageView, null, itemList_alert_dialog);
        itemList_recyclerView.setAdapter(adapter_itemList_alert_dialog);
        onScrollListner(filteredExisitngItemsNamesList, filteredExisitngItemsSerialNoList, filteredExistingItemsLastActiveList, filteredExistingItemsImageUrlList, filteredExistingItemsPriceList, filteredExistingItemsCategoryList, filteredExisitngItemsKeyIDList);

    }

    private void gettingHistoryList(String itemID) {
        itemHistoryRef = FirebaseDatabase.getInstance().getReference("Item_history/" + itemID);

        orderQuery = itemHistoryRef.orderByChild("Timestamp");
        orderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dateList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        dateList.add(dataSnapshot1.child("Date").getValue().toString());

                    }
                    Collections.reverse(dateList);
                    lastActiveDatelist.add(dateList.get(0));


                } else {
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
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        voucher_number_list.add(dataSnapshot1.child("Voucher_number").getValue().toString());
                        Voucher_amount_list.add(dataSnapshot1.child("Voucher_amount").getValue().toString());

                    }
                    pd.dismissProgressBar(Sale.this);
                } else {
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
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printTcp();
            }
        });

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

                Intent intent = new Intent(Sale.this, Customer_details.class);
                startActivityForResult(intent, CUSTOMER_ACTIVITY_REQUEST_CODE);
            }
        });

        item_add_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sale.this, Item_detail.class);
                intent.putExtra("ADD_ITEM", "FALSE");
                startActivityForResult(intent, ITEM_ACTIVITY_REQUEST_CODE);
            }
        });

        cash_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cash_checkbox.isChecked()) {
                    cash_editText.setVisibility(View.VISIBLE);

                }
                if (!cash_checkbox.isChecked()) {
                    cash_editText.setVisibility(View.GONE);

                }
            }
        });

        voucher_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voucher_checkbox.isChecked()) {
                    searchForVoucher_textView.setVisibility(View.VISIBLE);

                }
                if (!voucher_checkbox.isChecked()) {
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
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(), "date picker");

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
                startActivity(Intent.createChooser(it, "Choose Mail App"));
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

    private void onScrollCustomerListner(List<String> exisitngCustomerList, List<String> exisitngCustomerIDList, List<String> exisitngCustomerKeyIDList, List<String> existingCustomerPhnoList, List<String> existingCustomerDobList, List<String> existingCustomerAddressList, List<String> existingCustomerEmailList, List<String> existingCustomerImageUrlList) {
        itemList_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentCustomer = customerList_recyclerView.getLayoutManager().getChildCount();
                totalCustomer = customerList_recyclerView.getLayoutManager().getItemCount();
                scrollOutCustomer = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (isScrolling && (currentCustomer + scrollOutCustomer == totalCustomer)) {
                    isScrolling = false;
                    fetchDataforCustomerRecyclerView(exisitngCustomerList, exisitngCustomerIDList, existingCustomerPhnoList, existingCustomerDobList, existingCustomerAddressList, existingCustomerEmailList, exisitngCustomerKeyIDList, existingCustomerImageUrlList);
                }
            }
        });
    }

    private void onScrollListner(List<String> exisitngItemsNamesList, List<String> exisitngItemsSerialNoList, List<String> existingItemsLastActiveList, List<String> existingItemsImageUrlList, List<String> existingItemsPriceList, List<String> existingItemsCategoryList, List<String> exisitngItemsKeyIDList) {
        itemList_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = itemList_recyclerView.getLayoutManager().getChildCount();
                totalItems = itemList_recyclerView.getLayoutManager().getItemCount();
                scrollOutItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    fetchDataforRecyclerView(exisitngItemsNamesList, exisitngItemsSerialNoList, existingItemsLastActiveList, existingItemsImageUrlList, existingItemsPriceList, existingItemsCategoryList, exisitngItemsKeyIDList);
                }
            }
        });
    }

    private void fetchDataforRecyclerView(List<String> exisitngItemsNamesList, List<String> exisitngItemsSerialNoList, List<String> existingItemsLastActiveList, List<String> existingItemsImageUrlList, List<String> existingItemsPriceList, List<String> existingItemsCategoryList, List<String> exisitngItemsKeyIDList) {
        if (itemDatafullyloaded) {

        } else {
            alert_rbs_itemlist_progressBar.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int size = lessExisitngItemsNamesList.size() + 10;
                    for (int i = lessExisitngItemsNamesList.size(); i < size; i++) {
                        if (i < exisitngItemsNamesList.size()) {
                            lessExisitngItemsNamesList.add(exisitngItemsNamesList.get(i));
                            lessExisitngItemsSerialNoList.add(exisitngItemsSerialNoList.get(i));
                            lessExistingItemsLastActiveList.add(existingItemsLastActiveList.get(i));
                            lessExistingItemsImageUrlList.add(existingItemsImageUrlList.get(i));
                            lessExistingItemsPriceList.add(existingItemsPriceList.get(i));
                            lessExistingItemsCategoryList.add(existingItemsCategoryList.get(i));
                            lessExisitngItemsKeyIDList.add(exisitngItemsKeyIDList.get(i));
                        }

                    }
                    adapter_itemList_alert_dialog.notifyDataSetChanged();
                    alert_rbs_itemlist_progressBar.setVisibility(View.GONE);
                    if (lessExisitngItemsNamesList.size() == exisitngItemsNamesList.size()) {
                        itemDatafullyloaded = true;
                    }
                }
            }, 3000);
        }

    }

    private void onCustomerRecyclerViewScrollListner(List<String> exisitngCustomerList, List<String> exisitngCustomerIDList, List<String> exisitngCustomerKeyIDList, List<String> existingCustomerPhnoList, List<String> existingCustomerDobList, List<String> existingCustomerAddressList, List<String> existingCustomerEmailList, List<String> existingCustomerImageUrlList) {
        customerList_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isCustomerScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentCustomer = customerList_recyclerView.getLayoutManager().getChildCount();
                totalCustomer = customerList_recyclerView.getLayoutManager().getItemCount();
                scrollOutCustomer = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (isCustomerScrolling && (currentCustomer + scrollOutCustomer == totalCustomer)) {
                    isCustomerScrolling = false;
                    fetchDataforCustomerRecyclerView(exisitngCustomerList, exisitngCustomerIDList, existingCustomerPhnoList, existingCustomerDobList, existingCustomerAddressList, existingCustomerEmailList, exisitngCustomerKeyIDList, existingCustomerImageUrlList);
                }
            }
        });
    }

    private void fetchDataforCustomerRecyclerView(List<String> exisitngCustomerList, List<String> exisitngCustomerIDList, List<String> existingCustomerPhnoList, List<String> existingCustomerDobList, List<String> existingCustomerAddressList, List<String> existingCustomerEmailList, List<String> exisitngCustomerKeyIDList, List<String> existingCustomerImageUrlList) {
        if (customerDatafullyloaded) {

        } else {
            alert_rbs_customerlist_progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int size = lessExisitngCustomerList.size() + 10;
                    for (int i = lessExisitngCustomerList.size(); i < size; i++) {
                        if (i < exisitngCustomerList.size()) {
                            lessExisitngCustomerList.add(exisitngCustomerList.get(i));
                            lessExisitngCustomerIDList.add(exisitngCustomerIDList.get(i));
                            lessExistingCustomerPhnoList.add(existingCustomerPhnoList.get(i));
                            lessExistingCustomerDobList.add(existingCustomerDobList.get(i));
                            lessExistingCustomerAddressList.add(existingCustomerAddressList.get(i));
                            lessExistingCustomerEmailList.add(existingCustomerEmailList.get(i));
                            lessExisitngCustomerKeyIDList.add(exisitngCustomerKeyIDList.get(i));
                            lessExistingCustomerImageUrlList.add(existingCustomerImageUrlList.get(i));
                        }

                    }
                    adapter_customerList_alert_dialog.notifyDataSetChanged();
                    alert_rbs_customerlist_progressBar.setVisibility(View.GONE);
                    if (lessExisitngCustomerList.size() == exisitngCustomerList.size()) {
                        customerDatafullyloaded = true;
                    }

                }
            }, 3000);
        }

    }

    private boolean validateFields() {
        boolean valid = true;
        if (customerName_textView.getText().toString().equals("Search for customer ...")) {
            Toast.makeText(this, "Please select Customer!", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (itemName_textView.getText().toString().equals("Search for item ...")) {
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

        if (paid_editText.getText().toString().isEmpty()) {
            paid_editText.setError("Please enter paid amount");
            valid = false;
        }

        return valid;
    }

    private void detailsSubmit() {

        pd1.showProgressBar(Sale.this);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Sale.this.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            UniquePushID.getInstance().generateUniquePushID();
            String key = UniquePushID.getInstance().getUniquePushID();

            if (!rbsItemDetails.getCheck().equals("Sale new item")) {
                itemKeyID = rbsItemDetails.getKey();
            }

            if (rbsCustomerDetails.getCheck().equals("New customer")) {
                customerKeyID = rbsCustomerDetails.getKey();
            }

            reference.child("Sale_list").child(key).child("Customer_keyID").setValue(customerKeyID);
            reference.child("Sale_list").child(key).child("Item_keyID").setValue(itemKeyID);
            reference.child("Sale_list").child(key).child("Sale_price").setValue(sale_price_editText.getText().toString());
            reference.child("Sale_list").child(key).child("Date").setValue(date_textView.getText().toString());

            reference.child("Sale_list").child(key).child("Paid").setValue(paid_editText.getText().toString());

            reference.child("Sale_list").child(key).child("key_id").setValue(key);
            reference.child("Sale_list").child(key).child("added_by").setValue(firebaseAuthUID);

            if (rbsItemDetails.getCheck().equals("Sale existing item")) {
                if (rbsCustomerDetails.getCheck().equals("New customer")) {
                    rbsCustomerDetails.uploadCustomerDetails(Sale.this);
                }
                rbsItemDetails.switchStockSale(rbsCustomerDetails.getKey(), FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
            if (rbsItemDetails.getCheck().equals("Sale new item")) {
                rbsItemDetails.uploadNewItemDetails(Sale.this);
                if (rbsCustomerDetails.getCheck().equals("New customer")) {
                    rbsCustomerDetails.uploadCustomerDetails(Sale.this);
                }
            }

            System.out.println(rbsItemDetails.getKey());
            System.out.println(key);
            System.out.println(UserDetails.getInstance().getShopNmae());
            System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());
            System.out.println(UserDetails.getInstance().getShopLogo());
            System.out.println("Seller");
            System.out.println(rbsCustomerDetails.getCustomerName());
            System.out.println(rbsCustomerDetails.getKey());
            System.out.println("Buyer");
            System.out.println(rbsCustomerDetails.getFirstImageUrl());
            System.out.println("Trade");
            System.out.println(date.getTime());
            System.out.println(date_textView.getText().toString());


            new Customer_history_class().twelveValues(rbsCustomerDetails.getKey(), key, date_textView.getText().toString(), rbsItemDetails.getItemCategory(), String.valueOf(rbsItemDetails.getFirstImageUri()), rbsItemDetails.getKey(), rbsItemDetails.getItemName(), rbsItemDetails.getItemID(), "Buy", UserDetails.getInstance().getShopLogo(), FirebaseAuth.getInstance().getCurrentUser().getUid(), UserDetails.getInstance().getShopNmae(), date.getTime());
            pd1.dismissProgressBar(Sale.this);
            sendingdialog.show();

        } else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
            pd1.dismissProgressBar(Sale.this);
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = (Date) formatter.parse(dayOfMonth + "-" + month + "-" + year);
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
                String itemlstactive_returnString = data.getStringExtra("Last_Active");
                // set text view with string

                itemKeyID = itemkeyid_returnString;
                itemCategory = itemcategory_returnString;

                itemName_textView.setText(itemname_returnString);
                itemID_textView.setText(itemid_returnString);
                itemName_textView.setTextColor(getResources().getColor(R.color.gradientDarkBlue));
                itemPrice_textView.setText(itemprice_returnString);
                itemLastActive_textView.setText(itemlstactive_returnString);
                itemImage_imageView.setImageURI(rbsItemDetails.getImageUrlList().get(0));

                itemID_textView.setVisibility(View.VISIBLE);
                itemPriceCurrency_textView.setVisibility(View.VISIBLE);
                itemPrice_textView.setVisibility(View.VISIBLE);
                itemImage_imageView.setVisibility(View.VISIBLE);
                itemLastActive_linearLayout.setVisibility(View.VISIBLE);

                rbsItemDetails.setCheck("Sale new item");

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
                email_title_textview.setVisibility(View.VISIBLE);
                customerPhno_textView.setText(phone_no_returnString);
                customerPhno_textView.setVisibility(View.VISIBLE);
                phone_no_title_textview.setVisibility(View.VISIBLE);
                customerID_textView.setText(id_returnString);
                customerID_textView.setVisibility(View.VISIBLE);
                id_title_textview.setVisibility(View.VISIBLE);
                customerID_linearLayout.setVisibility(View.VISIBLE);

                rbsCustomerDetails.setKey(reference.push().getKey());
                rbsCustomerDetails.setCheck("New customer");

            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}

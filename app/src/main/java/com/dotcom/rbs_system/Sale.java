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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
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
import com.dotcom.rbs_system.Classes.InvoiceNumberGenerator;
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

public class Sale extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    RBSItemDetails rbsItemDetails;
    RBSCustomerDetails rbsCustomerDetails;

    Dialog confirmation_alert;
    TextView yes_btn_textview, cancel_btn_textview;

    Handler handler = new Handler();
    Handler handler2 = new Handler();

    AsyncTask asyncTask = null;

    String searchItem, findItem;
    String searchCustomer, findCustomer;

    String itemname_returnString;
    String itemid_returnString;
    String itemkeyid_returnString;
    String itemcategory_returnString;
    String itemlimage_returnString;

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

    TextView customerName_textView, customerEmail_textView, customerPhno_textView, customerID_textView;
    TextView viewCustomerDetails_textView;
    TextView invoiceNo_TextView;

    TextView email_title_textview, id_title_textview, phone_no_title_textview;

    Progress_dialog pd;
    String customerEmail_returnString = "false", customerPushID,customerUID;

    DatabaseReference reference, itemHistoryRef, emailToIDRef,emailToUIDRef,customerPurchaseHistoryRef;
    DatabaseReference existingCustomersRef, existingItemsRef, existingVoucherRef;
    Query orderQuery;

    TextView itemLastActive_textView, submit_textView;

    EditText searchItem_editText, searchCustomer_editText;

    ImageButton back_btn, print_btn;
    ImageButton searchItem_imageBtn;

    LinearLayout toggling_linear, itemLastActive_linearLayout, customerID_linearLayout;
    Boolean item_btn, customer;

    ImageView itemImage_imageView, customerImage_imageView;

    TextView itemName_textView, itemID_textView, itemPriceCurrency_textView, itemPrice_textView, customer_add_textView, item_add_textView;
    TextView viewItemDetails_textView;

    TextView suggest_price_TextView;
    TextView date_textView;

    String firebaseAuthUID;
    String customerKeyID, itemKeyID, itemCategory;

    List<String> exisitngCustomerList, exisitngCustomerIDList, exisitngCustomerKeyIDList, existingCustomerPhnoList, existingCustomerDobList, existingCustomerAddressList, existingCustomerEmailList, existingCustomerImageUrlList;
    List<String> lessExisitngCustomerList, lessExisitngCustomerIDList, lessExistingCustomerPhnoList, lessExistingCustomerDobList, lessExistingCustomerAddressList, lessExistingCustomerEmailList, lessExisitngCustomerKeyIDList, lessExistingCustomerImageUrlList;
    List<String> filteredExisitngCustomerList, filteredExisitngCustomerIDList, filteredExisitngCustomerKeyIDList, filteredExistingCustomerPhnoList, filteredExistingCustomerDobList, filteredExistingCustomerAddressList, filteredExistingCustomerEmailList, filteredExistingCustomerImageUrlList;
    List<String> exisitngItemsNamesList, exisitngItemsSerialNoList, exisitngItemsKeyIDList, existingItemsPriceList, existingItemsCategoryList, existingItemsLastActiveList, existingItemsImageUrlList;
    List<String> filteredExisitngItemsNamesList, filteredExisitngItemsSerialNoList, filteredExisitngItemsKeyIDList, filteredExistingItemsPriceList, filteredExistingItemsCategoryList, filteredExistingItemsLastActiveList, filteredExistingItemsImageUrlList;
    List<String> lessExisitngItemsNamesList, lessExisitngItemsSerialNoList, lessExisitngItemsKeyIDList, lessExistingItemsPriceList, lessExistingItemsCategoryList, lessExistingItemsLastActiveList, lessExistingItemsImageUrlList;
    List<String> voucher_number_list, Voucher_amount_list;
    List<String> dateList, lastActiveDatelist;

    EditText paid_editText;

    Date date;

    Progress_dialog pd1, pd2, pd3;

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
        try {
            generateInvoiceNo();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

        confirmation_alert = new Dialog(this);
        confirmation_alert.setContentView(R.layout.exit_confirmation_alert);
        yes_btn_textview = confirmation_alert.findViewById(R.id.yes_btn_textview);
        cancel_btn_textview = confirmation_alert.findViewById(R.id.cancel_btn_textview);

        rbsItemDetails.clearData();
        rbsCustomerDetails.clearData();

        pd = new Progress_dialog();
        item_btn = false;
        customer = false;

        pd1 = new Progress_dialog();
        pd2 = new Progress_dialog();
        pd3 = new Progress_dialog();

        ////

        itemList_alert_dialog = new Dialog(this);
        itemList_alert_dialog.setContentView(R.layout.alert_rbs_itemlist);

        searchItem_editText = itemList_alert_dialog.findViewById(R.id.searchItem_editText);

        itemList_recyclerView = itemList_alert_dialog.findViewById(R.id.itemList_recyclerView);
        itemList_recyclerView.setLayoutManager(new GridLayoutManager(Sale.this, 1));

        alert_rbs_itemlist_progressBar = itemList_alert_dialog.findViewById(R.id.alert_rbs_itemlist_progressBar);

        searchForItem_cardView = findViewById(R.id.searchForItem_cardView);
        searchForCustomer_cardView = findViewById(R.id.searchForCustomer_cardView);

        reference = FirebaseDatabase.getInstance().getReference();
        emailToIDRef = FirebaseDatabase.getInstance().getReference("email_to_id");
        emailToUIDRef = FirebaseDatabase.getInstance().getReference("email_to_uid");
        customerPurchaseHistoryRef = FirebaseDatabase.getInstance().getReference("Customer_purchase_history");

        toggling_linear = findViewById(R.id.toggling_linear);

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

        paid_editText = findViewById(R.id.paid_editText);

        itemLastActive_linearLayout = findViewById(R.id.itemLastActive_linearLayout);

        customerID_linearLayout = findViewById(R.id.customerID_linearLayout);

        itemName_textView = findViewById(R.id.itemName_textView);
        itemID_textView = findViewById(R.id.itemID_textView);
        itemPriceCurrency_textView = findViewById(R.id.itemPriceCurrency_textView);
        itemPriceCurrency_textView.setText(Currency.getInstance().getCurrency());
        itemPrice_textView = findViewById(R.id.itemPrice_textView);
        viewItemDetails_textView = findViewById(R.id.viewItemDetails_textView);

        customerName_textView = findViewById(R.id.customerName_textView);
        customerEmail_textView = findViewById(R.id.customerEmail_textView);
        customerID_textView = findViewById(R.id.customerID_textView);
        customerPhno_textView = findViewById(R.id.customerPhno_textView);
        viewCustomerDetails_textView = findViewById(R.id.viewCustomerDetails_textView);

        invoiceNo_TextView = (TextView) findViewById(R.id.invoiceNo_TextView);

        itemLastActive_textView = findViewById(R.id.itemLastActive_textView);

        itemLastActive_textView = findViewById(R.id.itemLastActive_textView);

        customer_add_textView = findViewById(R.id.customer_add_textView);

        itemImage_imageView = findViewById(R.id.itemImage_imageView);
        customerImage_imageView = findViewById(R.id.customerImage_imageView);

        //////

        customerList_alert_dialog = new Dialog(this);
        customerList_alert_dialog.setContentView(R.layout.alert_rbs_customerlist);

        searchCustomer_editText = customerList_alert_dialog.findViewById(R.id.searchCustomer_editText);

        customerList_recyclerView = customerList_alert_dialog.findViewById(R.id.customerList_recyclerView);
        customerList_recyclerView.setLayoutManager(new GridLayoutManager(Sale.this, 1));

        alert_rbs_customerlist_progressBar = customerList_alert_dialog.findViewById(R.id.alert_rbs_customerlist_progressBar);

        //////

        ////////////////////////
        suggest_price_TextView = findViewById(R.id.suggest_price_TextView);
        item_add_textView = findViewById(R.id.item_add_textView);

        email_title_textview = findViewById(R.id.email_title_textview);
        id_title_textview = findViewById(R.id.id_title_textview);
        phone_no_title_textview = findViewById(R.id.phone_no_title_textview);


        submit_textView = findViewById(R.id.submit_textView);
        date_textView = findViewById(R.id.date_textView);


        searchItem_imageBtn = findViewById(R.id.searchItem_imageBtn);
        back_btn = findViewById(R.id.back_btn);

        dateList = new ArrayList<>();
        lastActiveDatelist = new ArrayList<>();


        /////Firebase config
        firebaseAuthUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        existingCustomersRef = FirebaseDatabase.getInstance().getReference("Customer_list");
        existingItemsRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        existingVoucherRef = FirebaseDatabase.getInstance().getReference("Voucher_List");
        existingVoucherRef = FirebaseDatabase.getInstance().getReference("Voucher_List/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        ///////

        date = Calendar.getInstance().getTime();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);
        date_textView.setText(currentDateString);

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = formatter.parse(date.getDay() + "-" + date.getMonth() + "-" + date.getYear());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void generateInvoiceNo() throws ParseException {
        invoiceNo_TextView.setText(new InvoiceNumberGenerator().generateNumber());
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

        return "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                "[L]\n" +
                "[L]Invoice Printed" +
                "[C]<u type='double'>" + format.format(new Date()) + "</u>\n" +
                "[C]\n" +
                "[L]Invoice no: 12345\n" +
                "[L]\n" +
                "[C]<font color='bg-black'>================================</font>\n" +
                "[L]\n" +
                "[C]<font color='black' size='wide'> CUSTOMER </font>\n" +
                "[L]\n" +
                "[L]Name:" + customerName_textView.getText().toString() + "\n" +
                "[L]\n" +
                "[L]Email:" + customerEmail_textView.getText().toString() + "\n" +
                "[L]\n" +
                "[L]ID:" + customerID_textView.getText().toString() + "\n" +
                "[L]\n" +
                "[L]Phone no:" + customerPhno_textView.getText().toString() + "\n" +
                "[C]--------------------------------\n" +
                "[L]\n" +
                "[C]<font color='black' size='wide'> ITEM </font>\n" +
                "[L]\n" +
                "[L]Name:" + itemName_textView.getText().toString() + "\n" +
                "[L]\n" +
                "[L]Serial no:" + itemID_textView.getText().toString() + "\n" +
                "[L]\n" +
                "[C]================================\n" +
                "[L]\n" +
                "[L]Paid Amount: [R]" + Currency.getInstance().getCurrency() + paid_editText.getText().toString() + "\n" +
                "[L]\n" +
                "[L]Sale Date: [R]" + date_textView.getText().toString() + "\n" +
                "[C]\n" +
                "[C]<font color='bg-black'> -------------------------------- </font>\n" +
                "[C]\n" +
                "[L]<font color='black' size='tall'> Shopkeeper Terms & Conditions </font>\n" +
                "[C]\n" +
                "[L]1.This is the first condition. 2. This is the second condition\n" +
                "[C]\n" +
                "[L]<font color='black' size='tall'> Buy Local Terms & Conditions </font>\n" +
                "[C]\n" +
                "[L]1.This is the first condition. 2. This is the second condition\n" +
                "[L]\n" +
                "[L]\n" +
                "[L]\n";

    }

    private void checkForItemSell() {
        if (getIntent().getStringExtra("ITEM_SELL_CHECK").equals("TRUE")) {

            itemname_returnString = getIntent().getStringExtra("Item_name");
            itemid_returnString = getIntent().getStringExtra("Item_id");
            itemkeyid_returnString = getIntent().getStringExtra("Item_keyid");
            itemcategory_returnString = getIntent().getStringExtra("Item_category");
            itemlimage_returnString = getIntent().getStringExtra("Item_image");
            String itemprice_returnString = getIntent().getStringExtra("Item_price");
            String itemlstactive_returnString = getIntent().getStringExtra("Last_Active");
            customerEmail_returnString = getIntent().getStringExtra("Customer_email");
            // set text view with string

            String paidAmount_returnString = getIntent().getStringExtra("Paid_amount");
            paid_editText.setText(paidAmount_returnString);

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
            itemName_textView.setTextColor(getResources().getColor(R.color.gradientDarkBlue));
            itemID_textView.setVisibility(View.VISIBLE);
            itemPriceCurrency_textView.setVisibility(View.VISIBLE);
            itemPrice_textView.setVisibility(View.VISIBLE);
            itemImage_imageView.setVisibility(View.VISIBLE);
            itemLastActive_linearLayout.setVisibility(View.VISIBLE);
            itemLastActive_textView.setVisibility(View.VISIBLE);
            viewItemDetails_textView.setVisibility(View.VISIBLE);

            rbsItemDetails.setFirstImageUri(Uri.parse(itemlimage_returnString));


            itemImage_imageView.setVisibility(View.VISIBLE);
            viewItemDetails_textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Sale.this, Item_history.class);
                    intent.putExtra("ITEM_ID", itemkeyid_returnString);
                    intent.putExtra("ITEM_CATEGORY", itemcategory_returnString);
                    startActivity(intent);
                }
            });

            if (!customerEmail_returnString.equals("false")) {


                emailToIDRef.child(customerEmail_returnString).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        customerPushID = snapshot.getValue().toString();
                        existingCustomersRef.child(customerPushID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                customerName_textView.setText(snapshot.child("Name").getValue().toString());
                                customerEmail_textView.setText(snapshot.child("Email").getValue().toString());
                                customerID_textView.setText(snapshot.child("ID").getValue().toString());
                                customerPhno_textView.setText(snapshot.child("Phone_no").getValue().toString());

                                Picasso.get().load(snapshot.child("ID_Image_urls").child("image_1").getValue().toString()).into(customerImage_imageView);

                                //////

                                customerName_textView.setVisibility(View.VISIBLE);
                                customerName_textView.setTextColor(getResources().getColor(R.color.gradientDarkBlue));

                                customerEmail_textView.setVisibility(View.VISIBLE);
                                customerID_textView.setVisibility(View.VISIBLE);
                                customerPhno_textView.setVisibility(View.VISIBLE);
                                customerImage_imageView.setVisibility(View.VISIBLE);

                                customerID_linearLayout.setVisibility(View.VISIBLE);

                                customerKeyID = customerPushID;
                                rbsCustomerDetails.setCheck("Existing customer");
                                rbsCustomerDetails.setKey(customerKeyID);
                                rbsCustomerDetails.setCustomerName(snapshot.child("Name").getValue().toString());
                                rbsCustomerDetails.setFirstImageUrl(snapshot.child("ID_Image_urls").child("image_1").getValue().toString());

                                emailToUIDRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        customerUID = snapshot.child(customerEmail_returnString).getValue().toString();
                                        Toast.makeText(Sale.this, customerUID, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                viewCustomerDetails_textView.setVisibility(View.VISIBLE);
                                viewCustomerDetails_textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Sale.this, Customer_history.class);
                                        intent.putExtra("CUSTOMER_ID", customerKeyID);
                                        startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

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
                            exisitngItemsKeyIDList.add(String.valueOf(dataSnapshot2.getKey()));
                            gettingHistoryList(String.valueOf(dataSnapshot2.getKey()));

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

            @SuppressLint("NotifyDataSetChanged")
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

        adapter_itemList_alert_dialog = new Adapter_itemList_alert_dialog(Sale.this, lessExisitngItemsNamesList, lessExisitngItemsSerialNoList, lessExisitngItemsKeyIDList, lessExistingItemsPriceList, lessExistingItemsCategoryList, lessExistingItemsLastActiveList, lessExistingItemsImageUrlList, itemName_textView, itemID_textView, itemPriceCurrency_textView, itemPrice_textView, itemLastActive_textView, itemImage_imageView, viewItemDetails_textView, itemList_alert_dialog);
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

        cancel_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmation_alert.dismiss();
            }
        });


        yes_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        searchForItem_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemList_alert_dialog.show();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                itemList_alert_dialog.getWindow().setLayout(width - 10, height - 800); //Controlling width and height.
            }
        });

        searchForCustomer_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerList_alert_dialog.show();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                customerList_alert_dialog.getWindow().setLayout(width - 10, height - 800); //Controlling width and height.
            }
        });

        customer_add_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Sale.this, Add_Customer.class);
                startActivityForResult(intent, CUSTOMER_ACTIVITY_REQUEST_CODE);
            }
        });

        item_add_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sale.this, Add_item.class);
                intent.putExtra("ADD_ITEM", "FALSE");
                startActivityForResult(intent, ITEM_ACTIVITY_REQUEST_CODE);
            }
        });


        date_textView.setOnClickListener(new View.OnClickListener() {
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


        submit_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateFields())
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
                    fetchDataforRecyclerView();
                }
            }
        });
    }

    private void fetchDataforRecyclerView() {
        if (itemDatafullyloaded) {

        } else {
            alert_rbs_itemlist_progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void run() {
                    int size = lessExisitngItemsNamesList.size() + 2;
                    for (int i = lessExisitngItemsNamesList.size(); i < size; i++) {
                        if (i < exisitngItemsNamesList.size()) {
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
        if (customerName_textView.getText().toString().equals("Search for customer...")) {
            Toast.makeText(this, "Please select Customer!", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (itemName_textView.getText().toString().equals("Search for item...")) {
            Toast.makeText(this, "Please select Item!", Toast.LENGTH_SHORT).show();
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

            itemKeyID = rbsItemDetails.getKey();

            customerKeyID = rbsCustomerDetails.getKey();

            reference.child("Sale_list").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).child("Customer_keyID").setValue(customerKeyID);
            reference.child("Sale_list").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).child("Date").setValue(date_textView.getText().toString());
            reference.child("Sale_list").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).child("Item_keyID").setValue(itemKeyID);
            reference.child("Sale_list").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).child("invoice_no").setValue(invoiceNo_TextView.getText().toString());
            reference.child("Sale_list").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).child("Paid").setValue(paid_editText.getText().toString());
            reference.child("Sale_list").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).child("added_by").setValue(firebaseAuthUID);
            reference.child("Sale_list").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).child("key_id").setValue(key);

            System.out.println("called---------");
            if (rbsItemDetails.getCheck().equals("Sale existing item")) {
                System.out.println("Sale existing item");
                if (rbsCustomerDetails.getCheck().equals("New customer")) {
                    System.out.println("New customer");
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


            new Customer_history_class().twelveValues(rbsCustomerDetails.getKey(), key, date_textView.getText().toString(), rbsItemDetails.getItemCategory(), String.valueOf(rbsItemDetails.getFirstImageUri()), rbsItemDetails.getKey(), rbsItemDetails.getItemName(), rbsItemDetails.getItemID(), "Buy", UserDetails.getInstance().getShopLogo(), FirebaseAuth.getInstance().getCurrentUser().getUid(), UserDetails.getInstance().getShopName(), date.getTime());
            new Customer_history_class().uploadCustomerImagetoItemHistory(RBSItemDetails.getInstance().getKey(), UniquePushID.getInstance().getUniquePushID(), rbsCustomerDetails.getFirstImageUrl());
            pd1.dismissProgressBar(Sale.this);

            Intent intent = new Intent(Sale.this, Invoice_preview.class);
            intent.putExtra("Date", String.valueOf(date_textView.getText().toString()));
            intent.putExtra("Customer_Name", String.valueOf(customerName_textView.getText().toString()));
            intent.putExtra("Customer_Email", String.valueOf(customerEmail_textView.getText().toString()));
            intent.putExtra("Customer_ID", String.valueOf(customerID_textView.getText().toString()));
            intent.putExtra("Customer_Ph_No", String.valueOf(customerPhno_textView.getText().toString()));
            intent.putExtra("Item_Name", String.valueOf(itemName_textView.getText().toString()));
            intent.putExtra("Item_ID", String.valueOf(itemID_textView.getText().toString()));
            intent.putExtra("Item_Price_Currency", String.valueOf(itemPriceCurrency_textView.getText().toString()));
            intent.putExtra("Item_Price", String.valueOf(itemPrice_textView.getText().toString()));
            intent.putExtra("Paid_Amount", String.valueOf(paid_editText.getText().toString()));
            intent.putExtra("Invoice_No", String.valueOf(invoiceNo_TextView.getText().toString()));
            intent.putExtra("Invoice_Type", String.valueOf("Sell to customer"));

            if (!customerEmail_returnString.equals("false")) {
                customerPurchaseHistoryRef.child(customerUID).child(key).child("shopkeeper_name").setValue(UserDetails.getInstance().getShopName());
                customerPurchaseHistoryRef.child(customerUID).child(key).child("shopkeeper_image").setValue(UserDetails.getInstance().getShopLogo());
                customerPurchaseHistoryRef.child(customerUID).child(key).child("shopkeeper_keyId").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                customerPurchaseHistoryRef.child(customerUID).child(key).child("item_name").setValue(itemname_returnString);
                customerPurchaseHistoryRef.child(customerUID).child(key).child("item_image").setValue(itemlimage_returnString);
                customerPurchaseHistoryRef.child(customerUID).child(key).child("item_category").setValue(itemcategory_returnString);
                customerPurchaseHistoryRef.child(customerUID).child(key).child("item_keyId").setValue(itemkeyid_returnString);
                customerPurchaseHistoryRef.child(customerUID).child(key).child("item_serialno").setValue(itemid_returnString);
                customerPurchaseHistoryRef.child(customerUID).child(key).child("price").setValue(paid_editText.getText().toString());
                customerPurchaseHistoryRef.child(customerUID).child(key).child("date").setValue(date_textView.getText().toString());
            }
            finish();
            startActivity(intent);

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
            date = formatter.parse(dayOfMonth + "-" + month + "-" + year);
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

                viewItemDetails_textView.setVisibility(View.GONE);

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

                viewCustomerDetails_textView.setVisibility(View.GONE);

                rbsCustomerDetails.setKey(reference.push().getKey());
                rbsCustomerDetails.setCheck("New customer");

            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                confirmation_alert.show();

                return true;
            }
        }
        return false;
    }

}

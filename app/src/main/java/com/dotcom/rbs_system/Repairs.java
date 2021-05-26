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
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterRepairsFaultListRecyclerView;
import com.dotcom.rbs_system.Classes.Repair_details_edit;
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

public class Repairs extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    long timestamp;

    private static final int ITEM_ACTIVITY_REQUEST_CODE = 0;
    private static final int CUSTOMER_ACTIVITY_REQUEST_CODE = 0;

    double incremantalAmount;
    double incremantalPendingAmount;

    TextView customer_textView, itemName_textView,customer_add_textview,item_add_textView;

    Repair_details_edit repair_details_edit_obj;

    DatabaseReference existingCustomersRef, existingItemsRef;
    DatabaseReference reference;
    DatabaseReference faultListRef;
    DatabaseReference itemHistoryRef;
    DatabaseReference repairRef, repairTicketRef;
    Query orderQuery;

    Progress_dialoge pd1, pd2, pd3, pd4;
    Dialog sendingdialog;


    LinearLayout itemDetails, customerDetails, toggling_linear;
    LinearLayout changesConfirmation_linearLayout;

    ImageButton gmail_btn, sms_btn, print_btn;
    ImageButton Back_btn;
    TextView addFaults_textview,date_textview,submit_textview;
    Button btn_done;

    TextView category_textView, condition_textView, notes_textView, phno_textView, dob_textView, address_textView, email_textView;
    TextView date_text, balanceAmount_TextView;
    TextView date_textView;
    TextView ticketNo_TextView;
    CardView searchForItem_cardview, searchForCustomer_cardview;
    TextView pendingAgreed_price_textView;
    TextView confirmChanges_textView, cancleChanges_textView;
    TextView last_active_textView;

    EditText agreed_price_editText, paidAmount_editText, special_condition_editText;
    EditText pendingAgreed_price_editText;

    RecyclerView faultList_recyclerView;
    AdapterRepairsFaultListRecyclerView adapterRepairsFaultListRecyclerView;

    Boolean item_btn, customer;
    Boolean pendingPriceCheck = false;

    String firebaseAuthUID;
    String customerKeyID, itemKeyID;
    String customerName, itemName, customerID, itemID;
    String itemCategory;
    String key;

    List<String> exisitngCustomerList, exisitngCustomerIDList, exisitngCustomerKeyIDList, exisitngItemsList, exisitngItemsIDList, exisitngItemsKeyIDList;
    List<String> exisitngItemsCategoryList, existingItemsConditionsList, existingItemsNotesList, existingCustomerPhnoList, existingCustomerDobList, existingCustomerAddressList, existingCustomerEmailList;
    List<String> faultNameList, faultPriceList, faultKeyIDList;
    List<String> tempFaultNameList;
    List<String> tempFaultPriceList;
    List<String> tempFaultKeyIDList;
    List<String> dateList, lastActiveDatelist;
    List<String> pendingFaultNameList, pendingFaultPriceList, pendingFaultKeyIDList;
    List<Boolean> tempFaultRemoveCheckList;

    Date date;

    private ArrayList<SampleSearchModel> createItemsData() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i = 0; i < exisitngItemsList.size(); i++) {
            items.add(new SampleSearchModel(exisitngItemsList.get(i) + "\n(" + exisitngItemsIDList.get(i) + ")", exisitngItemsIDList.get(i), exisitngItemsList.get(i), exisitngItemsCategoryList.get(i), existingItemsConditionsList.get(i), existingItemsNotesList.get(i), lastActiveDatelist.get(i), exisitngItemsKeyIDList.get(i)));

        }

        return items;
    }

    private ArrayList<SampleSearchModel> createCustomerData() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i = 0; i < exisitngCustomerList.size(); i++) {
            items.add(new SampleSearchModel(exisitngCustomerList.get(i) + "\n(" + exisitngCustomerIDList.get(i) + ")", exisitngCustomerIDList.get(i), exisitngCustomerList.get(i), existingCustomerPhnoList.get(i), existingCustomerDobList.get(i), existingCustomerAddressList.get(i), existingCustomerEmailList.get(i), exisitngCustomerKeyIDList.get(i)));
        }

        return items;
    }

    private ArrayList<SampleSearchModel> createFaultListData() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i = 0; i < faultNameList.size(); i++) {
            items.add(new SampleSearchModel(faultNameList.get(i) + "\n(" + faultPriceList.get(i) + ")", faultKeyIDList.get(i), faultNameList.get(i), faultPriceList.get(i), null, null, null, null));

        }

        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairs);

        initialize();
        balanceAmount();
        getFaultsList();
        fetchingExisitingCustomers();
        fetchingExisitingItems();
        onClickListeners();
        historyActivity();
        editCheckAndProcess();
        item_btn = false;
        customer = false;


    }


    private void initialize() {
        repair_details_edit_obj = Repair_details_edit.getInstance();

        reference = FirebaseDatabase.getInstance().getReference();
        key = reference.push().getKey().toString();

        pd1 = new Progress_dialoge();
        pd2 = new Progress_dialoge();
        pd3 = new Progress_dialoge();
        pd4 = new Progress_dialoge();
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


        faultNameList = new ArrayList<>();
        faultPriceList = new ArrayList<>();
        faultKeyIDList = new ArrayList<>();

        tempFaultNameList = new ArrayList<>();
        tempFaultPriceList = new ArrayList<>();
        tempFaultKeyIDList = new ArrayList<>();
        tempFaultRemoveCheckList = new ArrayList<>();

        pendingFaultNameList = new ArrayList<>();
        pendingFaultPriceList = new ArrayList<>();
        pendingFaultKeyIDList = new ArrayList<>();

        dateList = new ArrayList<>();
        lastActiveDatelist = new ArrayList<>();

        faultListRef = FirebaseDatabase.getInstance().getReference("Listed_faults");

        addFaults_textview = (TextView) findViewById(R.id.addFaults_textview);

        adapterRepairsFaultListRecyclerView = new AdapterRepairsFaultListRecyclerView(Repairs.this, tempFaultNameList, tempFaultPriceList, tempFaultKeyIDList, tempFaultRemoveCheckList, pendingFaultNameList, pendingFaultPriceList, pendingFaultKeyIDList, Repairs.this, false);
        faultList_recyclerView = (RecyclerView) findViewById(R.id.faultList_recyclerView);
        faultList_recyclerView.setAdapter(adapterRepairsFaultListRecyclerView);
        faultList_recyclerView.setLayoutManager(new GridLayoutManager(Repairs.this, 1));

        date_textView = (TextView) findViewById(R.id.date_textView);
        balanceAmount_TextView = (TextView) findViewById(R.id.balanceAmount_TextView);

        agreed_price_editText = (EditText) findViewById(R.id.agreed_price_editText);
        paidAmount_editText = (EditText) findViewById(R.id.paidAmount_editText);

        special_condition_editText = (EditText) findViewById(R.id.special_condition_editText);
        pendingAgreed_price_editText = (EditText) findViewById(R.id.pendingAgreed_price_editText);


        itemDetails = (LinearLayout) findViewById(R.id.itemDetails);
        customerDetails = (LinearLayout) findViewById(R.id.customerDetails);
        toggling_linear = (LinearLayout) findViewById(R.id.toggling_linear);
        changesConfirmation_linearLayout = (LinearLayout) findViewById(R.id.changesConfirmation_linearLayout);

        exisitngCustomerList = new ArrayList<>();
        exisitngCustomerIDList = new ArrayList<>();
        exisitngCustomerKeyIDList = new ArrayList<>();
        exisitngItemsList = new ArrayList<>();
        exisitngItemsIDList = new ArrayList<>();
        exisitngItemsKeyIDList = new ArrayList<>();

        exisitngItemsCategoryList = new ArrayList<>();
        existingItemsConditionsList = new ArrayList<>();
        existingItemsNotesList = new ArrayList<>();
        existingCustomerPhnoList = new ArrayList<>();
        existingCustomerDobList = new ArrayList<>();
        existingCustomerAddressList = new ArrayList<>();
        existingCustomerEmailList = new ArrayList<>();

        submit_textview = (TextView) findViewById(R.id.submit_textview);
        date_textview = (TextView) findViewById(R.id.date_textview);
        Back_btn = (ImageButton) findViewById(R.id.Back_btn);
        date_text = (TextView) findViewById(R.id.date_of_birth_text);
        customer_add_textview = (TextView) findViewById(R.id.customer_add_textview);
        item_add_textView = (TextView) findViewById(R.id.item_add_textView);


        searchForCustomer_cardview = (CardView) findViewById(R.id.searchForCustomer_cardView);
        searchForItem_cardview = (CardView) findViewById(R.id.searchForItem_cardview);
        category_textView = (TextView) findViewById(R.id.category_textView);
        condition_textView = (TextView) findViewById(R.id.condition_textView);
        notes_textView = (TextView) findViewById(R.id.notes_textView);
        phno_textView = (TextView) findViewById(R.id.phno_textView);
        dob_textView = (TextView) findViewById(R.id.dob_textView);
        address_textView = (TextView) findViewById(R.id.vendor_address_textView);
        email_textView = (TextView) findViewById(R.id.post_code_textView);
        ticketNo_TextView = (TextView) findViewById(R.id.ticketNo_TextView);
        ticketNo_TextView.setText(key);
        pendingAgreed_price_textView = (TextView) findViewById(R.id.pendingAgreed_price_textView);
        confirmChanges_textView = (TextView) findViewById(R.id.confirmChanges_textView);
        cancleChanges_textView = (TextView) findViewById(R.id.cancleChanges_textView);
        last_active_textView = (TextView) findViewById(R.id.last_active_textView);
        customer_textView = (TextView) findViewById(R.id.customer_textView);
        itemName_textView= (TextView) findViewById(R.id.itemName_textView);

        /////Firebase config
        firebaseAuthUID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
        existingCustomersRef = FirebaseDatabase.getInstance().getReference("Customer_list");
        existingItemsRef = FirebaseDatabase.getInstance().getReference("Items");
        repairRef = FirebaseDatabase.getInstance().getReference("Repairs_list/" + firebaseAuthUID + "/" + repair_details_edit_obj.getTicketNo_TextView());
        repairTicketRef = FirebaseDatabase.getInstance().getReference("Repairs_ticket_list/" + firebaseAuthUID + "/" + repair_details_edit_obj.getTicketNo_TextView());

        date = Calendar.getInstance().getTime();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);
        date_textView.setText(currentDateString);

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = (Date) formatter.parse(Calendar.getInstance().get(Calendar.DATE) + "-" + Calendar.getInstance().get(Calendar.MONTH) + "-" + Calendar.getInstance().get(Calendar.YEAR));
            timestamp = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void fetchingExisitingCustomers() {
        pd2.showProgressBar(Repairs.this);
        exisitngCustomerList.clear();
        exisitngCustomerIDList.clear();
        existingCustomerPhnoList.clear();
        existingCustomerDobList.clear();
        existingCustomerAddressList.clear();
        existingCustomerEmailList.clear();
        exisitngCustomerKeyIDList.clear();

        existingCustomersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    exisitngCustomerList.add(String.valueOf(dataSnapshot1.child("Name").getValue()));
                    exisitngCustomerIDList.add(String.valueOf(dataSnapshot1.child("ID").getValue()));
                    existingCustomerPhnoList.add(String.valueOf(dataSnapshot1.child("Phone_no").getValue()));
                    existingCustomerDobList.add(String.valueOf(dataSnapshot1.child("DOB").getValue()));
                    existingCustomerAddressList.add(String.valueOf(dataSnapshot1.child("Address").getValue()));
                    existingCustomerEmailList.add(String.valueOf(dataSnapshot1.child("Email").getValue()));
                    exisitngCustomerKeyIDList.add(String.valueOf(dataSnapshot1.child("key_id").getValue()));
                }

                pd2.dismissProgressBar(Repairs.this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd2.dismissProgressBar(Repairs.this);
            }
        });

    }

    private void fetchingExisitingItems() {
        pd3.showProgressBar(Repairs.this);

        exisitngItemsList.clear();
        exisitngItemsIDList.clear();
        exisitngItemsCategoryList.clear();
        existingItemsConditionsList.clear();
        existingItemsNotesList.clear();
        exisitngItemsKeyIDList.clear();
        lastActiveDatelist.clear();
        dateList.clear();

        existingItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        exisitngItemsList.add(String.valueOf(dataSnapshot2.child("Item_name").getValue()));
                        exisitngItemsIDList.add(String.valueOf(dataSnapshot2.child("Item_id").getValue()));
                        exisitngItemsCategoryList.add(String.valueOf(dataSnapshot2.child("Category").getValue()));
                        existingItemsConditionsList.add(String.valueOf(dataSnapshot2.child("Condition").getValue()));
                        existingItemsNotesList.add(String.valueOf(dataSnapshot2.child("Notes").getValue()));
                        exisitngItemsKeyIDList.add(String.valueOf(dataSnapshot2.child("key_id").getValue()));
                        gettingHistoryList(String.valueOf(dataSnapshot2.child("key_id").getValue()));


                    }
                }
                pd3.dismissProgressBar(Repairs.this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd3.dismissProgressBar(Repairs.this);
            }
        });
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

    private void getFaultsList() {
        pd4.showProgressBar(Repairs.this);
        faultListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    faultNameList.add(String.valueOf(dataSnapshot1.child("Fault_name").getValue()));
                    faultPriceList.add(String.valueOf(dataSnapshot1.child("Fault_price").getValue()));
                    faultKeyIDList.add(String.valueOf(dataSnapshot1.child("key_id").getValue()));

                }
                pd4.dismissProgressBar(Repairs.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd4.dismissProgressBar(Repairs.this);
            }
        });
    }

    private void onClickListeners() {
        confirmChanges();
        cancleChanges();


        addFaults_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Repairs.this, "Search...",
                        "What are you looking for...?", null, createFaultListData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                tempFaultNameList.add(item.getName());
                                tempFaultKeyIDList.add(item.getId());
                                tempFaultPriceList.add(item.getVal1());
                                tempFaultRemoveCheckList.add(true);

                                pendingFaultNameList.add(item.getName());
                                pendingFaultKeyIDList.add(item.getId());
                                pendingFaultPriceList.add(item.getVal1());

                                adapterRepairsFaultListRecyclerView.notifyDataSetChanged();


                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        searchForCustomer_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Repairs.this, "Search...",
                        "What are you looking for...?", null, createCustomerData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                customerName = item.getName();
                                customerID = item.getId();
                                customer_textView.setText(item.getTitle());
                                phno_textView.setText(item.getVal1());
                                dob_textView.setText(item.getVal2());
                                address_textView.setText(item.getVal3());
                                email_textView.setText(item.getVal4());
                                customerKeyID = item.getVal5();
                                customer_textView.setTextColor(getResources().getColor(R.color.gradientDarkBlue));
                                customerDetails.setVisibility(View.VISIBLE);
                                customer = true;
                                if (item_btn == true && customer == true) {
                                    toggling_linear.setVisibility(View.VISIBLE);
                                }
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        searchForItem_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Repairs.this, "Search...",
                        "What are you looking for...?", null, createItemsData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   final SampleSearchModel item, int position) {
                                itemName = item.getName();
                                itemID = item.getId();
                                itemName_textView.setText(item.getTitle());
                                category_textView.setText(item.getVal1());
                                itemCategory = item.getVal1();
                                condition_textView.setText(item.getVal2());
                                notes_textView.setText(item.getVal3());
                                last_active_textView.setText(item.getVal4());
                                itemKeyID = item.getVal5();
                                itemName_textView.setTextColor(getResources().getColor(R.color.gradientDarkBlue));
                                itemDetails.setVisibility(View.VISIBLE);
                                item_btn = true;
                                if (item_btn == true && customer == true) {
                                    toggling_linear.setVisibility(View.VISIBLE);
                                }
                                dialog.dismiss();

                            }
                        }).show();
            }
        });

        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        date_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(), "date picker");

            }
        });

        customer_add_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Repairs.this, Customer_details.class);
                startActivityForResult(intent, CUSTOMER_ACTIVITY_REQUEST_CODE);
            }
        });

        item_add_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Repairs.this, Item_detail.class);
                startActivityForResult(intent, ITEM_ACTIVITY_REQUEST_CODE);
            }
        });
        print_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Repairs.this, "YEs working", Toast.LENGTH_SHORT).show();
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
//                Intent intent = new Intent(Repairs.this,Repair_details.class);
//                intent.putExtra("REPAIR_ID",repair_details_edit_obj.getTicketNo_TextView());
//                startActivity(intent);
                finish();
            }
        });


        submit_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getBooleanExtra("EDIT_CHECK", false)) {
                    if (pendingFaultNameList.size() == 0) {
                        Toast.makeText(Repairs.this, "Add new Faults", Toast.LENGTH_SHORT).show();
                    }
                    if (!pendingPriceCheck) {
                        pendingAgreed_price_editText.setError("Enter valid price");
                    }
                    if (pendingFaultNameList.size() != 0 && pendingPriceCheck) {
                        pd1.showProgressBar(Repairs.this);

                        boolean connected = false;
                        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Repairs.CONNECTIVITY_SERVICE);
                        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                            //we are connected to a network
                            connected = true;

                            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Pending_Price").setValue(pendingAgreed_price_editText.getText().toString());

                            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Pending_Faults").removeValue();
                            for (int i = 0; i < pendingFaultNameList.size(); i++) {
                                reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Pending_Faults").child("Fault_" + String.valueOf(i + 1)).child("Fault_name").setValue(pendingFaultNameList.get(i));
                                reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Pending_Faults").child("Fault_" + String.valueOf(i + 1)).child("Fault_price").setValue(pendingFaultPriceList.get(i));
                                reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Pending_Faults").child("Fault_" + String.valueOf(i + 1)).child("Fault_key").setValue(pendingFaultKeyIDList.get(i));
                            }
                            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Status").setValue("pending");

                            Toast.makeText(Repairs.this, "Submit Successfully123", Toast.LENGTH_SHORT).show();
                            sendingdialog.show();
                            pd1.dismissProgressBar(Repairs.this);
                            repair_details_edit_obj.clear();

                        } else {
                            Toast.makeText(Repairs.this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
                            connected = false;
                            pd1.dismissProgressBar(Repairs.this);
                        }
                    }

                } else {
                    if (validateFields() == true) {
                        detailsSubmit();
                    }
                }

            }
        });

    }

    private void confirmChanges() {
        confirmChanges_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pendingFaultNameList.size() != 0) {
                    repairRef.child("Pending_Faults").removeValue();
                    for (int i = 0; i < pendingFaultNameList.size(); i++) {
                        repairRef.child("Faults").child("Fault_" + String.valueOf(faultNameList.size() + 1)).child("Fault_name").setValue(pendingFaultNameList.get(i));
                        repairRef.child("Faults").child("Fault_" + String.valueOf(faultNameList.size() + 1)).child("Fault_price").setValue(pendingFaultPriceList.get(i));
                        repairRef.child("Faults").child("Fault_" + String.valueOf(faultNameList.size() + 1)).child("Fault_key").setValue(pendingFaultKeyIDList.get(i));
                    }

                    repairRef.child("Pending_Price").removeValue();
                    repairRef.child("Agreed_price").setValue(pendingAgreed_price_editText.getText().toString());
                    repairRef.child("Balance_amount").setValue(String.valueOf(Float.parseFloat(pendingAgreed_price_editText.getText().toString()) - Float.parseFloat(paidAmount_editText.getText().toString())));

                    repairTicketRef.child("Status").setValue("clear");

                    sendingdialog.show();

                } else {
                    Toast.makeText(Repairs.this, "No changes", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void cancleChanges() {
        cancleChanges_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                repairRef.child("Pending_Faults").removeValue();
                repairRef.child("Pending_Price").removeValue();
                repairTicketRef.child("Status").setValue("clear");

                Intent intent = new Intent(Repairs.this, Repair_details.class);
                intent.putExtra("REPAIR_ID", repair_details_edit_obj.getTicketNo_TextView());
                finish();
                startActivity(intent);
            }
        });
    }

    private boolean validateFields() {
        boolean valid = true;

        if (itemName_textView.getText().toString().equals("Search for item")) {
            Toast.makeText(this, "Please select item", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (customer_textView.getText().toString().equals("Search for customer")) {
            Toast.makeText(this, "Please select customer", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (agreed_price_editText.getText().toString().isEmpty()) {
            agreed_price_editText.setError("Please enter agreed price");
            valid = false;
        }
        if (date_textView.getText().toString().equals("Select date")) {
            Toast.makeText(this, "Select date", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (paidAmount_editText.getText().toString().isEmpty()) {
            paidAmount_editText.setError("Please enter paid amount");
            valid = false;
        }
        if (special_condition_editText.getText().toString().isEmpty()) {
            special_condition_editText.setError("Please enter special condition");
            valid = false;
        }

        if (tempFaultNameList.size() == 0) {
            Toast.makeText(this, "Please enter faults", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void detailsSubmit() {
        pd1.showProgressBar(Repairs.this);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Repairs.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Customer_keyID").setValue(customerKeyID);
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Item_keyID").setValue(itemKeyID);
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Customer_name").setValue(customerName);
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Item_name").setValue(itemName);
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Customer_id").setValue(customerID);
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Item_id").setValue(itemID);
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Item_category").setValue(category_textView.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Ticket_no").setValue(ticketNo_TextView.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Agreed_price").setValue(agreed_price_editText.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Date").setValue(date_textView.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Paid_amount").setValue(paidAmount_editText.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Balance_amount").setValue(balanceAmount_TextView.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Special_condition").setValue(special_condition_editText.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Timestamp").setValue(timestamp);
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("key_id").setValue(key);

            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Customer_keyID").setValue(customerKeyID);
            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Item_keyID").setValue(itemKeyID);
            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Customer_name").setValue(customerName);
            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Item_name").setValue(itemName);
            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Repair_key_id").setValue(key);
            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Ticket_no").setValue(ticketNo_TextView.getText().toString());
            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Status").setValue("clear");

            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Faults").removeValue();
            for (int i = 0; i < tempFaultNameList.size(); i++) {
                reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Faults").child("Fault_" + String.valueOf(i + 1)).child("Fault_name").setValue(tempFaultNameList.get(i));
                reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Faults").child("Fault_" + String.valueOf(i + 1)).child("Fault_price").setValue(tempFaultPriceList.get(i));
                reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Faults").child("Fault_" + String.valueOf(i + 1)).child("Fault_key").setValue(tempFaultKeyIDList.get(i));
            }

            Toast.makeText(this, "Submit Successfully", Toast.LENGTH_SHORT).show();
            pd1.dismissProgressBar(Repairs.this);
            sendingdialog.show();

        } else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
            pd1.dismissProgressBar(Repairs.this);
        }

    }

    private void balanceAmount() {
        agreed_price_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (getIntent().getBooleanExtra("EDIT_CHECK", false)) {

                } else {
                    if (agreed_price_editText.getText().toString().isEmpty()) {
                        balanceAmount_TextView.setText("NA");
                    } else {
                        balanceAmount_TextView.setText(agreed_price_editText.getText().toString());
                        paidAmount_editText.setText("");
                    }
                }

            }
        });

        paidAmount_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                pd.dismissProgressBar(Repairs.this);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                pd.dismissProgressBar(Repairs.this);

            }

            @Override
            public void afterTextChanged(Editable s) {
//                pd.showProgressBar(Repairs.this);
                if (balanceAmount_TextView.getText().toString().equals("NA")) {

                } else {
                    if (paidAmount_editText.getText().toString().equals("")) {

                    } else {
                        float agreedprice = Float.parseFloat(agreed_price_editText.getText().toString());
                        float paidprice = Float.parseFloat(paidAmount_editText.getText().toString());

                        if (paidprice > agreedprice) {
                            String sss = String.valueOf(agreedprice);
                            paidAmount_editText.setText(sss);
                            balanceAmount_TextView.setText("0.00");
                        } else {
                            String sss = String.valueOf(agreedprice - paidprice);
                            balanceAmount_TextView.setText(sss);
                        }
                    }

                }
//                pd.dismissProgressBar(Repairs.this);

            }
        });
    }

    private void editCheckAndProcess() {

        if (getIntent().getBooleanExtra("EDIT_CHECK", false)) {

            changesConfirmation_linearLayout.setVisibility(View.VISIBLE);
            pendingAgreed_price_editText.setVisibility(View.VISIBLE);
            pendingAgreed_price_textView.setVisibility(View.VISIBLE);

            itemKeyID = repair_details_edit_obj.getItemKeyID();
            itemCategory = repair_details_edit_obj.getCategory_textView();

            itemName_textView.setText(repair_details_edit_obj.getItemName_textView() + "\n(" + repair_details_edit_obj.getSerialNo_textView() + ")");
            itemName_textView.setTextColor(getResources().getColor(R.color.gradientDarkBlue));

            itemName = repair_details_edit_obj.getItemName_textView();
            itemID = repair_details_edit_obj.getSerialNo_textView();
            category_textView.setText(repair_details_edit_obj.getCategory_textView());
            condition_textView.setText(repair_details_edit_obj.getCondition_textView());
            last_active_textView.setText(repair_details_edit_obj.getLastActive_textView());
            notes_textView.setText(repair_details_edit_obj.getNotes_textView());
            itemDetails.setVisibility(View.VISIBLE);


            customerKeyID = repair_details_edit_obj.getCustomerKeyID();

            customer_textView.setText(repair_details_edit_obj.getCustomerName_textView() + "\n(" + repair_details_edit_obj.getId_textView() + ")");
            customer_textView.setTextColor(getResources().getColor(R.color.gradientDarkBlue));

            customerName = repair_details_edit_obj.getCustomerName_textView();
            customerID = repair_details_edit_obj.getId_textView();
            phno_textView.setText(repair_details_edit_obj.getPhno_textView());
            dob_textView.setText(repair_details_edit_obj.getDob_textView());
            address_textView.setText(repair_details_edit_obj.getAddress_textView());
            email_textView.setText(repair_details_edit_obj.getEmail_textView());
            customerDetails.setVisibility(View.VISIBLE);

            key = repair_details_edit_obj.getTicketNo_TextView();
            ticketNo_TextView.setText(repair_details_edit_obj.getTicketNo_TextView());
            agreed_price_editText.setText(repair_details_edit_obj.getAgreedPrice_TextView());
            date_textView.setText(repair_details_edit_obj.getDate_TextView());
            paidAmount_editText.setText(repair_details_edit_obj.getPaidAmount_TextView());
            balanceAmount_TextView.setText(repair_details_edit_obj.getBalanceAmount_TextView());
            special_condition_editText.setText(repair_details_edit_obj.getSpecialConditions_TextView());

            disableEditTexts(agreed_price_editText);
            disableEditTexts(paidAmount_editText);
            disableEditTexts(special_condition_editText);

            searchForCustomer_cardview.setOnClickListener(null);
            searchForItem_cardview.setOnClickListener(null);
            item_add_textView.setVisibility(View.GONE);
            customer_add_textview.setVisibility(View.GONE);
            date_textview.setVisibility(View.GONE);

            tempFaultNameList = repair_details_edit_obj.getFaultNameList();
            tempFaultPriceList = repair_details_edit_obj.getFaultPriceList();
            tempFaultKeyIDList = repair_details_edit_obj.getFaultKeyIDList();

            pendingFaultNameList = repair_details_edit_obj.getPendingFaultNameList();
            pendingFaultPriceList = repair_details_edit_obj.getPendingFaultPriceList();
            pendingFaultKeyIDList = repair_details_edit_obj.getPendingFaultKeyIDList();

            tempFaultNameList.addAll(pendingFaultNameList);
            tempFaultPriceList.addAll(pendingFaultPriceList);
            tempFaultKeyIDList.addAll(pendingFaultKeyIDList);


            for (int i = 0; i < (tempFaultNameList.size()); i++) {

                if (i < tempFaultNameList.size() - pendingFaultNameList.size()) {
                    tempFaultRemoveCheckList.add(false);
                } else {
                    tempFaultRemoveCheckList.add(true);
                }

            }

            adapterRepairsFaultListRecyclerView = new AdapterRepairsFaultListRecyclerView(this, tempFaultNameList, tempFaultPriceList, tempFaultKeyIDList, tempFaultRemoveCheckList, pendingFaultNameList, pendingFaultPriceList, pendingFaultKeyIDList, Repairs.this, true);
            faultList_recyclerView.setAdapter(adapterRepairsFaultListRecyclerView);

            timestamp = repair_details_edit_obj.getTimestamp();

            toggling_linear.setVisibility(View.VISIBLE);

            pendingAgreed_price_editText.setText(repair_details_edit_obj.getPendingPrice());
            if (Float.parseFloat(pendingAgreed_price_editText.getText().toString()) > Float.parseFloat(agreed_price_editText.getText().toString())) {
                pendingAgreed_price_editText.setTextColor(getResources().getColor(R.color.textBlue));
                pendingPriceCheck = true;
            }
            pendingAgreed_price_editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (pendingAgreed_price_editText.getText().toString().isEmpty()) {
                        pendingPriceCheck = false;
                    } else {
                        if (Float.parseFloat(pendingAgreed_price_editText.getText().toString()) > Float.parseFloat(agreed_price_editText.getText().toString())) {
                            pendingAgreed_price_editText.setTextColor(getResources().getColor(R.color.textBlue));
                            changesConfirmation_linearLayout.setVisibility(View.VISIBLE);
                            pendingPriceCheck = true;
                        } else {
                            pendingAgreed_price_editText.setTextColor(getResources().getColor(R.color.textGrey));
                            changesConfirmation_linearLayout.setVisibility(View.GONE);
                            pendingPriceCheck = false;
                        }
                    }

                }
            });


        }


    }

    public void disableEditTexts(EditText editText) {
        editText.setFocusable(false);
        editText.setClickable(false);
    }

    private void historyActivity() {
        itemDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Repairs.this, Item_history.class);
                intent.putExtra("ITEM_ID", itemKeyID);
                intent.putExtra("ITEM_CATEGORY", itemCategory);
                startActivity(intent);
            }
        });

        customerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Repairs.this, Customer_history.class);
                intent.putExtra("CUSTOMER_ID", customerKeyID);
                startActivity(intent);
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

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = (Date) formatter.parse(dayOfMonth + "-" + month + "-" + year);
            timestamp = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        date_textView.setText(currentDateString);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchingExisitingCustomers();
        fetchingExisitingItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ITEM_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                // get String data from Intent
                String itemname_returnString = data.getStringExtra("Item_name");
                String itemid_returnString = data.getStringExtra("Item_id");
                String itemcategory_returnString = data.getStringExtra("Item_category");
                String itemkeyid_returnString = data.getStringExtra("Item_keyid");
                String returnString = data.getStringExtra("Last_Active");
                // set text view with string
                last_active_textView.setText(returnString);
                itemDetails.setVisibility(View.VISIBLE);

                itemKeyID = itemkeyid_returnString;
                itemCategory = itemcategory_returnString;

                itemName_textView.setText(itemname_returnString + "\n" + itemid_returnString);
                itemName_textView.setTextColor(getResources().getColor(R.color.gradientDarkBlue));
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
                phno_textView.setText(phone_no_returnString);
                email_textView.setText(email_returnString);
                customerDetails.setVisibility(View.VISIBLE);

                customer_textView.setText(title_returnString + "\n" + id_returnString);
                customer_textView.setTextColor(getResources().getColor(R.color.gradientDarkBlue));
            }
        }
    }

    public void getIncremantalAmount(double incremantalAmount) {
        agreed_price_editText.setText(String.valueOf(incremantalAmount));
    }

    public void getPendingIncremantalAmount(double pendingIncremantalAmount) {
        pendingAgreed_price_editText.setText(String.valueOf(pendingIncremantalAmount));
    }
}
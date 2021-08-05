package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterRepairsFaultListRecyclerView;
import com.dotcom.rbs_system.Classes.Repair_details_edit;
import com.dotcom.rbs_system.Classes.UniquePushID;
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
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class AddRepairTicket extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    long timestamp;

    TextView viewCustomerDetails_textView;

    String key;


    ImageView customerImage_imageView;

    Repair_details_edit repair_details_edit_obj;

    DatabaseReference reference;
    DatabaseReference faultListRef;
    DatabaseReference repairRef, repairTicketRef;

    Progress_dialoge pd1, pd2, pd3, pd4;
    Dialog sendingdialog;

    LinearLayout toggling_linear,hideToggle_linearLayout;
    LinearLayout changesConfirmation_linearLayout, customerID_linearLayout;

    ImageButton gmail_btn, sms_btn, print_btn;
    ImageButton Back_btn;
    TextView addFaults_textview, date_textview, submit_textview;
    Button btn_done;

    TextView date_text;
    TextView date_textView;
    TextView ticketNo_TextView;
    TextView pendingAmount_textView;
    TextView confirmChanges_textView, cancleChanges_textView;

    EditText amount_editText, special_condition_editText;
    EditText pendingAmount_editText;

    EditText customerName_editText,customerId_editText,customerPhno_editText,customerEmail_editText;
    EditText itemName_editText,itemSerialNo_editText;

    RecyclerView faultList_recyclerView;
    AdapterRepairsFaultListRecyclerView adapterRepairsFaultListRecyclerView;

    Boolean item_btn, customer;
    Boolean pendingPriceCheck = true;

    String firebaseAuthUID;
    String customerName, customerID, itemID;
    String itemCategory;

    List<String> faultNameList, faultPriceList, faultKeyIDList;
    List<String> tempFaultNameList;
    List<String> tempFaultPriceList;
    List<String> tempFaultKeyIDList;
    List<String> dateList, lastActiveDatelist;
    List<String> pendingFaultNameList, pendingFaultPriceList, pendingFaultKeyIDList;
    List<Boolean> tempFaultRemoveCheckList;

    Date date;

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
        setContentView(R.layout.activity_add_repair_ticket);

        initialize();
        getFaultsList();
        onClickListeners();
        editCheckAndProcess();
        item_btn = false;
        customer = false;


    }

    private void initialize() {
        UniquePushID.getInstance().generateUniquePushID();
        key = UniquePushID.getInstance().getUniquePushID();

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

        faultListRef = FirebaseDatabase.getInstance().getReference("Listed_faults/"+FirebaseAuth.getInstance().getCurrentUser().getUid());

        addFaults_textview = (TextView) findViewById(R.id.addFaults_textview);

        adapterRepairsFaultListRecyclerView = new AdapterRepairsFaultListRecyclerView(AddRepairTicket.this, tempFaultNameList, tempFaultPriceList, tempFaultKeyIDList, tempFaultRemoveCheckList, pendingFaultNameList, pendingFaultPriceList, pendingFaultKeyIDList, AddRepairTicket.this, false);
        faultList_recyclerView = (RecyclerView) findViewById(R.id.faultList_recyclerView);
        faultList_recyclerView.setAdapter(adapterRepairsFaultListRecyclerView);
        faultList_recyclerView.setLayoutManager(new GridLayoutManager(AddRepairTicket.this, 1));

        date_textView = (TextView) findViewById(R.id.date_textView);

        customerName_editText = (EditText) findViewById(R.id.customerName_editText);
        customerId_editText = (EditText) findViewById(R.id.customerId_editText);
        customerPhno_editText = (EditText) findViewById(R.id.customerPhno_editText);
        customerEmail_editText = (EditText) findViewById(R.id.customerEmail_editText);

        itemName_editText = (EditText) findViewById(R.id.itemName_editText);
        itemSerialNo_editText = (EditText) findViewById(R.id.itemSerialNo_editText);

        amount_editText = (EditText) findViewById(R.id.amount_editText);

        special_condition_editText = (EditText) findViewById(R.id.special_condition_editText);
        pendingAmount_editText = (EditText) findViewById(R.id.pendingAmount_editText);

        toggling_linear = (LinearLayout) findViewById(R.id.toggling_linear);
        hideToggle_linearLayout = (LinearLayout) findViewById(R.id.hideToggle_linearLayout);
        changesConfirmation_linearLayout = (LinearLayout) findViewById(R.id.changesConfirmation_linearLayout);

        submit_textview = (TextView) findViewById(R.id.submit_textview);
        date_textview = (TextView) findViewById(R.id.date_textview);
        Back_btn = (ImageButton) findViewById(R.id.Back_btn);
        date_text = (TextView) findViewById(R.id.date_of_birth_text);
        viewCustomerDetails_textView = (TextView) findViewById(R.id.viewCustomerDetails_textView);

        ticketNo_TextView = (TextView) findViewById(R.id.ticketNo_TextView);
        ticketNo_TextView.setText(key);
        pendingAmount_textView = (TextView) findViewById(R.id.pendingAmount_textView);
        confirmChanges_textView = (TextView) findViewById(R.id.confirmChanges_textView);
        cancleChanges_textView = (TextView) findViewById(R.id.cancleChanges_textView);

        customerImage_imageView = (ImageView) findViewById(R.id.customerImage_imageView);

        customerID_linearLayout = (LinearLayout) findViewById(R.id.customerID_linearLayout);

        /////Firebase config
        firebaseAuthUID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
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

    private void getFaultsList() {
        pd4.showProgressBar(AddRepairTicket.this);
        faultListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    faultNameList.add(String.valueOf(dataSnapshot1.child("Fault_name").getValue()));
                    faultPriceList.add(String.valueOf(dataSnapshot1.child("Fault_price").getValue()));
                    faultKeyIDList.add(String.valueOf(dataSnapshot1.child("key_id").getValue()));

                }
                pd4.dismissProgressBar(AddRepairTicket.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd4.dismissProgressBar(AddRepairTicket.this);
            }
        });
    }

    private void onClickListeners() {
        confirmChanges();
        cancleChanges();


        addFaults_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(AddRepairTicket.this, "Search...",
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

        print_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddRepairTicket.this, "YEs working", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddRepairTicket.this, "Add new Faults", Toast.LENGTH_SHORT).show();
                    }
                    if (!pendingPriceCheck) {
                        pendingAmount_editText.setError("Enter valid price");
                    }
                    if (pendingFaultNameList.size() != 0 && pendingPriceCheck) {
                        pd1.showProgressBar(AddRepairTicket.this);

                        boolean connected = false;
                        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(AddRepairTicket.CONNECTIVITY_SERVICE);
                        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                            //we are connected to a network
                            connected = true;

                            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Pending_Amount").setValue(pendingAmount_editText.getText().toString());

                            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Pending_Faults").removeValue();
                            for (int i = 0; i < pendingFaultNameList.size(); i++) {
                                reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Pending_Faults").child("Fault_" + String.valueOf(i + 1)).child("Fault_name").setValue(pendingFaultNameList.get(i));
                                reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Pending_Faults").child("Fault_" + String.valueOf(i + 1)).child("Fault_price").setValue(pendingFaultPriceList.get(i));
                                reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Pending_Faults").child("Fault_" + String.valueOf(i + 1)).child("Fault_key").setValue(pendingFaultKeyIDList.get(i));
                            }
                            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Status").setValue("Pending Changes");

                            Toast.makeText(AddRepairTicket.this, "Submit Successfully123", Toast.LENGTH_SHORT).show();
                            sendingdialog.show();
                            pd1.dismissProgressBar(AddRepairTicket.this);
                            repair_details_edit_obj.clear();

                        } else {
                            Toast.makeText(AddRepairTicket.this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
                            connected = false;
                            pd1.dismissProgressBar(AddRepairTicket.this);
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

                    repairRef.child("Pending_Amount").removeValue();
                    repairRef.child("Amount").setValue(pendingAmount_editText.getText().toString());

                    repairTicketRef.child("Status").setValue("clear");

                    sendingdialog.show();

                } else {
                    Toast.makeText(AddRepairTicket.this, "No changes", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void cancleChanges() {
        cancleChanges_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                repairRef.child("Pending_Faults").removeValue();
                repairRef.child("Pending_Amount").removeValue();
                repairTicketRef.child("Status").setValue("clear");

                Intent intent = new Intent(AddRepairTicket.this, Repair_details.class);
                intent.putExtra("REPAIR_ID", repair_details_edit_obj.getTicketNo_TextView());
                finish();
                startActivity(intent);
            }
        });
    }

    private boolean validateFields() {
        boolean valid = true;

        if (amount_editText.getText().toString().isEmpty()) {
            amount_editText.setError("Please enter agreed price");
            valid = false;
        }
        if (date_textView.getText().toString().equals("Select date")) {
            Toast.makeText(this, "Select date", Toast.LENGTH_LONG).show();
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
        pd1.showProgressBar(AddRepairTicket.this);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(AddRepairTicket.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Customer_email").setValue(customerEmail_editText.getText().toString());
            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Customer_id").setValue(customerId_editText.getText().toString());
            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Customer_name").setValue(customerName_editText.getText().toString());
            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Customer_phno").setValue(customerPhno_editText.getText().toString());
            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Date").setValue(date_textView.getText().toString());
            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Item_name").setValue(itemName_editText.getText().toString());
            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Item_serial_no").setValue(itemSerialNo_editText.getText().toString());
            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Repair_key_id").setValue(key);
            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Status").setValue("clear");
            reference.child("Repairs_ticket_list").child(firebaseAuthUID).child(key).child("Ticket_no").setValue(ticketNo_TextView.getText().toString());

            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Amount").setValue(amount_editText.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Customer_email").setValue(customerEmail_editText.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Customer_id").setValue(customerId_editText.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Customer_name").setValue(customerName_editText.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Customer_phno").setValue(customerPhno_editText.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Date").setValue(date_textView.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Faults").removeValue();
            for (int i = 0; i < tempFaultNameList.size(); i++) {
                reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Faults").child("Fault_" + String.valueOf(i + 1)).child("Fault_name").setValue(tempFaultNameList.get(i));
                reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Faults").child("Fault_" + String.valueOf(i + 1)).child("Fault_price").setValue(tempFaultPriceList.get(i));
                reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Faults").child("Fault_" + String.valueOf(i + 1)).child("Fault_key").setValue(tempFaultKeyIDList.get(i));
            }
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Item_name").setValue(itemName_editText.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Item_serial_no").setValue(itemSerialNo_editText.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Special_condition").setValue(special_condition_editText.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Ticket_no").setValue(ticketNo_TextView.getText().toString());
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("Timestamp").setValue(timestamp);
            reference.child("Repairs_list").child(firebaseAuthUID).child(key).child("key_id").setValue(key);

            Toast.makeText(this, "Submit Successfully", Toast.LENGTH_SHORT).show();
            pd1.dismissProgressBar(AddRepairTicket.this);
            sendingdialog.show();

        } else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
            pd1.dismissProgressBar(AddRepairTicket.this);
        }

    }


    private void editCheckAndProcess() {

        if (getIntent().getBooleanExtra("EDIT_CHECK", false)) {
            hideToggle_linearLayout.setVisibility(View.GONE);

            changesConfirmation_linearLayout.setVisibility(View.VISIBLE);
            pendingAmount_editText.setVisibility(View.VISIBLE);
            pendingAmount_textView.setVisibility(View.VISIBLE);

            itemCategory = repair_details_edit_obj.getCategory_textView();

//            itemName = repair_details_edit_obj.getItemName_textView();
            itemID = repair_details_edit_obj.getSerialNo_textView();
            customerName = repair_details_edit_obj.getCustomerName_textView();
            customerID = repair_details_edit_obj.getId_textView();

            key = repair_details_edit_obj.getTicketNo_TextView();
            ticketNo_TextView.setText(repair_details_edit_obj.getTicketNo_TextView());
            amount_editText.setText(repair_details_edit_obj.getAmount_TextView());
            date_textView.setText(repair_details_edit_obj.getDate_TextView());
            special_condition_editText.setText(repair_details_edit_obj.getSpecialConditions_TextView());

            disableEditTexts(amount_editText);
            disableEditTexts(special_condition_editText);

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

            adapterRepairsFaultListRecyclerView = new AdapterRepairsFaultListRecyclerView(this, tempFaultNameList, tempFaultPriceList, tempFaultKeyIDList, tempFaultRemoveCheckList, pendingFaultNameList, pendingFaultPriceList, pendingFaultKeyIDList, AddRepairTicket.this, true);
            faultList_recyclerView.setAdapter(adapterRepairsFaultListRecyclerView);

            timestamp = repair_details_edit_obj.getTimestamp();

            toggling_linear.setVisibility(View.VISIBLE);

            pendingAmount_editText.setText(repair_details_edit_obj.getPendingAmount_TextView());
            if (Float.parseFloat(pendingAmount_editText.getText().toString()) > Float.parseFloat(amount_editText.getText().toString())) {
                pendingAmount_editText.setTextColor(getResources().getColor(R.color.textBlue));
                pendingPriceCheck = true;
            }
            pendingAmount_editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (pendingAmount_editText.getText().toString().isEmpty()) {
                        pendingPriceCheck = false;
                        Toast.makeText(AddRepairTicket.this, "if", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Float.parseFloat(pendingAmount_editText.getText().toString()) >= Float.parseFloat(amount_editText.getText().toString())) {
                            pendingAmount_editText.setTextColor(getResources().getColor(R.color.textBlue));
                            changesConfirmation_linearLayout.setVisibility(View.VISIBLE);
                            pendingPriceCheck = true;
                            Toast.makeText(AddRepairTicket.this, "else", Toast.LENGTH_SHORT).show();
                        } else {
                            pendingAmount_editText.setTextColor(getResources().getColor(R.color.textGrey));
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

    public void getIncremantalAmount(double incremantalAmount) {
        amount_editText.setText(String.valueOf(incremantalAmount));
    }

}
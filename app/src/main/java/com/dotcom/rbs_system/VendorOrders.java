package com.dotcom.rbs_system;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.Adapter_Vendor_order_list_RecyclerView;
import com.dotcom.rbs_system.Classes.ActionBarTitle;
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
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VendorOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VendorOrders extends Fragment{

    SimpleDateFormat format;
    String yesterdayDate,currentDate,specificDate;

    Adapter_Vendor_order_list_RecyclerView adapter_vendor_order_list_recyclerView;

    View view;
    DatabaseReference Vendor_orderRef;
    RecyclerView orders_list_recyclerview;
    List<String> order_no_vendor;
    List<String> shop_name;
    List<String> date;
    List<String> totalAmount;
    List<String> remainingAmount;
    List<String> vendor_order_status;
    List<String> shopkeeper_image;
    List<String> shopKeeper_keyID;
    List<String> invoiceKeyID;

    List<String> raworder_no_vendor;
    List<String> rawshop_name;
    List<String> rawdate;
    List<String> rawtotalAmount;
    List<String> rawRemainingAmount;
    List<String> rawvendor_order_status;
    List<String> rawshopkeeper_image;
    List<String> rawshopKeeper_keyID;
    List<String> rawinvoiceKeyID;

    TextView yesterday_date_btn,current_date_btn,specific_date_btn;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public VendorOrders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VendorOrders.
     */
    public static VendorOrders newInstance(String param1, String param2) {
        VendorOrders fragment = new VendorOrders();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vendor_orders, container, false);
        ActionBarTitle.getInstance().getTextView().setText("Vendor Orders");
        initialization();
        initialProcess();
        onclicklistners();


        return view;
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    private void initialization() {

        Vendor_orderRef = FirebaseDatabase.getInstance().getReference("Vendor_order/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        yesterday_date_btn = view.findViewById(R.id.yesterday_date_btn);
        current_date_btn = view.findViewById(R.id.current_date_btn);
        specific_date_btn = view.findViewById(R.id.specific_date_btn);

        orders_list_recyclerview = view.findViewById(R.id.orders_list_recyclerview);
        order_no_vendor = new ArrayList<>();
        shop_name = new ArrayList<>();
        date = new ArrayList<>();
        totalAmount = new ArrayList<>();
        remainingAmount = new ArrayList<>();
        vendor_order_status = new ArrayList<>();
        shopkeeper_image = new ArrayList<>();
        shopKeeper_keyID = new ArrayList<>();
        invoiceKeyID = new ArrayList<>();

        raworder_no_vendor = new ArrayList<>();
        rawshop_name = new ArrayList<>();
        rawdate = new ArrayList<>();
        rawtotalAmount = new ArrayList<>();
        rawRemainingAmount = new ArrayList<>();
        rawvendor_order_status = new ArrayList<>();
        rawshopkeeper_image = new ArrayList<>();
        rawshopKeeper_keyID = new ArrayList<>();
        rawinvoiceKeyID = new ArrayList<>();

    }

    ///////////////////////////////////////////////////////////////////////////////////////

    private void initialProcess() {
        datafetch();
    }

    private void datafetch() {
        Vendor_orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    rawshop_name.add(snapshot1.child("shopkeeper_name").getValue().toString());
                    raworder_no_vendor.add(snapshot1.child("invoice_no").getValue().toString());
                    rawdate.add(snapshot1.child("date").getValue().toString());
                    rawtotalAmount.add(snapshot1.child("totalBalance").getValue().toString());

                    rawvendor_order_status.add(snapshot1.child("status").getValue().toString());
                    rawshopkeeper_image.add(snapshot1.child("shopkeeper_imageUrl").getValue().toString());
                    rawshopKeeper_keyID.add(snapshot1.child("shopkeeper_keyID").getValue().toString());

                    shop_name.add(snapshot1.child("shopkeeper_name").getValue().toString());
                    order_no_vendor.add(snapshot1.child("invoice_no").getValue().toString());
                    date.add(snapshot1.child("date").getValue().toString());
                    totalAmount.add(snapshot1.child("totalBalance").getValue().toString());

                    vendor_order_status.add(snapshot1.child("status").getValue().toString());
                    shopkeeper_image.add(snapshot1.child("shopkeeper_imageUrl").getValue().toString());
                    shopKeeper_keyID.add(snapshot1.child("shopkeeper_keyID").getValue().toString());

                    if(snapshot1.child("remainingBalance").exists()){
                        rawRemainingAmount.add(snapshot1.child("remainingBalance").getValue().toString());
                        remainingAmount.add(snapshot1.child("remainingBalance").getValue().toString());
                    }else {
                        rawRemainingAmount.add("NA");
                        remainingAmount.add("NA");
                    }
                    if (snapshot1.child("invoice_keyId").exists()){
                        rawinvoiceKeyID.add(snapshot1.child("invoice_keyId").getValue().toString());
                        invoiceKeyID.add(snapshot1.child("invoice_keyId").getValue().toString());
                    }else {
                        rawinvoiceKeyID.add("NA");
                        invoiceKeyID.add("NA");
                    }

                }

                adapter_vendor_order_list_recyclerView = new Adapter_Vendor_order_list_RecyclerView(getActivity(), order_no_vendor, shop_name, date, totalAmount,remainingAmount, vendor_order_status, shopkeeper_image,shopKeeper_keyID, invoiceKeyID);
                orders_list_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                orders_list_recyclerview.setAdapter(adapter_vendor_order_list_recyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    private void onclicklistners() {
        yesterday_date_btn_listener();
        current_date_btn_listener();
        specific_date_btn_listener();
    }

    private void specific_date_btn_listener() {
        specific_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                format = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

                DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

                    // when dialog box is closed, below method will be called.
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int selectedMonth, int selectedDay) {
                        String year1 = String.valueOf(selectedYear);
                        String month1 = String.valueOf(selectedMonth + 1);
                        String day1 = String.valueOf(selectedDay);
                        specific_date_btn.setText(day1 + "/" + month1 + "/" + year1);
                        String dtStart = day1 + "/" + month1 + "/" + year1;
                        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date date = format2.parse(dtStart);
                            System.out.println(date);
                            specificDate = format.format(date);
                            specific_date_btn.setText(specificDate);
                            specificDate = specificDate;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        shop_name.clear();
                        order_no_vendor.clear();
                        date.clear();
                        totalAmount.clear();
                        remainingAmount.clear();
                        vendor_order_status.clear();
                        shopkeeper_image.clear();
                        shopKeeper_keyID.clear();
                        invoiceKeyID.clear();


                        for (int i = 0;i<rawdate.size();i++){
                            System.out.println(rawdate.get(i));
                            System.out.println(yesterdayDate);
                            System.out.println("-------------");

                            if (rawdate.get(i).equals(specificDate)){
                                shop_name.add(rawshop_name.get(i));
                                order_no_vendor.add(raworder_no_vendor.get(i));
                                date.add(rawdate.get(i));
                                totalAmount.add(rawtotalAmount.get(i));
                                remainingAmount.add(rawRemainingAmount.get(i));
                                vendor_order_status.add(rawvendor_order_status.get(i));
                                shopkeeper_image.add(rawshopkeeper_image.get(i));
                                shopKeeper_keyID.add(rawshopKeeper_keyID.get(i));
                                invoiceKeyID.add(rawinvoiceKeyID.get(i));
                            }
                        }

                        adapter_vendor_order_list_recyclerView.notifyDataSetChanged();
                        specific_date_btn.setOnClickListener(null);
                        specific_date_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                shop_name.clear();
                                order_no_vendor.clear();
                                date.clear();
                                totalAmount.clear();
                                remainingAmount.clear();
                                vendor_order_status.clear();
                                shopkeeper_image.clear();
                                shopKeeper_keyID.clear();
                                invoiceKeyID.clear();

                                for (int i = 0;i<rawshop_name.size();i++){
                                    shop_name.add(rawshop_name.get(i));
                                    order_no_vendor.add(raworder_no_vendor.get(i));
                                    date.add(rawdate.get(i));
                                    totalAmount.add(rawtotalAmount.get(i));
                                    remainingAmount.add(rawRemainingAmount.get(i));
                                    vendor_order_status.add(rawvendor_order_status.get(i));
                                    shopkeeper_image.add(rawshopkeeper_image.get(i));
                                    shopKeeper_keyID.add(rawshopKeeper_keyID.get(i));
                                    invoiceKeyID.add(rawinvoiceKeyID.get(i));
                                }
                                adapter_vendor_order_list_recyclerView.notifyDataSetChanged();
                                specific_date_btn.setOnClickListener(null);
                                specific_date_btn.setText("Specific Date");
                                specific_date_btn_listener();
                            }
                        });


                    }
                };

// Create the DatePickerDialog instance
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                        R.style.MaterialAlertDialog_MaterialComponents, datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Select the date");
                datePicker.show();

// Listener

            }
        });
    }

    private void current_date_btn_listener() {
        current_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop_name.clear();
                order_no_vendor.clear();
                date.clear();
                totalAmount.clear();
                remainingAmount.clear();
                vendor_order_status.clear();
                shopkeeper_image.clear();
                shopKeeper_keyID.clear();
                invoiceKeyID.clear();

                format = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                currentDate = format.format(c);

                for (int i = 0;i<rawdate.size();i++){
                    System.out.println(rawdate.get(i));
                    System.out.println(yesterdayDate);
                    System.out.println("-------------");

                    if (rawdate.get(i).equals(currentDate)){
                        shop_name.add(rawshop_name.get(i));
                        order_no_vendor.add(raworder_no_vendor.get(i));
                        date.add(rawdate.get(i));
                        totalAmount.add(rawtotalAmount.get(i));
                        remainingAmount.add(rawRemainingAmount.get(i));
                        vendor_order_status.add(rawvendor_order_status.get(i));
                        shopkeeper_image.add(rawshopkeeper_image.get(i));
                        shopKeeper_keyID.add(rawshopKeeper_keyID.get(i));
                        invoiceKeyID.add(rawinvoiceKeyID.get(i));
                    }
                }

                adapter_vendor_order_list_recyclerView.notifyDataSetChanged();
                current_date_btn.setOnClickListener(null);
                current_date_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shop_name.clear();
                        order_no_vendor.clear();
                        date.clear();
                        totalAmount.clear();
                        remainingAmount.clear();
                        vendor_order_status.clear();
                        shopkeeper_image.clear();
                        shopKeeper_keyID.clear();
                        invoiceKeyID.clear();

                        for (int i = 0;i<rawshop_name.size();i++){
                            shop_name.add(rawshop_name.get(i));
                            order_no_vendor.add(raworder_no_vendor.get(i));
                            date.add(rawdate.get(i));
                            totalAmount.add(rawtotalAmount.get(i));
                            remainingAmount.add(rawRemainingAmount.get(i));
                            vendor_order_status.add(rawvendor_order_status.get(i));
                            shopkeeper_image.add(rawshopkeeper_image.get(i));
                            shopKeeper_keyID.add(rawshopKeeper_keyID.get(i));
                            invoiceKeyID.add(rawinvoiceKeyID.get(i));
                        }
                        adapter_vendor_order_list_recyclerView.notifyDataSetChanged();
                        current_date_btn.setOnClickListener(null);
                        current_date_btn_listener();
                    }
                });

            }
        });
    }

    private void yesterday_date_btn_listener() {

        yesterday_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shop_name.clear();
                order_no_vendor.clear();
                date.clear();
                totalAmount.clear();
                remainingAmount.clear();
                vendor_order_status.clear();
                shopkeeper_image.clear();
                shopKeeper_keyID.clear();
                invoiceKeyID.clear();

                format = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                yesterdayDate = format.format(cal.getTime()); //your formatted date here

                for (int i = 0;i<rawdate.size();i++){
                    System.out.println(rawdate.get(i));
                    System.out.println(yesterdayDate);
                    System.out.println("-------------");

                    if (rawdate.get(i).equals(yesterdayDate)){
                        shop_name.add(rawshop_name.get(i));
                        order_no_vendor.add(raworder_no_vendor.get(i));
                        date.add(rawdate.get(i));
                        totalAmount.add(rawtotalAmount.get(i));
                        remainingAmount.add(rawRemainingAmount.get(i));
                        vendor_order_status.add(rawvendor_order_status.get(i));
                        shopkeeper_image.add(rawshopkeeper_image.get(i));
                        shopKeeper_keyID.add(rawshopKeeper_keyID.get(i));
                        invoiceKeyID.add(rawinvoiceKeyID.get(i));
                    }
                }

                adapter_vendor_order_list_recyclerView.notifyDataSetChanged();
                yesterday_date_btn.setOnClickListener(null);
                yesterday_date_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shop_name.clear();
                        order_no_vendor.clear();
                        date.clear();
                        totalAmount.clear();
                        remainingAmount.clear();
                        vendor_order_status.clear();
                        shopkeeper_image.clear();
                        shopKeeper_keyID.clear();
                        invoiceKeyID.clear();

                        for (int i = 0;i<rawshop_name.size();i++){
                            shop_name.add(rawshop_name.get(i));
                            order_no_vendor.add(raworder_no_vendor.get(i));
                            date.add(rawdate.get(i));
                            totalAmount.add(rawtotalAmount.get(i));
                            remainingAmount.add(rawRemainingAmount.get(i));
                            vendor_order_status.add(rawvendor_order_status.get(i));
                            shopkeeper_image.add(rawshopkeeper_image.get(i));
                            shopKeeper_keyID.add(rawshopKeeper_keyID.get(i));
                            invoiceKeyID.add(rawinvoiceKeyID.get(i));
                        }
                        adapter_vendor_order_list_recyclerView.notifyDataSetChanged();
                        yesterday_date_btn.setOnClickListener(null);
                        yesterday_date_btn_listener();
                    }
                });

            }
        });
    }

    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
        public void populateSetDate(int year, int month, int day) {
            specific_date_btn.setText(month+"/"+day+"/"+year);
        }

    }


}
package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterCustomerHistoryListRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterCustomerIDImagesRecyclerView;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Customer_history extends AppCompatActivity {

    LatLng latLng;

    Double latitude;
    Double longitude;

    Geocoder geocoder;

    List<Address> addressList;

    String customerKeyID;
    String selected_country_code;
    EditText editTextCarrierNumber;
    EditText ac_postcode,ac_houseNo;
    List<String> shopkeeper_name_textview, item_name_textview, item_category_textview, status_textView, shopkeeperImage_imageView_list, dateList, itemKeyId, itemImageView, shopkeeper_key_id, serial_no_textview, customerIDimageUrlList;

    RecyclerView customer_ID_Image_recyclerView;
    ImageView edit_image_image_view;

    RelativeLayout alert_background_relativelayout;

    LinearLayout header_profile;

    TextView edit_textview;

    SimpleDateFormat sfd;

    TextView customerDetailsToggle_textView;
    TextView customerName_textView, customerID_textView, phno_textView, customer_address_textView, customer_email_textView;

    DatabaseReference customerRef;
    DatabaseReference customerHistoryRef;
    Query orderQuery;

    RecyclerView customerHistoryRecyclerView;
    AdapterCustomerHistoryListRecyclerView adapterCustomerHistoryListRecyclerView;

    AdapterCustomerIDImagesRecyclerView adapterCustomerIDImagesRecyclerView;

    Progreess_dialog pd1, pd2;

    Boolean toggleCheck = true;

    ImageButton back_btn;

    CountryCodePicker ccp;

    EditText ac_email;

    TextView ac_address;
    TextView postcodeCheck_textView;

    TextView save_btn_textview, cancel_btn_textview;

    Dialog edit_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_history);

        Initialization();
        InitialDataFetch();
        ClickListeners();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Initialization() {

        edit_dialog = new Dialog(this);
        edit_dialog.setContentView(R.layout.edit_dialog_customer);

        ccp = edit_dialog.findViewById(R.id.ccp);
        editTextCarrierNumber = edit_dialog.findViewById(R.id.editText_carrierNumber);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);

        customerName_textView = findViewById(R.id.customerName_textView);
        customerID_textView = findViewById(R.id.customerID_textView);
        phno_textView = findViewById(R.id.phno_textView);
        customer_address_textView = findViewById(R.id.customer_address_textView);
        customer_email_textView = findViewById(R.id.customer_email_textView);
        back_btn = findViewById(R.id.back_btn);
        edit_textview = findViewById(R.id.edit_textview);
        header_profile = findViewById(R.id.header_profile);
        alert_background_relativelayout = findViewById(R.id.alert_background_relativelayout);
        edit_image_image_view = findViewById(R.id.edit_image_image_view);


        postcodeCheck_textView = edit_dialog.findViewById(R.id.postcodeCheck_textView);
        ac_postcode = edit_dialog.findViewById(R.id.ac_postcode);
        ac_houseNo = edit_dialog.findViewById(R.id.ac_houseNo);

        ac_address = edit_dialog.findViewById(R.id.ac_address);
        ac_email = edit_dialog.findViewById(R.id.ac_email);
        cancel_btn_textview = edit_dialog.findViewById(R.id.cancel_btn_textview);
        save_btn_textview = edit_dialog.findViewById(R.id.save_btn_textview);

        customerKeyID = getIntent().getStringExtra("CUSTOMER_ID");
        customerIDimageUrlList = new ArrayList<>();

        dateList = new ArrayList<>();
        itemImageView = new ArrayList<>();
        itemKeyId = new ArrayList<>();
        item_name_textview = new ArrayList<>();
        item_category_textview = new ArrayList<>();
        serial_no_textview = new ArrayList<>();
        status_textView = new ArrayList<>();
        shopkeeperImage_imageView_list = new ArrayList<>();
        shopkeeper_key_id = new ArrayList<>();
        shopkeeper_name_textview = new ArrayList<>();

        sfd = new SimpleDateFormat("dd-MM-yyyy");

        customerHistoryRef = FirebaseDatabase.getInstance().getReference("Customer_history/" + customerKeyID);

        orderQuery = customerHistoryRef.orderByChild("Timestamp");

        customerHistoryRecyclerView = findViewById(R.id.customerHistoryRecyclerView);
        customerHistoryRecyclerView.setLayoutManager(new GridLayoutManager(Customer_history.this, 1));


        customerDetailsToggle_textView = findViewById(R.id.customerDetailsToggle_textView);

        customerRef = FirebaseDatabase.getInstance().getReference("Customer_list");

        customer_ID_Image_recyclerView = findViewById(R.id.customer_ID_Image_recyclerView);
        customer_ID_Image_recyclerView.setLayoutManager(new GridLayoutManager(Customer_history.this, 2));


        pd1 = new Progreess_dialog();
        pd2 = new Progreess_dialog();

        Date date = Calendar.getInstance().getTime();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);

    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialDataFetch() {
        gettingHistoryList();
        getCustomer(customerKeyID);
    }

    private void gettingHistoryList() {
        pd1.showProgressBar(Customer_history.this);

        pd1.dismissProgressBar(Customer_history.this);
        customerHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        dateList.add(dataSnapshot1.child("Date").getValue().toString());
                        item_category_textview.add(dataSnapshot1.child("Item_keyCategory").getValue().toString());
                        itemImageView.add(dataSnapshot1.child("Item_image").getValue().toString());
                        itemKeyId.add(dataSnapshot1.child("Item_keyId").getValue().toString());
                        item_name_textview.add(dataSnapshot1.child("Item_name").getValue().toString());
                        serial_no_textview.add(dataSnapshot1.child("Item_serialno").getValue().toString());
                        status_textView.add(dataSnapshot1.child("RBS").getValue().toString());
                        shopkeeperImage_imageView_list.add(dataSnapshot1.child("Shopkeeper_image").getValue().toString());
                        shopkeeper_key_id.add(dataSnapshot1.child("Shopkeeper_keyId").getValue().toString());
                        shopkeeper_name_textview.add(dataSnapshot1.child("Shopkeeper_name").getValue().toString());

                    }
                    Collections.reverse(dateList);
                    Collections.reverse(itemImageView);
                    Collections.reverse(itemKeyId);
                    Collections.reverse(item_name_textview);
                    Collections.reverse(item_category_textview);
                    Collections.reverse(serial_no_textview);
                    Collections.reverse(status_textView);
                    Collections.reverse(shopkeeperImage_imageView_list);
                    Collections.reverse(shopkeeper_key_id);
                    Collections.reverse(shopkeeper_name_textview);


                } else {
                    pd1.dismissProgressBar(Customer_history.this);
                }

                adapterCustomerHistoryListRecyclerView = new AdapterCustomerHistoryListRecyclerView(Customer_history.this, dateList, item_category_textview, itemImageView, itemKeyId, item_name_textview, serial_no_textview, status_textView, shopkeeperImage_imageView_list, shopkeeper_key_id, shopkeeper_name_textview);
                customerHistoryRecyclerView.setAdapter(adapterCustomerHistoryListRecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getCustomer(String customerKeyID) {
        pd2.showProgressBar(Customer_history.this);
        customerRef.child(customerKeyID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    customerName_textView.setText(dataSnapshot.child("Name").getValue().toString());
                    customerID_textView.setText(dataSnapshot.child("ID").getValue().toString());
                    phno_textView.setText(dataSnapshot.child("Phone_no").getValue().toString());
                    customer_address_textView.setText(dataSnapshot.child("Address").getValue().toString());
                    customer_email_textView.setText(dataSnapshot.child("Email").getValue().toString());

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("ID_Image_urls").getChildren()) {
                        customerIDimageUrlList.add(dataSnapshot1.getValue().toString());
                    }

                    adapterCustomerIDImagesRecyclerView = new AdapterCustomerIDImagesRecyclerView(Customer_history.this, customerIDimageUrlList, alert_background_relativelayout, edit_image_image_view);
                    customer_ID_Image_recyclerView.setAdapter(adapterCustomerIDImagesRecyclerView);
                    pd2.dismissProgressBar(Customer_history.this);
                } else {
                    pd2.dismissProgressBar(Customer_history.this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void ClickListeners() {
        customerDetailsToggle();
        editbtn();
        cancelbtn();
        savebtn();
        backbtn();
        postcodeCheck_textView_listener();
    }

    private void postcodeCheck_textView_listener() {
        postcodeCheck_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPostcode();
            }
        });
    }


    private void editbtn() {
        edit_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextCarrierNumber.setText(phno_textView.getText().toString());
                ac_address.setText(customer_address_textView.getText().toString());
                ac_email.setText(customer_email_textView.getText().toString());
                edit_dialog.show();
            }
        });
    }

    private void savebtn() {
        save_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_data();
            }
        });
    }

    private void edit_data() {
        boolean check = false;

        if (!editTextCarrierNumber.getText().toString().equals(phno_textView.getText().toString())) {
            customerRef.child(customerKeyID).child("Phone_no").setValue(ccp.getFullNumberWithPlus());
            check = true;
        }
        if (!ac_email.getText().toString().equals("") && !customer_email_textView.getText().toString().equals(ac_email.getText().toString())) {
            customerRef.child(customerKeyID).child("Email").setValue(ac_email.getText().toString());
            check = true;
        }
        if (!ac_address.getText().toString().equals(customer_address_textView.getText().toString())&&ac_address.getText().toString().equals("----")) {
            customerRef.child(customerKeyID).child("Postcode").setValue(ac_postcode.getText().toString());
            customerRef.child(customerKeyID).child("House_door").setValue(ac_houseNo.getText().toString());
            customerRef.child(customerKeyID).child("Address").setValue(ac_address.getText().toString());
            check = true;
        }
        if (check == true) {
            Toast.makeText(this, "Data save Successfully", Toast.LENGTH_SHORT).show();
            recreate();
        } else {
            Toast.makeText(Customer_history.this, "Not Edit", Toast.LENGTH_SHORT).show();
        }

        edit_dialog.dismiss();

    }

    private void cancelbtn() {
        cancel_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_dialog.dismiss();
            }
        });
    }

    private void customerDetailsToggle() {
        customerDetailsToggle_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toggleCheck) {
                    header_profile.setVisibility(View.VISIBLE);
                    customerDetailsToggle_textView.setText("Hide Details");
                    toggleCheck = true;
                } else {
                    header_profile.setVisibility(View.GONE);
                    customerDetailsToggle_textView.setText("Show Item Details");
                    toggleCheck = false;
                }
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

    public void onCountryPickerClick(View view) {
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
                selected_country_code = ccp.getFullNumberWithPlus();
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void checkPostcode() {
        // SE13 6AZ
        String postcode = ac_postcode.getText().toString();
        latLng = getLocationFromAddress(Customer_history.this, postcode);

        if (latLng == null) {
            ac_postcode.setError("Enter Valid formatted postcode\n\"XXXX XXX\"");
            ac_address.setText("----");
            if (ac_houseNo.getText().toString().isEmpty()) {
                ac_houseNo.setError("Enter Door no");
            }
        } else {

            if (ac_houseNo.getText().toString().isEmpty()) {
                ac_houseNo.setError("Enter Door no");
            } else {
                latitude = latLng.latitude;
                longitude = latLng.longitude;

                addressList = new ArrayList<>();
                geocoder = new Geocoder(this, Locale.getDefault());

                try {
                    addressList = geocoder.getFromLocation(latitude, longitude, 1);
                    String country = addressList.get(0).getCountryName();
                    String city = addressList.get(0).getAdminArea();
                    String subCity = addressList.get(0).getSubAdminArea();
                    String area = addressList.get(0).getLocality();
                    String street = addressList.get(0).getThoroughfare();

                    if (street == null) {
                        street = "";
                    } else {
                        street = street + ", ";
                    }

                    if (area == null) {
                        area = "";
                    } else {
                        area = area + ", ";
                    }

                    if (subCity == null) {
                        subCity = "";
                    } else {
                        subCity = subCity + ", ";
                    }

                    if (city == null) {
                        city = "";
                    } else {
                        city = city + ", ";
                    }

                    if (country == null) {
                        country = "";
                    }

                    ac_address.setText(street + area + subCity + city + country + ", Door No: " + ac_houseNo.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            try {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            } catch (Exception e) {

            }


        } catch (IOException ex) {

            ex.printStackTrace();
        }

        System.out.println(p1);
        return p1;
    }
}

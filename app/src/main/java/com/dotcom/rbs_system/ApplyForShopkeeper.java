package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterItemDetailsImagesRecyclerView;
import com.dotcom.rbs_system.Classes.RBSCustomerDetails;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ApplyForShopkeeper extends AppCompatActivity {

    EditText ac_postcode,ac_houseNo;
    TextView uploadId_textView,submit_textView;
    TextView ac_address;
    TextView postcodeCheck_textView;
    LatLng latLng;
    Double latitude;
    Double longitude;
    List<Address> addressList;
    Geocoder geocoder;

    String selected_country_code;
    CountryCodePicker ccp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_shopkeeper);

        initialize();
        onClickListeners();
    }

    private void initialize() {

        ccp = findViewById(R.id.ccp);
//        ccp.registerCarrierNumberEditText(editTextCarrierNumber);
        postcodeCheck_textView = findViewById(R.id.postcodeCheck_textView);

        uploadId_textView = findViewById(R.id.uploadId_textView);
        submit_textView = findViewById(R.id.submit_textView);

        ac_houseNo = findViewById(R.id.ac_houseNo);
        ac_address = findViewById(R.id.ac_address);
        ac_postcode = findViewById(R.id.ac_postcode);

    }

    private void onClickListeners() {

        postcodeCheck_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPostcode();
            }
        });

        uploadId_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

        submit_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });




    }

    private void checkPostcode() {
        // SE13 6AZ
        String postcode = ac_postcode.getText().toString();
        latLng = getLocationFromAddress(ApplyForShopkeeper.this, postcode);

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

    public void onCountryPickerClick(View view) {
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
                selected_country_code = ccp.getFullNumberWithPlus();
            }
        });
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
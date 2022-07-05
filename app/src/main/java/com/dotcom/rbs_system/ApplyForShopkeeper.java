package com.dotcom.rbs_system;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterFilesNamesRecyclerView;
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

    EditText ac_postcode,ac_houseNo,ac_title,editTextCarrierNumber,ac_email;
    TextView uploadId_textView,submit_textView;
    TextView ac_address;
    TextView postcodeCheck_textView;
    LatLng latLng;
    Double latitude;
    Double longitude;
    List<Address> addressList;
    Geocoder geocoder;
    RecyclerView itemImage_recyclerView;
    AdapterFilesNamesRecyclerView adapterFilesNamesRecyclerView;

    String selected_country_code;
    CountryCodePicker ccp;

    List<Uri> documentUriList;
    List<String> documentNamesList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_shopkeeper);

        initialize();
        onClickListeners();
    }

    private void initialize() {

        documentUriList = new ArrayList<>();
        documentNamesList = new ArrayList<>();
        postcodeCheck_textView = findViewById(R.id.postcodeCheck_textView);

        uploadId_textView = findViewById(R.id.uploadId_textView);
        submit_textView = findViewById(R.id.submit_textView);

        ac_houseNo = findViewById(R.id.ac_houseNo);
        ac_address = findViewById(R.id.ac_address);
        ac_postcode = findViewById(R.id.ac_postcode);
        ac_title = findViewById(R.id.ac_title);
        editTextCarrierNumber = findViewById(R.id.editTextCarrierNumber);
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);
        ac_email = findViewById(R.id.ac_email);

        itemImage_recyclerView = findViewById(R.id.itemImage_recyclerView);
        itemImage_recyclerView.setLayoutManager(new GridLayoutManager(ApplyForShopkeeper.this, 1));
        adapterFilesNamesRecyclerView = new AdapterFilesNamesRecyclerView(ApplyForShopkeeper.this,documentNamesList,documentUriList);
        itemImage_recyclerView.setAdapter(adapterFilesNamesRecyclerView);

    }

    ////////////////////////////////////////////////////////////////////////////////////

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
                Intent filesIntent;
                filesIntent = new Intent(Intent.ACTION_GET_CONTENT);
                filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
                filesIntent.setType("*/*");  //use image/* for photos, etc.
                startActivityForResult(filesIntent, 1);

            }
        });

        submit_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateFields()){

                }

            }
        });

    }

    private boolean validateFields() {
        boolean valid = true;

        if (!ccp.isValidFullNumber()) {
            editTextCarrierNumber.setError("Please enter valid number");
            valid = false;
        }
        if (ac_title.getText().toString().isEmpty()) {
            ac_title.setError("Please enter name");
            valid = false;
        }
        if (ac_title.length() > 32) {
            ac_title.setError("Name character limit is 32");
            valid = false;
        }

        if (ac_address.getText().toString().equals("----")) {
            Toast.makeText(this, "Please enter valid postcode", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (ac_postcode.getText().toString().isEmpty()) {
            ac_postcode.setError("Please enter your postal code");
            valid = false;
        }
        if (ac_houseNo.getText().toString().isEmpty()) {
            ac_houseNo.setError("Please enter your Door no");
            valid = false;
        }
        if (ac_email.getText().toString().isEmpty()) {
            ac_email.setError("Please enter correct email");
            valid = false;

        }else if (!Patterns.EMAIL_ADDRESS.matcher(String.valueOf(ac_email.getText())).matches()) {
            ac_email.setError("Please enter a valid email");
            valid = false;
        }

        return valid;
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

        return p1;
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            documentUriList.clear();
            documentNamesList.clear();
            if(resultCode == RESULT_OK) {
                if (data.getClipData()==null){
                    Uri uri = data.getData();
                    documentUriList.add(uri);
                    documentNamesList.add(getFileName(uri));
                    
                    adapterFilesNamesRecyclerView.notifyDataSetChanged();

                }else if (data.getClipData().getItemCount()>5){
                    Toast.makeText(ApplyForShopkeeper.this, "Maximum 5 documents!", Toast.LENGTH_SHORT).show();
                    adapterFilesNamesRecyclerView.notifyDataSetChanged();

                }else {
                    // also check data.data because if the user select one file the file and uri will be in  data.data and data.getClipData() will be null
                    if (data.getClipData()!=null){
                        for(int i = 0; i < data.getClipData().getItemCount(); i++) {

                            Uri uri = data.getClipData().getItemAt(i).getUri();
                            documentUriList.add(uri);
                            documentNamesList.add(getFileName(uri));

                        }
                        adapterFilesNamesRecyclerView.notifyDataSetChanged();
                    }

                }
            }
            System.out.println();
        }
    }
}
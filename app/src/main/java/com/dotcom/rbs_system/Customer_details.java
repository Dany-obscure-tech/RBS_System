package com.dotcom.rbs_system;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterItemDetailsImagesRecyclerView;
import com.dotcom.rbs_system.Classes.RBSCustomerDetails;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Customer_details extends AppCompatActivity {

    Dialog confirmation_alert;
    TextView yes_btn_textview, cancel_btn_textview;

    String selected_country_code;

    EditText editTextCarrierNumber;

    CountryCodePicker ccp;

    LatLng latLng;
    Double latitude;
    Double longitude;
    Geocoder geocoder;
    List<Address> addressList;

    RBSCustomerDetails rbsCustomerDetails;

    AdapterItemDetailsImagesRecyclerView adapterItemDetailsImagesRecyclerView;
    RecyclerView itemImage_recyclerView;

    List<Uri> imageUrlList;
    Uri fileUri;

    String currentDateString;
    String key, key2;

    StorageReference storageReference;
    Progress_dialoge pd;

    Uri tempUri = null;
    Uri tempUri2 = null;
    Uri tempUri3 = null;

    StorageReference idStorageReference;

    ImageButton back_btn;
    ImageView id_imageView;

    TextView date_of_birth_text, uploadId_textView, date_textView, submit_textView, postcodeCheck_textView, ac_address;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    DatabaseReference reference;
    EditText ac_title, ac_houseNo, ac_id, ac_email, ac_postcode;

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE2 = 2;
    private static final int CAMERA_REQUEST_CODE3 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        pd = new Progress_dialoge();


        initialize();

        onClickListeners();

    }

    private void initialize() {
        rbsCustomerDetails = RBSCustomerDetails.getInstance();
        confirmation_alert = new Dialog(this);
        confirmation_alert.setContentView(R.layout.exit_confirmation_alert);
        yes_btn_textview = (TextView) confirmation_alert.findViewById(R.id.yes_btn_textview);
        cancel_btn_textview = (TextView) confirmation_alert.findViewById(R.id.cancel_btn_textview);

        ccp = findViewById(R.id.ccp);
        editTextCarrierNumber = (EditText) findViewById(R.id.editText_carrierNumber);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);

        imageUrlList = new ArrayList<>();
        rbsCustomerDetails.setImageUrlList(imageUrlList);

        storageReference = FirebaseStorage.getInstance().getReference();

        reference = FirebaseDatabase.getInstance().getReference();
        ac_title = (EditText) findViewById(R.id.ac_title);
        ac_houseNo = (EditText) findViewById(R.id.ac_houseNo);
        ac_id = (EditText) findViewById(R.id.ac_id);
        ac_address = (TextView) findViewById(R.id.ac_address);
        ac_email = (EditText) findViewById(R.id.ac_email);
        ac_postcode = (EditText) findViewById(R.id.ac_postcode);

        uploadId_textView = (TextView) findViewById(R.id.uploadId_textView);

        back_btn = (ImageButton) findViewById(R.id.back_btn);
        id_imageView = (ImageView) findViewById(R.id.id_imageView);

        date_textView = (TextView) findViewById(R.id.date_textView);
        postcodeCheck_textView = (TextView) findViewById(R.id.postcodeCheck_textView);
        submit_textView = (TextView) findViewById(R.id.submit_textView);
        date_of_birth_text = (TextView) findViewById(R.id.date_of_birth_text);


        idStorageReference = storageReference.child("Customer_IDs");

        itemImage_recyclerView = (RecyclerView) findViewById(R.id.itemImage_recyclerView);
        itemImage_recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapterItemDetailsImagesRecyclerView = new AdapterItemDetailsImagesRecyclerView(Customer_details.this, imageUrlList);
        itemImage_recyclerView.setAdapter(adapterItemDetailsImagesRecyclerView);


    }

    private void onClickListeners() {

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

        postcodeCheck_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPostcode();
            }
        });

        uploadId_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUrlList.size() < 2) {
                    ImagePicker.Companion.with(Customer_details.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start();
                } else {
                    Toast.makeText(Customer_details.this, "Maximum 2 images allowed!", Toast.LENGTH_SHORT).show();
                }


            }
        });


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        date_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = (calendar.get(Calendar.YEAR)) - 18;
                int month = 0;
                int day = 1;

                DatePickerDialog dialog = new DatePickerDialog(Customer_details.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
                currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;

                SimpleDateFormat input = new SimpleDateFormat("d/M/yyyy");
                SimpleDateFormat output = new SimpleDateFormat("EEEE, d MMMM yyyy");

                try {
                    date_of_birth_text.setText(output.format(input.parse(date)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };


        submit_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPostcode();
                if (validateFields() == true) {
                    detailsSubmit();
                }
            }
        });

    }

    private void checkPostcode() {
        // SE13 6AZ
        String postcode = ac_postcode.getText().toString();
        latLng = getLocationFromAddress(Customer_details.this, postcode);

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
                    String streetno = addressList.get(0).getSubThoroughfare();
                    String address = addressList.get(0).getAddressLine(0);
                    String check = addressList.get(0).getAddressLine(0);
//                Toast.makeText(this, check, Toast.LENGTH_SHORT).show();
//                System.out.println(address);
//                System.out.println(check);

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

        System.out.println(String.valueOf(p1));
        return p1;
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
        if (ac_id.getText().toString().isEmpty()) {
            ac_id.setError("Please enter id");
            valid = false;
        }
        if (imageUrlList.size() == 0) {
            Toast.makeText(this, "Please upload customer ID image", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (date_of_birth_text.getText().toString().equals("Select date")) {
            Toast.makeText(this, "Select date of birth", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (ac_address.getText().toString().equals("----")) {
            Toast.makeText(this, "Please enter valid postcode", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (ac_email.getText().toString().isEmpty()) {
            ac_email.setError("Please enter your email address");
            valid = false;
        }
        if (ac_postcode.getText().toString().isEmpty()) {
            ac_postcode.setError("Please enter your postal code");
            valid = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(String.valueOf(ac_email.getText())).matches()) {
            ac_email.setError("Please enter a valid email");
            valid = false;
        }


        return valid;
    }
//TODO

//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR, year);
//        calendar.set(Calendar.MONTH, month);
//        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//        date_of_birth_text.setText(currentDateString);
//    }

    private void detailsSubmit() {
        pd.showProgressBar(Customer_details.this);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Customer_details.this.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            key = reference.push().getKey();
            rbsCustomerDetails.setCustomerName(ac_title.getText().toString());
            rbsCustomerDetails.setCustomerPhNo(selected_country_code);
            rbsCustomerDetails.setCustomerHouseNo(ac_houseNo.getText().toString());
            rbsCustomerDetails.setCustomerId(ac_id.getText().toString());
            rbsCustomerDetails.setCustomerDob(date_of_birth_text.getText().toString());
            rbsCustomerDetails.setCustomerAddress(ac_address.getText().toString());
            rbsCustomerDetails.setCustomerEmail(ac_email.getText().toString());
            rbsCustomerDetails.setCustomerPostcode(ac_postcode.getText().toString());

            pass_back_data();
            pd.dismissProgressBar(Customer_details.this);
            finish();

        } else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
        }

    }

    private void pass_back_data() {

        // get the text from the EditText
        String ac_title_ = ac_title.getText().toString();
        String ac_id_ = ac_id.getText().toString();
        String ac_phone_no =  ccp.getFullNumberWithPlus();
        String ac_email_ = ac_email.getText().toString();

        // put the String to pass back into an Intent and close this activity
        Intent intent = new Intent();
        intent.putExtra("AC_title", ac_title_);
        intent.putExtra("AC_id", ac_id_);
        intent.putExtra("AC_key_id", key);
        intent.putExtra("AC_phone_no", ac_phone_no);
        intent.putExtra("AC_email", ac_email_);
        setResult(RESULT_FIRST_USER, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            fileUri = data.getData();
            id_imageView.setImageURI(fileUri);
            fileUri = data.getData();
            imageUrlList.add(fileUri);

            adapterItemDetailsImagesRecyclerView.notifyDataSetChanged();


            //You can get File object from intent
//            val file:File = ImagePicker.getFile(data)!!

            //You can also get File Path from intent
//                    val filePath:String = ImagePicker.getFilePath(data)!!
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    confirmation_alert.show();

                    return true;
            }
        }
        return false;
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
}

package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
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
import com.dotcom.rbs_system.Classes.UserDetails;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Registration extends AppCompatActivity {

    FirebaseAuth mAuth;

    String imagePickerCheck;

    Dialog confirmation_alert;
    TextView yes_btn_textview, cancel_btn_textview;

    ImageView profileImage_imageView;
    TextView profileImage_textView;
    TextView removeImage_textView;

    String selected_country_code;

    EditText editTextCarrierNumber;

    EditText ac_password;
    EditText ac_confirmPassword;

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
    Uri profileImageUri;

    String currentDateString;
    String key;

    StorageReference storageReference;
    Progress_dialog pd;

    StorageReference idStorageReference,profileImage_ref;

    ImageButton back_btn;
    ImageView id_imageView;

    TextView date_of_birth_text, uploadId_textView, date_textView, submit_textView, postcodeCheck_textView, ac_address;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    DatabaseReference reference;
    EditText ac_title, ac_houseNo, ac_id, ac_postcode;
    TextView ac_email;

    ImageView image_imageView;

    int i,k=0,l=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        pd = new Progress_dialog();

        initialize();

        onClickListeners();
    }

    private void initialize() {
        mAuth = FirebaseAuth.getInstance();

        rbsCustomerDetails = RBSCustomerDetails.getInstance();
        confirmation_alert = new Dialog(this);
        confirmation_alert.setContentView(R.layout.exit_confirmation_alert);
        yes_btn_textview = confirmation_alert.findViewById(R.id.yes_btn_textview);
        cancel_btn_textview = confirmation_alert.findViewById(R.id.cancel_btn_textview);

        ccp = findViewById(R.id.ccp);
        editTextCarrierNumber = findViewById(R.id.editText_carrierNumber);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);

        imageUrlList = new ArrayList<>();
        rbsCustomerDetails.setImageUrlList(imageUrlList);

        storageReference = FirebaseStorage.getInstance().getReference();

        reference = FirebaseDatabase.getInstance().getReference();
        ac_title = findViewById(R.id.ac_title);
        ac_houseNo = findViewById(R.id.ac_houseNo);
        ac_id = findViewById(R.id.ac_id);
        ac_address = findViewById(R.id.ac_address);
        ac_postcode = findViewById(R.id.ac_postcode);

        profileImage_imageView = findViewById(R.id.profileImage_imageView);
        profileImage_textView = findViewById(R.id.profileImage_textView);
        removeImage_textView = findViewById(R.id.removeImage_textView);

        ac_password = findViewById(R.id.ac_password);
        ac_confirmPassword = findViewById(R.id.ac_confirmPassword);

        ac_email = findViewById(R.id.ac_email);
        ac_email.setText(getIntent().getStringExtra("EMAIL"));

        uploadId_textView = findViewById(R.id.uploadId_textView);

        back_btn = findViewById(R.id.back_btn);
        id_imageView = findViewById(R.id.id_imageView);

        date_textView = findViewById(R.id.date_textView);
        postcodeCheck_textView = findViewById(R.id.postcodeCheck_textView);
        submit_textView = findViewById(R.id.submit_textView);
        date_of_birth_text = findViewById(R.id.date_of_birth_text);


        idStorageReference = storageReference.child("Customer_IDs");
        profileImage_ref = storageReference.child("Customer_Profile_image");

        itemImage_recyclerView = findViewById(R.id.itemImage_recyclerView);
        itemImage_recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapterItemDetailsImagesRecyclerView = new AdapterItemDetailsImagesRecyclerView(Registration.this, imageUrlList);
        itemImage_recyclerView.setAdapter(adapterItemDetailsImagesRecyclerView);

    }

    private void onClickListeners() {
        removeImage_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileImage_imageView.setImageResource(R.drawable.profile_icon);
                profileImageUri = null;
                removeImage_textView.setVisibility(View.GONE);
            }
        });

        profileImage_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickerCheck = "profile";
                ImagePicker.Companion.with(Registration.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

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
                imagePickerCheck = "id";
                if (imageUrlList.size() < 2) {
                    ImagePicker.Companion.with(Registration.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start();
                } else {
                    Toast.makeText(Registration.this, "Maximum 2 images allowed!", Toast.LENGTH_SHORT).show();
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

                DatePickerDialog dialog = new DatePickerDialog(Registration.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
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

                if (validateFields()) {
                    accountCreation(ac_email.getText().toString(),ac_password.getText().toString());
                }
            }
        });
    }

    private void checkPostcode() {
        // SE13 6AZ
        String postcode = ac_postcode.getText().toString();
        latLng = getLocationFromAddress(Registration.this, postcode);

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
        if (ac_postcode.getText().toString().isEmpty()) {
            ac_postcode.setError("Please enter your postal code");
            valid = false;
        }
        if (ac_houseNo.getText().toString().isEmpty()) {
            ac_houseNo.setError("Please enter your Door no");
            valid = false;
        }

        if (ac_password.getText().toString().isEmpty()){
            ac_password.setError("Please enter password!");
            valid = false;
        }

        if (!ac_confirmPassword.getText().toString().equals(ac_password.getText().toString())){
            ac_confirmPassword.setError("Password do not match!");
            valid = false;
        }

        return valid;
    }

    private void accountCreation(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            detailsSubmit();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Registration.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void detailsSubmit() {
        pd.showProgressBar(Registration.this);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;

            key = reference.push().getKey();

            reference.child("Users_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("customer_id").setValue(key);

            reference.child("Customer_list").child(key).child("Name").setValue(ac_title.getText().toString());
            reference.child("Customer_list").child(key).child("Phone_no").setValue(String.valueOf(ccp.getFullNumberWithPlus()));
            reference.child("Customer_list").child(key).child("ID").setValue(ac_id.getText().toString());
            reference.child("Customer_list").child(key).child("DOB").setValue(date_of_birth_text.getText().toString());
            reference.child("Customer_list").child(key).child("Address").setValue(ac_address.getText().toString());
            reference.child("Customer_list").child(key).child("House_door").setValue(ac_houseNo.getText().toString());
            reference.child("Customer_list").child(key).child("Email").setValue(ac_email.getText().toString());
            reference.child("Customer_list").child(key).child("Postcode").setValue(ac_postcode.getText().toString());
            reference.child("Customer_list").child(key).child("key_id").setValue(key);
            reference.child("Customer_list").child(key).child("added_by").setValue(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
            reference.child("Customer_list").child(key).child("uid").setValue(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));

            for (i = 0; i<imageUrlList.size();i++) {
                System.out.println(i);
                idStorageReference.child(key).child("image_"+String.valueOf(i+1)).putFile(imageUrlList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        idStorageReference.child(key).child("image_"+String.valueOf(l+1)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                System.out.println("called");

                                reference.child("Customer_list").child(key).child("ID_Image_urls").child("image_"+(k+1)).setValue(String.valueOf(uri.toString()));

                                k++;

                                if (k==imageUrlList.size()){
                                    if (profileImageUri!=null){
                                        profileImage_ref.child(key).child("profile_image").putFile(profileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                profileImage_ref.child(key).child("profile_image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {

                                                        reference.child("Customer_list").child(key).child("profile_image").setValue(uri.toString());

                                                        reference.child("email_to_id").child(ac_email.getText().toString().replace(".", ",")).setValue(key);
                                                        reference.child("email_to_uid").child(ac_email.getText().toString().replace(".", ",")).setValue(mAuth.getCurrentUser().getUid());

                                                        pd.dismissProgressBar(Registration.this);
                                                        Intent intent = new Intent(Registration.this,SplashActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Registration.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                                                        pd.dismissProgressBar(Registration.this);
                                                    }
                                                });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Registration.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                                                pd.dismissProgressBar(Registration.this);
                                            }
                                        });

                                    }else {
                                        reference.child("Customer_list").child(key).child("profile_image").setValue(UserDetails.getInstance().getDefaultProfileImage());

                                        reference.child("email_to_id").child(ac_email.getText().toString().replace(".", ",")).setValue(key);
                                        reference.child("email_to_uid").child(ac_email.getText().toString().replace(".", ",")).setValue(mAuth.getCurrentUser().getUid());

                                        pd.dismissProgressBar(Registration.this);
                                        Intent intent = new Intent(Registration.this,SplashActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            }
                        });
                        l++;




                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registration.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                        pd.dismissProgressBar(Registration.this);
                    }
                });
            }


        } else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imagePickerCheck == "id"){
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
        }if (imagePickerCheck == "profile"){
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                profileImageUri = data.getData();
                profileImage_imageView.setImageURI(profileImageUri);

                removeImage_textView.setVisibility(View.VISIBLE);


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
package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterItemDetailsImagesRecyclerView;
import com.dotcom.rbs_system.Classes.RBSCustomerDetails;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Customer_details extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    RBSCustomerDetails rbsCustomerDetails;

    AdapterItemDetailsImagesRecyclerView adapterItemDetailsImagesRecyclerView;
    RecyclerView itemImage_recyclerView;

    List<Uri> imageUrlList;
    Uri fileUri;

    String currentDateString;
    String key;

    StorageReference storageReference;
    Progress_dialoge pd;

    Uri tempUri=null;
    Uri tempUri2=null;
    Uri tempUri3=null;

    StorageReference idStorageReference;

    ImageButton back_btn;
    ImageView id_imageView;

    TextView date_of_birth_text,uploadId_textView,date_textView,submit_textView;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    DatabaseReference reference;
    EditText ac_title, ac_phoneno, ac_id, ac_address, ac_email,ac_postalcode;

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

        imageUrlList = new ArrayList<>();
        rbsCustomerDetails.setImageUrlList(imageUrlList);

        storageReference = FirebaseStorage.getInstance().getReference();

        reference = FirebaseDatabase.getInstance().getReference();
        ac_title = (EditText) findViewById(R.id.ac_title);
        ac_phoneno = (EditText) findViewById(R.id.ac_phoneno);
        ac_id = (EditText) findViewById(R.id.ac_id);
        ac_address = (EditText) findViewById(R.id.ac_address);
        ac_email = (EditText) findViewById(R.id.ac_email);
        ac_postalcode = (EditText) findViewById(R.id.ac_postalcode);

        uploadId_textView = (TextView) findViewById(R.id.uploadId_textView);

        back_btn = (ImageButton) findViewById(R.id.back_btn);
        id_imageView = (ImageView) findViewById(R.id.id_imageView);

        date_textView = (TextView) findViewById(R.id.date_textView);
        submit_textView = (TextView) findViewById(R.id.submit_textView);
        date_of_birth_text = (TextView) findViewById(R.id.date_of_birth_text);


        idStorageReference = storageReference.child("Customer_IDs");

        itemImage_recyclerView = (RecyclerView) findViewById(R.id.itemImage_recyclerView);
        itemImage_recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapterItemDetailsImagesRecyclerView = new AdapterItemDetailsImagesRecyclerView(Customer_details.this,imageUrlList);
        itemImage_recyclerView.setAdapter(adapterItemDetailsImagesRecyclerView);


    }

    private void onClickListeners() {

        uploadId_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUrlList.size()<2){
                    ImagePicker.Companion.with(Customer_details.this)
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)			//Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start();
                }else {
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
              Calendar calendar=Calendar.getInstance();
              int year= (calendar.get(Calendar.YEAR))-18;
              int month= 0;
              int day= 1;

              DatePickerDialog dialog = new DatePickerDialog(Customer_details.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,onDateSetListener,year,month,day);
              currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
              dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
              dialog.show();
            }
        });
        onDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date = dayOfMonth+"/"+ month + "/"+ year;

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
                if (validateFields() == true) {
                    detailsSubmit();
                }
            }
        });

    }

    private boolean validateFields() {
        boolean valid = true;

        if (ac_title.getText().toString().isEmpty()) {
            ac_title.setError("Please enter your name");
            valid = false;
        }
        if (ac_title.length() > 32) {
            ac_title.setError("Name character limit is 32");
            valid = false;
        }
        if (ac_phoneno.getText().toString().isEmpty()) {
            ac_phoneno.setError("Please enter your phone number");
            valid = false;
        }
        if (ac_phoneno.length() < 11) {
            ac_phoneno.setError("Please enter valid contact no");
            valid = false;
        }
        if (ac_id.getText().toString().isEmpty()) {
            ac_id.setError("Please enter your id");
            valid = false;
        }
        if (imageUrlList.size()==0){
            Toast.makeText(this, "Please upload customer ID image", Toast.LENGTH_LONG).show();
            valid=false;
        }
        if (date_of_birth_text.getText().toString().equals("Select date")) {
            Toast.makeText(this, "Select date of birth", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (ac_address.getText().toString().isEmpty()) {
            ac_address.setError("Please enter your date of birth");
            valid = false;
        }
        if (ac_email.getText().toString().isEmpty()) {
            ac_email.setError("Please enter your email address");
            valid = false;
        }
        if (ac_postalcode.getText().toString().isEmpty()) {
            ac_postalcode.setError("Please enter your postal code");
            valid = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(String.valueOf(ac_email.getText())).matches()) {
            ac_email.setError("Please enter a valid email");
            valid = false;
        }


        return valid;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date_of_birth_text.setText(currentDateString);
    }

    private void detailsSubmit() {
        pd.showProgressBar(Customer_details.this);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Customer_details.this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            key = reference.push().getKey();
            rbsCustomerDetails.setCustomerName(ac_title.getText().toString());
            rbsCustomerDetails.setCustomerPhNo(ac_phoneno.getText().toString());
            rbsCustomerDetails.setCustomerId(ac_id.getText().toString());
            rbsCustomerDetails.setCustomerDob(date_of_birth_text.getText().toString());
            rbsCustomerDetails.setCustomerAddress(ac_address.getText().toString());
            rbsCustomerDetails.setCustomerEmail(ac_email.getText().toString());
            rbsCustomerDetails.setCustomerPostalCode(ac_postalcode.getText().toString());

            pass_back_data();
            pd.dismissProgressBar(Customer_details.this);
            finish();

        }
        else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
        }

    }

    private void pass_back_data() {

        // get the text from the EditText
        String ac_title_ = ac_title.getText().toString();
        String ac_id_ = ac_id.getText().toString();
        String ac_phone_no = ac_phoneno.getText().toString();
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


}

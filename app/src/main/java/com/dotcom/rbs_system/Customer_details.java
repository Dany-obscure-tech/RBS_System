package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.os.Environment;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class Customer_details extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String currentDateString;

    StorageReference storageReference;
    Progress_dialoge pd;

    Uri tempUri=null;
    Uri tempUri2=null;
    Uri tempUri3=null;

    StorageReference idStorageReference;

    ImageButton Back_btn;
    Button date_btn, submit_btn, uploadId_id_front,uploadId_id_back,profile_pic_upload;
    ImageView id_front,id_back,profile_pic;
    TextView date_of_birth_text;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    DatabaseReference reference;
    EditText ac_title, ac_phoneno, ac_id, ac_address, ac_email;

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
        storageReference = FirebaseStorage.getInstance().getReference();

        reference = FirebaseDatabase.getInstance().getReference();
        ac_title = (EditText) findViewById(R.id.ac_title);
        ac_phoneno = (EditText) findViewById(R.id.ac_phoneno);
        ac_id = (EditText) findViewById(R.id.ac_id);
        ac_address = (EditText) findViewById(R.id.ac_address);
        ac_email = (EditText) findViewById(R.id.ac_email);

        Back_btn = (ImageButton) findViewById(R.id.Back_btn);

        date_btn = (Button) findViewById(R.id.date_btn);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        uploadId_id_front = (Button) findViewById(R.id.uploadId_id_front);
        uploadId_id_back = (Button) findViewById(R.id.uploadId_id_back);
        profile_pic_upload = (Button) findViewById(R.id.profile_pic_upload);
        id_front = (ImageView) findViewById(R.id.id_front);
        id_back = (ImageView) findViewById(R.id.id_back);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        date_of_birth_text = (TextView) findViewById(R.id.date_of_birth_text);


        idStorageReference = storageReference.child("Customer_IDs");
    }

    private void onClickListeners() {


        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        date_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment datepicker = new DatePickerFragment();
//                datepicker.show(getSupportFragmentManager(), "date picker");
//            }
//        });
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Calendar calendar=Calendar.getInstance();
              int year= calendar.get(Calendar.YEAR);
              int month= calendar.get(Calendar.MONTH);
              int day= calendar.get(Calendar.DAY_OF_MONTH);
              DatePickerDialog dialog= new DatePickerDialog(
                      Customer_details.this,
                      android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                      onDateSetListener,
                      year,month,day);
                currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
              dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
              dialog.show();
            }
        });
        onDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date=month+ "/"+dayOfMonth+"/"+ year;
                date_of_birth_text.setText(currentDateString);
            }
        };


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields() == true) {
                    detailsSubmit();
                }
            }
        });

        uploadId_id_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);

            }
        });
        uploadId_id_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE2);

            }
        });
        profile_pic_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE3);

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
        if (tempUri==null){
            Toast.makeText(this, "Please upload front side of id", Toast.LENGTH_LONG).show();
            valid=false;
        }if (tempUri2==null){
            Toast.makeText(this, "Please upload back side of id", Toast.LENGTH_LONG).show();
            valid=false;
        }if (tempUri3==null){
            Toast.makeText(this, "Please upload a profile picture", Toast.LENGTH_LONG).show();
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

            final String key = reference.push().getKey();
            reference.child("Customer_list").child(key).child("Name").setValue(ac_title.getText().toString());
            reference.child("Customer_list").child(key).child("Phone_no").setValue(ac_phoneno.getText().toString());
            reference.child("Customer_list").child(key).child("ID").setValue(ac_id.getText().toString());
            reference.child("Customer_list").child(key).child("DOB").setValue(date_of_birth_text.getText().toString());
            reference.child("Customer_list").child(key).child("Address").setValue(ac_address.getText().toString());
            reference.child("Customer_list").child(key).child("Email").setValue(ac_email.getText().toString());
            reference.child("Customer_list").child(key).child("key_id").setValue(key);
            reference.child("Customer_list").child(key).child("added_by").setValue(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));

            idStorageReference.child(ac_id.getText().toString()).child("ID").putFile(tempUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Customer_details.this, "Uploading finished!", Toast.LENGTH_SHORT).show();

                    idStorageReference.child(ac_id.getText().toString()).child("ID").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            reference.child("Customer_list").child(key).child("id_image_url").setValue(String.valueOf(uri));
                            pd.dismissProgressBar(Customer_details.this);
                            finish();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismissProgressBar(Customer_details.this);
                    Toast.makeText(Customer_details.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                    Toast.makeText(Customer_details.this, "Not Submitted", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            id_front.setImageBitmap(imageBitmap);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            tempUri = getImageUri(getApplicationContext(), imageBitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH

        }
        if (requestCode == CAMERA_REQUEST_CODE2 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            id_back.setImageBitmap(imageBitmap);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            tempUri2 = getImageUri(getApplicationContext(), imageBitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH

        }
        if (requestCode == CAMERA_REQUEST_CODE3 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            profile_pic.setImageBitmap(imageBitmap);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            tempUri3 = getImageUri(getApplicationContext(), imageBitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH

        }



    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


}

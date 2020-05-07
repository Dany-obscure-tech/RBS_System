package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterSettingsFaultListRecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {

    DatabaseReference faultListRef,reference, userDataRef,rbsMessageRef;
    StorageReference storageReference;

    private Uri logoDocUri = null;
    private Uri bannerDocUri = null;

    private static final int LOGO_READ_REQUEST_CODE = 42;
    private static final int BANNER_READ_REQUEST_CODE = 43;

    LinearLayout logoButtons_linearLayout,bannerButtons_linearLayout;

    ImageView logo_imageView,banner_imageView;

    ImageButton Back_btn;
    Button addFaultAlertenter_btn,addFaultsAlertCancel_btn;
    Button alertEditProfileSave_btn,alertEditProfileCancel_btn;
    Button addFaults_btn;
    Button addLogo_button,addBanner_button,logoSave_button,bannerSave_button;
    Button logoCancel_button,bannerCancel_button;
    Button profileEdit_button;
    Button editConditions_button;
    Button alertEditConditionEnter_btn,alertEditConditionCancel_btn;

    RecyclerView faultList_recyclerView;
    AdapterSettingsFaultListRecyclerView adapterSettingsFaultListRecyclerView;

    Dialog addFaultDialog,editProfileDialog,editConditionDialog;

    //    Add fault alert
    EditText alertFaultName_editText,alertFaultPrice_editText;
    EditText alertEditProfileEmail_editText,alertEditProfilePhno_editText,alertEditProfileAddress_editText;
    EditText alertAddCategoryName_editText;

    List<String> faultNameList, faultPriceList, faultKeyIDList;

    String logoUrlString = "";
    String bannerUrlString = "";

    Boolean logoBannerUploadCheck = true;
    Boolean editProfileEmailCheck = false;
    Boolean editProfilePhnoCheck = false;
    Boolean editProfileAddressCheck = false;
    Boolean editConditionsCheck = false;

    TextView email_textView,phno_textView,address_textView;
    TextView rbsMessage_textView,conditions_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialize();
        checkUserData();

        getRBSMessage();
        getFaultsList();
        onClickListeners();
    }

    private void initialize() {
        storageReference = FirebaseStorage.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference();
        userDataRef = FirebaseDatabase.getInstance().getReference("Users_data/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
        faultListRef = FirebaseDatabase.getInstance().getReference("Listed_faults");
        rbsMessageRef = FirebaseDatabase.getInstance().getReference("Admin");

        logoButtons_linearLayout = (LinearLayout)findViewById(R.id.logoButtons_linearLayout);
        bannerButtons_linearLayout = (LinearLayout)findViewById(R.id.bannerButtons_linearLayout);

        logo_imageView = (ImageView)findViewById(R.id.logo_imageView);
        banner_imageView = (ImageView)findViewById(R.id.banner_imageView);

        Back_btn=(ImageButton)findViewById(R.id.Back_btn);
        addFaults_btn = (Button)findViewById(R.id.addFaults_btn);
        addLogo_button = (Button)findViewById(R.id.addLogo_button);
        addBanner_button = (Button)findViewById(R.id.addBanner_button);
        logoSave_button = (Button)findViewById(R.id.logoSave_button);
        bannerSave_button = (Button)findViewById(R.id.bannerSave_button);
        logoCancel_button = (Button)findViewById(R.id.logoCancel_button);
        bannerCancel_button = (Button)findViewById(R.id.bannerCancel_button);
        profileEdit_button = (Button)findViewById(R.id.profileEdit_button);
        editConditions_button = (Button)findViewById(R.id.editConditions_button);

        faultNameList = new ArrayList<>();
        faultPriceList = new ArrayList<>();
        faultKeyIDList = new ArrayList<>();

        faultList_recyclerView = (RecyclerView) findViewById(R.id.faultList_recyclerView);

        addFaultDialog = new Dialog(this);
        addFaultDialog.setContentView(R.layout.alert_setting_add_fault);
        alertFaultName_editText = addFaultDialog.findViewById(R.id.alertFaultName_editText);
        alertFaultPrice_editText = addFaultDialog.findViewById(R.id.alertFaultPrice_editText);
        addFaultAlertenter_btn = addFaultDialog.findViewById(R.id.addFaultAlertenter_btn);
        addFaultsAlertCancel_btn = addFaultDialog.findViewById(R.id.addFaultsAlertCancel_btn);
        editProfileDialog = new Dialog(this);
        editProfileDialog.setContentView(R.layout.alert_edit_profile);
        alertEditProfileEmail_editText = editProfileDialog.findViewById(R.id.alertEditProfileEmail_editText);
        alertEditProfilePhno_editText = editProfileDialog.findViewById(R.id.alertEditProfilePhno_editText);
        alertEditProfileAddress_editText = editProfileDialog.findViewById(R.id.alertEditProfileAddress_editText);
        alertEditProfileSave_btn = editProfileDialog.findViewById(R.id.alertEditProfileSave_btn);
        alertEditProfileCancel_btn = editProfileDialog.findViewById(R.id.alertEditProfileCancel_btn);
        editConditionDialog = new Dialog(this);
        editConditionDialog.setContentView(R.layout.alert_edit_conditions);
        alertAddCategoryName_editText = editConditionDialog.findViewById(R.id.alertAddCategoryName_editText);
        alertEditConditionEnter_btn = editConditionDialog.findViewById(R.id.alertEditConditionEnter_btn);
        alertEditConditionCancel_btn = editConditionDialog.findViewById(R.id.alertEditConditionCancel_btn);

        email_textView = (TextView)findViewById(R.id.email_textView);
        phno_textView = (TextView)findViewById(R.id.phno_textView);
        address_textView = (TextView)findViewById(R.id.address_textView);
        conditions_textView = (TextView)findViewById(R.id.conditions_textView);
        rbsMessage_textView = (TextView)findViewById(R.id.rbsMessage_textView);
    }

    private void checkUserData() {
        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("logo").exists()){
                    Picasso.get().load(String.valueOf(dataSnapshot.child("logo").getValue())).into(logo_imageView);
                    logoUrlString = String.valueOf(dataSnapshot.child("logo").getValue());
                    logo_imageView.setVisibility(View.VISIBLE);
                }
                if (dataSnapshot.child("banner").exists()){
                    Picasso.get().load(String.valueOf(dataSnapshot.child("banner").getValue())).into(banner_imageView);
                    bannerUrlString = String.valueOf(dataSnapshot.child("banner").getValue());
                    banner_imageView.setVisibility(View.VISIBLE);
                }
                if (dataSnapshot.child("profile_data").child("email").exists()){
                    email_textView.setText(dataSnapshot.child("profile_data").child("email").getValue().toString());
                }
                if (dataSnapshot.child("profile_data").child("phno").exists()){
                    phno_textView.setText(dataSnapshot.child("profile_data").child("phno").getValue().toString());
                }
                if (dataSnapshot.child("profile_data").child("address").exists()){
                    address_textView.setText(dataSnapshot.child("profile_data").child("address").getValue().toString());
                }
                if (dataSnapshot.child("conditions").exists()){
                    conditions_textView.setText(dataSnapshot.child("conditions").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getRBSMessage() {
        rbsMessageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("rbs_message").exists()){
                    rbsMessage_textView.setText(dataSnapshot.child("rbs_message").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onClickListeners() {
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addFaults_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFaultDialog.show();
            }
        });

        addFaultAlertenter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAlertAddFault()==true){
                    detailsSubmitAlertAddFault();
                }

            }
        });

        addFaultsAlertCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFaultDialog.dismiss();
            }
        });

        addLogo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (logoBannerUploadCheck){
                    logoBannerUploadCheck = false;
                    // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
                    // browser.
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                    // Filter to only show results that can be "opened", such as a
                    // file (as opposed to a list of contacts or timezones)
                    intent.addCategory(Intent.CATEGORY_OPENABLE);

                    // Filter to show only images, using the image MIME data type.
                    // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
                    // To search for all documents available via installed storage providers,
                    // it would be "*/*".
                    intent.setType("image/*");

                    startActivityForResult(intent, LOGO_READ_REQUEST_CODE);
                }else {
                    Toast.makeText(Settings.this, "Information pending", Toast.LENGTH_SHORT).show();
                }

            }
        });

        addBanner_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logoBannerUploadCheck){
                    logoBannerUploadCheck = false;
                    // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
                    // browser.
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                    // Filter to only show results that can be "opened", such as a
                    // file (as opposed to a list of contacts or timezones)
                    intent.addCategory(Intent.CATEGORY_OPENABLE);

                    // Filter to show only images, using the image MIME data type.
                    // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
                    // To search for all documents available via installed storage providers,
                    // it would be "*/*".
                    intent.setType("image/*");

                    startActivityForResult(intent, BANNER_READ_REQUEST_CODE);
                }else {
                    Toast.makeText(Settings.this, "Information pending", Toast.LENGTH_SHORT).show();
                }

            }
        });

        logoSave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Uploading",Toast.LENGTH_SHORT).show();

                UploadTask uploadTask = storageReference.child("Users_data").child("company_logos").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("logo").putFile(logoDocUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.child("Users_data").child("company_logos").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("logo").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                reference.child("Users_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("logo").setValue(String.valueOf(uri));
                                logoUrlString = String.valueOf(uri);
                                logoButtons_linearLayout.setVisibility(View.GONE);
                                logoDocUri = null;
                                logoBannerUploadCheck = true;
                            }
                        });
                        Toast.makeText(getApplicationContext(),"Uploaded Successful.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        bannerSave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Uploading",Toast.LENGTH_SHORT).show();

                UploadTask uploadTask = storageReference.child("Users_data").child("company_banners").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("banner").putFile(bannerDocUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.child("Users_data").child("company_banners").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("banner").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                reference.child("Users_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("banner").setValue(String.valueOf(uri));
                                bannerUrlString = String.valueOf(uri);
                                bannerButtons_linearLayout.setVisibility(View.GONE);
                                bannerDocUri = null;
                                logoBannerUploadCheck = true;

                            }
                        });
                        Toast.makeText(getApplicationContext(),"Uploaded Successful.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        logoCancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!logoUrlString.isEmpty()){
                    Picasso.get().load(logoUrlString).into(logo_imageView);
                }else {
                    logo_imageView.setVisibility(View.GONE);
                }
                logoButtons_linearLayout.setVisibility(View.GONE);
                logoDocUri = null;
                logoBannerUploadCheck = true;
            }
        });

        bannerCancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bannerUrlString.isEmpty()){
                    Picasso.get().load(bannerUrlString).into(banner_imageView);
                }else {
                    banner_imageView.setVisibility(View.GONE);
                }
                bannerButtons_linearLayout.setVisibility(View.GONE);
                bannerDocUri = null;
                logoBannerUploadCheck = true;
            }
        });

        profileEdit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertEditProfileEmail_editText.setText(email_textView.getText().toString());
                alertEditProfilePhno_editText.setText(phno_textView.getText().toString());
                alertEditProfileAddress_editText.setText(address_textView.getText().toString());

                alertEditProfileEmail_editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (alertEditProfileEmail_editText.getText().toString().equals(email_textView.getText().toString())){
                            alertEditProfileEmail_editText.setTextColor(getResources().getColor(R.color.textGrey));
                            editProfileEmailCheck = false;
                        }else {
                            alertEditProfileEmail_editText.setTextColor(getResources().getColor(R.color.textBlue));
                            editProfileEmailCheck = true;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                alertEditProfilePhno_editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (alertEditProfilePhno_editText.getText().toString().equals(phno_textView.getText().toString())){
                            alertEditProfilePhno_editText.setTextColor(getResources().getColor(R.color.textGrey));
                            editProfilePhnoCheck = false;
                        }else {
                            alertEditProfilePhno_editText.setTextColor(getResources().getColor(R.color.textBlue));
                            editProfilePhnoCheck = true;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                alertEditProfileAddress_editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (alertEditProfileAddress_editText.getText().toString().equals(address_textView.getText().toString())){
                            alertEditProfileAddress_editText.setTextColor(getResources().getColor(R.color.textGrey));
                            editProfileAddressCheck = false;
                        }else {
                            editProfileAddressCheck = true;
                            alertEditProfileAddress_editText.setTextColor(getResources().getColor(R.color.textBlue));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                editProfileDialog.show();
            }
        });

        alertEditProfileSave_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editProfileEmailCheck&&!editProfilePhnoCheck&&!editProfileAddressCheck){
                    Toast.makeText(Settings.this, "No changes", Toast.LENGTH_SHORT).show();
                }else {
                    if (editProfileEmailCheck){
                        userDataRef.child("profile_data").child("email").setValue(alertEditProfileEmail_editText.getText().toString());
                        email_textView.setText(alertEditProfileEmail_editText.getText().toString());
                        editProfileEmailCheck =false;
                    }
                    if (editProfilePhnoCheck){
                        userDataRef.child("profile_data").child("phno").setValue(alertEditProfilePhno_editText.getText().toString());
                        phno_textView.setText(alertEditProfilePhno_editText.getText().toString());
                        editProfilePhnoCheck = false;
                    }
                    if (editProfileAddressCheck){
                        userDataRef.child("profile_data").child("address").setValue(alertEditProfileAddress_editText.getText().toString());
                        address_textView.setText(alertEditProfileAddress_editText.getText().toString());
                        editProfileAddressCheck = false;
                    }
                    editProfileDialog.dismiss();
                }
            }
        });

        alertEditProfileCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileDialog.dismiss();
            }
        });

        editConditions_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertAddCategoryName_editText.setText(conditions_textView.getText().toString());
                alertAddCategoryName_editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (alertAddCategoryName_editText.getText().toString().equals(conditions_textView.getText().toString())){
                            editConditionsCheck=false;
                            alertAddCategoryName_editText.setTextColor(getResources().getColor(R.color.textGrey));
                        }else {
                            editConditionsCheck=true;
                            alertAddCategoryName_editText.setTextColor(getResources().getColor(R.color.textBlue));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                editConditionDialog.show();
            }
        });

        alertEditConditionEnter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editConditionsCheck){
                    userDataRef.child("conditions").setValue(alertAddCategoryName_editText.getText().toString());
                    conditions_textView.setText(alertAddCategoryName_editText.getText().toString());
                    editConditionsCheck = false;
                    editConditionDialog.dismiss();
                }else {
                    Toast.makeText(Settings.this, "No changes", Toast.LENGTH_SHORT).show();
                }

            }
        });

        alertEditConditionCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editConditionDialog.dismiss();
            }
        });



    }

    private void getFaultsList() {
        faultListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    faultNameList.add(String.valueOf(dataSnapshot1.child("Fault_name").getValue()));
                    faultPriceList.add(String.valueOf(dataSnapshot1.child("Fault_price").getValue()));
                    faultKeyIDList.add(String.valueOf(dataSnapshot1.child("key_id").getValue()));
                }

                adapterSettingsFaultListRecyclerView = new AdapterSettingsFaultListRecyclerView(Settings.this,faultNameList,faultPriceList,faultKeyIDList);
                faultList_recyclerView.setAdapter(adapterSettingsFaultListRecyclerView);
                faultList_recyclerView.setLayoutManager(new GridLayoutManager(Settings.this,1));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean validateAlertAddFault() {
        Boolean valid = true;

        if (alertFaultName_editText.getText().toString().isEmpty()){
            alertFaultName_editText.setError("enter fault name");
            valid = false;
        }
        if (alertFaultPrice_editText.getText().toString().isEmpty()){
            alertFaultPrice_editText.setError("enter fault price");
            valid = false;
        }

        return valid;
    }

    private void detailsSubmitAlertAddFault() {
        String key = faultListRef.push().getKey();

        faultListRef.child(key).child("Fault_name").setValue(alertFaultName_editText.getText().toString());
        faultListRef.child(key).child("Fault_price").setValue(alertFaultPrice_editText.getText().toString());
        faultListRef.child(key).child("added_by").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        faultListRef.child(key).child("key_id").setValue(key);

        faultNameList.add(alertFaultName_editText.getText().toString());
        faultKeyIDList.add(key);
        faultPriceList.add(alertFaultPrice_editText.getText().toString());

        adapterSettingsFaultListRecyclerView.notifyDataSetChanged();
        addFaultDialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == LOGO_READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                logoDocUri = uri;
                logo_imageView.setImageURI(null);
                logo_imageView.setImageURI(logoDocUri);

                logo_imageView.setVisibility(View.VISIBLE);
                logoButtons_linearLayout.setVisibility(View.VISIBLE);
            }else {
                if (logoDocUri!=null){
                    logoBannerUploadCheck = false;
                }else {
                    logoBannerUploadCheck = true;
                }
            }
        }
        else if (requestCode == BANNER_READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                bannerDocUri = uri;
                banner_imageView.setImageURI(null);
                banner_imageView.setImageURI(bannerDocUri);

                banner_imageView.setVisibility(View.VISIBLE);
                bannerButtons_linearLayout.setVisibility(View.VISIBLE);
            }else {
                if (bannerDocUri!=null){
                    logoBannerUploadCheck = false;
                }else {
                    logoBannerUploadCheck = true;
                }
            }
        }
        else{
            logoBannerUploadCheck=true;
        }
    }


}

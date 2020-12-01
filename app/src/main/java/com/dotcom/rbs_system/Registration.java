package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Registration extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    DatePickerDialog.OnDateSetListener onDateSetListener;

    private FirebaseAuth mAuth;

    int profileImagOrCnic = 0;

    private static final int RC_SIGN_IN = 9001;

    StorageReference profileImageStorageReference;
    private GoogleSignInClient mGoogleSignInClient;
    Uri fileUri,idUri;
    ImageView id_front,id_imageView;
    String currentDateString;
    TextView date_of_birth_text;
    FirebaseAuth fAuth;
    DatabaseReference userRef;
    Button button_register,date_btn,uploadId_profile_image,uploadId_id_image,button_google;
    EditText editText_fullName,editText_contactNo,editText_address,editText_email,editText_password,editText_confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        Initialization();
        ClickListeners();
        Processes();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialization() {
        mAuth = FirebaseAuth.getInstance();

        profileImageStorageReference = FirebaseStorage.getInstance().getReference();

        id_front = (ImageView) findViewById(R.id.id_front);
        id_imageView = (ImageView) findViewById(R.id.id_imageView);

        date_of_birth_text = (TextView) findViewById(R.id.date_of_birth_text);

        userRef = FirebaseDatabase.getInstance().getReference("Users_data");

        fAuth = FirebaseAuth.getInstance();

        uploadId_profile_image = (Button) findViewById(R.id.uploadId_profile_image);
        uploadId_id_image = (Button) findViewById(R.id.uploadId_id_image);
        button_google = (Button) findViewById(R.id.button_google);
        date_btn = (Button) findViewById(R.id.date_btn);
        button_register = (Button) findViewById(R.id.button_register);

        editText_fullName = (EditText)findViewById(R.id.editText_fullName);
        editText_contactNo = (EditText)findViewById(R.id.editText_contactNo);
        editText_email = (EditText)findViewById(R.id.editText_email);
        editText_password = (EditText)findViewById(R.id.editText_password);
        editText_address = (EditText)findViewById(R.id.editText_address);
        editText_confirmPassword = (EditText)findViewById(R.id.editText_confirmPassword);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void ClickListeners() {
        googleSign_in_button_listner();
        registerButtonClick();
        date_btnClisckListner();
        takeProfileImage();
        takeIdImage();
    }

    private void googleSign_in_button_listner() {
        button_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });
    }

    private void takeProfileImage() {
        uploadId_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileImagOrCnic = 1;
                ImagePicker.Companion.with(Registration.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    private void takeIdImage() {
        uploadId_id_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileImagOrCnic = 2;
                ImagePicker.Companion.with(Registration.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    private void registerButtonClick() {
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }

            private void register() {
                final String fullname = editText_fullName.getText().toString();
                final String contactno = editText_contactNo.getText().toString();
                final String dob = date_of_birth_text.getText().toString();
                final String address = editText_address.getText().toString();
                final String email = editText_email.getText().toString();
                String password = editText_password.getText().toString();
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            final String userID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            Toast.makeText(Registration.this, "Registered!", Toast.LENGTH_SHORT).show();
                            userRef.child(userID).child("type").setValue("customer");
                            userRef.child(userID).child("fullname").setValue(fullname);
                            userRef.child(userID).child("contactno").setValue(contactno);
                            userRef.child(userID).child("dob").setValue(dob);
                            userRef.child(userID).child("address").setValue(address);
                            userRef.child(userID).child("email").setValue(email);

                            profileImageStorageReference.child("BuyLocal_Customer_Profile_image").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile_image").putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    profileImageStorageReference.child("BuyLocal_Customer_Profile_image").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile_image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            userRef.child(userID).child("profile_image_url").setValue(String.valueOf(uri));
                                            profileImageStorageReference.child("BuyLocal_Customer_Id_image").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ID").putFile(idUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    Toast.makeText(Registration.this, "Uploading finished!", Toast.LENGTH_SHORT).show();

                                                    profileImageStorageReference.child("BuyLocal_Customer_Id_image").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ID").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            userRef.child(userID).child("id_image_url").setValue(String.valueOf(uri));
                                                            Intent intent = new Intent(Registration.this, BuyLocal_main.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            finish();
                                                            startActivity(intent);
                                                        }
                                                    });

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Registration.this, "Not Submitted", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Registration.this, "Not Submitted", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }
                });
                fAuth.createUserWithEmailAndPassword(email,password).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registration.this, String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void date_btnClisckListner() {
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year= (calendar.get(Calendar.YEAR))-18;
                int month= 0;
                int day= 1;

                DatePickerDialog dialog = new DatePickerDialog(Registration.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,onDateSetListener,year,month,day);
                currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Processes() {
        createRequest();
        onDatesetListner();
    }

    private void createRequest() {
        //Sign In scope
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //
        //Google Api Client
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
    }

    private void onDatesetListner() {
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
    }

    private boolean validate(){
        boolean valid = true;

        return valid;
    }
    private boolean validateGoogleSigninFields(){
        boolean valid = true;

        return valid;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date_of_birth_text.setText(currentDateString);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            System.out.println("called here if");

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
        else if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            if(profileImagOrCnic == 1){
                fileUri = data.getData();
                id_front.setImageURI(fileUri);
                fileUri = data.getData();
                profileImagOrCnic = 0;
            }else {
                idUri = data.getData();
                id_imageView.setImageURI(idUri);
                idUri = data.getData();
                profileImagOrCnic = 0;
            }


            //You can get File object from intent
//            val file:File = ImagePicker.getFile(data)!!

                    //You can also get File Path from intent
//                    val filePath:String = ImagePicker.getFilePath(data)!!
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            GoogleSignInAccount signInAccount= GoogleSignIn.getLastSignedInAccount(Registration.this);

                            final String fullname = signInAccount.getDisplayName();
                            final String contactno = editText_contactNo.getText().toString();
                            final String dob = date_of_birth_text.getText().toString();
                            final String address = editText_address.getText().toString();
                            final String email = signInAccount.getEmail();

                            final String userID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            Toast.makeText(Registration.this, "Registered!", Toast.LENGTH_SHORT).show();
                            userRef.child(userID).child("type").setValue("customer");
                            userRef.child(userID).child("fullname").setValue(fullname);
                            userRef.child(userID).child("contactno").setValue(contactno);
                            userRef.child(userID).child("dob").setValue(dob);
                            userRef.child(userID).child("address").setValue(address);
                            userRef.child(userID).child("email").setValue(email);

                            profileImageStorageReference.child("BuyLocal_Customer_Profile_image").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile_image").putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    profileImageStorageReference.child("BuyLocal_Customer_Profile_image").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile_image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            userRef.child(userID).child("profile_image_url").setValue(String.valueOf(uri));
                                            profileImageStorageReference.child("BuyLocal_Customer_Id_image").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ID").putFile(idUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    Toast.makeText(Registration.this, "Uploading finished!", Toast.LENGTH_SHORT).show();

                                                    profileImageStorageReference.child("BuyLocal_Customer_Id_image").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ID").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            userRef.child(userID).child("id_image_url").setValue(String.valueOf(uri));
                                                            Intent intent = new Intent(Registration.this, BuyLocal_main.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            finish();
                                                            startActivity(intent);
                                                        }
                                                    });

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Registration.this, "Not Submitted", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Registration.this, "Not Submitted", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Registration.this, "Sorry authentication failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
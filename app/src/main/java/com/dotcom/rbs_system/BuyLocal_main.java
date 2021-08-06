package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Classes.UserDetails;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class BuyLocal_main extends AppCompatActivity {
    DatabaseReference customerProfileImageRef;
    StorageReference customerProfileImageStorageReference;

    BottomNavigationView bottomNavigationView;
    Dialog confirmation_alert;
    TextView yes_btn_textview, cancel_btn_textview;

    final BuyLocal_home fragment_buyLocalhome = new BuyLocal_home();
    final BuyLocal_Profile fragment_BuyLocal_profile = new BuyLocal_Profile();
    final BuyLocal_About fragment_BuyLocal_about = new BuyLocal_About();

    private long lastPressedTime;

    private static final int PERIOD = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_main);

        customerProfileImageStorageReference = FirebaseStorage.getInstance().getReference().child("Customer_Profile_image/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        customerProfileImageRef = FirebaseDatabase.getInstance().getReference("Users_data/"+FirebaseAuth.getInstance().getCurrentUser().getUid());

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        confirmation_alert = new Dialog(this);
        confirmation_alert.setContentView(R.layout.exit_confirmation_alert);
        yes_btn_textview = (TextView) confirmation_alert.findViewById(R.id.yes_btn_textview);
        cancel_btn_textview = (TextView) confirmation_alert.findViewById(R.id.cancel_btn_textview);

        //        To remove default colors of icons
        bottomNavigationView.setItemIconTintList(null);
        // Fragment home already selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, fragment_buyLocalhome).commit();
        // Bottom navigation menu fragments connected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        // Switch to page one
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, fragment_buyLocalhome).commit();
                        break;
                    case R.id.profile:
                        // Switch to page two
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, fragment_BuyLocal_profile,"FRAGMENT_PROFILE").commit();
                        break;
                    case R.id.about:
                        // Switch to page two
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, fragment_BuyLocal_about).commit();
                        break;
                }
                return true;
            }
        });
        setNavMenuItemThemeColors(Color.parseColor("#0075B5"));

        onClickListners();
    }

    private void onClickListners() {
        yes_btn_textview_listner();
        cancel_btn_textview_listner();
    }

    private void cancel_btn_textview_listner() {
        cancel_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmation_alert.dismiss();
            }
        });
    }

    private void yes_btn_textview_listner() {
        yes_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //Icon color changed when tap on icon
    public void setNavMenuItemThemeColors(int color) {
        //Setting default colors for menu item Text and Icon
        int navDefaultTextColor = Color.parseColor("#202020");
        int navDefaultIconColor = Color.parseColor("#737373");

        //Defining ColorStateList for menu item Text
        ColorStateList navMenuTextList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed}
                },
                new int[]{
                        color,
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor
                }
        );

        //Defining ColorStateList for menu item Icon
        ColorStateList navMenuIconList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed}
                },
                new int[]{
                        color,
                        navDefaultIconColor,
                        navDefaultIconColor,
                        navDefaultIconColor,
                        navDefaultIconColor
                }
        );

        bottomNavigationView.setItemTextColor(navMenuTextList);
        bottomNavigationView.setItemIconTintList(navMenuIconList);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();

            Toast.makeText(this, String.valueOf(fileUri), Toast.LENGTH_SHORT).show();

            customerProfileImageStorageReference.child("Profile_image").putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    customerProfileImageStorageReference.child("Profile_image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            customerProfileImageRef.child("profile_image_url").setValue(String.valueOf(uri.toString()));
                            UserDetails.getInstance().setProfileImageUrl(String.valueOf(uri.toString()));

                            getSupportFragmentManager().beginTransaction().detach(getSupportFragmentManager().findFragmentByTag("FRAGMENT_PROFILE")).attach(getSupportFragmentManager().findFragmentByTag("FRAGMENT_PROFILE")).commit();
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BuyLocal_main.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            });

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
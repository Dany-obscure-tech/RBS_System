package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Classes.InvoiceNumberGenerator;
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

import java.text.ParseException;

public class BuyLocal_main extends AppCompatActivity {
    Progress_dialog pd = new Progress_dialog();
    DatabaseReference customerProfileImageRef;
    StorageReference customerProfileImageStorageReference;

    BottomNavigationView bottomNavigationView;
    Dialog confirmation_alert;
    TextView yes_btn_textview, cancel_btn_textview;

    final BuyLocal_home fragment_buyLocalhome = new BuyLocal_home();
    final BuyLocal_Profile fragment_BuyLocal_profile = new BuyLocal_Profile();
    final BuyLocal_inbox fragment_BuyLocal_inbox = new BuyLocal_inbox();
    final BuyLocal_offers fragment_BuyLocal_offers = new BuyLocal_offers();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_main);

        customerProfileImageStorageReference = FirebaseStorage.getInstance().getReference().child("Customer_Profile_image/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        customerProfileImageRef = FirebaseDatabase.getInstance().getReference("Users_data/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

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
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, fragment_BuyLocal_profile, "FRAGMENT_PROFILE").commit();
                        break;
                    case R.id.inbox:
                        // Switch to page two
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, fragment_BuyLocal_inbox).commit();
                        break;

                    case R.id.offers:
                        // Switch to page two
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, fragment_BuyLocal_offers).commit();
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
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                confirmation_alert.show();

                return true;
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == 2) {
            getSupportFragmentManager().beginTransaction().detach(getSupportFragmentManager().findFragmentByTag("FRAGMENT_PROFILE")).attach(getSupportFragmentManager().findFragmentByTag("FRAGMENT_PROFILE")).commit();
        } else {
            pd.showProgressBar(BuyLocal_main.this);
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                Uri fileUri = data.getData();

                customerProfileImageStorageReference.child("Profile_image").putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        customerProfileImageStorageReference.child("Profile_image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                customerProfileImageRef.child("profile_image_url").setValue(String.valueOf(uri.toString()));
                                UserDetails.getInstance().setProfileImageUrl(String.valueOf(uri.toString()));
                                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).remove(fragment_BuyLocal_profile).commit();
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        pd.dismissProgressBar(BuyLocal_main.this);
                                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, fragment_BuyLocal_profile).commit();

                                    }
                                }, 1000);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismissProgressBar(BuyLocal_main.this);
                    }
                });
            }
        }


    }
}
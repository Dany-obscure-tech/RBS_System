package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.dotcom.rbs_system.Classes.BuylocalSlider;
import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.Classes.TermsAndConditions;
import com.dotcom.rbs_system.Classes.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    DatabaseReference RequiredAdminDataRef;
    Currency currencyObj;
    TermsAndConditions termsAndConditionsObj;
    List<String> buylocalsliderlist;
    BuylocalSlider buylocalSlider;
    DatabaseReference userRef,customerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Initialization();

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.INTERNET,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                new Handler().postDelayed(new Runnable() {

                    // Using handler with postDelayed called runnable run method

                    @Override

                    public void run() {
                        checkExistingUser();

                    }

                }, 0);


            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();

    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialization() {
        buylocalsliderlist = new ArrayList<>();

        RequiredAdminDataRef = FirebaseDatabase.getInstance().getReference("Initial_load_data/");
        currencyObj = Currency.getInstance();
        termsAndConditionsObj = TermsAndConditions.getInstance();
        buylocalSlider = BuylocalSlider.getInstance();

        InitialDataFetch();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void checkExistingUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            userTypeCheck();
        } else {
            Intent i = new Intent(SplashActivity.this, SignInActivity.class);

            startActivity(i);

            // close this activity

            finish();
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialDataFetch() {
        RequiredAdminDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currencyObj.setCurrency(dataSnapshot.child("currency").getValue().toString());
                termsAndConditionsObj.setTermsAndConditions(dataSnapshot.child("rbs_termsandconditions").getValue().toString());
                UserDetails.getInstance().setDefaultProfileImage(dataSnapshot.child("default_profile_pic").getValue().toString());
                if (dataSnapshot.child("buylocal_slider").exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        buylocalsliderlist.add(String.valueOf(dataSnapshot1.getValue()));
                    }
                    buylocalSlider.setBuylocalSliderList(buylocalsliderlist);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, BuyLocal_main.class);
        startActivity(intent);
        finish();
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void userTypeCheck() {
        userRef = FirebaseDatabase.getInstance().getReference("Users_data/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails.getInstance().setCustomerID(dataSnapshot.child("customer_id").getValue().toString());

                customerRef = FirebaseDatabase.getInstance().getReference("Customer_list/"+UserDetails.getInstance().getCustomerID());
                customerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserDetails.getInstance().setName(snapshot.child("Name").getValue().toString());
                        UserDetails.getInstance().setPhno(snapshot.child("Phone_no").getValue().toString());
                        UserDetails.getInstance().setAddress(snapshot.child("Address").getValue().toString());
                        UserDetails.getInstance().setEmail(snapshot.child("Email").getValue().toString());
                        UserDetails.getInstance().setProfileImageUrl(snapshot.child("profile_image").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                if (dataSnapshot.child("Shopkeeper_details").exists()) {
                    UserDetails.getInstance().setShopkeeper(true);
                    UserDetails.getInstance().setShopAddress(dataSnapshot.child("Shopkeeper_details").child("shop_address").getValue().toString());
                    UserDetails.getInstance().setShopBanner(dataSnapshot.child("Shopkeeper_details").child("shop_banner").getValue().toString());
                    UserDetails.getInstance().setShopEmail(dataSnapshot.child("Shopkeeper_details").child("shop_email").getValue().toString());
                    UserDetails.getInstance().setShopLogo(dataSnapshot.child("Shopkeeper_details").child("shop_logo").getValue().toString());
                    UserDetails.getInstance().setShopName(dataSnapshot.child("Shopkeeper_details").child("shop_name").getValue().toString());
                    UserDetails.getInstance().setShopPhno(dataSnapshot.child("Shopkeeper_details").child("shop_phno").getValue().toString());
                    UserDetails.getInstance().setShopTermsAndConditions(dataSnapshot.child("Shopkeeper_details").child("shop_termsandconditions").getValue().toString());
                } else {
                    UserDetails.getInstance().setShopkeeper(false);
                }

                if (dataSnapshot.child("Vendor_details").exists()) {
                    UserDetails.getInstance().setVendor(true);
                    UserDetails.getInstance().setVendorAddress(dataSnapshot.child("Vendor_details").child("vendor_address").getValue().toString());
                    UserDetails.getInstance().setVendorBanner(dataSnapshot.child("Vendor_details").child("vendor_banner").getValue().toString());
                    UserDetails.getInstance().setVendorEmail(dataSnapshot.child("Vendor_details").child("vendor_email").getValue().toString());
                    UserDetails.getInstance().setVendorLogo(dataSnapshot.child("Vendor_details").child("vendor_logo").getValue().toString());
                    UserDetails.getInstance().setVendorName(dataSnapshot.child("Vendor_details").child("vendor_name").getValue().toString());
                    UserDetails.getInstance().setVendorPhno(dataSnapshot.child("Vendor_details").child("vendor_phno").getValue().toString());
                    UserDetails.getInstance().setVendorTermsAndConditions(dataSnapshot.child("Vendor_details").child("vendor_termsandconditions").getValue().toString());
                    UserDetails.getInstance().setVendorPostCode(dataSnapshot.child("Vendor_details").child("vendor_postcode").getValue().toString());
                    UserDetails.getInstance().setVendorAppRegNo(dataSnapshot.child("Vendor_details").child("vendor_appregno").getValue().toString());
                    UserDetails.getInstance().setVendorRegNo(dataSnapshot.child("Vendor_details").child("vendor_regno").getValue().toString());
                    UserDetails.getInstance().setVendorUrl(dataSnapshot.child("Vendor_details").child("vendor_url").getValue().toString());
                } else {
                    UserDetails.getInstance().setVendor(false);
                }

                startMainActivity();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


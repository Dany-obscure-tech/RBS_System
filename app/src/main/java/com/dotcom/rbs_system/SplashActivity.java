package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.dotcom.rbs_system.Classes.BuylocalSlider;
import com.dotcom.rbs_system.Classes.Currency;
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
    List<String> buylocalsliderlist;
    BuylocalSlider buylocalSlider;
    DatabaseReference userRef;

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
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                new Handler().postDelayed(new Runnable() {

                    // Using handler with postDelayed called runnable run method

                    @Override

                    public void run() {
                        checkExistingUser();

                    }

                }, 0);


            }

            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();

    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialization() {
        buylocalsliderlist = new ArrayList<>();

        RequiredAdminDataRef = FirebaseDatabase.getInstance().getReference("Initial_load_data/");
        currencyObj = Currency.getInstance();
        buylocalSlider = BuylocalSlider.getInstance();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void checkExistingUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() !=null){

            userTypeCheck();
        }else {
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
                if (dataSnapshot.child("buylocal_slider").exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        buylocalsliderlist.add(String.valueOf(dataSnapshot1.getValue()));
                    }
                    buylocalSlider.setBuylocalSliderList(buylocalsliderlist);
                    startMainActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this,BuyLocal_main.class);
        startActivity(intent);
        finish();
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void userTypeCheck() {
        userRef = FirebaseDatabase.getInstance().getReference("Users_data/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Shopkeeper_details").exists()){
                    UserDetails.getInstance().setShopkeeper(true);
                    UserDetails.getInstance().setShopLogo(dataSnapshot.child("Shopkeeper_details").child("shop_logo").getValue().toString());
                    UserDetails.getInstance().setShopNmae(dataSnapshot.child("Shopkeeper_details").child("shop_name").getValue().toString());
                    UserDetails.getInstance().setShopBanner(dataSnapshot.child("Shopkeeper_details").child("shop_banner").getValue().toString());
                }else {
                    UserDetails.getInstance().setShopkeeper(false);
                }

                InitialDataFetch();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
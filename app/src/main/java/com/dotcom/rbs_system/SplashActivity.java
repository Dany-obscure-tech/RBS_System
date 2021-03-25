package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.dotcom.rbs_system.Classes.BuylocalSlider;
import com.dotcom.rbs_system.Classes.Currency;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    DatabaseReference currencyRef,buylocalSliderRef;
    Currency currencyObj;
    List<String> buylocalsliderlist;
    BuylocalSlider buylocalSlider;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Initialization();

        new Handler().postDelayed(new Runnable() {

// Using handler with postDelayed called runnable run method

            @Override

            public void run() {
                checkExistingUser();
            }

        }, 2*1000);

    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialization() {
        buylocalsliderlist = new ArrayList<>();

        userRef = FirebaseDatabase.getInstance().getReference("Users_data");

        currencyRef = FirebaseDatabase.getInstance().getReference("Admin/currency");
        buylocalSliderRef = FirebaseDatabase.getInstance().getReference("Admin/buylocal_slider");
        currencyObj = Currency.getInstance();
        buylocalSlider = BuylocalSlider.getInstance();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void checkExistingUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() !=null){
            InitialDataFetch();
            userTypeCheck(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        }else {
            Intent i = new Intent(SplashActivity.this, SignInActivity.class);

            startActivity(i);

            // close this activity

            finish();
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialDataFetch() {
        currencyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currencyObj.setCurrency(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buylocalSliderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
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

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void userTypeCheck(final String uid) {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(uid).child("type").exists()){
                    String type = dataSnapshot.child(uid).child("type").getValue().toString();
                    if (type.equals("customer")){
                        Intent intent = new Intent(SplashActivity.this,BuyLocal_main.class);
                        startActivity(intent);
                        finish();
                    }if (type.equals("shop keeper")){
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } if (type.equals("vendor")){
                        Intent intent = new Intent(SplashActivity.this, VendorMainScreen.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

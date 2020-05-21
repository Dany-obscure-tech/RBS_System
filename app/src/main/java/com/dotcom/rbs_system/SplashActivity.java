package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.dotcom.rbs_system.Classes.Currency;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    DatabaseReference currencyRef;
    Currency currencyObj;

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
        currencyRef = FirebaseDatabase.getInstance().getReference("Admin/currency");
        currencyObj = Currency.getInstance();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void checkExistingUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() !=null){
            InitialDataFetch();

            Intent i = new Intent(SplashActivity.this, MainActivity.class);

            startActivity(i);

            // close this activity

            finish();

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
    }
}

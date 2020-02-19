package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

// Using handler with postDelayed called runnable run method

            @Override

            public void run() {
                checkExistingUser();
            }

        }, 2*1000);

    }

    private void checkExistingUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() !=null){
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
}

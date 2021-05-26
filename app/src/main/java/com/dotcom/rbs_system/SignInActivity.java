package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    EditText editText_email,editText_password;
    Button button_signin,button_register;
    FirebaseAuth mAuth;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialize();
        signin();
    }

    private void initialize() {
        editText_email = (EditText)findViewById(R.id.editText_email);
        editText_password = (EditText)findViewById(R.id.editText_password);
        button_signin = (Button)findViewById(R.id.button_signin);
        button_register = (Button)findViewById(R.id.button_register);
        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
//                validateFields();
//                if (validateFields()== true){
//
//                }
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        mAuth = FirebaseAuth.getInstance();

        userRef = FirebaseDatabase.getInstance().getReference("Users_data");



    }

//    private boolean validateFields() {
//        boolean valid = true;
//        if (editText_email.getText().toString().isEmpty()){
//            Toast.makeText(this, "Please select email", Toast.LENGTH_LONG).show();
//            valid = false;
//        }
//        if (editText_password.getText().toString().isEmpty()){
//            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
//            valid=false;
//        }
//        return valid;
//    }


    private void signin() {
        String email, password;
        email = editText_email.getText().toString();
        password = editText_password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignInActivity.this,SplashActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void register() {
        Intent intent = new Intent(SignInActivity.this,Registration.class);
        startActivity(intent);
    }



}
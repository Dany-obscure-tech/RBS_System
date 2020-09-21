package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    FirebaseAuth fAuth;
    DatabaseReference userRef;
    Button button_register;
    EditText editText_fullName,editText_contactNo,editText_email,editText_password,editText_confirmPassword;

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
        userRef = FirebaseDatabase.getInstance().getReference("Users_data");

        fAuth = FirebaseAuth.getInstance();

        button_register = (Button) findViewById(R.id.button_register);

        editText_fullName = (EditText)findViewById(R.id.editText_fullName);
        editText_contactNo = (EditText)findViewById(R.id.editText_contactNo);
        editText_email = (EditText)findViewById(R.id.editText_email);
        editText_password = (EditText)findViewById(R.id.editText_password);
        editText_confirmPassword = (EditText)findViewById(R.id.editText_confirmPassword);
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void ClickListeners() {
        registerButtonClick();
    }

    private void registerButtonClick() {
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }

            private void register() {
                String email = editText_email.getText().toString();
                String password = editText_email.getText().toString();
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String userID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            Toast.makeText(Registration.this, "Registered!", Toast.LENGTH_SHORT).show();
                            userRef.child(userID).child("type").setValue("customer");
                            Intent intent = new Intent(Registration.this, BuyLocal_main.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
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


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Processes() {
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class RegisterationEmailCheck extends AppCompatActivity {

    EditText email_editText;
    TextView button_next_textview;
    DatabaseReference emailToUidRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration_email_check);

        Initialization();
        OnClicks();
    }

    private void Initialization() {
        emailToUidRef = FirebaseDatabase.getInstance().getReference("email_to_uid");

        email_editText = findViewById(R.id.email_editText);
        button_next_textview = findViewById(R.id.button_next_textview);
    }

    private void OnClicks() {
        button_next_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email_editText.getText().toString().isEmpty()) {
                    email_editText.setError("Please enter correct email");

                }else if (!Patterns.EMAIL_ADDRESS.matcher(String.valueOf(email_editText.getText())).matches()) {
                    email_editText.setError("Please enter a valid email");
                }else {
                    emailToUidRef.child(String.valueOf(email_editText.getText()).replace(".", ",").toLowerCase(Locale.ROOT)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                email_editText.setError("Already an account!");
                            }else {
                                Intent intent = new Intent(RegisterationEmailCheck.this,Registration.class);
                                intent.putExtra("EMAIL",String.valueOf(email_editText.getText()).toLowerCase(Locale.ROOT));
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }


}
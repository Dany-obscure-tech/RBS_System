package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Classes.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BuyLocal_profile_edit extends AppCompatActivity {

    ImageButton back_btn;
    TextView submit_textView;
    TextView name_textView,phno_textView,address_textView;
    EditText name_editText,phno_editText,address_editText;
    DatabaseReference userDetailsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_profile_edit);

        userDetailsRef = FirebaseDatabase.getInstance().getReference("Users_data/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        back_btn=(ImageButton)findViewById(R.id.back_btn);

        submit_textView=(TextView) findViewById(R.id.submit_textView);

        name_textView=(TextView) findViewById(R.id.name_textView);
        phno_textView=(TextView) findViewById(R.id.phno_textView);
        address_textView=(TextView) findViewById(R.id.address_textView);

        name_textView.setText(UserDetails.getInstance().getName());
        phno_textView.setText(UserDetails.getInstance().getPhno());
        address_textView.setText(UserDetails.getInstance().getAddress());

        name_editText=(EditText) findViewById(R.id.name_editText);
        phno_editText=(EditText) findViewById(R.id.phno_editText);
        address_editText=(EditText) findViewById(R.id.address_editText);

        onclicklistners();
    }

    private void onclicklistners() {
        back_btn_listner();
        submit_textView_listener();
    }

    private void submit_textView_listener() {
        submit_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    if (!name_editText.getText().toString().isEmpty()){
                        UserDetails.getInstance().setName(name_editText.getText().toString());
                        userDetailsRef.child("fullname").setValue(name_editText.getText().toString());
                    }
                    if (!phno_editText.getText().toString().isEmpty()){
                        UserDetails.getInstance().setPhno(phno_editText.getText().toString());
                        userDetailsRef.child("phno").setValue(phno_editText.getText().toString());
                    }
                    if (!address_textView.getText().toString().isEmpty()){
                        UserDetails.getInstance().setAddress(address_textView.getText().toString());
                        userDetailsRef.child("address").setValue(address_textView.getText().toString());
                    }
                    Intent returnIntent = new Intent();
                    setResult(2,returnIntent);
                    finish();
                }
            }
        });
    }

    private void back_btn_listner() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean validate() {
        Boolean valid = true;

        if (name_editText.getText().toString().isEmpty()&&phno_editText.getText().toString().isEmpty()&&address_textView.getText().toString().isEmpty()){
            Toast.makeText(BuyLocal_profile_edit.this, "Please enter new info", Toast.LENGTH_SHORT).show();
            valid = false;
        }else {
            if (name_editText.getText().toString().equals(UserDetails.getInstance().getName())){
                name_editText.setError("Please enter new name");
                valid = false;
            }

            if (phno_editText.getText().toString().equals(UserDetails.getInstance().getPhno())){
                phno_editText.setError("Please enter new Phno");
                valid = false;
            }

            if (address_editText.getText().toString().equals(UserDetails.getInstance().getAddress())){
                address_editText.setError("Please enter new address");
                valid = false;
            }
        }


        return valid;
    }
}
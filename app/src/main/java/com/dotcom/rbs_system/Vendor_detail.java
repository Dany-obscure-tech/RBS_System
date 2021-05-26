package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Vendor_detail extends AppCompatActivity {

    String key;

    ImageButton Back_btn;

    Button submit_btn;

    EditText ac_title, ac_phoneno;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_detail);

        Initialize();
        ClickListeners();
//        Processes();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Initialize() {

        reference = FirebaseDatabase.getInstance().getReference();

        Back_btn = (ImageButton) findViewById(R.id.Back_btn);
        submit_btn = (Button) findViewById(R.id.submit_textview);

        ac_title = (EditText) findViewById(R.id.ac_title);
        ac_phoneno = (EditText) findViewById(R.id.ac_phoneno);

    }



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void ClickListeners() {
        submitButton();
        backButton();
    }

    private void submitButton() {
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields() == true) {
                    detailsSubmit();
                }
            }
        });
    }

    private void backButton() {
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Processes() {

    }

    private boolean validateFields() {
        boolean valid = true;

        if (ac_title.getText().toString().isEmpty()) {
            ac_title.setError("Please enter your name");
            valid = false;
        }
        if (ac_title.length() > 32) {
            ac_title.setError("Name character limit is 32");
            valid = false;
        }
        if (ac_phoneno.getText().toString().isEmpty()) {
            ac_phoneno.setError("Please enter your phone number");
            valid = false;
        }
        if (ac_phoneno.length() < 11) {
            ac_phoneno.setError("Please enter valid contact no");
            valid = false;
        }

        return valid;
    }

    private void detailsSubmit() {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Vendor_detail.this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            key = reference.push().getKey();
            reference.child("Vendor_list").child(key).child("Name").setValue(ac_title.getText().toString());
            reference.child("Vendor_list").child(key).child("Phone_no").setValue(ac_phoneno.getText().toString());

            pass_back_data();
            finish();

        }
        else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
        }

    }

    private void pass_back_data() {

        // get the text from the EditText
        String ac_title_ = ac_title.getText().toString();
        String ac_phone_no = ac_phoneno.getText().toString();

        // put the String to pass back into an Intent and close this activity
        Intent intent = new Intent();
        intent.putExtra("AC_title", ac_title_);
        intent.putExtra("AC_key_id", key);
        intent.putExtra("AC_phone_no", ac_phone_no);
        setResult(RESULT_FIRST_USER, intent);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
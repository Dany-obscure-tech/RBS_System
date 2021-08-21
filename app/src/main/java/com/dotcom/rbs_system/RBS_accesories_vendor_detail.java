package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RBS_accesories_vendor_detail extends AppCompatActivity {

    String key;

    ImageButton back_btn;

    TextView submit_textview;

    EditText ac_title, ac_phoneno;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rbs_accesories_vendor_detail);

        Initialize();
        ClickListeners();
        //TODO ye accessory add ka sath connect ha aur is ko bhi online karna ha, aur is activity ka bhi proper name rakhna ha
//        Processes();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Initialize() {

        reference = FirebaseDatabase.getInstance().getReference();

        back_btn = findViewById(R.id.back_btn);
        submit_textview = findViewById(R.id.submit_textview);

        ac_title = findViewById(R.id.ac_title);
        ac_phoneno = findViewById(R.id.ac_phoneno);

    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void ClickListeners() {
        submitButton();
        backButton();
    }

    private void submitButton() {
        submit_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    detailsSubmit();
                }
            }
        });
    }

    private void backButton() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(RBS_accesories_vendor_detail.this.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            key = reference.push().getKey();
            reference.child("Vendor_list").child(key).child("Name").setValue(ac_title.getText().toString());
            reference.child("Vendor_list").child(key).child("Phone_no").setValue(ac_phoneno.getText().toString());

            pass_back_data();
            finish();

        } else {
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
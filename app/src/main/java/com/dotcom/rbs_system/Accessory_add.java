package com.dotcom.rbs_system;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Accessory_add extends AppCompatActivity {

    private static final int VENDOR_ACTIVITY_REQUEST_CODE = 0;
    Button vendor_add_btn;
    TextView searchForVendor_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory_add);

        Initialization();
        ClickListeners();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialization() {
        vendor_add_btn = (Button)findViewById(R.id.vendor_add_btn);
        searchForVendor_textView = (TextView)findViewById(R.id.searchForVendor_textView);

    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void ClickListeners() {
        addVendor();
    }

    private void addVendor() {
        vendor_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Accessory_add.this,Vendor_detail.class);
                startActivityForResult(intent,VENDOR_ACTIVITY_REQUEST_CODE);
            }
        });
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VENDOR_ACTIVITY_REQUEST_CODE) {

            if (resultCode == 1) { // Activity.RESULT_OK
                
                // get String data from Intent
                String title_returnString = data.getStringExtra("AC_title");
                String key_id_returnString = data.getStringExtra("AC_key_id");
                String phone_no_returnString = data.getStringExtra("AC_phone_no");
                // set text view with string

                searchForVendor_textView.setText(title_returnString+"\n("+phone_no_returnString+")");
//                customerDetails.setVisibility(View.VISIBLE);

                searchForVendor_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                searchForVendor_textView.setTextColor(getResources().getColor(R.color.textGrey));
            }
        }

    }

}
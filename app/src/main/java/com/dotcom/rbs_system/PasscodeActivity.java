package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PasscodeActivity extends AppCompatActivity {
    EditText passcode_editText;
    TextView passcodeSubmit_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        Initialize();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialize() {
        passcode_editText = (EditText)findViewById(R.id.passcode_editText);
        passcodeSubmit_textView = (TextView) findViewById(R.id.passcodeSubmit_textView);

        passcodeSubmit_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passcode_editText.getText().toString().equals("1234")){
                    if (getIntent().getStringExtra("ACTIVITY_CHECK").equals("RBS")){
                        Intent intent = new Intent(PasscodeActivity.this, RBS_mainscreen.class);
                        intent.putExtra("ACTIVITY_CHECK","RBS");
                        finish();
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(PasscodeActivity.this, VendorMainScreen.class);
                        intent.putExtra("ACTIVITY_CHECK","VENDOR");
                        startActivity(intent);
                    }

                }else {
                    passcode_editText.setError("Wrong passcode!");
                }
            }
        });

    }



}
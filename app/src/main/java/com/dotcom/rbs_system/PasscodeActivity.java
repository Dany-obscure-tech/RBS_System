package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PasscodeActivity extends AppCompatActivity {

    Button btn_1_button, btn_2_button, btn_3_button, btn_4_button, btn_5_button, btn_6_button, btn_7_button, btn_8_button, btn_9_button, btn_0_button;
    ImageButton clear_imagebutton, submit_imagebutton;
    Boolean valid = true;
    String passcode_value;
    int counter = 0;
    ImageView pincode_circle_1_imageview, pincode_circle_2_imageview, pincode_circle_3_imageview, pincode_circle_4_imageview;
    List<String> passcodestring;
    List<String> orignalpasscodestring;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        Initialize();
        onclicklistners();
    }


    private void Initialize() {
        passcodestring = new ArrayList<>();
        orignalpasscodestring = new ArrayList<>();

        orignalpasscodestring.add("1");
        orignalpasscodestring.add("2");
        orignalpasscodestring.add("3");
        orignalpasscodestring.add("4");

        btn_1_button = findViewById(R.id.btn_1_button);
        btn_3_button = findViewById(R.id.btn_3_button);
        btn_2_button = findViewById(R.id.btn_2_button);
        btn_4_button = findViewById(R.id.btn_4_button);
        btn_5_button = findViewById(R.id.btn_5_button);
        btn_6_button = findViewById(R.id.btn_6_button);
        btn_7_button = findViewById(R.id.btn_7_button);
        btn_8_button = findViewById(R.id.btn_8_button);
        btn_9_button = findViewById(R.id.btn_9_button);
        btn_0_button = findViewById(R.id.btn_0_button);
        pincode_circle_1_imageview = findViewById(R.id.pincode_circle_1_imageview);
        pincode_circle_2_imageview = findViewById(R.id.pincode_circle_2_imageview);
        pincode_circle_3_imageview = findViewById(R.id.pincode_circle_3_imageview);
        pincode_circle_4_imageview = findViewById(R.id.pincode_circle_4_imageview);
        clear_imagebutton = findViewById(R.id.clear_imagebutton);
        submit_imagebutton = findViewById(R.id.submit_imagebutton);

    }

    //Todo passcodeQuantity wala function empty ha
    public void passcodeQuantitycheck() {
//        if (passcodestring.size() == 4) {
//            submit_imagebutton.setEnabled(true);
//        } else {
//            submit_imagebutton.setEnabled(false);
//        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    private void onclicklistners() {
        btn_1_button_listner();
        btn_2_button_listner();
        btn_3_button_listner();
        btn_4_button_listner();
        btn_5_button_listner();
        btn_6_button_listner();
        btn_7_button_listner();
        btn_8_button_listner();
        btn_9_button_listner();
        btn_0_button_listner();
        clear_imagebutton_listner();
        submit_imagebutton_listner();
    }

    private void btn_1_button_listner() {
        btn_1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passcode_value = btn_1_button.getText().toString();

                if (passcodestring.size() < 4) {
                    passcodestring.add(passcode_value);
                    counter++;
                    switch (counter) {
                        case 1:
                            pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 2:
                            pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 3:
                            pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 4:
                            pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
//                        default:
//                            break;
                    }

                    passcodeQuantitycheck();
                } else {
                    passcodestring.clear();
                    pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    counter = 0;
                }
                if (passcodestring.size() == orignalpasscodestring.size()) {
                    for (int i = 0; i < 4; i++) {

                        if (!passcodestring.get(i).equals(orignalpasscodestring.get(i))) {
                            valid = false;
                        }

                    }
                    if (!valid) {
                        Toast.makeText(PasscodeActivity.this, "Wrong passcode", Toast.LENGTH_SHORT).show();
                        passcodestring.clear();
                        pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        counter = 0;
                        valid = true;

                    } else {

                        if (getIntent().getStringExtra("ACTIVITY_CHECK").equals("RBS")) {
                            Intent intent = new Intent(PasscodeActivity.this, RBS_mainscreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "RBS");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(PasscodeActivity.this, VendorMainScreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "VENDOR");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        }

                    }
                }

            }
        });
    }

    private void btn_2_button_listner() {
        btn_2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passcode_value = btn_2_button.getText().toString();
                if (passcodestring.size() < 4) {
                    passcodestring.add(passcode_value);
                    counter++;
                    switch (counter) {
                        case 1:
                            pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 2:
                            pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 3:
                            pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 4:
                            pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
//                        default:
//                            break;
                    }
                    passcodeQuantitycheck();
                } else {
                    passcodestring.clear();
                    pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    counter = 0;
                }
                if (passcodestring.size() == orignalpasscodestring.size()) {
                    for (int i = 0; i < 4; i++) {

                        if (!passcodestring.get(i).equals(orignalpasscodestring.get(i))) {
                            valid = false;
                        }

                    }
                    if (!valid) {
                        Toast.makeText(PasscodeActivity.this, "Wrong passcode", Toast.LENGTH_SHORT).show();
                        passcodestring.clear();
                        pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        counter = 0;
                        valid = true;

                    } else {

                        if (getIntent().getStringExtra("ACTIVITY_CHECK").equals("RBS")) {
                            Intent intent = new Intent(PasscodeActivity.this, RBS_mainscreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "RBS");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(PasscodeActivity.this, VendorMainScreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "VENDOR");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        }

                    }
                }
            }
        });
    }

    private void btn_3_button_listner() {
        btn_3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passcode_value = btn_3_button.getText().toString();
                if (passcodestring.size() < 4) {
                    passcodestring.add(passcode_value);
                    counter++;
                    switch (counter) {
                        case 1:
                            pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 2:
                            pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 3:
                            pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 4:
                            pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
//                        default:
//                            break;
                    }
                    passcodeQuantitycheck();
                } else {
                    passcodestring.clear();
                    pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    counter = 0;
                }
                if (passcodestring.size() == orignalpasscodestring.size()) {
                    for (int i = 0; i < 4; i++) {

                        if (!passcodestring.get(i).equals(orignalpasscodestring.get(i))) {
                            valid = false;
                        }

                    }
                    if (valid == false) {
                        Toast.makeText(PasscodeActivity.this, "Wrong passcode", Toast.LENGTH_SHORT).show();
                        passcodestring.clear();
                        pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        counter = 0;
                        valid = true;

                    } else {

                        if (getIntent().getStringExtra("ACTIVITY_CHECK").equals("RBS")) {
                            Intent intent = new Intent(PasscodeActivity.this, RBS_mainscreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "RBS");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(PasscodeActivity.this, VendorMainScreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "VENDOR");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        }

                    }
                }
            }
        });
    }

    private void btn_4_button_listner() {
        btn_4_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passcode_value = btn_4_button.getText().toString();
                if (passcodestring.size() < 4) {
                    passcodestring.add(passcode_value);
                    counter++;
                    switch (counter) {
                        case 1:
                            pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 2:
                            pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 3:
                            pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 4:
                            pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
//                        default:
//                            break;
                    }
                    passcodeQuantitycheck();
                } else {
                    passcodestring.clear();
                    pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    counter = 0;
                }
                if (passcodestring.size() == orignalpasscodestring.size()) {
                    for (int i = 0; i < 4; i++) {

                        if (!passcodestring.get(i).equals(orignalpasscodestring.get(i))) {
                            valid = false;
                        }

                    }
                    if (valid == false) {
                        Toast.makeText(PasscodeActivity.this, "Wrong passcode", Toast.LENGTH_SHORT).show();
                        passcodestring.clear();
                        pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        counter = 0;
                        valid = true;

                    } else {

                        if (getIntent().getStringExtra("ACTIVITY_CHECK").equals("RBS")) {
                            Intent intent = new Intent(PasscodeActivity.this, RBS_mainscreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "RBS");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(PasscodeActivity.this, VendorMainScreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "VENDOR");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        }

                    }
                }
            }
        });
    }

    private void btn_5_button_listner() {
        btn_5_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passcode_value = btn_5_button.getText().toString();
                if (passcodestring.size() < 4) {
                    passcodestring.add(passcode_value);
                    counter++;
                    switch (counter) {
                        case 1:
                            pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 2:
                            pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 3:
                            pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 4:
                            pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
//                        default:
//                            break;
                    }
                    passcodeQuantitycheck();
                } else {
                    passcodestring.clear();
                    pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    counter = 0;
                }
                if (passcodestring.size() == orignalpasscodestring.size()) {
                    for (int i = 0; i < 4; i++) {

                        if (!passcodestring.get(i).equals(orignalpasscodestring.get(i))) {
                            valid = false;
                        }

                    }
                    if (valid == false) {
                        Toast.makeText(PasscodeActivity.this, "Wrong passcode", Toast.LENGTH_SHORT).show();
                        passcodestring.clear();
                        pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        counter = 0;
                        valid = true;

                    } else {

                        if (getIntent().getStringExtra("ACTIVITY_CHECK").equals("RBS")) {
                            Intent intent = new Intent(PasscodeActivity.this, RBS_mainscreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "RBS");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(PasscodeActivity.this, VendorMainScreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "VENDOR");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        }

                    }
                }
            }
        });
    }

    private void btn_6_button_listner() {
        btn_6_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passcode_value = btn_6_button.getText().toString();
                if (passcodestring.size() < 4) {
                    passcodestring.add(passcode_value);
                    counter++;
                    switch (counter) {
                        case 1:
                            pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 2:
                            pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 3:
                            pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 4:
                            pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
//                        default:
//                            break;
                    }
                    passcodeQuantitycheck();
                } else {
                    passcodestring.clear();
                    pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    counter = 0;
                }
                if (passcodestring.size() == orignalpasscodestring.size()) {
                    for (int i = 0; i < 4; i++) {

                        if (!passcodestring.get(i).equals(orignalpasscodestring.get(i))) {
                            valid = false;
                        }

                    }
                    if (valid == false) {
                        Toast.makeText(PasscodeActivity.this, "Wrong passcode", Toast.LENGTH_SHORT).show();
                        passcodestring.clear();
                        pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        counter = 0;
                        valid = true;

                    } else {

                        if (getIntent().getStringExtra("ACTIVITY_CHECK").equals("RBS")) {
                            Intent intent = new Intent(PasscodeActivity.this, RBS_mainscreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "RBS");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(PasscodeActivity.this, VendorMainScreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "VENDOR");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        }

                    }
                }
            }
        });
    }

    private void btn_7_button_listner() {
        btn_7_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passcode_value = btn_7_button.getText().toString();
                if (passcodestring.size() < 4) {
                    passcodestring.add(passcode_value);
                    counter++;
                    switch (counter) {
                        case 1:
                            pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 2:
                            pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 3:
                            pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 4:
                            pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
//                        default:
//                            break;
                    }
                    passcodeQuantitycheck();
                } else {
                    passcodestring.clear();
                    pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    counter = 0;
                }
                if (passcodestring.size() == orignalpasscodestring.size()) {
                    for (int i = 0; i < 4; i++) {

                        if (!passcodestring.get(i).equals(orignalpasscodestring.get(i))) {
                            valid = false;
                        }

                    }
                    if (!valid) {
                        Toast.makeText(PasscodeActivity.this, "Wrong passcode", Toast.LENGTH_SHORT).show();
                        passcodestring.clear();
                        pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        counter = 0;
                        valid = true;

                    } else {

                        if (getIntent().getStringExtra("ACTIVITY_CHECK").equals("RBS")) {
                            Intent intent = new Intent(PasscodeActivity.this, RBS_mainscreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "RBS");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(PasscodeActivity.this, VendorMainScreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "VENDOR");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        }

                    }
                }
            }
        });
    }

    private void btn_8_button_listner() {
        btn_8_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passcode_value = btn_8_button.getText().toString();
                if (passcodestring.size() < 4) {
                    passcodestring.add(passcode_value);
                    counter++;
                    switch (counter) {
                        case 1:
                            pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 2:
                            pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 3:
                            pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 4:
                            pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
//                        default:
//                            break;
                    }
                    passcodeQuantitycheck();
                } else {
                    passcodestring.clear();
                    pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    counter = 0;
                }
                if (passcodestring.size() == orignalpasscodestring.size()) {
                    for (int i = 0; i < 4; i++) {

                        if (!passcodestring.get(i).equals(orignalpasscodestring.get(i))) {
                            valid = false;
                        }

                    }
                    if (!valid) {
                        Toast.makeText(PasscodeActivity.this, "Wrong passcode", Toast.LENGTH_SHORT).show();
                        passcodestring.clear();
                        pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        counter = 0;
                        valid = true;

                    } else {

                        if (getIntent().getStringExtra("ACTIVITY_CHECK").equals("RBS")) {
                            Intent intent = new Intent(PasscodeActivity.this, RBS_mainscreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "RBS");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(PasscodeActivity.this, VendorMainScreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "VENDOR");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        }

                    }
                }
            }
        });
    }

    private void btn_9_button_listner() {
        btn_9_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passcode_value = btn_9_button.getText().toString();
                if (passcodestring.size() < 4) {
                    passcodestring.add(passcode_value);
                    counter++;
                    switch (counter) {
                        case 1:
                            pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 2:
                            pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 3:
                            pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 4:
                            pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
//                        default:
//                            break;
                    }
                    passcodeQuantitycheck();
                } else {
                    passcodestring.clear();
                    pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    counter = 0;
                }
                if (passcodestring.size() == orignalpasscodestring.size()) {
                    for (int i = 0; i < 4; i++) {

                        if (!passcodestring.get(i).equals(orignalpasscodestring.get(i))) {
                            valid = false;
                        }

                    }
                    if (!valid) {
                        Toast.makeText(PasscodeActivity.this, "Wrong passcode", Toast.LENGTH_SHORT).show();
                        passcodestring.clear();
                        pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        counter = 0;
                        valid = true;

                    } else {

                        if (getIntent().getStringExtra("ACTIVITY_CHECK").equals("RBS")) {
                            Intent intent = new Intent(PasscodeActivity.this, RBS_mainscreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "RBS");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(PasscodeActivity.this, VendorMainScreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "VENDOR");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        }

                    }
                }
            }
        });
    }

    private void btn_0_button_listner() {
        btn_0_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passcode_value = btn_0_button.getText().toString();
                if (passcodestring.size() < 4) {
                    passcodestring.add(passcode_value);
                    counter++;
                    switch (counter) {
                        case 1:
                            pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 2:
                            pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 3:
                            pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
                        case 4:
                            pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle_fill));
                            break;
//                        default:
//                            break;
                    }
                    passcodeQuantitycheck();
                } else {
                    passcodestring.clear();
                    pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                    counter = 0;
                }
                if (passcodestring.size() == orignalpasscodestring.size()) {
                    for (int i = 0; i < 4; i++) {

                        if (!passcodestring.get(i).equals(orignalpasscodestring.get(i))) {
                            valid = false;
                        }

                    }
                    if (!valid) {
                        Toast.makeText(PasscodeActivity.this, "Wrong passcode", Toast.LENGTH_SHORT).show();
                        passcodestring.clear();
                        pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                        counter = 0;
                        valid = true;

                    } else {

                        if (getIntent().getStringExtra("ACTIVITY_CHECK").equals("RBS")) {
                            Intent intent = new Intent(PasscodeActivity.this, RBS_mainscreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "RBS");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(PasscodeActivity.this, VendorMainScreen.class);
                            intent.putExtra("ACTIVITY_CHECK", "VENDOR");
                            finish();
                            passcodestring.clear();
                            startActivity(intent);
                        }

                    }
                }
            }
        });
    }

    private void clear_imagebutton_listner() {
        clear_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (passcodestring.size() == 1 || passcodestring.size() > 1) {
                    passcodestring.remove(passcodestring.size() - 1);
                    switch (counter) {
                        case 1:
                            pincode_circle_1_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                            counter--;
                            break;
                        case 2:
                            pincode_circle_2_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                            counter--;
                            break;
                        case 3:
                            pincode_circle_3_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                            counter--;
                            break;
                        case 4:
                            pincode_circle_4_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pincode_circle));
                            counter--;
                            break;
//                        default:
//                            break;
                    }
                }
            }
        });
    }

    private void submit_imagebutton_listner() {
        submit_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(PasscodeActivity.this, "Enter passcode", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
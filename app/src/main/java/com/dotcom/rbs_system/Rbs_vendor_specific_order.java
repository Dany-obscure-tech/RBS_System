package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Rbs_vendor_specific_order extends AppCompatActivity {
    ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rbs_vendor_specific_order);
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        initialization();
        onclicklistners();
    }
///////////////////////////////////////////////////////////////////////////////////////////////////
    private void initialization() {

    }
///////////////////////////////////////////////////////////////////////////////////////////////////
    private void onclicklistners() {
        back_btn_listner();
    }

    private void back_btn_listner() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
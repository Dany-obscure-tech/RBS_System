package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class Repair_details extends AppCompatActivity {

    String repairID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_details);

        repairID = getIntent().getStringExtra("REPAIR_ID");

    }
}

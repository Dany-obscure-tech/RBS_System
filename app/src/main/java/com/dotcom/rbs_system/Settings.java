package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dotcom.rbs_system.Adapter.AdapterSettingsFaultListRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {

    ImageButton Back_btn;

    Button addFaults_btn;
    RecyclerView faultList_recyclerView;
    AdapterSettingsFaultListRecyclerView adapterSettingsFaultListRecyclerView;
    Dialog addFaultDialog;

//    Add fault alert
    EditText alertFaultName_editText,alertFaultPrice_editText;
    Button addFaultAlertenter_btn,addFaultsAlertCancel_btn;

    DatabaseReference faultListRef;

    List<String> faultNameList, faultPriceList, faultKeyIDList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialize();
        getFaultsList();
        onClickListeners();
    }

    private void initialize() {
        Back_btn=(ImageButton)findViewById(R.id.Back_btn);

        faultNameList = new ArrayList<>();
        faultPriceList = new ArrayList<>();
        faultKeyIDList = new ArrayList<>();

        faultListRef = FirebaseDatabase.getInstance().getReference("Listed_faults");

        addFaults_btn = (Button)findViewById(R.id.addFaults_btn);
        faultList_recyclerView = (RecyclerView) findViewById(R.id.faultList_recyclerView);

        addFaultDialog = new Dialog(this);
        addFaultDialog.setContentView(R.layout.alert_setting_add_fault);

        alertFaultName_editText = addFaultDialog.findViewById(R.id.alertFaultName_editText);
        alertFaultPrice_editText = addFaultDialog.findViewById(R.id.alertFaultPrice_editText);
        addFaultAlertenter_btn = addFaultDialog.findViewById(R.id.addFaultAlertenter_btn);
        addFaultsAlertCancel_btn = addFaultDialog.findViewById(R.id.addFaultsAlertCancel_btn);
    }

    private void onClickListeners() {
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addFaults_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFaultDialog.show();
            }
        });

        addFaultAlertenter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAlertAddFault()==true){
                    detailsSubmitAlertAddFault();
                }

            }
        });

        addFaultsAlertCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFaultDialog.dismiss();
            }
        });
    }

    private void getFaultsList() {
        faultListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    faultNameList.add(String.valueOf(dataSnapshot1.child("Fault_name").getValue()));
                    faultPriceList.add(String.valueOf(dataSnapshot1.child("Fault_price").getValue()));
                    faultKeyIDList.add(String.valueOf(dataSnapshot1.child("key_id").getValue()));
                }

                adapterSettingsFaultListRecyclerView = new AdapterSettingsFaultListRecyclerView(Settings.this,faultNameList,faultPriceList,faultKeyIDList);
                faultList_recyclerView.setAdapter(adapterSettingsFaultListRecyclerView);
                faultList_recyclerView.setLayoutManager(new GridLayoutManager(Settings.this,1));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean validateAlertAddFault() {
        Boolean valid = true;

        if (alertFaultName_editText.getText().toString().isEmpty()){
            alertFaultName_editText.setError("enter fault name");
            valid = false;
        }
        if (alertFaultPrice_editText.getText().toString().isEmpty()){
            alertFaultPrice_editText.setError("enter fault price");
            valid = false;
        }

        return valid;
    }

    private void detailsSubmitAlertAddFault() {
        String key = faultListRef.push().getKey();

        faultListRef.child(key).child("Fault_name").setValue(alertFaultName_editText.getText().toString());
        faultListRef.child(key).child("Fault_price").setValue(alertFaultPrice_editText.getText().toString());
        faultListRef.child(key).child("added_by").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        faultListRef.child(key).child("key_id").setValue(key);

        faultNameList.add(alertFaultName_editText.getText().toString());
        faultKeyIDList.add(key);
        faultPriceList.add(alertFaultPrice_editText.getText().toString());

        adapterSettingsFaultListRecyclerView.notifyDataSetChanged();
        addFaultDialog.dismiss();
    }
}

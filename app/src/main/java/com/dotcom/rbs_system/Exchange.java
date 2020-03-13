package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Exchange extends AppCompatActivity {
    ImageButton Back_btn,sms_btn,gmail_btn;
    DatabaseReference buyList_ref;

    List<String> categoryList;
    List<String> purchasePriceList;
    List<String> exisitngItemsKeyIDList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        initialize();



        getItemsKeyIDList();
        onClickListeners();



    }

    private void initialize() {
        buyList_ref = FirebaseDatabase.getInstance().getReference("Buy_list");

        Back_btn=(ImageButton)findViewById(R.id.Back_btn);
        gmail_btn=(ImageButton) findViewById(R.id.gmail_btn);
        sms_btn=(ImageButton)findViewById(R.id.sms_btn);

        exisitngItemsKeyIDList = new ArrayList<>();
        categoryList = new ArrayList<>();
        purchasePriceList = new ArrayList<>();
    }

    private void getItemsKeyIDList() {
        buyList_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String id;
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    id = String.valueOf(exisitngItemsKeyIDList.add(String.valueOf(dataSnapshot1.child("Item_keyID").getValue())));
                    for (int i = 0;i<exisitngItemsKeyIDList.size()+1;i++){

                    }
                    exisitngItemsKeyIDList.add(String.valueOf(dataSnapshot1.child("Item_keyID").getValue()));
                    purchasePriceList.add(String.valueOf(dataSnapshot1.child("Purchase_price").getValue()));
                }
                gettingItemCategoryAndDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gettingItemCategoryAndDetails() {

    }

    private void onClickListeners() {
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        gmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Exchange.this, "yes", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(Intent.ACTION_SEND);
                it.setType("message/rfc822");
                startActivity(Intent.createChooser(it,"Choose Mail App"));
            }
        });

        sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "0323"));
                intent.putExtra("sms_body", "Hi how are you");
                startActivity(intent);
            }
        });
    }
}

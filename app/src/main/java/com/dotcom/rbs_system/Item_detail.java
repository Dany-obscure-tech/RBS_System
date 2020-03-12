package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Item_detail extends AppCompatActivity {
    Button selectCategory_btn,submit_btn;
    DatabaseReference categoryRef,reference;
    EditText itemName_editText,notes_editText,itemId_editText;
    List<String> categoryList;
    RatingBar ratingBar;
    ImageButton Back_btn;

    private ArrayList<SampleSearchModel> createCategoryData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<categoryList.size();i++){
            items.add(new SampleSearchModel(categoryList.get(i),null,null,null,null,null,null,null));
        }

        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        initialize();
        getCategoryList();
        selectCategory();
        onClickListeners();
    }

    private void initialize() {
        Back_btn = (ImageButton)findViewById(R.id.Back_btn);
        itemName_editText = (EditText)findViewById(R.id.itemName_editText);
        itemId_editText = (EditText)findViewById(R.id.itemId_editText);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        notes_editText = (EditText)findViewById(R.id.notes_editText);
        categoryList = new ArrayList<>();
        categoryRef = FirebaseDatabase.getInstance().getReference("Categories");
        reference = FirebaseDatabase.getInstance().getReference();
        selectCategory_btn = (Button)findViewById(R.id.selectCategory_btn);
        submit_btn = (Button)findViewById(R.id.submit_btn);
    }

    private void getCategoryList() {
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    categoryList.add(String.valueOf(dataSnapshot1.getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void selectCategory() {
        selectCategory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Item_detail.this, "Search...",
                        "What are you looking for...?", null, createCategoryData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                selectCategory_btn.setText(item.getTitle());
                                selectCategory_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                selectCategory_btn.setTextColor(getResources().getColor(R.color.textGrey));

                                dialog.dismiss();
                            }
                        }).show();
                // hello
            }
        });
    }

    private void detailsSubmit() {
        String key = reference.push().getKey();
        reference.child("Items").child(selectCategory_btn.getText().toString()).child(key).child("Category").setValue(selectCategory_btn.getText().toString());
        reference.child("Items").child(selectCategory_btn.getText().toString()).child(key).child("Item_id").setValue(itemId_editText.getText().toString());
        reference.child("Items").child(selectCategory_btn.getText().toString()).child(key).child("added_by").setValue(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        reference.child("Items").child(selectCategory_btn.getText().toString()).child(key).child("Item_name").setValue(itemName_editText.getText().toString());
        reference.child("Items").child(selectCategory_btn.getText().toString()).child(key).child("Condition").setValue(ratingBar.getRating());
        reference.child("Items").child(selectCategory_btn.getText().toString()).child(key).child("Notes").setValue(notes_editText.getText().toString());
        reference.child("Items").child(selectCategory_btn.getText().toString()).child(key).child("key_id").setValue(key);

        finish();

    }

    private void onClickListeners() {
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsSubmit();
            }
        });
    }



}

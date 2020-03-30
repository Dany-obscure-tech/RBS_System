package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Accessories extends AppCompatActivity {


    Button stock_entry_btn,sale_btn;
    LinearLayout upperLayout,lowerLayout;

    DatabaseReference categoryRef,reference;
    List<String> categoryList;
    Button selectCategory_btn,submit_btn;
    Button selectCategory_btn2,searchForItem_btn,submit_btn2;
    RatingBar ratingBar;

    ImageButton Back_btn;

    List<String> exisitngItemsList,exisitngItemsIDList,exisitngItemsKeyIDList,exisitngItemsCategoryList,existingItemsConditionsList;

    EditText itemName_editText,itemId_editText,purchase_price_editText;

    DatabaseReference existingItemsRef;
    private String itemKeyID;
    private EditText suggest_price_editText, balance_editText,paid_editText;

    String firebaseAuthUID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());

    private ArrayList<SampleSearchModel> createCategoryData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<categoryList.size();i++){
            items.add(new SampleSearchModel(categoryList.get(i),null,null,null,null,null,null,null));
        }

        return items;
    }

    private ArrayList<SampleSearchModel> createItemsData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<exisitngItemsList.size();i++){
            items.add(new SampleSearchModel(exisitngItemsList.get(i)+"\n("+exisitngItemsIDList.get(i)+")",exisitngItemsIDList.get(i),exisitngItemsList.get(i),exisitngItemsCategoryList.get(i),existingItemsConditionsList.get(i),null,null,exisitngItemsKeyIDList.get(i)));
        }

        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories);

        initialize();

        layoutToggle();

        getCategoryList();

        onClickListeners();
    }

    private void initialize() {

        Back_btn=(ImageButton)findViewById(R.id.Back_btn);

        reference = FirebaseDatabase.getInstance().getReference();
        categoryRef = FirebaseDatabase.getInstance().getReference("Categories");
        categoryList = new ArrayList<>();

        stock_entry_btn = (Button)findViewById(R.id.stock_entry_btn);
        sale_btn = (Button)findViewById(R.id.sale_btn);

        upperLayout = (LinearLayout) findViewById(R.id.upperLayout);
        lowerLayout = (LinearLayout) findViewById(R.id.lowerLayout);

        selectCategory_btn = (Button)findViewById(R.id.selectCategory_btn);
        selectCategory_btn2 = (Button)findViewById(R.id.selectCategory_btn2);
        searchForItem_btn = (Button)findViewById(R.id.searchForItem_btn);

        suggest_price_editText = (EditText)findViewById(R.id.suggest_price_editText);
        paid_editText = (EditText)findViewById(R.id.paid_editText);
        balance_editText = (EditText)findViewById(R.id.balance_editText);

        itemName_editText = (EditText)findViewById(R.id.itemName_editText);
        itemId_editText = (EditText)findViewById(R.id.itemId_editText);
        purchase_price_editText = (EditText)findViewById(R.id.purchase_price_editText);

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        submit_btn = (Button)findViewById(R.id.submit_btn);
        submit_btn2 = (Button)findViewById(R.id.submit_btn2);

        existingItemsRef = FirebaseDatabase.getInstance().getReference("Accessories");

        exisitngItemsList = new ArrayList<>();
        exisitngItemsIDList = new ArrayList<>();
        exisitngItemsKeyIDList = new ArrayList<>();
        exisitngItemsCategoryList = new ArrayList<>();
        existingItemsConditionsList= new ArrayList<>();
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

    private void detailsSubmit() {
        final String key = reference.push().getKey();
        reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("Category").setValue(selectCategory_btn.getText().toString());
        reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("Item_id").setValue(itemId_editText.getText().toString());
        reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("added_by").setValue(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("Item_name").setValue(itemName_editText.getText().toString());
        reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("Condition").setValue(ratingBar.getRating());
        reference.child("Accessories").child(selectCategory_btn.getText().toString()).child(key).child("key_id").setValue(key);



        finish();

    }

    private void detailsSubmit2() {

        String key = reference.push().getKey();

        reference.child("Accessories_sale").child(key).child("Item_keyID").setValue(itemKeyID);
        reference.child("Accessories_sale").child(key).child("Suggested_price").setValue(suggest_price_editText.getText().toString());
        reference.child("Accessories_sale").child(key).child("Balance").setValue(balance_editText.getText().toString());
        reference.child("Accessories_sale").child(key).child("Paid").setValue(paid_editText.getText().toString());
        reference.child("Accessories_sale").child(key).child("key_id").setValue(key);
        reference.child("Accessories_sale").child(key).child("added_by").setValue(firebaseAuthUID);


        finish();
    }

    private void layoutToggle() {
        stock_entry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upperLayout.setVisibility(View.VISIBLE);
                lowerLayout.setVisibility(View.GONE);
            }
        });

        sale_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upperLayout.setVisibility(View.GONE);
                lowerLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void fetchingExisitingItems(String category) {

        exisitngItemsList.clear();
        exisitngItemsIDList.clear();
        exisitngItemsCategoryList.clear();
        existingItemsConditionsList.clear();
        exisitngItemsKeyIDList.clear();

        existingItemsRef.child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        exisitngItemsList.add(String.valueOf(dataSnapshot1.child("Item_name").getValue()));
                        exisitngItemsIDList.add(String.valueOf(dataSnapshot1.child("Item_id").getValue()));
                        exisitngItemsCategoryList.add(String.valueOf(dataSnapshot1.child("Category").getValue()));
                        existingItemsConditionsList.add(String.valueOf(dataSnapshot1.child("Condition").getValue()));
                        exisitngItemsKeyIDList.add(String.valueOf(dataSnapshot1.child("key_id").getValue()));
                }

                searchForItem_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SimpleSearchDialogCompat(Accessories.this, "Search...",
                                "What are you looking for...?", null, createItemsData(),
                                new SearchResultListener<SampleSearchModel>() {
                                    @Override
                                    public void onSelected(BaseSearchDialogCompat dialog,
                                                           SampleSearchModel item, int position) {
                                        searchForItem_btn.setText(item.getTitle());
                                        searchForItem_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                        searchForItem_btn.setTextColor(getResources().getColor(R.color.textGrey));

                                        itemKeyID = item.getVal5();

                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void onClickListeners() {
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selectCategory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessories.this, "Search...",
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

        selectCategory_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessories.this, "Search...",
                        "What are you looking for...?", null, createCategoryData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                selectCategory_btn2.setText(item.getTitle());
                                selectCategory_btn2.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                selectCategory_btn2.setTextColor(getResources().getColor(R.color.textGrey));

                                fetchingExisitingItems(item.getTitle());

                                dialog.dismiss();
                            }
                        }).show();
                // hello
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields() == true)
                    detailsSubmit();
            }
        });

        submit_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields2() == true)
                    detailsSubmit2();
            }
        });
    }

    private boolean validateFields() {
        boolean valid = true;

        if (selectCategory_btn.getText().toString().equals("Select Category")) {
            Toast.makeText(this, "Please select category", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (itemName_editText.getText().toString().isEmpty()) {
            itemName_editText.setError("Please enter item name");
            valid = false;
        }

        if (itemId_editText.getText().toString().isEmpty()) {
            itemId_editText.setError("Please enter item id");
            valid = false;
        }

        if (purchase_price_editText.getText().toString().isEmpty()) {
            purchase_price_editText.setError("Please enter item id");
            valid = false;
        }

        if (ratingBar.getRating()==0){
            Toast.makeText(this, "Please select rating", Toast.LENGTH_SHORT).show();
            valid=false;
        }


        return valid;
    }

    private boolean validateFields2() {
        boolean valid = true;

        if (selectCategory_btn2.getText().toString().equals("Select Category")) {
            Toast.makeText(this, "Please select category", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (searchForItem_btn.getText().toString().equals("Search for item")) {
            Toast.makeText(this, "Please select item", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (suggest_price_editText.getText().toString().isEmpty()) {
            suggest_price_editText.setError("Please enter item name");
            valid = false;
        }

        if (paid_editText.getText().toString().isEmpty()) {
            paid_editText.setError("Please enter item id");
            valid = false;
        }

        if (balance_editText.getText().toString().isEmpty()) {
            balance_editText.setError("Please enter item id");
            valid = false;
        }
        return valid;
    }

}

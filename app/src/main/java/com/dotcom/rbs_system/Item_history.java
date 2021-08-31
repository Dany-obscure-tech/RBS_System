package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterItemHistoryListRecyclerView;
import com.dotcom.rbs_system.Adapter.SliderAdapterExample;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Item_history extends AppCompatActivity {
    String itemID, itemCategory;
    List<String> dateList, status_list, shopkeeper_type_list, shopkeeper_name_list, customer_type_list, customer_name_list, shopkeeperImage_imageView, customerImage_imageView;
    List<String> itemImageUrl_list;
    List<String> shopkeeper_key_id,customer_key_id;

    DatabaseReference itemRef;
    DatabaseReference itemHistoryRef;
    Query orderQuery;

    RecyclerView itemHistoryRecyclerView;
    AdapterItemHistoryListRecyclerView adapterItemHistoryListRecyclerView;

    TextView itemDetailsToggle_textView;
    TextView itemName_textView, serialNo_textView, category_textView, condition_textView, item_description_textview;

    RelativeLayout itemDetails_relativelayout;

    Progreess_dialog pd1, pd2;

    Boolean toggleCheck = true;

    ImageButton back_btn;

    RatingBar ratingBar;

    EditText notes_editText;

    SliderView sliderView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_history);

        Initialization();
        InitialDataFetch();
        ClickListeners();


    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Initialization() {
        sliderView = findViewById(R.id.imageSliders);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.parseColor("#01A0DA"));
        sliderView.setIndicatorUnselectedColor(Color.parseColor("#F1F1F1"));
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        itemName_textView = findViewById(R.id.item_category_textView);
        item_description_textview = findViewById(R.id.item_description_textview);
        serialNo_textView = findViewById(R.id.serialNo_textView);
        category_textView = findViewById(R.id.category_textView);
        condition_textView = findViewById(R.id.condition_textView);
        back_btn = findViewById(R.id.back_btn);

        itemID = getIntent().getStringExtra("ITEM_ID");
        itemCategory = getIntent().getStringExtra("ITEM_CATEGORY");



        dateList = new ArrayList<>();
        status_list = new ArrayList<>();
        shopkeeper_type_list = new ArrayList<>();
        shopkeeper_name_list = new ArrayList<>();
        customer_type_list = new ArrayList<>();
        customer_name_list = new ArrayList<>();
        shopkeeperImage_imageView = new ArrayList<>();
        customerImage_imageView = new ArrayList<>();

        shopkeeper_key_id = new ArrayList<>();
        customer_key_id = new ArrayList<>();

        itemImageUrl_list = new ArrayList<>();

        itemHistoryRef = FirebaseDatabase.getInstance().getReference("Item_history/" + itemID);

        orderQuery = itemHistoryRef.orderByChild("Timestamp");

        itemHistoryRecyclerView = findViewById(R.id.itemHistoryRecyclerView);
        itemHistoryRecyclerView.setLayoutManager(new GridLayoutManager(Item_history.this, 1));

        itemDetailsToggle_textView = findViewById(R.id.itemDetailsToggle_textView);

        itemDetails_relativelayout = findViewById(R.id.itemDetails_relativelayout);

        itemRef = FirebaseDatabase.getInstance().getReference("Items");

        pd1 = new Progreess_dialog();
        pd2 = new Progreess_dialog();

    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialDataFetch() {
        gettingHistoryList();
        getItem(itemCategory, itemID);
    }

    private void gettingHistoryList() {
        itemHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd1.showProgressBar(Item_history.this);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        dateList.add(dataSnapshot1.child("Date").getValue().toString());
                        status_list.add(dataSnapshot1.child("RBS").getValue().toString());
                        shopkeeper_name_list.add(dataSnapshot1.child("Shopkeeper_name").getValue().toString());
                        shopkeeperImage_imageView.add(dataSnapshot1.child("Shopkeeper_image").getValue().toString());

                        shopkeeper_key_id.add(dataSnapshot1.child("Shopkeeper_keyId").getValue().toString());
                        customer_key_id.add(dataSnapshot1.child("Customer_keyId").getValue().toString());

                        if (dataSnapshot1.child("Customer_name").exists()) {
                            shopkeeper_type_list.add(dataSnapshot1.child("Shopkeeper_type").getValue().toString());
                            customer_type_list.add(dataSnapshot1.child("Customer_type").getValue().toString());
                            customer_name_list.add(dataSnapshot1.child("Customer_name").getValue().toString());
                            customerImage_imageView.add(dataSnapshot1.child("Customer_image").getValue().toString());
                        } else {
                            shopkeeper_type_list.add("NA");
                            customer_type_list.add("NA");
                            customer_name_list.add("NA");
                            customerImage_imageView.add("NA");
                        }

                        Collections.reverse(dateList);
                        Collections.reverse(status_list);
                        Collections.reverse(shopkeeper_name_list);
                        Collections.reverse(shopkeeperImage_imageView);
                        Collections.reverse(shopkeeper_type_list);
                        Collections.reverse(customer_type_list);
                        Collections.reverse(customer_name_list);
                        Collections.reverse(customerImage_imageView);

                        adapterItemHistoryListRecyclerView = new AdapterItemHistoryListRecyclerView(Item_history.this, status_list, shopkeeper_type_list, shopkeeper_name_list, customer_type_list, customer_name_list, shopkeeperImage_imageView, customerImage_imageView, dateList,shopkeeper_key_id,customer_key_id);
                        itemHistoryRecyclerView.setAdapter(adapterItemHistoryListRecyclerView);

                    }

                    pd1.dismissProgressBar(Item_history.this);
                } else {
                    pd1.dismissProgressBar(Item_history.this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd1.dismissProgressBar(Item_history.this);
            }
        });
    }

    private void getItem(String item_category, String item_keyID) {
        itemRef.child(item_category).child(item_keyID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd2.showProgressBar(Item_history.this);
                if (dataSnapshot.exists()) {
                    itemName_textView.setText(dataSnapshot.child("Item_name").getValue().toString());
                    serialNo_textView.setText(dataSnapshot.child("Item_id").getValue().toString());
                    category_textView.setText(dataSnapshot.child("Category").getValue().toString());
                    condition_textView.setText(dataSnapshot.child("Condition").getValue().toString());
                    item_description_textview.setText(dataSnapshot.child("Description").getValue().toString());

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Image_urls").getChildren()) {
                        itemImageUrl_list.add(dataSnapshot1.getValue().toString());
                    }

                    SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(Item_history.this, itemImageUrl_list);
                    sliderView.setSliderAdapter(sliderAdapterExample);

                    pd2.dismissProgressBar(Item_history.this);
                } else {
                    pd2.dismissProgressBar(Item_history.this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd2.dismissProgressBar(Item_history.this);
            }
        });
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void ClickListeners() {
        itemDetails_relativelayoutToggle();
        back_btn();
    }

    private void back_btn() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void itemDetails_relativelayoutToggle() {
        itemDetailsToggle_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toggleCheck) {
                    itemDetails_relativelayout.setVisibility(View.VISIBLE);
                    itemDetailsToggle_textView.setText("Hide details");

                    toggleCheck = true;
                } else {
                    itemDetails_relativelayout.setVisibility(View.GONE);
                    itemDetailsToggle_textView.setText("Show Item Details");
                    toggleCheck = false;
                }
            }
        });
    }
}

package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.AdapterCategoryProductsItemRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Buylocal_category_products extends AppCompatActivity {

    AdapterCategoryProductsItemRecyclerView viewAdapter;

    ImageButton back_btn;
    
    RecyclerView categoryRecyclerView;
    EditText search_editText;
    
    List<String> filtereditemname;
    List<String> filteredprice;
    List<String> filtereditemImage;
    List<String> filtereditemkeyid;
    List<String> filteredshopkeeperKeyId;
    List<String> filtereditemCategory;

    List<String> itemname;
    List<String> price;
    List<String> itemImage;
    List<String> itemkeyid;
    List<String> shopkeeperKeyId;
    List<String> itemCategory;
    
    String category_text;
    TextView category_textview;
    DatabaseReference shopKeepersStockRef;
    ImageButton search_imageBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buylocal_category_products);

        shopKeepersStockRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers");
        back_btn = (ImageButton) findViewById(R.id.back_btn);

        categoryRecyclerView = (RecyclerView) findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(Buylocal_category_products.this, 2));

        search_imageBtn = (ImageButton) findViewById(R.id.search_imageBtn);
        search_editText = (EditText) findViewById(R.id.search_editText);

        category_textview = (TextView) findViewById(R.id.category_textview);
        category_text = getIntent().getStringExtra("CATEGORY_NAME");
        category_textview.setText(category_text);

        filtereditemname = new ArrayList<>();
        filteredprice = new ArrayList<>();
        filtereditemImage = new ArrayList<>();
        filtereditemkeyid = new ArrayList<>();
        filteredshopkeeperKeyId = new ArrayList<>();
        filtereditemCategory = new ArrayList<>();

        itemname = new ArrayList<>();
        price = new ArrayList<>();
        itemImage = new ArrayList<>();
        itemkeyid = new ArrayList<>();
        shopkeeperKeyId = new ArrayList<>();
        itemCategory = new ArrayList<>();


        InitialProcess();
        search_imageBtn_listener();
        onclicklistners();

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialProcess() {
        datafetch();
    }

    private void datafetch() {
        shopKeepersStockRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    for (DataSnapshot snapshot2 : snapshot1.child(category_text).getChildren()) {

                        itemname.add(snapshot2.child("Item_name").getValue().toString());
                        price.add(snapshot2.child("Price").getValue().toString());
                        itemImage.add(snapshot2.child("Image").getValue().toString());
                        itemkeyid.add(snapshot2.getKey().toString());
                        shopkeeperKeyId.add(snapshot2.child("Category").getValue().toString());
                        itemCategory.add(snapshot1.getKey());

                        filtereditemname.add(snapshot2.child("Item_name").getValue().toString());
                        filteredprice.add(snapshot2.child("Price").getValue().toString());
                        filtereditemImage.add(snapshot2.child("Image").getValue().toString());
                        filtereditemkeyid.add(snapshot2.getKey().toString());
                        filtereditemCategory.add(snapshot2.child("Category").getValue().toString());
                        filteredshopkeeperKeyId.add(snapshot1.getKey());

                    }


                }

                viewAdapter = new AdapterCategoryProductsItemRecyclerView(Buylocal_category_products.this, filtereditemname, filteredprice, filtereditemImage, filtereditemkeyid, filteredshopkeeperKeyId, filtereditemCategory);
                categoryRecyclerView.setAdapter(viewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void onclicklistners() {
        back_btn_listner();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void search_imageBtn_listener() {
        search_imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                filtereditemname.clear();
                filteredprice.clear();
                filtereditemImage.clear();
                filtereditemkeyid.clear();
                filteredshopkeeperKeyId.clear();
                filtereditemCategory.clear();

                if(!search_editText.getText().toString().isEmpty()){

                    for (int i = 0; i<itemname.size();i++){

                        System.out.println(itemname.get(i));
                        if (itemname.get(i).regionMatches(true,0, search_editText.getText().toString(), 0, search_editText.getText().toString().length())){

                            filtereditemname.add(itemname.get(i));
                            filteredprice.add(price.get(i));
                            filtereditemImage.add(itemImage.get(i));
                            filtereditemkeyid.add(itemkeyid.get(i));
                            filteredshopkeeperKeyId.add(shopkeeperKeyId.get(i));
                            filtereditemCategory.add(itemCategory.get(i));
                        }
                    }
                }else {
                    for (int i = 0; i<itemname.size();i++){
                        filtereditemname.add(itemname.get(i));
                        filteredprice.add(price.get(i));
                        filtereditemImage.add(itemImage.get(i));
                        filtereditemkeyid.add(itemkeyid.get(i));
                        filteredshopkeeperKeyId.add(shopkeeperKeyId.get(i));
                        filtereditemCategory.add(itemCategory.get(i));

                    }

                }

                viewAdapter.notifyDataSetChanged();

            }
        });
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
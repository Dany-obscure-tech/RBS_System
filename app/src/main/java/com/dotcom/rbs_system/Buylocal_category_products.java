package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.AdapterCategoryProductsItemRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterSpotlightItemListRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Buylocal_category_products extends AppCompatActivity {

    ImageButton back_btn;
    RecyclerView categoryRecyclerView;
    List<String> itemname, price, itemImage;
    String category_text;
    TextView category_textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buylocal_category_products);

        back_btn=(ImageButton)findViewById(R.id.back_btn);
        categoryRecyclerView=(RecyclerView)findViewById(R.id.categoryRecyclerView);
        category_textview=(TextView)findViewById(R.id.category_textview);
        category_text = getIntent().getStringExtra("CATEGORY_NAME");
        category_textview.setText(category_text);

        itemname = new ArrayList<String>();
        price = new ArrayList<String>();
        itemImage = new ArrayList<String>();

        itemname.add("PC");
        itemname.add("Laptop");



        AdapterCategoryProductsItemRecyclerView viewAdapter = new AdapterCategoryProductsItemRecyclerView(Buylocal_category_products.this, itemname, null, null);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(Buylocal_category_products.this,2));
        categoryRecyclerView.setAdapter(viewAdapter);



        onclicklistners();

    }

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
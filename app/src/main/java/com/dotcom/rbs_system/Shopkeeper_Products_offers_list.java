package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.dotcom.rbs_system.Adapter.AdapterProductsOffersListRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterShopProductsRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Shopkeeper_Products_offers_list extends AppCompatActivity {
    ImageButton back_btn;
    RecyclerView products_offers;
    List<String> itemname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper__products_offers_list);
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        products_offers=(RecyclerView) findViewById(R.id.products_offers);
        itemname = new ArrayList<String>();
        itemname.add("Shirts");
        itemname.add("Tie");
        itemname.add("Mobiles");
        AdapterProductsOffersListRecyclerView adapterProductsOffersListRecyclerView = new AdapterProductsOffersListRecyclerView(Shopkeeper_Products_offers_list.this, itemname, null, null,null,null,null,null);
        products_offers.setLayoutManager(new GridLayoutManager(Shopkeeper_Products_offers_list.this,1));
        products_offers.setAdapter(adapterProductsOffersListRecyclerView);
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
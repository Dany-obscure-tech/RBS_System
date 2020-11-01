package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dotcom.rbs_system.Adapter.AdapterShopProductsRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterShopProductsShowcaseListRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Shopkeeper_shop_showcase extends AppCompatActivity {

    RecyclerView your_products_RecyclerView;
    List<String> product_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeerper_shop_showcase);

        your_products_RecyclerView=(RecyclerView)findViewById(R.id.your_products_RecyclerView);

        product_name = new ArrayList<String>();
        product_name.add("Shirt");
        product_name.add("Desktop");
        product_name.add("Mobiles");

        AdapterShopProductsShowcaseListRecyclerView adapterShopProductsShowcaseListRecyclerView = new AdapterShopProductsShowcaseListRecyclerView(Shopkeeper_shop_showcase.this, product_name, null, null,null,null);
        your_products_RecyclerView.setLayoutManager(new GridLayoutManager(Shopkeeper_shop_showcase.this,1));
        your_products_RecyclerView.setAdapter(adapterShopProductsShowcaseListRecyclerView);

    }
}
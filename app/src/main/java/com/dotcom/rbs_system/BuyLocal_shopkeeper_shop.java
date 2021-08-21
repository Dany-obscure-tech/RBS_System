package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.dotcom.rbs_system.Adapter.AdapterShopProductsRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BuyLocal_shopkeeper_shop extends AppCompatActivity {

    RecyclerView shop_products_RecyclerView;
    List<String> itemname;
    ImageButton back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_shopkeeper_shop);

        shop_products_RecyclerView = findViewById(R.id.shop_products_RecyclerView);
        back_btn =  findViewById(R.id.back_btn);
        itemname = new ArrayList<>();
        itemname.add("Laptop");
        itemname.add("Desktop");
        itemname.add("Mobiles");

        onclicklistners();

        AdapterShopProductsRecyclerView adapterShopProductsRecyclerView = new AdapterShopProductsRecyclerView(BuyLocal_shopkeeper_shop.this, itemname, null, null);
        shop_products_RecyclerView.setLayoutManager(new GridLayoutManager(BuyLocal_shopkeeper_shop.this, 2));
        shop_products_RecyclerView.setAdapter(adapterShopProductsRecyclerView);

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
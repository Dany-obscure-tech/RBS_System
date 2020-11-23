package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dotcom.rbs_system.Adapter.AdapterProductsOffersListRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterProductsOffersReceivedListRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BuyLocal_shopkeeperProductDetails extends AppCompatActivity {

    RecyclerView offers_received;
    List<String> customername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_shopkeeper_product_details);
        offers_received=(RecyclerView) findViewById(R.id.offers_received);
        customername = new ArrayList<String>();
        customername.add("Shahzaib Mehfooz");
        customername.add("Daniyal");
        customername.add("Husnain");
        AdapterProductsOffersReceivedListRecyclerView adapterProductsOffersReceivedListRecyclerView = new AdapterProductsOffersReceivedListRecyclerView(BuyLocal_shopkeeperProductDetails.this, customername, null, null,null,null,null);
        offers_received.setLayoutManager(new GridLayoutManager(BuyLocal_shopkeeperProductDetails.this,1));
        offers_received.setAdapter(adapterProductsOffersReceivedListRecyclerView);
    }
}
package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.dotcom.rbs_system.Adapter.AdapterOffersItemListRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BuyLocal_offers_option extends AppCompatActivity {

    RecyclerView offers;
    ImageButton back_btn;
    List<String> itemname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_offers_option);
        initialization();
        onclicklistners();

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initialization() {
        offers = (RecyclerView)findViewById(R.id.offers);
        back_btn = (ImageButton)findViewById(R.id.back_btn);
        itemname = new ArrayList<String>();
        itemname.add("Shirts");
        AdapterOffersItemListRecyclerView adapterOffersItemListRecyclerView=new AdapterOffersItemListRecyclerView(BuyLocal_offers_option.this,itemname,null,null,null,null,null);

        offers.setLayoutManager(new LinearLayoutManager(BuyLocal_offers_option.this,RecyclerView.HORIZONTAL,true));
        offers.setAdapter(adapterOffersItemListRecyclerView);
        offers.setLayoutManager(new GridLayoutManager(BuyLocal_offers_option.this,1));
        offers.setAdapter(adapterOffersItemListRecyclerView);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void onclicklistners() {
        back_btn_listner();
    }
    private void back_btn_listner() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
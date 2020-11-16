package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterShopProductsRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterShopProductsShowcaseListRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Shopkeeper_shop_showcase extends AppCompatActivity {

    RecyclerView your_products_RecyclerView;
    List<String> product_name;
    ImageButton back_btn;
    ImageView menu_btn;
    RelativeLayout side_option_menu;
    TextView offers_option;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeerper_shop_showcase);

        your_products_RecyclerView=(RecyclerView)findViewById(R.id.your_products_RecyclerView);
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        menu_btn=(ImageView)findViewById(R.id.menu_btn);
        side_option_menu=(RelativeLayout)findViewById(R.id.side_option_menu);
        offers_option=(TextView)findViewById(R.id.offers_option);
        onclicklistners();
        product_name = new ArrayList<String>();
        product_name.add("Shirts");
        product_name.add("Desktop");
        product_name.add("Mobiles");

        AdapterShopProductsShowcaseListRecyclerView adapterShopProductsShowcaseListRecyclerView = new AdapterShopProductsShowcaseListRecyclerView(Shopkeeper_shop_showcase.this, product_name, null, null,null,null);
        your_products_RecyclerView.setLayoutManager(new GridLayoutManager(Shopkeeper_shop_showcase.this,1));
        your_products_RecyclerView.setAdapter(adapterShopProductsShowcaseListRecyclerView);

    }

    private void onclicklistners() {
        back_btn_listner();
        menu_btn_listner();
        offers_option_listner();
    }

    private void offers_option_listner() {
        offers_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                side_option_menu.setVisibility(View.GONE);
                Intent intent=new Intent(Shopkeeper_shop_showcase.this,Shopkeeper_Products_offers_list.class);
                startActivity(intent);
            }
        });
    }

    private void menu_btn_listner() {
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (side_option_menu.getVisibility()==View.VISIBLE){
                    side_option_menu.setVisibility(View.GONE);
                }
                else {
                    side_option_menu.setVisibility(View.VISIBLE);
                }
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
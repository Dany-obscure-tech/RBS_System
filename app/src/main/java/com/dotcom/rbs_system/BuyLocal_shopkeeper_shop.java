package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.AdapterShopProductsRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BuyLocal_shopkeeper_shop extends AppCompatActivity {

    RecyclerView shop_products_RecyclerView;
    AdapterShopProductsRecyclerView adapterShopProductsRecyclerView;

    List<String> itemname;
    List<String> price;
    List<String> itemImage;
    List<String> productID;
    List<String> productCategory;

    ImageButton back_btn;
    String shopkeeperId;

    TextView shopkeeperName;
    ImageView shopkeeperLogo,shopkeeperBanner;

    DatabaseReference shopkeeperStockRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_shopkeeper_shop);

        shop_products_RecyclerView = findViewById(R.id.shop_products_RecyclerView);
        shop_products_RecyclerView.setLayoutManager(new GridLayoutManager(BuyLocal_shopkeeper_shop.this, 2));

        back_btn =  findViewById(R.id.back_btn);
        itemname = new ArrayList<>();
        price = new ArrayList<>();
        itemImage = new ArrayList<>();
        productID = new ArrayList<>();
        productCategory = new ArrayList<>();

        shopkeeperName = (TextView)findViewById(R.id.shopkeeperName);
        shopkeeperName.setText(getIntent().getStringExtra("SHOPKEEPER_NAME"));
        shopkeeperLogo = (ImageView) findViewById(R.id.shopkeeperLogo);
        Picasso.get().load(getIntent().getStringExtra("SHOPKEEPER_LOGO")).into(shopkeeperLogo);
        shopkeeperBanner = (ImageView) findViewById(R.id.shopkeeperBanner);
        Picasso.get().load(getIntent().getStringExtra("SHOPKEEPER_BANNER")).into(shopkeeperBanner);

        shopkeeperId = getIntent().getStringExtra("SHOPKEEPER_ID");

        shopkeeperStockRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/" + shopkeeperId);

        initialProcesses();
        onclicklistners();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void initialProcesses() {
        fetchingShopkeeperStockDetails();

    }

    private void fetchingShopkeeperStockDetails() {
        shopkeeperStockRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){

                    for (DataSnapshot snapshot2: snapshot1.getChildren()){

                        itemname.add(snapshot2.child("Item_name").getValue().toString());
                        price.add(snapshot2.child("Price").getValue().toString());
                        itemImage.add(snapshot2.child("Image").getValue().toString());
                        productID.add(snapshot2.getKey());
                        productCategory.add(snapshot2.child("Category").getValue().toString());
                    }
                }

                adapterShopProductsRecyclerView = new AdapterShopProductsRecyclerView(BuyLocal_shopkeeper_shop.this, itemname, price,itemImage,productID,productCategory,shopkeeperId);
                shop_products_RecyclerView.setAdapter(adapterShopProductsRecyclerView);
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

    private void back_btn_listner() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
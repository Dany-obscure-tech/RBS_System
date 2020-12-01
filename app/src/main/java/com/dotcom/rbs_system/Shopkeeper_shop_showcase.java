package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterShopProductsRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterShopProductsShowcaseListRecyclerView;
import com.dotcom.rbs_system.Adapter.SliderAdapterExample;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Shopkeeper_shop_showcase extends AppCompatActivity {

    DatabaseReference stockRef;
    RecyclerView your_products_RecyclerView;
    List<String> product_name;
    List<String> category;
    List<String> product_price;
    List<String> product_no_of_offers;
    List<String> image;
    List<String> key_idList;
    ImageButton back_btn;
    ImageView menu_btn;
    RelativeLayout side_option_menu;
    TextView offers_option;

    StorageReference itemImageStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeerper_shop_showcase);


        stockRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        your_products_RecyclerView=(RecyclerView)findViewById(R.id.your_products_RecyclerView);
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        menu_btn=(ImageView)findViewById(R.id.menu_btn);
        side_option_menu=(RelativeLayout)findViewById(R.id.side_option_menu);
        offers_option=(TextView)findViewById(R.id.offers_option);
        product_name = new ArrayList<String>();
        category = new ArrayList<String>();
        product_price = new ArrayList<String>();
        product_no_of_offers = new ArrayList<String>();
        image = new ArrayList<String>();
        key_idList = new ArrayList<String>();





        initialProcess();
        onclicklistners();
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    private void initialProcess() {
        fetchData();
    }

    private void fetchData() {
        stockRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    for (DataSnapshot snapshot2:snapshot1.getChildren()){
                        category.add(snapshot2.child("Category").getValue().toString());
                        product_name.add(snapshot2.child("Item_name").getValue().toString());
                        product_price.add(snapshot2.child("Price").getValue().toString());
                        image.add(snapshot2.child("Image").getValue().toString());
                        product_no_of_offers.add(String.valueOf(snapshot2.child("Offers").getChildrenCount()));
                        key_idList.add(snapshot2.child("Item_id").getValue().toString());





                    }
                }

                AdapterShopProductsShowcaseListRecyclerView adapterShopProductsShowcaseListRecyclerView = new AdapterShopProductsShowcaseListRecyclerView(Shopkeeper_shop_showcase.this, product_name, category, image,product_price,product_no_of_offers,key_idList);
                your_products_RecyclerView.setLayoutManager(new GridLayoutManager(Shopkeeper_shop_showcase.this,1));
                your_products_RecyclerView.setAdapter(adapterShopProductsShowcaseListRecyclerView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////

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
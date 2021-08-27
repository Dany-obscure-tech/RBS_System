package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterOffersItemListRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyLocal_offers_option extends AppCompatActivity {

    AdapterOffersItemListRecyclerView adapterOffersItemListRecyclerView;

    DatabaseReference customerOffersRef;
    ImageButton search_imageBtn;
    EditText search_editText;
    RecyclerView offers;
    ImageButton back_btn;

    List<String> itemname;
    List<String> price;
    List<String> itemImage;
    List<String> offer_status;
    List<String> offer_product_price;
    List<String> product_offer_msg;
    List<String> product_keyId;
    List<String> shopkeeperID;
    List<String> itemCategory;

    List<String> filtereditemname;
    List<String> filteredprice;
    List<String> filtereditemImage;
    List<String> filteredoffer_status;
    List<String> filteredoffer_product_price;
    List<String> filteredproduct_offer_msg;
    List<String> filteredproduct_keyId;
    List<String> filteredshopkeeperID;
    List<String> filtereditemCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_offers_option);
        initialization();
        initialProcess();
        onclicklistners();

    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initialization() {
        search_imageBtn = (ImageButton)findViewById(R.id.search_imageBtn);
        search_editText = (EditText) findViewById(R.id.search_editText);
        customerOffersRef = FirebaseDatabase.getInstance().getReference("Customer_offers/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        offers = findViewById(R.id.offers);
        back_btn = findViewById(R.id.back_btn);

        filtereditemname = new ArrayList<>();
        filteredprice = new ArrayList<>();
        filtereditemImage = new ArrayList<>();
        filteredoffer_status = new ArrayList<>();
        filteredoffer_product_price = new ArrayList<>();
        filteredproduct_offer_msg = new ArrayList<>();
        filteredproduct_keyId = new ArrayList<>();
        filteredshopkeeperID = new ArrayList<>();
        filtereditemCategory = new ArrayList<>();

        itemname = new ArrayList<>();
        price = new ArrayList<>();
        itemImage = new ArrayList<>();
        offer_status = new ArrayList<>();
        offer_product_price = new ArrayList<>();
        product_offer_msg = new ArrayList<>();
        product_keyId = new ArrayList<>();
        shopkeeperID = new ArrayList<>();
        itemCategory = new ArrayList<>();


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void initialProcess() {
        fetchData();
    }

    private void fetchData() {
        customerOffersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    itemname.add(snapshot1.child("item_name").getValue().toString());
                    price.add(snapshot1.child("item_price").getValue().toString());
                    itemImage.add(snapshot1.child("item_image").getValue().toString());
                    offer_status.add(snapshot1.child("offer_status").getValue().toString());
                    offer_product_price.add(snapshot1.child("amount").getValue().toString());
                    product_offer_msg.add(snapshot1.child("message").getValue().toString());
                    product_keyId.add(snapshot1.child("item_keyId").getValue().toString());
                    shopkeeperID.add(snapshot1.child("shopkeeper").getValue().toString());
                    itemCategory.add(snapshot1.child("item_category").getValue().toString());

                    filtereditemname.add(snapshot1.child("item_name").getValue().toString());
                    filteredprice.add(snapshot1.child("item_price").getValue().toString());
                    filtereditemImage.add(snapshot1.child("item_image").getValue().toString());
                    filteredoffer_status.add(snapshot1.child("offer_status").getValue().toString());
                    filteredoffer_product_price.add(snapshot1.child("amount").getValue().toString());
                    filteredproduct_offer_msg.add(snapshot1.child("message").getValue().toString());
                    filteredproduct_keyId.add(snapshot1.child("item_keyId").getValue().toString());
                    filteredshopkeeperID.add(snapshot1.child("shopkeeper").getValue().toString());
                    filtereditemCategory.add(snapshot1.child("item_category").getValue().toString());
                }

                adapterOffersItemListRecyclerView = new AdapterOffersItemListRecyclerView(BuyLocal_offers_option.this, filtereditemname, filteredprice, filtereditemImage, filteredoffer_status, filteredoffer_product_price, filteredproduct_offer_msg, filteredproduct_keyId, filteredshopkeeperID, filtereditemCategory);
                offers.setLayoutManager(new GridLayoutManager(BuyLocal_offers_option.this, 1));
                offers.setAdapter(adapterOffersItemListRecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void onclicklistners() {
        back_btn_listner();
        search_imageBtn_listener();
    }

    private void back_btn_listner() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void search_imageBtn_listener() {
        search_imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filtereditemname.clear();
                filteredprice.clear();
                filtereditemImage.clear();
                filteredoffer_status.clear();
                filteredoffer_product_price.clear();
                filteredproduct_offer_msg.clear();
                filteredproduct_keyId.clear();
                filteredshopkeeperID.clear();
                filtereditemCategory.clear();

                if(!search_editText.getText().toString().isEmpty()){

                    for (int i = 0; i<itemname.size();i++){

                        if (itemname.get(i).regionMatches(true, i, search_editText.getText().toString(), 0, search_editText.getText().toString().length())){
                            System.out.println(itemname.get(i));
                            filtereditemname.add(itemname.get(i));
                            filteredprice.add(price.get(i));
                            filtereditemImage.add(itemImage.get(i));
                            filteredoffer_status.add(offer_status.get(i));
                            filteredoffer_product_price.add(offer_product_price.get(i));
                            filteredproduct_offer_msg.add(product_offer_msg.get(i));
                            filteredproduct_keyId.add(product_keyId.get(i));
                            filteredshopkeeperID.add(shopkeeperID.get(i));
                            filtereditemCategory.add(itemCategory.get(i));
                        }
                    }
                }else {
                    for (int i = 0; i<itemname.size();i++){
                        filtereditemname.add(itemname.get(i));
                        filteredprice.add(price.get(i));
                        filtereditemImage.add(itemImage.get(i));
                        filteredoffer_status.add(offer_status.get(i));
                        filteredoffer_product_price.add(offer_product_price.get(i));
                        filteredproduct_offer_msg.add(product_offer_msg.get(i));
                        filteredproduct_keyId.add(product_keyId.get(i));
                        filteredshopkeeperID.add(shopkeeperID.get(i));
                        filtereditemCategory.add(itemCategory.get(i));
                    }

                }

                adapterOffersItemListRecyclerView.notifyDataSetChanged();

            }
        });
    }
}
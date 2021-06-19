package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dotcom.rbs_system.Classes.ActionBarTitle;
import com.dotcom.rbs_system.Classes.Exchanged_itemdata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Rbs_options extends AppCompatActivity {

    DatabaseReference bannerRef;

    CardView BUY,Sale,repair,Exchange,Accessories,Settings;

    ImageButton icon1,icon2,icon3,icon5,icon4,icon6,icon8,icon9,back_btn;
    Button alert_addAccessory_btn,alert_saleAccessory_btn;

    ImageView mainBanner_imageView;

    Dialog selectAccessory_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rbs_options);

        ActionBarTitle.getInstance().getTextView().setText("POS");

        bannerRef = FirebaseDatabase.getInstance().getReference("Admin/banner");

        selectAccessory_dialog = new Dialog(this);
        selectAccessory_dialog.setContentView(R.layout.alert_select_accessory_screen);

        alert_addAccessory_btn = (Button) selectAccessory_dialog.findViewById(R.id.alert_addAccessory_btn);
        alert_saleAccessory_btn = (Button) selectAccessory_dialog.findViewById(R.id.alert_saleAccessory_btn);

        BUY=(CardView)findViewById(R.id.BUY);
        back_btn=(ImageButton) findViewById(R.id.back_btn);
        Sale=(CardView)findViewById(R.id.Sale);
        repair=(CardView)findViewById(R.id.repair);
        Exchange=(CardView)findViewById(R.id.Exchange);
        Accessories=(CardView)findViewById(R.id.Accessories);
        Settings=(CardView)findViewById(R.id.Settings);

        icon2=(ImageButton)findViewById(R.id.icon2);
        icon4=(ImageButton)findViewById(R.id.icon4);
        icon1=(ImageButton)findViewById(R.id.icon1);
        icon3=(ImageButton)findViewById(R.id.icon3);
        icon5=(ImageButton)findViewById(R.id.icon5);
        icon6=(ImageButton)findViewById(R.id.icon6);
        icon8=(ImageButton)findViewById(R.id.icon8);
        icon9=(ImageButton)findViewById(R.id.icon9);

        mainBanner_imageView = (ImageView)findViewById(R.id.mainBanner_imageView);
        getBannerImage();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAccessory_dialog.show();
            }
        });

        Sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rbs_options.this,Sale.class);
                intent.putExtra("ITEM_SELL_CHECK","FALSE");
                startActivity(intent);
            }
        });

        icon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAccessory_dialog.show();
            }
        });

        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rbs_options.this,Repair_Ticket.class);
                startActivity(intent);
            }
        });
        icon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rbs_options.this,Exchange.class);
                startActivity(intent);
            }
        });
        BUY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rbs_options.this,Buy.class);
                Exchanged_itemdata.getInstance().setExchangeCheck(false);
                Exchanged_itemdata.getInstance().setExchangeFromBuyCheck(false);
                Exchanged_itemdata.getInstance().clearData();

                startActivity(intent);
            }
        });
        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rbs_options.this,Buy.class);
                Exchanged_itemdata.getInstance().setExchangeCheck(false);
                Exchanged_itemdata.getInstance().setExchangeFromBuyCheck(false);
                Exchanged_itemdata.getInstance().clearData();
                startActivity(intent);
            }
        });
        Sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rbs_options.this,Sale.class);
                intent.putExtra("ITEM_SELL_CHECK","FALSE");
                Exchanged_itemdata.getInstance().setExchangeCheck(false);
                Exchanged_itemdata.getInstance().setExchangeFromBuyCheck(false);
                Exchanged_itemdata.getInstance().clearData();
                startActivity(intent);
            }
        });
        icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rbs_options.this,Sale.class);
                intent.putExtra("ITEM_SELL_CHECK","FALSE");
                Exchanged_itemdata.getInstance().setExchangeCheck(false);
                Exchanged_itemdata.getInstance().setExchangeFromBuyCheck(false);
                Exchanged_itemdata.getInstance().clearData();
                startActivity(intent);
            }
        });
        repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rbs_options.this,Repair_Ticket.class);
                startActivity(intent);
            }
        });
        Exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rbs_options.this,Exchange.class);
                startActivity(intent);
            }
        });

        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rbs_options.this,Settings.class);
                startActivity(intent);
            }
        });

        icon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rbs_options.this,Settings.class);
                startActivity(intent);
            }
        });

        alert_addAccessory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rbs_options.this,Accessory_add.class);
                startActivity(intent);
                selectAccessory_dialog.dismiss();
            }
        });
        alert_saleAccessory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rbs_options.this,Accessory_sale.class);
                startActivity(intent);
                selectAccessory_dialog.dismiss();
            }
        });

        icon8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rbs_options.this,RBS_Vendors.class);
                startActivity(intent);
            }
        });
        icon9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Rbs_options.this,Smart_Share.class);
                startActivity(intent);
            }
        });
    }


    private void getBannerImage() {
        bannerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Picasso.get().load(dataSnapshot.getValue().toString()).into(mainBanner_imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
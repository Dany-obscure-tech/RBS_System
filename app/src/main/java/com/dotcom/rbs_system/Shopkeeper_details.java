package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Shopkeeper_details extends AppCompatActivity {

    ImageButton back_btn;
    String shopkeeperId;
    DatabaseReference shopkeeperDetailsRef;

    ImageView banner_imageView,logo_imageView;
    TextView shop_name_textView,shop_address_textView,shop_email_textView,shop_phno_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_details);

        initialization();
        datafetch();
        onclicklistners();

    }

    private void initialization() {
        banner_imageView = (ImageView)findViewById(R.id.banner_imageView);
        logo_imageView = (ImageView)findViewById(R.id.logo_imageView);

        shop_name_textView = (TextView)findViewById(R.id.shop_name_textView);
        shop_address_textView = (TextView)findViewById(R.id.shop_address_textView);
        shop_email_textView = (TextView)findViewById(R.id.shop_email_textView);
        shop_phno_textView = (TextView)findViewById(R.id.shop_phno_textView);


        shopkeeperId = getIntent().getStringExtra("SHOPKEEPER_ID");
        Toast.makeText(Shopkeeper_details.this, shopkeeperId, Toast.LENGTH_SHORT).show();
        back_btn = findViewById(R.id.back_btn);
        shopkeeperDetailsRef = FirebaseDatabase.getInstance().getReference("Users_data/"+shopkeeperId+"/Shopkeeper_details");
    }

    private void datafetch() {
        shopkeeperDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shop_name_textView.setText(snapshot.child("shop_name").getValue().toString());
                shop_address_textView.setText(snapshot.child("shop_address").getValue().toString());
                shop_email_textView.setText(snapshot.child("shop_email").getValue().toString());
                shop_phno_textView.setText(snapshot.child("shop_phno").getValue().toString());

                Picasso.get().load(snapshot.child("shop_logo").getValue().toString()).into(logo_imageView);
                Picasso.get().load(snapshot.child("shop_banner").getValue().toString()).into(banner_imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
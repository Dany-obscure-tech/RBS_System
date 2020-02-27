package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    CardView BUY,Sale,repair,Exchange;
    ImageButton icon1,icon2,icon3,icon5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BUY=(CardView)findViewById(R.id.BUY);
        Sale=(CardView)findViewById(R.id.Sale);
        repair=(CardView)findViewById(R.id.repair);
        Exchange=(CardView)findViewById(R.id.Exchange);
        icon2=(ImageButton)findViewById(R.id.icon2);
        icon1=(ImageButton)findViewById(R.id.icon1);
        icon3=(ImageButton)findViewById(R.id.icon3);
        icon5=(ImageButton)findViewById(R.id.icon5);
        Sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Sale.class);
                startActivity(intent);
            }
        });
        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Buy.class);
                startActivity(intent);
            }
        });
        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Repairs.class);
                startActivity(intent);
            }
        });
        icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Sale.class);
                startActivity(intent);
            }
        });
        icon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Exchange.class);
                startActivity(intent);
            }
        });
        BUY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Buy.class);
                startActivity(intent);
            }
        });
        Sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Sale.class);
                startActivity(intent);
            }
        });
        repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Repairs.class);
                startActivity(intent);
            }
        });
        Exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Exchange.class);
                startActivity(intent);
            }
        });
    }
}

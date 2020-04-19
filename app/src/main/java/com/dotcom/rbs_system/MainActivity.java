package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dotcom.rbs_system.Classes.Exchanged_itemdata;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
//Navigation drawer
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    //Navigation drawer

    CardView BUY,Sale,repair,Exchange,Accessories,Settings;
    ImageButton icon1,icon2,icon3,icon5,icon4,icon6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Nav drawer
        dl = (DrawerLayout)findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch(id)
                {
                    case R.id.nav_profile:
                        Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_settings:
                        intent = new Intent(MainActivity.this, Settings.class);
                        startActivity(intent);
                        closeDrawer();
                        return true;
                    case R.id.nav_logout:
                        Toast.makeText(MainActivity.this, "My Cart",Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;
                }
                return true;
            }

            private void closeDrawer() {
                if (dl.isDrawerOpen(GravityCompat.START)) {
                    dl.closeDrawer(GravityCompat.START);
                }
            }
        });



        BUY=(CardView)findViewById(R.id.BUY);
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

        Accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Accessories.class);
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

        icon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Accessories.class);
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
                Exchanged_itemdata.getInstance().setExchangeCheck(false);
                Exchanged_itemdata.getInstance().setExchangeFromBuyCheck(false);
                Exchanged_itemdata.getInstance().clearData();

                startActivity(intent);
            }
        });
        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Buy.class);
                Exchanged_itemdata.getInstance().setExchangeCheck(false);
                Exchanged_itemdata.getInstance().setExchangeFromBuyCheck(false);
                Exchanged_itemdata.getInstance().clearData();
                startActivity(intent);
            }
        });
        Sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Sale.class);
                Exchanged_itemdata.getInstance().setExchangeCheck(false);
                Exchanged_itemdata.getInstance().setExchangeFromBuyCheck(false);
                Exchanged_itemdata.getInstance().clearData();
                startActivity(intent);
            }
        });
        icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Sale.class);
                Exchanged_itemdata.getInstance().setExchangeCheck(false);
                Exchanged_itemdata.getInstance().setExchangeFromBuyCheck(false);
                Exchanged_itemdata.getInstance().clearData();
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

        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Settings.class);
                startActivity(intent);
            }
        });

        icon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Settings.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}

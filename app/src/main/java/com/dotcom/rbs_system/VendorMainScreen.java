package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class VendorMainScreen extends AppCompatActivity {

    private DrawerLayout vendor_drawer_layout;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    RecyclerView vendor_inventory_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_main_screen);
        vendor_drawer_layout = (DrawerLayout)findViewById(R.id.vendor_drawer_layout);
        vendor_inventory_items=(RecyclerView)findViewById(R.id.vendor_inventory_items);


        t = new ActionBarDrawerToggle(this, vendor_drawer_layout,R.string.Open, R.string.Close);

        vendor_drawer_layout.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.nav_shop:

                        break;
                    case R.id.nav_orders:
                        intent = new Intent(VendorMainScreen.this, vendor_orders_screen.class);
                        startActivity(intent);
                        closeDrawer();
                        return true;
                    case R.id.nav_profile:
                        Toast.makeText(VendorMainScreen.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_logout:
                        Toast.makeText(VendorMainScreen.this, "Logout", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        return true;
                }


                return true;

            }
            private void closeDrawer() {
                if (vendor_drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    vendor_drawer_layout.closeDrawer(GravityCompat.START);
                }
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}

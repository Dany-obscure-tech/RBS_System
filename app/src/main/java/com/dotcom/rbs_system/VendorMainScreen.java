package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterCategoryRBS_RecyclerView;
import com.dotcom.rbs_system.Adapter.Adapter_Vendor_inventory_RecyclerView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class VendorMainScreen extends AppCompatActivity {

    private DrawerLayout vendor_drawer_layout;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    final VendorHome fragment_vendor_home = new VendorHome();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_main_screen);
        vendor_drawer_layout = (DrawerLayout)findViewById(R.id.vendor_drawer_layout);


        t = new ActionBarDrawerToggle(this, vendor_drawer_layout,R.string.Open, R.string.Close);

        vendor_drawer_layout.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        nv = (NavigationView)findViewById(R.id.nv);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.screenContainer,fragment_vendor_home).commit();
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_shop:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.screenContainer,fragment_vendor_home).commit();
                        closeDrawer();
                        break;
                    case R.id.nav_orders:
                        return true;
                    case R.id.nav_profile:
                        Toast.makeText(VendorMainScreen.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent1 = new Intent(VendorMainScreen.this,SignInActivity.class);
                        finish();
                        startActivity(intent1);
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

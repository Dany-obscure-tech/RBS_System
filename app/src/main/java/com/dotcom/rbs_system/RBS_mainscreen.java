package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class RBS_mainscreen extends AppCompatActivity {
    private DrawerLayout vendor_drawer_layout;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    final Rbs_home rbs_home = new Rbs_home();
    final Rbs_passcode rbs_passcode = new Rbs_passcode();
    final RBS_Vendor_Orders rbs_vendor_orders = new RBS_Vendor_Orders();
    final RBS_offers rbs_offers = new RBS_offers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_b_s_mainscreen);

        Initialize();
    }

    private void Initialize() {

        vendor_drawer_layout = (DrawerLayout) findViewById(R.id.rbs_drawer_layout);


        t = new ActionBarDrawerToggle(this, vendor_drawer_layout, R.string.Open, R.string.Close);

        vendor_drawer_layout.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        nv = (NavigationView) findViewById(R.id.nv);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, rbs_home).commit();
        nv.getMenu().findItem(R.id.nav_home).setChecked(true);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, rbs_home).commit();
                        closeDrawer();
                        break;

                    case R.id.nav_rbs:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, rbs_passcode).commit();
                        closeDrawer();
                        break;

                    case R.id.nav_rbs_vendor_orders:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, rbs_vendor_orders).commit();
                        closeDrawer();
                        break;

                    case R.id.nav_rbs_offers:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, rbs_offers).commit();
                        closeDrawer();
                        break;


                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent1 = new Intent(RBS_mainscreen.this, SignInActivity.class);
                        finish();
                        startActivity(intent1);
                        Toast.makeText(RBS_mainscreen.this, "Logout", Toast.LENGTH_SHORT).show();
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

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
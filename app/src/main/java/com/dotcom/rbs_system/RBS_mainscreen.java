package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Classes.ActionBarTitle;
import com.google.android.material.navigation.NavigationView;

public class RBS_mainscreen extends AppCompatActivity {
    ActionBar actionBar;

    private long lastPressedTime;

    private static final int PERIOD = 2000;

    TextView actionBarTitle;

    private DrawerLayout vendor_drawer_layout;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    final Rbs_home rbs_home = new Rbs_home();
    final RBS_option rbs_option = new RBS_option();
    final RBS_Vendor_Orders rbs_vendor_orders = new RBS_Vendor_Orders();
    final RBS_setting rbs_setting = new RBS_setting();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_b_s_mainscreen);

        Initialize();
    }

    private void Initialize() {
        actionBar = this.getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.profile_screen_header));

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT);

        actionBarTitle = new TextView(getApplicationContext());

        actionBarTitle.setLayoutParams(lp);
        actionBarTitle.setTextColor(Color.WHITE);
        actionBarTitle.setTextSize(22);
        actionBarTitle.setTypeface(Typeface.DEFAULT_BOLD);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(actionBarTitle);

        ActionBarTitle.getInstance().setTextView(actionBarTitle);

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
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, rbs_option).commit();
                        closeDrawer();
                        break;

                    case R.id.nav_rbs_vendor_orders:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, rbs_vendor_orders).commit();
                        closeDrawer();
                        break;

                    case R.id.nav_settings:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, rbs_setting,"RBS Settings").commit();
                        closeDrawer();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Press again to exit.",
                                Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }

}
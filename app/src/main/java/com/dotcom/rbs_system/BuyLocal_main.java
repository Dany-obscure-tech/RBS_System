package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BuyLocal_main extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    final Home fragment_home = new Home();
    final Profile fragment_profile = new Profile();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_main);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationView);

        //        To remove default colors of icons
        bottomNavigationView.setItemIconTintList(null);
        // Fragment home already selected
        bottomNavigationView.setSelectedItemId(R.id.home);
        // Bottom navigation menu fragments connected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        // Switch to page one
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.screenContainer,fragment_home).commit();
                        break;
                    case R.id.profile:
                        // Switch to page two
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.screenContainer,fragment_profile).commit();
                        break;
                }
                return true;
            }
        });
        setNavMenuItemThemeColors(Color.parseColor("#0075B5"));
    }

//Icon color changed when tap on icon
    public void setNavMenuItemThemeColors(int color){
        //Setting default colors for menu item Text and Icon
        int navDefaultTextColor = Color.parseColor("#202020");
        int navDefaultIconColor = Color.parseColor("#737373");

        //Defining ColorStateList for menu item Text
        ColorStateList navMenuTextList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed}
                },
                new int[] {
                        color,
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor
                }
        );

        //Defining ColorStateList for menu item Icon
        ColorStateList navMenuIconList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed}
                },
                new int[] {
                        color,
                        navDefaultIconColor,
                        navDefaultIconColor,
                        navDefaultIconColor,
                        navDefaultIconColor
                }
        );

        bottomNavigationView.setItemTextColor(navMenuTextList);
        bottomNavigationView.setItemIconTintList(navMenuIconList);
    }
}
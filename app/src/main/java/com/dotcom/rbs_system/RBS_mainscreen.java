package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Classes.ActionBarTitle;
import com.dotcom.rbs_system.Classes.UserDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RBS_mainscreen extends AppCompatActivity {

    private static final int LOGO_READ_REQUEST_CODE = 42;
    private static final int BANNER_READ_REQUEST_CODE = 43;


    ActionBar actionBar;
    StorageReference storageReference;
    DatabaseReference reference;

    Dialog confirmation_alert;
    TextView yes_btn_textview, cancel_btn_textview;

    TextView actionBarTitle;

    NavigationView nv;
    ImageView shopLogo_imageView;
    TextView shopName_textView, shopEmail_textView;

    private DrawerLayout vendor_drawer_layout;
    private ActionBarDrawerToggle t;
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
        reference = FirebaseDatabase.getInstance().getReference("Users_data/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Shopkeeper_details");
        storageReference = FirebaseStorage.getInstance().getReference().child("Users_data");

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

        confirmation_alert = new Dialog(this);
        confirmation_alert.setContentView(R.layout.exit_confirmation_alert);
        yes_btn_textview = confirmation_alert.findViewById(R.id.yes_btn_textview);
        cancel_btn_textview = confirmation_alert.findViewById(R.id.cancel_btn_textview);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(actionBarTitle);

        ActionBarTitle.getInstance().setTextView(actionBarTitle);

        vendor_drawer_layout = findViewById(R.id.rbs_drawer_layout);

        t = new ActionBarDrawerToggle(this, vendor_drawer_layout, R.string.Open, R.string.Close);

        vendor_drawer_layout.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        nv = findViewById(R.id.nv);
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
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, rbs_setting, "RBS Settings").commit();
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

        View navheader = nv.getHeaderView(0);
        shopLogo_imageView = navheader.findViewById(R.id.shopLogo_imageView);
        Picasso.get().load(UserDetails.getInstance().getShopLogo()).into(shopLogo_imageView);

        shopName_textView = navheader.findViewById(R.id.shopName_textView);
        shopName_textView.setText(UserDetails.getInstance().getShopName());

        shopEmail_textView = navheader.findViewById(R.id.shopEmail_textView);
        shopEmail_textView.setText(UserDetails.getInstance().getShopEmail());


        onClickListners();

    }

    private void onClickListners() {

        cancel_btn_textview_listner();
        yes_btn_textview_listner();

    }

    private void cancel_btn_textview_listner() {
        cancel_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmation_alert.dismiss();
            }
        });
    }

    private void yes_btn_textview_listner() {
        yes_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                confirmation_alert.show();

                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == LOGO_READ_REQUEST_CODE) {
                storageReference.child("shopkeeper_logos").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("logo").putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.child("shopkeeper_logos").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("logo").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                reference.child("shop_logo").setValue(String.valueOf(uri.toString()));
                                UserDetails.getInstance().setShopLogo(String.valueOf(uri.toString()));
                                getSupportFragmentManager().beginTransaction().detach(getSupportFragmentManager().findFragmentByTag("RBS Settings")).attach(getSupportFragmentManager().findFragmentByTag("RBS Settings")).commit();

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RBS_mainscreen.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (requestCode == BANNER_READ_REQUEST_CODE) {
                storageReference.child("shopkeeper_banner").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("banner").putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.child("shopkeeper_banner").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("banner").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                reference.child("shop_banner").setValue(String.valueOf(uri.toString()));
                                UserDetails.getInstance().setShopBanner(String.valueOf(uri.toString()));
                                getSupportFragmentManager().beginTransaction().detach(getSupportFragmentManager().findFragmentByTag("RBS Settings")).attach(getSupportFragmentManager().findFragmentByTag("RBS Settings")).commit();

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RBS_mainscreen.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }
}
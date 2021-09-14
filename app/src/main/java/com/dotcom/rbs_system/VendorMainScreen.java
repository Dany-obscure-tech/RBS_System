package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.dotcom.rbs_system.Classes.VendorStockDetails;
import com.github.dhaval2404.imagepicker.ImagePicker;
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

public class VendorMainScreen extends AppCompatActivity {

    private static final int LOGO_READ_REQUEST_CODE = 42;
    private static final int BANNER_READ_REQUEST_CODE = 43;

    StorageReference storageReference;
    DatabaseReference reference;
    ImageView vendorLogo_imageView;
    TextView VendorName_textView,vendorEmail_textView;

    ActionBar actionBar;
    TextView actionBarTitle;

    Dialog confirmation_alert;
    TextView yes_btn_textview, cancel_btn_textview;

    private DrawerLayout vendor_drawer_layout;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    final VendorShop fragment_vendor_home = new VendorShop();
    final VendorProfile fragment_vendor_profile = new VendorProfile();
    private Uri fileUri;
    VendorStockDetails vendorStockDetails;

    DatabaseReference vendorStockRef;
    StorageReference vendorStockImageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_main_screen);

        reference = FirebaseDatabase.getInstance().getReference("Users_data/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Vendor_details");
        storageReference = FirebaseStorage.getInstance().getReference().child("Users_data");

        actionBar = this.getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.screen_header_rectangle));

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

        vendorStockRef = FirebaseDatabase.getInstance().getReference("Vendor_stock/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        vendorStockImageReference = FirebaseStorage.getInstance().getReference().child("Vendor_Stock_Images/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        vendor_drawer_layout = findViewById(R.id.vendor_drawer_layout);

        confirmation_alert = new Dialog(this);
        confirmation_alert.setContentView(R.layout.exit_confirmation_alert);
        yes_btn_textview = confirmation_alert.findViewById(R.id.yes_btn_textview);
        cancel_btn_textview = confirmation_alert.findViewById(R.id.cancel_btn_textview);


        t = new ActionBarDrawerToggle(this, vendor_drawer_layout, R.string.Open, R.string.Close);

        vendor_drawer_layout.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        nv = findViewById(R.id.nv);
        View navheader = nv.getHeaderView(0);
        vendorLogo_imageView = navheader.findViewById(R.id.vendorLogo_imageView);
        Picasso.get().load(UserDetails.getInstance().getVendorLogo()).into(vendorLogo_imageView);

        VendorName_textView = navheader.findViewById(R.id.VendorName_textView);
        VendorName_textView.setText(UserDetails.getInstance().getVendorName());

        vendorEmail_textView = navheader.findViewById(R.id.vendorEmail_textView);
        vendorEmail_textView.setText(UserDetails.getInstance().getVendorEmail());

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, fragment_vendor_home).commit();
        nv.setCheckedItem(R.id.nav_shop);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_shop:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, new VendorShop()).commit();
                        closeDrawer();
                        break;
                    case R.id.nav_orders:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, new VendorOrders()).commit();
                        closeDrawer();
                        break;
                    case R.id.nav_profile:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, new VendorProfile(), "VENDOR_PROFILE_FRAGMENT").commit();
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

        oncreateListners();
    }

    private void oncreateListners() {
        yes_btn_textview_listner();
        cancel_btn_textview_listner();
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
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOGO_READ_REQUEST_CODE) {
                storageReference.child("vendors_logos").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("logo").putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.child("vendors_logos").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("logo").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                reference.child("vendor_logo").setValue(String.valueOf(uri.toString()));
                                UserDetails.getInstance().setVendorLogo(String.valueOf(uri.toString()));
                                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, fragment_vendor_profile).commit();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VendorMainScreen.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (requestCode == BANNER_READ_REQUEST_CODE) {
                storageReference.child("vendors_banners").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("banner").putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.child("vendors_banners").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("banner").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                reference.child("vendor_banner").setValue(String.valueOf(uri.toString()));
                                UserDetails.getInstance().setVendorBanner(String.valueOf(uri.toString()));
                                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.screenContainer, fragment_vendor_profile).commit();

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VendorMainScreen.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                vendorStockDetails = VendorStockDetails.getInstance();

                //Image Uri will not be null for RESULT_OK
                fileUri = data.getData();

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(VendorMainScreen.this.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;

                    System.out.println("called");
                    System.out.println(vendorStockDetails.getCategory());
                    System.out.println(vendorStockDetails.getKeyId());

                    vendorStockImageReference.child(vendorStockDetails.getCategory()).child(vendorStockDetails.getKeyId()).child("stock_image").putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            vendorStockImageReference.child(vendorStockDetails.getCategory()).child(vendorStockDetails.getKeyId()).child("stock_image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    vendorStockRef.child(vendorStockDetails.getCategory()).child(vendorStockDetails.getKeyId()).child("Image_url").setValue(String.valueOf(uri.toString()));
                                    recreate();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(VendorMainScreen.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(VendorMainScreen.this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
                    connected = false;
                }
            }

            //You can get File object from intent
//            val file:File = ImagePicker.getFile(data)!!

            //You can also get File Path from intent
//                    val filePath:String = ImagePicker.getFilePath(data)!!
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
        } else if (resultCode == 111) {
            recreate();
            nv.setCheckedItem(R.id.nav_shop);
        }
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


}

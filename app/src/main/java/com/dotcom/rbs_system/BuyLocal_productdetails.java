package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.SliderAdapterExample;
import com.dotcom.rbs_system.Classes.Currency;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BuyLocal_productdetails extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    RelativeLayout offer_relativeLayout;
    String currency,profileImageUrl,customerName;
    CardView imageSlider;
    SliderView sliderView;
    List<String> imageUrl;
    EditText alertReportDescription_editText,alertMakeOfferAmount_editText,alertMakeOfferMessage_editText;
    TextView report_btn,distance_textView;
    TextView product_name_textview, category_textView, item_description_textview, itemPrice_textView, currency_textView;
    TextView offerStatus_textView,offerAmountCurrency_textView,offerAmount_textView,offerMessage_textView;
    Dialog report_alert_dialog;
    Dialog make_offer_alert_dialog;
    Button alertReportCancel_btn, make_offer_btn, alertMakeOfferCancel_btn, alertMakeOfferSubmit_btn,alertReportSubmit_btn;
    ImageButton back_btn, whatsapp_icon;
    ImageView profileImage;
    String productID, category,shopkeeperID;
    DatabaseReference itemRef, reportRef,userRef,stockRef,customerOfferRef;
    StorageReference itemImageStorageRef;
    int i, noOfimages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_productdetails);

        currency = Currency.getInstance().getCurrency();

        productID = getIntent().getStringExtra("PRODUCT_ID");
        category = getIntent().getStringExtra("CATEGORY");
        shopkeeperID = getIntent().getStringExtra("SHOPKEEPER_ID");

        imageUrl = new ArrayList<>();

        itemRef = FirebaseDatabase.getInstance().getReference("Items/" + category + "/" + productID);
        stockRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/"+shopkeeperID+"/"+ category + "/" + productID);
        reportRef = FirebaseDatabase.getInstance().getReference();
        customerOfferRef = FirebaseDatabase.getInstance().getReference();
        itemImageStorageRef = FirebaseStorage.getInstance().getReference().child("Item_Images/" + productID);

        offer_relativeLayout = (RelativeLayout) findViewById(R.id.offer_relativeLayout);

        imageSlider = (CardView) findViewById(R.id.imageSlider);
        back_btn = (ImageButton) findViewById(R.id.back_btn);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        whatsapp_icon = (ImageButton) findViewById(R.id.whatsapp_icon);
        report_btn = (TextView) findViewById(R.id.report_btn);
        distance_textView = (TextView) findViewById(R.id.distance_textView);

        product_name_textview = (TextView) findViewById(R.id.product_name_textview);
        category_textView = (TextView) findViewById(R.id.category_textView);
        item_description_textview = (TextView) findViewById(R.id.item_description_textview);
        itemPrice_textView = (TextView) findViewById(R.id.itemPrice_textView);
        currency_textView = (TextView) findViewById(R.id.currency_textView);

        offerStatus_textView = (TextView) findViewById(R.id.offerStatus_textView);
        offerAmountCurrency_textView = (TextView) findViewById(R.id.offerAmountCurrency_textView);
        offerAmount_textView = (TextView) findViewById(R.id.offerAmount_textView);
        offerMessage_textView = (TextView) findViewById(R.id.offerMessage_textView);

        report_alert_dialog = new Dialog(this);
        report_alert_dialog.setContentView(R.layout.alert_report);
        make_offer_alert_dialog = new Dialog(this);
        make_offer_alert_dialog.setContentView(R.layout.alert_make_offer);

        alertReportCancel_btn = report_alert_dialog.findViewById(R.id.alertReportCancel_btn);
        alertReportDescription_editText = report_alert_dialog.findViewById(R.id.alertReportDescription_editText);
        alertReportSubmit_btn = report_alert_dialog.findViewById(R.id.alertReportSubmit_btn);
        alertMakeOfferCancel_btn = make_offer_alert_dialog.findViewById(R.id.alertMakeOfferCancel_btn);
        alertMakeOfferSubmit_btn = make_offer_alert_dialog.findViewById(R.id.alertMakeOfferSubmit_btn);
        alertMakeOfferAmount_editText = make_offer_alert_dialog.findViewById(R.id.alertMakeOfferAmount_editText);
        alertMakeOfferMessage_editText = make_offer_alert_dialog.findViewById(R.id.alertMakeOfferMessage_editText);
        make_offer_btn = (Button) findViewById(R.id.make_offer_btn);

        sliderView = findViewById(R.id.imageSliders);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.parseColor("#01A0DA"));
        sliderView.setIndicatorUnselectedColor(Color.parseColor("#F1F1F1"));
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        InitialProcess();
        onclicklistners();
        midProcesses();


    }

    //////////////////////////////////////////////////////////////////////////////////

    private void onclicklistners() {
        report_btn_listner();
        alertReportSubmit_btn_listner();
        alertReportCancel_btn_listner();
        make_offer_btn_listner();
        alertMakeOfferCancel_btn_listner();
        alertMakeOfferSubmit_btn_listner();
        back_btn_listner();
        whatsapp_icon_listner();
        profileImage_listner();
    }

    private void alertReportSubmit_btn_listner() {
        alertReportSubmit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!alertReportDescription_editText.getText().toString().isEmpty()) {
                    reportRef.child("Customer_reports").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(productID).child("report_description").setValue(alertReportDescription_editText.getText().toString());
                    report_alert_dialog.dismiss();
                } else {
                    alertReportDescription_editText.setError("Enter Description!");
                }
            }
        });
    }

    private void profileImage_listner() {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyLocal_productdetails.this, BuyLocal_shopkeeper_shop.class);
                startActivity(intent);
            }
        });
    }

    private void whatsapp_icon_listner() {
        whatsapp_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Your body is here";
                String shareSub = "Your subject";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        });
    }

    private void back_btn_listner() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void alertMakeOfferCancel_btn_listner() {
        alertMakeOfferCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make_offer_alert_dialog.dismiss();
            }
        });
    }

    private void alertMakeOfferSubmit_btn_listner() {
        alertMakeOfferSubmit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (offerSubmitValidation()){
                    long timestamp = Calendar.getInstance().getTime().getTime();
                    stockRef.child("Offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("message").setValue(alertMakeOfferMessage_editText.getText().toString());
                    stockRef.child("Offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("amount").setValue(alertMakeOfferAmount_editText.getText().toString());
                    stockRef.child("Offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("customer").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    stockRef.child("Offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("customer_image").setValue(profileImageUrl);
                    stockRef.child("Offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("customer_name").setValue(customerName);
                    stockRef.child("Offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("timestamp").setValue(timestamp);

                    customerOfferRef.child("Customer_offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(productID).child("message").setValue(alertMakeOfferMessage_editText.getText().toString());
                    customerOfferRef.child("Customer_offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(productID).child("amount").setValue(alertMakeOfferAmount_editText.getText().toString());
                    customerOfferRef.child("Customer_offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(productID).child("shopkeeper").setValue(shopkeeperID);
                    customerOfferRef.child("Customer_offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(productID).child("offer_status").setValue("offer pending");
                    customerOfferRef.child("Customer_offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(productID).child("timestamp").setValue(timestamp);

                    make_offer_alert_dialog.dismiss();
                    recreate();
                }
            }
        });
    }

    private void make_offer_btn_listner() {

            make_offer_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (make_offer_btn.getText().toString().equals("CANCLE OFFER")){
                        stockRef.child("Offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                        customerOfferRef.child("Customer_offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(productID).removeValue();
                        recreate();
                    }else {
                        make_offer_alert_dialog.show();
                    }
                }
            });


    }

    private void alertReportCancel_btn_listner() {
        alertReportCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report_alert_dialog.dismiss();
            }
        });
    }

    private void report_btn_listner() {
        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report_alert_dialog.show();
            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////

    private void InitialProcess() {
        fetchingImageUrl();
        checkingOffer();
        fetchingItemDetails();

    }

    private void checkingOffer() {
        customerOfferRef.child("Customer_offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("offer_status").getValue().toString().equals("offer pending")){
                        offer_relativeLayout.setVisibility(View.VISIBLE);
                        offer_relativeLayout.setBackground(getResources().getDrawable(R.drawable.profile_screen_header));
                        offerAmountCurrency_textView.setText(Currency.getInstance().getCurrency());
                        offerStatus_textView.setText(snapshot.child("offer_status").getValue().toString());
                        offerAmount_textView.setText(snapshot.child("amount").getValue().toString());
                        offerMessage_textView.setText(snapshot.child("message").getValue().toString());
                        make_offer_btn.setText("CANCLE OFFER");

                    }else if (snapshot.child("offer_status").getValue().toString().equals("offer accepted")){
                        offer_relativeLayout.setVisibility(View.VISIBLE);
                        offer_relativeLayout.setBackgroundColor(getResources().getColor(R.color.textGreen));
                        offerAmountCurrency_textView.setText(Currency.getInstance().getCurrency());
                        offerStatus_textView.setText(snapshot.child("offer_status").getValue().toString());
                        offerAmount_textView.setText(snapshot.child("amount").getValue().toString());
                        offerMessage_textView.setText(snapshot.child("message").getValue().toString());
                        make_offer_btn.setText("CANCLE OFFER");

                    }else if (snapshot.child("offer_status").getValue().toString().equals("bought")){
                        offer_relativeLayout.setVisibility(View.VISIBLE);
                        offer_relativeLayout.setBackground(getResources().getDrawable(R.drawable.profile_screen_header));
                        offerAmountCurrency_textView.setText(Currency.getInstance().getCurrency());
                        offerStatus_textView.setText(snapshot.child("offer_status").getValue().toString());
                        offerAmount_textView.setText(snapshot.child("amount").getValue().toString());
                        offerMessage_textView.setText(snapshot.child("message").getValue().toString());
                        make_offer_btn.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchingItemDetails() {
        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noOfimages= Integer.parseInt(snapshot.child("No_of_images").getValue().toString());
                product_name_textview.setText(snapshot.child("Item_name").getValue().toString());
                category_textView.setText(snapshot.child("Category").getValue().toString());
                item_description_textview.setText(snapshot.child("Description").getValue().toString());
                itemPrice_textView.setText(snapshot.child("Price").getValue().toString());
                currency_textView.setText(currency);
                fetchingItemImages();
                fetchingShopkeeperLocation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchingImageUrl() {
        userRef = FirebaseDatabase.getInstance().getReference("Users_data/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profileImageUrl = snapshot.child("profile_image_url").getValue().toString();
                customerName = snapshot.child("fullname").getValue().toString();
                Toast.makeText(BuyLocal_productdetails.this, customerName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchingShopkeeperLocation() {
        userRef = FirebaseDatabase.getInstance().getReference("Users_data/" + shopkeeperID + "/Location");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Double shopkeeperlat = Double.valueOf(snapshot.child("lat").getValue().toString());
                Double shopkeeperlong = Double.valueOf(snapshot.child("long").getValue().toString());
                calculateDistance(shopkeeperlat,shopkeeperlong);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void calculateDistance(final Double shopkeeperlat, final Double shopkeeperong) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location!=null){
                        Geocoder geocoder = new Geocoder(BuyLocal_productdetails.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            Double totaldistance = distance(shopkeeperlat,shopkeeperong,addresses.get(0).getLatitude(),addresses.get(0).getLongitude())/1.609;
                            totaldistance = (double) Math.round(totaldistance * 100) / 100;

                            distance_textView.setText(String.valueOf(totaldistance)+ " Miles");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void fetchingItemImages() {


        for (i = 1;i<=noOfimages;++i){

            itemImageStorageRef.child("image_"+String.valueOf(i)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    imageUrl.add(String.valueOf(uri));


                    System.out.println("called fun i="+i+" "+imageUrl.size());
//
                    if (imageUrl.size()==noOfimages){
                        SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(BuyLocal_productdetails.this,imageUrl);
                        sliderView.setSliderAdapter(sliderAdapterExample);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BuyLocal_productdetails.this, "failed", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    /////////////////////////////////////////////////////////////////////////////////////

    private void midProcesses(){

    }

    private boolean offerSubmitValidation() {
        boolean valid = true;
        if (alertMakeOfferMessage_editText.getText().toString().isEmpty()){
            alertMakeOfferMessage_editText.setError("Please enter message");
            valid = false;
        }
        if (alertMakeOfferAmount_editText.getText().toString().isEmpty()){
            alertMakeOfferAmount_editText.setError("Please enter amount");
            valid = false;
        }
        return valid;
    }

}
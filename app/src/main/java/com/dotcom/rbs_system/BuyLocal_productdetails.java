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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BuyLocal_productdetails extends AppCompatActivity {

    TextView alertReportSubmit_textview, alertReportCancel_textview, alertMakeOfferSubmit_textview, alertMakeOfferCancel_textview;
    FusedLocationProviderClient fusedLocationProviderClient;
    RelativeLayout offer_relativeLayout;
    String currency, profileImageUrl, customerName;
    CardView imageSlider;
    SliderView sliderView;
    List<String> imageUrl;
    EditText alertReportDescription_editText, alertMakeOfferAmount_editText, alertMakeOfferMessage_editText;
    TextView distance_textView, shopKeeperName_textView;
    TextView product_name_textview, category_textView, item_description_textview, itemPrice_textView, currency_textView;
    TextView offerStatus_textView, offerAmountCurrency_textView, offerAmount_textView, offerMessage_textView;
    TextView make_offer_textView, communicate_textView;
    ImageButton share_imageButton, report_imageButton;
    Dialog report_alert_dialog;
    Dialog make_offer_alert_dialog;
    ImageButton back_btn;
    LinearLayout shop_details_linearlayout;
    String productID, productName, category, shopkeeperID, conversationKey = null;
    DatabaseReference itemRef, reportRef, userRef, stockRef, customerOfferRef, agreedOfferRef, boughtOfferRef, userConversationRef;
    StorageReference itemImageStorageRef;
    int i, noOfimages;
    boolean agreedBoughtCheck = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_productdetails);

        currency = Currency.getInstance().getCurrency();

        String[] arraySpinner = new String[]{
                "1", "2", "3", "4", "5", "6", "7"
        };


        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkData != null) {
            //     http://buyLocalrbs_test.com/productdetails/-MUSpP4dkoK4w4f45VaT/Mobile/CfofZGxuR4TC1OEBRMymclpscR73
            productID = appLinkData.getPathSegments().get(1);
            category = appLinkData.getPathSegments().get(2);
            shopkeeperID = appLinkData.getPathSegments().get(3);
        } else {
            productID = getIntent().getStringExtra("PRODUCT_ID");
            category = getIntent().getStringExtra("CATEGORY");
            shopkeeperID = getIntent().getStringExtra("SHOPKEEPER_ID");
        }

        imageUrl = new ArrayList<>();

        itemRef = FirebaseDatabase.getInstance().getReference("Items/" + category + "/" + productID);
        stockRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/" + shopkeeperID + "/" + category + "/" + productID);
        agreedOfferRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/" + shopkeeperID + "/" + category + "/" + productID + "/Accepted_Offer");
        boughtOfferRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/" + shopkeeperID + "/" + category + "/" + productID + "/Bought_Offer");
        reportRef = FirebaseDatabase.getInstance().getReference();
        customerOfferRef = FirebaseDatabase.getInstance().getReference();
        itemImageStorageRef = FirebaseStorage.getInstance().getReference().child("Item_Images/" + productID);
        userConversationRef = FirebaseDatabase.getInstance().getReference("User_conversation/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        offer_relativeLayout = findViewById(R.id.offer_relativeLayout);

        imageSlider = findViewById(R.id.imageSlider);
        back_btn = findViewById(R.id.back_btn);
        shop_details_linearlayout = findViewById(R.id.shop_details_linearlayout);
        share_imageButton = findViewById(R.id.share_imageButton);
        report_imageButton = findViewById(R.id.report_imageButton);
        distance_textView = findViewById(R.id.distance_textView);
        shopKeeperName_textView = findViewById(R.id.shopKeeperName_textView);

        product_name_textview = findViewById(R.id.product_name_textview);
        category_textView = findViewById(R.id.category_textView);
        item_description_textview = findViewById(R.id.item_description_textview);
        itemPrice_textView = findViewById(R.id.itemPrice_textView);
        currency_textView = findViewById(R.id.currency_textView);

        offerStatus_textView = findViewById(R.id.offerStatus_textView);
        offerAmountCurrency_textView = findViewById(R.id.offerAmountCurrency_textView);
        offerAmount_textView = findViewById(R.id.offerAmount_textView);
        offerMessage_textView = findViewById(R.id.offerMessage_textView);

        report_alert_dialog = new Dialog(this);
        report_alert_dialog.setContentView(R.layout.alert_report);
        make_offer_alert_dialog = new Dialog(this);
        make_offer_alert_dialog.setContentView(R.layout.alert_make_offer);

        alertReportCancel_textview = report_alert_dialog.findViewById(R.id.alertReportCancel_textview);
        alertReportDescription_editText = report_alert_dialog.findViewById(R.id.alertReportDescription_editText);
        alertReportSubmit_textview = report_alert_dialog.findViewById(R.id.alertReportSubmit_textview);

        ////Spinner code
        Spinner s = report_alert_dialog.findViewById(R.id.Spinner01);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.custom_spinner, arraySpinner);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        s.setAdapter(adapter);
        /////Spiner code
        alertMakeOfferCancel_textview = make_offer_alert_dialog.findViewById(R.id.alertMakeOfferCancel_textview);
        alertMakeOfferSubmit_textview = make_offer_alert_dialog.findViewById(R.id.alertMakeOfferSubmit_textview);
        alertMakeOfferAmount_editText = make_offer_alert_dialog.findViewById(R.id.alertMakeOfferAmount_editText);
        alertMakeOfferMessage_editText = make_offer_alert_dialog.findViewById(R.id.alertMakeOfferMessage_editText);
        make_offer_textView = findViewById(R.id.make_offer_textView);
        communicate_textView = findViewById(R.id.communicate_textView);

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
        alertReportSubmit_textview_listner();
        alertReportCancel_textview_listner();
        make_offer_btn_listner();
        alertMakeOfferCancel_textview_listner();
        alertMakeOfferSubmit_textview_listner();
        back_btn_listner();
        whatsapp_icon_listner();
        profileImage_listner();
        shop_details_linearlayout_listner();
        communicate_btn_listner();
    }

    private void shop_details_linearlayout_listner() {
        shop_details_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyLocal_productdetails.this, BuyLocal_shopkeeper_shop.class);
                //TODO is ma BuyLocal_shopkeeper_shop ki activity online karni ha
                //TODO aur distance wala bhi online karna ha
                startActivity(intent);
            }
        });
    }

    private void communicate_btn_listner() {
        communicate_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyLocal_productdetails.this, BuyLocal_messaging.class);
                intent.putExtra("ID", shopkeeperID);
                intent.putExtra("SHOPKEEPER_NAME", shopKeeperName_textView.getText().toString());
                intent.putExtra("PRODUCT_ID", productID);
                intent.putExtra("CATEGORY", category);
                intent.putExtra("CONVERSATION_KEY", conversationKey);
                intent.putExtra("PRODUCT_NAME", productName);
                intent.putExtra("PRODUCT_IMAGE", imageUrl.get(0));
                startActivity(intent);
            }
        });
    }

    private void alertReportSubmit_textview_listner() {
        alertReportSubmit_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!alertReportDescription_editText.getText().toString().trim().isEmpty()) {
                    reportRef.child("Customer_reports").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(productID).child("report_description").setValue(alertReportDescription_editText.getText().toString());
                    report_alert_dialog.dismiss();
                } else {
                    alertReportDescription_editText.setError("Enter Description!");
                }
            }
        });
    }

    private void profileImage_listner() {

    }

    private void whatsapp_icon_listner() {
        share_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "http://buyLocalrbstest.com/productdetails/" + productID + "/" + category + "/" + shopkeeperID;
                String shareSub = productName;
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

    private void alertMakeOfferCancel_textview_listner() {
        alertMakeOfferCancel_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make_offer_alert_dialog.dismiss();

            }
        });
    }

    private void alertMakeOfferSubmit_textview_listner() {
        alertMakeOfferSubmit_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (offerSubmitValidation()) {
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
                    customerOfferRef.child("Customer_offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(productID).child("offer_status").setValue("Offer Pending");
                    customerOfferRef.child("Customer_offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(productID).child("timestamp").setValue(timestamp);

                    make_offer_alert_dialog.dismiss();
                    recreate();
                }
            }
        });
    }

    private void make_offer_btn_listner() {

        make_offer_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (make_offer_textView.getText().toString().equals("Cancle Offer")) {
                    if (agreedBoughtCheck) {
                        agreedOfferRef.removeValue();
                    }
                    stockRef.child("Offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                    customerOfferRef.child("Customer_offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(productID).removeValue();
                    recreate();
                } else {
                    make_offer_alert_dialog.show();
                }
            }
        });


    }

    private void alertReportCancel_textview_listner() {
        alertReportCancel_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report_alert_dialog.dismiss();
            }
        });
    }

    private void report_btn_listner() {
        report_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO report ko online karna ha
                report_alert_dialog.show();
            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////

    private void InitialProcess() {
        checkingForExistingConversation();
        fetchingImageUrl();
        checkingOffer();
        fetchingItemDetails();

    }

    private void checkingForExistingConversation() {
        userConversationRef.child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    conversationKey = snapshot.child("conversation_id").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkingOffer() {
        customerOfferRef.child("Customer_offers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    switch (snapshot.child("offer_status").getValue().toString()) {
                        case "Offer Pending":
                            offer_relativeLayout.setVisibility(View.VISIBLE);
                            offer_relativeLayout.setBackground(getResources().getDrawable(R.drawable.screen_header_rectangle));
                            offerAmountCurrency_textView.setText(Currency.getInstance().getCurrency());
                            offerStatus_textView.setText(snapshot.child("offer_status").getValue().toString());
                            offerAmount_textView.setText(snapshot.child("amount").getValue().toString());
                            offerMessage_textView.setText(snapshot.child("message").getValue().toString());
                            make_offer_textView.setText("Cancle Offer");

                            break;
                        case "offer accepted":
                            offer_relativeLayout.setVisibility(View.VISIBLE);
                            offer_relativeLayout.setBackgroundColor(getResources().getColor(R.color.textGreen));
                            offerAmountCurrency_textView.setText(Currency.getInstance().getCurrency());
                            offerStatus_textView.setText(snapshot.child("offer_status").getValue().toString());
                            offerAmount_textView.setText(snapshot.child("amount").getValue().toString());
                            offerMessage_textView.setText(snapshot.child("message").getValue().toString());
                            make_offer_textView.setText("Cancle Offer");
                            agreedBoughtCheck = true;

                            break;
                        case "bought":
                            offer_relativeLayout.setVisibility(View.VISIBLE);
                            offer_relativeLayout.setBackground(getResources().getDrawable(R.drawable.screen_header_rectangle));
                            offerAmountCurrency_textView.setText(Currency.getInstance().getCurrency());
                            offerStatus_textView.setText(snapshot.child("offer_status").getValue().toString());
                            offerAmount_textView.setText(snapshot.child("amount").getValue().toString());
                            offerMessage_textView.setText(snapshot.child("message").getValue().toString());
                            make_offer_textView.setVisibility(View.GONE);
                            break;
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
                noOfimages = Integer.parseInt(snapshot.child("No_of_images").getValue().toString());
                product_name_textview.setText(snapshot.child("Item_name").getValue().toString());
                productName = snapshot.child("Item_name").getValue().toString();
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
                calculateDistance(shopkeeperlat, shopkeeperlong);
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
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(BuyLocal_productdetails.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            Double totaldistance = distance(shopkeeperlat, shopkeeperong, addresses.get(0).getLatitude(), addresses.get(0).getLongitude()) / 1.609;
                            totaldistance = (double) Math.round(totaldistance * 100) / 100;

                            distance_textView.setText(String.valueOf(totaldistance) + " Miles");

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


        for (i = 1; i <= noOfimages; ++i) {

            itemImageStorageRef.child("image_" + i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    imageUrl.add(String.valueOf(uri));


                    System.out.println("called fun i=" + i + " " + imageUrl.size());
//
                    if (imageUrl.size() == noOfimages) {
                        SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(BuyLocal_productdetails.this, imageUrl);
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

    private void midProcesses() {

    }

    private boolean offerSubmitValidation() {
        boolean valid = true;
        if (alertMakeOfferMessage_editText.getText().toString().trim().isEmpty()) {
            alertMakeOfferMessage_editText.setError("Please enter message");
            valid = false;
        }
        if (alertMakeOfferAmount_editText.getText().toString().trim().isEmpty()) {
            alertMakeOfferAmount_editText.setError("Please enter amount");
            valid = false;
        }
        return valid;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkingForExistingConversation();
    }
}
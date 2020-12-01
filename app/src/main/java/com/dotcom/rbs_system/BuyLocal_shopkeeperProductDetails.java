package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterProductsOffersReceivedListRecyclerView;
import com.dotcom.rbs_system.Adapter.SliderAdapterExample;
import com.dotcom.rbs_system.Classes.Currency;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BuyLocal_shopkeeperProductDetails extends AppCompatActivity {

    RecyclerView offers_received;
    CardView buy_local;

    List<String> customername;
    List<String> offered_price_list;
    List<String> product_offer_msg_list;
    List<String> profileImageUrl_list;
    List<String> date_list;
    List<String> customer_keyid;
    Long timestamp;

    TextView customer_name,offersTextView;
    TextView currency;
    TextView offered_price;
    TextView product_offer_msg;
    Button accept_offer_btn,cancel_offer_btn,sell_offer_btn;
    TextView date_textView;
    ImageView profileImage;

    TextView product_name_textview, category_textView, item_description_textview, itemPrice_textView, currency_textView;
    int i, noOfimages;
    DatabaseReference itemRef, offerlistRef,agreedOfferRef,boughtOfferRef,customerOfferStatusRef;
    String productID, category,date,activeCustomerID;
    StorageReference itemImageStorageRef;
    ArrayList imageUrl;
    SliderView sliderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_shopkeeper_product_details);
        productID = getIntent().getStringExtra("PRODUCT_ID");
        category = getIntent().getStringExtra("CATEGORY");
        
        offersTextView = (TextView)findViewById(R.id.offersTextView);
        customer_name = (TextView)findViewById(R.id.customer_name);
        currency = (TextView)findViewById(R.id.currency);
        offered_price = (TextView)findViewById(R.id.offered_price);
        accept_offer_btn = (Button)findViewById(R.id.accept_offer_btn);
        cancel_offer_btn = (Button)findViewById(R.id.cancel_offer_btn);
        sell_offer_btn = (Button)findViewById(R.id.sell_offer_btn);
        date_textView = (TextView)findViewById(R.id.date_textView);
        product_offer_msg = (TextView)findViewById(R.id.product_offer_msg);
        profileImage = (ImageView)findViewById(R.id.profileImage);

        itemRef = FirebaseDatabase.getInstance().getReference("Items/" + category + "/" + productID);
        agreedOfferRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+ category + "/" + productID+"/Accepted_Offer");
        boughtOfferRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+ category + "/" + productID+"/Bought_Offer");
        offerlistRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+ category + "/" + productID+"/Offers");
        itemImageStorageRef = FirebaseStorage.getInstance().getReference().child("Item_Images/" + productID);

        product_name_textview = (TextView) findViewById(R.id.product_name_textview);
        category_textView = (TextView) findViewById(R.id.category_textView);
        item_description_textview = (TextView) findViewById(R.id.item_description_textview);
        itemPrice_textView = (TextView) findViewById(R.id.itemPrice_textView);
        currency_textView = (TextView) findViewById(R.id.currency_textView);

        offers_received=(RecyclerView) findViewById(R.id.offers_received);

        buy_local=(CardView) findViewById(R.id.buy_local);

        customername = new ArrayList<String>();
        offered_price_list = new ArrayList<String>();
        product_offer_msg_list = new ArrayList<String>();
        profileImageUrl_list = new ArrayList<String>();
        date_list = new ArrayList<String>();
        customer_keyid = new ArrayList<String>();

        imageUrl = new ArrayList<>();

        sliderView = findViewById(R.id.imageSliders);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.parseColor("#01A0DA"));
        sliderView.setIndicatorUnselectedColor(Color.parseColor("#F1F1F1"));
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();


        InitialProcess();
        onClickListeners();
    }


    /////////////////////////////////////////////////////////////////////////////////
    private void InitialProcess() {
        fetchingItemDetails();
        checkingforAgreedOffers();

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
                currency_textView.setText(Currency.getInstance().getCurrency());
                fetchingItemImages();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkingforAgreedOffers() {
        agreedOfferRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                        buy_local.setVisibility(View.VISIBLE);
                        offersTextView.setText("Accepted Offer");

                        offerlistRef.child(snapshot.getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                customer_name.setText(snapshot.child("customer_name").getValue().toString());
                                currency = (TextView)findViewById(R.id.currency);
                                offered_price.setText(snapshot.child("amount").getValue().toString());
                                product_offer_msg.setText(snapshot.child("message").getValue().toString());
                                activeCustomerID = snapshot.child("customer").getValue().toString();
                                Picasso.get().load(snapshot.child("customer_image").getValue().toString()).into(profileImage);
                                timestamp = Long.valueOf(snapshot.child("timestamp").getValue().toString());

                                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                                cal.setTimeInMillis(timestamp);
                                date = DateFormat.format("dd MMMM yyyy", cal).toString();
                                date_textView.setText(date);

                                customerOfferStatusRef = FirebaseDatabase.getInstance().getReference("Customer_offers/"+ snapshot.child("customer").getValue().toString()+"/"+ productID+"/offer_status");

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(BuyLocal_shopkeeperProductDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                }else {
                    boughtOfferRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                buy_local.setVisibility(View.VISIBLE);
                                offersTextView.setText("Sold to");
                                cancel_offer_btn.setVisibility(View.GONE);
                                sell_offer_btn.setVisibility(View.GONE);

                                offerlistRef.child(snapshot.getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        customer_name.setText(snapshot.child("customer_name").getValue().toString());
                                        currency = (TextView)findViewById(R.id.currency);
                                        offered_price.setText(snapshot.child("amount").getValue().toString());
                                        product_offer_msg.setText(snapshot.child("message").getValue().toString());
                                        activeCustomerID = snapshot.child("customer").getValue().toString();
                                        Picasso.get().load(snapshot.child("customer_image").getValue().toString()).into(profileImage);
                                        timestamp = Long.valueOf(snapshot.child("timestamp").getValue().toString());

                                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                                        cal.setTimeInMillis(timestamp);
                                        date = DateFormat.format("dd MMMM yyyy", cal).toString();
                                        date_textView.setText(date);

                                        customerOfferStatusRef = FirebaseDatabase.getInstance().getReference("Customer_offers/"+ snapshot.child("customer").getValue().toString()+"/"+ productID+"/offer_status");

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(BuyLocal_shopkeeperProductDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else {
                                fetchingOffers();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchingOffers() {
        offerlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    customername.add(snapshot1.child("customer_name").getValue().toString());
                    offered_price_list.add(snapshot1.child("amount").getValue().toString());
                    product_offer_msg_list.add(snapshot1.child("message").getValue().toString());
                    customer_keyid.add(snapshot1.child("customer").getValue().toString());
                    profileImageUrl_list.add(snapshot1.child("customer_image").getValue().toString());
                    timestamp = Long.valueOf(snapshot1.child("timestamp").getValue().toString());

                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                    cal.setTimeInMillis(timestamp);
                    date = DateFormat.format("dd MMMM yyyy", cal).toString();

                    date_list.add(date);

                }
                AdapterProductsOffersReceivedListRecyclerView adapterProductsOffersReceivedListRecyclerView = new AdapterProductsOffersReceivedListRecyclerView(BuyLocal_shopkeeperProductDetails.this,category, productID, customer_keyid, customername, product_offer_msg_list, offered_price_list, profileImageUrl_list,date_list);
                offers_received.setLayoutManager(new GridLayoutManager(BuyLocal_shopkeeperProductDetails.this,1));
                offers_received.setAdapter(adapterProductsOffersReceivedListRecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BuyLocal_shopkeeperProductDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                        SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(BuyLocal_shopkeeperProductDetails.this,imageUrl);
                        sliderView.setSliderAdapter(sliderAdapterExample);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BuyLocal_shopkeeperProductDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    /////////////////////////////////////////////////////////////////////////////////

    private void onClickListeners() {
        cancel_offer_btnOnclick();
        sell_offer_btnOnclick();

    }

    private void cancel_offer_btnOnclick() {
        cancel_offer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agreedOfferRef.removeValue();
                customerOfferStatusRef.setValue("offer pending");
                recreate();
            }
        });
    }

    private void sell_offer_btnOnclick() {
        sell_offer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agreedOfferRef.removeValue();
                boughtOfferRef.setValue(activeCustomerID);
                customerOfferStatusRef.setValue("bought");
                recreate();
            }
        });
    }


}
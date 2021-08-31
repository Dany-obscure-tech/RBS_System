package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterItemDetailsImagesRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterProductsOffersReceivedListRecyclerView;
import com.dotcom.rbs_system.Adapter.SliderAdapterExample;
import com.dotcom.rbs_system.Classes.Currency;
import com.github.dhaval2404.imagepicker.ImagePicker;
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
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RBS_Shopkeeper_Inventory_Details extends AppCompatActivity {

    Dialog editItem_alert;
    StorageReference idStorageReference;
    DatabaseReference reference;
    String key, itemCategory;
    int k = 0, l = 0;

    RecyclerView offers_received;
    RecyclerView itemImage_recyclerView;
    AdapterItemDetailsImagesRecyclerView adapterItemDetailsImagesRecyclerView;

    Uri fileUri;
    Uri firstImageUri;

    CardView buy_local;

    Intent intent;

    List<String> customername;
    List<String> offered_price_list;
    List<String> product_offer_msg_list;
    List<String> profileImageUrl_list;
    List<String> date_list;
    List<String> customer_keyid;
    Long timestamp;

    List<Uri> imageUrlList;

    TextView customer_name, offersTextView;
    TextView currency;
    TextView edit_textView;
    TextView offered_price, cancel_offer_textview;
    TextView product_offer_msg, accept_offer_textview, sell_offer_textview;
    TextView date_textView;
    TextView item_personal_notes_textview;
    TextView yes_btn_textview, cancel_btn_textview;
    EditText description_editText;
    TextView condition_textView;
    TextView uploadId_textView;

    ImageView profileImage;
    ImageView id_imageView;

    ImageButton back_btn;

    Spinner spinner;

    TextView product_name_textview, category_textView, item_description_textview, itemPrice_textView, currency_textView;
    int i, noOfimages;
    DatabaseReference itemRef, stockItemRef, offerlistRef, agreedOfferRef, boughtOfferRef, customerOfferStatusRef;
    String productID, category, date, activeCustomerID;
    StorageReference itemImageStorageRef;
    List imageUrl;
    SliderView sliderView;

    String itemname_returnString, itemid_returnString, itemcategory_returnString, itemkeyid_returnString, itemprice_returnString, itemlstactive_returnString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rbs_shopkeeper_inventory_details);

        idStorageReference = FirebaseStorage.getInstance().getReference().child("Item_Images");
        reference = FirebaseDatabase.getInstance().getReference();

        imageUrlList = new ArrayList<>();

        editItem_alert = new Dialog(this);
        editItem_alert.setContentView(R.layout.edit_item_alert);
        itemImage_recyclerView = editItem_alert.findViewById(R.id.itemImage_recyclerView);
        spinner = editItem_alert.findViewById(R.id.spinner);
        id_imageView = editItem_alert.findViewById(R.id.id_imageView);
        itemImage_recyclerView = editItem_alert.findViewById(R.id.itemImage_recyclerView);
        itemImage_recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapterItemDetailsImagesRecyclerView = new AdapterItemDetailsImagesRecyclerView(RBS_Shopkeeper_Inventory_Details.this, imageUrlList);
        itemImage_recyclerView.setAdapter(adapterItemDetailsImagesRecyclerView);

        uploadId_textView = editItem_alert.findViewById(R.id.uploadId_textView);
        yes_btn_textview = editItem_alert.findViewById(R.id.yes_btn_textview);
        cancel_btn_textview = editItem_alert.findViewById(R.id.cancel_btn_textview);
        description_editText = editItem_alert.findViewById(R.id.description_editText);

        productID = getIntent().getStringExtra("PRODUCT_ID");
        category = getIntent().getStringExtra("CATEGORY");

        edit_textView = findViewById(R.id.edit_textView);

        offersTextView = findViewById(R.id.offersTextView);
        customer_name = findViewById(R.id.customer_name);
        currency = findViewById(R.id.currency);
        offered_price = findViewById(R.id.offered_price);
        accept_offer_textview = findViewById(R.id.accept_offer_textview);
        cancel_offer_textview = findViewById(R.id.cancel_offer_textview);
        sell_offer_textview = findViewById(R.id.sell_offer_textview);
        date_textView = findViewById(R.id.date_textView);
        item_personal_notes_textview = findViewById(R.id.item_personal_notes_textview);
        condition_textView = findViewById(R.id.condition_textView);
        product_offer_msg = findViewById(R.id.product_offer_msg);
        profileImage = findViewById(R.id.profileImage_imageView);
        back_btn = findViewById(R.id.back_btn);

        itemRef = FirebaseDatabase.getInstance().getReference("Items/" + category + "/" + productID);
        stockItemRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers" + FirebaseAuth.getInstance().getCurrentUser().getUid() + category + "/" + productID);
        agreedOfferRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + category + "/" + productID + "/Accepted_Offer");
        boughtOfferRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + category + "/" + productID + "/Bought_Offer");
        offerlistRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + category + "/" + productID + "/Offers");
        itemImageStorageRef = FirebaseStorage.getInstance().getReference().child("Item_Images/" + productID);

        product_name_textview = findViewById(R.id.product_name_textview);
        category_textView = findViewById(R.id.category_textView);
        item_description_textview = findViewById(R.id.item_description_textview);
        itemPrice_textView = findViewById(R.id.itemPrice_textView);
        currency_textView = findViewById(R.id.currency_textView);

        offers_received = findViewById(R.id.offers_received);

        buy_local = findViewById(R.id.buy_local);

        customername = new ArrayList<>();
        offered_price_list = new ArrayList<>();
        product_offer_msg_list = new ArrayList<>();
        profileImageUrl_list = new ArrayList<>();
        date_list = new ArrayList<>();
        customer_keyid = new ArrayList<>();


        imageUrl = new ArrayList<>();

        sliderView = findViewById(R.id.imageSliders);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.parseColor("#01A0DA"));
        sliderView.setIndicatorUnselectedColor(Color.parseColor("#F1F1F1"));
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.custom_spinner,
                getResources().getStringArray(R.array.list)
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner.setAdapter(adapter);


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
                noOfimages = Integer.parseInt(snapshot.child("No_of_images").getValue().toString());
                product_name_textview.setText(snapshot.child("Item_name").getValue().toString());
                category_textView.setText(snapshot.child("Category").getValue().toString());
                item_description_textview.setText(snapshot.child("Description").getValue().toString());
                description_editText.setText(snapshot.child("Description").getValue().toString());
                itemPrice_textView.setText(snapshot.child("Price").getValue().toString());
                item_personal_notes_textview.setText(snapshot.child("Notes").getValue().toString());
                condition_textView.setText(snapshot.child("Condition").getValue().toString());
                currency_textView.setText(Currency.getInstance().getCurrency());

                itemname_returnString = snapshot.child("Item_name").getValue().toString();
                itemid_returnString = snapshot.child("Item_id").getValue().toString();
                itemcategory_returnString = snapshot.child("Category").getValue().toString();
                itemCategory = itemcategory_returnString;
                itemkeyid_returnString = snapshot.child("key_id").getValue().toString();
                key = itemkeyid_returnString;
                itemprice_returnString = snapshot.child("Price").getValue().toString();

                if (snapshot.child("Last_active").exists()) {
                    itemlstactive_returnString = snapshot.child("Last_active").getValue().toString();
                } else {
                    itemlstactive_returnString = "NA";
                }

                for (DataSnapshot dataSnapshot : snapshot.child("Image_urls").getChildren()) {
                    imageUrl.add(dataSnapshot.getValue().toString());
                }

                SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(RBS_Shopkeeper_Inventory_Details.this, imageUrl);
                sliderView.setSliderAdapter(sliderAdapterExample);

                intent = new Intent(RBS_Shopkeeper_Inventory_Details.this, Sale.class);
                intent.putExtra("ITEM_SELL_CHECK", "TRUE");

                intent.putExtra("Item_name", itemname_returnString);
                intent.putExtra("Item_id", itemid_returnString);
                intent.putExtra("Item_category", itemcategory_returnString);
                intent.putExtra("Item_keyid", itemkeyid_returnString);
                intent.putExtra("Item_price", itemprice_returnString);
                intent.putExtra("Last_Active", itemlstactive_returnString);
                intent.putExtra("Item_image", imageUrl.get(0).toString());

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
                if (snapshot.exists()) {
                    buy_local.setVisibility(View.VISIBLE);
                    offersTextView.setText("Accepted Offer");

                    offerlistRef.child(snapshot.getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            customer_name.setText(snapshot.child("customer_name").getValue().toString());
                            currency = findViewById(R.id.currency);
                            offered_price.setText(snapshot.child("amount").getValue().toString());
                            product_offer_msg.setText(snapshot.child("message").getValue().toString());
                            activeCustomerID = snapshot.child("customer").getValue().toString();
                            Picasso.get().load(snapshot.child("customer_image").getValue().toString()).into(profileImage);
                            timestamp = Long.valueOf(snapshot.child("timestamp").getValue().toString());

                            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                            cal.setTimeInMillis(timestamp);
                            date = DateFormat.format("dd MMMM yyyy", cal).toString();
                            date_textView.setText(date);

                            customerOfferStatusRef = FirebaseDatabase.getInstance().getReference("Customer_offers/" + snapshot.child("customer").getValue().toString() + "/" + productID + "/offer_status");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(RBS_Shopkeeper_Inventory_Details.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    boughtOfferRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                buy_local.setVisibility(View.VISIBLE);
                                offersTextView.setText("Sold to");
                                cancel_offer_textview.setVisibility(View.GONE);
                                sell_offer_textview.setVisibility(View.GONE);

                                offerlistRef.child(snapshot.getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        customer_name.setText(snapshot.child("customer_name").getValue().toString());
                                        currency = findViewById(R.id.currency);
                                        offered_price.setText(snapshot.child("amount").getValue().toString());
                                        product_offer_msg.setText(snapshot.child("message").getValue().toString());
                                        activeCustomerID = snapshot.child("customer").getValue().toString();
                                        Picasso.get().load(snapshot.child("customer_image").getValue().toString()).into(profileImage);
                                        timestamp = Long.valueOf(snapshot.child("timestamp").getValue().toString());

                                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                                        cal.setTimeInMillis(timestamp);
                                        date = DateFormat.format("dd MMMM yyyy", cal).toString();
                                        date_textView.setText(date);

                                        customerOfferStatusRef = FirebaseDatabase.getInstance().getReference("Customer_offers/" + snapshot.child("customer").getValue().toString() + "/" + productID + "/offer_status");

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(RBS_Shopkeeper_Inventory_Details.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
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
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
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
                AdapterProductsOffersReceivedListRecyclerView adapterProductsOffersReceivedListRecyclerView = new AdapterProductsOffersReceivedListRecyclerView(RBS_Shopkeeper_Inventory_Details.this, category, productID, customer_keyid, customername, product_offer_msg_list, offered_price_list, profileImageUrl_list, date_list);
                offers_received.setLayoutManager(new GridLayoutManager(RBS_Shopkeeper_Inventory_Details.this, 1));
                offers_received.setAdapter(adapterProductsOffersReceivedListRecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RBS_Shopkeeper_Inventory_Details.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /////////////////////////////////////////////////////////////////////////////////
    private void onClickListeners() {
        cancel_offer_textviewOnclick();
        sell_offer_textviewOnclick();
        back_btn_listner();
        edit_textView_listener();
        yes_btn_textview_listener();
        cancel_btn_textview_listener();
        uploadId_textView_listener();

    }

    private void cancel_btn_textview_listener() {
        cancel_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editItem_alert.dismiss();
            }
        });
    }

    private void uploadId_textView_listener() {
        uploadId_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUrlList.size() < 5) {
                    ImagePicker.Companion.with(RBS_Shopkeeper_Inventory_Details.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start();
                } else {
                    Toast.makeText(RBS_Shopkeeper_Inventory_Details.this, "Maximum 5 images allowed!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void yes_btn_textview_listener() {
        yes_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateAlertvalues()) {
                    recreate();
                    editItem_alert.dismiss();
                    Toast.makeText(RBS_Shopkeeper_Inventory_Details.this, "Edit Completed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private boolean validateAlertvalues() {
        boolean valid = true;

        if (!spinner.getSelectedItem().toString().equals("Please select item condition") && !spinner.getSelectedItem().toString().equals(condition_textView.getText().toString())) {
            itemRef.child("Condition").setValue(spinner.getSelectedItem().toString());
        }
        if (!description_editText.getText().toString().isEmpty() && !description_editText.getText().toString().equals(item_description_textview.getText().toString())) {
            itemRef.child("Description").setValue(description_editText.getText().toString());
        } else {
            description_editText.setError("Enter description");
            valid = false;
        }
        if (imageUrlList.size() != 0) {
            int deletedImageNumber = 5 - imageUrlList.size();
            for (i = 1; i <= 5; i++) {
                if (deletedImageNumber != 0) {
                    System.out.println(deletedImageNumber + " - " + (imageUrlList.size() + 1));
                    idStorageReference.child(key).child("image_" + (imageUrlList.size() + i)).delete();
                    reference.child("Items").child(itemCategory).child(key).child("Image_urls").child("image_" + (imageUrlList.size() + i)).removeValue();
                    deletedImageNumber--;
                }
            }
            for (i = 0; i < imageUrlList.size(); i++) {

                idStorageReference.child(key).child("image_" + String.valueOf(i + 1)).putFile(imageUrlList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        idStorageReference.child(key).child("image_" + (l + 1)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                if (k == 0) {
                                    firstImageUri = uri;

//                                if (check=="Add new item"){
//
//                                    spotLightRef = FirebaseDatabase.getInstance().getReference("Spotlight");
//
//                                    spotLightRef.child(key).child("key_id").setValue(key);
//                                    spotLightRef.child(key).child("Category").setValue(itemCategory);
//                                    spotLightRef.child(key).child("Item_name").setValue(itemName);
//                                    spotLightRef.child(key).child("Price").setValue(itemPrice);
//                                    spotLightRef.child(key).child("id_image_url").setValue(uri.toString());
//                                    spotLightRef.child(key).child("shopkeeper").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                }

                                }
                                reference.child("Items").child(itemCategory).child(key).child("Image_urls").child("image_" + (k + 1)).setValue(String.valueOf(uri.toString()));
                                k++;

//                                        if (k==imageUrlList.size()){
//                                            if (check=="Sale new item") {
//                                                rbsItemDetails.switchStockSale(rbsCustomerDetails.getKey(), FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//                                            }
//                                            if (check=="Buy new item"){
//                                                uploadToStock();
//                                                uploadToRbsInvoiceList(rbsCustomerDetails.getKey(),addedBy);
//                                                updateStockOwner(addedBy);
//
//                                            }
//                                            if (check=="Add new item"){
//                                                uploadToStock();
//
//                                            }
//
//                                        }
                            }
                        });
                        l++;

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RBS_Shopkeeper_Inventory_Details.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        return valid;
    }

    private void edit_textView_listener() {
        edit_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editItem_alert.show();
            }
        });
    }

    private void back_btn_listner() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void cancel_offer_textviewOnclick() {
        cancel_offer_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agreedOfferRef.removeValue();
                customerOfferStatusRef.setValue("offer pending");
                recreate();
            }
        });
    }

    private void sell_offer_textviewOnclick() {
        sell_offer_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                agreedOfferRef.removeValue();
//                boughtOfferRef.setValue(activeCustomerID);
//                customerOfferStatusRef.setValue("bought");
//                recreate();
                startActivity(intent);
            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            fileUri = data.getData();
            id_imageView.setImageURI(fileUri);
            fileUri = data.getData();
            imageUrlList.add(fileUri);

            adapterItemDetailsImagesRecyclerView.notifyDataSetChanged();


            //You can get File object from intent
//            val file:File = ImagePicker.getFile(data)!!

            //You can also get File Path from intent
//                    val filePath:String = ImagePicker.getFilePath(data)!!
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

}
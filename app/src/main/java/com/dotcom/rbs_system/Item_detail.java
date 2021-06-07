package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterItemCategoryListRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterItemDetailsImagesRecyclerView;
import com.dotcom.rbs_system.Classes.RBSItemDetails;
import com.dotcom.rbs_system.Model.SampleSearchModel;
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

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Item_detail extends AppCompatActivity {

    RBSItemDetails rbsItemDetails;

    AdapterItemCategoryListRecyclerView adapterItemCategoryListRecyclerView;

    RecyclerView categoryList_recyclerView;
    Dialog categoryList_alert_dialog;

    // Progress dialog
    Progress_dialoge pd;

    LinearLayout selectCategory_linearLayout;

    AdapterItemDetailsImagesRecyclerView adapterItemDetailsImagesRecyclerView;
    RecyclerView itemImage_recyclerView;

    private static final int CAMERA_REQUEST_CODE = 1;
    TextView submit_textView,rating_textView;
    TextView selectCategory_textView,uploadId_textView;

    DatabaseReference categoryRef,reference;

    ImageView id_imageView, categoryIcon_imageView;

    EditText itemName_editText,notes_editText,itemId_editText,price_editText,description_editText;
    EditText search_editText;
    RatingBar ratingBar;
    ImageButton Back_btn;
    Uri fileUri;
    List<String> categoryList,filteredCategoryList,categoryImageList,filteredCategoryImageList,imageDownloadUrlList;
    List<Uri> imageUrlList;
    List<UploadTask.TaskSnapshot> taskSnapshotList;
    StorageReference idStorageReference;
    StorageReference storageReference;

    String searchMe;
    String passbackItemImageUrl;

    int i,j,k=0,l=0;

    private ArrayList<SampleSearchModel> createCategoryData(){

        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i=0;i<categoryList.size();i++){
            items.add(new SampleSearchModel(categoryList.get(i),null,null,null,null,null,null,null));
        }

        return items;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        initialize();
        getCategoryList();
        selectCategory();
        onClickListeners();
        getRating();

    }

    private void initialize() {
        rbsItemDetails = RBSItemDetails.getInstance();

        pd = new Progress_dialoge();

        imageUrlList = new ArrayList<>();
        rbsItemDetails.setImageUrlList(imageUrlList);
        imageDownloadUrlList = new ArrayList<>();
        taskSnapshotList = new ArrayList<>();

        selectCategory_linearLayout = (LinearLayout) findViewById(R.id.selectCategory_linearLayout);

        itemImage_recyclerView = (RecyclerView) findViewById(R.id.itemImage_recyclerView);
        itemImage_recyclerView.setLayoutManager(new GridLayoutManager(this,5));
        adapterItemDetailsImagesRecyclerView = new AdapterItemDetailsImagesRecyclerView(Item_detail.this,imageUrlList);
        itemImage_recyclerView.setAdapter(adapterItemDetailsImagesRecyclerView);

        storageReference = FirebaseStorage.getInstance().getReference();
        Back_btn = (ImageButton)findViewById(R.id.back_btn);
        itemName_editText = (EditText)findViewById(R.id.itemName_editText);
        itemId_editText = (EditText)findViewById(R.id.itemId_editText);
        price_editText = (EditText)findViewById(R.id.price_editText);
        description_editText = (EditText)findViewById(R.id.description_editText);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        notes_editText = (EditText)findViewById(R.id.notes_editText);
        categoryList = new ArrayList<>();
        categoryImageList = new ArrayList<>();
        filteredCategoryList = new ArrayList<>();
        filteredCategoryImageList = new ArrayList<>();
        categoryRef = FirebaseDatabase.getInstance().getReference("Categories");
        reference = FirebaseDatabase.getInstance().getReference();
        submit_textView = (TextView)findViewById(R.id.submit_textView);
        rating_textView = (TextView)findViewById(R.id.rating_textView);
        uploadId_textView = (TextView) findViewById(R.id.uploadId_textView);
        id_imageView = (ImageView) findViewById(R.id.id_imageView);
        categoryIcon_imageView = (ImageView) findViewById(R.id.categoryIcon_imageView);
        selectCategory_textView = (TextView) findViewById(R.id.selectCategory_textView);
        idStorageReference = storageReference.child("Item_Images");

        categoryList_alert_dialog = new Dialog(this);
        categoryList_alert_dialog.setContentView(R.layout.alert_rbs_categorylist);

        categoryList_recyclerView = (RecyclerView)categoryList_alert_dialog.findViewById(R.id.categoryList_recyclerView);
        search_editText = (EditText) categoryList_alert_dialog.findViewById(R.id.search_editText);
        categoryList_recyclerView.setLayoutManager(new GridLayoutManager(Item_detail.this,1));

    }

    private void getCategoryList() {
        pd.showProgressBar(Item_detail.this);

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        categoryList.add(String.valueOf(dataSnapshot1.child("name").getValue()));
                        filteredCategoryList.add(String.valueOf(dataSnapshot1.child("name").getValue()));
                        categoryImageList.add(String.valueOf(dataSnapshot1.child("image_url").getValue()));
                        filteredCategoryImageList.add(String.valueOf(dataSnapshot1.child("image_url").getValue()));
                    }
                    adapterItemCategoryListRecyclerView = new AdapterItemCategoryListRecyclerView(Item_detail.this, categoryList_alert_dialog,selectCategory_textView,categoryIcon_imageView,filteredCategoryList,filteredCategoryImageList);
                    categoryList_recyclerView.setAdapter(adapterItemCategoryListRecyclerView);

                    searchCategory();
                    pd.dismissProgressBar(Item_detail.this);
                }else {
                    pd.dismissProgressBar(Item_detail.this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismissProgressBar(Item_detail.this);
            }
        });
    }

    private void selectCategory() {
        selectCategory_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                new SimpleSearchDialogCompat(Item_detail.this, "Search...",
//                        "What are you looking for...?", null, createCategoryData(),
//                        new SearchResultListener<SampleSearchModel>() {
//                            @Override
//                            public void onSelected(BaseSearchDialogCompat dialog,
//                                                   SampleSearchModel item, int position) {
//                                selectCategory_textView.setText(item.getTitle());
//                                selectCategory_textView.setTextColor(getResources().getColor(R.color.colorPrimary));
//
//                                categoryIcon_imageView.setVisibility(View.VISIBLE);
//
//                                dialog.dismiss();
//                            }
//                        }).show();
//                // hello

                categoryList_alert_dialog.show();

            }
        });
    }

    private void detailsSubmit() {

        pd.showProgressBar(Item_detail.this);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Item_detail.this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            rbsItemDetails.setItemCategory(selectCategory_textView.getText().toString());
            rbsItemDetails.setItemID(itemId_editText.getText().toString());
            rbsItemDetails.setAddedBy(FirebaseAuth.getInstance().getCurrentUser().getUid());
            rbsItemDetails.setItemName(itemName_editText.getText().toString());
            rbsItemDetails.setItemCondition(rating_textView.getText().toString());
            rbsItemDetails.setPersonalNotes(notes_editText.getText().toString());
            rbsItemDetails.setItemPrice(price_editText.getText().toString());
            rbsItemDetails.setItemDescription(description_editText.getText().toString());
            rbsItemDetails.setNoOfImages(String.valueOf(imageUrlList.size()));

            if (getIntent().getStringExtra("ADD_ITEM").equals("TRUE")){
                rbsItemDetails.setCheck("Add new item");
                rbsItemDetails.setActivity(Item_detail.this);
                rbsItemDetails.uploadNewItemDetails(Item_detail.this);
//                rbsItemDetails.clearData();
                pd.dismissProgressBar(Item_detail.this);

                System.out.println("called");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","1");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }else {
                pass_back_data();
                pd.dismissProgressBar(Item_detail.this);
                finish();
            }





        }else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
        }

    }

    private void pass_back_data() {

      //   get the text from the EditText
        String last_active = "NA";
        // put the String to pass back into an Intent and close this activity
        Intent intent = new Intent();
        intent.putExtra("Item_name", itemName_editText.getText().toString());
        intent.putExtra("Item_id", itemId_editText.getText().toString());
        intent.putExtra("Item_category", selectCategory_textView.getText().toString());
        intent.putExtra("Item_price", price_editText.getText().toString());
        intent.putExtra("Last_Active", last_active);
        setResult(RESULT_OK, intent);
    }

    private void onClickListeners() {
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        uploadId_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUrlList.size()<5){
                    ImagePicker.Companion.with(Item_detail.this)
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)			//Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start();
                }else {
                    Toast.makeText(Item_detail.this, "Maximum 5 images allowed!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        submit_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields() == true)
                detailsSubmit();
            }
        });
    }

    private boolean validateFields() {
        boolean valid = true;

        if (selectCategory_textView.getText().toString().equals("Select Category")) {
            Toast.makeText(this, "Please select item category", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (itemName_editText.getText().toString().isEmpty()) {
            itemName_editText.setError("Please enter item name");
            valid = false;
        }
        if (description_editText.getText().toString().isEmpty()) {
            description_editText.setError("Please enter item description");
            valid = false;
        }
        if (imageUrlList.size()==0){
            Toast.makeText(this, "Please upload an item image", Toast.LENGTH_LONG).show();
            valid=false;
        }
        if (itemId_editText.getText().toString().isEmpty()) {
            itemId_editText.setError("Please enter item id");
            valid = false;
        }
        if (notes_editText.getText().toString().isEmpty()) {
            notes_editText.setError("Please enter notes");
            valid = false;
        }
        if (price_editText.getText().toString().isEmpty()) {
            price_editText.setError("Please enter price");
            valid = false;
        }
        if (ratingBar.getRating()==0){
            Toast.makeText(this, "Please select item condition", Toast.LENGTH_SHORT).show();
            valid=false;
        }


        return valid;
    }

    private void getRating() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (ratingBar.getRating()==0){
                    rating_textView.setText("NA");
                }else if (ratingBar.getRating()==1){
                    rating_textView.setText("D");
                }else if (ratingBar.getRating()==2){
                    rating_textView.setText("C");
                }else if (ratingBar.getRating()==3){
                    rating_textView.setText("B");
                }else if (ratingBar.getRating()==4){
                    rating_textView.setText("A");
                }else if (ratingBar.getRating()==5){
                    rating_textView.setText("A+");
                }
            }
        });
    }

    private void searchCategory() {
        search_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterList();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void filterList() {
        filteredCategoryList.clear();
        filteredCategoryImageList.clear();
        System.out.println(categoryList.size());
        for (int j = 0; j<categoryList.size();j++){

            searchMe = categoryList.get(j);

            int searchMeLength = searchMe.length();
            int findMeLength = search_editText.getText().toString().length();
                    boolean foundIt = false;
            for (int i = 0;
                 i <= (searchMeLength - findMeLength);
                 i++) {
                if (searchMe.regionMatches(true,i, search_editText.getText().toString(), 0, findMeLength)) {
                    filteredCategoryList.add(searchMe);
                    filteredCategoryImageList.add(categoryImageList.get(j));
                            foundIt = true;
                            System.out.println(searchMe);
                    break;
                }
            }
                    if (!foundIt)
                        System.out.println("No match found.");
        }
        adapterItemCategoryListRecyclerView.notifyDataSetChanged();

    }

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

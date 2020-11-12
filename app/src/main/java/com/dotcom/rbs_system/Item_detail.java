package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterItemDetailsImagesRecyclerView;
import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Item_detail extends AppCompatActivity {

    // Progress dialog
    Progress_dialoge pd;

    AdapterItemDetailsImagesRecyclerView adapterItemDetailsImagesRecyclerView;
    RecyclerView itemImage_recyclerView;

    private static final int CAMERA_REQUEST_CODE = 1;
    Button submit_btn,uploadId_btn;
    TextView selectCategory_textView;
    DatabaseReference categoryRef,reference;
    ImageView id_imageView;
    EditText itemName_editText,notes_editText,itemId_editText,price_editText,description_editText;
    List<String> categoryList;
    RatingBar ratingBar;
    ImageButton Back_btn;
    Uri fileUri;
    List<Uri> imageUrlList;
    StorageReference idStorageReference;
    StorageReference storageReference;

    String key;
    String key2;

    int i,j,k=0;

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
    }

    private void initialize() {
        pd = new Progress_dialoge();

        imageUrlList = new ArrayList<>();

        itemImage_recyclerView = (RecyclerView) findViewById(R.id.itemImage_recyclerView);
        itemImage_recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        adapterItemDetailsImagesRecyclerView = new AdapterItemDetailsImagesRecyclerView(Item_detail.this,imageUrlList);
        itemImage_recyclerView.setAdapter(adapterItemDetailsImagesRecyclerView);

        storageReference = FirebaseStorage.getInstance().getReference();
        Back_btn = (ImageButton)findViewById(R.id.Back_btn);
        itemName_editText = (EditText)findViewById(R.id.itemName_editText);
        itemId_editText = (EditText)findViewById(R.id.itemId_editText);
        price_editText = (EditText)findViewById(R.id.price_editText);
        description_editText = (EditText)findViewById(R.id.description_editText);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        notes_editText = (EditText)findViewById(R.id.notes_editText);
        categoryList = new ArrayList<>();
        categoryRef = FirebaseDatabase.getInstance().getReference("Categories");
        reference = FirebaseDatabase.getInstance().getReference();
        submit_btn = (Button)findViewById(R.id.submit_btn);
        uploadId_btn = (Button) findViewById(R.id.uploadId_btn);
        id_imageView = (ImageView) findViewById(R.id.id_imageView);
        selectCategory_textView = (TextView) findViewById(R.id.selectCategory_textView);
        idStorageReference = storageReference.child("Item_Images");

    }

    private void getCategoryList() {
        pd.showProgressBar(Item_detail.this);

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    categoryList.add(String.valueOf(dataSnapshot1.getValue()));
                }
                pd.dismissProgressBar(Item_detail.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismissProgressBar(Item_detail.this);
            }
        });
    }

    private void selectCategory() {
        selectCategory_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Item_detail.this, "Search...",
                        "What are you looking for...?", null, createCategoryData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                selectCategory_textView.setText(item.getTitle());
                                selectCategory_textView.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                                selectCategory_textView.setTextColor(getResources().getColor(R.color.textGrey));

                                dialog.dismiss();
                            }
                        }).show();
                // hello

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
            key = reference.push().getKey();
            reference.child("Items").child(selectCategory_textView.getText().toString()).child(key).child("Category").setValue(selectCategory_textView.getText().toString());
            reference.child("Items").child(selectCategory_textView.getText().toString()).child(key).child("Item_id").setValue(itemId_editText.getText().toString());
            reference.child("Items").child(selectCategory_textView.getText().toString()).child(key).child("added_by").setValue(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
            reference.child("Items").child(selectCategory_textView.getText().toString()).child(key).child("Item_name").setValue(itemName_editText.getText().toString());
            reference.child("Items").child(selectCategory_textView.getText().toString()).child(key).child("Condition").setValue(ratingBar.getRating());
            reference.child("Items").child(selectCategory_textView.getText().toString()).child(key).child("Notes").setValue(notes_editText.getText().toString());
            reference.child("Items").child(selectCategory_textView.getText().toString()).child(key).child("Price").setValue(price_editText.getText().toString());
            reference.child("Items").child(selectCategory_textView.getText().toString()).child(key).child("Description").setValue(description_editText.getText().toString());
            reference.child("Items").child(selectCategory_textView.getText().toString()).child(key).child("key_id").setValue(key);
            reference.child("Items").child(selectCategory_textView.getText().toString()).child(key).child("No_of_images").setValue(String.valueOf(imageUrlList.size()));


            for (i = 0; i<imageUrlList.size();i++) {

                key2 = reference.push().getKey();
                idStorageReference.child(key).child("image_"+String.valueOf(i+1)).putFile(imageUrlList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismissProgressBar(Item_detail.this);

                        if(i==imageUrlList.size()){
                            pass_back_data();
                            finish();
                        }


                    }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismissProgressBar(Item_detail.this);
                            Toast.makeText(Item_detail.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                        }
                    });
            }

            ///////////////////////////////////////////////////////////



            ///////////////////////////////////////////////////////////

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
        intent.putExtra("Item_keyid", key);
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

        uploadId_btn.setOnClickListener(new View.OnClickListener() {
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

        submit_btn.setOnClickListener(new View.OnClickListener() {
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
            Toast.makeText(this, "Please select category", Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "Please upload a picture", Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "Please select rating", Toast.LENGTH_SHORT).show();
            valid=false;
        }


        return valid;
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

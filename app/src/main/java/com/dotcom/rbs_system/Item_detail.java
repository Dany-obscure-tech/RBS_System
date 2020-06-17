package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Model.SampleSearchModel;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Item_detail extends AppCompatActivity {

    // Progress dialog
    Progress_dialoge pd;

    private static final int CAMERA_REQUEST_CODE = 1;
    Button submit_btn,uploadId_btn;
    TextView selectCategory_textView;
    DatabaseReference categoryRef,reference;
    ImageView id_imageView;
    EditText itemName_editText,notes_editText,itemId_editText,price_editText;
    List<String> categoryList;
    RatingBar ratingBar;
    ImageButton Back_btn;
    Uri tempUri=null;
    StorageReference idStorageReference;
    StorageReference storageReference;

    String key;

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

        storageReference = FirebaseStorage.getInstance().getReference();
        Back_btn = (ImageButton)findViewById(R.id.Back_btn);
        itemName_editText = (EditText)findViewById(R.id.itemName_editText);
        itemId_editText = (EditText)findViewById(R.id.itemId_editText);
        price_editText = (EditText)findViewById(R.id.price_editText);
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
            reference.child("Items").child(selectCategory_textView.getText().toString()).child(key).child("key_id").setValue(key);

            idStorageReference.child(itemId_editText.getText().toString()).child("image").putFile(tempUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    idStorageReference.child(itemId_editText.getText().toString()).child("image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            reference.child("Items").child(selectCategory_textView.getText().toString()).child(key).child("id_image_url").setValue(String.valueOf(uri));

                            pd.dismissProgressBar(Item_detail.this);
                            pass_back_data();
                            finish();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismissProgressBar(Item_detail.this);
                    Toast.makeText(Item_detail.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            });


        }
        else
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;




    }

    private void pass_back_data() {
//        Intent getting_intent = getIntent();
//        String get_intent = getting_intent.getStringExtra("BUY");
//        if (get_intent.equals("ON")){
//            String category=selectCategory_btn.getText().toString() ;
//            Float condition= ratingBar.getRating() ;
//            String notes=notes_editText.getText().toString() ;
//            Intent intent= new Intent();
//            intent.putExtra("Category", category);
//            intent.putExtra("Condition", condition);
//            intent.putExtra("Notes", notes);
//            setResult(RESULT_OK, intent);
//
//        }


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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);

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
        if (tempUri==null){
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            id_imageView.setImageBitmap(imageBitmap);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            tempUri = getImageUri(getApplicationContext(), imageBitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

}

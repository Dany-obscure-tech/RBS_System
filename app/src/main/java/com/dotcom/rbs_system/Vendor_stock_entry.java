package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Vendor_stock_entry extends AppCompatActivity {
    DatabaseReference reference;
    Dialog confirmation_alert;
    TextView yes_btn_textview, cancel_btn_textview;
    StorageReference stockImageStorageReference;
    ImageView image_imageView;
    ImageButton back_btn;
    CardView searchForItem_cardView;

    Uri fileUri = null;

    EditText vendor_item_name_editText, vendor_item_price_editText, vendor_item_qty_editText, vendor_sno_editText;
    TextView item_category_textView, add_btn, invoiceDate_textView, date_textView, uploadImage_textView;

    DatePickerDialog.OnDateSetListener onDateSetListener;
    List<String> Vendor_category_data_list;
    String vendor_stock_entry_id, vendorStockCategory;
    String currentDateString;
    String firebaseAuthUID;
    Date date = Calendar.getInstance().getTime();


    private ArrayList<SampleSearchModel> getting_vendor_categories_data() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
//        for (int i=0;i<Voucher_amount_list.size();i++){
        for (int i = 0; i < Vendor_category_data_list.size(); i++) {
//            items.add(new SampleSearchModel(voucher_number_list.get(i)+"\n("+Voucher_amount_list.get(i)+")",null,null,null,null,null,null,null));
            items.add(new SampleSearchModel(Vendor_category_data_list.get(i), null, null, null, null, null, null, null));
        }

        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_stock_entry);
        Initialization();
        Onclicklistners();
        //TODO yaha pa edit alert ma bug ha
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialization() {
        stockImageStorageReference = FirebaseStorage.getInstance().getReference().child("Vendor_Stock_Images");

        back_btn = findViewById(R.id.back_btn);
        confirmation_alert = new Dialog(this);
        confirmation_alert.setContentView(R.layout.exit_confirmation_alert);
        yes_btn_textview = confirmation_alert.findViewById(R.id.yes_btn_textview);
        cancel_btn_textview = confirmation_alert.findViewById(R.id.cancel_btn_textview);

        image_imageView = findViewById(R.id.image_imageView);

        item_category_textView = findViewById(R.id.item_category_textView);
        add_btn = findViewById(R.id.add_btn);
        date_textView = findViewById(R.id.date_textView);
        invoiceDate_textView = findViewById(R.id.invoiceDate_textView);
        uploadImage_textView = findViewById(R.id.uploadImage_textView);
        currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);
        invoiceDate_textView.setText(currentDateString);

        vendor_item_name_editText = findViewById(R.id.vendor_item_name_editText);
        vendor_item_price_editText = findViewById(R.id.vendor_item_price_editText);
        vendor_item_qty_editText = findViewById(R.id.vendor_item_qty_editText);
        vendor_sno_editText = findViewById(R.id.vendor_sno_editText);

        searchForItem_cardView = findViewById(R.id.searchForItem_cardView);


        Vendor_category_data_list = new ArrayList<>();
        Vendor_category_data_list.add("Laptop");
        Vendor_category_data_list.add("Tablet");
        Vendor_category_data_list.add("Mobile");
        Vendor_category_data_list.add("PC");

        reference = FirebaseDatabase.getInstance().getReference();
        vendor_stock_entry_id = reference.push().getKey();

        firebaseAuthUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void Onclicklistners() {
        yes_btn_textview_listner();
        cancel_btn_textview_listner();
        back_btn_listner();
        searchForCategories_listner();
        add_btn_listner();
        date_textView_listner();
        uploadImage_textView_listener();
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

    private void uploadImage_textView_listener() {
        uploadImage_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
    }

    private void date_textView_listner() {
        date_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = (calendar.get(Calendar.YEAR)) - 18;
                int month = 0;
                int day = 1;

                DatePickerDialog dialog = new DatePickerDialog(Vendor_stock_entry.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
                currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;

                SimpleDateFormat input = new SimpleDateFormat("d/M/yyyy");
                SimpleDateFormat output = new SimpleDateFormat("EEEE, d MMMM yyyy");

                try {
                    invoiceDate_textView.setText(output.format(input.parse(date)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void add_btn_listner() {
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatefields()) {
                    stock_entry_data();
                }
            }
        });
    }

    private void searchForCategories_listner() {
        searchForItem_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SimpleSearchDialogCompat(Vendor_stock_entry.this, "Search results...",
                        "Search for voucher number.", null, getting_vendor_categories_data(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                item_category_textView.setText(item.getTitle());
                                item_category_textView.setTextColor(getResources().getColor(R.color.textBlue));
                                dialog.dismiss();
                            }
                        }).show();

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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void stock_entry_data() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Vendor_stock_entry.this.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            final String key = reference.push().getKey();
            reference.child("Vendor_stock").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Category").setValue(item_category_textView.getText().toString());
            reference.child("Vendor_stock").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Name").setValue(vendor_item_name_editText.getText().toString());
            reference.child("Vendor_stock").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Price").setValue(vendor_item_price_editText.getText().toString());
            reference.child("Vendor_stock").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Quantity").setValue(vendor_item_qty_editText.getText().toString());
            reference.child("Vendor_stock").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Sno").setValue(vendor_sno_editText.getText().toString());
            reference.child("Vendor_stock").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Date").setValue(date_textView.getText().toString());
            reference.child("Vendor_stock").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Added_by").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
            vendorStockCategory = item_category_textView.getText().toString();

            stockImageStorageReference.child(firebaseAuthUID).child(vendorStockCategory).child(key).child("image").putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    stockImageStorageReference.child(firebaseAuthUID).child(vendorStockCategory).child(key).child("image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            reference.child("Vendor_stock").child(firebaseAuthUID).child(vendorStockCategory).child(key).child("Image_url").setValue(String.valueOf(uri.toString()));
                            Toast.makeText(Vendor_stock_entry.this, "Stock entered!", Toast.LENGTH_SHORT).show();

                            setResult(111, new Intent());
                            finish();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        } else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
        }
        connected = false;
    }

    private void takePicture() {
        ImagePicker.Companion.with(Vendor_stock_entry.this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    private boolean validatefields() {
        boolean valid = true;

        if (item_category_textView.getText().toString().equals("Search for category ...")) {
            Toast.makeText(Vendor_stock_entry.this, "Please select category", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (vendor_item_name_editText.getText().toString().isEmpty()) {
            vendor_item_name_editText.setError("Please enter item name");
            valid = false;
        }

        if (vendor_sno_editText.getText().toString().isEmpty()) {
            vendor_sno_editText.setError("Please enter item sno");
            valid = false;
        }

        if (vendor_item_qty_editText.getText().toString().isEmpty()) {
            vendor_item_qty_editText.setError("Please enter price");
            valid = false;

        }else {
            if (Float.parseFloat(vendor_item_qty_editText.getText().toString())==0){
                vendor_item_qty_editText.setError("Please enter valid price");
            }else {
                vendor_item_qty_editText.setText(vendor_item_qty_editText.getText().toString().replaceFirst("^0+(?!$)",""));
                if (vendor_item_qty_editText.getText().toString().startsWith(".")){
                    vendor_item_qty_editText.setText("0"+ vendor_item_qty_editText.getText().toString());
                }
            }
        }


        if (vendor_item_qty_editText.getText().toString().equals("0")) {
            vendor_item_qty_editText.setError("Please enter item quantity");
            valid = false;
        }
        if (fileUri == null) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (vendor_item_price_editText.getText().toString().isEmpty()) {
            vendor_item_price_editText.setError("Please enter price");
            valid = false;

        }else {
            if (Float.parseFloat(vendor_item_price_editText.getText().toString())==0){
                vendor_item_price_editText.setError("Please enter valid price");
            }else {
                vendor_item_price_editText.setText(vendor_item_price_editText.getText().toString().replaceFirst("^0+(?!$)",""));
                if (vendor_item_price_editText.getText().toString().startsWith(".")){
                    vendor_item_price_editText.setText("0"+ vendor_item_price_editText.getText().toString());
                }
            }
        }


        return valid;
    }

    public void onBackPressed() {
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            fileUri = data.getData();
            image_imageView.setImageURI(fileUri);

            //You can get File object from intent
//            val file:File = ImagePicker.getFile(data)!!

            //You can also get File Path from intent
//                    val filePath:String = ImagePicker.getFilePath(data)!!
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
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
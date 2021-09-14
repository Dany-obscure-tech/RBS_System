package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterAccessoriesItemsRecyclerView;
import com.dotcom.rbs_system.Classes.Currency;
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
import com.hbb20.CountryCodePicker;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Accessory_add extends AppCompatActivity {

    DatabaseReference reference;
    Dialog confirmation_alert;
    TextView yes_btn_textview, cancel_btn_textview;

    ImageButton back_btn;
    CardView searchForItem_cardView;

    EditText shopKeeper_item_name_editText, shopKeeper_item_price_editText, shopKeeper_item_qty_editText;
    TextView item_category_textView, add_btn, invoiceDate_textView;

    DatePickerDialog.OnDateSetListener onDateSetListener;
    List<String> shopKeeper_category_data_list;
    String shopKeeper_stock_entry_id, shopKeeperStockCategory;
    String currentDateString;
    String firebaseAuthUID;
    Date date = Calendar.getInstance().getTime();


    private ArrayList<SampleSearchModel> getting_shopKeeper_categories_data() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
//        for (int i=0;i<Voucher_amount_list.size();i++){
        for (int i = 0; i < shopKeeper_category_data_list.size(); i++) {
//            items.add(new SampleSearchModel(voucher_number_list.get(i)+"\n("+Voucher_amount_list.get(i)+")",null,null,null,null,null,null,null));
            items.add(new SampleSearchModel(shopKeeper_category_data_list.get(i), null, null, null, null, null, null, null));
        }

        return items;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory_add);
        Initialization();
        Onclicklistners();
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialization() {

        back_btn = findViewById(R.id.back_btn);
        confirmation_alert = new Dialog(this);
        confirmation_alert.setContentView(R.layout.exit_confirmation_alert);
        yes_btn_textview = confirmation_alert.findViewById(R.id.yes_btn_textview);
        cancel_btn_textview = confirmation_alert.findViewById(R.id.cancel_btn_textview);

        item_category_textView = findViewById(R.id.item_category_textView);
        add_btn = findViewById(R.id.add_btn);
        invoiceDate_textView = findViewById(R.id.invoiceDate_textView);
        currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);
        invoiceDate_textView.setText(currentDateString);

        shopKeeper_item_name_editText = findViewById(R.id.shopKeeper_item_name_editText);
        shopKeeper_item_price_editText = findViewById(R.id.shopKeeper_item_price_editText);
        shopKeeper_item_qty_editText = findViewById(R.id.shopKeeper_item_qty_editText);

        searchForItem_cardView = findViewById(R.id.searchForItem_cardView);

        shopKeeper_category_data_list = new ArrayList<>();
        shopKeeper_category_data_list.add("Laptop");
        shopKeeper_category_data_list.add("Tablet");
        shopKeeper_category_data_list.add("Mobile");
        shopKeeper_category_data_list.add("PC");

        reference = FirebaseDatabase.getInstance().getReference();
        shopKeeper_stock_entry_id = reference.push().getKey();

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

    private void date_textView_listner() {
        invoiceDate_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = (calendar.get(Calendar.YEAR)) - 18;
                int month = 0;
                int day = 1;

                DatePickerDialog dialog = new DatePickerDialog(Accessory_add.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
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

                new SimpleSearchDialogCompat(Accessory_add.this, "Search results...",
                        "Search for voucher number.", null, getting_shopKeeper_categories_data(),
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
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Accessory_add.this.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            final String key = reference.push().getKey();
            reference.child("ShopKeeper_accessories").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Category").setValue(item_category_textView.getText().toString());
            reference.child("ShopKeeper_accessories").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Name").setValue(shopKeeper_item_name_editText.getText().toString());
            reference.child("ShopKeeper_accessories").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Price").setValue(shopKeeper_item_price_editText.getText().toString());
            reference.child("ShopKeeper_accessories").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Quantity").setValue(shopKeeper_item_qty_editText.getText().toString());
            reference.child("ShopKeeper_accessories").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Date").setValue(invoiceDate_textView.getText().toString());

            shopKeeperStockCategory = item_category_textView.getText().toString();

        } else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
        }
        connected = false;
    }

    private boolean validatefields() {
        boolean valid = true;

        if (item_category_textView.getText().toString().equals("Search for category ...")) {
            Toast.makeText(Accessory_add.this, "Please select category", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (shopKeeper_item_name_editText.getText().toString().isEmpty()) {
            shopKeeper_item_name_editText.setError("Please enter item name");
            valid = false;
        }

        if (shopKeeper_item_qty_editText.getText().toString().isEmpty()) {
            shopKeeper_item_qty_editText.setError("Please enter price");
            valid = false;

        }else {
            if (Float.parseFloat(shopKeeper_item_qty_editText.getText().toString())==0){
                shopKeeper_item_qty_editText.setError("Please enter valid price");
            }else {
                shopKeeper_item_qty_editText.setText(shopKeeper_item_qty_editText.getText().toString().replaceFirst("^0+(?!$)",""));
                if (shopKeeper_item_qty_editText.getText().toString().startsWith(".")){
                    shopKeeper_item_qty_editText.setText("0"+ shopKeeper_item_qty_editText.getText().toString());
                }
            }
        }

        if (shopKeeper_item_qty_editText.getText().toString().equals("0")) {
            shopKeeper_item_qty_editText.setError("Please enter item quantity");
            valid = false;
        }

        if (shopKeeper_item_price_editText.getText().toString().isEmpty()) {
            shopKeeper_item_price_editText.setError("Please enter price");
            valid = false;

        }else {
            if (Float.parseFloat(shopKeeper_item_price_editText.getText().toString())==0){
                shopKeeper_item_price_editText.setError("Please enter valid price");
            }else {
                shopKeeper_item_price_editText.setText(shopKeeper_item_price_editText.getText().toString().replaceFirst("^0+(?!$)",""));
                if (shopKeeper_item_price_editText.getText().toString().startsWith(".")){
                    shopKeeper_item_price_editText.setText("0"+ shopKeeper_item_price_editText.getText().toString());
                }
            }
        }

        return valid;
    }

    public void onBackPressed() {
        finish();

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

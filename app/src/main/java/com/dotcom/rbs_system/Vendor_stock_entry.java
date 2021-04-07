package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Vendor_stock_entry extends AppCompatActivity {
    ImageButton back_btn;
    LinearLayout searchForCategories;
    TextView item_category_textView, add_btn;
    List<String> Vendor_category_data_list;
    DatabaseReference reference;
    String vendor_stock_entry_id;
    String firebaseAuthUID;
    EditText vendor_item_name_editText, vendor_item_price_editText, vendor_item_qty_editText;

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
    }

    private void Onclicklistners() {
        back_btn_listner();
        searchForCategories_listner();
        add_btn_listner();
    }

    private void add_btn_listner() {
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatefields() == true) {
                    stock_entry_data();
                }
            }
        });
    }

    private void stock_entry_data() {
        boolean connected = false;
        Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Vendor_stock_entry.this.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            String key = reference.push().getKey();
            reference.child("Vendor_stock").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Category").setValue(item_category_textView.getText().toString());
            reference.child("Vendor_stock").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Name").setValue(vendor_item_name_editText.getText().toString());
            reference.child("Vendor_stock").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Price").setValue(vendor_item_price_editText.getText().toString());
            reference.child("Vendor_stock").child(firebaseAuthUID).child(item_category_textView.getText().toString()).child(key).child("Quantity").setValue(vendor_item_qty_editText.getText().toString());


            Toast.makeText(this, "Ended Succesfully", Toast.LENGTH_SHORT).show();
            finish();

        } else {
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
        }
        connected = false;
    }

    private void searchForCategories_listner() {
        searchForCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SimpleSearchDialogCompat(Vendor_stock_entry.this, "Search results...",
                        "Search for voucher number.", null, getting_vendor_categories_data(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                item_category_textView.setText(item.getTitle());
                                item_category_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                                item_category_textView.setTextColor(getResources().getColor(R.color.textGrey));
                                dialog.dismiss();
                            }
                        }).show();

            }
        });
    }

    private boolean validatefields() {
        boolean valid = true;

        if (item_category_textView.getText().toString().isEmpty()) {
            item_category_textView.setError("Please select category");
            valid = false;
        }
        if (item_category_textView.getText().toString().equals("Search for category ...")) {
            item_category_textView.setError("Please select category");
            valid = false;
        }

        if (vendor_item_name_editText.getText().toString().isEmpty()) {
            vendor_item_name_editText.setError("Please enter item name");
            valid = false;
        }

        if (vendor_item_price_editText.getText().toString().isEmpty()) {
            vendor_item_price_editText.setError("Please enter item price");
            valid = false;
        }
        if (vendor_item_qty_editText.getText().toString().isEmpty()) {
            vendor_item_qty_editText.setError("Please enter item quantity");
            valid = false;
        }
        if (vendor_item_qty_editText.getText().toString().equals("0")) {
            vendor_item_qty_editText.setError("Please enter item quantity");
            valid = false;
        }

        return valid;
    }

    private void back_btn_listner() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Initialization() {
        back_btn = (ImageButton) findViewById(R.id.back_btn);
        item_category_textView = (TextView) findViewById(R.id.company_name_textView);
        vendor_item_name_editText = (EditText) findViewById(R.id.vendor_item_name_editText);
        vendor_item_price_editText = (EditText) findViewById(R.id.vendor_item_price_editText);
        vendor_item_qty_editText = (EditText) findViewById(R.id.vendor_item_qty_editText);
        add_btn = (TextView) findViewById(R.id.add_btn);
        searchForCategories = (LinearLayout) findViewById(R.id.searchForCategories);
        Vendor_category_data_list = new ArrayList<>();
        Vendor_category_data_list.add("Laptop");
        Vendor_category_data_list.add("Tablet");
        Vendor_category_data_list.add("Mobile");
        Vendor_category_data_list.add("PC");
        reference = FirebaseDatabase.getInstance().getReference();
        vendor_stock_entry_id = reference.push().getKey().toString();
        firebaseAuthUID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public void onBackPressed() {
        finish();

    }
}
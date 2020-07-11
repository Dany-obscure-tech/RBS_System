package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Accessory_sale extends AppCompatActivity {

    Button saleAccessory_btn,date_btn,alertSaleAccessoryCancel_btn;
    ImageButton Back_btn;
    EditText customer_name_editText,customer_phone_no_editText;
    //    Date date;
    TextView date_textView,select_category_textView,select_itemname_textView;
    Dialog accessorySaleAlert;
    RecyclerView accessoryItemList_recyclerView;
    DatabaseReference existing_categories,existing_items;
    List<String> accessories_categories_raw_list,accessories_categories_filter_list;
    List<String> accessories_item_name_list,category_data,accessories_item_quantity_list,accessories_item_total_price_list,accessories_item_unit_price_list;
    List<String> accessories_category_list;

    private ArrayList<SampleSearchModel> selectCategoryData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i = 0; i< accessories_categories_filter_list.size(); i++){
            items.add(new SampleSearchModel(accessories_categories_filter_list.get(i), null,null,null,null,null,null,null));
        }

        return items;
    }


////////////////////////////////ON CREATE/////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory_sale);
        Initialization();
        ClickListeners();
        fetching_item_name();

    }


    ////////////////////////////ON CREATE/////////////////////////
    private void Initialization() {

        existing_categories = FirebaseDatabase.getInstance().getReference("Accessories_invoices/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        accessories_categories_raw_list = new ArrayList<>();
        accessories_categories_filter_list = new ArrayList<>();
        accessories_item_name_list = new ArrayList<>();
        category_data = new ArrayList<>();
        accessories_item_quantity_list = new ArrayList<>();
        accessories_item_total_price_list = new ArrayList<>();
        accessories_item_unit_price_list = new ArrayList<>();
        accessories_category_list = new ArrayList<>();

        Back_btn = (ImageButton) findViewById(R.id.Back_btn);
        customer_name_editText = (EditText) findViewById(R.id.customer_name_editText);
        customer_phone_no_editText = (EditText) findViewById(R.id.customer_phone_no_editText);
        date_btn = (Button) findViewById(R.id.date_btn);
        accessoryItemList_recyclerView = (RecyclerView)findViewById(R.id.accessoryItemList_recyclerView);
        accessoryItemList_recyclerView.setLayoutManager(new GridLayoutManager(Accessory_sale.this,1));
        //TODO
//        accessoryItemList_recyclerView.setAdapter(adapterAccessoriesItemsRecyclerView);
        accessorySaleAlert = new Dialog(this);
        accessorySaleAlert.setContentView(R.layout.alert_sale_accesory_item);
        saleAccessory_btn = (Button) findViewById(R.id.saleAccessory_btn);
        alertSaleAccessoryCancel_btn = (Button) accessorySaleAlert.findViewById(R.id.alertSaleAccessoryCancel_btn);
        select_category_textView = (TextView) accessorySaleAlert.findViewById(R.id.select_category_textView);
        select_itemname_textView = (TextView) accessorySaleAlert.findViewById(R.id.select_itemname_textView);



//        date= Calendar.getInstance().getTime();
//        String currentDateString= DateFormat.getDateInstance(DateFormat.FULL).format(date);
//        date_textView.setText(currentDateString);
//        date_textView.setText(currentDateString);
    }

    private void ClickListeners() {

//        selectdate();
        backbtn();
        alertSaleAccessory();
        alertCancel();
        select_sale_category();
        select_item_name();


    }

    private void select_item_name() {
        select_itemname_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void select_sale_category() {
        select_category_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Accessory_sale.this, "Search...",
                        "What are you looking for...?", null, selectCategoryData(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                select_category_textView.setText(item.getTitle());
//                                vendorName = item.getName();
//                                vendorPhno = item.getId();
                                select_category_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                                select_category_textView.setTextColor(getResources().getColor(R.color.textGrey));
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void alertCancel() {
        alertSaleAccessoryCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessorySaleAlert.dismiss();
            }
        });
    }

    private void alertSaleAccessory() {
        saleAccessory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessorySaleAlert.show();
                fetching_categories();

            }
        });

    }

    private void fetching_categories() {
        existing_categories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    accessories_categories_raw_list.add(String.valueOf(dataSnapshot1.child("catagory").getValue()));
                }
                accessories_categories_filter_list = new ArrayList<String>(new LinkedHashSet<String>(accessories_categories_raw_list));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetching_item_name() {
        existing_categories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    for (DataSnapshot dataSnapshot2:dataSnapshot1.child("Accessory_items").getChildren()){
                        accessories_item_name_list.add(String.valueOf(dataSnapshot2.child("name").getValue()));
                        Toast.makeText(Accessory_sale.this,String.valueOf(dataSnapshot1.child("catagory").getValue()), Toast.LENGTH_SHORT).show();
                        accessories_category_list.add(String.valueOf(dataSnapshot1.child("catagory").getValue()));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void backbtn() {
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void selectdate() {
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker=new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"date picker");
            }
        });
    }


}
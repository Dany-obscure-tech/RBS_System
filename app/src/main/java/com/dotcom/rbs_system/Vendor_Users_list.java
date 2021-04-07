package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.Adapter_Vendor_shopkeeper_invoices_RecyclerView;
import com.dotcom.rbs_system.Adapter.Adapter_Vendor_users_list_RecyclerView;
import com.dotcom.rbs_system.Model.SampleSearchModel;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Vendor_Users_list extends AppCompatActivity {

    RecyclerView users_list_recyclerview;
    List<String> user_name;
    ImageButton back_btn;
    TextView add_users_btn,cancel_btn;
    Dialog add_user_alert_dialog;
    LinearLayout select_user_category;
    TextView user_category_textView;
    List<String> user_category_list;


    private ArrayList<SampleSearchModel> setting_user_categories_data() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
//        for (int i=0;i<Voucher_amount_list.size();i++){
        for (int i = 0; i < user_category_list.size(); i++) {
//            items.add(new SampleSearchModel(voucher_number_list.get(i)+"\n("+Voucher_amount_list.get(i)+")",null,null,null,null,null,null,null));
            items.add(new SampleSearchModel(user_category_list.get(i), null, null, null, null, null, null, null));
        }

        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor__users_list);
        initialization();
        onclicklistners();

        Adapter_Vendor_users_list_RecyclerView adapter_vendor_users_list_recyclerView=new Adapter_Vendor_users_list_RecyclerView(Vendor_Users_list.this,null,user_name,null,null);

        users_list_recyclerview.setLayoutManager(new GridLayoutManager(Vendor_Users_list.this,1));
        users_list_recyclerview.setAdapter(adapter_vendor_users_list_recyclerView);
    }

    private void onclicklistners() {
        back_btn_listner();
        add_users_btn_listner();
        select_user_category_listner();
        cancel_btn_listner();

    }

    private void cancel_btn_listner() {
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_user_alert_dialog.dismiss();
            }
        });
    }

    private void select_user_category_listner() {
        select_user_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(Vendor_Users_list.this, "Search results...",
                        "Search for voucher number.", null, setting_user_categories_data(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                user_category_textView.setText(item.getTitle());
                                user_category_textView.setBackground(getResources().getDrawable(R.drawable.main_button_grey));
                                user_category_textView.setTextColor(getResources().getColor(R.color.textGrey));
                                dialog.dismiss();
                            }
                        }).show();

            }


        });
    }

    private void add_users_btn_listner() {
        add_users_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_user_alert_dialog.show();
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

    private void initialization() {
        users_list_recyclerview=(RecyclerView)findViewById(R.id.users_list_recyclerview);
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        add_users_btn=(TextView)findViewById(R.id.add_users_btn);
        user_name=new ArrayList<>();
        user_name.add("Mehmood");
        user_name.add("Amjad");
        user_name.add("Ali");
        add_user_alert_dialog = new Dialog(this);
        add_user_alert_dialog.setContentView(R.layout.alert_vendor_add_users);
        select_user_category = (LinearLayout) add_user_alert_dialog.findViewById(R.id.select_user_category);
        user_category_textView = (TextView) add_user_alert_dialog.findViewById(R.id.user_category_textView);
        cancel_btn = (TextView) add_user_alert_dialog.findViewById(R.id.cancel_btn);
        user_category_list=new ArrayList<>();
        user_category_list.add("Data Entry");
        user_category_list.add("Salesman");
    }
}
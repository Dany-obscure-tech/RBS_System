package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.Adapter_Vendor_users_list_RecyclerView;
import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class Vendor_Users_list extends AppCompatActivity {

    RecyclerView users_list_recyclerview;
    List<String> user_category, user_name, user_phone_number, user_email;
    ImageButton back_btn;
    TextView add_users_btn, cancel_btn, save_btn, email_editText, name_editText;
    Dialog add_user_alert_dialog;
    LinearLayout select_user_category;
    TextView user_category_textView, random_passcode_editText;
    List<String> user_category_list;
    String selected_country_code;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    EditText editTextCarrierNumber;

    CountryCodePicker ccp;


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

        //TODO "users_activity" ka button invisible ha
        //TODo xml pa kam karna ha
        //TODO dany dialog box ko online karna ha add new user waley kay "String.valueOf(ccp.getFullNumberWithPlus());" is ko retrive data ka liya use kar ley
        Adapter_Vendor_users_list_RecyclerView adapter_vendor_users_list_recyclerView = new Adapter_Vendor_users_list_RecyclerView(Vendor_Users_list.this, null, user_name, null, null);

        users_list_recyclerview.setLayoutManager(new GridLayoutManager(Vendor_Users_list.this, 1));
        users_list_recyclerview.setAdapter(adapter_vendor_users_list_recyclerView);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    private void initialization() {
        users_list_recyclerview = (RecyclerView) findViewById(R.id.users_list_recyclerview);
        back_btn = (ImageButton) findViewById(R.id.back_btn);
        add_users_btn = (TextView) findViewById(R.id.add_users_btn);
        user_category = new ArrayList<>();
        user_name = new ArrayList<>();
        user_phone_number = new ArrayList<>();
        user_email = new ArrayList<>();
        user_name.add("Mehmood");
        user_name.add("Amjad");
        user_name.add("Ali");
        add_user_alert_dialog = new Dialog(this);
        add_user_alert_dialog.setContentView(R.layout.alert_vendor_add_users);
        select_user_category = (LinearLayout) add_user_alert_dialog.findViewById(R.id.select_user_category);
        user_category_textView = (TextView) add_user_alert_dialog.findViewById(R.id.user_category_textView);
        cancel_btn = (TextView) add_user_alert_dialog.findViewById(R.id.cancel_btn);
        save_btn = (TextView) add_user_alert_dialog.findViewById(R.id.save_btn);
        name_editText = (TextView) add_user_alert_dialog.findViewById(R.id.name_editText);
        random_passcode_editText = (TextView) add_user_alert_dialog.findViewById(R.id.random_passcode_editText);
        email_editText = (TextView) add_user_alert_dialog.findViewById(R.id.email_editText);
        ccp = add_user_alert_dialog.findViewById(R.id.ccp);
        editTextCarrierNumber = add_user_alert_dialog.findViewById(R.id.editText_carrierNumber);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);

        user_category_list = new ArrayList<>();
        user_category_list.add("Data Entry");
        user_category_list.add("Salesman");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    private void onclicklistners() {
        back_btn_listner();
        add_users_btn_listner();
        select_user_category_listner();
        cancel_btn_listner();
        save_btn_listner();

    }

    private void save_btn_listner() {
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatefields()) {
                    Toast.makeText(Vendor_Users_list.this, "New User Created", Toast.LENGTH_SHORT).show();
                    add_user_alert_dialog.dismiss();
                    recreate();
                }
            }
        });
    }

    private boolean validatefields() {
        boolean valid = true;
        String email = email_editText.getText().toString().trim();
        if (email_editText.getText().toString().equals("")) {
            email_editText.setError("Enter email address");
            valid = false;
        }
        if (!email.matches(emailPattern)) {
            email_editText.setError("Enter valid email address");
            valid = false;
        }
        if (!ccp.isValidFullNumber()) {
            editTextCarrierNumber.setError("Please enter valid number");
            valid = false;
        }
        if (random_passcode_editText.getText().toString().equals("")) {
            random_passcode_editText.setError("Enter passcode");
            valid = false;
        }
        if (random_passcode_editText.getText().toString().length() <4) {
            random_passcode_editText.setError("Enter complete passcode");
            valid = false;
        }
        if (user_category_textView.getText().toString().equals("Select user category ...")) {
            Toast.makeText(Vendor_Users_list.this, "Select the user category", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (name_editText.getText().toString().equals("")) {
            name_editText.setError("Enter username");
            valid = false;
        }
        return valid;
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
                                user_category_textView.setTextColor(getResources().getColor(R.color.textBlue));
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

    public void onCountryPickerClick(View view) {
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
                selected_country_code = ccp.getFullNumberWithPlus();
            }
        });
    }
}
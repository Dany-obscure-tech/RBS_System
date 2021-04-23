package com.dotcom.rbs_system;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VendorProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VendorProfile extends Fragment {
    DatabaseReference userDataRef;
    View view;
    TextView view_users_btn,edit_vendor_details_cancel_btn,change_passcode_btn,change_passcode_cancel_btn,change_passcode_submit_btn,change_new_passcode_cancel_btn,edit_vendor_details_btn;
    Dialog change_passcode_alert_dialog,new_passcode_alert_dialog,edit_vendor_info_alert_dialog;

    TextView appRegNo_textView, name_textView, company_name_textView,company_reg_no_textView,post_code_textView,vendor_address_textView,vendor_phone_textView,vendor_mobile_textView,vendor_email_textView,vendor_url_textView;
    ImageView profileImage_imageView,store_banner_imageView;

    EditText edit_pastcode_editText,edit_address_editText,edit_phone_number_editText,edit_mobile_number_editText,edit_email_address_editText,edit_url_address_editText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VendorProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VendorProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static VendorProfile newInstance(String param1, String param2) {
        VendorProfile fragment = new VendorProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_vendor_profile, container, false);
        initialization();
        onclicklistners();
        datafetch();

        return view;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    private void initialization() {
        userDataRef = FirebaseDatabase.getInstance().getReference("Users_data/"+FirebaseAuth.getInstance().getCurrentUser().getUid());

        appRegNo_textView = (TextView)view.findViewById(R.id.appRegNo_textView);
        name_textView = (TextView)view.findViewById(R.id.name_textView);
        company_name_textView = (TextView)view.findViewById(R.id.company_name_textView);
        company_reg_no_textView = (TextView)view.findViewById(R.id.company_reg_no_textView);
        post_code_textView = (TextView)view.findViewById(R.id.post_code_textView);
        vendor_address_textView = (TextView)view.findViewById(R.id.vendor_address_textView);
        vendor_phone_textView = (TextView)view.findViewById(R.id.vendor_phone_textView);
        vendor_mobile_textView = (TextView)view.findViewById(R.id.vendor_mobile_textView);
        vendor_email_textView = (TextView)view.findViewById(R.id.vendor_email_textView);
        vendor_url_textView = (TextView)view.findViewById(R.id.vendor_url_textView);

        profileImage_imageView = (ImageView) view.findViewById(R.id.profileImage_imageView);
        store_banner_imageView = (ImageView)view.findViewById(R.id.store_banner_imageView);

        view_users_btn=(TextView)view.findViewById(R.id.view_users_btn);
        edit_vendor_details_btn=(TextView)view.findViewById(R.id.edit_vendor_details_btn);
        change_passcode_alert_dialog = new Dialog(getActivity());
        change_passcode_alert_dialog.setContentView(R.layout.alert_vendor_change_passcode);
        change_passcode_cancel_btn = (TextView) change_passcode_alert_dialog.findViewById(R.id.change_passcode_cancel_btn);
        change_passcode_submit_btn = (TextView) change_passcode_alert_dialog.findViewById(R.id.change_passcode_submit_btn);
        new_passcode_alert_dialog = new Dialog(getActivity());
        new_passcode_alert_dialog.setContentView(R.layout.alert_vendor_new_passcode);
        edit_vendor_info_alert_dialog = new Dialog(getActivity());
        edit_vendor_info_alert_dialog.setContentView(R.layout.alert_vendor_details_edit);
        edit_pastcode_editText=(EditText)edit_vendor_info_alert_dialog.findViewById(R.id.edit_pastcode_editText);
        edit_address_editText=(EditText)edit_vendor_info_alert_dialog.findViewById(R.id.edit_address_editText);
        edit_phone_number_editText=(EditText)edit_vendor_info_alert_dialog.findViewById(R.id.edit_phone_number_editText);
        edit_mobile_number_editText=(EditText)edit_vendor_info_alert_dialog.findViewById(R.id.edit_mobile_number_editText);
        edit_email_address_editText=(EditText)edit_vendor_info_alert_dialog.findViewById(R.id.edit_email_address_editText);
        edit_url_address_editText=(EditText)edit_vendor_info_alert_dialog.findViewById(R.id.edit_url_address_editText);
        change_passcode_btn=(TextView) edit_vendor_info_alert_dialog.findViewById(R.id.change_passcode_btn);
        edit_vendor_details_cancel_btn=(TextView) edit_vendor_info_alert_dialog.findViewById(R.id.edit_vendor_details_cancel_btn);
        change_new_passcode_cancel_btn = (TextView) new_passcode_alert_dialog.findViewById(R.id.change_new_passcode_cancel_btn);

        edit_pastcode_editText.setText(post_code_textView.getText().toString());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private void datafetch() {
        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    name_textView.setText(dataSnapshot.child("fullname").getValue().toString());
                    vendor_address_textView.setText(dataSnapshot.child("address").getValue().toString());
                    vendor_phone_textView.setText(dataSnapshot.child("phno").getValue().toString());
                    appRegNo_textView.setText(dataSnapshot.child("app_reg_no").getValue().toString());
                    vendor_email_textView.setText(dataSnapshot.child("email").getValue().toString());
                    company_name_textView.setText(dataSnapshot.child("company_name").getValue().toString());

                    company_reg_no_textView.setText(dataSnapshot.child("company_regno").getValue().toString());
                    post_code_textView.setText(dataSnapshot.child("post_code").getValue().toString());
                    vendor_mobile_textView.setText(dataSnapshot.child("mobno").getValue().toString());
                    vendor_url_textView.setText(dataSnapshot.child("url").getValue().toString());

                    Picasso.get().load(String.valueOf(dataSnapshot.child("profile_image_url").getValue().toString())).into(profileImage_imageView);
                    Picasso.get().load(String.valueOf(dataSnapshot.child("banner").getValue().toString())).into(store_banner_imageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    private void onclicklistners() {
        view_users_btn_listner();
        edit_vendor_details_btn_listner();
        change_passcode_btn_listner();
        change_passcode_cancel_btn_listner();
        change_passcode_submit_btn_listner();
        change_new_passcode_cancel_btn_listner();
        edit_vendor_details_cancel_btn_listner();
    }

    private void edit_vendor_details_cancel_btn_listner() {
        edit_vendor_details_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_vendor_info_alert_dialog.dismiss();
            }
        });
    }

    private void edit_vendor_details_btn_listner() {
        edit_vendor_details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_vendor_info_alert_dialog.show();
            }
        });
    }

    private void change_new_passcode_cancel_btn_listner() {
        change_new_passcode_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_passcode_alert_dialog.dismiss();
            }
        });
    }

    private void change_passcode_btn_listner() {
        change_passcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_passcode_alert_dialog.show();
            }
        });
    }

    private void change_passcode_cancel_btn_listner() {
        change_passcode_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_passcode_alert_dialog.dismiss();
            }
        });
    }

    private void change_passcode_submit_btn_listner() {
        change_passcode_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_passcode_alert_dialog.show();
                change_passcode_alert_dialog.dismiss();
            }
        });
    }

    private void view_users_btn_listner() {
        view_users_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Vendor_Users_list.class);
                startActivity(intent);
            }
        });
    }

}
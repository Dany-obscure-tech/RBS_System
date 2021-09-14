package com.dotcom.rbs_system;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Classes.ActionBarTitle;
import com.dotcom.rbs_system.Classes.UserDetails;
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
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VendorProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VendorProfile extends Fragment {
    private static final int LOGO_READ_REQUEST_CODE = 42;
    private static final int BANNER_READ_REQUEST_CODE = 43;
    StorageReference vendorBannerStorageReference, vendorLogoStorageReference;

    DatabaseReference userDataRef;

    String selected_country_code;

    ImageView banner_image_view, logo_image_view;

    EditText editTextCarrierNumber;

    CountryCodePicker ccp;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    RelativeLayout banner_background_relativelayout, logo_background_relativelayout;

    View view;
    TextView edit_logo_image_textView, view_users_btn, edit_vendor_details_cancel_btn, edit_vendor_details_save_btn, change_passcode_btn, change_passcode_cancel_btn, change_passcode_submit_btn, change_new_passcode_cancel_btn, edit_vendor_details_btn_textView;
    Dialog change_passcode_alert_dialog, edit_vendor_info_alert_dialog;

    TextView edit_banner_btn_textiew;

    TextView appRegNo_textView, name_textView, company_name_textView, company_reg_no_textView, post_code_textView, vendor_address_textView, vendor_phone_textView, vendor_email_textView, vendor_url_textView;
    ImageView logo_imageView, store_banner_imageView;

    EditText random_passcode_editText, new_passcode_edittext, new_passcode_again_edittext, edit_pastcode_editText, edit_address_editText, edit_email_address_editText, edit_url_address_editText;

    String buttonPressCheck;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Uri fileUri;

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
        view = inflater.inflate(R.layout.fragment_vendor_profile, container, false);
        ActionBarTitle.getInstance().getTextView().setText("Vendor Profile");

        initialization();
        onclicklistners();
        datafetch();

        return view;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    private void initialization() {

        banner_background_relativelayout = view.findViewById(R.id.banner_background_relativelayout);
        logo_background_relativelayout = view.findViewById(R.id.logo_background_relativelayout);
        logo_image_view = view.findViewById(R.id.logo_image_view);
        edit_logo_image_textView = view.findViewById(R.id.edit_logo_image_textView);

        vendorBannerStorageReference = FirebaseStorage.getInstance().getReference().child("Users_data/vendors_banners/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        vendorLogoStorageReference = FirebaseStorage.getInstance().getReference().child("Users_data/vendors_logos/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        userDataRef = FirebaseDatabase.getInstance().getReference("Users_data/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        appRegNo_textView = view.findViewById(R.id.appRegNo_textView);
        name_textView = view.findViewById(R.id.name_textView);
        company_name_textView = view.findViewById(R.id.company_name_textView);
        company_reg_no_textView = view.findViewById(R.id.company_reg_no_textView);
        post_code_textView = view.findViewById(R.id.post_code_textView);
        vendor_address_textView = view.findViewById(R.id.customer_address_textView);
        vendor_phone_textView = view.findViewById(R.id.vendor_phone_textView);
        vendor_email_textView = view.findViewById(R.id.vendor_email_textView);
        vendor_url_textView = view.findViewById(R.id.vendor_url_textView);

        logo_imageView = view.findViewById(R.id.logo_imageView);
        banner_image_view = view.findViewById(R.id.banner_image_view);
        store_banner_imageView = view.findViewById(R.id.store_banner_imageView);

        view_users_btn = view.findViewById(R.id.view_users_btn);
        edit_vendor_details_btn_textView = view.findViewById(R.id.edit_vendor_details_btn_textView);
        change_passcode_alert_dialog = new Dialog(getActivity());
        change_passcode_alert_dialog.setContentView(R.layout.alert_vendor_change_passcode);
        change_passcode_cancel_btn = change_passcode_alert_dialog.findViewById(R.id.change_passcode_cancel_btn);
        change_passcode_submit_btn = change_passcode_alert_dialog.findViewById(R.id.change_passcode_submit_btn);
        random_passcode_editText = change_passcode_alert_dialog.findViewById(R.id.random_passcode_editText);
        new_passcode_edittext = change_passcode_alert_dialog.findViewById(R.id.new_passcode_edittext);
        new_passcode_again_edittext = change_passcode_alert_dialog.findViewById(R.id.new_passcode_again_edittext);
        edit_vendor_info_alert_dialog = new Dialog(getActivity());
        edit_vendor_info_alert_dialog.setContentView(R.layout.alert_vendor_details_edit);
        ccp = edit_vendor_info_alert_dialog.findViewById(R.id.ccp);
        editTextCarrierNumber = edit_vendor_info_alert_dialog.findViewById(R.id.editText_carrierNumber);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);
        edit_pastcode_editText = edit_vendor_info_alert_dialog.findViewById(R.id.edit_pastcode_editText);
        edit_address_editText = edit_vendor_info_alert_dialog.findViewById(R.id.edit_address_editText);
        edit_email_address_editText = edit_vendor_info_alert_dialog.findViewById(R.id.edit_email_address_editText);
        edit_url_address_editText = edit_vendor_info_alert_dialog.findViewById(R.id.edit_url_address_editText);
        change_passcode_btn = edit_vendor_info_alert_dialog.findViewById(R.id.change_passcode_btn);

        edit_vendor_details_cancel_btn = edit_vendor_info_alert_dialog.findViewById(R.id.edit_vendor_details_cancel_btn);
        edit_vendor_details_save_btn = edit_vendor_info_alert_dialog.findViewById(R.id.edit_vendor_details_save_btn);

        edit_banner_btn_textiew = view.findViewById(R.id.edit_banner_btn_textiew);

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private void datafetch() {
        name_textView.setText(UserDetails.getInstance().getName());
        vendor_address_textView.setText(UserDetails.getInstance().getVendorAddress());
        vendor_phone_textView.setText(UserDetails.getInstance().getVendorPhno());
        appRegNo_textView.setText(UserDetails.getInstance().getVendorAppRegNo());
        vendor_email_textView.setText(UserDetails.getInstance().getVendorEmail());
        company_name_textView.setText(UserDetails.getInstance().getVendorName());
        company_reg_no_textView.setText(UserDetails.getInstance().getVendorRegNo());
        post_code_textView.setText(UserDetails.getInstance().getVendorPostCode());
        vendor_url_textView.setText(UserDetails.getInstance().getVendorUrl());

        Picasso.get().load(String.valueOf(UserDetails.getInstance().getVendorLogo())).into(logo_imageView);
        Picasso.get().load(String.valueOf(UserDetails.getInstance().getVendorLogo())).into(logo_image_view);
        Picasso.get().load(String.valueOf(UserDetails.getInstance().getVendorBanner())).into(store_banner_imageView);
        Picasso.get().load(String.valueOf(UserDetails.getInstance().getVendorBanner())).into(banner_image_view);

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    private void onclicklistners() {
        view_users_btn_listner();
        edit_vendor_details_btn_listner();
        change_passcode_btn_listner();
        change_passcode_cancel_btn_listner();
        change_passcode_submit_btn_listner();
        edit_vendor_details_cancel_btn_listner();
        edit_vendor_details_save_btn_listner();
        edit_banner_btn_textiew_listener();
        edit_profilepic_btn_textiew_listener();
        store_banner_imageView_listner();
        banner_background_relativelayout_listner();
        logo_background_relativelayout_listner();
        logo_imageView_listner();

    }

    private void logo_imageView_listner() {
        logo_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logo_background_relativelayout.setVisibility(View.VISIBLE);
                logo_image_view.setVisibility(View.VISIBLE);
                edit_logo_image_textView.setVisibility(View.VISIBLE);

            }
        });
    }

    private void logo_background_relativelayout_listner() {
        logo_background_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logo_background_relativelayout.setVisibility(View.GONE);
                logo_image_view.setVisibility(View.GONE);
                edit_logo_image_textView.setVisibility(View.GONE);
            }
        });
    }

    private void banner_background_relativelayout_listner() {
        banner_background_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                banner_background_relativelayout.setVisibility(View.GONE);
                banner_image_view.setVisibility(View.GONE);
            }
        });
    }


    private void store_banner_imageView_listner() {
        store_banner_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                banner_background_relativelayout.setVisibility(View.VISIBLE);
                banner_image_view.setVisibility(View.VISIBLE);
            }
        });
    }

    private void edit_profilepic_btn_textiew_listener() {
        edit_logo_image_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
                // browser.
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                // Filter to only show results that can be "opened", such as a
                // file (as opposed to a list of contacts or timezones)
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                // Filter to show only images, using the image MIME data type.
                // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
                // To search for all documents available via installed storage providers,
                // it would be "*/*".
                intent.setType("image/*");

                getActivity().startActivityForResult(intent, LOGO_READ_REQUEST_CODE);
            }
        });
    }

    private void edit_banner_btn_textiew_listener() {
        edit_banner_btn_textiew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
                // browser.
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                // Filter to only show results that can be "opened", such as a
                // file (as opposed to a list of contacts or timezones)
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                // Filter to show only images, using the image MIME data type.
                // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
                // To search for all documents available via installed storage providers,
                // it would be "*/*".
                intent.setType("image/*");

                getActivity().startActivityForResult(intent, BANNER_READ_REQUEST_CODE);
            }
        });
    }

    private void edit_vendor_details_save_btn_listner() {
        edit_vendor_details_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo is ma country code picker bhi dalna ha
                if (validate_fields()) {

                    getFragmentManager().beginTransaction().replace(R.id.screenContainer, new VendorProfile()).commit();

                    edit_vendor_info_alert_dialog.dismiss();
                }
            }
        });
    }


    private boolean validate_fields() {
        boolean valid = true;

        String email = edit_address_editText.getText().toString().trim();

        if (edit_pastcode_editText.getText().toString().equals(post_code_textView.getText().toString())) {

        } else if (edit_pastcode_editText.getText().toString().equals("")) {
            edit_pastcode_editText.setError("Enter postcode");
            valid = false;
        } else {
            userDataRef.child("post_code").setValue(edit_pastcode_editText.getText().toString());
            Toast.makeText(getActivity(), "Yes post code edit complete", Toast.LENGTH_SHORT).show();
        }

        if (editTextCarrierNumber.getText().toString().equals(vendor_phone_textView.getText().toString())) {
        } else if (!ccp.isValidFullNumber()) {
            editTextCarrierNumber.setError("Please enter valid number");
            valid = false;
        } else {
            userDataRef.child("phno").setValue(String.valueOf(ccp.getFullNumberWithPlus()));
        }

        if (edit_address_editText.getText().toString().equals(vendor_address_textView.getText().toString())) {

        } else if (edit_address_editText.getText().toString().equals("")) {
            edit_address_editText.setError("Enter address");
            valid = false;
        } else {
            userDataRef.child("address").setValue(edit_address_editText.getText().toString());
        }

        if (edit_email_address_editText.getText().toString().equals(vendor_email_textView.getText().toString())) {

        } else if (edit_email_address_editText.getText().toString().equals("")) {
            edit_email_address_editText.setError("Enter email address");
            valid = false;
        } else if (email.matches(emailPattern)) {
            userDataRef.child("email").setValue(edit_email_address_editText.getText().toString());
        } else {
            edit_email_address_editText.setError("Enter valid email address");
            valid = false;
        }

        if (edit_url_address_editText.getText().toString().equals(vendor_url_textView.getText().toString())) {

        } else if (edit_url_address_editText.getText().toString().equals("")) {
            edit_url_address_editText.setError("Enter Website URL");
            valid = false;
        } else if (URLUtil.isValidUrl(edit_url_address_editText.getText().toString())) {
            userDataRef.child("url").setValue(edit_url_address_editText.getText().toString());
        } else {
            edit_url_address_editText.setError("Enter valid URL");
            valid = false;
        }


        return valid;
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
        edit_vendor_details_btn_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_vendor_info_alert_dialog.show();
                edit_pastcode_editText.setText(post_code_textView.getText().toString());
                edit_address_editText.setText(vendor_address_textView.getText().toString());
                editTextCarrierNumber.setText(vendor_phone_textView.getText().toString());
                edit_email_address_editText.setText(vendor_email_textView.getText().toString());
                edit_url_address_editText.setText(vendor_url_textView.getText().toString());
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

                if (validate_change_passcode()) {
                    edit_vendor_info_alert_dialog.dismiss();
                    change_passcode_alert_dialog.dismiss();
                }
            }
        });
    }

    private boolean validate_change_passcode() {
        boolean valid = true;
        if (random_passcode_editText.getText().toString().equals("")) {
            random_passcode_editText.setError("Enter old passcode");
            valid = false;
        }
        //TODo passcode ko online kar kay validate karna ha online kay sath
        if (new_passcode_edittext.getText().toString().equals("")) {
            new_passcode_edittext.setError("Enter new passcode");
            valid = false;
        }

        if (!new_passcode_edittext.getText().toString().equals(new_passcode_again_edittext.getText().toString())) {
            new_passcode_again_edittext.setError("New Passcode not match");
            valid = false;
        }
        if (new_passcode_edittext.getText().toString().length() < 4) {
            new_passcode_edittext.setError("Enter complete passcode");
            valid = false;
        }
        return valid;
    }

    private void view_users_btn_listner() {
        view_users_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Vendor_Users_list.class);
                startActivity(intent);
            }
        });
    }

    private void reloadingFragment() {
        Fragment currentFragment = getParentFragmentManager().findFragmentByTag("VENDOR_PROFILE_FRAGMENT");
        androidx.fragment.app.FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();
    }

    ///////////////////////

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
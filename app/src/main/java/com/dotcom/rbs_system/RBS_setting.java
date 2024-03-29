package com.dotcom.rbs_system;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterSettingsFaultListRecyclerView;
import com.dotcom.rbs_system.Classes.ActionBarTitle;
import com.dotcom.rbs_system.Classes.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RBS_setting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RBS_setting extends Fragment {

    View view;

    String port_value, ip_address;

    public static final String PRINTER_SETTINGS = "Printer_Settings";
    public static final String Printer_Port_Number = "printer_port_number";
    public static final String Printer_Ip_Address = "printer_ip_address";

    DatabaseReference faultListRef, reference, shopKeeperDataRef, rbsMessageRef;
    StorageReference storageReference;

    private Uri logoDocUri = null;
    private Uri bannerDocUri = null;

    private static final int LOGO_READ_REQUEST_CODE = 42;
    private static final int BANNER_READ_REQUEST_CODE = 43;

    RelativeLayout banner_background_relativelayout, logo_background_relativelayout;

    ImageView logo_imageView, banner_imageView, banner_image_view, logo_image_view;

    TextView edit_banner_textview, add_faults_textview, port_number_textview, ip_address_textview, edit_logo_image_textView;

    TextView save_passcode_change_textview,cancel_passcode_change_textview,passcode_change_textview, addFaultsave_textview, addFaultsCancel_textview, save_printer_settings_textview, cancel_printer_settings_textview, cancel_terms_conditions_textview, save_terms_conditions_textview, edit_terms_conditions_textview;

    TextView printer_setting_textview, terms_conditions_textview;

    RecyclerView faultList_recyclerView;

    AdapterSettingsFaultListRecyclerView adapterSettingsFaultListRecyclerView;

    Dialog addFaultDialog, editProfileDialog, edit_printer_settings_Dialog, editTermsConditionsDialog,change_passcode_Dialog;

    EditText alertFaultName_editText, alertFaultPrice_editText, port_number_edittext, ip_address_edittext, term_conditions_edittext,old_passcode_edittext,new_passcode_edittext,new_passcode_again_edittext;

    List<String> faultNameList, faultPriceList, faultKeyIDList;

    TextView shop_name_textView, shop_email_textView, shop_phno_textView, shop_address_textView;
    TextView rbsMessage_textView;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public RBS_setting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RBS_setting.
     */
    public static RBS_setting newInstance(String param1, String param2) {
        RBS_setting fragment = new RBS_setting();
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

        view = inflater.inflate(R.layout.fragment_rbs_setting, container, false);

        ActionBarTitle.getInstance().getTextView().setText("RBS Settings");

        initialize();
        initialOperations();
        onClickListeners();
        // Inflate the layout for this fragment
        return view;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void initialize() {
        storageReference = FirebaseStorage.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference();
        shopKeeperDataRef = FirebaseDatabase.getInstance().getReference("Users_data/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Shopkeeper_details");
        faultListRef = FirebaseDatabase.getInstance().getReference("Listed_faults/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        rbsMessageRef = FirebaseDatabase.getInstance().getReference("Admin");


        banner_background_relativelayout = view.findViewById(R.id.banner_background_relativelayout);
        logo_background_relativelayout = view.findViewById(R.id.logo_background_relativelayout);

        logo_imageView = view.findViewById(R.id.logo_imageView);
        banner_imageView = view.findViewById(R.id.banner_imageView);
        banner_image_view = view.findViewById(R.id.banner_image_view);
        logo_image_view = view.findViewById(R.id.logo_image_view);

        add_faults_textview = view.findViewById(R.id.add_faults_textview);
        edit_logo_image_textView = view.findViewById(R.id.edit_logo_image_textView);
        edit_banner_textview = view.findViewById(R.id.edit_banner_textview);
        printer_setting_textview = view.findViewById(R.id.printer_setting_textview);
        terms_conditions_textview = view.findViewById(R.id.terms_conditions_textview);


        faultNameList = new ArrayList<>();
        faultPriceList = new ArrayList<>();
        faultKeyIDList = new ArrayList<>();

        faultList_recyclerView = view.findViewById(R.id.faultList_recyclerView);

        addFaultDialog = new Dialog(getActivity());
        change_passcode_Dialog = new Dialog(getActivity());
        edit_printer_settings_Dialog = new Dialog(getActivity());
        editTermsConditionsDialog = new Dialog(getActivity());
        addFaultDialog.setContentView(R.layout.alert_setting_add_fault);
        change_passcode_Dialog.setContentView(R.layout.change_passcode_alert);
        old_passcode_edittext=change_passcode_Dialog.findViewById(R.id.old_passcode_edittext);
        new_passcode_edittext=change_passcode_Dialog.findViewById(R.id.new_passcode_edittext);
        new_passcode_again_edittext=change_passcode_Dialog.findViewById(R.id.new_passcode_again_edittext);
        save_passcode_change_textview=change_passcode_Dialog.findViewById(R.id.save_passcode_change_textview);
        cancel_passcode_change_textview=change_passcode_Dialog.findViewById(R.id.cancel_passcode_change_textview);
        edit_printer_settings_Dialog.setContentView(R.layout.printer_settings_alert);
        editTermsConditionsDialog.setContentView(R.layout.edit_terms_conditions_alert);
        alertFaultName_editText = addFaultDialog.findViewById(R.id.alertFaultName_editText);
        alertFaultPrice_editText = addFaultDialog.findViewById(R.id.alertFaultPrice_editText);
        port_number_edittext = view.findViewById(R.id.port_number_edittext);
        addFaultsave_textview = addFaultDialog.findViewById(R.id.addFaultsave_textview);
        passcode_change_textview =view.findViewById(R.id.passcode_change_textview);
        addFaultsCancel_textview = addFaultDialog.findViewById(R.id.addFaultsCancel_textview);
        port_number_edittext = edit_printer_settings_Dialog.findViewById(R.id.port_number_edittext);
        term_conditions_edittext = editTermsConditionsDialog.findViewById(R.id.term_conditions_edittext);
        ip_address_edittext = edit_printer_settings_Dialog.findViewById(R.id.ip_address_edittext);
        save_printer_settings_textview = edit_printer_settings_Dialog.findViewById(R.id.save_printer_settings_textview);
        cancel_printer_settings_textview = edit_printer_settings_Dialog.findViewById(R.id.cancel_printer_settings_textview);
        cancel_terms_conditions_textview = editTermsConditionsDialog.findViewById(R.id.cancel_terms_conditions_textview);
        save_terms_conditions_textview = editTermsConditionsDialog.findViewById(R.id.save_terms_conditions_textview);
        port_number_textview = view.findViewById(R.id.port_number_textview);
        ip_address_textview = view.findViewById(R.id.ip_address_textview);
        edit_terms_conditions_textview = view.findViewById(R.id.edit_terms_conditions_textview);

        SharedPreferences preferences = getActivity().getSharedPreferences(RBS_setting.PRINTER_SETTINGS, MODE_PRIVATE);
        port_value = preferences.getString(RBS_setting.Printer_Port_Number, "");
        ip_address = preferences.getString(RBS_setting.Printer_Ip_Address, "");

        port_number_textview.setText(String.valueOf(port_value));

        ip_address_textview.setText(String.valueOf(ip_address));

        editProfileDialog = new Dialog(getActivity());

        shop_name_textView = view.findViewById(R.id.shop_name_textView);
        shop_email_textView = view.findViewById(R.id.shop_email_textView);
        shop_phno_textView = view.findViewById(R.id.shop_phno_textView);
        shop_address_textView = view.findViewById(R.id.shop_address_textView);


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void initialOperations() {
        checkUserData();
        getRBSMessage();
        getFaultsList();
    }

    private void checkUserData() {
        Picasso.get().load(UserDetails.getInstance().getShopLogo()).into(logo_imageView);
        Picasso.get().load(UserDetails.getInstance().getShopBanner()).into(banner_imageView);
        Picasso.get().load(UserDetails.getInstance().getShopLogo()).into(logo_image_view);
        Picasso.get().load(UserDetails.getInstance().getShopBanner()).into(banner_image_view);

        shop_name_textView.setText(UserDetails.getInstance().getShopName());
        shop_address_textView.setText(UserDetails.getInstance().getShopAddress());
        shop_email_textView.setText(UserDetails.getInstance().getShopEmail());
        shop_phno_textView.setText(UserDetails.getInstance().getShopPhno());
        terms_conditions_textview.setText(UserDetails.getInstance().getShopTermsAndConditions());

        term_conditions_edittext.setText(terms_conditions_textview.getText());

    }

    private void getRBSMessage() {
        rbsMessageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("rbs_message").exists()) {
                    rbsMessage_textView.setText(dataSnapshot.child("rbs_message").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFaultsList() {
        faultListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    faultNameList.add(String.valueOf(dataSnapshot1.child("Fault_name").getValue()));
                    faultPriceList.add(String.valueOf(dataSnapshot1.child("Fault_price").getValue()));
                    faultKeyIDList.add(String.valueOf(dataSnapshot1.child("key_id").getValue()));
                }

                adapterSettingsFaultListRecyclerView = new AdapterSettingsFaultListRecyclerView(getActivity(), faultNameList, faultPriceList, faultKeyIDList);
                faultList_recyclerView.setAdapter(adapterSettingsFaultListRecyclerView);
                faultList_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void onClickListeners() {

        cancel_passcode_change_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_passcode_Dialog.dismiss();
            }
        });

        save_passcode_change_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passcode_validations()){
                    Toast.makeText(getActivity(), "Passcode Changed", Toast.LENGTH_SHORT).show();
                    change_passcode_Dialog.dismiss();
                }
            }
        });

        passcode_change_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_passcode_Dialog.show();
            }
        });

        save_terms_conditions_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTermsAndConditions();
            }

        });
        cancel_terms_conditions_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTermsConditionsDialog.dismiss();
            }
        });

        edit_terms_conditions_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTermsConditionsDialog.show();
            }
        });

        edit_logo_image_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        logo_background_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logo_background_relativelayout.setVisibility(View.GONE);
                logo_image_view.setVisibility(View.GONE);
                edit_logo_image_textView.setVisibility(View.GONE);
            }
        });

        logo_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logo_background_relativelayout.setVisibility(View.VISIBLE);
                logo_image_view.setVisibility(View.VISIBLE);
                edit_logo_image_textView.setVisibility(View.VISIBLE);

            }
        });


        banner_background_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                banner_background_relativelayout.setVisibility(View.GONE);
                banner_image_view.setVisibility(View.GONE);
            }
        });

        banner_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                banner_background_relativelayout.setVisibility(View.VISIBLE);
                banner_image_view.setVisibility(View.VISIBLE);
            }
        });

        cancel_printer_settings_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_printer_settings_Dialog.dismiss();
            }
        });

        save_printer_settings_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), String.valueOf(port_number_edittext.getText().toString()), Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = getActivity().getSharedPreferences(PRINTER_SETTINGS, MODE_PRIVATE).edit();

                editor.putString(Printer_Port_Number, String.valueOf(port_number_edittext.getText().toString()));
                editor.putString(Printer_Ip_Address, String.valueOf(ip_address_edittext.getText().toString()));
                editor.apply();

                port_number_textview.setText(String.valueOf(port_number_edittext.getText().toString()));
                ip_address_textview.setText(String.valueOf(ip_address_edittext.getText().toString()));

                edit_printer_settings_Dialog.dismiss();

            }
        });

        printer_setting_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                port_number_edittext.setText(port_number_textview.getText());

                ip_address_edittext.setText(ip_address_textview.getText());
                edit_printer_settings_Dialog.show();
            }
        });

        add_faults_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFaultDialog.show();
            }
        });

        addFaultsave_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAlertAddFault()) {
                    detailsSubmitAlertAddFault();
                }

            }
        });

        addFaultsCancel_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFaultDialog.dismiss();
            }
        });


        edit_banner_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private boolean passcode_validations() {
        boolean valid=true;
        if (old_passcode_edittext.getText().toString().equals("")){
            old_passcode_edittext.setError("Enter old passcode");
            valid=false;
        }
        //TODo passcode ko online kar kay validate karna ha online kay sath
        if (new_passcode_edittext.getText().toString().equals("")){
            new_passcode_edittext.setError("Enter new passcode");
            valid=false;
        }

        if (!new_passcode_edittext.getText().toString().equals(new_passcode_again_edittext.getText().toString())){
            new_passcode_again_edittext.setError("New Passcode not match");
            valid=false;
        }
        if (new_passcode_edittext.getText().toString().length()<4){
            new_passcode_edittext.setError("Enter complete passcode");
            valid=false;
        }

        return valid;
    }

    private void updateTermsAndConditions() {
        if (terms_conditions_textview.getText().toString().equals(term_conditions_edittext.getText().toString())) {
            term_conditions_edittext.setError("Edit to save!");
        } else {
            shopKeeperDataRef.child("shop_termsandconditions").setValue(term_conditions_edittext.getText().toString());
            UserDetails.getInstance().setShopTermsAndConditions(term_conditions_edittext.getText().toString());
            terms_conditions_textview.setText(term_conditions_edittext.getText().toString());

            editTermsConditionsDialog.dismiss();
        }
    }

    private boolean validateAlertAddFault() {
        boolean valid = true;

        if (alertFaultName_editText.getText().toString().isEmpty()) {
            alertFaultName_editText.setError("enter fault name");
            valid = false;
        }

        if (alertFaultPrice_editText.getText().toString().isEmpty()) {
            alertFaultPrice_editText.setError("Please enter price");
            valid = false;

        }else {
            if (Float.parseFloat(alertFaultPrice_editText.getText().toString())==0){
                alertFaultPrice_editText.setError("Please enter valid price");
            }else {
                alertFaultPrice_editText.setText(alertFaultPrice_editText.getText().toString().replaceFirst("^0+(?!$)",""));
                if (alertFaultPrice_editText.getText().toString().startsWith(".")){
                    alertFaultPrice_editText.setText("0"+ alertFaultPrice_editText.getText().toString());
                }
            }
        }

        return valid;
    }

    private void detailsSubmitAlertAddFault() {
        String key = faultListRef.push().getKey();

        faultListRef.child(key).child("Fault_name").setValue(alertFaultName_editText.getText().toString());
        faultListRef.child(key).child("Fault_price").setValue(alertFaultPrice_editText.getText().toString());
        faultListRef.child(key).child("added_by").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        faultListRef.child(key).child("key_id").setValue(key);

        faultNameList.add(alertFaultName_editText.getText().toString());
        faultKeyIDList.add(key);
        faultPriceList.add(alertFaultPrice_editText.getText().toString());

        adapterSettingsFaultListRecyclerView.notifyDataSetChanged();
        addFaultDialog.dismiss();
    }

}
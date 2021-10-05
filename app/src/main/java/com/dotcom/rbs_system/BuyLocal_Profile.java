package com.dotcom.rbs_system;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.AdapterBuylocalCustomerHistoryListRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterCustomerHistoryListRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterCustomerIDImagesRecyclerView;
import com.dotcom.rbs_system.Classes.UserDetails;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BuyLocal_Profile extends Fragment {

    RelativeLayout alert_background_relativelayout;

    List<String> shopkeeper_name_textview, item_name_textview, item_category_textview, shopkeeperImage_imageView_list, dateList, itemKeyId, itemImageView, shopkeeper_key_id, serial_no_textview;

    TextView name, phno, email, address, creationDate_textView, edit_textView, edit_image_textView,numberOfPurchases_textView;

    ImageView profileImage_imageView, edit_image_image_view;

    RecyclerView customer_history_recyclerview;

    AdapterBuylocalCustomerHistoryListRecyclerView adapterBuylocalCustomerHistoryListRecyclerView;

    DatabaseReference Customer_purchase_history_ref;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public BuyLocal_Profile() {

    }

    public static BuyLocal_Profile newInstance(String param1, String param2) {
        BuyLocal_Profile fragment = new BuyLocal_Profile();
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

        View view = inflater.inflate(R.layout.fragment_buylocal_profile, container, false);

        customer_history_recyclerview = view.findViewById(R.id.customer_history_recyclerview);
        customer_history_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        profileImage_imageView = view.findViewById(R.id.profileImage_imageView);
        edit_image_textView = view.findViewById(R.id.edit_image_textView);
        numberOfPurchases_textView = view.findViewById(R.id.numberOfPurchases_textView);
        alert_background_relativelayout = view.findViewById(R.id.alert_background_relativelayout);
        edit_image_image_view = view.findViewById(R.id.edit_image_image_view);
        name = view.findViewById(R.id.name);
        phno = view.findViewById(R.id.phno);
        address = view.findViewById(R.id.address);

        dateList = new ArrayList<>();
        itemImageView = new ArrayList<>();
        itemKeyId = new ArrayList<>();
        item_name_textview = new ArrayList<>();
        item_category_textview = new ArrayList<>();
        serial_no_textview = new ArrayList<>();
        shopkeeperImage_imageView_list = new ArrayList<>();
        shopkeeper_key_id = new ArrayList<>();
        shopkeeper_name_textview = new ArrayList<>();

        creationDate_textView = view.findViewById(R.id.creationDate_textView);
        email = view.findViewById(R.id.email);
        edit_textView = view.findViewById(R.id.edit_textView);

        Customer_purchase_history_ref = FirebaseDatabase.getInstance().getReference("Customer_purchase_history/"+FirebaseAuth.getInstance().getCurrentUser().getUid());

        edit_image_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        InitialProcess();

        onclicklistners();

        datafetch();

        return view;
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void InitialProcess() {
        getPurchaseHistoryList();
    }

    private void getPurchaseHistoryList() {
        Customer_purchase_history_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    dateList.add(snapshot1.child("date").getValue().toString());
                    itemImageView.add(snapshot1.child("item_image").getValue().toString());
                    itemKeyId.add(snapshot1.child("item_keyId").getValue().toString());
                    item_name_textview.add(snapshot1.child("item_name").getValue().toString());
                    item_category_textview.add(snapshot1.child("item_category").getValue().toString());
                    serial_no_textview.add(snapshot1.child("item_serialno").getValue().toString());
                    shopkeeperImage_imageView_list.add(snapshot1.child("shopkeeper_image").getValue().toString());
                    shopkeeper_key_id.add(snapshot1.child("shopkeeper_keyId").getValue().toString());
                    shopkeeper_name_textview.add(snapshot1.child("shopkeeper_name").getValue().toString());
                }

                numberOfPurchases_textView.setText(String.valueOf(item_name_textview.size()));
                adapterBuylocalCustomerHistoryListRecyclerView = new AdapterBuylocalCustomerHistoryListRecyclerView(getActivity(), dateList, item_category_textview, itemImageView, itemKeyId, item_name_textview, serial_no_textview, shopkeeperImage_imageView_list, shopkeeper_key_id, shopkeeper_name_textview);
                customer_history_recyclerview.setAdapter(adapterBuylocalCustomerHistoryListRecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void onclicklistners() {
        edit_btn_listner();
        alert_background_relativelayout_listner();
        profileImage_imageView_listner();
        edit_image_textView_listener();
    }

    private void alert_background_relativelayout_listner() {
        alert_background_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_background_relativelayout.setVisibility(View.GONE);
                edit_image_image_view.setVisibility(View.GONE);
                edit_image_textView.setVisibility(View.GONE);
            }
        });
    }

    private void profileImage_imageView_listner() {
        profileImage_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert_background_relativelayout.setVisibility(View.VISIBLE);
                edit_image_image_view.setVisibility(View.VISIBLE);
                edit_image_textView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void edit_image_textView_listener() {
        edit_image_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(getActivity())
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(333);
            }
        });
    }

    private void edit_btn_listner() {
        edit_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BuyLocal_profile_edit.class);
                startActivityForResult(intent, 2);
            }
        });
    }

    private void datafetch() {

        name.setText(UserDetails.getInstance().getName());
        address.setText(UserDetails.getInstance().getAddress());
        phno.setText(UserDetails.getInstance().getPhno());
        email.setText(UserDetails.getInstance().getEmail());

        SimpleDateFormat sfd = new SimpleDateFormat("dd-MMMM-yyyy ");
        creationDate_textView.setText(sfd.format(new Date(FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp())));

        Picasso.get().load(UserDetails.getInstance().getProfileImageUrl()).into(profileImage_imageView);
        Picasso.get().load(UserDetails.getInstance().getProfileImageUrl()).into(edit_image_image_view);

    }


}
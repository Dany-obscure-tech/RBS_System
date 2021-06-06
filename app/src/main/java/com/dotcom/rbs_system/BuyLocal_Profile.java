package com.dotcom.rbs_system;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    DatabaseReference customerdataRef,customer_offerRef;

    RelativeLayout alert_background_relativelayout;



    TextView name,dob,phno,email,address,creationDate_textView,edit_textView,edit_image_textView;

    ImageView idImage,profileImage_imageView,edit_image_image_view;

    List<String> price, itemImage;
    List<String> offer_status;
    List<String> offer_product_price;
    List<String> product_offer_msg;

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

        customerdataRef = FirebaseDatabase.getInstance().getReference("Users_data");
        customer_offerRef = FirebaseDatabase.getInstance().getReference("Customer_offers");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buylocal_profile, container, false);

        profileImage_imageView=(ImageView)view.findViewById(R.id.profileImage_imageView);
        edit_image_textView=(TextView) view.findViewById(R.id.edit_image_textView);
        alert_background_relativelayout=(RelativeLayout) view.findViewById(R.id.alert_background_relativelayout);
        edit_image_image_view=(ImageView)view.findViewById(R.id.edit_image_image_view);
        idImage=(ImageView)view.findViewById(R.id.idImage);
        name=(TextView)view.findViewById(R.id.name);
        phno=(TextView)view.findViewById(R.id.phno);
        address=(TextView)view.findViewById(R.id.address);
        creationDate_textView=(TextView)view.findViewById(R.id.creationDate_textView);
        dob=(TextView)view.findViewById(R.id.dob);
        email=(TextView)view.findViewById(R.id.email);
        edit_textView=(TextView) view.findViewById(R.id.edit_textView);
        price = new ArrayList<String>();
        itemImage = new ArrayList<String>();
        offer_status = new ArrayList<String>();
        offer_product_price = new ArrayList<String>();
        product_offer_msg = new ArrayList<String>();

        edit_image_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });




        onclicklistners();

        offerListFetch();
        datafetch();

        return view;
    }

    private void onclicklistners() {
        edit_btn_listner();
        alert_background_relativelayout_listner();
        profileImage_imageView_listner();
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

    private void edit_btn_listner() {
        edit_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),BuyLocal_profile_edit.class);
                startActivity(intent);
            }
        });
    }

    private void datafetch() {
        customerdataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    name.setText(dataSnapshot.child("fullname").getValue().toString());
                    address.setText(dataSnapshot.child("address").getValue().toString());
                    phno.setText(dataSnapshot.child("contactno").getValue().toString());
                    dob.setText(dataSnapshot.child("dob").getValue().toString());
                    email.setText(dataSnapshot.child("email").getValue().toString());

                    SimpleDateFormat sfd = new SimpleDateFormat("dd-MMMM-yyyy ");
                    creationDate_textView.setText(String.valueOf(sfd.format(new Date(FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp()))));

                    Picasso.get().load(String.valueOf(dataSnapshot.child("profile_image_url").getValue().toString())).into(profileImage_imageView);
                    Picasso.get().load(String.valueOf(dataSnapshot.child("profile_image_url").getValue().toString())).into(edit_image_image_view);
                    Picasso.get().load(String.valueOf(dataSnapshot.child("id_image_url").getValue().toString())).into(idImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void offerListFetch() {
        customer_offerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
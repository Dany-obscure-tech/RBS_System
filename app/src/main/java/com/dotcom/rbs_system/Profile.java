package com.dotcom.rbs_system;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Fragment {

    DatabaseReference customerdataRef;

    TextView name,dob,phno,email,address;

    ImageView profileImage;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public Profile() {

    }

    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage=(ImageView)view.findViewById(R.id.menu_btn);
        name=(TextView)view.findViewById(R.id.name);
        phno=(TextView)view.findViewById(R.id.phno);
        address=(TextView)view.findViewById(R.id.address);
        dob=(TextView)view.findViewById(R.id.dob);
        email=(TextView)view.findViewById(R.id.email);

        datafetch();

        return view;
    }

    private void datafetch() {
        customerdataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    name.setText(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("fullname").getValue().toString());
                    address.setText(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("address").getValue().toString());
                    phno.setText(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("contactno").getValue().toString());
                    dob.setText(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("dob").getValue().toString());
                    email.setText(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
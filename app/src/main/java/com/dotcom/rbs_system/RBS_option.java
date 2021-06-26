package com.dotcom.rbs_system;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dotcom.rbs_system.Classes.ActionBarTitle;
import com.dotcom.rbs_system.Classes.Exchanged_itemdata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RBS_option#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RBS_option extends Fragment {

    View view;

    DatabaseReference bannerRef;

    CardView BUY,Sale,repair,Exchange,Accessories,Settings;

    ImageButton icon1,icon2,icon3,icon4,icon8;
    Button alert_addAccessory_btn,alert_saleAccessory_btn;

    ImageView mainBanner_imageView;

    Dialog selectAccessory_dialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RBS_option() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RBS_option.
     */
    // TODO: Rename and change types and number of parameters
    public static RBS_option newInstance(String param1, String param2) {
        RBS_option fragment = new RBS_option();
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
        view= inflater.inflate(R.layout.fragment_rbs_option, container, false);

        ActionBarTitle.getInstance().getTextView().setText("POS");

        bannerRef = FirebaseDatabase.getInstance().getReference("Admin/banner");

        selectAccessory_dialog = new Dialog(getActivity());
        selectAccessory_dialog.setContentView(R.layout.alert_select_accessory_screen);

        alert_addAccessory_btn = (Button) selectAccessory_dialog.findViewById(R.id.alert_addAccessory_btn);
        alert_saleAccessory_btn = (Button) selectAccessory_dialog.findViewById(R.id.alert_saleAccessory_btn);

        BUY=(CardView)view.findViewById(R.id.BUY);

        Sale=(CardView)view.findViewById(R.id.Sale);
        repair=(CardView)view.findViewById(R.id.repair);
        Exchange=(CardView)view.findViewById(R.id.Exchange);
        Accessories=(CardView)view.findViewById(R.id.Accessories);
        Settings=(CardView)view.findViewById(R.id.Settings);

        icon2=(ImageButton)view.findViewById(R.id.icon2);
        icon4=(ImageButton)view.findViewById(R.id.icon4);
        icon1=(ImageButton)view.findViewById(R.id.icon1);
        icon3=(ImageButton)view.findViewById(R.id.icon3);
        icon8=(ImageButton)view.findViewById(R.id.icon8);

        mainBanner_imageView = (ImageView)view.findViewById(R.id.mainBanner_imageView);
        getBannerImage();

        Accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAccessory_dialog.show();
            }
        });

        Sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Sale.class);
                intent.putExtra("ITEM_SELL_CHECK","FALSE");
                startActivity(intent);
            }
        });

        icon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAccessory_dialog.show();
            }
        });

        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Repair_Ticket.class);
                startActivity(intent);
            }
        });

        BUY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Buy.class);
                Exchanged_itemdata.getInstance().setExchangeCheck(false);
                Exchanged_itemdata.getInstance().setExchangeFromBuyCheck(false);
                Exchanged_itemdata.getInstance().clearData();

                startActivity(intent);
            }
        });
        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Buy.class);
                Exchanged_itemdata.getInstance().setExchangeCheck(false);
                Exchanged_itemdata.getInstance().setExchangeFromBuyCheck(false);
                Exchanged_itemdata.getInstance().clearData();
                startActivity(intent);
            }
        });
        Sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Sale.class);
                intent.putExtra("ITEM_SELL_CHECK","FALSE");
                Exchanged_itemdata.getInstance().setExchangeCheck(false);
                Exchanged_itemdata.getInstance().setExchangeFromBuyCheck(false);
                Exchanged_itemdata.getInstance().clearData();
                startActivity(intent);
            }
        });
        icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Sale.class);
                intent.putExtra("ITEM_SELL_CHECK","FALSE");
                Exchanged_itemdata.getInstance().setExchangeCheck(false);
                Exchanged_itemdata.getInstance().setExchangeFromBuyCheck(false);
                Exchanged_itemdata.getInstance().clearData();
                startActivity(intent);
            }
        });
        repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Repair_Ticket.class);
                startActivity(intent);
            }
        });

        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), RBS_settings.class);
                startActivity(intent);
            }
        });

        alert_addAccessory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Accessory_add.class);
                startActivity(intent);
                selectAccessory_dialog.dismiss();
            }
        });
        alert_saleAccessory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Accessory_sale.class);
                startActivity(intent);
                selectAccessory_dialog.dismiss();
            }
        });

        icon8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),RBS_Vendors.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getBannerImage() {
        bannerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Picasso.get().load(dataSnapshot.getValue().toString()).into(mainBanner_imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
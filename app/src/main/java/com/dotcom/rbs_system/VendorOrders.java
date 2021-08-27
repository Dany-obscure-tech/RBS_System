package com.dotcom.rbs_system;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dotcom.rbs_system.Adapter.Adapter_Vendor_order_list_RecyclerView;
import com.dotcom.rbs_system.Classes.ActionBarTitle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VendorOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VendorOrders extends Fragment {

    View view;
    DatabaseReference Vendor_orderRef;
    RecyclerView orders_list_recyclerview;
    List<String> order_no_vendor;
    List<String> shop_name;
    List<String> date;
    List<String> totalAmount;
    List<String> vendor_order_status;
    List<String> shopkeeper_image;
    List<String> shopKeeper_keyID;
    List<String> invoiceKeyID;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public VendorOrders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VendorOrders.
     */
    public static VendorOrders newInstance(String param1, String param2) {
        VendorOrders fragment = new VendorOrders();
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
        view = inflater.inflate(R.layout.fragment_vendor_orders, container, false);
        ActionBarTitle.getInstance().getTextView().setText("Vendor Orders");
        initialization();
        initialProcess();
        onclicklistners();


        return view;
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    private void initialization() {

        Vendor_orderRef = FirebaseDatabase.getInstance().getReference("Vendor_order/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        orders_list_recyclerview = view.findViewById(R.id.orders_list_recyclerview);
        order_no_vendor = new ArrayList<>();
        shop_name = new ArrayList<>();
        date = new ArrayList<>();
        totalAmount = new ArrayList<>();
        vendor_order_status = new ArrayList<>();
        shopkeeper_image = new ArrayList<>();
        shopKeeper_keyID = new ArrayList<>();
        invoiceKeyID = new ArrayList<>();

    }

    ///////////////////////////////////////////////////////////////////////////////////////

    private void initialProcess() {
        datafetch();
    }

    private void datafetch() {
        Vendor_orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    shop_name.add(snapshot1.child("shopkeeper_name").getValue().toString());
                    order_no_vendor.add(snapshot1.child("invoice_no").getValue().toString());
                    date.add(snapshot1.child("date").getValue().toString());
                    totalAmount.add(snapshot1.child("totalBalance").getValue().toString());
                    vendor_order_status.add(snapshot1.child("status").getValue().toString());
                    shopkeeper_image.add(snapshot1.child("shopkeeper_imageUrl").getValue().toString());
                    shopKeeper_keyID.add(snapshot1.child("shopkeeper_keyID").getValue().toString());
                    invoiceKeyID.add(snapshot1.child("invoice_keyId").getValue().toString());
                }

                Adapter_Vendor_order_list_RecyclerView adapter_vendor_order_list_recyclerView = new Adapter_Vendor_order_list_RecyclerView(getActivity(), order_no_vendor, shop_name, date, totalAmount, vendor_order_status, shopkeeper_image,shopKeeper_keyID, invoiceKeyID);
                orders_list_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                orders_list_recyclerview.setAdapter(adapter_vendor_order_list_recyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    private void onclicklistners() {

    }


}
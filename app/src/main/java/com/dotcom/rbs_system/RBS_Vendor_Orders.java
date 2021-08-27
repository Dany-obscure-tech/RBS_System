package com.dotcom.rbs_system;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.Adapter_RBS_Vendor_orders_list_RecyclerView;
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
 * Use the {@link RBS_Vendor_Orders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RBS_Vendor_Orders extends Fragment {
    DatabaseReference Vendor_orderRef;
    RecyclerView rbs_vendor_orders_recyclerview;
    View view;
    List<String> order_no_vendor;
    List<String> vendor_name;
    List<String> date;
    List<String> totalAmount;
    List<String> vendor_order_status;
    List<String> vendor_image;
    List<String> vendor_keyID;
    List<String> invoiceKeyID;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public RBS_Vendor_Orders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RBS_Vendor_Orders.
     */
    public static RBS_Vendor_Orders newInstance(String param1, String param2) {
        RBS_Vendor_Orders fragment = new RBS_Vendor_Orders();
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
        view = inflater.inflate(R.layout.fragment_rbs_vendor_orders, container, false);
        ActionBarTitle.getInstance().getTextView().setText("Vendor Orders");
        initialization();
        return view;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void initialization() {
        Vendor_orderRef = FirebaseDatabase.getInstance().getReference("Shopkeeper_vendor_order/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        rbs_vendor_orders_recyclerview = view.findViewById(R.id.rbs_vendor_orders_recyclerview);
        order_no_vendor = new ArrayList<>();
        vendor_name = new ArrayList<>();
        date = new ArrayList<>();
        totalAmount = new ArrayList<>();
        vendor_order_status = new ArrayList<>();
        vendor_image = new ArrayList<>();
        vendor_keyID = new ArrayList<>();
        invoiceKeyID = new ArrayList<>();

        initialProcess();
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
                    vendor_name.add(snapshot1.child("vendor_name").getValue().toString());
                    order_no_vendor.add(snapshot1.child("invoice_no").getValue().toString());
                    date.add(snapshot1.child("date").getValue().toString());
                    totalAmount.add(snapshot1.child("totalBalance").getValue().toString());
                    vendor_order_status.add(snapshot1.child("status").getValue().toString());
                    vendor_image.add(snapshot1.child("vendor_imageUrl").getValue().toString());
                    vendor_keyID.add(snapshot1.child("vendor_keyID").getValue().toString());
                    invoiceKeyID.add(snapshot1.child("invoice_keyId").getValue().toString());
                }

                Adapter_RBS_Vendor_orders_list_RecyclerView adapter_rbs_vendor_orders_list_recyclerView = new Adapter_RBS_Vendor_orders_list_RecyclerView(getActivity(), order_no_vendor, vendor_name, date, totalAmount, vendor_order_status, vendor_image, vendor_keyID, invoiceKeyID);
                rbs_vendor_orders_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                rbs_vendor_orders_recyclerview.setAdapter(adapter_rbs_vendor_orders_list_recyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void onclicklistners() {

    }
}
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
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.Adapter_Vendor_inventory_RecyclerView;
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
 * Use the {@link VendorShop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VendorShop extends Fragment {

    Adapter_Vendor_inventory_RecyclerView adapter_vendor_inventory_recyclerView;

    View view;

    RecyclerView vendor_inventory_RecyclerView;

    List<String> stockName_list;
    List<String> stockCategory_list;
    List<String> stockPrice_list;
    List<String> stockQuantity_list;
    List<String> stockImageUrl_list;
    List<String> stockkeyId_list;
    List<String> edit_btn_list;
    TextView vendor_inventory_add_btn_textiew;


    DatabaseReference vendorStockRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VendorShop() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VendorShop.
     */
    // TODO: Rename and change types and number of parameters
    public static VendorShop newInstance(String param1, String param2) {
        VendorShop fragment = new VendorShop();
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
        view = inflater.inflate(R.layout.fragment_vendor_shop, container, false);
        ActionBarTitle.getInstance().getTextView().setText("Vendor Home");

        Initialize();
        InitialOperations();
        Onclicklistners();

        return view;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialize() {
        vendorStockRef = FirebaseDatabase.getInstance().getReference("Vendor_stock/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        vendor_inventory_RecyclerView = (RecyclerView) view.findViewById(R.id.vendor_inventory_RecyclerView);
        vendor_inventory_RecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));

        vendor_inventory_add_btn_textiew = (TextView) view.findViewById(R.id.vendor_inventory_add_btn_textiew);

        stockCategory_list = new ArrayList<>();
        stockName_list = new ArrayList<>();
        stockCategory_list = new ArrayList<>();
        stockPrice_list = new ArrayList<>();
        stockQuantity_list = new ArrayList<>();
        stockImageUrl_list = new ArrayList<>();
        stockkeyId_list = new ArrayList<>();
        edit_btn_list = new ArrayList<>();

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialOperations() {
        fetchStock();
    }

    private void fetchStock() {
        vendorStockRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot1:snapshot.getChildren()){

                    for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                        stockName_list.add(dataSnapshot2.child("Name").getValue().toString());
                        stockCategory_list.add(dataSnapshot2.child("Category").getValue().toString());
                        stockPrice_list.add(dataSnapshot2.child("Price").getValue().toString());
                        stockQuantity_list.add(dataSnapshot2.child("Quantity").getValue().toString());
                        stockImageUrl_list.add(dataSnapshot2.child("Image_url").getValue().toString());
                        stockkeyId_list.add(dataSnapshot2.getKey().toString());
                    }

                }


                adapter_vendor_inventory_recyclerView = new Adapter_Vendor_inventory_RecyclerView(getActivity(),stockName_list, stockCategory_list,stockPrice_list,stockQuantity_list,stockImageUrl_list,stockkeyId_list,edit_btn_list);
                vendor_inventory_RecyclerView.setAdapter(adapter_vendor_inventory_recyclerView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void Onclicklistners() {
        vendor_inventory_add_btn_textiew_listner();
    }

    private void vendor_inventory_add_btn_textiew_listner() {
        vendor_inventory_add_btn_textiew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),Vendor_stock_entry.class);
                startActivityForResult(intent,111);

            }
        });
    }



}
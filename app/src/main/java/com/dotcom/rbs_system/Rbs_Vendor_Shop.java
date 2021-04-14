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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.Adapter_RBS_Vendor_inventory_RecyclerView;
import com.dotcom.rbs_system.Model.SampleSearchModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Rbs_Vendor_Shop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Rbs_Vendor_Shop extends Fragment {
    DatabaseReference vendorNameListRef, vendorStockRef;

    View view;

    RecyclerView rbs_vendor_products_recyclerview;

    LinearLayout searchForVendors,vendor_details_linearLayout;

    ImageView store_banner_imageView,profileImage_imageView;

    TextView vendor_name_textView,confirm_order_btn;
    TextView vendor_address_textView,vendor_phone_textView,vendor_email_textView;

    List<String> vendors_name_list,vendors_phno_list,vendors_id_list,vendors_address_list,vendors_email_list,vendors_profileImage_list,vendors_banner_list;
    List<String> vendor_stock_category_list, vendor_stockName_list, vendor_stock_currency_list, vendor_stock_price_list, vendor_stock_quantity_list, vendor_stock_imageView_list, vendor_stock_selection_checkbox_list;

    private ArrayList<SampleSearchModel> setting_vendors_name_data() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        for (int i = 0; i < vendors_name_list.size(); i++) {
            items.add(new SampleSearchModel(vendors_name_list.get(i)+"\n"+vendors_phno_list.get(i), vendors_id_list.get(i), vendors_name_list.get(i), vendors_phno_list.get(i), vendors_address_list.get(i), vendors_email_list.get(i), vendors_profileImage_list.get(i), vendors_banner_list.get(i)));
        }

        return items;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Rbs_Vendor_Shop() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Rbs_Vendor_Shop.
     */
    // TODO: Rename and change types and number of parameters
    public static Rbs_Vendor_Shop newInstance(String param1, String param2) {
        Rbs_Vendor_Shop fragment = new Rbs_Vendor_Shop();
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
        view= inflater.inflate(R.layout.fragment_rbs__vendor__shop, container, false);
        initialization();
        intialProcesses();
        onclicklistners();
        return view;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    private void initialization() {
        vendorNameListRef = FirebaseDatabase.getInstance().getReference("Vendor_list");
        vendorStockRef = FirebaseDatabase.getInstance().getReference("Vendor_stock");

        rbs_vendor_products_recyclerview=(RecyclerView)view.findViewById(R.id.rbs_vendor_products_recyclerview);
        rbs_vendor_products_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(),1));

        searchForVendors=(LinearLayout)view.findViewById(R.id.searchForVendors);
        vendor_details_linearLayout=(LinearLayout)view.findViewById(R.id.vendor_details_linearLayout);

        store_banner_imageView=(ImageView) view.findViewById(R.id.store_banner_imageView);
        profileImage_imageView=(ImageView) view.findViewById(R.id.profileImage_imageView);

        vendor_name_textView=(TextView)view.findViewById(R.id.vendor_name_textView);
        vendor_phone_textView =(TextView)view.findViewById(R.id.vendor_phone_textView);
        vendor_address_textView =(TextView)view.findViewById(R.id.vendor_address_textView);
        vendor_email_textView =(TextView)view.findViewById(R.id.vendor_email_textView);

        confirm_order_btn=(TextView)view.findViewById(R.id.confirm_order_btn);
        vendor_stock_category_list = new ArrayList<>();
        vendor_stockName_list = new ArrayList<>();
        vendor_stock_currency_list = new ArrayList<>();
        vendor_stock_price_list = new ArrayList<>();
        vendor_stock_quantity_list = new ArrayList<>();
        vendor_stock_imageView_list = new ArrayList<>();
        vendor_stock_selection_checkbox_list = new ArrayList<>();

        vendors_name_list = new ArrayList<>();
        vendors_phno_list = new ArrayList<>();
        vendors_id_list = new ArrayList<>();
        vendors_address_list = new ArrayList<>();
        vendors_email_list = new ArrayList<>();
        vendors_profileImage_list = new ArrayList<>();
        vendors_banner_list = new ArrayList<>();

        vendor_stock_category_list.add("Mobile");
        vendor_stockName_list.add("Samsung C7 Screens");
        vendor_stock_currency_list.add("$");
        vendor_stock_price_list.add("1000455");
        vendor_stock_quantity_list.add("10");

        vendor_stock_imageView_list.add("https://samsungmobilespecs.com/wp-content/uploads/2018/03/Samsung-Galaxy-C7-Price-Specs-featured-581x571.jpg");

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private void intialProcesses() {
        vendorNameListFetch();
    }

    private void vendorNameListFetch() {
        vendorNameListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    vendors_name_list.add(snapshot1.child("Name").getValue().toString());
                    vendors_phno_list.add(snapshot1.child("Phone_no").getValue().toString());
                    vendors_address_list.add(snapshot1.child("address").getValue().toString());
                    vendors_email_list.add(snapshot1.child("email").getValue().toString());
                    vendors_profileImage_list.add(snapshot1.child("profile_image_url").getValue().toString());
                    vendors_banner_list.add(snapshot1.child("banner").getValue().toString());
                    vendors_id_list.add(snapshot1.getKey().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private void onclicklistners() {
        searchForVendors_listner();
        confirm_order_btn_listner();
    }

    private void confirm_order_btn_listner() {
        confirm_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Rbs_vendor_order.class);
                startActivity(intent);
            }
        });
    }

    private void searchForVendors_listner() {
        searchForVendors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(getActivity(), "Search results...",
                        "Search for voucher number.", null, setting_vendors_name_data(),
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   SampleSearchModel item, int position) {
                                vendor_name_textView.setText(item.getName());
                                vendor_name_textView.setTextColor(getResources().getColor(R.color.textBlue));
                                vendor_phone_textView.setText(item.getVal1());
                                vendor_address_textView.setText(item.getVal2());
                                vendor_email_textView.setText(item.getVal3());
                                Picasso.get().load(item.getVal4()).into(profileImage_imageView);
                                Picasso.get().load(item.getVal5()).into(store_banner_imageView);
                                vendor_details_linearLayout.setVisibility(View.VISIBLE);
                                confirm_order_btn.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                                fetchVendorStock(item.getId());
                            }
                        }).show();

            }
        });
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private void fetchVendorStock(String id) {
        vendorStockRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){

                }
                Adapter_RBS_Vendor_inventory_RecyclerView adapter_rbs_vendor_inventory_recyclerView=new Adapter_RBS_Vendor_inventory_RecyclerView(getActivity(), vendor_stock_category_list, vendor_stockName_list, vendor_stock_currency_list, vendor_stock_price_list, vendor_stock_quantity_list, vendor_stock_imageView_list,null);
                rbs_vendor_products_recyclerview.setAdapter(adapter_rbs_vendor_inventory_recyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
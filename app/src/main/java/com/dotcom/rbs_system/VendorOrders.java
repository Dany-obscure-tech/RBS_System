package com.dotcom.rbs_system;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dotcom.rbs_system.Adapter.Adapter_Vendor_inventory_RecyclerView;
import com.dotcom.rbs_system.Adapter.Adapter_Vendor_order_list_RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VendorOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VendorOrders extends Fragment {
    RecyclerView orders_list_recyclerview;
    List<String> shop_name,order_no_vendor;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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
    // TODO: Rename and change types and number of parameters
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
        View view= inflater.inflate(R.layout.fragment_vendor_orders, container, false);
        orders_list_recyclerview = (RecyclerView) view.findViewById(R.id.orders_list_recyclerview);
        shop_name = new ArrayList<>();
        order_no_vendor = new ArrayList<>();
        shop_name.add("ITECH Computers");
        shop_name.add("Forex Trading");
        order_no_vendor.add("ITECH Computers");
        order_no_vendor.add("Forex Trading");
        Adapter_Vendor_order_list_RecyclerView adapter_vendor_order_list_recyclerView=new Adapter_Vendor_order_list_RecyclerView(getActivity(),order_no_vendor,shop_name,null,null,null,null,null,null);

        orders_list_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(),1));
        orders_list_recyclerview.setAdapter(adapter_vendor_order_list_recyclerView);

        return view;
    }
}
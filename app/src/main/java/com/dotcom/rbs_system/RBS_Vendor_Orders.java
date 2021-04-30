package com.dotcom.rbs_system;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dotcom.rbs_system.Adapter.AdapterShopProductsShowcaseListRecyclerView;
import com.dotcom.rbs_system.Adapter.Adapter_RBS_Vendor_orders_list_RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RBS_Vendor_Orders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RBS_Vendor_Orders extends Fragment {
    RecyclerView rbs_vendor_orders_recyclerview;
    View view;
    List<String> invoice_no_list,vendor_name_list,amount_currency_list,amount_list,paid_currency_list,paid_list,date_list,balance_currency_list,balance_list,order_status_list;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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
    // TODO: Rename and change types and number of parameters
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
        view = inflater.inflate(R.layout.fragment_r_b_s__vendor__orders, container, false);
        initialization();
        onclicklistners();
        return view;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void onclicklistners() {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void initialization() {
        rbs_vendor_orders_recyclerview = (RecyclerView) view.findViewById(R.id.rbs_vendor_orders_recyclerview);
        invoice_no_list = new ArrayList<String>();
        vendor_name_list = new ArrayList<String>();
        amount_currency_list = new ArrayList<String>();
        amount_list = new ArrayList<String>();
        paid_currency_list = new ArrayList<String>();
        paid_list = new ArrayList<String>();
        date_list = new ArrayList<String>();
        balance_currency_list = new ArrayList<String>();
        balance_list = new ArrayList<String>();
        order_status_list = new ArrayList<String>();
        invoice_no_list.add("00012333");
        vendor_name_list.add("ITech Computers");
        amount_currency_list.add("$");
        amount_list.add("10000");
        paid_currency_list.add("$");
        paid_list.add("10043");
        date_list.add("4/30/2021");
        balance_currency_list.add("$");
        balance_list.add("790");
        order_status_list.add("pending");
        Adapter_RBS_Vendor_orders_list_RecyclerView adapter_rbs_vendor_orders_list_recyclerView = new Adapter_RBS_Vendor_orders_list_RecyclerView(getActivity(), invoice_no_list, vendor_name_list, amount_currency_list,amount_list,paid_currency_list,paid_list,date_list,balance_currency_list,balance_list,order_status_list);
        rbs_vendor_orders_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rbs_vendor_orders_recyclerview.setAdapter(adapter_rbs_vendor_orders_list_recyclerView);
    }
}
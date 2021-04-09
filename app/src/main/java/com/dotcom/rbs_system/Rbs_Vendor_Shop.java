package com.dotcom.rbs_system;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.Adapter_RBS_Vendor_inventory_RecyclerView;
import com.dotcom.rbs_system.Adapter.Adapter_Vendor_order_list_RecyclerView;
import com.dotcom.rbs_system.Model.SampleSearchModel;

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

    View view;
    RecyclerView rbs_vendor_products_recyclerview;
    LinearLayout searchForVendors;
    TextView vendor_name_textView,confirm_order_btn;
    List<String> vendors_name_list;
    List<String> vendor_stock_category_textView, vendor_stockName_textView, vendor_stock_currency_textview, vendor_stock_price_textview, vendor_stock_quantity_textView,vendor_stock_imageView,vendor_stock_selection_checkbox;

    private ArrayList<SampleSearchModel> setting_vendors_name_data() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
//        for (int i=0;i<Voucher_amount_list.size();i++){
        for (int i = 0; i < vendors_name_list.size(); i++) {
//            items.add(new SampleSearchModel(voucher_number_list.get(i)+"\n("+Voucher_amount_list.get(i)+")",null,null,null,null,null,null,null));
            items.add(new SampleSearchModel(vendors_name_list.get(i), null, null, null, null, null, null, null));
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
        onclicklistners();
        return view;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////
    private void initialization() {
        rbs_vendor_products_recyclerview=(RecyclerView)view.findViewById(R.id.rbs_vendor_products_recyclerview);
        searchForVendors=(LinearLayout)view.findViewById(R.id.searchForVendors);
        vendor_name_textView=(TextView)view.findViewById(R.id.vendor_name_textView);
        confirm_order_btn=(TextView)view.findViewById(R.id.confirm_order_btn);
        vendor_stock_category_textView = new ArrayList<>();
        vendor_stockName_textView = new ArrayList<>();
        vendor_stock_currency_textview = new ArrayList<>();
        vendor_stock_price_textview = new ArrayList<>();
        vendor_stock_quantity_textView = new ArrayList<>();
        vendor_stock_imageView = new ArrayList<>();
        vendor_stock_selection_checkbox = new ArrayList<>();
        vendors_name_list = new ArrayList<>();
        vendor_stockName_textView.add("Samsung C7 Screens");
        vendors_name_list.add("Itech Computers");
        vendors_name_list.add("Phi Computers");
        vendor_stock_imageView.add("https://samsungmobilespecs.com/wp-content/uploads/2018/03/Samsung-Galaxy-C7-Price-Specs-featured-581x571.jpg");
        Adapter_RBS_Vendor_inventory_RecyclerView adapter_rbs_vendor_inventory_recyclerView=new Adapter_RBS_Vendor_inventory_RecyclerView(getActivity(),null,vendor_stockName_textView,null,null,null,vendor_stock_imageView,null);

        rbs_vendor_products_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rbs_vendor_products_recyclerview.setAdapter(adapter_rbs_vendor_inventory_recyclerView);
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
                                vendor_name_textView.setText(item.getTitle());
                                vendor_name_textView.setTextColor(getResources().getColor(R.color.textBlue));
                                dialog.dismiss();
                            }
                        }).show();

            }
        });
    }
}
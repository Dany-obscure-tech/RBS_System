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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.AdapterShopProductsShowcaseListRecyclerView;
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
 * Use the {@link Rbs_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Rbs_home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    DatabaseReference stockRef;
    RecyclerView your_products_RecyclerView;
    List<String> product_name;
    List<String> category;
    List<String> product_price;
    List<String> product_no_of_offers;
    List<String> image;
    List<String> key_idList;
    RelativeLayout side_option_menu;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Rbs_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Rbs_home.
     */
    // TODO: Rename and change types and number of parameters
    public static Rbs_home newInstance(String param1, String param2) {
        Rbs_home fragment = new Rbs_home();
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
        View view = inflater.inflate(R.layout.fragment_rbs_home, container, false);
        Initialize(view);
        initialProcess();
        onclicklistners();
        return view;
    }

    private void Initialize(View view) {
        stockRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        your_products_RecyclerView=(RecyclerView)view.findViewById(R.id.your_products_RecyclerView);
        side_option_menu=(RelativeLayout)view.findViewById(R.id.side_option_menu);
        product_name = new ArrayList<String>();
        category = new ArrayList<String>();
        product_price = new ArrayList<String>();
        product_no_of_offers = new ArrayList<String>();
        image = new ArrayList<String>();
        key_idList = new ArrayList<String>();
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    private void initialProcess() {
        fetchData();
    }

    private void fetchData() {
        stockRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    for (DataSnapshot snapshot2:snapshot1.getChildren()){
                        category.add(snapshot2.child("Category").getValue().toString());
                        product_name.add(snapshot2.child("Item_name").getValue().toString());
                        product_price.add(snapshot2.child("Price").getValue().toString());
                        image.add(snapshot2.child("Image").getValue().toString());
                        product_no_of_offers.add(String.valueOf(snapshot2.child("Offers").getChildrenCount()));
                        key_idList.add(snapshot2.child("Item_id").getValue().toString());





                    }
                }

                AdapterShopProductsShowcaseListRecyclerView adapterShopProductsShowcaseListRecyclerView = new AdapterShopProductsShowcaseListRecyclerView(getActivity(), product_name, category, image,product_price,product_no_of_offers,key_idList);
                your_products_RecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                your_products_RecyclerView.setAdapter(adapterShopProductsShowcaseListRecyclerView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    private void onclicklistners() {
    }


}
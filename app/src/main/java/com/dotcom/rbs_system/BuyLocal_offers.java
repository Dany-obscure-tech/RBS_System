package com.dotcom.rbs_system;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dotcom.rbs_system.Adapter.AdapterOffersItemListRecyclerView;
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
 * Use the {@link BuyLocal_offers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyLocal_offers extends Fragment {
    View view;

    AdapterOffersItemListRecyclerView adapterOffersItemListRecyclerView;

    DatabaseReference customerOffersRef;
    ImageButton search_imageBtn;
    EditText search_editText;
    RecyclerView offers;

    List<String> itemname;
    List<String> price;
    List<String> itemImage;
    List<String> offer_status;
    List<String> offer_product_price;
    List<String> product_offer_msg;
    List<String> product_keyId;
    List<String> shopkeeperID;
    List<String> itemCategory;

    List<String> filtereditemname;
    List<String> filteredprice;
    List<String> filtereditemImage;
    List<String> filteredoffer_status;
    List<String> filteredoffer_product_price;
    List<String> filteredproduct_offer_msg;
    List<String> filteredproduct_keyId;
    List<String> filteredshopkeeperID;
    List<String> filtereditemCategory;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public BuyLocal_offers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyLocal_offers.
     */
    public static BuyLocal_offers newInstance(String param1, String param2) {
        BuyLocal_offers fragment = new BuyLocal_offers();
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
        view = inflater.inflate(R.layout.fragment_buy_local_offers, container, false);

        search_imageBtn = (ImageButton)view.findViewById(R.id.search_imageBtn);
        search_editText = (EditText) view.findViewById(R.id.search_editText);
        customerOffersRef = FirebaseDatabase.getInstance().getReference("Customer_offers/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        offers = view.findViewById(R.id.offers);

        filtereditemname = new ArrayList<>();
        filteredprice = new ArrayList<>();
        filtereditemImage = new ArrayList<>();
        filteredoffer_status = new ArrayList<>();
        filteredoffer_product_price = new ArrayList<>();
        filteredproduct_offer_msg = new ArrayList<>();
        filteredproduct_keyId = new ArrayList<>();
        filteredshopkeeperID = new ArrayList<>();
        filtereditemCategory = new ArrayList<>();

        itemname = new ArrayList<>();
        price = new ArrayList<>();
        itemImage = new ArrayList<>();
        offer_status = new ArrayList<>();
        offer_product_price = new ArrayList<>();
        product_offer_msg = new ArrayList<>();
        product_keyId = new ArrayList<>();
        shopkeeperID = new ArrayList<>();
        itemCategory = new ArrayList<>();

        initialProcess();

        return view;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void initialProcess() {
        fetchData();
    }

    private void fetchData() {
        customerOffersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    itemname.add(snapshot1.child("item_name").getValue().toString());
                    price.add(snapshot1.child("item_price").getValue().toString());
                    itemImage.add(snapshot1.child("item_image").getValue().toString());
                    offer_status.add(snapshot1.child("offer_status").getValue().toString());
                    offer_product_price.add(snapshot1.child("amount").getValue().toString());
                    product_offer_msg.add(snapshot1.child("message").getValue().toString());
                    product_keyId.add(snapshot1.child("item_keyId").getValue().toString());
                    shopkeeperID.add(snapshot1.child("shopkeeper").getValue().toString());
                    itemCategory.add(snapshot1.child("item_category").getValue().toString());

                    filtereditemname.add(snapshot1.child("item_name").getValue().toString());
                    filteredprice.add(snapshot1.child("item_price").getValue().toString());
                    filtereditemImage.add(snapshot1.child("item_image").getValue().toString());
                    filteredoffer_status.add(snapshot1.child("offer_status").getValue().toString());
                    filteredoffer_product_price.add(snapshot1.child("amount").getValue().toString());
                    filteredproduct_offer_msg.add(snapshot1.child("message").getValue().toString());
                    filteredproduct_keyId.add(snapshot1.child("item_keyId").getValue().toString());
                    filteredshopkeeperID.add(snapshot1.child("shopkeeper").getValue().toString());
                    filtereditemCategory.add(snapshot1.child("item_category").getValue().toString());
                }

                adapterOffersItemListRecyclerView = new AdapterOffersItemListRecyclerView(getActivity(), filtereditemname, filteredprice, filtereditemImage, filteredoffer_status, filteredoffer_product_price, filteredproduct_offer_msg, filteredproduct_keyId, filteredshopkeeperID, filtereditemCategory);
                offers.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                offers.setAdapter(adapterOffersItemListRecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void onclicklistners() {
        search_imageBtn_listener();
    }


    private void search_imageBtn_listener() {
        search_imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filtereditemname.clear();
                filteredprice.clear();
                filtereditemImage.clear();
                filteredoffer_status.clear();
                filteredoffer_product_price.clear();
                filteredproduct_offer_msg.clear();
                filteredproduct_keyId.clear();
                filteredshopkeeperID.clear();
                filtereditemCategory.clear();

                if(!search_editText.getText().toString().isEmpty()){

                    for (int i = 0; i<itemname.size();i++){

                        if (itemname.get(i).regionMatches(true, 0, search_editText.getText().toString(), 0, search_editText.getText().toString().length())){
                            System.out.println(itemname.get(i));
                            filtereditemname.add(itemname.get(i));
                            filteredprice.add(price.get(i));
                            filtereditemImage.add(itemImage.get(i));
                            filteredoffer_status.add(offer_status.get(i));
                            filteredoffer_product_price.add(offer_product_price.get(i));
                            filteredproduct_offer_msg.add(product_offer_msg.get(i));
                            filteredproduct_keyId.add(product_keyId.get(i));
                            filteredshopkeeperID.add(shopkeeperID.get(i));
                            filtereditemCategory.add(itemCategory.get(i));
                        }
                    }
                }else {
                    for (int i = 0; i<itemname.size();i++){
                        filtereditemname.add(itemname.get(i));
                        filteredprice.add(price.get(i));
                        filtereditemImage.add(itemImage.get(i));
                        filteredoffer_status.add(offer_status.get(i));
                        filteredoffer_product_price.add(offer_product_price.get(i));
                        filteredproduct_offer_msg.add(product_offer_msg.get(i));
                        filteredproduct_keyId.add(product_keyId.get(i));
                        filteredshopkeeperID.add(shopkeeperID.get(i));
                        filtereditemCategory.add(itemCategory.get(i));
                    }

                }

                adapterOffersItemListRecyclerView.notifyDataSetChanged();

            }
        });
    }
}
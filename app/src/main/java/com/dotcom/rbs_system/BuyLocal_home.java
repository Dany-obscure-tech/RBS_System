package com.dotcom.rbs_system;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.AdapterCategoryRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterSpotlightItemListRecyclerView;
import com.dotcom.rbs_system.Adapter.SliderAdapterExample;
import com.dotcom.rbs_system.Classes.BuylocalSlider;
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
 * Use the {@link BuyLocal_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyLocal_home extends Fragment {
    AdapterCategoryRecyclerView adapterCategoryRecyclerView;

    Button test_btn, test1_btn;
    EditText search_editText;

    BuylocalSlider buylocalSliderlistObj;

    DatabaseReference spotlightItemsRef,categoriesRef;


    RecyclerView spotlightRecyclerView, category_recyclerview;

    CardView side_menu_cardview;

    List<String> slider_link_list, itemname, price, itemImage;
    List<String> imageUrl;
    List<String> category_text,category_icon, key_idList, categoryList, shopkeeperList;

    ImageView menu_btn;

    TextView logout, communication_option, rbs_option, vendor_option, offers_option;

    RelativeLayout side_option_menu_bg_relativeLayout;

    ImageButton search_imageBtn;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public BuyLocal_home() {

    }


    public static BuyLocal_home newInstance(String param1, String param2) {
        BuyLocal_home fragment = new BuyLocal_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buylocalSliderlistObj = BuylocalSlider.getInstance();

        spotlightItemsRef = FirebaseDatabase.getInstance().getReference("Spotlight");
        categoriesRef = FirebaseDatabase.getInstance().getReference("Categories");

        imageUrl = new ArrayList<>();
        key_idList = new ArrayList<>();
        categoryList = new ArrayList<>();
        shopkeeperList = new ArrayList<>();
        category_text = new ArrayList<>();
        category_icon = new ArrayList<>();

        imageUrl = buylocalSliderlistObj.getBuylocalSliderList();

        itemname = new ArrayList<>();
        price = new ArrayList<>();
        itemImage = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(getActivity(), imageUrl);

        View view = inflater.inflate(R.layout.fragment_buylocal_home, container, false);
        slider_link_list = new ArrayList<>();

        spotlightRecyclerView = view.findViewById(R.id.spotlightRecyclerView);
        category_recyclerview = view.findViewById(R.id.category_recyclerview);

        category_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        search_imageBtn = view.findViewById(R.id.search_imageBtn);

        test_btn = view.findViewById(R.id.test_btn);
        test1_btn = view.findViewById(R.id.test1_btn);
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Repair_Ticket.class));
            }
        });
        test1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), VendorMainScreen.class));
            }
        });

        search_editText = view.findViewById(R.id.search_editText);

        menu_btn = view.findViewById(R.id.menu_btn);

        side_menu_cardview = view.findViewById(R.id.side_menu_cardview);

        logout = view.findViewById(R.id.logout);
        communication_option = view.findViewById(R.id.communication_option);
        rbs_option = view.findViewById(R.id.rbs_option);
        vendor_option = view.findViewById(R.id.vendor_option);
        offers_option = view.findViewById(R.id.offers_option);

        side_option_menu_bg_relativeLayout = view.findViewById(R.id.side_option_menu_bg_relativeLayout);
        datafetch();
        categoryfetch();
        Onclick_listners();

        return view;
    }

    private void categoryfetch() {
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    category_text.add(snapshot1.child("name").getValue().toString());
                    category_icon.add(snapshot1.child("image_url").getValue().toString());
                }

                adapterCategoryRecyclerView = new AdapterCategoryRecyclerView(getActivity(), category_icon, category_text);
                category_recyclerview.setAdapter(adapterCategoryRecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void Onclick_listners() {
        rbs_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PasscodeActivity.class);
                side_menu_cardview.setVisibility(View.GONE);
                side_option_menu_bg_relativeLayout.setVisibility(View.GONE);
                intent.putExtra("ACTIVITY_CHECK", "RBS");
                getActivity().startActivity(intent);
            }
        });

        vendor_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PasscodeActivity.class);
                side_menu_cardview.setVisibility(View.GONE);
                side_option_menu_bg_relativeLayout.setVisibility(View.GONE);
                intent.putExtra("ACTIVITY_CHECK", "VENDOR");
                getActivity().startActivity(intent);
            }
        });

        offers_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BuyLocal_offers_option.class);
                side_menu_cardview.setVisibility(View.GONE);
                side_option_menu_bg_relativeLayout.setVisibility(View.GONE);
                getActivity().startActivity(intent);
            }
        });

        communication_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BuyLocal_conversations.class);
                side_menu_cardview.setVisibility(View.GONE);
                side_option_menu_bg_relativeLayout.setVisibility(View.GONE);
                getActivity().startActivity(intent);
            }
        });

        search_imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!search_editText.getText().toString().isEmpty()) {
                    Intent intent = new Intent(getActivity(), SearchResult.class);
                    intent.putExtra("SEARCHED", search_editText.getText().toString());
                    getActivity().startActivity(intent);
                } else {
                    search_editText.setError("Please search valid item");
                }
            }
        });

        side_option_menu_bg_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                side_menu_cardview.setVisibility(View.GONE);
                side_option_menu_bg_relativeLayout.setVisibility(View.GONE);
            }
        });

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (side_menu_cardview.getVisibility() == View.VISIBLE) {
                    side_menu_cardview.setVisibility(View.GONE);
                    side_option_menu_bg_relativeLayout.setVisibility(View.GONE);
                } else {
                    side_menu_cardview.setVisibility(View.VISIBLE);
                    side_option_menu_bg_relativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                side_menu_cardview.setVisibility(View.GONE);
                side_option_menu_bg_relativeLayout.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });
    }

    private void datafetch() {
        spotlightItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        itemname.add(String.valueOf(dataSnapshot1.child("Item_name").getValue()));
                        price.add(String.valueOf(dataSnapshot1.child("Price").getValue()));
                        itemImage.add(String.valueOf(dataSnapshot1.child("Item_image").getValue()));
                        key_idList.add(dataSnapshot1.getKey());
                        categoryList.add(String.valueOf(dataSnapshot1.child("Category").getValue()));
                        shopkeeperList.add(String.valueOf(dataSnapshot1.child("shopkeeper").getValue()));
                    }

                    AdapterSpotlightItemListRecyclerView viewAdapter = new AdapterSpotlightItemListRecyclerView(getActivity(), itemname, price, itemImage, key_idList, categoryList, shopkeeperList);
                    spotlightRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    spotlightRecyclerView.setAdapter(viewAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
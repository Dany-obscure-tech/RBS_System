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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyLocal_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyLocal_home extends Fragment {
    Button test_btn;
    EditText search_editText;

    BuylocalSlider buylocalSliderlistObj;

    SliderView sliderView;

    DatabaseReference spotlightItemsRef;

    MaterialSearchView materialSearchView;

    RecyclerView spotlightRecyclerView,category_recyclerview;

    CardView imageSlider;

    List<String> slider_link_list, itemname, price, itemImage;
    List<String> imageUrl;
    List<String> category_text,key_idList,categoryList,shopkeeperList;

    ImageView menu_btn;

    TextView logout,communication_option,rbs_option,vendor_option,offers_option;

    RelativeLayout side_option_menu,side_option_menu_bg_relativeLayout;

    ImageButton search_imageBtn;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

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
        imageUrl = new ArrayList<>();
        key_idList = new ArrayList<>();
        categoryList = new ArrayList<>();
        shopkeeperList = new ArrayList<>();
        category_text = new ArrayList<>();

        category_text.add("PC");
        category_text.add("Laptop");
        category_text.add("Mobile");
        category_text.add("Tablet");
        category_text.add("PC");
        category_text.add("Laptop");
        category_text.add("Mobile");
        category_text.add("Tablet");


        imageUrl = buylocalSliderlistObj.getBuylocalSliderList();

        itemname = new ArrayList<String>();
        price = new ArrayList<String>();
        itemImage = new ArrayList<String>();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(getActivity(),imageUrl);

        View view = inflater.inflate(R.layout.fragment_buylocal_home, container, false);
        slider_link_list=new ArrayList<>();

        datafetch();
        categoryfetch();

        spotlightRecyclerView = (RecyclerView) view.findViewById(R.id.spotlightRecyclerView);
        category_recyclerview = (RecyclerView) view.findViewById(R.id.category_recyclerview);

        AdapterCategoryRecyclerView adapterCategoryRecyclerView=new AdapterCategoryRecyclerView(getActivity(),null,category_text);

        category_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));

        category_recyclerview.setAdapter(adapterCategoryRecyclerView);

        search_imageBtn=(ImageButton) view.findViewById(R.id.search_imageBtn);

        test_btn=(Button) view.findViewById(R.id.test_btn);
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),RBS_mainscreen.class));
            }
        });

        search_editText=(EditText) view.findViewById(R.id.search_editText);

        menu_btn=(ImageView)view.findViewById(R.id.menu_btn);

        imageSlider=(CardView)view.findViewById(R.id.imageSlider);

        logout=(TextView) view.findViewById(R.id.logout);
        communication_option=(TextView) view.findViewById(R.id.communication_option);
        rbs_option=(TextView) view.findViewById(R.id.rbs_option);
        vendor_option=(TextView) view.findViewById(R.id.vendor_option);
        offers_option=(TextView) view.findViewById(R.id.offers_option);

        side_option_menu=(RelativeLayout)view.findViewById(R.id.side_option_menu);
        side_option_menu_bg_relativeLayout=(RelativeLayout)view.findViewById(R.id.side_option_menu_bg_relativeLayout);
        Onclick_listners();

        sliderView = view.findViewById(R.id.imageSliders);
        sliderView.setSliderAdapter(sliderAdapterExample);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.parseColor("#01A0DA"));
        sliderView.setIndicatorUnselectedColor(Color.parseColor("#F1F1F1"));
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        return view;
    }

    private void categoryfetch() {


    }

    private void Onclick_listners() {
        rbs_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PasscodeActivity.class);
                intent.putExtra("ACTIVITY_CHECK","RBS");
                getActivity().startActivity(intent);
            }
        });

        vendor_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PasscodeActivity.class);
                intent.putExtra("ACTIVITY_CHECK","VENDOR");
                getActivity().startActivity(intent);
            }
        });

        offers_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(),BuyLocal_offers_option.class);
                getActivity().startActivity(intent);
            }
        });

        communication_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BuyLocal_conversations.class);
                getActivity().startActivity(intent);
            }
        });

        search_imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!search_editText.getText().toString().isEmpty()){
                    Intent intent = new Intent(getActivity(),SearchResult.class);
                    intent.putExtra("SEARCHED",search_editText.getText().toString());
                    getActivity().startActivity(intent);
                }else {
                    search_editText.setError("Please search valid item");
                }
            }
        });

        side_option_menu_bg_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                side_option_menu_bg_relativeLayout.setVisibility(View.GONE);
                side_option_menu.setVisibility(View.GONE);
            }
        });

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (side_option_menu.getVisibility()==View.VISIBLE){
                    side_option_menu.setVisibility(View.GONE);
                    side_option_menu_bg_relativeLayout.setVisibility(View.GONE);
                }
                else {
                    side_option_menu.setVisibility(View.VISIBLE);
                    side_option_menu_bg_relativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                side_option_menu.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(),SignInActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });
    }

    private void datafetch() {
        spotlightItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        itemname.add(String.valueOf(dataSnapshot1.child("Item_name").getValue()));
                        price.add(String.valueOf(dataSnapshot1.child("Price").getValue()));
                        itemImage.add(String.valueOf(dataSnapshot1.child("id_image_url").getValue()));
                        key_idList.add(dataSnapshot1.getKey());
                        categoryList.add(String.valueOf(dataSnapshot1.child("Category").getValue()));
                        shopkeeperList.add(String.valueOf(dataSnapshot1.child("shopkeeper").getValue()));
                    }

                    AdapterSpotlightItemListRecyclerView viewAdapter = new AdapterSpotlightItemListRecyclerView(getActivity(), itemname, price, itemImage,key_idList,categoryList,shopkeeperList);
                    spotlightRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                    spotlightRecyclerView.setAdapter(viewAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        materialSearchView.setMenuItem(menuItem);

    }

}
package com.dotcom.rbs_system;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dotcom.rbs_system.Adapter.AdapterItemHistoryListRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterSpotlightItemListRecyclerView;
import com.dotcom.rbs_system.Adapter.SliderAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    MaterialSearchView materialSearchView;
    String[] list;

    RecyclerView spotlightRecyclerView;
    AdapterSpotlightItemListRecyclerView adapterSpotlightItemListRecyclerView;

//    private SliderAdapter adapter;
//    private SliderView ads_slider;
    List<String> slider_link_list,itemname,price;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        materialSearchView=view.findViewById(R.id.search_view);
//        ads_slider = view.findViewById(R.id.ads_slider);
        slider_link_list=new ArrayList<>();
        itemname=new ArrayList<>();
        price=new ArrayList<>();

        spotlightRecyclerView = (RecyclerView)view.findViewById(R.id.spotlightRecyclerView);
        spotlightRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),1));

        itemname.add("shirt");
        itemname.add("Shirt");

        price.add("1000");
        price.add("1000");

        adapterSpotlightItemListRecyclerView = new AdapterSpotlightItemListRecyclerView(getActivity(),itemname,price);
        spotlightRecyclerView.setAdapter(adapterSpotlightItemListRecyclerView);

//        ads_slider();
//        list= new String[]{"Mobile","Laptop","PC"};
//        materialSearchView.closeSearch();
//        materialSearchView.setSuggestions(list);
//        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                //here create your filtering
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //you can make change realtime if you typing here
//                return false;
//            }
//        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void ads_slider() {
        slider_link_list.clear();
        slider_link_list.add("https://images.app.goo.gl/89ZpdayMsTmwGgzg7");
        slider_link_list.add("https://images.app.goo.gl/MseFpaFozRZZSE8r6");
//        adapter = new SliderAdapter(getActivity(), slider_link_list);
//        ads_slider.setSliderAdapter(adapter);
//        ads_slider.setIndicatorAnimation(IndicatorAnimations.WORM);
//        ads_slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//        ads_slider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//        ads_slider.setIndicatorSelectedColor(Color.WHITE);
//        ads_slider.setIndicatorUnselectedColor(Color.GRAY);
//
//        ads_slider.setScrollTimeInSec(4);
//        ads_slider.startAutoCycle();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        materialSearchView.setMenuItem(menuItem);

    }
    //    @Override
//    public boolean onCreatOptionsMenu(Menu menu){
//
//    }
}
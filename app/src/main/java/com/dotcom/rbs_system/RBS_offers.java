package com.dotcom.rbs_system;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dotcom.rbs_system.Adapter.AdapterProductsOffersListRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RBS_offers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RBS_offers extends Fragment {

    //TODO to be removed

    View view;
    ImageButton back_btn;
    RecyclerView products_offers;
    List<String> itemname;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RBS_offers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RBS_offers.
     */
    // TODO: Rename and change types and number of parameters
    public static RBS_offers newInstance(String param1, String param2) {
        RBS_offers fragment = new RBS_offers();
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
        view= inflater.inflate(R.layout.fragment_rbs_offers, container, false);

        back_btn=(ImageButton)view.findViewById(R.id.back_btn);
        products_offers=(RecyclerView)view.findViewById(R.id.products_offers);
        itemname = new ArrayList<String>();
        itemname.add("Shirts");
        itemname.add("Tie");
        itemname.add("Mobiles");
        AdapterProductsOffersListRecyclerView adapterProductsOffersListRecyclerView = new AdapterProductsOffersListRecyclerView(getActivity(), itemname, null, null,null,null,null,null);
        products_offers.setLayoutManager(new GridLayoutManager(getActivity(),1));
        products_offers.setAdapter(adapterProductsOffersListRecyclerView);
        onclicklistners();
        return view;
    }

    private void onclicklistners() {

    }
}
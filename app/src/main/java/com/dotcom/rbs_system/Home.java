package com.dotcom.rbs_system;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.AdapterSpotlightItemListRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    DatabaseReference itemsRef;

    MaterialSearchView materialSearchView;

    RecyclerView recyclerView;

    List<String> slider_link_list, itemname, price, itemImage;

    ImageView menu_btn;

    TextView logout;

    RelativeLayout side_option_menu;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Home() {

    }



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

        itemsRef = FirebaseDatabase.getInstance().getReference("Spotlight");

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



        View view = inflater.inflate(R.layout.fragment_home, container, false);
        slider_link_list=new ArrayList<>();

        datafetch();

        recyclerView = (RecyclerView) view.findViewById(R.id.spotlightRecyclerView);

        menu_btn=(ImageView)view.findViewById(R.id.menu_btn);

        logout=(TextView) view.findViewById(R.id.logout);

        side_option_menu=(RelativeLayout)view.findViewById(R.id.side_option_menu);
        Onclick_listners();

        return view;
    }

    private void Onclick_listners() {
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String store_user_status;
                if (side_option_menu.getVisibility()==View.VISIBLE){
                    side_option_menu.setVisibility(View.GONE);
                }
                else {
                    side_option_menu.setVisibility(View.VISIBLE);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(),SignInActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });
    }

    private void datafetch() {
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        itemname.add(String.valueOf(dataSnapshot1.child("Item_name").getValue()));
                        price.add(String.valueOf(dataSnapshot1.child("Price").getValue()));
                        itemImage.add(String.valueOf(dataSnapshot1.child("id_image_url").getValue()));
                    }

                    AdapterSpotlightItemListRecyclerView viewAdapter = new AdapterSpotlightItemListRecyclerView(getActivity(), itemname, price, itemImage);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                    recyclerView.setAdapter(viewAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ads_slider() {
        slider_link_list.clear();
        slider_link_list.add("https://images.app.goo.gl/89ZpdayMsTmwGgzg7");
        slider_link_list.add("https://images.app.goo.gl/MseFpaFozRZZSE8r6");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        materialSearchView.setMenuItem(menuItem);

    }

}
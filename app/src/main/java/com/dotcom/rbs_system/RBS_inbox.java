package com.dotcom.rbs_system;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dotcom.rbs_system.Adapter.AdapterShopkeeperConversationListRecyclerView;
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
 * Use the {@link RBS_inbox#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RBS_inbox extends Fragment {
    View view;

    RecyclerView conversation_list_recyclerView;

    DatabaseReference userConversationRef;

    AdapterShopkeeperConversationListRecyclerView adapterShopkeeperConversationListRecyclerView;

    List<String> convoIdList, user2KeyIdList, user2NameList,user2ImageList,dateList,userType;
    List<String>  messageList;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public RBS_inbox() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RBS_inbox.
     */
    public static RBS_inbox newInstance(String param1, String param2) {
        RBS_inbox fragment = new RBS_inbox();
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
        view = inflater.inflate(R.layout.fragment_r_b_s_inbox, container, false);

        messageList = new ArrayList<>();
        user2ImageList = new ArrayList<>();
        convoIdList = new ArrayList<>();
        user2KeyIdList = new ArrayList<>();
        user2NameList = new ArrayList<>();
        dateList = new ArrayList<>();
        userType = new ArrayList<>();

        conversation_list_recyclerView = (RecyclerView) view.findViewById(R.id.conversation_list_recyclerView);
        conversation_list_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        userConversationRef = FirebaseDatabase.getInstance().getReference("User_conversation/" + FirebaseAuth.getInstance().getCurrentUser().getUid()+"/ShopkeeperInbox");

        InitialProcesses();

        return view;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialProcesses() {
        fetchingConvoList();
    }

    private void fetchingConvoList() {
        userConversationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    for (DataSnapshot snapshot2:snapshot1.getChildren()){

                        convoIdList.add(snapshot2.child("conversation_id").getValue().toString());
                        user2KeyIdList.add(snapshot2.getKey());
                        user2NameList.add(snapshot2.child("user_name").getValue().toString());
                        user2ImageList.add(snapshot2.child("user_image").getValue().toString());
                        messageList.add(snapshot2.child("last_message").getValue().toString());
                        dateList.add(snapshot2.child("last_message_time").getValue().toString());
                        userType.add(snapshot1.getKey());
                    }

                }
                adapterShopkeeperConversationListRecyclerView = new AdapterShopkeeperConversationListRecyclerView(getActivity(),messageList, convoIdList, user2KeyIdList, user2NameList,user2ImageList,dateList,userType);
                conversation_list_recyclerView.setAdapter(adapterShopkeeperConversationListRecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
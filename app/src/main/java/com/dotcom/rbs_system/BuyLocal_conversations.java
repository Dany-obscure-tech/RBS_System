package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dotcom.rbs_system.Adapter.AdapterConversationListRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyLocal_conversations extends AppCompatActivity {
    ImageButton back_btn;

    DatabaseReference userConversationRef;

    AdapterConversationListRecyclerView adapterConversationListRecyclerView;

    RecyclerView conversation_list_recyclerView;

    RelativeLayout action_bar;

    List<String> itemIdList, convoIdList, user2List;
    List<String> imageUrl, nameList, messageList, itemCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_conversations);

        Initialize();
        InitialProcesses();
        OnClicks();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Initialize()  {

        imageUrl= new ArrayList<>();
        nameList = new ArrayList<>();
        messageList= new ArrayList<>();
        itemCategory= new ArrayList<>();

        itemIdList = new ArrayList<>();
        convoIdList = new ArrayList<>();
        user2List = new ArrayList<>();

        back_btn = (ImageButton) findViewById(R.id.back_btn);

        conversation_list_recyclerView = (RecyclerView)findViewById(R.id.conversation_list_recyclerView);
        conversation_list_recyclerView.setLayoutManager(new GridLayoutManager(BuyLocal_conversations.this,1));

        action_bar = (RelativeLayout)findViewById(R.id.action_bar);

        userConversationRef = FirebaseDatabase.getInstance().getReference("User_conversation/"+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void InitialProcesses() {
        fetchingConvoList();
    }

    private void fetchingConvoList() {
        userConversationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    itemIdList.add(snapshot1.child("item_id").getValue().toString());
                    nameList.add(snapshot1.child("item_name").getValue().toString());
                    itemCategory.add(snapshot1.child("item_category").getValue().toString());
                    convoIdList.add(snapshot1.child("conversation_id").getValue().toString());
                    imageUrl.add(snapshot1.child("item_image").getValue().toString());
                    user2List.add(snapshot1.child("user2").getValue().toString());
                    messageList.add(snapshot1.child("last_message").getValue().toString());

                }
                adapterConversationListRecyclerView = new AdapterConversationListRecyclerView(BuyLocal_conversations.this,imageUrl,nameList,messageList,itemCategory,itemIdList,convoIdList,user2List);
                conversation_list_recyclerView.setAdapter(adapterConversationListRecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void OnClicks() {
        back_btn_listner();
    }

    private void back_btn_listner() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
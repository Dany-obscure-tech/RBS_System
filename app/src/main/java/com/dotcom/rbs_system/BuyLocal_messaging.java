package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.AdapterMessageRecyclerView;
import com.dotcom.rbs_system.Model.Data_model_chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class BuyLocal_messaging extends AppCompatActivity {

    AdapterMessageRecyclerView adapter_message;

    DatabaseReference conversationRef, userConversationRef, itemRef, userDataRef;

    StorageReference itemImageStorageRef;

    EditText message_editText;

    ImageButton messageSend_button, back_btn;

    ImageView profileImage;

    TextView user2Name_textView;

    String itemId, itemCategory, secondUserID,seconduserName, conversationKey = null;
    String itemName,itemImage;

    ArrayList senderList, messagelist;
    ArrayList<Data_model_chat> chatList;

    RecyclerView messagingRecyclerView;

    Boolean newConversation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_messaging);

        Initialize();
        InitialProcesses();
        OnClickListeners();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialize() {

        senderList = new ArrayList();
        messagelist = new ArrayList();
        chatList = new ArrayList();

        itemId = getIntent().getStringExtra("PRODUCT_ID");
        itemName = getIntent().getStringExtra("PRODUCT_NAME");
        itemImage = getIntent().getStringExtra("PRODUCT_IMAGE");
        itemCategory = getIntent().getStringExtra("CATEGORY");
        secondUserID = getIntent().getStringExtra("ID");
        seconduserName = getIntent().getStringExtra("SHOPKEEPER_NAME");
        conversationKey = getIntent().getStringExtra("CONVERSATION_KEY");

        userConversationRef = FirebaseDatabase.getInstance().getReference("User_conversation");
        itemRef = FirebaseDatabase.getInstance().getReference("Items/"+itemCategory+"/"+itemId);
        userDataRef = FirebaseDatabase.getInstance().getReference("Users_data/"+secondUserID);
        conversationRef = FirebaseDatabase.getInstance().getReference("Conversation");
        if (conversationKey==null){
            newConversation = true;
            conversationKey = conversationRef.push().getKey();
        }

        itemImageStorageRef = FirebaseStorage.getInstance().getReference().child("Item_Images/" + itemId+"/image_1");

        message_editText = (EditText)findViewById(R.id.message_editText);

        user2Name_textView = (TextView) findViewById(R.id.user2Name_textView);

        profileImage = (ImageView) findViewById(R.id.profileImage_imageView);

        messageSend_button = (ImageButton) findViewById(R.id.messageSend_button);
        back_btn = (ImageButton) findViewById(R.id.back_btn);

        messagingRecyclerView = (RecyclerView)findViewById(R.id.messaging_recyclerView);
        messagingRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        messagingRecyclerView.setLayoutManager(linearLayoutManager);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void OnClickListeners() {
        sendMessageButton();
        back_btn_listner();
    }

    private void sendMessageButton() {
        messageSend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!message_editText.getText().toString().isEmpty()){
                    if (newConversation){

                        userConversationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(secondUserID).child("conversation_id").setValue(conversationKey);
                        userConversationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(secondUserID).child("item_id").setValue(itemId);
                        userConversationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(secondUserID).child("item_category").setValue(itemCategory);
                        userConversationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(secondUserID).child("item_name").setValue(itemName);
                        userConversationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(secondUserID).child("item_image").setValue(itemImage);
                        userConversationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(secondUserID).child("user2").setValue(secondUserID);
                        userConversationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(secondUserID).child("user2_name").setValue(seconduserName);

                        userConversationRef.child(secondUserID).child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("conversation_id").setValue(conversationKey);
                        userConversationRef.child(secondUserID).child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("item_id").setValue(itemId);
                        userConversationRef.child(secondUserID).child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("item_category").setValue(itemCategory);
                        userConversationRef.child(secondUserID).child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("item_name").setValue(itemName);
                        userConversationRef.child(secondUserID).child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("item_image").setValue(itemImage);
                        userConversationRef.child(secondUserID).child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("user2").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                        userConversationRef.child(secondUserID).child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("user2_name").setValue("Customer name");

                        //////////////////////////

//                        userConversationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(itemId).child("conversation_id").setValue(conversationKey);
//                        userConversationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(itemId).child("item_id").setValue(itemId);
//                        userConversationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(itemId).child("item_category").setValue(itemCategory);
//                        userConversationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(itemId).child("item_name").setValue(itemName);
//                        userConversationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(itemId).child("item_image").setValue(itemImage);
//                        userConversationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(itemId).child("user2").setValue(secondUserID);
//
//                        userConversationRef.child(secondUserID).child(itemId).child("conversation_id").setValue(conversationKey);
//                        userConversationRef.child(secondUserID).child(itemId).child("item_id").setValue(itemId);
//                        userConversationRef.child(secondUserID).child(itemId).child("item_category").setValue(itemCategory);
//                        userConversationRef.child(secondUserID).child(itemId).child("item_name").setValue(itemName);
//                        userConversationRef.child(secondUserID).child(itemId).child("item_image").setValue(itemImage);
//                        userConversationRef.child(secondUserID).child(itemId).child("user2").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

                        conversationRef.child(conversationKey).child("user1").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        conversationRef.child(conversationKey).child("user2").setValue(secondUserID);
                        conversationRef.child(conversationKey).child("item").setValue(itemId);
                        conversationRef.child(conversationKey).child("item_category").setValue(itemCategory);
                        newConversation = false;
                    }

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("sender",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    hashMap.put("message",message_editText.getText().toString());

                    userConversationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child(secondUserID).child("last_message").setValue(message_editText.getText().toString());
                    userConversationRef.child(secondUserID).child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("last_message").setValue(message_editText.getText().toString());
                    conversationRef.child(conversationKey).child("messages").push().setValue(hashMap);
                    message_editText.setText("");
                }

            }
        });
    }

    private void back_btn_listner() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialProcesses() {
        getUser2Data();
        getMessagelist();
    }

    private void getUser2Data() {
        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user2Name_textView.setText(snapshot.child("fullname").getValue().toString());
                Picasso.get().load(snapshot.child("profile_image_url").getValue().toString()).into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMessagelist() {
        conversationRef.child(conversationKey).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    chatList.clear();
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        Data_model_chat mChat = snapshot1.getValue(Data_model_chat.class);
                        chatList.add(mChat);
                    }
                    adapter_message = new AdapterMessageRecyclerView(BuyLocal_messaging.this,chatList);
                    messagingRecyclerView.setAdapter(adapter_message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
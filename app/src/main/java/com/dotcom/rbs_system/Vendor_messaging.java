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
import com.dotcom.rbs_system.Classes.UserDetails;
import com.dotcom.rbs_system.Model.Data_model_chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Vendor_messaging extends AppCompatActivity {
    String shopKeeperID,vendorID;
    String conversationID;
    String secondUserName,secondUserImage;
    String firstUserName,firstUserImage;

    DatabaseReference vendorInbox,shopkeeperInbox,conversationRef, secondUserDataRef;

    Boolean newConversation;

    EditText message_editText;

    ImageButton messageSend_button, back_btn;

    ArrayList<Data_model_chat> chatList;

    RecyclerView messagingRecyclerView;

    AdapterMessageRecyclerView adapter_message;

    ImageView profileImage_imageView;
    TextView user2Name_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_messaging);

        Initialize();
        InitialProcess();
        OnClickListeners();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialize() {
        user2Name_textView = findViewById(R.id.user2Name_textView);
        profileImage_imageView = findViewById(R.id.profileImage_imageView);

        chatList = new ArrayList();

        messageSend_button = (ImageButton) findViewById(R.id.messageSend_button);
        back_btn = (ImageButton) findViewById(R.id.back_btn);
        message_editText = (EditText) findViewById(R.id.message_editText);

        shopKeeperID= getIntent().getStringExtra("SHOPKEEPER_ID");
        vendorID= getIntent().getStringExtra("VENDOR_ID");

        vendorInbox = FirebaseDatabase.getInstance().getReference("User_conversation/"+vendorID+"/VendorInbox/"+shopKeeperID);
        shopkeeperInbox = FirebaseDatabase.getInstance().getReference("User_conversation/"+shopKeeperID+"/ShopkeeperInbox/Vendor/"+vendorID);

        messagingRecyclerView = (RecyclerView) findViewById(R.id.messaging_recyclerView);
        messagingRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        messagingRecyclerView.setLayoutManager(linearLayoutManager);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void InitialProcess() {
        checkForConversation();
        getSecondUserData();
    }

    private void getSecondUserData() {
        if (vendorID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

            secondUserDataRef = FirebaseDatabase.getInstance().getReference("Users_data/"+shopKeeperID+"/Shopkeeper_details");
            secondUserDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user2Name_textView.setText(snapshot.child("shop_name").getValue().toString());
                    Picasso.get().load(snapshot.child("shop_logo").getValue().toString()).into(profileImage_imageView);
                    secondUserName = snapshot.child("shop_name").getValue().toString();
                    secondUserImage = snapshot.child("shop_logo").getValue().toString();
                    firstUserName = UserDetails.getInstance().getVendorName();
                    firstUserImage = UserDetails.getInstance().getVendorLogo();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }else if (shopKeeperID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            secondUserDataRef = FirebaseDatabase.getInstance().getReference("Users_data/"+vendorID+"/Vendor_details");
            secondUserDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user2Name_textView.setText(snapshot.child("vendor_name").getValue().toString());
                    Picasso.get().load(snapshot.child("vendor_logo").getValue().toString()).into(profileImage_imageView);

                    secondUserName = snapshot.child("vendor_name").getValue().toString();
                    secondUserImage = snapshot.child("vendor_logo").getValue().toString();

                    firstUserName = UserDetails.getInstance().getShopName();
                    firstUserImage = UserDetails.getInstance().getShopLogo();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void checkForConversation() {
        vendorInbox.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    conversationID = snapshot.child("conversation_id").getValue().toString();
                    newConversation = false;
                }else {
                    conversationID = vendorInbox.push().getKey();
                    newConversation = true;
                }
                conversationRef = FirebaseDatabase.getInstance().getReference("Conversation/"+conversationID);
                getMessagelist(conversationRef);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMessagelist(DatabaseReference conversationRef) {
        conversationRef.child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    chatList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Data_model_chat mChat = snapshot1.getValue(Data_model_chat.class);
                        chatList.add(mChat);
                    }
                    adapter_message = new AdapterMessageRecyclerView(Vendor_messaging.this, chatList);
                    messagingRecyclerView.setAdapter(adapter_message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void OnClickListeners(){
        sendMessageButton();
        back_btn_listner();
    }
    private void sendMessageButton() {
        messageSend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!message_editText.getText().toString().isEmpty()) {
                    if (newConversation) {

                        vendorInbox.child("conversation_id").setValue(conversationID);

                        shopkeeperInbox.child("conversation_id").setValue(conversationID);

                        if (vendorID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            vendorInbox.child("user_name").setValue(secondUserName);
                            vendorInbox.child("user_image").setValue(secondUserImage);
                            shopkeeperInbox.child("user_name").setValue(firstUserName);
                            shopkeeperInbox.child("user_image").setValue(firstUserImage);
                        }else{
                            vendorInbox.child("user_name").setValue(firstUserName);
                            vendorInbox.child("user_image").setValue(firstUserImage);
                            shopkeeperInbox.child("user_name").setValue(secondUserName);
                            shopkeeperInbox.child("user_image").setValue(secondUserImage);
                        }

                        conversationRef.child("shopkeeper").setValue(shopKeeperID);
                        conversationRef.child("vendor").setValue(vendorID);;
                        newConversation = false;
                    }

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("sender", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    hashMap.put("message", message_editText.getText().toString());
                    hashMap.put("time", new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime()));

                    vendorInbox.child("last_message").setValue(message_editText.getText().toString());
                    vendorInbox.child("last_message_time").setValue(hashMap.get("time"));

                    shopkeeperInbox.child("last_message").setValue(message_editText.getText().toString());
                    shopkeeperInbox.child("last_message_time").setValue(hashMap.get("time"));

                    conversationRef.child("messages").push().setValue(hashMap);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
}
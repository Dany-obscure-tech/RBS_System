package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.BuyLocal_messaging;
import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterConversationListRecyclerView extends RecyclerView.Adapter<AdapterConversationListRecyclerView.ViewHolder> {

    Context context;

    List<String> imageUrl, nameList, messageList,itemCategory;
    List<String> itemIdList, convoIdList, user2List;
    List<String> user2NameList;

    public AdapterConversationListRecyclerView(Context context, List<String> imageUrl, List<String> nameList, List<String> messageList, List<String> itemCategory, List<String> itemIdList, List<String> convoIdList, List<String> user2List, List<String> user2NameList){
        this.context = context;
        this.nameList = nameList;
        this.imageUrl = imageUrl;
        this.itemCategory = itemCategory;
        this.itemIdList = itemIdList;
        this.messageList = messageList;
        this.convoIdList = convoIdList;
        this.user2List = user2List;
        this.user2NameList = user2NameList;
    }

    @NonNull
    @Override
    public AdapterConversationListRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterConversationListRecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_conversation_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterConversationListRecyclerView.ViewHolder holder, final int position) {
        holder.user_name_textview.setText(user2NameList.get(position));
        holder.last_msg_textView.setText(messageList.get(position));
        Picasso.get().load(imageUrl.get(position)).into(holder.product_image);

        holder.conversationItem_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicate_btn_listner(user2List.get(position),itemIdList.get(position),itemCategory.get(position),convoIdList.get(position),nameList.get(position), imageUrl.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return user2List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView conversationItem_cardView;
        TextView user_name_textview,last_msg_textView;
        ImageView product_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            conversationItem_cardView = (CardView) itemView.findViewById(R.id.conversationItem_cardView);
            user_name_textview = (TextView) itemView.findViewById(R.id.user_name_textview);
            last_msg_textView = (TextView) itemView.findViewById(R.id.last_msg_textView);
            product_image = (ImageView) itemView.findViewById(R.id.product_image);
        }
    }

    private void communicate_btn_listner(String shopkeeperID, String productID, String category, String conversationKey, String productName, String imageUrl) {
                Intent intent = new Intent(context, BuyLocal_messaging.class);
                intent.putExtra("ID",shopkeeperID);
                intent.putExtra("PRODUCT_ID",productID);
                intent.putExtra("CATEGORY",category);
                intent.putExtra("CONVERSATION_KEY",conversationKey);
                intent.putExtra("PRODUCT_NAME",productName);
                intent.putExtra("PRODUCT_IMAGE",imageUrl);
                context.startActivity(intent);
    }
}




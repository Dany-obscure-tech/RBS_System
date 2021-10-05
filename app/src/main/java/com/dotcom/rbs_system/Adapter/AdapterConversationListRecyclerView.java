package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;
import com.dotcom.rbs_system.Vendor_messaging;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterConversationListRecyclerView extends RecyclerView.Adapter<AdapterConversationListRecyclerView.ViewHolder> {

    Context context;

    List<String>  messageList,convoIdList, user2KeyIdList, user2NameList,user2ImageList,dateList;

    public AdapterConversationListRecyclerView(Context context, List<String> messageList, List<String> convoIdList, List<String> user2KeyIdList, List<String> user2NameList, List<String> user2ImageList, List<String> dateList) {
        this.context = context;
        this.messageList = messageList;
        this.convoIdList = convoIdList;
        this.user2KeyIdList = user2KeyIdList;
        this.user2NameList = user2NameList;
        this.user2ImageList = user2ImageList;
        this.dateList = dateList;
    }

    @NonNull
    @Override
    public AdapterConversationListRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_conversation_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterConversationListRecyclerView.ViewHolder holder, final int position) {
        holder.user_name_textview.setText(user2NameList.get(position));
        holder.last_msg_textView.setText(messageList.get(position));
        holder.date_textview.setText(dateList.get(position));
        Picasso.get().load(user2ImageList.get(position)).into(holder.product_image);

        holder.conversationItem_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Vendor_messaging.class);
                intent.putExtra("SHOPKEEPER_ID", user2KeyIdList.get(position));
                intent.putExtra("VENDOR_ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return user2KeyIdList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView conversationItem_cardView;
        TextView user_name_textview, last_msg_textView,date_textview;
        ImageView product_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            conversationItem_cardView = (CardView) itemView.findViewById(R.id.conversationItem_cardView);
            user_name_textview = (TextView) itemView.findViewById(R.id.user_name_textview);
            last_msg_textView = (TextView) itemView.findViewById(R.id.last_msg_textView);
            date_textview = (TextView) itemView.findViewById(R.id.date_textview);
            product_image = (ImageView) itemView.findViewById(R.id.product_image);
        }
    }

}




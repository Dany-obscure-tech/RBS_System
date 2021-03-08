package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Model.Data_model_chat;
import com.dotcom.rbs_system.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class AdapterMessageRecyclerView extends RecyclerView.Adapter<AdapterMessageRecyclerView.VieHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<Data_model_chat> chat;


    public AdapterMessageRecyclerView(Context context, List<Data_model_chat> chat){
        this.context = context;
        this.chat = chat;
    }

    @NonNull
    @Override
    public AdapterMessageRecyclerView.VieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_right,parent,false);
            return new AdapterMessageRecyclerView.VieHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_left,parent,false);
            return new AdapterMessageRecyclerView.VieHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMessageRecyclerView.VieHolder holder, int position) {
        Data_model_chat mchat = chat.get(position);
        holder.show_message.setText(mchat.getMessage());
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    public class VieHolder extends RecyclerView.ViewHolder {
        TextView show_message;

        public VieHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (chat.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}

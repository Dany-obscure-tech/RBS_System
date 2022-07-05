package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Buylocal_category_products;
import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFilesNamesRecyclerView extends RecyclerView.Adapter<AdapterFilesNamesRecyclerView.MyViewHolder> {
    Context context;
    List<String> fileNameList;
    List<Uri> fileUriList;

    public AdapterFilesNamesRecyclerView(Context context, List<String> fileNameList, List<Uri> fileUriList) {
        this.context = context;
        this.fileNameList = fileNameList;
        this.fileUriList = fileUriList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.recyclerview_file_name_item, parent, false);
        AdapterFilesNamesRecyclerView.MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.fileName_Textview.setText(fileNameList.get(position));
        holder.remove_tetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileNameList.remove(holder.getBindingAdapterPosition());
                fileUriList.remove(holder.getBindingAdapterPosition());

                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView fileName_Textview;
        TextView remove_tetView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName_Textview =  itemView.findViewById(R.id.fileName_Textview);
            remove_tetView = (TextView) itemView.findViewById(R.id.remove_tetView);

        }
    }
}

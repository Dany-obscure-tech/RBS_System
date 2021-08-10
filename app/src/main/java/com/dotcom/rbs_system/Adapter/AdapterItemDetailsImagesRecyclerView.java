package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.BuyLocal_shopkeeperProductDetails;
import com.dotcom.rbs_system.R;

import java.util.List;

public class AdapterItemDetailsImagesRecyclerView extends RecyclerView.Adapter<AdapterItemDetailsImagesRecyclerView.ViewHolder> {

    Context context;
    List<Uri> imageUrlList;

    public AdapterItemDetailsImagesRecyclerView(Context context, List<Uri> imageUrlList) {
        this.context = context;
        this.imageUrlList = imageUrlList;
        Toast.makeText(context, String.valueOf(context), Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public AdapterItemDetailsImagesRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterItemDetailsImagesRecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_itemdetails_image_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItemDetailsImagesRecyclerView.ViewHolder holder, final int position) {

        holder.image_imageView.setImageURI(imageUrlList.get(position));

        holder.removeImage_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUrlList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_imageView;
        TextView removeImage_textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_imageView = (ImageView)itemView.findViewById(R.id.image_imageView);
            removeImage_textView = (TextView) itemView.findViewById(R.id.removeImage_textView);
        }
    }
}

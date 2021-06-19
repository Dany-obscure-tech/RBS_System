package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCustomerIDImagesRecyclerView extends RecyclerView.Adapter<AdapterCustomerIDImagesRecyclerView.ViewHolder> {

    Context context;
    List<String> customer_ID_list;

    public AdapterCustomerIDImagesRecyclerView(Context context, List<String> customer_ID_list) {
        this.context = context;
        this.customer_ID_list = customer_ID_list;


    }

    @NonNull
    @Override
    public AdapterCustomerIDImagesRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterCustomerIDImagesRecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_customer_id_image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCustomerIDImagesRecyclerView.ViewHolder holder, final int position) {

        Picasso.get().load(customer_ID_list.get(position)).into(holder.customer_id_imageView);
    }

    @Override
    public int getItemCount() {
        return customer_ID_list.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView customer_id_imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customer_id_imageView = (ImageView) itemView.findViewById(R.id.customer_id_imageView);
        }
    }
}

package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCustomerIDImagesRecyclerView extends RecyclerView.Adapter<AdapterCustomerIDImagesRecyclerView.ViewHolder> {

    Context context;
    List<String> customer_ID_list;
    ImageView edit_image_image_view;
    RelativeLayout alert_background_relativelayout;

    public AdapterCustomerIDImagesRecyclerView(Context context, List<String> customer_ID_list, RelativeLayout alert_background_relativelayout, ImageView edit_image_image_view) {
        this.context = context;
        this.customer_ID_list = customer_ID_list;
        this.alert_background_relativelayout = alert_background_relativelayout;
        this.edit_image_image_view = edit_image_image_view;

    }

    @NonNull
    @Override
    public AdapterCustomerIDImagesRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_customer_id_image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCustomerIDImagesRecyclerView.ViewHolder holder, final int position) {

        Picasso.get().load(customer_ID_list.get(position)).into(holder.customer_id_imageView);
        holder.customer_id_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(customer_ID_list.get(position)).into(edit_image_image_view);
                alert_background_relativelayout.setVisibility(View.VISIBLE);
                edit_image_image_view.setVisibility(View.VISIBLE);
            }
        });
        alert_background_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_background_relativelayout.setVisibility(View.GONE);
                edit_image_image_view.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customer_ID_list.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView customer_id_imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customer_id_imageView = itemView.findViewById(R.id.customer_id_imageView);

        }
    }
}

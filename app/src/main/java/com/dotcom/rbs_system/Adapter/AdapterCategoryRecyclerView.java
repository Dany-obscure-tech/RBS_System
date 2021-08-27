package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Buylocal_category_products;
import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCategoryRecyclerView extends RecyclerView.Adapter<AdapterCategoryRecyclerView.MyViewHolder> {
    Context context;
    List<String> category_icon;
    List<String> category_name;

    public AdapterCategoryRecyclerView(Context context, List<String> category_icon, List<String> category_name) {
        this.context = context;
        this.category_icon = category_icon;
        this.category_name = category_name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.recyclerview_category_items, parent, false);
        AdapterCategoryRecyclerView.MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.category_Item_Textview.setText(category_name.get(position));
        Picasso.get().load(category_icon.get(position)).into(holder.category_item_Icon);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Buylocal_category_products.class);
                intent.putExtra("CATEGORY_NAME", category_name.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return category_name.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView category_item_Icon;
        TextView category_Item_Textview;
        CardView button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            category_item_Icon = (ImageView) itemView.findViewById(R.id.category_item_Icon);
            category_Item_Textview = (TextView) itemView.findViewById(R.id.category_Item_Textview);
            button = (CardView) itemView.findViewById(R.id.button);


        }
    }
}

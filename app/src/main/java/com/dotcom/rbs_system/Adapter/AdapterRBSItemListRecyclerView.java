package com.dotcom.rbs_system.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterRBSItemListRecyclerView extends RecyclerView.Adapter<AdapterRBSItemListRecyclerView.ViewHolder> {

    Context context;
    List<String> categoryList,categoryImageList;
    Dialog dialog;
    TextView selectCategory_textView;
    ImageView categoryIcon_imageView;

    public AdapterRBSItemListRecyclerView(Context context, Dialog dialog, TextView selectCategory_textView, ImageView categoryIcon_imageView, List<String> categoryList, List<String> categoryImageList) {
        this.context = context;
        this.categoryList = categoryList;
        this.categoryImageList = categoryImageList;
        this.dialog = dialog;
        this.selectCategory_textView = (TextView) ((Activity)context).findViewById(R.id.selectCategory_textView);
        this.categoryIcon_imageView = (ImageView) ((Activity)context).findViewById(R.id.categoryIcon_imageView);
    }


    @NonNull
    @Override
    public AdapterRBSItemListRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterRBSItemListRecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_rbs_item_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterRBSItemListRecyclerView.ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView selectCategory_textView;
        ImageView categoryIcon_imageView;
        CardView button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            selectCategory_textView = (TextView) itemView.findViewById(R.id.selectCategory_textView);
            categoryIcon_imageView = (ImageView)itemView.findViewById(R.id.categoryIcon_imageView);
            button = (CardView) itemView.findViewById(R.id.button);
        }
    }
}

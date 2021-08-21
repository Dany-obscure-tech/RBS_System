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

public class AdapterItemCategoryListRecyclerView extends RecyclerView.Adapter<AdapterItemCategoryListRecyclerView.ViewHolder> {

    Context context;
    List<String> categoryList, categoryImageList;
    Dialog dialog;
    TextView selectCategory_textView;
    ImageView categoryIcon_imageView;

    public AdapterItemCategoryListRecyclerView(Context context, Dialog dialog, TextView selectCategory_textView, ImageView categoryIcon_imageView, List<String> categoryList, List<String> categoryImageList) {
        this.context = context;
        this.categoryList = categoryList;
        this.categoryImageList = categoryImageList;
        this.dialog = dialog;
        this.selectCategory_textView = ((Activity) context).findViewById(R.id.selectCategory_textView);
        this.categoryIcon_imageView = ((Activity) context).findViewById(R.id.categoryIcon_imageView);
    }


    @NonNull
    @Override
    public AdapterItemCategoryListRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_category_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterItemCategoryListRecyclerView.ViewHolder holder, final int position) {
        holder.selectCategory_textView.setText(categoryList.get(position));
        Picasso.get().load(categoryImageList.get(position)).into(holder.categoryIcon_imageView);

        holder.button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    holder.button.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorLightGrey));
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    holder.button.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    holder.button.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                    selectCategory_textView.setText(categoryList.get(position));
                    categoryIcon_imageView.setVisibility(View.VISIBLE);
                    categoryIcon_imageView.setImageDrawable(holder.categoryIcon_imageView.getDrawable());
                    dialog.dismiss();
                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView selectCategory_textView;
        ImageView categoryIcon_imageView;
        CardView button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            selectCategory_textView = itemView.findViewById(R.id.selectCategory_textView);
            categoryIcon_imageView = itemView.findViewById(R.id.categoryIcon_imageView);
            button = itemView.findViewById(R.id.button);
        }
    }
}

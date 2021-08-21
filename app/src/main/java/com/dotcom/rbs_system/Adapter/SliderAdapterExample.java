package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dotcom.rbs_system.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapterExample extends
        SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private List<String> imageUrl;

    public SliderAdapterExample(Context context, List<String> imageUrl) {
        this.context = context;
        this.imageUrl = imageUrl;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {


        Picasso.get().load(imageUrl.get(position)).into(viewHolder.iv_auto_image_slider);
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return imageUrl.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView iv_auto_image_slider;
        ImageView imageGifContainer;


        public SliderAdapterVH(View itemView) {
            super(itemView);
            iv_auto_image_slider = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);

            this.itemView = itemView;
        }
    }

}
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

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewHolder> {
    Context context;
    List <String> slider_image;

    public SliderAdapter(Context context, List<String> slider_image) {
        this.context=context;
        this.slider_image = slider_image;
    }

    @Override
    public SliderAdapter.SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, parent, false);
        SliderAdapter.SliderViewHolder sliderViewHolder=new SliderAdapter.SliderViewHolder(view);


        return sliderViewHolder;
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {

        for (int i = 0; i < slider_image.size(); i++){
                Picasso.get().load(slider_image.get(i)).into(viewHolder.home_featured_image);

        }

//        switch (position) {
//            case 0:
//                Picasso.get().load(R.drawable.sliderdemo3).into(viewHolder.home_featured_image);
//                break;
//
//            case 1:
//                Picasso.get().load(R.drawable.slidedemo).into(viewHolder.home_featured_image);
//                break;
//
//            case 2:
//                Picasso.get().load(R.drawable.slidedemo2).into(viewHolder.home_featured_image);
//                break;
//
//        }
        final String sliderImage= slider_image.get(position);
        if (!sliderImage.isEmpty()){

            Picasso.get().load(sliderImage).into(viewHolder.home_featured_image);
        }
    }

    @Override
    public int getCount() {
        return slider_image.size();
    }

    class SliderViewHolder extends SliderViewAdapter.ViewHolder {

        ImageView home_featured_image;

        public SliderViewHolder(View itemView) {
            super(itemView);

            home_featured_image = itemView.findViewById(R.id.home_featured_image);
        }
    }

}

package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.rbs_system.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterExample extends
        SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

  private Context context;
  private List<String> imageUrl;

  public SliderAdapterExample(Context context,List<String> imageUrl) {
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

    viewHolder.textViewDescription.setText("Description");
    viewHolder.textViewDescription.setTextSize(16);
    viewHolder.textViewDescription.setTextColor(Color.WHITE);

    Picasso.get().load(imageUrl.get(position)).into(viewHolder.imageGifContainer);

    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public int getCount() {
    //slider view count could be dynamic size
    return imageUrl.size();
  }

  class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

    View itemView;
    ImageView imageViewBackground;
    ImageView imageGifContainer;
    TextView textViewDescription;

    public SliderAdapterVH(View itemView) {
      super(itemView);
      imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
      imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
      textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
      this.itemView = itemView;
    }
  }

}
package com.dotcom.rbs_system.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_itemList_alert_dialog extends RecyclerView.Adapter<Adapter_itemList_alert_dialog.ViewHolder> {

    Context context;
    List<String> exisitngItemsNamesList, exisitngItemsSerialNoList,exisitngItemsKeyIDList,existingItemsPriceList,existingItemsLastActiveList,existingItemsImageUrlList;
    TextView itemName_textView,  itemID_textView,  itemPrice_textView,  itemLastActive_textView;
    TextView itemPriceCurrency_textView;
    ImageView itemImage_imageView;

    Dialog itemList_alert_dialog;

    public Adapter_itemList_alert_dialog(Context context, List<String> exisitngItemsNamesList, List<String> exisitngItemsSerialNoList, List<String> exisitngItemsKeyIDList, List<String> existingItemsPriceList, List<String> existingItemsLastActiveList, List<String> existingItemsImageUrlList, TextView itemName_textView, TextView itemID_textView, TextView itemPriceCurrency_textView, TextView itemPrice_textView, TextView itemLastActive_textView, ImageView itemImage_imageView, Dialog itemList_alert_dialog) {
        this.context = context;
        this.exisitngItemsNamesList = exisitngItemsNamesList;
        this.exisitngItemsSerialNoList = exisitngItemsSerialNoList;
        this.exisitngItemsKeyIDList = exisitngItemsKeyIDList;
        this.existingItemsPriceList = existingItemsPriceList;
        this.existingItemsLastActiveList = existingItemsLastActiveList;
        this.existingItemsImageUrlList = existingItemsImageUrlList;
        this.itemList_alert_dialog = itemList_alert_dialog;

        this.itemName_textView = itemName_textView;
        this.itemID_textView = itemID_textView;
        this.itemPriceCurrency_textView = itemPriceCurrency_textView;
        this.itemPrice_textView = itemPrice_textView;
        this.itemLastActive_textView = itemLastActive_textView;
        this.itemImage_imageView = itemImage_imageView;
    }

    @NonNull
    @Override
    public Adapter_itemList_alert_dialog.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Adapter_itemList_alert_dialog.ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_itemlist_alert_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter_itemList_alert_dialog.ViewHolder holder, int position) {
        holder.itemName_textView.setText(exisitngItemsNamesList.get(position));
        holder.itemSerialNo_textView.setText(exisitngItemsSerialNoList.get(position));
        holder.itemPrice_textView.setText(existingItemsPriceList.get(position));
        holder.itemLastActive_textView.setText(existingItemsLastActiveList.get(position));
        holder.itemPriceCurrency_textView.setText(Currency.getInstance().getCurrency());

        Picasso.get().load(existingItemsImageUrlList.get(position)).into(holder.itemImage_imageView);

        holder.searchForItem_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemName_textView.setText(holder.itemName_textView.getText().toString());
                itemID_textView.setText(holder.itemSerialNo_textView.getText().toString());
                itemPriceCurrency_textView.setText(Currency.getInstance().getCurrency());
                itemPrice_textView.setText(holder.itemPrice_textView.getText().toString());
                itemLastActive_textView.setText(holder.itemLastActive_textView.getText().toString());

                itemImage_imageView.setImageDrawable(holder.itemImage_imageView.getDrawable());

                //////

                itemName_textView.setVisibility(View.VISIBLE);
                itemName_textView.setTextColor(context.getResources().getColor(R.color.gradientDarkBlue));

                itemID_textView.setVisibility(View.VISIBLE);
                itemPriceCurrency_textView.setVisibility(View.VISIBLE);
                itemPrice_textView.setVisibility(View.VISIBLE);
                itemLastActive_textView.setVisibility(View.VISIBLE);

                itemImage_imageView.setVisibility(View.VISIBLE);

                itemList_alert_dialog.dismiss();
            }
        });
    }



    @Override
    public int getItemCount() {
        return exisitngItemsNamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName_textView,itemSerialNo_textView,itemPrice_textView,itemLastActive_textView,itemPriceCurrency_textView;
        ImageView itemImage_imageView;
        CardView searchForItem_cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            searchForItem_cardView = (CardView) itemView.findViewById(R.id.searchForItem_cardView);

            itemName_textView = (TextView)itemView.findViewById(R.id.itemName_textView);
            itemSerialNo_textView = (TextView)itemView.findViewById(R.id.itemSerialNo_textView);
            itemPrice_textView = (TextView)itemView.findViewById(R.id.itemPrice_textView);
            itemLastActive_textView = (TextView)itemView.findViewById(R.id.itemLastActive_textView);
            itemPriceCurrency_textView = (TextView)itemView.findViewById(R.id.itemPriceCurrency_textView);
            itemImage_imageView = (ImageView) itemView.findViewById(R.id.itemImage_imageView);

        }
    }
}

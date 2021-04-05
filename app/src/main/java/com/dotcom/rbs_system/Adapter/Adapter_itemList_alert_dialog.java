package com.dotcom.rbs_system.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_itemList_alert_dialog extends RecyclerView.Adapter<Adapter_itemList_alert_dialog.ViewHolder> {

    Context context;
    List<String> exisitngItemsNamesList, exisitngItemsSerialNoList,exisitngItemsKeyIDList,existingItemsPriceList,existingItemsLastActiveList,existingItemsImageUrlList;

    public Adapter_itemList_alert_dialog(Context context, List<String> exisitngItemsNamesList, List<String> exisitngItemsSerialNoList, List<String> exisitngItemsKeyIDList, List<String> existingItemsPriceList, List<String> existingItemsLastActiveList, List<String> existingItemsImageUrlList) {
        this.context = context;
        this.exisitngItemsNamesList = exisitngItemsNamesList;
        this.exisitngItemsSerialNoList = exisitngItemsSerialNoList;
        this.exisitngItemsKeyIDList = exisitngItemsKeyIDList;
        this.existingItemsPriceList = existingItemsPriceList;
        this.existingItemsLastActiveList = existingItemsLastActiveList;
        this.existingItemsImageUrlList = existingItemsImageUrlList;
    }

    @NonNull
    @Override
    public Adapter_itemList_alert_dialog.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Adapter_itemList_alert_dialog.ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_itemlist_alert_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_itemList_alert_dialog.ViewHolder holder, int position) {
        holder.itemName_textView.setText(exisitngItemsNamesList.get(position));
        holder.itemSerialNo_textView.setText(exisitngItemsSerialNoList.get(position));
        holder.itemPrice_textView.setText(existingItemsPriceList.get(position));
        holder.itemLastActive_textView.setText(existingItemsLastActiveList.get(position));
        holder.itemPriceCurrency_textView.setText(Currency.getInstance().getCurrency());

        Picasso.get().load(existingItemsImageUrlList.get(position)).into(holder.itemImage_imageView);
    }

    @Override
    public int getItemCount() {
        return exisitngItemsNamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName_textView,itemSerialNo_textView,itemPrice_textView,itemLastActive_textView,itemPriceCurrency_textView;
        ImageView itemImage_imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName_textView = (TextView)itemView.findViewById(R.id.itemName_textView);
            itemSerialNo_textView = (TextView)itemView.findViewById(R.id.itemSerialNo_textView);
            itemPrice_textView = (TextView)itemView.findViewById(R.id.itemPrice_textView);
            itemLastActive_textView = (TextView)itemView.findViewById(R.id.itemLastActive_textView);
            itemPriceCurrency_textView = (TextView)itemView.findViewById(R.id.itemPriceCurrency_textView);
            itemImage_imageView = (ImageView) itemView.findViewById(R.id.itemImage_imageView);

        }
    }
}

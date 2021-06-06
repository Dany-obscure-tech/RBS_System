package com.dotcom.rbs_system.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProductsOffersReceivedListRecyclerView extends  RecyclerView.Adapter<AdapterProductsOffersReceivedListRecyclerView.MyViewHolder> {

    DatabaseReference offerlistRef,customerOfferStatusRef;
    String category;
    Context context;
    String item_keyid;
    List<String> customer_keyid;
    List<String> customer_name;
    List<String> offered_price;
    List<String> product_offer_msg;
    List<String> profileImage;
    List<String> date_list;

    Activity activity;

    public AdapterProductsOffersReceivedListRecyclerView(Context context,String category,String item_keyid, List<String> customer_keyid, List<String> customer_name, List<String> product_offer_msg, List<String> offered_price, List<String> profileImage,List<String> date_list) {

        this.context = context;
        this.category = category;
        this.customer_name = customer_name;
        this.offered_price = offered_price;
        this.product_offer_msg = product_offer_msg;
        this.profileImage = profileImage;
        this.date_list = date_list;
        this.item_keyid = item_keyid;
        this.customer_keyid = customer_keyid;

        activity = (Activity)context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.recyclerview_shop_products_received_offers_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.customer_name.setText(customer_name.get(position));
        holder.offered_price.setText(offered_price.get(position));
        holder.product_offer_msg.setText(product_offer_msg.get(position));
        holder.date_textView.setText(date_list.get(position));
        holder.currency.setText(Currency.getInstance().getCurrency());

        Picasso.get().load(profileImage.get(position)).into(holder.profileImage);

        holder.accept_offer_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offerlistRef = FirebaseDatabase.getInstance().getReference("Stock/Shopkeepers/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+ category + "/" + item_keyid+"/");
                offerlistRef.child("Accepted_Offer").setValue(customer_keyid.get(position));
                customerOfferStatusRef = FirebaseDatabase.getInstance().getReference("Customer_offers/"+ customer_keyid.get(position)+"/"+ item_keyid+"/offer_status");
                customerOfferStatusRef.setValue("offer accepted");
                activity.recreate();
            }
        });
    }

    @Override
    public int getItemCount() {
        return customer_name.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView customer_name;
        TextView currency;
        TextView offered_price;
        TextView product_offer_msg;
        TextView accept_offer_textview;
        TextView date_textView;
        ImageView profileImage;
        TextView msg_offer_textview;


        public MyViewHolder(View itemView) {
            super(itemView);

            customer_name = (TextView) itemView.findViewById(R.id.customer_name);
            msg_offer_textview = (TextView) itemView.findViewById(R.id.msg_offer_textview);
            currency = (TextView) itemView.findViewById(R.id.currency);
            offered_price = (TextView) itemView.findViewById(R.id.offered_price);
            accept_offer_textview = (TextView) itemView.findViewById(R.id.accept_offer_textview);
            date_textView = (TextView) itemView.findViewById(R.id.date_textView);
            product_offer_msg = (TextView) itemView.findViewById(R.id.product_offer_msg);
            profileImage = (ImageView) itemView.findViewById(R.id.profileImage_imageView);
            profileImage = (ImageView) itemView.findViewById(R.id.profileImage_imageView);
        }
    }
}

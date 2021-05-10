package com.dotcom.rbs_system.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_customerList_alert_dialog extends RecyclerView.Adapter<Adapter_customerList_alert_dialog.ViewHolder> {

    Context context;

    List<String> exisitngCustomerList,exisitngCustomerKeyIDList,existingCustomerIDList,existingCustomerPhnoList,existingCustomerEmailList,existingCustomerImageUrlList;
    TextView customerName_textView,customerEmail_textView,customerPhno_textView;
    ImageView customerImage_imageView;
    Dialog customerList_alert_dialog;

    public Adapter_customerList_alert_dialog(Context context, List<String> exisitngCustomerList, List<String> exisitngCustomerKeyIDList, List<String> existingCustomerIDList, List<String> existingCustomerPhnoList, List<String> existingCustomerEmailList, List<String> existingCustomerImageUrlList, TextView customerName_textView, TextView customerEmail_textView, TextView customerPhno_textView, ImageView customerImage_imageView, Dialog customerList_alert_dialog) {
        this.context = context;
        this.exisitngCustomerList = exisitngCustomerList;
        this.exisitngCustomerKeyIDList = exisitngCustomerKeyIDList;
        this.existingCustomerIDList = existingCustomerIDList;
        this.existingCustomerPhnoList = existingCustomerPhnoList;
        this.existingCustomerEmailList = existingCustomerEmailList;
        this.existingCustomerImageUrlList = existingCustomerImageUrlList;
        this.customerName_textView = customerName_textView;
        this.customerEmail_textView = customerEmail_textView;
        this.customerPhno_textView = customerPhno_textView;
        this.customerImage_imageView = customerImage_imageView;
        this.customerList_alert_dialog = customerList_alert_dialog;
    }

    @NonNull
    @Override
    public Adapter_customerList_alert_dialog.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Adapter_customerList_alert_dialog.ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_customerlist_alert_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter_customerList_alert_dialog.ViewHolder holder, int position) {
        holder.customerName_textView.setText(exisitngCustomerList.get(position));
        holder.customerEmail_textView.setText(existingCustomerEmailList.get(position));
        holder.customerPhno_textView.setText(existingCustomerPhnoList.get(position));
        holder.customerID_textView.setText(existingCustomerIDList.get(position));

        Picasso.get().load(existingCustomerImageUrlList.get(position)).into(holder.customerImage_imageView);

        holder.searchForCustomer_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerName_textView.setText(holder.customerName_textView.getText().toString());
                customerEmail_textView.setText(holder.customerEmail_textView.getText().toString());
                customerPhno_textView.setText(holder.customerPhno_textView.getText().toString());

                customerImage_imageView.setImageDrawable(holder.customerImage_imageView.getDrawable());

                //////

                customerName_textView.setVisibility(View.VISIBLE);
                customerName_textView.setTextColor(context.getResources().getColor(R.color.gradientDarkBlue));

                customerEmail_textView.setVisibility(View.VISIBLE);
                customerPhno_textView.setVisibility(View.VISIBLE);
                customerImage_imageView.setVisibility(View.VISIBLE);

                customerList_alert_dialog.dismiss();
            }
        });
    }



    @Override
    public int getItemCount() {
        return exisitngCustomerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName_textView,customerEmail_textView,customerID_textView,customerPhno_textView;
        ImageView customerImage_imageView;
        CardView searchForCustomer_cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            searchForCustomer_cardView = (CardView) itemView.findViewById(R.id.searchForCustomer_cardView);

            customerName_textView = (TextView)itemView.findViewById(R.id.customerName_textView);
            customerEmail_textView = (TextView)itemView.findViewById(R.id.customerEmail_textView);
            customerPhno_textView = (TextView)itemView.findViewById(R.id.customerPhno_textView);
            customerID_textView = (TextView)itemView.findViewById(R.id.customerID_textView);
            customerImage_imageView = (ImageView) itemView.findViewById(R.id.customerImage_imageView);

        }
    }
}

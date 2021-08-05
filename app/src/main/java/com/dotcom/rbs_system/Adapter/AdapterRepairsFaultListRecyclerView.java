package com.dotcom.rbs_system.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.Classes.Repair_details_edit;
import com.dotcom.rbs_system.R;
import com.dotcom.rbs_system.AddRepairTicket;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterRepairsFaultListRecyclerView extends RecyclerView.Adapter<AdapterRepairsFaultListRecyclerView.ViewHolder> {
    Context context;
    Currency currencyObj;
    List<String> faultNameList, faultPriceList, faultKeyIDList;
    List<Boolean> faultRemoveCheckList;
    List<String> pendingFaultNameList, pendingFaultPriceList, pendingFaultKeyIDList;
    Activity activity;

    AddRepairTicket addRepairTicket;

    boolean pendingCheck;


    double incremantalAmount;
    double incremantalPendingAmount;

    public AdapterRepairsFaultListRecyclerView(Context context, List<String> faultNameList, List<String> faultPriceList, List<String> faultKeyIDList, List<Boolean> faultRemoveCheckList, List<String> pendingFaultNameList, List<String> pendingFaultPriceList, List<String> pendingFaultKeyIDList, AddRepairTicket addRepairTicket, boolean pendingCheck) {
        currencyObj = Currency.getInstance();

        this.context = context;
        this.faultNameList = faultNameList;
        this.faultPriceList = faultPriceList;
        this.faultKeyIDList = faultKeyIDList;
        this.faultRemoveCheckList=faultRemoveCheckList;

        this.pendingFaultNameList=pendingFaultNameList;
        this.pendingFaultPriceList=pendingFaultPriceList;
        this.pendingFaultKeyIDList=pendingFaultKeyIDList;


        activity = (Activity)context;
        this.addRepairTicket = addRepairTicket;

        this.pendingCheck=pendingCheck;

    }

    @NonNull
    @Override
    public AdapterRepairsFaultListRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_settings_faultlist_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterRepairsFaultListRecyclerView.ViewHolder holder, final int position) {
        holder.faultName_textView.setText(faultNameList.get(position));
        holder.faultPrice_textView.setText(currencyObj.getCurrency()+faultPriceList.get(position));

        if (faultKeyIDList==null){
            holder.remove_textView.setVisibility(View.GONE);
        }
        if (faultRemoveCheckList!=null){
            if (!faultRemoveCheckList.get(position)){
                holder.remove_textView.setVisibility(View.GONE);
            }else {
                holder.remove_textView.setVisibility(View.VISIBLE);
            }

        }
        if (faultKeyIDList!=null){

            if (position==faultNameList.size()-1){
                incremantalAmount = 0.0;
                if (!pendingCheck){
                    for (int i = 0;i<=position;i++){
                        incremantalAmount = incremantalAmount+(Double.parseDouble(faultPriceList.get(i)));
                    }
                }else {
                    incremantalAmount = Double.parseDouble(Repair_details_edit.getInstance().getAmount_TextView());
                }
                addRepairTicket.getIncremantalAmount(incremantalAmount);

            }
            if (position==faultNameList.size()-1){
                incremantalPendingAmount = 0.0;
                for (int i = 0;i<faultNameList.size();i++){
                    incremantalPendingAmount = incremantalPendingAmount+(Double.parseDouble(faultPriceList.get(i)));
                }
            }

            holder.remove_textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!pendingCheck){
                        incremantalAmount = incremantalAmount-(Double.parseDouble(faultPriceList.get(position)));
                    }


                    incremantalPendingAmount = incremantalPendingAmount-(Double.parseDouble(faultPriceList.get(position)));


                    faultNameList.remove(position);
                    faultPriceList.remove(position);
                    faultKeyIDList.remove(position);

                    pendingFaultNameList.remove(faultNameList.size()-position);
                    pendingFaultPriceList.remove(faultNameList.size()-position);
                    pendingFaultKeyIDList.remove(faultNameList.size()-position);


                    addRepairTicket.getIncremantalAmount(incremantalAmount);

                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return faultNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView faultName_textView,faultPrice_textView;
        TextView remove_textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            faultName_textView = (TextView)itemView.findViewById(R.id.faultName_textView);
            faultPrice_textView = (TextView)itemView.findViewById(R.id.faultPrice_textView);

            remove_textView = (TextView) itemView.findViewById(R.id.remove_textView);
        }
    }



}

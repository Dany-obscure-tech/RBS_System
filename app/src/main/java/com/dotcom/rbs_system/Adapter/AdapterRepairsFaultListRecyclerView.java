package com.dotcom.rbs_system.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterRepairsFaultListRecyclerView extends RecyclerView.Adapter<AdapterRepairsFaultListRecyclerView.ViewHolder> {
    Context context;
    Currency currencyObj;
    List<String> faultNameList, faultPriceList, faultKeyIDList;
    List<Boolean> faultRemoveCheckList;
    List<String> deletedPendingFaults;
    List<String> pendingFaultNameList, pendingFaultPriceList, pendingFaultKeyIDList;
    Activity activity;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Listed_faults");

    public AdapterRepairsFaultListRecyclerView(Context context, List<String> faultNameList, List<String> faultPriceList, List<String> faultKeyIDList, List<Boolean> faultRemoveCheckList, List<String> pendingFaultNameList,List<String> pendingFaultPriceList,List<String> pendingFaultKeyIDList) {
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
            holder.remove_textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    faultNameList.remove(position);
                    faultPriceList.remove(position);
                    faultKeyIDList.remove(position);


                    pendingFaultNameList.remove(faultNameList.size()-position);
                    pendingFaultPriceList.remove(faultNameList.size()-position);
                    pendingFaultKeyIDList.remove(faultNameList.size()-position);

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

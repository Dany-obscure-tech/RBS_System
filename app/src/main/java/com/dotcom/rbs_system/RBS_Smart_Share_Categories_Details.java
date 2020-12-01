package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.dotcom.rbs_system.Adapter.AdapterCategoryProductsItemRBSRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterCategoryProductsItemRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RBS_Smart_Share_Categories_Details extends AppCompatActivity {
    ImageButton back_btn;
    RecyclerView categoryRecyclerView;
    List<String> itemname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_b_s__smart__share__categories__details);
        initializations();
        onclicklistners();
        itemname.add("Tablet");
        AdapterCategoryProductsItemRBSRecyclerView adapterCategoryProductsItemRBSRecyclerView = new AdapterCategoryProductsItemRBSRecyclerView(RBS_Smart_Share_Categories_Details.this, itemname, null, null);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(RBS_Smart_Share_Categories_Details.this,2));
        categoryRecyclerView.setAdapter(adapterCategoryProductsItemRBSRecyclerView);

    }

    private void initializations() {
        back_btn=findViewById(R.id.back_btn);
        categoryRecyclerView=(RecyclerView)findViewById(R.id.categoryRecyclerView);
        itemname = new ArrayList<String>();
    }

    private void onclicklistners() {
        back_btn_listner();
    }

    private void back_btn_listner() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
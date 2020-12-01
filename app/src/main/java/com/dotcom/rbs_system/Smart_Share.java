package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.dotcom.rbs_system.Adapter.AdapterCategoryRBS_RecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterCategoryRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterRBSGalleryItemListRecyclerView;
import com.dotcom.rbs_system.Adapter.AdapterSpotlightItemListRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Smart_Share extends AppCompatActivity {
    ImageButton Back_btn;
    RecyclerView category_recyclerview,galleryRecyclerView;
    List<String> category_text,itemname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart__share);
        initialization();
        onclicklistners();
        category_text.add("PC");
        itemname.add("PC");
        AdapterCategoryRBS_RecyclerView adapterCategoryRBS_recyclerView=new AdapterCategoryRBS_RecyclerView(Smart_Share.this,null,category_text);
        category_recyclerview.setLayoutManager(new LinearLayoutManager(Smart_Share.this,RecyclerView.HORIZONTAL,true));
        category_recyclerview.setAdapter(adapterCategoryRBS_recyclerView);

        AdapterRBSGalleryItemListRecyclerView adapterRBSGalleryItemListRecyclerView = new AdapterRBSGalleryItemListRecyclerView(Smart_Share.this, itemname, null, null,null,null,null);
        galleryRecyclerView.setLayoutManager(new GridLayoutManager(Smart_Share.this,2));
        galleryRecyclerView.setAdapter(adapterRBSGalleryItemListRecyclerView);

    }

    private void initialization() {
        Back_btn=findViewById(R.id.Back_btn);
        category_recyclerview=findViewById(R.id.category_recyclerview);
        galleryRecyclerView=findViewById(R.id.galleryRecyclerView);
        category_text = new ArrayList<>();
        itemname = new ArrayList<>();
    }

    private void onclicklistners() {
        Back_btn_listner();
    }

    private void Back_btn_listner() {
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
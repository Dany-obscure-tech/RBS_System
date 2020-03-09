package com.dotcom.rbs_system.Model;

import ir.mirrajabi.searchdialog.core.Searchable;

public class SampleSearchModel implements Searchable {
    private String mTitle;
    private String mID;
    private String mName;
    private String mCategory;

    public SampleSearchModel(String title, String id,String name, String category) {
        mTitle = title;
        mID = id;
        mName = name;
        mCategory = category;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public String getId() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public String getCategory() {
        return mCategory;
    }

    public SampleSearchModel setTitle(String title) {
        mTitle = title;
        return this;
    }
}

package com.dotcom.rbs_system.Model;

import ir.mirrajabi.searchdialog.core.Searchable;

public class SampleSearchModel implements Searchable {
    private String mTitle;
    private String mID;
    private String mName;
    private String mval1;
    private String mval2;
    private String mval3;
    private String mval4;

    public SampleSearchModel(String title, String id,String name, String val1, String val2, String val3, String val4) {
        mTitle = title;
        mID = id;
        mName = name;
        mval1 = val1;
        mval2 = val2;
        mval3 = val3;
        mval4 = val4;

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

    public String getVal1() {
        return mval1;
    }

    public SampleSearchModel setTitle(String title) {
        mTitle = title;
        return this;
    }

    public String getVal2() {
        return mval2;
    }

    public String getVal3() {
        return mval3;
    }

    public String getVal4() {
        return mval4;
    }
}

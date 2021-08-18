package com.dotcom.rbs_system.Classes;

import java.util.ArrayList;
import java.util.List;

public class RBSVendorSelectedStock {
    private static RBSVendorSelectedStock rbsVendorSelectedStock = new RBSVendorSelectedStock();

    private static List<String> vendor_stock_category_textView;
    private static List<String> vendor_stockName_textView;
    private static List<String> vendor_stock_price_textview;
    private static List<String> vendor_stock_quantity_textView;
    private static List<String> vendor_stock_imageView;
    private static List<String> vendor_stock_keyID_list;



    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void listInitialization(){
        vendor_stock_category_textView = new ArrayList<>();
        vendor_stockName_textView = new ArrayList<>();
        vendor_stock_price_textview = new ArrayList<>();
        vendor_stock_quantity_textView = new ArrayList<>();
        vendor_stock_imageView = new ArrayList<>();
        vendor_stock_keyID_list = new ArrayList<>();
    }

    public List<String> getVendor_stock_category_textView() {
        return vendor_stock_category_textView;
    }

    public void setVendor_stock_category_textView(List<String> vendor_stock_category_textView) {
        this.vendor_stock_category_textView = vendor_stock_category_textView;
    }

    public List<String> getVendor_stockName_textView() {
        return vendor_stockName_textView;
    }

    public void setVendor_stock_keyID_list(List<String> vendor_stock_keyID_list) {
        this.vendor_stock_keyID_list = vendor_stock_keyID_list;
    }

    public List<String> getVendor_stock_keyID_list() {
        return vendor_stock_keyID_list;
    }

    public void setVendor_stockName_textView(List<String> vendor_stockName_textView) {
        this.vendor_stockName_textView = vendor_stockName_textView;
    }

    public List<String> getVendor_stock_price_textview() {
        return vendor_stock_price_textview;
    }

    public void setVendor_stock_price_textview(List<String> vendor_stock_price_textview) {
        this.vendor_stock_price_textview = vendor_stock_price_textview;
    }

    public List<String> getVendor_stock_quantity_textView() {
        return vendor_stock_quantity_textView;
    }

    public void setVendor_stock_quantity_textView(List<String> vendor_stock_quantity_textView) {
        this.vendor_stock_quantity_textView = vendor_stock_quantity_textView;
    }

    public List<String> getVendor_stock_imageView() {
        return vendor_stock_imageView;
    }

    public void setVendor_stock_imageView(List<String> vendor_stock_imageView) {
        this.vendor_stock_imageView = vendor_stock_imageView;
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////

    private RBSVendorSelectedStock(){}

    public static RBSVendorSelectedStock getInstance(){

        return rbsVendorSelectedStock;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public  void clearData(){
        vendor_stock_category_textView.clear();
        vendor_stockName_textView.clear();
        vendor_stock_price_textview.clear();
        vendor_stock_quantity_textView.clear();
        vendor_stock_imageView.clear();
        vendor_stock_keyID_list.clear();
    }
}

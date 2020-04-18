package com.dotcom.rbs_system.Classes;

import android.net.Uri;

public class Exchanged_itemdata {
    static Exchanged_itemdata obj = new Exchanged_itemdata();
    private Boolean exchangeCheck = false;

    String Customer_keyID;
    String Item_keyID;
    String Purchase_price;
    String Quantity;
    String Date;
    String Cash;
    String Voucher;
    String Paid;
    String Name;
    String Condition;
    String Category;
    String Notes;




    private Exchanged_itemdata(){}

    public Boolean getExchangeCheck() {
        return exchangeCheck;
    }

    public void setExchangeCheck(Boolean exchangeCheck) {
        this.exchangeCheck = exchangeCheck;
    }

    public String getCustomer_keyID() {
        return Customer_keyID;
    }

    public void setCustomer_keyID(String customer_keyID) {
        Customer_keyID = customer_keyID;
    }

    public String getItem_keyID() {
        return Item_keyID;
    }

    public void setItem_keyID(String item_keyID) {
        Item_keyID = item_keyID;
    }

    public String getPurchase_price() {
        return Purchase_price;
    }

    public void setPurchase_price(String purchase_price) {
        Purchase_price = purchase_price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCash() {
        return Cash;
    }

    public void setCash(String cash) {
        Cash = cash;
    }

    public String getVoucher() {
        return Voucher;
    }

    public void setVoucher(String voucher) {
        Voucher = voucher;
    }

    public String getPaid() {
        return Paid;
    }

    public void setPaid(String paid) {
        Paid = paid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public static Exchanged_itemdata getInstance(){
        return obj;
    }
}

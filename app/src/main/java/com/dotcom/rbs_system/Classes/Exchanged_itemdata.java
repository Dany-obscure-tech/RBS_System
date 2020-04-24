package com.dotcom.rbs_system.Classes;

import android.net.Uri;

public class Exchanged_itemdata {
    static Exchanged_itemdata obj = new Exchanged_itemdata();
    private Boolean exchangeCheck = false;
    private Boolean exchangeFromBuyCheck = false;

    String Customer_keyID = "";
    String CustomerButtonText = "";
    String phNo = "";
    String dob = "";
    String address = "";
    String email = "";

    String Item_keyID = "";
    String Purchase_price = "";
    String Quantity = "";
    String Date = "";
    String Cash = "";
    String Voucher = "";
    String Paid = "";
    String Name = "";
    String Condition = "";
    String Category = "";
    String Notes = "";




    private Exchanged_itemdata(){}

    public void clearData(){
        Customer_keyID = "";
        CustomerButtonText = "";
        phNo = "";
        dob = "";
        address = "";
        email = "";

        Item_keyID = "";
        Purchase_price = "";
        Quantity = "";
        Date = "";
        Cash = "";
        Voucher = "";
        Paid = "";
        Name = "";
        Condition = "";
        Category = "";
        Notes = "";
    }

    public Boolean getExchangeCheck() {
        return exchangeCheck;
    }

    public void setExchangeCheck(Boolean exchangeCheck) {
        this.exchangeCheck = exchangeCheck;
    }

    public String getCustomer_keyID() {
        return Customer_keyID;
    }

    public String getCustomerButtonText() {
        return CustomerButtonText;
    }

    public void setCustomerButtonText(String customerButtonText) {
        CustomerButtonText = customerButtonText;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Boolean getExchangeFromBuyCheck() {
        return exchangeFromBuyCheck;
    }

    public void setExchangeFromBuyCheck(Boolean exchangeFromBuyCheck) {
        this.exchangeFromBuyCheck = exchangeFromBuyCheck;
    }

    public static Exchanged_itemdata getInstance(){
        return obj;
    }
}
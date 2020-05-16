package com.dotcom.rbs_system.Classes;

import java.util.Date;
import java.util.List;

public class Repair_details_edit {
    static Repair_details_edit obj = new Repair_details_edit();

    String itemKeyID;
    String itemName_textView;
    String serialNo_textView;
    String category_textView;
    String condition_textView;
    String notes_textView;

    String customerKeyID;
    String customerName_textView;
    String id_textView;
    String phno_textView;
    String dob_textView;
    String address_textView;
    String email_textView;

    String ticketNo_TextView;
    String agreedPrice_TextView;
    String date_TextView;
    String paidAmount_TextView;
    String balanceAmount_TextView;
    String specialConditions_TextView;

    List<String> faultNameList;
    List<String> faultPriceList;
    List<String> faultKeyIDList;

    long timestamp;


    public String getItemKeyID() {
        return itemKeyID;
    }

    public void setItemKeyID(String itemKeyID) {
        this.itemKeyID = itemKeyID;
    }

    public String getItemName_textView() {
        return itemName_textView;
    }

    public void setItemName_textView(String itemName_textView) {
        this.itemName_textView = itemName_textView;
    }

    public String getSerialNo_textView() {
        return serialNo_textView;
    }

    public void setSerialNo_textView(String serialNo_textView) {
        this.serialNo_textView = serialNo_textView;
    }

    public String getCategory_textView() {
        return category_textView;
    }

    public void setCategory_textView(String category_textView) {
        this.category_textView = category_textView;
    }

    public String getCondition_textView() {
        return condition_textView;
    }

    public void setCondition_textView(String condition_textView) {
        this.condition_textView = condition_textView;
    }

    public String getNotes_textView() {
        return notes_textView;
    }

    public void setNotes_textView(String notes_textView) {
        this.notes_textView = notes_textView;
    }

    public String getCustomerKeyID() {
        return customerKeyID;
    }

    public void setCustomerKeyID(String customerKeyID) {
        this.customerKeyID = customerKeyID;
    }

    public String getCustomerName_textView() {
        return customerName_textView;
    }

    public void setCustomerName_textView(String customerName_textView) {
        this.customerName_textView = customerName_textView;
    }

    public String getId_textView() {
        return id_textView;
    }

    public void setId_textView(String id_textView) {
        this.id_textView = id_textView;
    }

    public String getPhno_textView() {
        return phno_textView;
    }

    public void setPhno_textView(String phno_textView) {
        this.phno_textView = phno_textView;
    }

    public String getDob_textView() {
        return dob_textView;
    }

    public void setDob_textView(String dob_textView) {
        this.dob_textView = dob_textView;
    }

    public String getAddress_textView() {
        return address_textView;
    }

    public void setAddress_textView(String address_textView) {
        this.address_textView = address_textView;
    }

    public String getEmail_textView() {
        return email_textView;
    }

    public void setEmail_textView(String email_textView) {
        this.email_textView = email_textView;
    }

    public String getTicketNo_TextView() {
        return ticketNo_TextView;
    }

    public void setTicketNo_TextView(String ticketNo_TextView) {
        this.ticketNo_TextView = ticketNo_TextView;
    }

    public String getAgreedPrice_TextView() {
        return agreedPrice_TextView;
    }

    public void setAgreedPrice_TextView(String agreedPrice_TextView) {
        this.agreedPrice_TextView = agreedPrice_TextView;
    }

    public String getDate_TextView() {
        return date_TextView;
    }

    public void setDate_TextView(String date_TextView) {
        this.date_TextView = date_TextView;
    }

    public String getPaidAmount_TextView() {
        return paidAmount_TextView;
    }

    public void setPaidAmount_TextView(String paidAmount_TextView) {
        this.paidAmount_TextView = paidAmount_TextView;
    }

    public String getBalanceAmount_TextView() {
        return balanceAmount_TextView;
    }

    public void setBalanceAmount_TextView(String balanceAmount_TextView) {
        this.balanceAmount_TextView = balanceAmount_TextView;
    }

    public String getSpecialConditions_TextView() {
        return specialConditions_TextView;
    }

    public void setSpecialConditions_TextView(String specialConditions_TextView) {
        this.specialConditions_TextView = specialConditions_TextView;
    }

    public List<String> getFaultNameList() {
        return faultNameList;
    }

    public void setFaultNameList(List<String> faultNameList) {
        this.faultNameList = faultNameList;
    }

    public List<String> getFaultPriceList() {
        return faultPriceList;
    }

    public void setFaultPriceList(List<String> faultPriceList) {
        this.faultPriceList = faultPriceList;
    }

    public List<String> getFaultKeyIDList() {
        return faultKeyIDList;
    }

    public void setFaultKeyIDList(List<String> faultKeyIDList) {
        this.faultKeyIDList = faultKeyIDList;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    private Repair_details_edit() {
    }

    public static Repair_details_edit getInstance(){
        return obj;
    }
}

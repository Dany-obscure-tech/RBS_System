package com.dotcom.rbs_system.Classes;

import java.util.List;

public class Repair_details_edit {
    static Repair_details_edit obj = new Repair_details_edit();

    String itemKeyID;
    String itemName_textView;
    String serialNo_textView;
    String category_textView;
    String condition_textView;
    String notes_textView;
    String lastActive_textView;

    String customerKeyID;
    String customerName_textView;
    String id_textView;
    String phno_textView;
    String dob_textView;
    String address_textView;
    String email_textView;

    String ticketNo_TextView;
    String ticketkeyId_TextView;
    String amount_TextView;
    String date_TextView;
    String specialConditions_TextView;

    String pendingAmount_TextView;

    List<String> faultNameList;
    List<String> faultPriceList;
    List<String> faultKeyIDList;

    List<String> pendingFaultNameList;
    List<String> pendingFaultPriceList;
    List<String> pendingFaultKeyIDList;

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

    public String getLastActive_textView() {
        return lastActive_textView;
    }

    public void setLastActive_textView(String lastActive_textView) {
        this.lastActive_textView = lastActive_textView;
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

    public String getTicketkeyId_TextView() {
        return ticketkeyId_TextView;
    }

    public void setTicketkeyId_TextView(String ticketkeyId_TextView) {
        this.ticketkeyId_TextView = ticketkeyId_TextView;
    }

    public String getAmount_TextView() {
        return amount_TextView;
    }

    public void setAmount_TextView(String amount_TextView) {
        this.amount_TextView = amount_TextView;
    }

    public String getDate_TextView() {
        return date_TextView;
    }

    public void setDate_TextView(String date_TextView) {
        this.date_TextView = date_TextView;
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

    public String getPendingAmount_TextView() {
        return pendingAmount_TextView;
    }

    public void setPendingAmount_TextView(String pendingAmount_TextView) {
        this.pendingAmount_TextView = pendingAmount_TextView;
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

    public List<String> getPendingFaultNameList() {
        return pendingFaultNameList;
    }

    public void setPendingFaultNameList(List<String> pendingFaultNameList) {
        this.pendingFaultNameList = pendingFaultNameList;
    }

    public List<String> getPendingFaultPriceList() {
        return pendingFaultPriceList;
    }

    public void setPendingFaultPriceList(List<String> pendingFaultPriceList) {
        this.pendingFaultPriceList = pendingFaultPriceList;
    }

    public List<String> getPendingFaultKeyIDList() {
        return pendingFaultKeyIDList;
    }

    public void setPendingFaultKeyIDList(List<String> pendingFaultKeyIDList) {
        this.pendingFaultKeyIDList = pendingFaultKeyIDList;
    }

    public void clear(){
        this.itemKeyID = null;
        this.serialNo_textView = null;
        this.category_textView = null;
        this.condition_textView = null;
        this.notes_textView = null;

        this.customerKeyID = null;
        this.customerName_textView = null;
        this.id_textView = null;
        this.phno_textView = null;
        this.dob_textView = null;
        this.address_textView = null;
        this.email_textView = null;

        this.ticketNo_TextView = null;
        this.amount_TextView = null;
        this.date_TextView = null;
        this.specialConditions_TextView = null;

        this.faultNameList = null;
        this.faultPriceList = null;
        this.faultKeyIDList = null;

        this.pendingFaultNameList = null;
        this.pendingFaultPriceList = null;
        this.pendingFaultKeyIDList = null;
        this.pendingAmount_TextView = null;
    }


    private Repair_details_edit() {
    }

    public static Repair_details_edit getInstance(){
        return obj;
    }
}

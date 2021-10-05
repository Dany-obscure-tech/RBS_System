package com.dotcom.rbs_system.Classes;

import java.util.ArrayList;
import java.util.List;

public class RepairTicketFaults {
    private static RepairTicketFaults repairTicketFaultsObj = new RepairTicketFaults();

    List<String> faultNameList;
    List<String> faultPriceList;

    Boolean pendingFaults = false;
    List<String> pendingFaultNameList;
    List<String> pendingFaultPriceList;


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

    public Boolean getPendingFaults() {
        return pendingFaults;
    }

    public void setPendingFaults(Boolean pendingFaults) {
        this.pendingFaults = pendingFaults;
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

    private RepairTicketFaults() {}

    public static RepairTicketFaults getInstance() {
        return repairTicketFaultsObj;
    }
}

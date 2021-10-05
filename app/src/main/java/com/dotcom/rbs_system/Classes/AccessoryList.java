package com.dotcom.rbs_system.Classes;

import java.util.List;

public class AccessoryList {
    private static AccessoryList accessoryListObj = new AccessoryList();

    List<String> accessoryNameList;
    List<String> accessoryQuantityList;


    public List<String> getAccessoryNameList() {
        return accessoryNameList;
    }

    public void setAccessoryNameList(List<String> accessoryNameList) {
        this.accessoryNameList = accessoryNameList;
    }

    public List<String> getAccessoryQuantityList() {
        return accessoryQuantityList;
    }

    public void setAccessoryQuantityList(List<String> accessoryQuantityList) {
        this.accessoryQuantityList = accessoryQuantityList;
    }

    private AccessoryList() {}

    public static AccessoryList getInstance() {
        return accessoryListObj;
    }
}

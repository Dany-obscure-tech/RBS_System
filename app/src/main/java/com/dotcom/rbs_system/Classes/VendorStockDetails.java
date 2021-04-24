package com.dotcom.rbs_system.Classes;

public class VendorStockDetails {
    private static VendorStockDetails vendorStockDetails = new VendorStockDetails();
    String category;
    String keyId;

    private VendorStockDetails(){}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public static VendorStockDetails getInstance(){
        return vendorStockDetails;
    }

}

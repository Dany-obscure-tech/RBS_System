package com.dotcom.rbs_system.Classes;

public class CustomerPurchaseHistoryItemDetails_class {
    private static CustomerPurchaseHistoryItemDetails_class currencyObj = new CustomerPurchaseHistoryItemDetails_class();

    String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    private CustomerPurchaseHistoryItemDetails_class() {}

    public static CustomerPurchaseHistoryItemDetails_class getInstance() {
        return currencyObj;
    }
}

package com.dotcom.rbs_system.Classes;

public class Currency {
    private static Currency currencyObj = new Currency();

    String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    private Currency() {}

    public static Currency getInstance() {
        return currencyObj;
    }
}

package com.dotcom.rbs_system.Classes;

public class TermsAndConditions {
    private static TermsAndConditions termsAndConditionsObj = new TermsAndConditions();

    String termsAndConditions;

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    private TermsAndConditions() {}

    public static TermsAndConditions getInstance() {
        return termsAndConditionsObj;
    }
}

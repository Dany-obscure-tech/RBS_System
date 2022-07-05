package com.dotcom.rbs_system.Classes;

public class UserDetails {
    private static UserDetails userDetailsObj = new UserDetails();

    String name,address,phno,email,profileImageUrl,customerID;
    Boolean isShopkeeper;
    Boolean isVendor;
    String shopName,shopLogo,shopBanner,shopAddress,shopEmail,shopPhno,shopTermsAndConditions;
    String vendorName,vendorLogo,vendorAppRegNo,vendorBanner,vendorAddress,vendorEmail,vendorPostCode,vendorRegNo,vendorUrl,vendorPhno,vendorTermsAndConditions;

    String defaultProfileImage;
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Boolean getShopkeeper() {
        return isShopkeeper;
    }

    public void setShopkeeper(Boolean shopkeeper) {
        isShopkeeper = shopkeeper;
    }

    public Boolean getVendor() {
        return isVendor;
    }

    public void setVendor(Boolean vendor) {
        isVendor = vendor;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopBanner() {
        return shopBanner;
    }

    public void setShopBanner(String shopBanner) {
        this.shopBanner = shopBanner;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public String getShopPhno() {
        return shopPhno;
    }

    public void setShopPhno(String shopPhno) {
        this.shopPhno = shopPhno;
    }

    public String getShopTermsAndConditions() {
        return shopTermsAndConditions;
    }

    public void setShopTermsAndConditions(String shopTermsAndConditions) {
        this.shopTermsAndConditions = shopTermsAndConditions;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorLogo() {
        return vendorLogo;
    }

    public void setVendorLogo(String vendorLogo) {
        this.vendorLogo = vendorLogo;
    }

    public String getVendorAppRegNo() {
        return vendorAppRegNo;
    }

    public void setVendorAppRegNo(String vendorAppRegNo) {
        this.vendorAppRegNo = vendorAppRegNo;
    }

    public String getVendorBanner() {
        return vendorBanner;
    }

    public void setVendorBanner(String vendorBanner) {
        this.vendorBanner = vendorBanner;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public String getVendorPostCode() {
        return vendorPostCode;
    }

    public void setVendorPostCode(String vendorPostCode) {
        this.vendorPostCode = vendorPostCode;
    }

    public String getVendorRegNo() {
        return vendorRegNo;
    }

    public void setVendorRegNo(String vendorRegNo) {
        this.vendorRegNo = vendorRegNo;
    }

    public String getVendorUrl() {
        return vendorUrl;
    }

    public void setVendorUrl(String vendorUrl) {
        this.vendorUrl = vendorUrl;
    }

    public String getVendorPhno() {
        return vendorPhno;
    }

    public void setVendorPhno(String vendorPhno) {
        this.vendorPhno = vendorPhno;
    }

    public String getVendorTermsAndConditions() {
        return vendorTermsAndConditions;
    }

    public void setVendorTermsAndConditions(String vendorTermsAndConditions) {
        this.vendorTermsAndConditions = vendorTermsAndConditions;
    }

    public String getDefaultProfileImage() {
        return defaultProfileImage;
    }

    public void setDefaultProfileImage(String defaultProfileImage) {
        this.defaultProfileImage = defaultProfileImage;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private UserDetails() {}

    public static UserDetails getInstance() {
        return userDetailsObj;
    }
}

package com.dotcom.rbs_system.Classes;

public class UserDetails {
    private static UserDetails userDetailsObj = new UserDetails();

    String name,address,phno,email,profileImageUrl;
    Boolean isShopkeeper;
    String shopName,shopLogo,shopBanner,shopAddress,shopEmail,shopPhno,shopTermsAndConditions;

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


    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Boolean getShopkeeper() {
        return isShopkeeper;
    }

    public void setShopkeeper(Boolean shopkeeper) {
        isShopkeeper = shopkeeper;
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

    private UserDetails() {}

    public static UserDetails getInstance() {
        return userDetailsObj;
    }
}

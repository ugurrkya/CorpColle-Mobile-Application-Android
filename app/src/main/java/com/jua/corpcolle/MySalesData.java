package com.jua.corpcolle;

public class MySalesData {
    private String businessid;
    private String businessname;
    private String clientid;
    private String date;
    private String productdescription;
    private String productimage;
    private String productprice;
    private String userAddress;
    private String userPhone;
    private String useridentity;
    private String userimage;
    private String productname;

    public MySalesData(String businessid, String businessname, String clientid, String date, String productdescription, String productimage, String productprice, String userAddress, String userPhone, String useridentity, String userimage, String productname) {
        this.businessid = businessid;
        this.businessname = businessname;
        this.clientid = clientid;
        this.date = date;
        this.productdescription = productdescription;
        this.productimage = productimage;
        this.productprice = productprice;
        this.userAddress = userAddress;
        this.userPhone = userPhone;
        this.useridentity = useridentity;
        this.userimage = userimage;
        this.productname = productname;
    }

    public MySalesData() {
    }

    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUseridentity() {
        return useridentity;
    }

    public void setUseridentity(String useridentity) {
        this.useridentity = useridentity;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }
}

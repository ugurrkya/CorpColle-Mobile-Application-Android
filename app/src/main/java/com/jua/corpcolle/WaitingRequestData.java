package com.jua.corpcolle;

public class WaitingRequestData {
    private String requestid;
    private String clientid;
    private String businessid;
    private String businessimage;
    private String businessname;
    private String situation;
    private String productname;
    private String productprice;
    private String productdescription;
    private String productimage;
    private String useridentity;
    private String userPhone;
    private String userimage;
    private String date;
    private String userAddress;

    public WaitingRequestData(String requestid, String clientid, String businessid, String businessimage, String businessname, String situation, String productname, String productprice, String productdescription, String productimage, String useridentity, String userPhone, String userimage, String date,String userAddress) {
        this.requestid = requestid;
        this.clientid = clientid;
        this.businessid = businessid;
        this.businessimage = businessimage;
        this.businessname = businessname;
        this.situation = situation;
        this.productname = productname;
        this.productprice = productprice;
        this.productdescription = productdescription;
        this.productimage = productimage;
        this.useridentity = useridentity;
        this.userPhone = userPhone;
        this.userimage = userimage;
        this.date = date;
        this.userAddress = userAddress;
    }

    public WaitingRequestData() {
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }

    public String getBusinessimage() {
        return businessimage;
    }

    public void setBusinessimage(String businessimage) {
        this.businessimage = businessimage;
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
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

    public String getUseridentity() {
        return useridentity;
    }

    public void setUseridentity(String useridentity) {
        this.useridentity = useridentity;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
}

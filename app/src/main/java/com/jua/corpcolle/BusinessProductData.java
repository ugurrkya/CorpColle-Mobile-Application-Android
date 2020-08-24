package com.jua.corpcolle;

public class BusinessProductData {
    private String businessemail;
    private String businessabout;
    private String businessid;
    private String businessimage;
    private String businessname;
    private String productimage;
    private String productprice;
    private String productdescription;
    private String productname;

    public BusinessProductData(String businessemail, String businessabout, String businessid, String businessimage, String businessname,String productimage, String productprice, String productdescription, String productname) {
        this.businessemail = businessemail;
        this.businessabout = businessabout;
        this.businessid = businessid;
        this.businessimage = businessimage;
        this.businessname = businessname;
        this.productimage = productimage;
        this.productprice = productprice;
        this.productdescription = productdescription;
        this.productname = productname;
    }

    public BusinessProductData() {
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getBusinessemail() {
        return businessemail;
    }

    public void setBusinessemail(String businessemail) {
        this.businessemail = businessemail;
    }

    public String getBusinessabout() {
        return businessabout;
    }

    public void setBusinessabout(String businessabout) {
        this.businessabout = businessabout;
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

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription;
    }
}

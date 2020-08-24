package com.jua.corpcolle;

public class BusinessData {
    private String businessemail;
    private String businessabout;
    private String businessid;
    private String businessimage;
    private String businessname;
    private String businesspass;

    public BusinessData(String businessemail, String businessabout, String businessid, String businessimage, String businessname, String businesspass) {
        this.businessemail = businessemail;
        this.businessabout = businessabout;
        this.businessid = businessid;
        this.businessimage = businessimage;
        this.businessname = businessname;
        this.businesspass = businesspass;
    }

    public BusinessData() {
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

    public String getBusinesspass() {
        return businesspass;
    }

    public void setBusinesspass(String businesspass) {
        this.businesspass = businesspass;
    }
}

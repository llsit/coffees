package com.cmu.nuts.coffee9.model;

/**
 * Created by tcdm053 on 31/1/2561.
 */

public class Shop {
    private String sid;
    private String name;
    private String address;
    private String detail;
    private String locationl;
    private String open_houre;
    private String price;
    private String uid;

    public static String tag = "Shop";

    public Shop(String sid, String name, String address, String detail, String locationl, String open_houre, String price, String uid) {
        this.sid = sid;
        this.name = name;
        this.address = address;
        this.detail = detail;
        this.locationl = locationl;
        this.open_houre = open_houre;
        this.price = price;
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getLocationl() {
        return locationl;
    }

    public void setLocationl(String locationl) {
        this.locationl = locationl;
    }

    public String getOpen_houre() {
        return open_houre;
    }

    public void setOpen_houre(String open_houre) {
        this.open_houre = open_houre;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public static String getTag() {
        return tag;
    }

    public static void setTag(String tag) {
        Shop.tag = tag;
    }
}

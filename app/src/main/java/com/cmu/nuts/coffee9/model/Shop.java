package com.cmu.nuts.coffee9.model;

import org.parceler.Parcel;

/**
 * Created by tcdm053 on 31/1/2561.
 */

@Parcel
public class Shop {
    String sid;
    String name;
    String address;
    String detail;
    String location;
    String rating;
    String price;
    String uid;


    public Shop(String sid, String name, String address, String detail, String location, String rating, String price, String uid) {
        this.sid = sid;
        this.name = name;
        this.address = address;
        this.detail = detail;
        this.location = location;
        this.rating = rating;
        this.price = price;
        this.uid = uid;
    }

    public static String tag = "coffee_shop";

    public Shop() {
    }

    public static void setTag(String tag) {
        Shop.tag = tag;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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
}

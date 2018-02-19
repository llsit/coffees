package com.cmu.nuts.coffee9.main.model;

/*
 * Created by nuts on 1/27/2018.
 */

import android.widget.EditText;
import android.widget.RadioButton;

public class AddDataShop {
    private String name;
    private String address;
    private String detail;
    private String authorID;
    private String location;
    private String open_hour;
    private String price;

    public AddDataShop(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public AddDataShop(String name, String addressshop, String detail, String authorID, EditText location, EditText open_hour, RadioButton price) {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public AddDataShop(String name, String address, String detail, String authorID, String location, String open_hour, String price) {
        this.name = name;
        this.address = address;
        this.detail = detail;
        this.authorID = authorID;
        this.location = location;
        this.open_hour = open_hour;
        this.price = price;
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

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOpen_hour() {
        return open_hour;
    }

    public void setOpen_hour(String open_hour) {
        this.open_hour = open_hour;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

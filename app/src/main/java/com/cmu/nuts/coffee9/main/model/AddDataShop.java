package com.cmu.nuts.coffee9.main.model;

/*
 * Created by nuts on 1/27/2018.
 */

public class AddDataShop {
    public String nameshop;
    public String Addressshop;
    public String detail,authorID;
    public String min,mid,max;



    public AddDataShop() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public AddDataShop(String nameshop, String Addressshop, String detail, String authorID) {
        this.nameshop = nameshop;
        this.Addressshop = Addressshop;
        this.detail = detail;
        this.authorID = authorID;
    }
}

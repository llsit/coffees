package com.example.nuts.coffee9.main;

/*
 * Created by nuts on 1/27/2018.
 */

public class addDataShop {
    public String nameshop;
    public String Addressshop;
    public String detail,authorID;
    public String min,mid,max;



    public addDataShop() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public addDataShop(String nameshop, String Addressshop,String detail,String authorID) {
        this.nameshop = nameshop;
        this.Addressshop = Addressshop;
        this.detail = detail;
        this.authorID = authorID;
    }
}

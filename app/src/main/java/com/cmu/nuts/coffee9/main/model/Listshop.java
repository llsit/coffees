package com.cmu.nuts.coffee9.main.model;

/*
 * Created by nuts on 1/31/2018.
 */

public class Listshop {

    private String nameshop;
    private String detail;
    private String address;

    public Listshop(String nameshop, String detail, String address) {
        this.nameshop = nameshop;
        this.detail = detail;
        this.address = address;

    }

    public Listshop() {
    }

    public String getNameshop() {
        return nameshop;
    }

    public void setNameshop(String nameshop) {
        this.nameshop = nameshop;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getaddress() {
        return address;
    }

    public void setAddressshop(String addressshop) {
        address = addressshop;
    }
}

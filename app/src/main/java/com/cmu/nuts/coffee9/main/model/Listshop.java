package com.cmu.nuts.coffee9.main.model;

/*
 * Created by nuts on 1/31/2018.
 */

public class Listshop {

    private String nameshop;
    private String detail;
    private String Addressshop;

    public Listshop(String nameshop, String detail, String Addressshop) {
        this.nameshop = nameshop;
        this.detail = detail;
        this.Addressshop = Addressshop;

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

    public String getAddressshop() {
        return Addressshop;
    }

    public void setAddressshop(String addressshop) {
        Addressshop = addressshop;
    }
}

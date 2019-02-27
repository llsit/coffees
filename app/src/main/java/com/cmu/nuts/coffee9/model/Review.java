package com.cmu.nuts.coffee9.model;

/**
 * Created by Jaylers on 07-Feb-18.
 */

public class Review {
    private String rid;
    private String uid;
    private String sid;
    private String detail;
    private String img_url;
    private String datetime;
    private String star;

    public static String tag = "Review";

    public Review() {
    }

    public static String getTag() {
        return tag;
    }

    public static void setTag(String tag) {
        Review.tag = tag;
    }

    public Review(String rid, String uid, String sid, String detail, String img_url, String datetime, String star) {
        this.rid = rid;
        this.uid = uid;
        this.sid = sid;
        this.detail = detail;
        this.img_url = img_url;
        this.datetime = datetime;
        this.star = star;

    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }
}

package com.cmu.nuts.coffee9.model;

/**
 * Created by Jaylers on 07-Feb-18.
 */

public class Share {
    public static String tag = "Share";

    private String shid;
    private String uid;
    private String sid;
    private String img_url;
    private String datetime;

    public Share(String shid, String uid, String sid, String img_url, String datetime) {
        this.shid = shid;
        this.uid = uid;
        this.sid = sid;
        this.img_url = img_url;
        this.datetime = datetime;
    }

    public static String getTag() {
        return tag;
    }

    public static void setTag(String tag) {
        Share.tag = tag;
    }

    public String getShid() {
        return shid;
    }

    public void setShid(String shid) {
        this.shid = shid;
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
}

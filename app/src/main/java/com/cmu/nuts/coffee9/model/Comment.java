package com.cmu.nuts.coffee9.model;

/**
 * Created by Jaylers on 07-Feb-18.
 */

public class Comment {
    private String cid;
    private String uid;
    private String rid;
    private String detail;
    private String datetime;

    public static String tag = "Comment";

    public Comment() {
    }

    public Comment(String cid, String uid, String rid, String detail, String datetime) {
        this.cid = cid;
        this.uid = uid;
        this.rid = rid;
        this.detail = detail;
        this.datetime = datetime;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}

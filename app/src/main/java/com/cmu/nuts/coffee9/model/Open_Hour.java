package com.cmu.nuts.coffee9.model;


public class Open_Hour {
    private String sid;
    private String tid;
    private String date;
    private String time;

    public static String tag = "Open_Hour";

    public Open_Hour(String sid, String tid, String date, String time) {
        this.sid = sid;
        this.tid = tid;
        this.date = date;
        this.time = time;
    }

    public Open_Hour() {

    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static String getTag() {
        return tag;
    }

    public static void setTag(String tag) {
        Open_Hour.tag = tag;
    }
}

package com.cmu.nuts.coffee9.model;


import java.io.Serializable;

public class Open_Hour implements Serializable {
    private String sid;
    private String tid;
    private String date;
    private String timestart;
    private String timeend;

    public static String tag = "Open_Hour";

    public Open_Hour(String sid, String tid, String date, String timestart, String timeend) {
        this.sid = sid;
        this.tid = tid;
        this.date = date;
        this.timestart = timestart;
        this.timeend = timeend;
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

    public String getTimestart() {
        return timestart;
    }

    public void setTimestart(String timestar) {
        this.timestart = timestar;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    public static String getTag() {
        return tag;
    }

    public static void setTag(String tag) {
        Open_Hour.tag = tag;
    }
}

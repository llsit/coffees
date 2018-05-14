package com.cmu.nuts.coffee9.main.model;

public class OpenTime {
    private String tid;
    private String sid;
    private String date;
    private String timeend;
    private String timestart;

    public OpenTime(String tid, String sid, String date, String timeend, String timestart) {
        this.tid = tid;
        this.sid = sid;
        this.date = date;
        this.timeend = timeend;
        this.timestart = timestart;
    }

    public OpenTime() {
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    public String getTimestart() {
        return timestart;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
    }
}

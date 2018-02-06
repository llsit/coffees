package com.cmu.nuts.coffee9.model;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Jaylers on 07-Feb-18.
 */

public class Favorite {
    public static String tag = "Favorite";

    private String fid;
    private String uid;
    private String sid;

    public Favorite(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Favorite(String fid, String uid, String sid) {
        this.fid = fid;
        this.uid = uid;
        this.sid = sid;
    }

    public static String getTag() {
        return tag;
    }

    public static void setTag(String tag) {
        Favorite.tag = tag;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
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
}

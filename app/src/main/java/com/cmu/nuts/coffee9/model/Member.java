package com.cmu.nuts.coffee9.model;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by nuts on 12/19/2017.
 **/
@IgnoreExtraProperties
public class Member {
    private String uid;
    private String name;
    private String email;
    private String photoUrl;
    private String provider;
    private String birthDate;
    private String regDate;

    public static String tag = "Member";

    public Member() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Member(String uid, String name, String email, String photoUrl, String provider, String birthDate, String regDate) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.provider = provider;
        this.birthDate = birthDate;
        this.regDate = regDate;
    }

    public Member(String uid, String mname, String email, String photoUrl, String provider, String birthDate, long regDate) {
        this.uid = uid;
        this.name = mname;
        this.email = email;
        this.photoUrl = photoUrl;
        this.provider = provider;
        this.birthDate = birthDate;
        this.regDate = String.valueOf(regDate);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public static String getTag() {
        return tag;
    }

    public static void setTag(String tag) {
        Member.tag = tag;
    }
}

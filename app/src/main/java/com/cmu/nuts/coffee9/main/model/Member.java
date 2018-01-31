package com.cmu.nuts.coffee9.main.model;
import com.google.firebase.database.IgnoreExtraProperties;
/**
 * Created by nuts on 12/19/2017.
 **/
@IgnoreExtraProperties
public class Member {
    public String uid;
    public String name;
    public String email;
    public String photoUrl;
    public String provider;

    public Member() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Member(String uid, String name, String email, String photoUrl, String provider) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.provider = provider;
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
}

package com.example.nuts.coffee9;
import com.google.firebase.database.IgnoreExtraProperties;
/**
 * Created by nuts on 12/19/2017.
 **/
@IgnoreExtraProperties

public class member {
    public String name;
    public String email;

    public member() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public member(String name, String uemail) {
        this.name = name;
        this.email = uemail;
    }

}

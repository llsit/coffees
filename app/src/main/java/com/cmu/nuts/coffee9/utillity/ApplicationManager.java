package com.cmu.nuts.coffee9.utillity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.cmu.nuts.coffee9.beforlogin.login;

/**
 * Created by tcdm053 on 16/2/2561.
 */

public class ApplicationManager {
    private Activity activity;

    ApplicationManager(Context context) {
        this.activity = (Activity) context;
    }

    public void signOut(){
//        SharedUserInfo sharedSignedUser = new SharedUserInfo(activity);
//        sharedSignedUser.setStateSignIn(SharedFlag.flag_unknown);
//        Intent intent = new Intent(activity, AuthActivity.class);
//        activity.startActivity(intent);
//        activity.finish();
    }

    void restartApplication(){
        Intent intent = new Intent(activity, login.class);
        activity.startActivity(intent);
        activity.finish();
    }
}

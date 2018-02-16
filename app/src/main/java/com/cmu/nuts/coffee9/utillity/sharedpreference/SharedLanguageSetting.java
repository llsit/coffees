package com.cmu.nuts.coffee9.utillity.sharedpreference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.cmu.nuts.coffee9.utillity.sharedstring.SharedFlag;
import com.cmu.nuts.coffee9.utillity.sharedstring.SharedKey;

/**
 * Created by tcdm053 on 16/2/2561.
 */

public class SharedLanguageSetting {
    private Activity activity;
    private SharedPreferences setting;
    private SharedPreferences.Editor editor;

    public SharedLanguageSetting(Activity activity) {
        this.activity = activity;
        setting = activity.getSharedPreferences(SharedKey.key_settings, Context.MODE_PRIVATE);
        editor = setting.edit();
    }

    public void setLanguage(String local){
        editor.putString(SharedKey.getKey_setting_language, local.toUpperCase());
        editor.apply();
    }

    public String  getLanguage(){
        return setting.getString(SharedKey.getKey_setting_language, SharedFlag.flag_language_en);
    }
}

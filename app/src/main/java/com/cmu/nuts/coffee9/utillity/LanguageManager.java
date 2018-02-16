package com.cmu.nuts.coffee9.utillity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.utillity.sharedpreference.SharedLanguageSetting;
import com.cmu.nuts.coffee9.utillity.sharedstring.SharedFlag;

import java.util.Locale;

/**
 * Created by tcdm053 on 16/2/2561.
 */

public class LanguageManager {
    private Activity activity;
    private Context context;
    private SharedLanguageSetting setting;

    public LanguageManager(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        setting = new SharedLanguageSetting(activity);
    }

    public void setApplicationLanguage(){
        String language = getLanguage();
        Configuration config = new Configuration();
        switch (language){
            case "TH" : config.locale = new Locale("th"); break;
            case "EN" : config.locale = Locale.ENGLISH; break;
            default: config.locale = Locale.ENGLISH; break;
        }
        activity.getResources().updateConfiguration(config, null);
    }

    public void reloadLanguage(){
        String language = getLanguage();
        Configuration config = new Configuration();
        switch (language){
            case "TH" : config.locale = new Locale("th"); break;
            case "EN" : config.locale = Locale.ENGLISH; break;
            default: config.locale = Locale.ENGLISH; break;
        }
        activity.getResources().updateConfiguration(config, null);

        ApplicationManager applicationManager = new ApplicationManager(activity);
        applicationManager.restartApplication();
    }

    public void setLanguage(String local){
        String text = SharedFlag.flag_unknown;
        setting.setLanguage(local);
        switch (getLanguage()){
            case "TH" : text = activity.getString(R.string.txt_language_thailand); break;
            case "EN" : text = activity.getString(R.string.txt_language_english); break;
        }
        Toast toast = Toast.makeText(context, activity.getString(R.string.pref_title_language)
                .concat(text), Toast.LENGTH_SHORT);
        toast.show();
        reloadLanguage();
    }

    public String getLanguage(){
        return setting.getLanguage();
    }
}
package com.cmu.nuts.coffee9.preferences.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.utillity.LanguageManager;
import com.cmu.nuts.coffee9.utillity.sharedstring.SharedFlag;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguageFragment extends Fragment {


    public LanguageFragment() {
        // Required empty public constructor
    }
    LanguageManager languageManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_language, container, false);
        ButterKnife.bind(this, view);
        Activity activity = getActivity();
        languageManager = new LanguageManager(activity);
        return view;
    }

    @OnClick(R.id.relative_language_th) public void onThai(){
        languageManager.setLanguage(SharedFlag.flag_language_th);
    }

    @OnClick(R.id.relative_language_en) public void onEnglish(){
        languageManager.setLanguage(SharedFlag.flag_language_en);
    }

    @OnClick(R.id.language_img_back) public void onBack(){
        PreferencesFragment preferencesFragment = new PreferencesFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.pref_container, preferencesFragment);
        transaction.commit();
    }

}

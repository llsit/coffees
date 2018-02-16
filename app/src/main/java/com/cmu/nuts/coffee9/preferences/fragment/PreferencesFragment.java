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
import com.cmu.nuts.coffee9.main.fragment.AddShopFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreferencesFragment extends Fragment {


    public PreferencesFragment() {
        // Required empty public constructor
    }

    private Activity activity;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_preferences, container, false);
        ButterKnife.bind(this, view);

        activity = getActivity();
        return view;
    }

    @OnClick(R.id.title_img_pref) public void onBack(){
        activity.finish();
    }

    @OnClick(R.id.pref_profile) public void onProfile(){
        ProfileWithEditFragment profileWithEditFragment = new ProfileWithEditFragment();
        FragmentManager manager = getFragmentManager();
        assert manager != null;
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.pref_container, profileWithEditFragment);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.pref_language) public void onLanguage(){
        LanguageFragment languageFragment = new LanguageFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.pref_container, languageFragment);
        transaction.commit();
    }

}

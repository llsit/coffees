package com.cmu.nuts.coffee9.preferences.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.beforlogin.login;
import com.google.firebase.auth.FirebaseAuth;

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

    @OnClick(R.id.pref_logout)
    public void logOut() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        Intent intent = new Intent(activity, login.class);
        startActivity(intent);
    }

    @OnClick(R.id.img_pref_back)
    public void onBack() {
        activity.finish();
    }

    @OnClick(R.id.pref_profile)
    public void onProfile() {
        ProfileWithEditFragment profileWithEditFragment = new ProfileWithEditFragment();
        FragmentManager manager = getFragmentManager();
        assert manager != null;
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.pref_container, profileWithEditFragment);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.pref_account)
    public void onAccout() {

    }

    @OnClick(R.id.pref_language)
    public void onLanguage() {
        LanguageFragment languageFragment = new LanguageFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.pref_container, languageFragment);
        transaction.commit();
    }

    @OnClick(R.id.pref_term)
    public void onTerm() {

    }

    @OnClick(R.id.pref_about)
    public void onAbout() {

    }

}

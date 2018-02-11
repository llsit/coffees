package com.cmu.nuts.coffee9.preferences;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.fragment.NearByFragment;
import com.cmu.nuts.coffee9.preferences.fragment.PreferencesFragment;
import com.cmu.nuts.coffee9.preferences.fragment.ProfileWithEditFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        ButterKnife.bind(this);

        PreferencesFragment preferencesFragment = new PreferencesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.pref_container, preferencesFragment);
        fragmentTransaction.commit();
    }
}

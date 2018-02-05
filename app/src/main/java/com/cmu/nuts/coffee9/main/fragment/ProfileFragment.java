package com.cmu.nuts.coffee9.main.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.beforlogin.login;
import com.cmu.nuts.coffee9.main.SettingsActivity;
import com.cmu.nuts.coffee9.main.model.Member;
import com.cmu.nuts.coffee9.utillity.TimeManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends Fragment {

    public Button btn_logout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private Activity activity;

    @BindView(R.id.display_name) TextView display_email;
    @BindView(R.id.display_email) TextView display_name;
    @BindView(R.id.display_uid) TextView display_uid;
    @BindView(R.id.display_reg_date) TextView display_reg;
    @BindView(R.id.btn_settings) Button btn_settings;
    @BindView(R.id.progressBar_profile) ProgressBar progressBar;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        btn_logout = view.findViewById(R.id.btn_logout);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Member.tag).child(currentUser.getUid());
        return view;
    }

    @OnClick(R.id.btn_logout) public void logout(){
        signOut();
    }

    @OnClick(R.id.btn_settings) public void settings(){
        Intent intent = new Intent(activity, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                btn_settings.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                TimeManager timeManager = new TimeManager();
                Member member = dataSnapshot.getValue(Member.class);
                display_email.setText("Email : ".concat(member.getEmail()));
                display_name.setText("Name : ".concat(member.getName()));
                display_reg.setText("Joined : ".concat(timeManager.epochConverter(Long.valueOf(member.getRegDate()))));
                display_uid.setText("UID : ".concat(member.getUid()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, "Failed to load member data!",
                        Toast.LENGTH_SHORT).show();
            }
        };

        databaseReference.addListenerForSingleValueEvent(listener);
        valueEventListener = listener;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (valueEventListener != null){
            databaseReference.removeEventListener(valueEventListener);
        }
    }

    private void signOut(){
        Toast.makeText(getActivity(), "Logging out",Toast.LENGTH_SHORT).show();
        auth.signOut();
        Intent intent = new Intent(getActivity(), login.class);
        startActivity(intent);
    }
}

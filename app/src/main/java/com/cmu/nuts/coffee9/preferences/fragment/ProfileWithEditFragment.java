package com.cmu.nuts.coffee9.preferences.fragment;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Member;
import com.cmu.nuts.coffee9.utillity.ImageManager;
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
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileWithEditFragment extends Fragment {


    public ProfileWithEditFragment() {
        // Required empty public constructor
    }

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private Activity activity;

    @BindView(R.id.edt_display_name) EditText display_name;
    @BindView(R.id.edt_display_email) TextView display_email;
    @BindView(R.id.display_uid) TextView display_uid;
    @BindView(R.id.display_reg_date) TextView display_reg;
    @BindView(R.id.img_profile) CircleImageView img_profile;
    @BindView(R.id.btn_done) Button btn_settings;
    @BindView(R.id.progressBar_profile)
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_with_edit, container, false);
        ButterKnife.bind(this, view);

        activity = getActivity();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Member.tag).child(currentUser.getUid());

        //updateProfile();
        return view;
    }

    @OnClick(R.id.btn_done) public void onBtnSetting(){

    private Uri path;
    private final int SELECT_PHOTO = 1;

    @OnClick(R.id.img_profile)
    public void onProfile() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    path = data.getData();
                    ImageManager imageManager = new ImageManager(activity);
                    imageManager.uploadImage(path.getPath());
                }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                btn_done.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                TimeManager timeManager = new TimeManager();
                Member member = dataSnapshot.getValue(Member.class);
                display_email.setText(member.getEmail());
                display_name.setText((member.getName()));
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
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }

    @OnClick(R.id.profile_edit_img_pref)
    public void onBack() {
        PreferencesFragment preferencesFragment = new PreferencesFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.pref_container, preferencesFragment);
        transaction.commit();
    }
}

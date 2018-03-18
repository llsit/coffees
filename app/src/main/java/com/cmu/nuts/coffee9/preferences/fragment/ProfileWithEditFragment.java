package com.cmu.nuts.coffee9.preferences.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
    @BindView(R.id.btn_edit_done) Button btn_settings;
    @BindView(R.id.progressBar_profile) ProgressBar progressBar;

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

        return view;
    }

    @OnClick(R.id.img_profile) public void onProfile(){
        ImagePicker.create(this).returnMode(ReturnMode.ALL).folderMode(true)
                .toolbarFolderTitle("Folder").toolbarImageTitle("Tap to select")
                .toolbarArrowColor(Color.BLUE).single().limit(1)
                .showCamera(true).imageDirectory("Camera").enableLog(true)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image imageFile = ImagePicker.getFirstImageOrNull(data);
            img_profile.setImageURI(Uri.parse(imageFile.getPath()));
            Toast.makeText(activity,"Picked image "+ imageFile.getPath(), Toast.LENGTH_SHORT).show();
            if (imageFile.getPath() != null){
                ImageManager imageManager = new ImageManager(activity);
                imageManager.uploadImage(Uri.fromFile(new File(imageFile.getPath())));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TimeManager timeManager = new TimeManager();
                Member member = dataSnapshot.getValue(Member.class);
                display_email.setText(member.getEmail());
                display_name.setText((member.getName()));
                display_reg.setText(activity.getString(R.string.txt_reg_prompt).concat(timeManager.epochConverter(Long.valueOf(member.getRegDate()))));
                display_uid.setText(activity.getString(R.string.txt_uid_prompt).concat(member.getUid()));
                btn_settings.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
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

    @OnClick(R.id.profile_edit_img_pref) public void onBack(){
        PreferencesFragment preferencesFragment = new PreferencesFragment();
        FragmentManager manager = getFragmentManager();
        assert manager != null;
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.pref_container, preferencesFragment);
        transaction.commit();
    }

    @OnClick(R.id.btn_edit_done) public void updateProfile() {
        String name = display_name.getText().toString();
        String email = display_email.getText().toString();
        String personId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Member.tag).child(personId);
        databaseReference.child("name").setValue(name);
        databaseReference.child("email").setValue(email);
        Toast.makeText(activity, "Update Successfully!",
                Toast.LENGTH_SHORT).show();
    }
}

package com.cmu.nuts.coffee9.preferences.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileWithEditFragment extends Fragment implements DatePickerDialog.OnDateSetListener {


    public ProfileWithEditFragment() {
        // Required empty public constructor
    }

    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private Activity activity;

    @BindView(R.id.edt_display_name)
    EditText display_name;
    @BindView(R.id.edt_display_email)
    TextView display_email;
    @BindView(R.id.display_uid)
    TextView display_uid;
    @BindView(R.id.display_reg_date)
    TextView display_reg;
    @BindView(R.id.img_profile)
    CircleImageView img_profile;
    @BindView(R.id.btn_edit_done)
    Button btn_settings;
    @BindView(R.id.progressBar_profile)
    ProgressBar progressBar;

    private TextView dateTextView;
    private SimpleDateFormat simpleDateFormat;
    private ImageView date;
    private String birthdate = null;
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_with_edit, container, false);
        ButterKnife.bind(this, view);

        activity = getActivity();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Member.tag).child(currentUser.getUid());

        dateTextView = view.findViewById(R.id.display_birth_date);
        date = view.findViewById(R.id.calendar);
        simpleDateFormat = new SimpleDateFormat("dd - MM - yyyy", Locale.US);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TimeManager timeManager = new TimeManager();
                final Member member = dataSnapshot.getValue(Member.class);
                assert member != null;
                display_email.setText(member.getEmail());
                display_name.setText((member.getName()));
                display_reg.setText(activity.getString(R.string.txt_reg_prompt).concat(timeManager.epochConverter(Long.valueOf(member.getRegDate()))));
                display_uid.setText(activity.getString(R.string.txt_uid_prompt).concat(member.getUid()));
                dateTextView.setText("Birth date : " + member.getBirthDate());
                birthdate = member.getBirthDate();
                if (member.getPhotoUrl() != null) {
                    Picasso.get()
                            .load(member.getPhotoUrl())
                            .placeholder(R.drawable.img_user)
                            .fit().centerCrop()
                            .error(R.drawable.img_user)
                            .into(img_profile);
                }
                btn_settings.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (birthdate.equals("")) {
                            showDate(2000, 01, 01);
                        } else {
                            showDate(Integer.parseInt(member.getBirthDate().substring(10, 14)), Integer.parseInt(member.getBirthDate().substring(5, 7)), Integer.parseInt(member.getBirthDate().substring(0, 2)));
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, "Failed to load member data!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    //    @VisibleForTesting
    void showDate(int year, int monthOfYear, int dayOfMonth) {
        Toast.makeText(activity, "year = " + year + "month = " + monthOfYear + " day = " + dayOfMonth + " spinner = " + R.style.DatePickerSpinner, Toast.LENGTH_SHORT).show();
        new SpinnerDatePickerDialogBuilder()
                .context(getActivity())
                .callback(ProfileWithEditFragment.this)
                .spinnerTheme(R.style.DatePickerSpinner)
                .defaultDate(year, monthOfYear - 1, dayOfMonth)
                .minDate(1940, 0, 1)
                .maxDate(2018, 0, 1)
                .build()
                .show();
    }

    @OnClick(R.id.img_profile)
    public void onProfile() {
        ImagePicker.create(this).returnMode(ReturnMode.ALL).folderMode(true)
                .toolbarFolderTitle("Folder").toolbarImageTitle("Tap to select")
                .toolbarArrowColor(Color.WHITE).single().limit(1)
                .showCamera(true).imageDirectory("Camera").enableLog(true)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image imageFile = ImagePicker.getFirstImageOrNull(data);
            img_profile.setImageURI(Uri.parse(imageFile.getPath()));
            Toast.makeText(activity, "Picked image " + imageFile.getPath(), Toast.LENGTH_SHORT).show();
            if (imageFile.getPath() != null) {
                ImageManager imageManager = new ImageManager(activity);
                imageManager.uploadImage(Uri.fromFile(new File(imageFile.getPath())));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @OnClick(R.id.profile_edit_img_pref)
    public void onBack() {
        PreferencesFragment preferencesFragment = new PreferencesFragment();
        FragmentManager manager = getFragmentManager();
        assert manager != null;
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.pref_container, preferencesFragment);
        transaction.commit();
    }

    @OnClick(R.id.btn_edit_done)
    public void updateProfile() {

        if (display_name.getText().length() == 0) {
            TextInputLayout textInputLayout1 = view.findViewById(R.id.input_layout_name);
            textInputLayout1.setError("กรุณากรอกชื่อ");
        } else if (display_email.getText().length() == 0) {
            TextInputLayout textInputLayout1 = view.findViewById(R.id.input_layout_email);
            textInputLayout1.setError("กรุณากรอกอีเมล");
        } else {
            String name = display_name.getText().toString();
            String email = display_email.getText().toString();
            String personId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child(Member.tag).child(personId);
            databaseReference.child("name").setValue(name);
            databaseReference.child("email").setValue(email);
            if (birthdate != null) {
                databaseReference.child("birthDate").setValue(birthdate);
            }

            Toast.makeText(activity, "Update Successfully!", Toast.LENGTH_SHORT).show();
            PreferencesFragment preferencesFragment = new PreferencesFragment();
            FragmentManager manager = getFragmentManager();
            assert manager != null;
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.pref_container, preferencesFragment);
            transaction.commit();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        dateTextView.setText("Birth date : " + simpleDateFormat.format(calendar.getTime()));
        birthdate = simpleDateFormat.format(calendar.getTime());
//        databaseReference.child("birthDate").setValue(simpleDateFormat.format(calendar.getTime()));
    }
}

package com.cmu.nuts.coffee9.main.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.beforlogin.login;
import com.cmu.nuts.coffee9.main.model.Member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    TextView display_email;
    TextView display_name;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        btn_logout = view.findViewById(R.id.btn_logout);
        display_name = view.findViewById(R.id.display_name);
        display_email = view.findViewById(R.id.display_email);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Member").child(currentUser.getUid());
        return view;
    }

    @OnClick(R.id.btn_logout) public void logout(){
        signOut();
    }

    @Override
    public void onStart() {
        super.onStart();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member member = dataSnapshot.getValue(Member.class);
                display_email.setText("Email : ".concat(member.getEmail()));
                display_name.setText("Name : ".concat(member.getName()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to load member data!",
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
        Toast.makeText(getActivity(), "Logout",Toast.LENGTH_SHORT).show();
        auth.signOut();
        Intent intent = new Intent(getActivity(), login.class);
        startActivity(intent);
    }
}

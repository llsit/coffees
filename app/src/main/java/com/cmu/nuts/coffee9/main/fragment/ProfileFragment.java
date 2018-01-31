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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public Button btn_logout;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        //btn_logout = getActivity().findViewById(R.id.btn_logout);
        btn_logout = view.findViewById(R.id.btn_logout);
        TextView display_name = view.findViewById(R.id.display_name);
        TextView display_email = view.findViewById(R.id.display_email);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();

        assert currentUser != null;
        display_email.setText("Email : " + currentUser.getEmail());
        display_name.setText("Name : " + currentUser.getDisplayName());
        btn_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Logout",Toast.LENGTH_SHORT).show();
                auth.signOut();
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
            }
        });

        return view;
    }
}

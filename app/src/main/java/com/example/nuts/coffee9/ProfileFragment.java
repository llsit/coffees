package com.example.nuts.coffee9;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView display_email,display_name;
    public Button btn_logout;
    private FirebaseAuth auth;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        //btn_logout = getActivity().findViewById(R.id.btn_logout);
        btn_logout = view.findViewById(R.id.btn_logout);
        display_name = view.findViewById(R.id.display_name);
        display_email = view.findViewById(R.id.display_email);

        auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();

        display_email.setText("Email : " + currentUser.getEmail());

        btn_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Logout",Toast.LENGTH_SHORT).show();
//                auth.signOut();
//                Intent intent = new Intent(getActivity(), login.class);
//                startActivity(intent);
            }
        });

        return view;
    }

    public interface OnFragmentInteractionListener {
    }
}

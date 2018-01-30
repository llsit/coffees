package com.example.nuts.coffee9.main.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.nuts.coffee9.R;
import com.example.nuts.coffee9.main.addDataShop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddShopFragment extends Fragment {

    EditText nameshop,Addressshop,detail,location,time;
    Button btn_add;
    RadioButton min,mid,max,radioPrice;

    private String coffee_ID,name,addressshop,Detail,locat,authorID ;

    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    FirebaseUser user;

    public AddShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_shop, container, false);
        // Inflate the layout for this fragment
        Toolbar toolbar = view.findViewById(R.id.toolbarAddShop);
        Activity activity = getActivity();
        assert ((AppCompatActivity)getActivity()) != null;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Coffee Shop");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        /*public void onRadioButtonClicked(View view) {
            // Is the button now checked?
            boolean checked = ((RadioButton) view).isChecked();

            // Check which radio button was clicked
            switch(view.getId()) {
                case R.id.radio_pirates:
                    if (checked)
                        // Pirates are the best
                        break;
                case R.id.radio_ninjas:
                    if (checked)
                        // Ninjas rule
                        break;
            }
        }*/
        addShop(view);
        return view;
    }

    private void addShop(View view) {
        nameshop = view.findViewById(R.id.nameshop);
        Addressshop = view.findViewById(R.id.Addressshop);
        detail = view.findViewById(R.id.detail);
        btn_add = view.findViewById(R.id.btn_add);
        min = view.findViewById(R.id.min);
        mid = view.findViewById(R.id.mid);
        max = view.findViewById(R.id.max);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameshop.getText().toString();
                addressshop = Addressshop.getText().toString();
                Detail = detail.getText().toString();
                authorID = user.getUid();
                coffee_ID = mDatabase.push().getKey();

                addDataShop shopData = new addDataShop(name, addressshop,Detail,authorID);
                mDatabase.child("coffee_shop").child(coffee_ID).setValue(shopData);

                Toast.makeText(getActivity(), "Add Success", Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void onBackPressed() {
        getActivity().onBackPressed();
    }

}

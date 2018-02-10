package com.cmu.nuts.coffee9.main.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.model.AddDataShop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddShopFragment extends Fragment {

    EditText nameshop, Addressshop, detail, location, open_hour;
    Button btn_add;
    RadioButton price, min, mid, max;

    private String coffee_ID, name, addressshop, Detail, authorID, locat, open, prices;


    private DatabaseReference mDatabase;
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

        assert ((AppCompatActivity) getActivity()) != null;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Coffee Shop");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    @SuppressLint("WrongViewCast")
    private void addShop(final View view) {
        nameshop = view.findViewById(R.id.nameshop);
        Addressshop = view.findViewById(R.id.Addressshop);
        detail = view.findViewById(R.id.detail);
        location = view.findViewById(R.id.location);
        open_hour = view.findViewById(R.id.open_hour);
        //max = view.findViewById(R.id.max);
        //mid = view.findViewById(R.id.mid);
        //min = view.findViewById(R.id.min);
        //price = view.findViewById(R.id.radioPrice);
        btn_add = view.findViewById(R.id.btn_add);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        /*
        boolean checked = price.isChecked();
        switch (price.getId()) {
            case R.id.min:
                if (checked)
                    prices = min.getText().toString();
                    break;
            case R.id.mid:
                if (checked)
                    prices = mid.getText().toString();
                    break;
            case R.id.max:
                if (checked)
                    prices = max.getText().toString();
                    break;
        }

        if (min.isChecked()) {
            prices = "0-100";
        } else if (mid.isChecked()) {
            prices = mid.getText().toString();
        } else if (max.isChecked()) {
            prices = max.getText().toString();
        }*/

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameshop.getText().toString();
                addressshop = Addressshop.getText().toString();
                Detail = detail.getText().toString();
                authorID = user.getUid();
                coffee_ID = mDatabase.push().getKey();
                locat = location.getText().toString();
                open = open_hour.getText().toString();
                prices = "test";

                AddDataShop shopData = new AddDataShop(name, addressshop, Detail, authorID, locat, open, prices);
                mDatabase.child("coffee_shop").child(coffee_ID).setValue(shopData);

                Toast.makeText(getActivity(), "Add Success", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void onBackPressed() {
        getActivity().onBackPressed();
    }

}

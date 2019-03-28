package com.cmu.nuts.coffee9.main.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.beforlogin.login;
import com.cmu.nuts.coffee9.main.adapter.ShopRecyclerAdapter;
import com.cmu.nuts.coffee9.model.Member;
import com.cmu.nuts.coffee9.model.Shop;
import com.cmu.nuts.coffee9.preferences.PreferencesActivity;
import com.cmu.nuts.coffee9.utillity.TimeManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private Activity activity;

    @BindView(R.id.display_email)
    TextView display_email;
    @BindView(R.id.display_name)
    TextView display_name;
    @BindView(R.id.display_birth_date)
    TextView display_birth_date;
    @BindView(R.id.progressBar_profile)
    ProgressBar progressBar;
    @BindView(R.id.img_profile2)
    CircleImageView img_profile;
    private RecyclerView recyclerView;
    private ArrayList<Shop> arrayList = new ArrayList<>();
    FirebaseUser currentUser;

    @SuppressLint("SetTextI18n")
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        recyclerView = view.findViewById(R.id.recycler_coffee_list);
        recyclerView.setNestedScrollingEnabled(false);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        assert currentUser != null;
        databaseReference = FirebaseDatabase.getInstance().getReference(Member.tag).child(currentUser.getUid());

        getOwnlist();
        return view;
    }

    private void getOwnlist() {
        DatabaseReference OwndatabaseReference = FirebaseDatabase.getInstance().getReference(Shop.tag);
        OwndatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Shop shops = item.getValue(Shop.class);
                        assert shops != null;
                        if (shops.getUid().equals(currentUser.getUid())) {
                            arrayList.add(shops);
                        }
                    }
                    ShopRecyclerAdapter shopRecyclerAdapter = new ShopRecyclerAdapter(arrayList, getActivity());
                    RecyclerView.LayoutManager recycle = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(recycle);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(shopRecyclerAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                TimeManager timeManager = new TimeManager();
                Member member = dataSnapshot.getValue(Member.class);
                assert member != null;
                display_email.setText(member.getEmail());
                display_name.setText(member.getName());
                display_birth_date.setText(member.getBirthDate());
                Picasso.get()
                        .load(member.getPhotoUrl())
                        .resize(250, 250)
                        .placeholder(R.drawable.img_user)
                        .error(R.drawable.img_user)
                        .into(img_profile);
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
}

package com.cmu.nuts.coffee9.beforlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.model.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class register extends AppCompatActivity {

    //defining view objects
    private EditText editTextName,editTextEmail,editTextPassword,editTextDate;

    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTextName = findViewById(R.id.editTextName);
        editTextEmail =  findViewById(R.id.editTextEmail);
        editTextPassword =  findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressBar2);
    }

    @OnClick(R.id.textViewSignin) public void signIn(){
        Intent intent = new Intent(register.this, login.class);
        startActivity(intent);
    }

    @OnClick(R.id.buttonSignup) public void signUp(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        progressBar.setVisibility(View.VISIBLE);
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        name = editTextName.getText().toString();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            //Success
                            FirebaseUser mid = task.getResult().getUser();
                            newMember(mid);
                        }else{
                            //display some message here
                            Toast.makeText(register.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void newMember(FirebaseUser mid) {
        Member user = new Member(mid.getUid(), name, mid.getEmail(),
                String.valueOf(mid.getPhotoUrl()), String.valueOf(mid.getProviders()),
                "", mid.getMetadata().getCreationTimestamp());

        mDatabase.child(Member.tag).child(mid.getUid()).setValue(user);

        Toast.makeText(register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(register.this, login.class);
        startActivity(intent);
        finish();
    }

}

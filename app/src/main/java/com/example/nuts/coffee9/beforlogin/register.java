package com.example.nuts.coffee9.beforlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nuts.coffee9.R;
import com.example.nuts.coffee9.main.member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class register extends AppCompatActivity {

    //defining view objects
    private EditText editTextName,editTextEmail,editTextPassword,editTextDate;

    private Button buttonSignup;

    private TextView textViewSignIn;
    private ProgressBar progressBar2;
    private FirebaseAuth auth;

    private DatabaseReference mDatabase;

    private String id,email,password,mname,mdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        //database

//        final FirebaseUser members = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTextName = findViewById(R.id.editTextName);
        editTextEmail =  findViewById(R.id.editTextEmail);
        editTextPassword =  findViewById(R.id.editTextPassword);
        textViewSignIn = findViewById(R.id.textViewSignin);
        progressBar2 = findViewById(R.id.progressBar2);

        buttonSignup =  findViewById(R.id.buttonSignup);


        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar2.setVisibility(View.VISIBLE);
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                mname = editTextName.getText().toString();
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //checking if success
                                progressBar2.setVisibility(View.GONE);
                                if(task.isSuccessful()){
                                    //Success
//                                    Toast.makeText(register.this, "Register Success", Toast.LENGTH_SHORT).show();
                                    FirebaseUser mid = task.getResult().getUser();
//                                    uemail = mid.getEmail();
                                    id = mid.getUid();
                                    Newmember(email,id,mname);
                                }else{
                                    //display some message here
                                    Toast.makeText(register.this,"Registration Error",Toast.LENGTH_LONG).show();
                                }

                            }
                        });
            }
        });

        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
            }
        });

    }

    private void Newmember(String uemail, String mid, String name) {

        member user = new member(name, uemail);
        mDatabase.child("member").child(mid).setValue(user);

        Toast.makeText(register.this, "Register Success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(register.this, login.class);
        startActivity(intent);
        finish();
    }

}

package com.example.nuts.coffee9;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Member;


public class register extends AppCompatActivity {

    //defining view objects
    private EditText editTextName,editTextEmail,editTextPassword;

    private Button buttonSignup;

    private TextView textViewSignin;

    private FirebaseAuth auth;

    private DatabaseReference mDatabase;

    private String id,email,password,mname;
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
        textViewSignin = findViewById(R.id.textViewSignin);

        buttonSignup =  findViewById(R.id.buttonSignup);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                mname = editTextName.getText().toString();
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //checking if success
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
    }

    private void Newmember(String uemail, String mid, String name) {

        member user = new member(name, uemail);
        mDatabase.child("member").child(mid).setValue(user);

        Toast.makeText(register.this, "Register Success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(register.this, login.class);
        startActivity(intent);
    }

}

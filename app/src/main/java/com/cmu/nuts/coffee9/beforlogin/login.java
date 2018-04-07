package com.cmu.nuts.coffee9.beforlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.main;
import com.cmu.nuts.coffee9.model.Member;
import com.cmu.nuts.coffee9.utillity.LanguageManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class login extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText inputEmail, inputPassword;
    private Button btnLogin;
    private TextView link_signup;
    private CallbackManager mCallbackManager;
    private ProgressDialog progressDialog;
    private LanguageManager languageManager;
    private DatabaseReference mDatabase;

    private static final String TAG = "FacebookLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        languageManager = new LanguageManager(this);
        languageManager.setApplicationLanguage();
        progressDialog = ProgressDialog.show(this, "", "Please wait ...", true, false);

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        link_signup = findViewById(R.id.link_signup);
        btnLogin = findViewById(R.id.btn_login);
        //Button btnfacebook = findViewById(R.id.login_facebook);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();

        //go to register
        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
            }
        });

        //Login Email and password
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(inputEmail, inputPassword);
            }
        });

        //login Facebook
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_facebook);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
                updateUI(null);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        });

// ...

    }

    private void signIn(EditText inputEmail, final EditText inputPassword) {
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getString(R.string.err_email_require));
            //Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!(email.contains("@"))) {
            inputEmail.setError(getString(R.string.err_email_invalid));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            inputPassword.setError(getString(R.string.err_pass_require));
            //Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 6) {
            inputPassword.setError(getString(R.string.err_pass_too_short));
            return;
        }

        progressDialog.show();

        //authenticate user
        try {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                // there was an error
                                if (progressDialog.isShowing()) progressDialog.dismiss();
                                if (password.length() < 6) {
                                    inputPassword.setError(getString(R.string.minimum_password));
                                } else {
                                    Toast.makeText(login.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                FirebaseUser user = auth.getCurrentUser();
                                updateUI(user);
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(login.this, getString(R.string.auth_failed) + " with error " + e, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();

        updateUI(currentUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        progressDialog.show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                addMember(user);
                                updateUI(user);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void addMember(FirebaseUser user) {
        String uid = user.getUid();
        String name = user.getDisplayName();
        String email = user.getEmail();
        String photoUrl = Objects.requireNonNull(user.getPhotoUrl()).toString();
        String provider = Objects.requireNonNull(user.getProviders()).toString();
        String birthdate = "";
        String regDate = String.valueOf(Objects.requireNonNull(user.getMetadata()).getCreationTimestamp());

        Member member = new Member(uid, name, email, photoUrl, provider, birthdate, regDate);

        mDatabase.child(Member.tag).child(uid).setValue(member);

    }

    private void updateUI(FirebaseUser user) {
        if (progressDialog.isShowing()) progressDialog.dismiss();
        if (user != null) {
            Intent intent = new Intent(login.this, main.class);
            startActivity(intent);
            finish();
        }
    }

}

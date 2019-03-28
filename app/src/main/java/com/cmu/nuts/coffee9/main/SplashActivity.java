package com.cmu.nuts.coffee9.main;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmu.nuts.coffee9.R;
import com.facebook.login.Login;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int SPLASH_DISPLAY_TIME = 3000;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                System.out.println("SplashActivity");
                /* Create an intent that will start the main activity. */
                Intent i = new Intent(SplashActivity.this, Login.class);

                SplashActivity.this.startActivity(i);
//                startActivity(mainIntent);
//                /* Finish splash activity so user cant go back to it. */
                finish();
//
//                     /* Apply our splash exit (fade out) and main
//                        entry (fade in) animation transitions. */
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, SPLASH_DISPLAY_TIME);
    }
}

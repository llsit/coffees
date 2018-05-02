package com.cmu.nuts.coffee9.main.review;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.main.reviewDisplayFragment;
import com.cmu.nuts.coffee9.model.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class review_display_activity extends AppCompatActivity {

    private String shop_ID;
    private String review_ID;
    private DatabaseReference mDatabase;

    private ImageView review_display_back;
    //comment
    private EditText add_comment;
    private ImageButton send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_display_activity);
        Toolbar toolbar = findViewById(R.id.toolbar_display_review);
        setSupportActionBar(toolbar);

        reviewDisplayFragment reviewDisplayFragment = new reviewDisplayFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.review_display_container, reviewDisplayFragment);
        fragmentTransaction.commit();


        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        review_ID = intent.getStringExtra("reviewID");
        shop_ID = intent.getStringExtra("shopID");


        review_display_back = findViewById(R.id.review_display_back);
        review_display_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Fragment fragobj = new reviewDisplayFragment();
        Bundle bundle = new Bundle();
        bundle.putString("reviewID", review_ID);
        bundle.putString("shopID", shop_ID);
        // set Fragmentclass Arguments
        Toast.makeText(this, review_ID + "   " + shop_ID, Toast.LENGTH_SHORT).show();

        fragobj.setArguments(bundle);
        comment();
    }

    private void comment() {
        add_comment = findViewById(R.id.add_comment);
        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_comment.getText().length() > 3) {
                    String datetime = DateFormat.getDateTimeInstance().format(new Date());
                    String uid = FirebaseAuth.getInstance().getUid();
                    String detail = add_comment.getText().toString();
                    String cid = mDatabase.push().getKey();
                    String rid = shop_ID;

                    Comment comment = new Comment(cid, uid, rid, detail, datetime);
                    mDatabase.child(Comment.tag).child(rid).child(cid).setValue(comment);
                    Toast.makeText(review_display_activity.this, "Comment Complete", Toast.LENGTH_SHORT).show();
                    add_comment.setText("");
                } else {
                    add_comment.setError("Your comment is too shot");
                }
            }
        });
    }

}

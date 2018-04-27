package com.cmu.nuts.coffee9.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.model.Share;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FullImageActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        Toolbar toolbar = findViewById(R.id.toolbar_full_image);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        ArrayList<String> array = bundle.getStringArrayList("mylist");

        final String shop_ID = bundle.getString("shopid");

        TextView text = findViewById(R.id.test);

        String url = null;
        assert array != null;
        for (int i = 0; i < array.size(); i++) {
            url = array.get(i);

        }

        ImageView back = findViewById(R.id.full_image_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView imgview = findViewById(R.id.image);

        Picasso.get()
                .load(url)
                .placeholder(R.drawable.img_preview)
                .error(R.drawable.img_preview)
                .into(imgview);

        ImageView Delimgview = findViewById(R.id.delete);

        final String finalUrl = url;

        Delimgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert shop_ID != null;
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Share.tag).child(shop_ID);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            Share shares = item.getValue(Share.class);
                            assert shares != null;
                            if (shares.getImg_url().equals(finalUrl)) {
                                String id = shares.getShid();
                                deleteImg(id, shop_ID, finalUrl);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Share", "Failed to get database", databaseError.toException());
                    }
                });
            }
        });

    }

    private void deleteImg(String id, String shop_ID, String finalUrl) {
        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(finalUrl);
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Delete Image", "onSuccess: deleted file");
            }
        });
        DatabaseReference Img_del = FirebaseDatabase.getInstance().getReference(Share.tag).child(shop_ID).child(id);
        Img_del.removeValue();
        Toast.makeText(FullImageActivity.this, "done", Toast.LENGTH_LONG).show();
        finish();
    }

}

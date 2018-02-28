package com.cmu.nuts.coffee9.utillity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cmu.nuts.coffee9.R;
import com.cmu.nuts.coffee9.utillity.sharedstring.FirebaseKey;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by tcdm053 on 28/2/2561.
 */

public class ImageManager {
    private Activity activity;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private UploadTask uploadTask;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    public ImageManager(Activity activity){
        this.activity = activity;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    CircleImageView image;
    public void uploadImage(String path){
        StorageReference riversRef = storageRef.child(FirebaseKey.img_profile_key).child(firebaseUser.getUid());
        uploadTask = riversRef.putFile(Uri.parse(path));
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(activity,"Upload fail", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Toast.makeText(activity,"Upload successfully", Toast.LENGTH_SHORT).show();
                image = activity.findViewById(R.id.img_profile);
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Glide.with(activity)
                        .load(downloadUrl)
                        .into(image);
            }
        });
    }
}

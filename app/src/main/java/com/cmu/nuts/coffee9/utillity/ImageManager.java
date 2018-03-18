package com.cmu.nuts.coffee9.utillity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.cmu.nuts.coffee9.model.Member;
import com.cmu.nuts.coffee9.preferences.PreferencesActivity;
import com.cmu.nuts.coffee9.utillity.sharedstring.FirebaseKey;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by tcdm053 on 28/2/2561.
 */

public class ImageManager {
    private Activity activity;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage storage;

    private UploadTask uploadTask;

    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    public ImageManager(Activity activity){
        this.activity = activity;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        storageRef = storage.getReference();

        databaseRef = FirebaseDatabase.getInstance().getReference()
                .child(Member.tag).child(firebaseUser.getUid());
    }

    public void uploadImage(Uri path){
        final ProgressDialog progress = ProgressDialog.show(activity, "Upload Task",
                "Starting upload", true);
        progress.show();
        StorageReference riversRef = storageRef.child(FirebaseKey.img_profile_key).child(firebaseUser.getUid());
        Log.d("Upload", "Uploading" + firebaseUser.getDisplayName() + " name " + path.toString());
        // Register observers to listen for when the download is done or if it fails
        uploadTask = riversRef.putFile(path);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                progress.setMessage("Uploading " + ((taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100) + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                progress.dismiss();
                Log.e("onFailure",exception.getMessage());
                Toast.makeText(activity,"Upload fail, cause by " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progress.dismiss();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                databaseRef.child("photoUrl").setValue(downloadUrl);
                Intent intent = new Intent(activity, PreferencesActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }
}

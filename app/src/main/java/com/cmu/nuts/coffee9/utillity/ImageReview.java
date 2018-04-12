package com.cmu.nuts.coffee9.utillity;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.cmu.nuts.coffee9.main.review.ReviewActivity;
import com.cmu.nuts.coffee9.model.Review_Image;
import com.cmu.nuts.coffee9.utillity.sharedstring.FirebaseKey;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageReview {
    private Class<ReviewActivity> activity;
    private StorageReference storageRef;
    private DatabaseReference databaseRef,mDatabase;
    private Context context;

    public ImageReview(Class<ReviewActivity> activity, String rid){
        this.activity = activity;
        storageRef = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference()
                .child(Review_Image.tag).child(rid);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void uploadImage(String rid,Uri path){
//        final ProgressDialog progress = ProgressDialog.show(context, "Upload Task",
//                "Starting upload", true);
//        progress.show();
        String img_id = mDatabase.push().getKey();
        StorageReference riversRef = storageRef.child(rid).child(img_id).child(FirebaseKey.img_profile_key);
        Log.d("Upload", "Uploading" + rid + " name " + path.getPath());
        // Register observers to listen for when the download is done or if it fails
        UploadTask uploadTask = riversRef.putFile(path);

        try {
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    progress.setMessage("Uploading " + ((taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100) + "%");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
//                    progress.dismiss();
                    Log.e("onFailure",exception.getMessage());
                    Toast.makeText(context,"Upload fail, cause by " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    progress.dismiss();
                    databaseRef.child("img_url").setValue(taskSnapshot.getDownloadUrl().toString());
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

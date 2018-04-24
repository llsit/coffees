package com.cmu.nuts.coffee9.utillity;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cmu.nuts.coffee9.main.data_shop.DataShopActivity;
import com.cmu.nuts.coffee9.model.Share;
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

public class ImageShare {
    private Class<DataShopActivity> activity;
    private FirebaseUser firebaseUser;

    private StorageReference storageRef;
    private DatabaseReference databaseRef,mDatabase;

    public ImageShare(Class<DataShopActivity> activity, String shop_id) {
        this.activity = activity;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference();

        databaseRef = FirebaseDatabase.getInstance().getReference()
                .child(Share.tag).child(shop_id);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void uploadImage(String shop_ID, Uri path){
//        final ProgressDialog progress = ProgressDialog.show(activity, "Upload Task",
//                "Starting upload", true);
//        progress.show();
        final String image_id = mDatabase.push().getKey();
        StorageReference riversRef = storageRef.child(FirebaseKey.img_shared_key).child(image_id);
        Log.d("Upload", "Uploading" + shop_ID + " name " + path.getPath());
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
//                    Toast.makeText(activity,"Upload fail, cause by " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    progress.dismiss();
                    databaseRef.child(image_id).child("img_url").setValue(taskSnapshot.getDownloadUrl().toString());
                    databaseRef.child(image_id).child("image_id").setValue(image_id);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

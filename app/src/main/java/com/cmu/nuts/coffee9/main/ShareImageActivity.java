package com.cmu.nuts.coffee9.main;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cmu.nuts.coffee9.R;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ShareImageActivity extends AppCompatActivity {

    private ImageView back, add_image, display;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_image);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        back = findViewById(R.id.share_image_back);
        add_image = findViewById(R.id.share_image_add_image);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(ShareImageActivity.this).folderMode(true)
                        .toolbarFolderTitle("Folder").toolbarImageTitle("Tap to select")
                        .toolbarArrowColor(Color.WHITE).multi().limit(10)
                        .showCamera(true).imageDirectory("Camera").enableLog(true)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);

            upload_photo(images);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upload_photo(List<Image> images) {

        text = findViewById(R.id.share_image__text);
        display = findViewById(R.id.share_image_display_image);
        if (images == null) return;

        StringBuilder stringBuffer = new StringBuilder();
//        ImageShare imageManager = new ImageShare(ShareImageActivity.class);
        for (int i = 0, l = images.size(); i < l; i++) {
//            stringBuffer.append(images.get(i).getPath());
//            imageManager.uploadImage(Uri.fromFile(new File(images.get(i).getPath())));

//            Log.v(String.valueOf(ShareImageActivity.this), "index=" + stringBuffer);
//            File file = new File(String.valueOf(stringBuffer));

            Uri uri = Uri.fromFile(new File(images.get(i).getPath()));
            stringBuffer.append(uri);
            Picasso.get()
                    .load(uri)
                    .placeholder(R.drawable.img_preview)
                    .fit().centerCrop()
                    .error(R.drawable.img_preview)
                    .into(display);
            Toast.makeText(this, " Loading " + uri.getUserInfo(), Toast.LENGTH_SHORT).show();
        }
        text.setText(stringBuffer.toString());
    }

}

package com.example.zac.myapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.zac.myapplication.R;

import java.io.IOException;

public class PreviewImageLarge extends AppCompatActivity {

    private ImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image_large);


        imageView = (ImageView) findViewById(R.id.PreviewImageCapture);


        Intent intent = getIntent();
        if (intent != null) {
            String message = intent.getStringExtra("MESSAGE");
            Uri image = Uri.parse(message);
            Bitmap thumbnail = null;
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), image);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(thumbnail);
        }
    }



}

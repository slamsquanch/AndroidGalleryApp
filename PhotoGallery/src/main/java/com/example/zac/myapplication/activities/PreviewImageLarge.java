package com.example.zac.myapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.zac.myapplication.R;
import com.example.zac.myapplication.classes.Image;
import com.example.zac.myapplication.classes.PhotoUpload;
import com.example.zac.myapplication.database.DBHelper;

import java.io.IOException;

public class PreviewImageLarge extends AppCompatActivity {

    private ImageView imageView = null;
    private Uri currentURI = null; //for clicking a BUTTON
    private final String IMG_NAME = "IMG_";
    private String dir = null;
    private int month, day, year;   // used for TimeStamps


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image_large);


        imageView = (ImageView) findViewById(R.id.PreviewImageCapture);


        Intent intent = getIntent();
        if (intent != null) {
            String message = intent.getStringExtra("URI");
            currentURI = Uri.parse(message);
            Bitmap thumbnail = null;
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), currentURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(thumbnail);
            dir = intent.getStringExtra("DIR");
            month = Integer.parseInt(intent.getStringExtra("MONTH"));
            day = Integer.parseInt(intent.getStringExtra("DAY"));
            year = Integer.parseInt(intent.getStringExtra("YEAR"));
        }
    }



    public void insertToDB() {
        DBHelper.getInstance(this).insertPhoto(
                new Image(0, currentURI.toString(), IMG_NAME + Image.getCount(), "",
                        "" + (month + 1) + "-" + day + "-" + year));
        Intent intent = new Intent(this, GalleryGrid.class);
        startActivity(intent);
    }



    public void uploadPhoto(View view) {
        insertToDB();
    }



}

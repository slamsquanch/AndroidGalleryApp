package com.example.zac.myapplication.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.zac.myapplication.R;
import com.example.zac.myapplication.classes.Image;
import com.example.zac.myapplication.database.DBHelper;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class StartupActivity extends AppCompatActivity {

    private final int REQUEST_RUNTIME_PERMISSION = 0;
    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    private Uri currentURI = null; //for clicking a BUTTON
    private final String IMG_NAME = "IMG_";
    private ImageView ivImage; //initialize this in onCreate
    private String dir = null;
    private int month, day, year;   // used for TimeStamps

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ivImage = null;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        //
        checkPermissions();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("QuickPic");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        //Ignores URI Issue
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //Makes Directory if does not exist
        dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/PICTURES/";
        File newDirectory = new File(dir);
        newDirectory.mkdirs();

        //ivImage = (ImageView) findViewById(R.id.PreviewImageCapture);
    }



    // TAKE PHOTO BUTTON.  Opens Android Camera using intent.
    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Creating file for image
        Calendar mcurrentDate = Calendar.getInstance();
        //+ Calendar.YEAR + Calendar.MONTH + Calendar.DAY_OF_MONTH + "_" +
        String timeStamp = "" + Calendar.YEAR + Calendar.MONTH + Calendar.DAY_OF_MONTH + Calendar.HOUR + Calendar.MINUTE + Calendar.SECOND;
        //new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image = new File(dir, "IMAGE_" + timeStamp + ".jpg");
        currentURI = Uri.fromFile(image);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, currentURI);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA || requestCode == SELECT_FILE) {
                onCaptureImageResult(data);
            }
        }
    }

    //Use this for now
    private void onCaptureImageResult(Intent data) {

        Intent intent = new Intent(this, PreviewImageLarge.class);
        intent.putExtra("MESSAGE", currentURI.toString());
        startActivity(intent);
    }


    // UPLOAD PHOTO BUTTON.  Choose from filesystem using intent.
    public void uploadPhoto(View view) {

        Intent intent;

        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_FILE);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_FILE);
        }
    }


    void checkPermissions() {
        //select which permission you want
        final String permission = Manifest.permission.CAMERA;
        //final String permission = Manifest.permission.Storage;
        // if in fragment use getActivity()
        if (ContextCompat.checkSelfPermission(StartupActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(StartupActivity.this, permission)) {

            } else {
                ActivityCompat.requestPermissions(StartupActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_RUNTIME_PERMISSION);
            }
        } else {
            // you have permission go ahead
            // ignored
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RUNTIME_PERMISSION:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    // you have permission go ahead
                    // ignored
                }else{
                    // you dont have permission show toast
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void insertToDB(View view) {
            DBHelper.getInstance(this).insertPhoto(
                    new Image(currentURI.toString(), IMG_NAME + Image.getCount(), "",
                              "" + (month) + "-" + day + "-" + year));
            Intent intent = new Intent(this, GalleryGrid.class);
            startActivity(intent);
    }
}
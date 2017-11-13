package com.example.zac.myapplication.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zac.myapplication.R;
import com.example.zac.myapplication.classes.Image;
import com.example.zac.myapplication.database.DBHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import static java.lang.Math.round;

public class StartupActivity extends AppCompatActivity {

    private final int REQUEST_RUNTIME_PERMISSION = 0;
    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    private Uri currentURI = null; //for clicking a BUTTON
    private String dir = null;
    private int month, day, year;   // used for TimeStamps
    private String timeStamp;
    private double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        checkPermissions();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("QuickPic");

        //Ignores URI Issue
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //Makes Directory if does not exist
        dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/PICTURES/";
        File newDirectory = new File(dir);
        newDirectory.mkdirs();

    }



    // TAKE PHOTO BUTTON.  Opens Android Camera using intent.
    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        setDate();
        File image = new File(dir, "IMAGE_" + timeStamp + ".jpg");
        currentURI = Uri.fromFile(image);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, currentURI);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case SELECT_FILE:
                if (resultCode == RESULT_OK) {
                    onSelectImageResult(data);
                }
                break;

            case REQUEST_CAMERA:
                Log.e("CAMERA", "resultCode: " + resultCode);
                Log.e("CAMERA", "resultCode: " + RESULT_OK);
                if (resultCode == RESULT_OK) {
                    onCaptureImageResult(data);
                }
                break;
        }
    super.onActivityResult(requestCode, resultCode, data);
    }





    @SuppressWarnings("deprecation")
    private void onSelectImageResult(Intent data) {

        setDate();
        // Get the Uri of the selected file
        Uri uri = data.getData();
        currentURI = uri;
        Log.d("select file", "File Uri: " + uri.toString());
        this.grantUriPermission(this.getPackageName(), currentURI, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        this.getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Get the path
        String path = null;
        try {
            path = getPath(this, uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Log.d("select file", "File Path: " + path);
        // Get the file instance
        // Initiate the upload
        Intent intent = new Intent(this, PreviewImageLarge.class);
        intent.putExtra("URI", currentURI.toString());
        intent.putExtra("DIR", dir);
        intent.putExtra("MONTH", "" + month);
        intent.putExtra("DAY", "" + day);
        intent.putExtra("YEAR", "" + year);
        startActivity(intent);
    }



    //Use this for now
    private void onCaptureImageResult(Intent data) {
        setDate();
        Intent intent = new Intent(this, PreviewImageLarge.class);
        intent.putExtra("URI", currentURI.toString());
        intent.putExtra("DIR", dir);
        intent.putExtra("MONTH", "" + month);
        intent.putExtra("DAY", "" + day);
        intent.putExtra("YEAR", "" + year);
        startActivity(intent);
    }



    // UPLOAD PHOTO BUTTON.  Choose from filesystem using intent.
    public void uploadPhoto(View view) {

        Intent intent;
        setDate();
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
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



    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;

    }



    private void setDate() {
        Calendar mcurrentDate = Calendar.getInstance();
        //+ Calendar.YEAR + Calendar.MONTH + Calendar.DAY_OF_MONTH + "_" +
        timeStamp = "" + mcurrentDate.YEAR + mcurrentDate.MONTH + mcurrentDate.DAY_OF_MONTH
                + mcurrentDate.HOUR + mcurrentDate.MINUTE + mcurrentDate.SECOND;

        month = mcurrentDate.get(Calendar.MONTH) + 1;
        day = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        year = mcurrentDate.get(Calendar.YEAR);
        Log.d("DATE", "   DATE: " + month + "-" + day + "-" + year);

    }


    public void viewGallery(View view) {
        setDate();
        Intent intent = new Intent(this, GalleryGrid.class);
        intent.putExtra("MONTH", "" + (month - 1));
        intent.putExtra("DAY", "" + day);
        intent.putExtra("YEAR", "" + year);
        Log.d("", "BEFORE SWITCH INTENT");
        startActivity(intent);
    }
}

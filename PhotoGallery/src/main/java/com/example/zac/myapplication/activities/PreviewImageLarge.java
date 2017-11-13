package com.example.zac.myapplication.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zac.myapplication.R;
import com.example.zac.myapplication.classes.Image;
import com.example.zac.myapplication.database.DBHelper;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;

public class PreviewImageLarge extends AppCompatActivity implements LocationListener {

    private ImageView imageView = null;
    private Uri currentURI = null; //for clicking a BUTTON
    private final String IMG_NAME = "IMG_";
    private String dir = null;
    private int month, day, year;   // used for TimeStamps
    private String caption = "";
    private final int PLACE_PICKER_REQUEST = 2;
    LocationManager locationManager;
    private double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image_large);

        checkPermissions();
        imageView = (ImageView) findViewById(R.id.PreviewImageCapture);



        Intent intent = getIntent();
        if (intent != null) {
            String message = intent.getStringExtra("URI");
            //message = intent.getStringExtra("URI_CAMERA");
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
        getLocation();
    }




    public void uploadPhoto(View view) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter caption tags");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tag = input.getText().toString();
                caption = tag;
                if (caption == null )
                    caption = "";
                upload();

            }
        });

        builder.show();
    }


    public void selectLocation(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                Log.e("PLACE_PICKER", "ABOUT TO START LOCATION ACTIVITY");
                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
                Log.e("PLACE_PICKER", "PLACE PICKER FAILED");
            }
    }


    @SuppressWarnings("deprecation")
    private void setLocation(Intent data) {
        Log.e("PLACE_PICKER", "inside setLocation()");
        Place place = PlacePicker.getPlace(data, this);
        String toastMsg = String.format("Place: %s", place.getName());
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
        Log.e("LOCATION", "" + place.getLatLng());

        latitude = place.getLatLng().latitude;
        longitude = place.getLatLng().longitude;
    }


    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                Log.e("OnActivityResult", "resultCode: " + resultCode);
                Log.e("OnActivityResult", "RESULT_OK: " + RESULT_OK);
                if (resultCode == RESULT_OK) {
                    Log.e("OnActivityResult", "INSIDE ACTIVITY RESULT");
                    setLocation(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(this, "Current Location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
        //locationText.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        Log.e("PLACE_PICKER", "inside OnLocationChanged()");
        Log.e("Location: ", location.getLatitude() + ", " + location.getLongitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Please enable GPS", Toast.LENGTH_SHORT).show();
    }


    public void upload() {
        DBHelper.getInstance(this).insertPhoto(
                new Image(0, currentURI.toString(), IMG_NAME + Image.getCount(), caption,
                        "" + month + "-" + day + "-" + year, latitude, longitude));


        Intent intent = new Intent(this, GalleryGrid.class);
        intent.putExtra("MONTH", "" + month);
        intent.putExtra("DAY", "" + day);
        intent.putExtra("YEAR", "" + year);

        startActivity(intent);
    }

    public void checkPermissions() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }



    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }



    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}

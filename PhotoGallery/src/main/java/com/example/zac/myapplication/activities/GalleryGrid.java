package com.example.zac.myapplication.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import java.util.StringTokenizer;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.zac.myapplication.R;
import com.example.zac.myapplication.classes.CreateList;
import com.example.zac.myapplication.classes.Image;
import com.example.zac.myapplication.classes.RecyclerViewAdapter;
import com.example.zac.myapplication.database.DBHelper;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GalleryGrid extends AppCompatActivity implements LocationListener {

    private TextView mTextMessage;
    private boolean startDateOn = false, endDateOn = false;
    private boolean captionFilterOn = false;
    private boolean startLocationOn = false, endLocationOn = false;
    private String caption = null;
    private int curMonth, curDay, curYear;   // used for TimeStamps
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;
    private RecyclerView rv;
    private RecyclerViewAdapter adapter;
    private LocationManager locationManager;
    private double startLat = 100, startLong = 200, endLat = 100, endLong = 200;
    private double latCenter = 0, longCenter = 0;
    private final int START_PICKER_REQUEST = 1;
    private final int END_PICKER_REQUEST = 2;
    //private MenuItem pickDate = null;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_share:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_camera:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_gallery:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_grid);

        Intent intent = getIntent();
        curMonth = Integer.parseInt(intent.getStringExtra("MONTH"));
        curDay = Integer.parseInt(intent.getStringExtra("DAY"));
        curYear = Integer.parseInt(intent.getStringExtra("YEAR"));

        startMonth = -1;
        startDay = -1;
        startYear = -1;
        endMonth = -1;
        endDay = -1;
        endYear = -1;

        if (intent != null) {
            mTextMessage = (TextView) findViewById(R.id.message);
            rv = (RecyclerView) findViewById(R.id.gallery);
            rv.setHasFixedSize(true);
            ArrayList<CreateList> createLists = prepareData();

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
            rv.setLayoutManager(layoutManager);

            adapter = new RecyclerViewAdapter(getApplicationContext(), createLists);
            rv.setAdapter(adapter);

            // Toolbar
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);
            getSupportActionBar().setTitle("QuickPic");

            // Bottom nav bar
            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            //pickDate = (MenuItem) findViewById(R.id.filter_location);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }



    /*
     * Start date selecter dailog for the date filter.
     */
    public void filterStartDate(MenuItem item) {
        //Log.e("PICK_START_DATE", "   START:  " + (startMonth + 1) + "-" + startDay + "-" + startYear);
        DatePickerDialog dp = new DatePickerDialog(GalleryGrid.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                setStartDate(month, day, year);
            }
        }, curYear, curMonth, curDay);

        dp.setTitle("Select Start Date");
        dp.show();
    }



    /*
     * End date selecter dialog for the date filter.
     */
    public void filterEndDate(MenuItem item) {
        //Log.e("PICK_END_DATE", "   END:  " + (endMonth + 1) +  "-" + endDay + "-" + endYear);
        DatePickerDialog dp = new DatePickerDialog(GalleryGrid.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                setEndDate(month, day, year );
            }
        }, curYear, curMonth, curDay);

        dp.setTitle("Select End Date");
        dp.show();
    }




    /*
     * Caption filter dialog.
     */
    public void filterCaption(MenuItem item) {
        final EditText searchedTag = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Filter Caption")
                .setMessage("search for tags")
                .setView(searchedTag)
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String tag = searchedTag.getText().toString();
                        setCaption(tag);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }



    /*
     * Set the caption and activate the caption filter.
    */
    public void setCaption(String tag) {
        if (!tag.equals(""))
            this.captionFilterOn = true;
        else
            this.captionFilterOn = false;
        this.caption = tag.toUpperCase();
        Log.e("SEARCH_TAG", "CAPTION_BOOL:  " + captionFilterOn);
        Log.e("SEARCH_TAG", "SEARCH_TAG:  " + tag);
        ArrayList<CreateList> createLists = prepareData();

        refreshGallery(createLists);
    }




    /*
     *  Imporant Method:  prepareData() accesses images from the SQLite database (DBhelper) to display.
     *                    this method also does all of the filtering.
     */
    private ArrayList<CreateList> prepareData(){
        Log.e("LocationBool", "Location Bool: startOn: " + startLocationOn + ", endOn: " + endLocationOn);
        ArrayList<CreateList> imageList = new ArrayList<>();

        ArrayList<Image> dbImages = DBHelper.getInstance(this).getGallery();
        for(int i = 0; i < dbImages.size(); i ++) {
            Log.e("dbIMAGE", "   ID: " + dbImages.get(i).getID() + "   DATE: " + dbImages.get(i).getTimeStamp());
        }

        // IF Tag Filter is the only filter being used.
        if (captionFilterOn && !startDateOn && !endDateOn && !startLocationOn && !endLocationOn) {
            for(int i = 0; i < dbImages.size(); i++) {
                CreateList createList = new CreateList();

                // Tokenize the caption by space to get multiple tags for each photo.
                String str = dbImages.get(i).getCaption();
                String[] splitStr = str.trim().split("\\s+");

                for (String token: splitStr) {
                    //Log.e("TOKENS", "TOKEN: " + token);
                    if (caption.equals(token.toUpperCase())) {
                        loadData(imageList, dbImages, createList, i);
                    }
                }
            }
            return imageList;
        }

        // IF ONLY DATE Filter is being used
        else if (startDateOn && endDateOn && !captionFilterOn && !startLocationOn && !endLocationOn) {
            Log.e("DateFiler_PrepareData", "INSIDE DATE_FILTER LOOP");
            for (int i = 0; i < dbImages.size(); i++) {
                CreateList createList = new CreateList();

                if (compareStartTimestamp(dbImages.get(i).getTimeStamp(),
                        startMonth, startDay, startYear) && compareEndTimestamp(dbImages.get(i).getTimeStamp(),
                        endMonth, endDay, endYear)) {
                    loadData(imageList, dbImages, createList, i);

                }
            }
            return imageList;
        }

        // IF Tag AND Date Filters are being used
        else if (startDateOn && endDateOn && captionFilterOn && !startLocationOn && !endLocationOn) {
            // IF ONLY DATE Filter is being used
            Log.e("DateFiler_PrepareData", "INSIDE DATE_FILTER LOOP");
            for (int i = 0; i < dbImages.size(); i++) {
                CreateList createList = new CreateList();

                String str = dbImages.get(i).getCaption();
                String[] splitStr = str.trim().split("\\s+");

                for (String token: splitStr) {
                    //Log.e("TOKENS", "TOKEN: " + token);
                    if (caption.equals(token.toUpperCase())) {
                        if (compareStartTimestamp(dbImages.get(i).getTimeStamp(),
                                startMonth, startDay, startYear) && compareEndTimestamp(dbImages.get(i).getTimeStamp(),
                                endMonth, endDay, endYear)) {
                            loadData(imageList, dbImages, createList, i);
                        }
                    }
                }
            }
            return imageList;
        }

        // If ONLY Location filter is being used.
        else if (startLocationOn && endLocationOn && !captionFilterOn && !startDateOn && !endDateOn) {
            for (int i = 0; i < dbImages.size(); i++) {
                CreateList createList = new CreateList();

                if (isWithinLocation(dbImages.get(i).getLatitude(), dbImages.get(i).getLongitude())) {
                    loadData(imageList, dbImages, createList, i);
                }
            }
            return imageList;
        }

        // If Location AND Tag filters are being used.
        else if (startLocationOn && endLocationOn && captionFilterOn && !startDateOn && !endDateOn) {
            for (int i = 0; i < dbImages.size(); i++) {
                CreateList createList = new CreateList();

                String str = dbImages.get(i).getCaption();
                String[] splitStr = str.trim().split("\\s+");

                for (String token: splitStr) {
                    //Log.e("TOKENS", "TOKEN: " + token);
                    if (caption.equals(token.toUpperCase())) {
                        if (isWithinLocation(dbImages.get(i).getLatitude(), dbImages.get(i).getLongitude())) {
                            loadData(imageList, dbImages, createList, i);
                        }
                    }
                }
            }
            return imageList;
        }

        // If Location AND Date filters are being used.
        else if (startLocationOn && endLocationOn && !captionFilterOn && startDateOn && endDateOn) {
            for (int i = 0; i < dbImages.size(); i++) {
                CreateList createList = new CreateList();

                if (isWithinLocation(dbImages.get(i).getLatitude(), dbImages.get(i).getLongitude())) {
                    if (compareStartTimestamp(dbImages.get(i).getTimeStamp(),
                            startMonth, startDay, startYear) && compareEndTimestamp(dbImages.get(i).getTimeStamp(),
                            endMonth, endDay, endYear)) {
                        loadData(imageList, dbImages, createList, i);
                    }
                }
            }
            return imageList;
        }

        // If ALL FILTERS are being used.
        else if (startLocationOn && endLocationOn && captionFilterOn && startDateOn && endDateOn) {
            for (int i = 0; i < dbImages.size(); i++) {
                CreateList createList = new CreateList();

                String str = dbImages.get(i).getCaption();
                String[] splitStr = str.trim().split("\\s+");

                for (String token: splitStr) {
                    //Log.e("TOKENS", "TOKEN: " + token);
                    if (caption.equals(token.toUpperCase())) {
                        if (isWithinLocation(dbImages.get(i).getLatitude(), dbImages.get(i).getLongitude())) {
                            if (compareStartTimestamp(dbImages.get(i).getTimeStamp(),
                                    startMonth, startDay, startYear) && compareEndTimestamp(dbImages.get(i).getTimeStamp(),
                                    endMonth, endDay, endYear)) {
                                loadData(imageList, dbImages, createList, i);
                            }
                        }
                    }
                }
            }
            return imageList;
        }



        // No Filters!
        else {
                // IF nothing is being used.
                for (int i = 0; i < dbImages.size(); i++) {
                    CreateList createList = new CreateList();
                    loadData(imageList, dbImages, createList, i);
                }
            return imageList;
            }
    }


    /*
     * Only gets called from inside prepareData().  Loads in the photos to gallery from the database.
     */
    private void loadData(ArrayList<CreateList> imageList, ArrayList<Image> dbImages, CreateList createList, int i) {
        createList.setImgName(dbImages.get(i).getCaption());
        createList.setImgID(dbImages.get(i).getID());
        // URI
        Uri uri = Uri.parse(dbImages.get(i).getUri());
        Bitmap thumbnail = null;
        try {
            InputStream stream = getContentResolver().openInputStream(uri);
            thumbnail = BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        createList.setImgBitmap(thumbnail);
        imageList.add(createList);

    }



    /*
     * Refreshes the displayed photo gallery.  Gets called from filter setters.
     */
    private void refreshGallery(ArrayList<CreateList> imgs) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        rv.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(getApplicationContext(), imgs);
        rv.setAdapter(adapter);
    }



    /*
     * Returns true if the A) image date comes after OR during the  B) Filter starting date.
     */
    boolean compareStartTimestamp(String imgDate, int cmpMonth, int cmpDay, int cmpYear) {
        String str = imgDate;
        String[] splitStr = str.trim().split("-");
        int month = Integer.parseInt(splitStr[0]);
        int day = Integer.parseInt(splitStr[1]);
        int year = Integer.parseInt(splitStr[2]);
        Log.e("COMPARE_ST_DATE", "Parsed start date(mm-dd-yy): " + month + "-" + day + "-" + year);
        Log.e("COMPARE_ST_DATE", "cmp start date(mm-dd-yy): " + cmpMonth + "-" + cmpDay + "-" + cmpYear);

        if (year > cmpYear)
            return true;
        if (year == cmpYear)
            if (month > cmpMonth)
                return true;
            else if (month == cmpMonth)
                if (day >= cmpDay)
                    return true;
        Log.e("COMPARE_ST_DATE", "start date comaprison: BAD NEWS BEARS");
        return false;
    }




    /*
     * Returns true if the A) image date comes before OR during the  B) Filter ending date.
     */
    boolean compareEndTimestamp(String imgDate, int cmpMonth, int cmpDay, int cmpYear) {
        String str = imgDate;
        String[] splitStr = str.trim().split("-");
        int month = Integer.parseInt(splitStr[0]);
        int day = Integer.parseInt(splitStr[1]);
        int year = Integer.parseInt(splitStr[2]);
        Log.e("COMPARE_END_DATE", "Parsed end date(mm-dd-yy): " + month + "-" + day + "-" + year);
        Log.e("COMPARE_END_DATE", "cmp end date(mm-dd-yy): " + cmpMonth + "-" + cmpDay + "-" + cmpYear);

        if (year < cmpYear)
            return true;
        if (year == cmpYear)
            if (month < cmpMonth)
                return true;
            else if (month == cmpMonth)
                if (day <= cmpDay)
                    return true;
        Log.e("COMPARE_END_DATE", "end date comaprison: BAD NEWS BEARS");
        return false;
    }



    /*
     * Sets the start date filter and activates the filter to display results.
     * Will ONLY activate immediately IF an End date filter has been set.
     */
    public void setStartDate(int month, int day, int year) {
        startYear = year;
        startMonth = month + 1;
        startDay = day;
        this.startDateOn = true;

        if (this.endDateOn) {
            ArrayList<CreateList> createLists = prepareData();
            refreshGallery(createLists);
        }
    }



    /*
     * Sets the end date filter and activates the filter to display results.
     *  Will ONLY activate immediately IF a Start date filter has been set.
     */
    public void setEndDate(int month, int day, int year) {
        endYear = year;
        endMonth = month + 1;
        endDay = day;
        this.endDateOn = true;

        if (this.startDateOn) {
            ArrayList<CreateList> createLists = prepareData();
            refreshGallery(createLists);
        }
    }



    /*
     *  RESETS EVERY FILTER.   BACK TO DEFAULT GALLERY VIEW.
     */
    public void resetFilters(MenuItem item) {
        this.captionFilterOn = false;
        this.caption = "";
        this.startDateOn = false;
        this.endDateOn = false;
        startMonth = -1;
        startDay = -1;
        startYear = -1;
        endMonth = -1;
        endDay = -1;
        endYear = -1;
        this.startLocationOn = false;
        this.endLocationOn = false;
        startLat = 100;
        startLong = 200;
        endLat = 100;
        endLong = 200;
        ArrayList<CreateList> createLists = prepareData();
        refreshGallery(createLists);
    }






    /*
     *  ******     LOCATION STUFF in the methods below!   *******
     */




    /*
     * Opens start location picker.
     */
    public void selectStartLocation(MenuItem item) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Log.e("START_PICKER", "ABOUT TO START LOCATION ACTIVITY");
            startActivityForResult(builder.build(this), START_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            Log.e("START_PICKER", "START PLACE PICKER FAILED");
        }
    }



    /*
     * Opens end location picker.
     */
    public void selectEndLocation(MenuItem item) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Log.e("END_PICKER", "ABOUT TO START LOCATION ACTIVITY");
            startActivityForResult(builder.build(this), END_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            Log.e("END_PICKER", "END PICKER FAILED");
        }
    }


    /*
     * Sets Start location filter.
     */
    @SuppressWarnings("deprecation")
    private void setStartLocation(Intent data) {
        Log.e("START_PICKER", "inside setStartLocation()");
        Place place = PlacePicker.getPlace(data, this);
        String toastMsg = String.format("Place: %s", place.getName());
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
        startLat = place.getLatLng().latitude;
        startLong = place.getLatLng().longitude;
        //Log.e("START_LOCATION", "Chosen start loc:  LAT: " + startLat +  ",  LON: " + startLong);
        if (startLat <= 90 && startLong <= 180) {  // I'm using impossible lat long values for false/reset.
            this.startLocationOn = true;
            Log.e("START_LOCATION", "Chosen start loc:  " + place.getLatLng());
            if (this.endLocationOn) {   // if End location has already been set, we can apply the filter.
                ArrayList<CreateList> createLists = prepareData();
                refreshGallery(createLists);
            }
        }
        else
            this.startLocationOn = false;
    }


    /*
     * Sets End location filter.
     */
    @SuppressWarnings("deprecation")
    private void setEndLocation(Intent data) {
        Log.e("END_PICKER", "inside setEndLocation()");
        Place place = PlacePicker.getPlace(data, this);
        String toastMsg = String.format("Place: %s", place.getName());
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
        //Log.e("END_LOCATION", "Chosen end loc:  LAT: " + endLat +  ",  LON: " + endLong);
        endLat = place.getLatLng().latitude;
        endLong = place.getLatLng().longitude;

        if (endLat <= 90 && endLong <= 180) {  // I'm using impossible lat long values for false/reset.
            this.endLocationOn = true;
            Log.e("END_LOCATION", "Chosen end loc:  " + place.getLatLng());
            if (this.startLocationOn) {   // if Start Location has already been set, we can apply the filter.
                ArrayList<CreateList> createLists = prepareData();
                refreshGallery(createLists);
            }
        }
        else
            this.endLocationOn = false;
    }



    /*
     *  Returns whether or not the passed in photo Location is between the start and end location filter points.
     */
    private boolean isWithinLocation(double latTest, double longTest) {
        //Get the distance between start and end filter points.
        float[] results = new float[1];
        Location.distanceBetween(startLat, startLong, endLat, endLong, results);
        float startToEndDistance = results[0];   // distance bewteen start and end points. IN METERS.

        float halfDist = startToEndDistance / 2;  // half the distance between the two points.

        results = new float[1];
        Location.distanceBetween(startLat, startLong, latTest, longTest, results);
        float distancePointToStart = results[0];   // distance bewteen start and end points.

        results = new float[1];
        Location.distanceBetween(endLat, endLong, latTest, longTest, results);
        float distancePointToEnd = results[0];   // distance bewteen start and end points.

        if (distancePointToStart <= halfDist && distancePointToEnd <= startToEndDistance) {
            return true;
        }
        else if (distancePointToEnd <= halfDist && distancePointToStart <= startToEndDistance) {
            return true;
        }
        else
            return false;
    }


    /*
     * Used to get the current location.   May not be useful here.
     */
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
            case START_PICKER_REQUEST:
                Log.e("OnActivityResult", "resultCode: " + resultCode);
                Log.e("OnActivityResult", "RESULT_OK: " + RESULT_OK);
                if (resultCode == RESULT_OK) {
                    setStartLocation(data);
                }
                break;

            case END_PICKER_REQUEST:
                Log.e("OnActivityResult", "resultCode: " + resultCode);
                Log.e("OnActivityResult", "RESULT_OK: " + RESULT_OK);
                if (resultCode == RESULT_OK) {
                    setEndLocation(data);
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
        //locationManager.removeUpdates(this);
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






    /*
     *   ***********   LOCATION PERMISSION STUFF BELOW  ***********
     */


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
        }
    }
}

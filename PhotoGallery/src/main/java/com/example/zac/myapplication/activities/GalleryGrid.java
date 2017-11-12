package com.example.zac.myapplication.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import java.util.StringTokenizer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.zac.myapplication.R;
import com.example.zac.myapplication.classes.CreateList;
import com.example.zac.myapplication.classes.Image;
import com.example.zac.myapplication.classes.RecyclerViewAdapter;
import com.example.zac.myapplication.database.DBHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GalleryGrid extends AppCompatActivity {

    private TextView mTextMessage;
    private boolean startDateOn = false, endDateOn = false;
    private boolean captionFilterOn = false;
    private boolean locationFilterOn = false;
    private String caption = null;
    private int curMonth, curDay, curYear;   // used for TimeStamps
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;
    private Thread thread;
    private RecyclerView rv;
    private RecyclerViewAdapter adapter;
    //private MenuItem pickDate = null;

    private final String imgNames[] = {
            "samus",
            "placeholder",
            "starwars",
            "Droid-Tales",
            "LegoStarWars",
            "LegoStarWars2",
            "lego_2",
            "samus",
            "placeholder",
            "starwars",
            "Droid-Tales",
            "LegoStarWars",
            "LegoStarWars2",
            "lego_2"
    };

    private final int imgIDs[] = {
            R.drawable.samus,
            R.drawable.placeholder,
            R.drawable.starwars,
            R.drawable.droidtales,
            R.drawable.legostarwars,
            R.drawable.legostarwars2,
            R.drawable.lego_2,
            R.drawable.samus,
            R.drawable.placeholder,
            R.drawable.starwars,
            R.drawable.droidtales,
            R.drawable.legostarwars,
            R.drawable.legostarwars2,
            R.drawable.lego_2
    };



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
            rv = (RecyclerView) findViewById(R.id.imagegallery);
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

        /*Uri uri = Uri.parse("android.resource://com.example.zac.myapplication.activities/drawable/starwars.jpg");
        try {
            InputStream stream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }



    /*public void filterTime(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GalleryGrid.this);
        builder.setTitle("Filter by Time");
        builder.setItems(new CharSequence[]
                        {"Start Date", "End Date", "Apply"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                Log.e("PICK_START_DATE", "   START:  " + (startMonth + 1) + "-" + startDay + "-" + startYear);
                                //showCalenderStart(curYear, curDay, curMonth);
                                DatePickerDialog dp = new DatePickerDialog(GalleryGrid.this, new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                                        setStartDate(month, day, year);
                                    }
                                }, curYear, curMonth, curDay);

                                dp.setTitle("Select Start Date");
                                dp.show();
                                break;
                            case 1:
                                Log.e("PICK_END_DATE", "   END:  " + (endMonth + 1) +  "-" + endDay + "-" + endYear);
                                showCalenderEnd(curYear, curDay, curMonth);
                                break;
                            case 2:
                                break;
                        }
                    }
                });
        builder.create().show();
    }*/


    public void filterStartDate(MenuItem item) {
        Log.e("PICK_START_DATE", "   START:  " + (startMonth + 1) + "-" + startDay + "-" + startYear);
        DatePickerDialog dp = new DatePickerDialog(GalleryGrid.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                setStartDate(month, day, year);
            }
        }, curYear, curMonth, curDay);

        dp.setTitle("Select Start Date");
        dp.show();
    }




    public void filterEndDate(MenuItem item) {
        Log.e("PICK_END_DATE", "   END:  " + (endMonth + 1) +  "-" + endDay + "-" + endYear);
        DatePickerDialog dp = new DatePickerDialog(GalleryGrid.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                setEndDate(month, day, year );
            }
        }, curYear, curMonth, curDay);

        dp.setTitle("Select End Date");
        dp.show();
    }



    /*void showCalenderStart(int year, int day, int month) {
        // show today
        DatePickerDialog dp = new DatePickerDialog(GalleryGrid.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                setStartDate(month, day, year);
            }
        }, curYear, curMonth, curDay);

        dp.setTitle("Select Start Date");
        dp.show();

    }



    void showCalenderEnd(int year, int day, int month) {
        DatePickerDialog dp = new DatePickerDialog(GalleryGrid.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                setEndDate(month, day, year );
            }
        }, curYear, curMonth, curDay);

        dp.setTitle("Select End Date");
        dp.show();
    }*/




    public void filterCaption(MenuItem item) {
        final EditText searchedTag = new EditText(this);

        // Set the default text to a link of the Queen
        //txtUrl.setHint("http://www.librarising.com/astrology/celebs/images2/QR/queenelizabethii.jpg");

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


    private void setCaption(String tag) {
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



    public void filterLocation(MenuItem item) {
        this.captionFilterOn = true;

    }

    /*public void filterLocation(MenuItem item) {
        this.locationFilterOn = true;
    }*/



    private ArrayList<CreateList> prepareData(){

        ArrayList<CreateList> imageList = new ArrayList<>();

        ArrayList<Image> dbImages = DBHelper.getInstance(this).getGallery();
        for(int i = 0; i < dbImages.size(); i ++) {
            Log.d("dbIMAGE", "   ID: " + dbImages.get(i).getID() + "   DATE: " + dbImages.get(i).getTimeStamp());
        }


        // IF Tag Filter is the only filter being used.
        if (captionFilterOn && !startDateOn && !endDateOn) {
            for(int i = 0; i < dbImages.size(); i++) {
                CreateList createList = new CreateList();

                // Tokenize the caption by space to get multiple tags for each photo.
                String str = dbImages.get(i).getCaption();
                String[] splitStr = str.trim().split("\\s+");

                for (String token: splitStr) {
                    //Log.e("TOKENS", "TOKEN: " + token);
                    if (caption.equals(token.toUpperCase())) {
                        // get image tag
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
                }
            }
            return imageList;
        }



        else if (startDateOn && endDateOn && !captionFilterOn) {
            // IF ONLY DATE Filter is being used
            Log.e("DateFiler_PrepareData", "INSIDE DATE_FILTER LOOP");
            for (int i = 0; i < dbImages.size(); i++) {
                CreateList createList = new CreateList();

                if (compareStartTimestamp(dbImages.get(i).getTimeStamp(),
                        startMonth, startDay, startYear) && compareEndTimestamp(dbImages.get(i).getTimeStamp(),
                        endMonth, endDay, endYear)) {
                    // get image tag
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
            }
            return imageList;
        }



        // IF Tag AND Date Filters are being used
        else if (startDateOn && endDateOn && captionFilterOn) {
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
                            // get image tag
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
                    }
                }
            }
            return imageList;
        }

    else {
            // IF nothing is being used.
            for (int i = 0; i < dbImages.size(); i++) {
                CreateList createList = new CreateList();
                // get image tag
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
        }
        return imageList;

    }



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

        //Log.e("COMPARE_ST_DATE", "start date comaprison: " + (year >= cmpYear && month > cmpMonth && day > cmpDay));
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

        //Log.e("COMPARE_END_DATE", "end date comaprison: " + (year <= cmpYear && month < cmpMonth && day < cmpDay));
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
        ArrayList<CreateList> createLists = prepareData();
        refreshGallery(createLists);
    }
}

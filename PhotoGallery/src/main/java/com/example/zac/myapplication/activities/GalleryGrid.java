package com.example.zac.myapplication.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.zac.myapplication.Dialogs.LocationDialog;
import com.example.zac.myapplication.Dialogs.TagDialog;
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
    private boolean timeFilterOn = false;
    private boolean captionFilterOn = false;
    private boolean locationFilterOn = false;
    private int curMonth, curtDay, curYear;   // used for TimeStamps
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
        curtDay = Integer.parseInt(intent.getStringExtra("DAY"));
        curYear = Integer.parseInt(intent.getStringExtra("YEAR"));

        this.startMonth = curMonth;
        this.startDay = curtDay;
        this.startYear = curYear;
        this.endMonth = curMonth;
        this.endDay = curtDay;
        this.endYear = curYear;

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



    public void filterTime(MenuItem item) {
        timeFilterOn = true;
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
                                showCalenderStart(curYear, curtDay, curMonth);
                                Log.e("PICK_START_DATE", "   START:  " + (startMonth + 1) + "-" + startDay + "-" + startYear);
                                break;
                            case 1:
                                showCalenderEnd(curYear, curtDay, curMonth);
                                Log.e("PICK_END_DATE", "   END:  " + (endMonth + 1) +  "-" + endDay + "-" + endYear);
                                break;
                            case 2:
                                break;
                        }
                    }
                });
        builder.create().show();
    }



    void showCalenderStart(int year, int day, int month) {
        // show today
        DatePickerDialog dp = new DatePickerDialog(GalleryGrid.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                setStartDate(month, day, year);
            }
        }, startYear, startMonth, startDay);

        dp.setTitle("Select Start Date");
        dp.show();

    }



    void showCalenderEnd(int year, int day, int month) {
        DatePickerDialog dp = new DatePickerDialog(GalleryGrid.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                setEndDate(month, day, year );
            }
        }, endYear, endMonth, endDay);

        dp.setTitle("Select End Date");
        dp.show();


    }


    /*public void filterTime(MenuItem item) {
        this.timeFilterOn = true;
        Intent intent = new Intent(this, DateActivity.class);
        intent.putExtra("MONTH", "" + curMonth);
        intent.putExtra("DAY", "" + curtDay);
        intent.putExtra("YEAR", "" + curYear);
        startActivity(intent);

    }*/


    public void filterCaption(MenuItem item) {
        this.captionFilterOn = true;
        final TagDialog td = new TagDialog();
        td.show(getFragmentManager(), "Dialog");

        thread = new Thread () {

            @Override
            public void run () {
                while (td.getStatus ().equals ("") || td.getStatus ().equals ("waiting"));
                Log.e (":)", td.getStatus());
                if (td.getStatus().equals ("search")) {
                    final ArrayList<CreateList> imgs = prepareDataCaption();
                    Log.e (":)", "" + imgs.size());
                    runOnUiThread(new Runnable () {

                        @Override
                        public void run () {
                            refreshGallery(imgs);
                        }

                    });
                }
            }
        };
        thread.start();
    }




    public void filterLocation(MenuItem item) {
        this.captionFilterOn = true;
        final LocationDialog td = new LocationDialog();
        td.show(getFragmentManager(), "Dialog");

        thread = new Thread () {

            @Override
            public void run () {
                while (td.getStatus ().equals ("") || td.getStatus ().equals ("waiting"));
                Log.e (":)", td.getStatus());
                if (td.getStatus().equals ("search")) {
                    final ArrayList<CreateList> imgs = prepareDataCaption();
                    Log.e (":)", "" + imgs.size());
                    runOnUiThread(new Runnable () {

                        @Override
                        public void run () {

                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                            rv.setLayoutManager(layoutManager);

                            adapter = new RecyclerViewAdapter(getApplicationContext(), imgs);
                            rv.setAdapter(adapter);
                        }

                    });
                }
            }
        };
        thread.start();
    }

    /*public void filterLocation(MenuItem item) {
        this.locationFilterOn = true;
    }*/


    private ArrayList<CreateList> prepareDataCaption(){

        ArrayList<CreateList> imageList = new ArrayList<>();

        ArrayList<Image> dbImages = DBHelper.getInstance(getApplicationContext()).filterCaption(TagDialog.search_text);
        Log.e (":)", "" + dbImages.size());
        for(int i = 0; i < dbImages.size(); i ++) {
            Log.d("dbIMAGE", "   ID: " + dbImages.get(i).getID() + "   DATE: " + dbImages.get(i).getTimeStamp());
        }

        //}
        for(int i = 0; i < dbImages.size(); i++) {
            CreateList createList = new CreateList();
            // get image tag
            createList.setImgName(dbImages.get(i).getCaption());
            createList.setImgID(dbImages.get(i).getID());
            // URI
            Uri uri = Uri.parse(dbImages.get(i).getUri());
            Bitmap thumbnail = null;
            try {
                InputStream stream = getContentResolver().openInputStream(uri);
                thumbnail = BitmapFactory.decodeStream(stream );
            } catch (IOException e) {
                e.printStackTrace();
            }
            createList.setImgBitmap(thumbnail);
            imageList.add(createList);
        }
        return imageList;
    }


    private ArrayList<CreateList> prepareData(){

        ArrayList<CreateList> imageList = new ArrayList<>();
        /*for(int i = 0; i < imgNames.length; i++){   // DUMMY DATA loop
            CreateList createList = new CreateList();
            createList.setImgName(imgNames[i]);
            createList.setImgID(imgIDs[i]);
            imageList.add(createList);
        }
        return theimage;*/

        ArrayList<Image> dbImages = DBHelper.getInstance(this).getGallery();
        for(int i = 0; i < dbImages.size(); i ++) {
            Log.d("dbIMAGE", "   ID: " + dbImages.get(i).getID() + "   DATE: " + dbImages.get(i).getTimeStamp());
        }

        /*if (dbImages.size() == 0) {     // empty placeholders if I feel like it.
            for(int i = 0; i < 9; i++){
            createList.setImgName("empty");
            createList.setImgID( R.drawable.placeholder);
            imageList.add(createList);
        }*/
        //}

        for(int i = 0; i < dbImages.size(); i++) {
            CreateList createList = new CreateList();
            // get image tag
            createList.setImgName(dbImages.get(i).getCaption());
            createList.setImgID(dbImages.get(i).getID());
            // URI
            Uri uri = Uri.parse(dbImages.get(i).getUri());
            Bitmap thumbnail = null;
            try {
                InputStream stream = getContentResolver().openInputStream(uri);
                thumbnail = BitmapFactory.decodeStream(stream );
            } catch (IOException e) {
                e.printStackTrace();
            }
            createList.setImgBitmap(thumbnail);
            imageList.add(createList);

        }
        return imageList;
    }


    private void refreshGallery(ArrayList<CreateList> imgs) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        rv.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(getApplicationContext(), imgs);
        rv.setAdapter(adapter);
    }


    public void setStartDate(int month, int day, int year) {
        startYear = year;
        startMonth = month;
        startDay = day;
    }


    public void setEndDate(int month, int day, int year) {
        endYear = year;
        endMonth = month;
        endDay = day;
    }



    /*public void filterTime(MenuItem item) {
        this.captionFilterOn = true;
        final TimeDialog td = new TimeDialog();
        td.show(getFragmentManager(), "Dialog");

        thread = new Thread () {

            @Override
            public void run () {
                while (td.getStatus ().equals ("") || td.getStatus ().equals ("waiting"));
                Log.e (":)", td.getStatus());
                if (td.getStatus().equals ("search")) {
                    final ArrayList<CreateList> imgs = prepareDataCaption();
                    Log.e (":)", "" + imgs.size());
                    runOnUiThread(new Runnable () {

                        @Override
                        public void run () {

                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                            rv.setLayoutManager(layoutManager);

                            adapter = new RecyclerViewAdapter(getApplicationContext(), imgs);
                            rv.setAdapter(adapter);
                        }

                    });
                }
            }
        };
        thread.start();
    }*/
}

package com.example.zac.myapplication.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.zac.myapplication.R;
import com.example.zac.myapplication.classes.CreateList;
import com.example.zac.myapplication.classes.Image;
import com.example.zac.myapplication.classes.RecyclerViewAdapter;
import com.example.zac.myapplication.database.DBHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GalleryGrid extends AppCompatActivity {

    private TextView mTextMessage;
    private boolean timeFilterOn = false;
    private boolean captionFilterOn = false;
    private boolean locationFilterOn = false;
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
        if (intent != null) {

            mTextMessage = (TextView) findViewById(R.id.message);
            RecyclerView rv = (RecyclerView) findViewById(R.id.imagegallery);
            rv.setHasFixedSize(true);
            ArrayList<CreateList> createLists = prepareData();

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
            rv.setLayoutManager(layoutManager);

            RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), createLists);
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
       this.timeFilterOn = true;
    }


    public void filterCaption(MenuItem item) {
        this.captionFilterOn = true;
    }


    public void filterLocation(MenuItem item) {
        this.locationFilterOn = true;
        /*pickDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                // show today
                DatePickerDialog dp = new DatePickerDialog(PreviewImageLarge.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                    }
                }, year, month, day);
                dp.setTitle("Select date");
                dp.show();

                return false;
            }
        });*/
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
        if (!timeFilterOn && !captionFilterOn && !locationFilterOn ){
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
        }
        return imageList;
    }

}

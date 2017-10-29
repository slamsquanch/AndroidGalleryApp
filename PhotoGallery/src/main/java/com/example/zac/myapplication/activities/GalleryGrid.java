package com.example.zac.myapplication.activities;

import android.content.Intent;
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
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.zac.myapplication.R;
import com.example.zac.myapplication.classes.CreateList;
import com.example.zac.myapplication.classes.RecyclerViewAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class GalleryGrid extends AppCompatActivity {

    private TextView mTextMessage;

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

        Log.d("","INSIDE CREATE");

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





    private ArrayList<CreateList> prepareData(){

        ArrayList<CreateList> theimage = new ArrayList<>();
        for(int i = 0; i < imgNames.length; i++){
            CreateList createList = new CreateList();
            createList.setImgName(imgNames[i]);
            createList.setImgID(imgIDs[i]);
            theimage.add(createList);
        }
        return theimage;
    }

}

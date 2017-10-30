package com.example.zac.myapplication.classes;

import android.content.Intent;
import android.net.Uri;

import com.example.zac.myapplication.activities.GalleryGrid;
import com.example.zac.myapplication.database.DBHelper;

/**
 * Created by zacattack101 on 2017-10-29.
 */

public class PhotoUpload {
    private static Uri currentURI = null; //for clicking a BUTTON
    private static String dir = null;
    private static int month, day, year;   // used for TimeStamps
    public static final String IMG_NAME = "IMG_";


    public void setCurrentURI(Uri curURI) {
        this.currentURI = curURI;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setDate(int month, int day, int year) {
        this.month = month;
        this.day = day;
        this.year = year;
    }


    public Uri getCurrentURI() {
        return this.currentURI;
    }

    public String getDir() {
        return this.dir;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    public int getYear() {
        return this.year;
    }


}

package com.example.zac.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.zac.myapplication.classes.Image;

import java.util.ArrayList;

/**
 * Created by Zac on 2017-10-11.
 */

public class DBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "Image.db";
    public static final String DATABASE_ID = "id";
    public static final String TABLE_NAME = "ImageTable";
    public static final String[] COLUMN_STRING = {"id", "uri", "imgName", "caption", "time", "latitude", "longitude"};
    private static DBHelper database = null;




    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE;
        CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "uri TEXT," +
                "imgName TEXT," +
                "caption TEXT," +
                "time TEXT," +
                "latitude DOUBLE," +
                "longitude DOUBLE" +
                ")";
        db.execSQL(CREATE_TABLE);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS ImageTable");
        onCreate(db);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public static DBHelper getInstance(Context context) {
        if (database == null) {
            database = new DBHelper(context.getApplicationContext());
        }
        return database;
    }


    // insert pic
    public void insertPhoto(Image img) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put("id", img.getID());
        values.put("uri", img.getUri());
        values.put("imgName", img.getImgName());
        values.put("caption", img.getCaption());
        values.put("time", img.getTimeStamp());
        values.put("latitude", img.getLatitude());
        values.put("longitude", img.getLongitude());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    // delete pic
    public void deletePhoto(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, DATABASE_ID + " = ?",
                new String[] {id});
        db.close();
    }


    // retrieve pic
    public Image getPhoto(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMN_STRING,
                DATABASE_ID + "=?",
                new String[] {id},
                null,
                null,
                null,
                null);
        if (cursor != null)
            cursor.moveToFirst();
        Image img = new Image();
        populate(img, cursor);

        db.close();
        return img;
    }


    // get all the photos
    public ArrayList<Image> getGallery() {
        ArrayList<Image> images = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY ID ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Image temp = new Image();
                populate(temp, cursor);

                images.add(temp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return images;
    }


    /*public ArrayList<Image> filterTimestamp(String dateTimeFrom, String dateTimeTo) {
        ArrayList<Image> filteredPhotos = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY TIME ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Image temp = new Image();
                populate(temp, cursor);

                // Do date comparison compare to, date formatter

                if (temp.getTimeStamp().equals(dateTime)) {
                    filteredPhotos.add(temp);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return filteredPhotos;
    } */

    public ArrayList<Image> filterCaption (String tag) {
        ArrayList<Image> filteredPhotos = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY TIME ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        if (tag == null || tag.isEmpty())
            return getGallery();
        else {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Image temp = new Image();
                    populate(temp, cursor);

                    if (temp.getCaption().toLowerCase().contains(tag.toLowerCase()))
                        filteredPhotos.add(temp);
                } while (cursor.moveToNext());
                cursor.close();
                db.close();
            }
            return filteredPhotos;
        }
    }



    private void populate(Image photo, Cursor cursor) {
        photo.setID(cursor.getInt(0));
        photo.setUri(cursor.getString(1));
        photo.setImgName(cursor.getString(2));
        photo.setCaption(cursor.getString(3));
        photo.setTime(cursor.getString(4));
        photo.setLatitude(cursor.getDouble(5));
        photo.setLongitude(cursor.getDouble(6));
    }



}

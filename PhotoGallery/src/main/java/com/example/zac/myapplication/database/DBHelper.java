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
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Image.db";
    public static final String DATABASE_ID = "id";
    public static final String TABLE_NAME = "ImageTable";
    public static final String[] COLUMN_STRING = {"id", "uri", "imgName", "caption", "time"};
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
                "caption TEXT," +
                "description TEXT," +
                "date TEXT" +
                ")";
        db.execSQL(CREATE_TABLE);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS image");
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
    public void insertPhoto(Image i) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uri", i.getUri());
        values.put("imgName", i.getImgName());
        values.put("caption", i.getCaption());
        values.put("time", i.getTimeStamp());
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
        img.setID(cursor.getInt(0));
        img.setUri(cursor.getString(1));
        img.setImgName(cursor.getString(2));
        img.setCaption(cursor.getString(3));
        img.setTime(cursor.getString(4));

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


    public ArrayList<Image> filterTimestamp(String dateTime) {
        ArrayList<Image> filteredPhotos = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY TIME ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Image temp = new Image();
                populate(temp, cursor);

                if (temp.getTimeStamp().equals(dateTime)) {
                    filteredPhotos.add(temp);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return filteredPhotos;
    }


    private void populate(Image photo, Cursor cursor) {
        photo.setID(cursor.getInt(0));
        photo.setUri(cursor.getString(1));
        photo.setImgName(cursor.getString(2));
        photo.setCaption(cursor.getString(3));
        photo.setTime(cursor.getString(4));
    }



}

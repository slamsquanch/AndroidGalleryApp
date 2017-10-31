package com.example.zac.myapplication.classes;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Zac Koop on 2017-10-28.
 */

public class CreateList {
    private Bitmap imgBitmap;
    private String name;
    private int id;



    public void setImgName(String imgName) {
        this.name = imgName;
    }

    public String getImgName() {
        return name;
    }

    public void setImgID(int ID) {
        this.id = ID;
    }

    public int getImgID() {
        return id;
    }

    public void setImgBitmap(Bitmap bitmap) {
        this.imgBitmap = bitmap;
    }

    public Bitmap getImgBitmap() {
        return this.imgBitmap;
    }


}
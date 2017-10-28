package com.example.zac.myapplication.activities;

import com.example.zac.myapplication.classes.Image;

import java.util.ArrayList;

/**
 * Created by Zac on 2017-10-09.
 */

public class Album {
    ArrayList<Image> myAlbum;
    String albumName;

    public Album(String name, ArrayList<Image> album) {
        this.albumName = name;
        this.myAlbum = album;
    }

}

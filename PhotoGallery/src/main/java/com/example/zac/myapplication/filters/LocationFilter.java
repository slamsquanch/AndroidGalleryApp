package com.example.zac.myapplication.filters;

import android.location.Location;

import com.example.zac.myapplication.classes.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zac on 2017-10-09.
 */

public class LocationFilter extends Filter {

    @Override
    public List<Image> filterImages(String keyword, ArrayList<Image> gallery) {
        for (int i = 0; i < gallery.size(); i++ ) {

        }
        return null;
    }

    public boolean checkLocationRadius(double lat1, double lng1, Location location2) {
        double lat2 = location2.getLatitude();
        double lng2 = location2.getLongitude();

        // lat1 and lng1 are the values of a previously stored location
        //if (distance(lat1, lng1, lat2, lng2) < 0.1) { // if distance < 0.1 miles we take locations as equal
            //do what you want to do...
        return true;
    }
}

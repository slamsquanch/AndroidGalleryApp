package com.example.zac.myapplication.filters;

import com.example.zac.myapplication.classes.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zac on 2017-10-09.
 */

public class TimeFilter extends Filter {

    @Override
    public List<Image> filterImages(String keyword, ArrayList<Image> gallery) {
        for (int i = 0; i < gallery.size(); i++ ) {

        }
        return null;
    }

    public boolean compareDateTime() {

        return true;
    }
}

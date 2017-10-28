package com.example.zac.myapplication.filters;

import com.example.zac.myapplication.classes.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zac on 2017-10-09.
 */

public class CaptionFilter extends Filter {

    @Override
    public List<Image> filterImages(String keyword, ArrayList<Image> gallery) {
        for (int i = 0; i < gallery.size(); i++ ) {
            for (int j = 0; j < gallery.get(i).getCaption().length(); j++) {

            }
        }
        return null;
    }
}

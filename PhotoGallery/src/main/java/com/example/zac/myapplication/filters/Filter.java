package com.example.zac.myapplication.filters;
import com.example.zac.myapplication.classes.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zac on 2017-10-09.
 */

public abstract class Filter {

    public abstract List<Image> filterImages(String search, ArrayList<Image> gallery);
}

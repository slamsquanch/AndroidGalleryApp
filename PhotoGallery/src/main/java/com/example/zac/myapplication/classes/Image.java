package com.example.zac.myapplication.classes;
import android.location.Location;

/**
 * Created by Zac on 2017-10-09.
 */

public class Image {
    private String uri;
    private int id;
    private String imgName;
    private String caption;
    private String timeStamp;
    private String location;
    private static int count = 0;

    // constructor retrieving ALL information about a photo.
    public Image(String URI, String name, String caption, String time, String location) {
        this.uri = URI;
        this.imgName = name;
        this.caption = caption;
        this.timeStamp = time;
        this.location = location;
        this.count++;
    }

    // constructor retrieving ALL information about a photo.
    public Image(String URI, String name, String caption, String time) {
        this.uri = URI;
        this.imgName = name;
        this.caption = caption;
        this.timeStamp = time;
    }

    // alternative constructor taking only a name and URI of an image.
    public Image(String name, String uri) {
        this.imgName = name;
        this.uri = uri;
        this.count++;
    }

    public Image(){
        this.count++;
    }



    // SETTERS
    public void setID(int i) {
        this.id=i;
    }

    public void setUri(String u)  {
        this.uri = u;
    }

    public void setImgName(String n) {
        this.imgName = n;
    }

    public void setCaption(String c) {
        this.caption = c;
    }

    public void setTime(String t) {
        this.timeStamp = t;
    }

    public void setLocationString(String loc) {
        this.location = loc;
    }

    public void setLocationGPS(String loc) {
        Location.convert(loc);
    }



    //GETTERS
    public int getID() { return this.id; }

    public String getUri() {
        return this.uri;
    }

    public String getImgName() {
        return this.imgName;
    }

    public String getCaption() {
        return this.caption;
    }

    public String getTimeStamp() { return this.timeStamp; }

    public String getLocation() { return this.location; }

    public static int getCount() {
        return count;
    }
}

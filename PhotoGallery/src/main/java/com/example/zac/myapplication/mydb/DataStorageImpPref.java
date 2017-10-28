package com.example.zac.myapplication.mydb;

import android.content.SharedPreferences;
import android.widget.Toast;
import android.content.Context;

/**
 * Created by Zac on 2017-09-19.
 */

public class DataStorageImpPref implements IDataStore
{
    Context myContext;
    static String STRING_THING;

    public DataStorageImpPref(Context context){
        myContext = context;
    }

    public void saveState(String key, String content)
    {
        SharedPreferences settings = myContext.getSharedPreferences("ProgramSettings", 0);

        SharedPreferences.Editor editor = settings.edit();

        editor.putString(key, content);

        editor.commit();
    }


    public String getState(String key)
    {

        SharedPreferences settings = myContext.getSharedPreferences("ProgramSettings", 0);
        return settings.getString(key, STRING_THING);
    }
}
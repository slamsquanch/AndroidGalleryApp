package com.example.zac.myapplication.mydb;

/**
 * Created by Zac on 2017-09-19.
 */

public class DataStorageImp implements IDataStore
{
    String state = null;
    public void saveState(String content)
    {
        this.state = content;
    }
    public String getState()
    {
        return state;
    }
}
package com.example.zac.myapplication.mydb;

/**
 * Created by Zac on 2017-09-19.
 */

public interface IDataStore {
    String getState(String key);
    void saveState(String key, String state);
}

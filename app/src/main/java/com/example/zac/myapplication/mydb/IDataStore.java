package com.example.zac.myapplication.mydb;

/**
 * Created by Zac on 2017-09-19.
 */

public interface IDataStore {
    String getState();
    void saveState(String state);
}

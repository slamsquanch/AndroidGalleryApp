package com.example.zac.myapplication;

import com.example.zac.myapplication.mydb.DataStorageImp;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void dataStorage_isWorking() throws Exception {
        DataStorageImp db = new DataStorageImp();
        //db.saveState("Testing");
        //String temp = db.getState();
        //assertEquals("Testing", db.getState());
    }


}
package com.example.zac.myapplication;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.zac.myapplication.activities.GalleryGrid;
import com.example.zac.myapplication.activities.StartupActivity;
import com.example.zac.myapplication.database.DBHelper;
import com.example.zac.myapplication.classes.Image;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Zac on 2017-09-12.
 */



@RunWith(AndroidJUnit4.class)
public class MyApplicationEspressoTest {

    @Rule
    public ActivityTestRule<StartupActivity> mActivityRule1 =
            new ActivityTestRule<>(StartupActivity.class);


    DBHelper dbHelper;
    int gallerySize = 12;


    private void populateDB() {
        for (int i = 0; i < gallerySize; i++) {
            Image img = new Image(i, "uri" + i, "img_" + i, "tag", "10-31-2017", 49.2827, 123.1207);
            dbHelper.insertPhoto(img);
        }
    }

    private void emptyDB() {
        ArrayList<Image> images = dbHelper.getGallery();
        for (int i = 0; i < images.size(); i++) {
            dbHelper.deletePhoto("" + images.get(i).getID());
        }
    }

    /*@Before
    public void before() {
        dbHelper = DBHelper.getInstance(InstrumentationRegistry.getTargetContext());
        emptyDB();
        populateDB();
    }

    @After
    public void after() {
        emptyDB();
        dbHelper.close();
    }*/

    @Test
    public void testFilterMenuCaption() throws InterruptedException {
        onView(withId(R.id.view_gallery)).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        //Thread.sleep(5000);
        onView(withText("Start Date")).perform(click());
        //onView(withId(R.id.gallery)).check(new RecyclerViewItemCountAssertion(12));
    }


}

/*class RecyclerViewItemCountAssertion implements ViewAssertion {

    private final Matcher<Integer> matcher;

    public RecyclerViewItemCountAssertion(int expectedCount) {
        this.matcher = is(expectedCount);
    }

    public RecyclerViewItemCountAssertion(Matcher<Integer> matcher) {
        this.matcher = matcher;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertThat(adapter.getItemCount(), matcher);
    }
}*/

package com.example.myfirstapp;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.Before;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import com.example.myfirstapp.model.DBHelper;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventModel;
import com.example.myfirstapp.model.EventModelImpl;

@RunWith(AndroidJUnit4.class)
public class ModelTest {

    DBHelper dbHelper;
    EventModel eventModel;

    public static final int EVENT_ID = 0;
    public static final String EVENT_TITLE = "event1";
    public static final String EVENT_DESCRIPTION = "event_description";
    public static final int EVENT_ORGANIZER_ID = 1;
    public static final int EVENT_COUNT = 2;
    public static final String EVENT_TIME = "20:00";
    public static final String EVENT_DATE = "21.02.2022";
    public static final String EVENT_LOCATION = "Germany";
    public static final String EVENT_CREATED_AT = "now";
    public static final String EVENT_UPDATED_AT = "now";

    public static final String EVENT_NEW_TITLE = "new_title";

    @Before
    public void setUpEventModel() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        dbHelper = new DBHelper(appContext);
        eventModel = new EventModelImpl(dbHelper);
    }


    @Test
    public void insertEventTestIsSuccessful() {
        Event testEvent = new Event(EVENT_ID, EVENT_ORGANIZER_ID, EVENT_TITLE,
                EVENT_COUNT, 0);
        assertThat(eventModel.addEvent(testEvent), is(true));
    }

}

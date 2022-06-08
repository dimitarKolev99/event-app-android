package com.example.myfirstapp;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.filters.LargeTest;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.example.myfirstapp.controller.EventController;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventDBAdapter;
import com.example.myfirstapp.model.EventModel;
import com.example.myfirstapp.model.EventModelImpl;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    EventController eventController;
    EventDBAdapter eventDBAdapter;

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


    @Before
    public void createDB() {
        Context context = ApplicationProvider.getApplicationContext();
        eventDBAdapter = EventDBAdapter.getInstance(context);
        eventController = new EventController(new EventModelImpl(eventDBAdapter), context);
    }

    @Test
    public void insertEventTest() {
        Event testEvent = new Event(EVENT_ID, EVENT_ORGANIZER_ID, EVENT_TITLE, EVENT_DESCRIPTION,
                EVENT_COUNT, EVENT_LOCATION, EVENT_DATE, EVENT_TIME, EVENT_CREATED_AT,
                EVENT_UPDATED_AT);

        eventController.onAddButtonClicked(testEvent);
        assertThat(eventController.onViewLoaded().get(0).getTitle(), is(EVENT_TITLE));
    }







    public void setUp() {

    }


}

package com.example.myfirstapp;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.junit.runner.RunWith;

import org.junit.Before;

import java.util.Random;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.example.myfirstapp.controller.EventControllerImpl;
import com.example.myfirstapp.model.DBHelper;
import com.example.myfirstapp.model.Event;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    EventControllerImpl eventControllerImpl;
    DBHelper dbHelper;
    SharedPreferences preferences;

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

    int TEST_USER_PREFERENCES;

    /**
     * get context and set up the DB Helper class
     * delete all events in the db
     * init shared prefs and put a value in it
     * get the value from Shared Preferences back and assign the variable to be tested to it
     */
    @Before
    public void createDB() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DBHelper(context);
        dbHelper.deleteAllEvents();


        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int user_id = new Random().nextInt();
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(String.valueOf(R.string.pref_user_id), user_id);
        edit.commit();

        TEST_USER_PREFERENCES = preferences.getInt(String.valueOf(R.string.pref_user_id), 23);
    }

    /**
     * Testing if insertion returns true (success)
     */
    @Test
    public void insertEventTestIsSuccessful() {
        Event testEvent = new Event(EVENT_ID, EVENT_ORGANIZER_ID, EVENT_TITLE,
                EVENT_DESCRIPTION, EVENT_COUNT, EVENT_LOCATION, EVENT_DATE, EVENT_TIME,
                EVENT_CREATED_AT, EVENT_UPDATED_AT);
        assertThat(dbHelper.insert(testEvent), is(true));
    }

    /**
     * Testing if updating returns true (success)
     * and that the selected event has the correct properties
     */
    @Test
    public void updateEventIsSuccessful() {
        Event testEvent = new Event(EVENT_ID, EVENT_ORGANIZER_ID, EVENT_TITLE,
                EVENT_DESCRIPTION, EVENT_COUNT, EVENT_LOCATION, EVENT_DATE, EVENT_TIME,
                EVENT_CREATED_AT, EVENT_UPDATED_AT);

        Event updatedEvent = new Event(EVENT_ID, EVENT_ORGANIZER_ID, EVENT_NEW_TITLE,
                EVENT_DESCRIPTION, EVENT_COUNT, EVENT_LOCATION, EVENT_DATE, EVENT_TIME,
                EVENT_CREATED_AT, EVENT_UPDATED_AT);

        dbHelper.deleteAllEvents();
        assertThat(dbHelper.insert(testEvent), is(true));
        assertThat(dbHelper.update(updatedEvent), is(true));
        assertThat(dbHelper.getAllEvents().get(0).getTitle(), is(EVENT_NEW_TITLE));
    }

    /**
     * Testing if deleting returns true (success)
     * and that selecting returns an empty list
     */
    @Test
    public void deleteEventIsSuccessful() {
        Event testEvent = new Event(EVENT_ID, EVENT_ORGANIZER_ID, EVENT_TITLE,
                EVENT_DESCRIPTION, EVENT_COUNT, EVENT_LOCATION, EVENT_DATE, EVENT_TIME,
                EVENT_CREATED_AT, EVENT_UPDATED_AT);

        dbHelper.deleteAllEvents();

        dbHelper.insert(testEvent);

        assertThat(dbHelper.delete(testEvent), is(true));
        assertThat(dbHelper.getAllEvents().isEmpty(), is(true));
    }


    /**
     * Testing if inserting and then selecting is correct
     */
    @Test
    public void insertEventIsSuccessWithSELECT() {
        Event testEvent = new Event(EVENT_ID, TEST_USER_PREFERENCES, EVENT_TITLE,
                EVENT_DESCRIPTION, EVENT_COUNT, EVENT_LOCATION, EVENT_DATE, EVENT_TIME,
                EVENT_CREATED_AT, EVENT_UPDATED_AT);
        //Insert the test event
        dbHelper.insert(testEvent);
        //Select
        Event selectedEvent = dbHelper.getAllEvents().get(0);
        //Test if properties are correct
        assertThat(selectedEvent.getId(), is(EVENT_ID));
        assertThat(selectedEvent.getTitle(), is(EVENT_TITLE));
        assertThat(selectedEvent.getOrganizer_id(), is(not(23))); //should not be the def value from SharedPreferences
        assertThat(selectedEvent.getDescription(), is(EVENT_DESCRIPTION));
        assertThat(selectedEvent.getInterested_count(), is(EVENT_COUNT));
        assertThat(selectedEvent.getDate(), is(EVENT_DATE));
        assertThat(selectedEvent.getTime(), is(EVENT_TIME));
        assertThat(selectedEvent.getCreated_at(), is(EVENT_CREATED_AT));
        assertThat(selectedEvent.getLocation(), is(EVENT_LOCATION));
        assertThat(selectedEvent.getUpdated_at(), is(EVENT_UPDATED_AT));
    }


}

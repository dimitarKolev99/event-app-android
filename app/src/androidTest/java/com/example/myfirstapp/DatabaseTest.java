package com.example.myfirstapp;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.filters.LargeTest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import java.util.Random;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.example.myfirstapp.controller.EventController;
import com.example.myfirstapp.model.DBHelper;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventDBAdapter;
import com.example.myfirstapp.model.EventModel;
import com.example.myfirstapp.model.EventModelImpl;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    EventController eventController;
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

    @Test
    public void insertEventTestIsSuccessful() {
        Event testEvent = new Event(EVENT_ID, EVENT_ORGANIZER_ID, EVENT_TITLE,
                EVENT_DESCRIPTION, EVENT_COUNT, EVENT_LOCATION, EVENT_DATE, EVENT_TIME,
                EVENT_CREATED_AT, EVENT_UPDATED_AT);
        assertThat(dbHelper.insert(testEvent), is(true));
    }

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


    @Test
    public void insertEventIsSuccessWithSELECT() {
        Event testEvent = new Event(EVENT_ID, TEST_USER_PREFERENCES, EVENT_TITLE,
                EVENT_DESCRIPTION, EVENT_COUNT, EVENT_LOCATION, EVENT_DATE, EVENT_TIME,
                EVENT_CREATED_AT, EVENT_UPDATED_AT);
        dbHelper.insert(testEvent);
        assertThat(dbHelper.getAllEvents().get(0).getId(), is(EVENT_ID));
        assertThat(dbHelper.getAllEvents().get(0).getOrganizer_id(), is(not(23)));
        assertThat(dbHelper.getAllEvents().get(0).getTitle(), is(EVENT_TITLE));
        assertThat(dbHelper.getAllEvents().get(0).getDescription(), is(EVENT_DESCRIPTION));
        assertThat(dbHelper.getAllEvents().get(0).getInterested_count(), is(EVENT_COUNT));
        assertThat(dbHelper.getAllEvents().get(0).getDate(), is(EVENT_DATE));
        assertThat(dbHelper.getAllEvents().get(0).getTime(), is(EVENT_TIME));
        assertThat(dbHelper.getAllEvents().get(0).getCreated_at(), is(EVENT_CREATED_AT));
        assertThat(dbHelper.getAllEvents().get(0).getLocation(), is(EVENT_LOCATION));
        assertThat(dbHelper.getAllEvents().get(0).getUpdated_at(), is(EVENT_UPDATED_AT));
    }


}

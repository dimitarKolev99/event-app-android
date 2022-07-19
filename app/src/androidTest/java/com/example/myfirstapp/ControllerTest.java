package com.example.myfirstapp;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myfirstapp.controller.EventController;
import com.example.myfirstapp.controller.EventControllerImpl;
import com.example.myfirstapp.model.DBHelper;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventModel;
import com.example.myfirstapp.model.EventModelImpl;

import java.util.ArrayList;
import java.util.List;


public class ControllerTest {
    public static final int EVENT_ID = 0;
    public static final String EVENT_TITLE = "event1";
    public static final int EVENT_ORGANIZER_ID = 1;
    public static final int EVENT_COUNT = 2;
    public static final int EVENT_FAV_STATUS = 1;
    public static final String EVENT_DESCRIPTION = "event_description";
    public static final String EVENT_TIME = "20:00";
    public static final String EVENT_DATE = "21.02.2022";
    public static final String EVENT_LOCATION = "Germany";
    public static final String EVENT_CREATED_AT = "now";
    public static final String EVENT_UPDATED_AT = "now";

    private DBHelper dbHelper;
    private EventModel eventModel;
    private EventController eventController;
    private Event testEvent;
    private List<Event> eventList;

    @Before
    public void setUpController() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        dbHelper = new DBHelper(appContext);
        eventModel = new EventModelImpl(dbHelper);
        eventController = new EventControllerImpl(eventModel, appContext);
        eventList = new ArrayList<>();
        testEvent = new Event(EVENT_ID,
                EVENT_ORGANIZER_ID,
                EVENT_TITLE,
                EVENT_COUNT,
                EVENT_FAV_STATUS);
        eventList.add(testEvent);
    }

    @Test
    public void testGetEvents() {
        assertEquals(eventList, eventController.onViewLoaded());
    }

    @Test
    public void testAddEvent() {
        assertTrue(eventController.onAddButtonClicked(testEvent));
    }

    @Test
    public void testEditEvent() {
        eventController.deleteAllEvents();
        eventController.onAddButtonClicked(testEvent);
        Event event = new Event(EVENT_ID, 2, "TITLE", 2, 3);
        assertTrue(eventController.onEditButtonClicked(event));
    }

    @Test
    public void testRemoveEvent() {
        assertTrue(eventController.onRemoveButtonClicked(testEvent));
    }


}

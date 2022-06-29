package com.example.myfirstapp.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.EditText;

import com.example.myfirstapp.model.Event;

public interface SaveEventHelper {

    /**
     * get values from EditText fields and create an Event object
     * @param bitmap
     * @param editTextTitle
     * @param editTextDescription
     * @param date_picker
     * @param time_picker
     * @param event_location
     * @param context
     * @param prefs
     * @return the Event object
     */
    Event saveEditEvent(Bitmap bitmap, EditText editTextTitle, EditText editTextDescription,
                        EditText date_picker, EditText time_picker, EditText event_location,
                        Context context, SharedPreferences prefs);


    /**
     * get values from EditText fields and create an Event object
     * @param bitmap
     * @param editTextTitle
     * @param editTextDescription
     * @param date_picker
     * @param time_picker
     * @param event_location
     * @param context
     * @param prefs
     * @param id
     * @return the Event object
     */
    Event editEvent(Bitmap bitmap, EditText editTextTitle, EditText editTextDescription,
                    EditText date_picker, EditText time_picker, EditText event_location,
                    Context context, SharedPreferences prefs, int id);
}

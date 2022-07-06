package com.example.myfirstapp.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.model.Event;

import java.util.List;

public interface SaveEventHelper {

    /**
     * get values from EditText fields and create an Event object
     * @param editTextTitle
     * @return the Event object
     */
    Event saveEditEvent(EditText editTextTitle, Bitmap bitmap, Context context);


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

    /**
     * convert the base64 strings from the api to images
     * @param eventList the response from the api
     * @return the updated list of events with paths to the images (local)
     */
    List<Event> processResponse(List<Event> eventList, Context context, EventAdapter eventAdapter, RecyclerView recyclerView);
}

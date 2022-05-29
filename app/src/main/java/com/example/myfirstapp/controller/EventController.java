package com.example.myfirstapp.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventModel;

import java.util.List;

public class EventController {
    EventModel eventModel;
    List<Event> list = null;
    Context context;

    public EventController(EventModel eventModel, Context context) {
        this.eventModel = eventModel;
        this.context = context;
    }

    public List<Event> onViewLoaded() {
        try {
            list = eventModel.getAllEvents();
            if (list == null) {
                Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            showErrorToast(e.getMessage());
        }
        return list;
    }

    public void onAddButtonClicked(Event event) {

            boolean success = eventModel.addEvent(event);
            if (success) {
                list = eventModel.getAllEvents();
            }

        /*
        catch (Exception e) {
//            Log.d("EVENT_CONTROLLER", "fail");
            showErrorToast(e.getMessage());
        }

         */
    }

    public void onRemoveButtonClicked(Event event) {
        try {
            boolean success = eventModel.deleteEvent(event);
            if (success) {
                list = eventModel.getAllEvents();
            }
        } catch (Exception e) {
            showErrorToast(e.getMessage());
        }
    }

    public void onEditButtonClicked(Event event) {
        try {
            boolean success = eventModel.updateEvent(event);
            if (success) {
                list = eventModel.getAllEvents();
            }
        } catch (Exception e) {
            showErrorToast(e.getMessage());
        }
    }

    public void showErrorToast(String errorMessage) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
    }
}

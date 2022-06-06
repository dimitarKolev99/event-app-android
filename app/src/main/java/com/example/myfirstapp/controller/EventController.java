package com.example.myfirstapp.controller;

import android.content.Context;
import android.os.AsyncTask;
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

    public List<Event> getList() {
        return list;
    }

    public List<Event> getUserEvents(int id) {
        return eventModel.getUserEvents(id);
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

    public boolean onAddButtonClicked(Event event) {
        //new InsertEventAsyncTask(eventModel).execute(event);
        boolean success = false;
        try {

            success = eventModel.addEvent(event);
            if (success) {
                list = eventModel.getAllEvents();
            }
        }
        catch (Exception e) {
            showErrorToast(e.getMessage());
        }


        return success;

    }

    public boolean onRemoveButtonClicked(Event event) {
        boolean success = false;
        try {
            success = eventModel.deleteEvent(event);
            if (success) {
                list = eventModel.getAllEvents();
            }
        } catch (Exception e) {
            showErrorToast(e.getMessage());
        }
        return success;
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
//        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
        Log.d("EVENT_CONTROLLER", "ERROR in CONTROLLER");
    }

    /*
    private static class InsertEventAsyncTask extends AsyncTask<Event, Void, List<Event>> {
        private EventModel eventModel;

        private InsertEventAsyncTask(EventModel eventModel) {
            this.eventModel = eventModel;
        }

        @Override
        protected List<Event> doInBackground(Event... events) {
            eventModel.addEvent(events[0]);
            return eventModel.getAllEvents();
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);

        }
    }

     */
}

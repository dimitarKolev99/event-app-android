package com.example.myfirstapp.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventModel;
import com.example.myfirstapp.utils.internal_storage_helper.InternalStorageHelper;
import com.example.myfirstapp.utils.internal_storage_helper.InternalStorageHelperImpl;

import java.util.ArrayList;
import java.util.List;

public class EventControllerImpl implements EventController{
    EventModel eventModel;
    List<Event> list = null;
    Context context;
    private UpdateEventsListItemsListener updateEventsListItemsListener;
    private SetRefreshingListener setRefreshingListener;
    private EventAdapter eventAdapter;
    private RecyclerView recyclerView;

    public EventControllerImpl(EventModel eventModel, Context context, EventAdapter eventAdapter,
                               RecyclerView recyclerView) {
        this.eventModel = eventModel;
        this.context = context;
        this.eventAdapter = eventAdapter;
        this.recyclerView = recyclerView;
    }

    public EventControllerImpl(EventModel eventModel, Context context) {
        this.eventModel = eventModel;
        this.context = context;
    }

    public List<Event> getList() {
        return list;
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
            if (eventModel == null) {
                Log.d("EVENT_CONTROLLER", "NULL");
            }
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

    public boolean onEditButtonClicked(Event event) {
        boolean success = false;
        try {
            success = eventModel.updateEvent(event);
            if (success) {
                list = eventModel.getAllEvents();
            }
        } catch (Exception e) {
            showErrorToast(e.getMessage());
        }
        return success;
    }

    @Override
    public void insertMany(List<Event> eventList) {
        eventModel.insertMany(eventList);
    }

    @Override
    public void deleteAllEvents() {
        eventModel.deleteAllEvents();
    }

    public void showErrorToast(String errorMessage) {
//        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
        Log.d("EVENT_CONTROLLER", errorMessage);
    }




    public void setUpdateEventsListItemsListener(UpdateEventsListItemsListener updateEventsListItemsListener) {
        this.updateEventsListItemsListener = updateEventsListItemsListener;
    }

    public void setSetRefreshingListener(SetRefreshingListener setRefreshingListener) {
        this.setRefreshingListener = setRefreshingListener;
    }
}

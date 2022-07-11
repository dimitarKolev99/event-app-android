package com.example.myfirstapp.model;

import android.util.Log;

import java.util.List;

public class EventModelImpl implements EventModel{
    DBHelper dbHelper;

    List<Event> eventList;

    public EventModelImpl(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        eventList = this.dbHelper.getAllEvents();
    }

    private void refresh() {
        eventList.clear();
        eventList = this.dbHelper.getAllEvents();
    }

    @Override
    public boolean addEvent(Event event) {
        boolean addSuccess = dbHelper.insert(event);
        if (addSuccess) {
            refresh();
        }
        return addSuccess;
    }

    @Override
    public boolean updateEvent(Event event) {
        boolean updateSuccess = dbHelper.update(event);
        if (updateSuccess) {
            refresh();
        }
        return updateSuccess;
    }

    @Override
    public boolean deleteEvent(Event event) {
        boolean deleteSuccess = dbHelper.delete(event);
        if (deleteSuccess) {
            refresh();
        }
        return deleteSuccess;
    }

    @Override
    public List<Event> getAllEvents() {
        if (this.eventList != null && this.eventList.size() > 0) {
            return this.eventList;
        }
        return null;
    }

    @Override
    public void insertMany(List<Event> eventList) {
        dbHelper.insertMany(eventList);
    }

    @Override
    public void deleteAllEvents() {
        dbHelper.deleteAllEvents();
    }



    /*
    @Override
    public List<Event> getUserEvents(int id) {
        return this.dbHelper.getUserEvents(String.valueOf(id));
    }

     */
}

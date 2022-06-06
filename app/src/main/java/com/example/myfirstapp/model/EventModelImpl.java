package com.example.myfirstapp.model;

import android.util.Log;

import java.util.List;

public class EventModelImpl implements EventModel{
    EventDBAdapter eventDBAdapter;

    List<Event> eventList;

    public EventModelImpl(EventDBAdapter eventDBAdapter) {
        this.eventDBAdapter = eventDBAdapter;
        eventList = this.eventDBAdapter.getAllEvents();
    }

    private void refresh() {
        eventList.clear();
        eventList = this.eventDBAdapter.getAllEvents();
    }

    @Override
    public boolean addEvent(Event event) {
        boolean addSuccess = eventDBAdapter.insert(event);
        if (addSuccess) {
            refresh();
        }
        return addSuccess;
    }

    @Override
    public boolean updateEvent(Event event) {
        boolean updateSuccess = eventDBAdapter.update(event);
        if (updateSuccess) {
            refresh();
        }
        return updateSuccess;
    }

    @Override
    public boolean deleteEvent(Event event) {
        boolean deleteSuccess = eventDBAdapter.delete(event);
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
    public List<Event> getUserEvents(int id) {
        return this.eventDBAdapter.getUserEvents(String.valueOf(id));
    }
}

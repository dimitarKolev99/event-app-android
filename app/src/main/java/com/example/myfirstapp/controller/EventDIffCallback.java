package com.example.myfirstapp.controller;

import androidx.recyclerview.widget.DiffUtil;

import com.example.myfirstapp.model.Event;

import java.util.List;

public class EventDIffCallback extends DiffUtil.Callback {
    private final List<Event> mOldEventsList;
    private final List<Event> mNewEventsList;


    public EventDIffCallback(List<Event> oldEventsList, List<Event> newEventsList) {
        this.mOldEventsList = oldEventsList;
        this.mNewEventsList = newEventsList;
    }

    @Override
    public int getOldListSize() {
        return mOldEventsList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewEventsList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldEventsList.get(oldItemPosition).getId() == mNewEventsList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Event oldEvent = mOldEventsList.get(oldItemPosition);
        final Event newEvent = mNewEventsList.get(newItemPosition);

        return oldEvent.getTitle().equals(newEvent.getTitle());
    }
}
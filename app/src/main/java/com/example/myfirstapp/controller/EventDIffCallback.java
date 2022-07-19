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
        if (mNewEventsList != null) {
            return mNewEventsList.size();

        }
        return 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldEventsList.get(oldItemPosition).getTitle() == mNewEventsList.get(newItemPosition).getTitle();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Event oldEvent = mOldEventsList.get(oldItemPosition);
        final Event newEvent = mNewEventsList.get(newItemPosition);

        return oldEvent.getTitle().equals(newEvent.getTitle());
    }
}

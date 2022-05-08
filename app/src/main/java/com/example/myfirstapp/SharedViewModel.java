package com.example.myfirstapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myfirstapp.model.Event;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<List<Event>> eventMutableLiveData = new MutableLiveData<>();

    public void setEvents(List<Event> events) {
        eventMutableLiveData.setValue(events);
    }

    public LiveData<List<Event>> getEvents() {
        return eventMutableLiveData;
    }
}

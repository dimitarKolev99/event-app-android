package com.example.myfirstapp.controller;

import com.example.myfirstapp.model.Event;

import java.util.List;

public interface MainActivityView extends View {

    public void showAllEvents(List<Event> events);
    public void updateViewOnAdd(List<Event> events);
    public void updateViewOnUpdate(List<Event> events);
    public void updateViewOnDelete(List<Event> events);

    public void showErrorToast(String errorMessage);

}

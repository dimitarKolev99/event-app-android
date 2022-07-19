package com.example.myfirstapp.controller;

import com.example.myfirstapp.model.Event;

import java.util.List;

public interface EventController {

    /**
     * call the model and get all events
     * @return a list of events
     */
    List<Event> onViewLoaded();

    /**
     * call the model and add the event
     * @param event
     * @return if the operation was successful or not
     */
    boolean onAddButtonClicked(Event event);

    /**
     * call the model and remove the event
     * @param event
     * @return if the operation was successful or not
     */
    boolean onRemoveButtonClicked(Event event);

    /**
     * call the model and edit the event
     * @param event
     */
    boolean onEditButtonClicked(Event event);

    /**
     * insert the response from the api to the local sqlite db
     * @param eventList the response from the api
     */
    void insertMany(List<Event> eventList);

    /**
     * delete all from table events in local db
     */
    void deleteAllEvents();


    void setUpdateEventsListItemsListener(UpdateEventsListItemsListener updateEventsListItemsListener);

    void setSetRefreshingListener(SetRefreshingListener setRefreshingListener);
}

package com.example.myfirstapp.model;

import java.util.List;

public interface EventModel {

    /**
     * insert an event object into the events table
     * @param event
     * @return if insertion was successful or not
     */
    public boolean addEvent(Event event);

    /**
     * update an event in the events table on id of
     * the given Event object
     * @param event
     * @return if updating was successful or not
     */
    public boolean updateEvent(Event event);


    /**
     * delete an event in the events table on id
     * of the givent Event object
     * @param event
     * @return if deletion was successful or not
     */
    public boolean deleteEvent(Event event);

    /**
     * get a list of all events in the
     * events table
     * @return List of Event object
     */
    public List<Event> getAllEvents();

    /**
     * get events of the user
     * @param id the user id
     * @return
     */
    public List<Event> getUserEvents(int id);
}

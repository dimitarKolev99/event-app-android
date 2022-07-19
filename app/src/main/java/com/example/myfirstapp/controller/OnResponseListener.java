package com.example.myfirstapp.controller;

import com.example.myfirstapp.model.Event;

import java.util.List;

public interface OnResponseListener {
    /**
     * listener for network requests
     * @param statusCode the response code of the request
     * @param eventList the response body
     */
    void onResponse(int statusCode, List<Event> eventList);
}

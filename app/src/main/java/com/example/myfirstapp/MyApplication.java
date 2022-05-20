package com.example.myfirstapp;

import android.app.Application;

import com.example.myfirstapp.model.EventDBAdapter;

public class MyApplication extends Application {

    static EventDBAdapter eventDBAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        eventDBAdapter = EventDBAdapter.getInstance(this);
    }

    public static EventDBAdapter getEventDBAdapter() {
        return eventDBAdapter;
    }
}

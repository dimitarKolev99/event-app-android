package com.example.myfirstapp.model;

import android.graphics.Bitmap;

import com.example.myfirstapp.utils.BitmapToByteArrayHelper;

public class Event {
    String title;

    public Event(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

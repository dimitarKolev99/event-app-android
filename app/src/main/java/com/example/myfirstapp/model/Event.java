package com.example.myfirstapp.model;

import android.graphics.Bitmap;

import com.example.myfirstapp.utils.BitmapToByteArrayHelper;

public class Event {
    String title;
    String imagePath;
    String imageName;

    public Event(String title) {
        this.title = title;
    }

    public Event(String title, String imagePath, String imageName) {
        this.title = title;
        this.imagePath = imagePath;
        this.imageName = imageName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.title = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}

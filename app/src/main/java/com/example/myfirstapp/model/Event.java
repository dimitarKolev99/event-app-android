package com.example.myfirstapp.model;

import android.graphics.Bitmap;

import com.example.myfirstapp.utils.BitmapToByteArrayHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

public class Event {
    int eventID;
    int organizerID;
    String title;
    String imagePath;
    String imageURI;
    String imageName;
    int interestedCount;
    int favStatus;
    String base64Img;

    public Event(String title) {
        this.title = title;
    }

    public Event(int eventID, int organizerID, String title, String imagePath, String imageName, int interestedCount,
                 int favStatus) {
        this.eventID = eventID;
        this.organizerID = organizerID;
        this.title = title;
        this.imagePath = imagePath;
        this.imageName = imageName;
        this.interestedCount = interestedCount;
        this.favStatus = favStatus;
        this.imageURI = imagePath+"/img"+imageName+".jpg";
    }

    public int getEventID() {
        return eventID;
    }

    public int getOrganizerID() {
        return organizerID;
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

    public int getInterestedCount() {
        return interestedCount;
    }

    public void setInterestedCount(int interestedCount) {
        this.interestedCount = interestedCount;
    }

    public int getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(int favStatus) {
        this.favStatus = favStatus;
    }

    public String getBase64Img() {
        return base64Img;
    }

    public void setBase64Img(String base64Img) {
        this.base64Img = base64Img;
    }

    public String getGson(Event event) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(event);
    }
}

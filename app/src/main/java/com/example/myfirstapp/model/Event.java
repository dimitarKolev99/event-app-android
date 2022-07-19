package com.example.myfirstapp.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Event {
    private int eventid;
    private int organizerid;
    private String title;
    private String imagepath;
    private String imageURI;
    private String imageName;
    private int interestedcount;
    private int favstatus;
    private String base64Img;

    public Event(String title) {
        this.title = title;
    }

    public Event(int eventid, int organizerid, String title, int interestedcount,
                 int favstatus) {
        this.eventid = eventid;
        this.organizerid = organizerid;
        this.title = title;
        this.interestedcount = interestedcount;
        this.favstatus = favstatus;
    }

    public Event(int eventid, int organizerid, String title, String imagepath, String imageName, int interestedcount,
                 int favstatus) {
        this.eventid = eventid;
        this.organizerid = organizerid;
        this.title = title;
        this.imagepath = imagepath;
        this.imageName = imageName;
        this.interestedcount = interestedcount;
        this.favstatus = favstatus;
        this.imageURI = imagepath +"/img"+imageName+".jpg";
    }

    public int getEventid() {
        return eventid;
    }

    public int getOrganizerid() {
        return organizerid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getInterestedcount() {
        return interestedcount;
    }

    public void setInterestedcount(int interestedcount) {
        this.interestedcount = interestedcount;
    }

    public int getFavstatus() {
        return favstatus;
    }

    public void setFavstatus(int favstatus) {
        this.favstatus = favstatus;
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

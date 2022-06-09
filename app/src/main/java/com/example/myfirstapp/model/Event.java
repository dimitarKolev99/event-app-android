package com.example.myfirstapp.model;

import android.graphics.Bitmap;

import com.example.myfirstapp.utils.BitmapToByteArrayHelper;

public class Event {
    int id;
    int organizer_id;
    String title;
    String description;
    String image;
    String imgName;
    int interested_count;
    String location;
    String date;
    String time;
    String created_at;
    String updated_at;
    BitmapToByteArrayHelper bitmapToByteArrayHelper = new BitmapToByteArrayHelper();

    public Event(int id, int organizer_id, String title, String description,
                 String image, String imgName,
                 int interested_count, String location, String date, String time, String created_at,
                 String updated_at)
    {
        this.id = id;
        this.organizer_id = organizer_id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.imgName = imgName;
        this.interested_count = interested_count;
        this.location = location;
        this.date = date;
        this.time = time;
        this.created_at = created_at;
        this.updated_at = updated_at;

    }

    public Event(int id, int organizer_id, String title, String description,
                 int interested_count, String location, String date, String time, String created_at,
                 String updated_at)
    {
        this.id = id;
        this.organizer_id = organizer_id;
        this.title = title;
        this.description = description;
        this.interested_count = interested_count;
        this.location = location;
        this.date = date;
        this.time = time;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Event(String title) {
       this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrganizer_id() {
        return organizer_id;
    }

    public void setOrganizer_id(int organizer_id) {
        this.organizer_id = organizer_id;
    }

    public void setDate(String date) { this.date = date; }

    public String getDate() { return date; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public byte[] getImage() {
//        return bitmapToByteArrayHelper.getByteArrayFromBitmap(image);
//    }

    public String getImageUri() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
//    public void setImage(Bitmap image) {
//        this.image = image;
//    }

    public int getInterested_count() { return interested_count; }

    public void setInterested_count(int interested_count) { this.interested_count = interested_count; }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}

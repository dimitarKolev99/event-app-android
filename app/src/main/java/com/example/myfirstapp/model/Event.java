package com.example.myfirstapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

import java.sql.Blob;

@Entity(tableName = "Event")
public class Event {

    @PrimaryKey(autoGenerate = true)

    private int id;

    private String title;

    private String description;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    private int interested_count;

    @Ignore
    public Event(String title, String description, byte[] image, int interested_count) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.interested_count = interested_count;
    }


    public Event(String title, String description, int interested_count) {
        this.title = title;
        this.description = description;
        this.interested_count = interested_count;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setImage(byte[] image) {
      this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public int getInterested_count() {
        return interested_count;
    }
}

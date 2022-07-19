package com.example.myfirstapp.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.model.Event;

import java.util.List;

public interface SaveEventHelper {


    /**
     * call SaveImageAsyncTask and InsertDeleteAsyncTask
     * convert the base64 strings from the api to images
     * @param eventList the response from the api
     */
    void processNetworkResponse(List<Event> eventList);
}

package com.example.myfirstapp.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfirstapp.AddEditEventActivity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.internal_storage_helper.InternalStorageHelper;
import com.example.myfirstapp.internal_storage_helper.InternalStorageHelperImpl;
import com.example.myfirstapp.model.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SaveEventHelperImpl implements SaveEventHelper{

    InternalStorageHelper internalStorageHelper = new InternalStorageHelperImpl();

    int flag = 0;

    public Event saveEditEvent(EditText editTextTitle) {
        String title = editTextTitle.getText().toString();

        return new Event(title);

    }

    public Event editEvent(Bitmap bitmap, EditText editTextTitle, EditText editTextDescription,
                           EditText date_picker, EditText time_picker, EditText event_location,
                           Context context, SharedPreferences prefs, int id) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String date = date_picker.getText().toString();

        String time = time_picker.getText().toString();
        String location = event_location.getText().toString();

        if (title.trim().isEmpty() || description.trim().isEmpty() ||
                date.trim().isEmpty() || time.trim().isEmpty() || location.trim().isEmpty()) {
            Toast.makeText(context, "Please insert all necessary information", Toast.LENGTH_SHORT).show();
            flag = -1;
        }

        Random random = new Random();
        int rand_id = random.nextInt();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date datee = new Date(System.currentTimeMillis());

        String imgName = "img" + String.valueOf(rand_id) + ".jpg";

        String path =
                internalStorageHelper.saveToInternalStorage(bitmap, rand_id, context);
        //I/O OPERATION -> SLOW

        return new Event(title);

    }


}

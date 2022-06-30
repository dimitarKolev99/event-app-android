package com.example.myfirstapp.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfirstapp.AddEditEventActivity;
import com.example.myfirstapp.MainActivity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.internal_storage_helper.InternalStorageHelper;
import com.example.myfirstapp.internal_storage_helper.InternalStorageHelperImpl;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class SaveEventHelperImpl implements SaveEventHelper{

    InternalStorageHelper internalStorageHelper = new InternalStorageHelperImpl();

    EventModel eventModel;
    EventController eventController;

    private String title;
    private int id;

    int flag = 0;


    public SaveEventHelperImpl(EventModel eventModel, EventController eventController) {
        this.eventModel = eventModel;
        this.eventController = eventController;
    }

    public Event saveEditEvent(EditText editTextTitle, Bitmap bitmap, Context context) {
        String titleField = editTextTitle.getText().toString();
        title = titleField;
        int id1 = new Random().nextInt();

        id = id1;

//        String path = internalStorageHelper.saveToInternalStorage(bitmap, id, context);

        new LoadImgAsyncTask(bitmap, id, context).execute();

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

    private class LoadImgAsyncTask extends AsyncTask<Void, Void, String> {
        private Bitmap bitmap;
        private int rand_id;
        private Context context;

        private LoadImgAsyncTask(Bitmap bitmap, int rand_id, Context context) {
            this.bitmap = bitmap;
            this.rand_id = rand_id;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return internalStorageHelper.saveToInternalStorage(bitmap, rand_id, context);
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);

            if (path != null) {
//                eventController.onAddButtonClicked(new Event(title, path, String.valueOf(id)));
            }
        }
    }


}

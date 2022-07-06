package com.example.myfirstapp.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.example.myfirstapp.async_tasks.LoadImgAsyncTask;
import com.example.myfirstapp.internal_storage_helper.InternalStorageHelper;
import com.example.myfirstapp.internal_storage_helper.InternalStorageHelperImpl;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventModel;
import com.example.myfirstapp.network.PostEventRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class SaveEventHelperImpl implements SaveEventHelper{

    InternalStorageHelperImpl internalStorageHelper = new InternalStorageHelperImpl();

    EventModel eventModel;
    EventController eventController;
    SaveImgAsyncTask saveImgAsyncTask;

    private String title;
    private int id;

    int flag = 0;


    public SaveEventHelperImpl(EventModel eventModel,
                               EventController eventController) {
        this.eventModel = eventModel;
        this.eventController = eventController;
    }

    public Event saveEditEvent(EditText editTextTitle, Bitmap bitmap, Context context) {
        String titleField = editTextTitle.getText().toString();
        title = titleField;
        int id1 = new Random().nextInt();

        id = id1;

//        String path = internalStorageHelper.saveToInternalStorage(bitmap, id, context);

        new LoadImgAsyncTask(bitmap, id, context, internalStorageHelper).execute();

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

    @Override
    public List<Event> processResponse(List<Event> eventList, Context context, EventAdapter eventAdapter, RecyclerView recyclerView) {
        new DeleteImagesAsyncTask(context).execute(eventList);
        new SaveImgAsyncTask(context, internalStorageHelper, eventAdapter, recyclerView).execute(eventList);
        return null;
    }

    private class DeleteImagesAsyncTask extends AsyncTask<List<Event>, Void, Void> {
        private Context context;

        public DeleteImagesAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(List<Event>... eventList) {

            for (Event event: eventList[0]) {
                context.deleteFile("img" + event.getTitle() + ".jpg");
            }
            Log.d("DELETE", String.valueOf(eventList.length));
            return null;
        }
    }


    private class SaveImgAsyncTask extends AsyncTask<List<Event>, Void, List<Event>>{

        private Context context;
        private InternalStorageHelper internalStorageHelper;
        private EventAdapter eventAdapter;
        private RecyclerView recyclerView;


        private SaveImgAsyncTask(Context context,
                                 InternalStorageHelperImpl internalStorageHelper,
                                 EventAdapter eventAdapter,
                                 RecyclerView recyclerView) {
            this.context = context;
            this.internalStorageHelper = internalStorageHelper;
            this.eventAdapter = eventAdapter;
            this.recyclerView = recyclerView;
        }

        @Override
        protected List<Event> doInBackground(List<Event>... lists) {

            List<Event> newList = new ArrayList<>();

            for (Event e: lists[0]) {
                e.setImagePath(internalStorageHelper.saveToInternalStorageFromBase64(e.getBase64Img(), e.getEventID(), context));
                newList.add(e);
            }
            return newList;
        }

        @Override
        protected void onPostExecute(List<Event> newList) {
            super.onPostExecute(newList);
                eventAdapter.updateEventsListItems(newList);
                recyclerView.setAdapter(eventAdapter);
                //update adapter here
//            eventController.deleteAll();
//                eventController.addMany(newList);
            }
        }
    }



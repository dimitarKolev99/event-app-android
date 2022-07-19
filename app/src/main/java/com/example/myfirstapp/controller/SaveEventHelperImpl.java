package com.example.myfirstapp.controller;

import android.content.Context;
import android.os.AsyncTask;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.myfirstapp.MainActivity;
import com.example.myfirstapp.utils.internal_storage_helper.InternalStorageHelper;
import com.example.myfirstapp.utils.internal_storage_helper.InternalStorageHelperImpl;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventModel;

import java.util.ArrayList;
import java.util.List;

public class SaveEventHelperImpl implements SaveEventHelper{


    Context context;
    EventController eventController;
    SwipeRefreshLayout mSwipeRefreshLayout;
    EventAdapter eventAdapter;
    RecyclerView recyclerView;

    public SaveEventHelperImpl(Context context,
                               EventController eventController,
                               SwipeRefreshLayout mSwipeRefreshLayout,
                               EventAdapter eventAdapter,
                               RecyclerView recyclerView) {
        this.context = context;
        this.eventController = eventController;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.eventAdapter = eventAdapter;
        this.recyclerView = recyclerView;
    }

    public SaveEventHelperImpl(EventController eventController) {
        this.eventController = eventController;
    }


    public void processNetworkResponse(List<Event> eventList) {
        new SaveImgAsyncTask(context, eventAdapter, recyclerView, mSwipeRefreshLayout).execute(eventList);
    }



    private class SaveImgAsyncTask extends AsyncTask<List<Event>, Void, List<Event>>{

        private Context context;
        private EventAdapter eventAdapter;
        private RecyclerView recyclerView;
        private SwipeRefreshLayout mSwipeRefreshLayout;


        private SaveImgAsyncTask(Context context,
                                 EventAdapter eventAdapter,
                                 RecyclerView recyclerView,
                                 SwipeRefreshLayout mSwipeRefreshLayout) {
            this.context = context;
            this.eventAdapter = eventAdapter;
            this.recyclerView = recyclerView;
            this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        }

        @Override
        protected List<Event> doInBackground(List<Event>... lists) {

            List<Event> newList = new ArrayList<>();
            InternalStorageHelper internalStorageHelper = new InternalStorageHelperImpl();
            for (Event e: lists[0]) {

                String path = internalStorageHelper.saveToInternalStorageFromBase64(e.getBase64Img(), e.getEventid(), context);
                e.setImagepath(path);
                newList.add(e);
            }
            return newList;
        }

        @Override
        protected void onPostExecute(List<Event> newList) {
            super.onPostExecute(newList);

                eventAdapter.updateEventsListItems(newList);
                recyclerView.setAdapter(eventAdapter);

                mSwipeRefreshLayout.setRefreshing(false);
                //update adapter here
                new DeleteInsertEventsLocalDBAsyncTask(eventController).execute(newList);
            }
        }

    private class DeleteInsertEventsLocalDBAsyncTask extends AsyncTask<List<Event>, Void, Void>{

        private EventController eventController;

        private DeleteInsertEventsLocalDBAsyncTask(EventController eventController) {
            this.eventController = eventController;
        }

        @Override
        protected Void doInBackground(List<Event>... lists) {

            eventController.deleteAllEvents();
            eventController.insertMany(lists[0]);

            return null;
        }
    }
}





package com.example.myfirstapp.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;

@Database(entities = Event.class, version = 1)
public abstract class EventDatabase extends RoomDatabase {

    private static EventDatabase instance;

    public abstract EventDao eventDao();

    public static synchronized EventDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    EventDatabase.class, "event_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private EventDao eventDao;

        private PopulateDBAsyncTask(EventDatabase db) {
            eventDao = db.eventDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String s = "Hello";


            try {
                byte[] byteData = s.getBytes("UTF-8");

                eventDao.insert(new Event("Title 1", "Description 1", byteData, 0));
                eventDao.insert(new Event("Title 2", "Description 2", byteData, 1));
                eventDao.insert(new Event("Title 3", "Description 3", byteData, 10));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}

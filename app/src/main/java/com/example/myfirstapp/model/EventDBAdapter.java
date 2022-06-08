package com.example.myfirstapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.example.myfirstapp.utils.BitmapToByteArrayHelper;

import java.util.ArrayList;
import java.util.List;

public class EventDBAdapter {
    private static final String TAG = EventDBAdapter.class.getSimpleName();

    private static final String DB_NAME = "event_database.db";
    private static final int DB_VERSION = 1;

    private static final String EVENT_TABLE = "event_table";

    private Context context;
    SharedPreferences prefs;

    private static final String COLUMN_EVENT_ID = "event_id";
    private static final String COLUMN_ORGANIZER_ID = "event_organizer_id";
    private static final String COLUMN_TITLE = "event_title";
    private static final String COLUMN_DESCRIPTION = "event_description";
    private static final String COLUMN_IMAGE = "event_image";
    private static final String COLUMN_IMAGE_NAME = "event_image_name";
    private static final String COLUMN_INTERESTED_COUNT = "event_interested_count";
    private static final String COLUMN_LOCATION = "event_location";
    private static final String COLUMN_DATE = "event_date";
    private static final String COLUMN_TIME = "event_time";
    private static final String COLUMN_CREATED_AT = "event_created_at";
    private static final String COLUMN_UPDATED_AT = "event_updated_at";

    private static final String USER_TABLE = "user_table";

    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "user_username";
    private static final String COLUMN_EMAIL = "user_email";
    private static final String COLUMN_USER_CREATED_AT = "user_created_at";
    private static final String COLUMN_USER_UPDATED_AT = "user_updated_at";

    private static final String EVENT_USERS_TABLE = "event_users_table";

    private static final String COLUMN_EVENT_USERS_EVENT_ID = "event_id";
    private static final String COLUMN_EVENT_USERS_USER_ID = "user_id";

    private static final String[] event_table_columns = new String[] {
            COLUMN_EVENT_ID,
            COLUMN_ORGANIZER_ID,
            COLUMN_TITLE,
            COLUMN_DESCRIPTION,
//            COLUMN_IMAGE,
//            COLUMN_IMAGE_NAME,
            COLUMN_INTERESTED_COUNT,
            COLUMN_LOCATION,
            COLUMN_DATE,
            COLUMN_TIME,
            COLUMN_CREATED_AT,
            COLUMN_UPDATED_AT
    };

    private static final String[] user_table_columns = new String[] {
            COLUMN_USER_ID,
            COLUMN_USERNAME,
            COLUMN_EMAIL,
            COLUMN_USER_CREATED_AT,
            COLUMN_USER_UPDATED_AT
    };

    private static final String[] event_users_table_columns = new String[] {
            COLUMN_EVENT_USERS_EVENT_ID,
            COLUMN_EVENT_USERS_USER_ID
    };

    private static final BitmapToByteArrayHelper bitmapToByteArrayHelper = new BitmapToByteArrayHelper();

    /*
    private static final String CREATE_EVENT_TABLE = "CREATE TABLE " + EVENT_TABLE +
            "(" +
            COLUMN_EVENT_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_TITLE + " TEXT NOT NULL, " +
            COLUMN_ORGANIZER_ID + " INTEGER NOT NULL, " +
            COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
            COLUMN_IMAGE + " BLOB, " +
            COLUMN_INTERESTED_COUNT + " INTEGER DEFAULT 0, " +
            COLUMN_LOCATION + " TEXT NOT NULL, " +
            COLUMN_TIME + " TEXT, " +
            COLUMN_CREATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
            COLUMN_UPDATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
            " FOREIGN KEY (" + COLUMN_ORGANIZER_ID + ") REFERENCES " + USER_TABLE + "(user_id)" +
            ");";

     */

    /*
    private static final String CREATE_EVENT_TABLE = "CREATE TABLE " + EVENT_TABLE +
            "(" +
            COLUMN_EVENT_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_TITLE + " TEXT NOT NULL, " +
            COLUMN_ORGANIZER_ID + " INTEGER NOT NULL, " +
            COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
            COLUMN_IMAGE + " BLOB, " +
            COLUMN_INTERESTED_COUNT + " INTEGER DEFAULT 0, " +
            COLUMN_LOCATION + " TEXT NOT NULL, " +
            COLUMN_TIME + " TEXT, " +
            COLUMN_CREATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
            COLUMN_UPDATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP" +
            ");";

     */

    private static final String CREATE_EVENT_TABLE = "CREATE TABLE " + EVENT_TABLE +
            "(" +
            COLUMN_EVENT_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_TITLE + " TEXT NOT NULL, " +
            COLUMN_ORGANIZER_ID + " INTEGER NOT NULL, " +
            COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
            COLUMN_IMAGE + " TEXT, " +
            COLUMN_IMAGE_NAME + " TEXT, " +
            COLUMN_INTERESTED_COUNT + " INTEGER DEFAULT 0, " +
            COLUMN_LOCATION + " TEXT NOT NULL, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_TIME + " TEXT, " +
            COLUMN_CREATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
            COLUMN_UPDATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP" +
            ");";

    private static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE +
            "(" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_USERNAME + " TEXT, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_USER_CREATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
            COLUMN_USER_UPDATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP " +
            ");";

    private static final String CREATE_EVENT_USERS_TABLE = "CREATE TABLE " + EVENT_USERS_TABLE +
            "(" +
            COLUMN_EVENT_USERS_EVENT_ID + " INTEGER REFERENCES " + EVENT_TABLE + "(event_id) " +
            "ON UPDATE CASCADE ON DELETE CASCADE, " +
            COLUMN_EVENT_USERS_USER_ID + " INTEGER REFERENCES " + USER_TABLE + "(user_id) " +
            "ON UPDATE CASCADE, " +
            " PRIMARY KEY (" + COLUMN_EVENT_USERS_EVENT_ID + ", " + COLUMN_EVENT_USERS_USER_ID + ")" +
            ");";

    private SQLiteDatabase sqLiteDatabase;
    private static EventDBAdapter instance;

    private EventDBAdapter(Context context) {
        this.context = context;
        sqLiteDatabase = new EventDBHelper(this.context, DB_NAME, null, DB_VERSION).getWritableDatabase();
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static EventDBAdapter getInstance(Context context) {
        if (instance == null) {
            instance = new EventDBAdapter(context);
        }
        return instance;
    }

    //insert, update, delete methods

    public boolean insert(Event event) {

        long result;
        result = sqLiteDatabase.insert(EVENT_TABLE, null, loadContentValues(event));

        if (result == -1) {
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
        }
        return result > 0;

    }

    private ContentValues loadContentValues(Event event) {
        ContentValues contentValues = new ContentValues();

        if (event.getTitle() != null) {
            contentValues.put(COLUMN_TITLE, event.getTitle());
        }

        if (event.getDescription() != null) {
            contentValues.put(COLUMN_DESCRIPTION, event.getDescription());
        }

        if (event.getImageUri() != null) {
            contentValues.put(COLUMN_IMAGE, event.getImageUri());
        }

        if (event.getImgName() != null) {
            contentValues.put(COLUMN_IMAGE_NAME, event.getImgName());
        }


        contentValues.put(COLUMN_INTERESTED_COUNT, event.getInterested_count());

        if (event.getLocation() != null) {
            contentValues.put(COLUMN_LOCATION, event.getLocation());
        }

        if (event.getDate() != null) {
            contentValues.put(COLUMN_DATE, event.getDate());
        }

        if (event.getTime() != null) {
            contentValues.put(COLUMN_TIME, event.getTime());
        }

        if (event.getCreated_at() != null) {
            contentValues.put(COLUMN_CREATED_AT, event.getCreated_at());
        }

        if (event.getUpdated_at() != null) {
            contentValues.put(COLUMN_UPDATED_AT, event.getUpdated_at());
        }

        contentValues.put(COLUMN_EVENT_ID, event.getId());
        contentValues.put(COLUMN_ORGANIZER_ID, prefs.getInt(String.valueOf(R.string.pref_user_id), 0));

        return contentValues;
    }

    public boolean update(Event event) {
        return sqLiteDatabase.update(EVENT_TABLE, loadContentValues(event), COLUMN_EVENT_ID+ " = " + event.getId()
        , null) > 0;
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<Event>();

        Cursor cursor = sqLiteDatabase.query(EVENT_TABLE, event_table_columns, null, null, null,
                null, null);


        /*
        if (cursor != null && cursor.getCount() > 0 && cursor.getString(4) != null
        && cursor.getString(5) != null) {
            while (cursor.moveToNext()) {
                Event event = new Event(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11)
                );

                events.add(event);
            }
            cursor.close();
        }

         */

        Log.d(TAG, cursor.getString(6));
         if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Event event = new Event(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9)
                );

                events.add(event);
            }
            cursor.close();
        }

        return events;
    }

    public List<Event> getUserEvents(String id) {
        List<Event> events = new ArrayList<Event>();

        Cursor cursor = sqLiteDatabase.query(EVENT_TABLE, event_table_columns, "event_organizer_id = ?",
                new String[] {id}, null,
                null, null);

        Log.d(TAG, "HERE");

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Event event = new Event(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11)
                );

                events.add(event);
            }
            cursor.close();
        }

        return events;
    }

    public boolean delete(Event event) {

        return sqLiteDatabase.delete(EVENT_TABLE, COLUMN_EVENT_ID+ " = " + event.getId(), null) > 0;
    }

    public static class EventDBHelper extends SQLiteOpenHelper {

        public EventDBHelper(Context context, String databaseName,
                             SQLiteDatabase.CursorFactory factory,
                             int dbVersion)
        {
            super(context, databaseName, factory, dbVersion);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
//            sqLiteDatabase.execSQL(CREATE_USER_TABLE);
            sqLiteDatabase.execSQL(CREATE_EVENT_TABLE);
//            sqLiteDatabase.execSQL(CREATE_EVENT_USERS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ EVENT_TABLE);
//            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ USER_TABLE);
//            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EVENT_USERS_TABLE);
            onCreate(sqLiteDatabase);

        }


    }
}

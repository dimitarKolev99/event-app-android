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

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

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

    private static final String[] event_table_columns = new String[]{
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

    private static final String CREATE_EVENT_TABLE = "CREATE TABLE " + EVENT_TABLE +
            "(" +
                COLUMN_EVENT_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_ORGANIZER_ID + " INTEGER NOT NULL, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
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

    SQLiteDatabase sqLiteDatabase;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        sqLiteDatabase = this.getWritableDatabase();
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ EVENT_TABLE);
        onCreate(sqLiteDatabase);
    }

    //insert, update, delete methods

    public boolean insert(Event event) {
        sqLiteDatabase = this.getWritableDatabase();
        long result;
        result = sqLiteDatabase.insert(EVENT_TABLE, null, loadContentValues(event));

        return result != -1;

    }

    private ContentValues loadContentValues(Event event) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_EVENT_ID, event.getId());
        contentValues.put(COLUMN_ORGANIZER_ID, event.getOrganizer_id());
        contentValues.put(COLUMN_TITLE, event.getTitle());
        contentValues.put(COLUMN_DESCRIPTION, event.getDescription());
        contentValues.put(COLUMN_INTERESTED_COUNT, event.getInterested_count());
        contentValues.put(COLUMN_LOCATION, event.getLocation());
        contentValues.put(COLUMN_DATE, event.getDate());
        contentValues.put(COLUMN_TIME, event.getTime());
        contentValues.put(COLUMN_CREATED_AT, event.getCreated_at());
        contentValues.put(COLUMN_UPDATED_AT, event.getUpdated_at());

        return contentValues;
    }

    public boolean update(Event event) {
        sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.update(EVENT_TABLE, loadContentValues(event), COLUMN_EVENT_ID + " = " + event.getId()
                , null) > 0;
    }

    public List<Event> getAllEvents() {
        sqLiteDatabase = this.getReadableDatabase();
        List<Event> events = new ArrayList<Event>();

        Cursor cursor = sqLiteDatabase.query(EVENT_TABLE, event_table_columns, null, null, null,
                null, null);

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
        sqLiteDatabase = this.getReadableDatabase();
        List<Event> events = new ArrayList<Event>();

        Cursor cursor = sqLiteDatabase.query(EVENT_TABLE, event_table_columns, "event_organizer_id = ?",
                new String[]{id}, null,
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
        sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(EVENT_TABLE, COLUMN_EVENT_ID + " = " + event.getId(), null) > 0;
    }

    public void deleteAllEvents() {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " +  EVENT_TABLE);
    }
}

package com.example.myfirstapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myfirstapp.controller.EventAdapter;
import com.example.myfirstapp.controller.EventController;
import com.example.myfirstapp.controller.OnItemClickListener;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventModel;
import com.example.myfirstapp.model.EventModelImpl;
import com.example.myfirstapp.network.Controller;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";

    Handler mHandler = new Handler();

    EventController eventController;

    private EventModel eventModel;

    public static final String REQUEST_CODE = "REQUEST_CODE";

    RecyclerView recyclerView;

    EventAdapter eventAdapter;

    FloatingActionButton addEventBtn;

    public static final int ADD_EVENT_REQUEST = 1;
    public static final int EDIT_EVENT_REQUEST = 2;

    List<Event> eventList = new ArrayList<>();

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

//        eventModel = new EventModelImpl(MyApplication.getEventDBAdapter());
//        eventController = new EventController(eventModel, this);
        setViews();
//        new GetEventAsyncTask(eventController).execute();

        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                doCall();
            }
        }).start();

         */

        addEventBtn = findViewById(R.id.add_event_btn);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditEventActivity.class);
                intent.putExtra(REQUEST_CODE, ADD_EVENT_REQUEST);
                openActivityForResult(intent);
            }
        });
    }

    public void openActivityForResult(Intent intent) {
        addEventActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> addEventActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK &&
                            result.getData().getIntExtra(REQUEST_CODE, 0) == ADD_EVENT_REQUEST) {

                        recreate();

                        Toast.makeText(MainActivity.this, "On Activity Success Result", Toast.LENGTH_SHORT).show();

                    } else if (result.getResultCode() == Activity.RESULT_OK &&
                            result.getData().getIntExtra(REQUEST_CODE, 0) == EDIT_EVENT_REQUEST) {
                        int id = result.getData().getIntExtra(AddEditEventActivity.EXTRA_ID, -1);

                        if (id == -1) {
                            Toast.makeText(getApplicationContext(), "Event couldn't be updated", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        recreate();

                        Toast.makeText(MainActivity.this, "Event updated!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Saving event failed", Toast.LENGTH_SHORT).show();
                    }


                }
            }
    );

    public void setViews() {

        eventAdapter = new EventAdapter(new ArrayList<Event>(), MainActivity.this);
        eventAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onEventClick(Event event) {
                Intent intent = new Intent(MainActivity.this, AddEditEventActivity.class);
                intent.putExtra(REQUEST_CODE, EDIT_EVENT_REQUEST);
                intent.putExtra(AddEditEventActivity.EXTRA_ID, event.getId());
                intent.putExtra(AddEditEventActivity.EXTRA_TITLE, event.getTitle());
                intent.putExtra(AddEditEventActivity.EXTRA_DESCRIPTION, event.getDescription());
                intent.putExtra(AddEditEventActivity.EXTRA_DATE, event.getDate());
                intent.putExtra(AddEditEventActivity.EXTRA_TIME, event.getTime());
                intent.putExtra(AddEditEventActivity.EXTRA_LOCATION, event.getLocation());
                intent.putExtra(AddEditEventActivity.EXTRA_IMAGE_NAME, event.getImgName());
                intent.putExtra(AddEditEventActivity.EXTRA_IMAGE_URI, event.getImageUri());

                openActivityForResult(intent);

            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        setItemTouchHelper();
//        setClickListener(eventAdapter);
    }

    public void updateResView(List<Event> eventList) {
//        eventAdapter.updateEventsListItems(eventList);
    }

    private void setItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                /*
                if (eventController.onRemoveButtonClicked(eventAdapter.getEventAt(viewHolder.getAdapterPosition()))) {
                    eventList = eventController.getList();
                    updateResView(eventList);
                    recyclerView.setAdapter(eventAdapter);
                    Toast.makeText(MainActivity.this, "Event deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Deletion failed", Toast.LENGTH_SHORT).show();
                }

                 */

            }


        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_frag_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_events:
                //Query the db here
                /*
                eventList = eventController.getUserEvents(sharedPreferences.getInt(String.valueOf(R.string.pref_user_id), 0));
                updateResView(eventList);
                recyclerView.setAdapter(eventAdapter);

                 */
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(this, "Paused Main", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        new GetEventAsyncTask(eventController).execute();

//        Toast.makeText(this, "Resumed Main", Toast.LENGTH_SHORT).show();
    }

    private void doCall() {

        //final String data = NetworkService.INSTANCE.getUsers("https://fakestoreapi.com/products/");
//        Controller controller = new Controller();
//        controller.start();


        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String[] data = new String[]{"1", "2", "3", "4", "5", "6"};
//                viewModel.setText(data);
            }
        });

    }

    private static class InsertEventAsyncTask extends AsyncTask<Event, Void, List<Event>> {
        private EventController eventController;
        private EventAdapter eventAdapter;

        private InsertEventAsyncTask(EventController eventController, EventAdapter eventAdapter) {
            this.eventController = eventController;
            this.eventAdapter = eventAdapter;
        }

        @Override
        protected List<Event> doInBackground(Event... events) {
            eventController.onAddButtonClicked(events[0]);
            return eventController.onViewLoaded();
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);

        }
    }

    private class GetEventAsyncTask extends AsyncTask<Void, Void, List<Event>> {
        private EventController eventController;

        private GetEventAsyncTask(EventController eventController) {
            this.eventController = eventController;
        }

        @Override
        protected List<Event> doInBackground(Void... voids) {
            return eventController.onViewLoaded();
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);

            if (events != null) {
                eventList = events;
                updateResView(events);
                recyclerView.setAdapter(eventAdapter);
            }
        }
    }
}
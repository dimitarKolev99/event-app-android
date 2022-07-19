package com.example.myfirstapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myfirstapp.controller.EventAdapter;
import com.example.myfirstapp.controller.EventController;
import com.example.myfirstapp.controller.EventControllerImpl;
import com.example.myfirstapp.controller.OnItemClickListener;
import com.example.myfirstapp.controller.OnResponseListener;
import com.example.myfirstapp.controller.SaveEventHelper;
import com.example.myfirstapp.controller.SaveEventHelperImpl;
import com.example.myfirstapp.controller.SetRefreshingListener;
import com.example.myfirstapp.model.DBHelper;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventModel;
import com.example.myfirstapp.model.EventModelImpl;
import com.example.myfirstapp.network.DialogBuilderListener;
import com.example.myfirstapp.network.GetRequest;
import com.example.myfirstapp.network.UpdateEvent;
import com.example.myfirstapp.network.WebSocketClient;
import com.example.myfirstapp.utils.BitmapToByteArrayHelper;
import com.example.myfirstapp.utils.ImageEncoder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.WebSocket;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    private EventController eventController;

    private EventModel eventModel;

    public static final String REQUEST_CODE = "REQUEST_CODE";

    RecyclerView recyclerView;

    EventAdapter eventAdapter;

    FloatingActionButton addEventBtn;

    public static final int ADD_EVENT_REQUEST = 1;
    public static final int EDIT_EVENT_REQUEST = 2;

    List<Event> eventList = new ArrayList<>();

    SharedPreferences sharedPreferences;

    ImageEncoder imageEncoder;

    BitmapToByteArrayHelper bitmapToByteArrayHelper;

    GetRequest getRequest;

    SaveEventHelper saveEventHelper;

    SwipeRefreshLayout mSwipeRefreshLayout;

    static WebSocket webSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("prefs", Context.MODE_PRIVATE);

        bitmapToByteArrayHelper = new BitmapToByteArrayHelper();

        imageEncoder = new ImageEncoder(bitmapToByteArrayHelper, this);

        eventModel = new EventModelImpl(new DBHelper(this));
        eventController = new EventControllerImpl(eventModel, this);
        setViews();

        mSwipeRefreshLayout = findViewById(R.id.swipe_container);

        saveEventHelper = new SaveEventHelperImpl(this, eventController, mSwipeRefreshLayout, eventAdapter, recyclerView);

        getRequest = new GetRequest();

        getRequest.setOnResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(int statusCode, List<Event> eventList) {
                if (statusCode != 200) {
                    new GetEventAsyncTask(eventController).execute();
                } else {
                    saveEventHelper.processNetworkResponse(eventList);
                }
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple_200,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        WebSocketClient webSocketClient = new WebSocketClient();
        webSocketClient.setListener(new DialogBuilderListener() {
            @Override
            public void buildDialog() {
                runOnUiThread(() -> {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("New events just came in");
                    builder.setMessage("Do you want to update?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            turnOnSwipeRefresh();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create().show();
                });
            }
        });
        webSocket = webSocketClient.getWebSocket();
        setListener();
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        if (isNetworkAvailable()) {
            initSocketConnection();
            mSwipeRefreshLayout.post(new Runnable() {

                @Override
                public void run() {

                    mSwipeRefreshLayout.setRefreshing(true);

                    // Fetching data from server
                    getRequest.performRequest(GetRequest.GET_ALL_EVENTS, "0", null);
                }
            });
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
            new GetEventAsyncTask(eventController).execute();
        }

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

    public static WebSocket getWebSocket() {

        return webSocket;
    }

    private void initSocketConnection() {
        WebSocketClient webSocketClient = new WebSocketClient();
        webSocketClient.setListener(new DialogBuilderListener() {
            @Override
            public void buildDialog() {

            }
        });
        webSocket = webSocketClient.getWebSocket();
    }


    public void openActivityForResult(Intent intent) {
        addEventActivityResultLauncher.launch(intent);
    }

    private void turnOnSwipeRefresh() {
        if (isNetworkAvailable()) {
            initSocketConnection();
            mSwipeRefreshLayout.post(new Runnable() {

                @Override
                public void run() {

                    mSwipeRefreshLayout.setRefreshing(true);

                    // Fetching data from server
                    getRequest.performRequest(GetRequest.GET_ALL_EVENTS, "0", null);
                }
            });
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
            new GetEventAsyncTask(eventController).execute();
        }
    }

    ActivityResultLauncher<Intent> addEventActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK &&
                            result.getData().getIntExtra(REQUEST_CODE, 0) == ADD_EVENT_REQUEST) {


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
        recyclerView = findViewById(R.id.recyclerView);
        eventAdapter = new EventAdapter(new ArrayList<Event>(), MainActivity.this, eventController,
                new UpdateEvent(MainActivity.this), recyclerView);
        eventAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onEventClick(Event event) {
                Intent intent = new Intent(MainActivity.this, AddEditEventActivity.class);
                intent.putExtra(REQUEST_CODE, EDIT_EVENT_REQUEST);
                intent.putExtra(AddEditEventActivity.EXTRA_ID, event.getEventid());
                intent.putExtra(AddEditEventActivity.EXTRA_ORGANIZER_ID, event.getOrganizerid());
                intent.putExtra(AddEditEventActivity.EXTRA_TITLE, event.getTitle());
                intent.putExtra(AddEditEventActivity.EXTRA_IMAGE_URI, event.getImagepath());
                intent.putExtra(AddEditEventActivity.EXTRA_IMAGE_NAME, event.getImageName());

                openActivityForResult(intent);

            }
        });

        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        setItemTouchHelper();
    }

    public void updateResView(List<Event> eventList) {
        eventAdapter.updateEventsListItems(eventList);
    }

    private void setListener() {
        eventController.setSetRefreshingListener(new SetRefreshingListener() {
            @Override
            public void setSwipeRefresherVal(boolean val) {
                mSwipeRefreshLayout.setRefreshing(val);
            }
        });
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
                new DeleteEventAsyncTask(eventAdapter, viewHolder).execute();
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

    void setRefresh(boolean bool) {
        mSwipeRefreshLayout.setRefreshing(bool);
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
    public void onRefresh() {
        if (isNetworkAvailable()) {
            getRequest.performRequest(GetRequest.GET_ALL_EVENTS, null, null);
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
            new GetEventAsyncTask(eventController).execute();
        }
    }

    private class DeleteEventAsyncTask extends AsyncTask<Event, Void, List<Event>> {
        private EventAdapter eventAdapter;
        private RecyclerView.ViewHolder viewHolder;

        private DeleteEventAsyncTask(EventAdapter eventAdapter,
                                     RecyclerView.ViewHolder viewHolder) {
            this.eventAdapter = eventAdapter;
            this.viewHolder = viewHolder;
        }

        @Override
        protected List<Event> doInBackground(Event... events) {
            eventController.onRemoveButtonClicked(eventAdapter.getEventAt(viewHolder.getAdapterPosition()));
            return eventController.onViewLoaded();
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);

            updateResView(events);
            recyclerView.setAdapter(eventAdapter);

        }
    }

    public class GetEventAsyncTask extends AsyncTask<Void, Void, List<Event>> {
        private EventController eventController;

        public GetEventAsyncTask(EventController eventController) {
            this.eventController = MainActivity.this.eventController;
        }

        @Override
        protected List<Event> doInBackground(Void... voids) {
            return MainActivity.this.eventController.onViewLoaded();
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);

                if (events != null) {
                    Toast.makeText(MainActivity.this, "Events Local DB NOT NULL", Toast.LENGTH_SHORT).show();
                    eventList = events;
                    updateResView(events);
                    recyclerView.setAdapter(eventAdapter);
                    setRefresh(false);
                } else {
                    Toast.makeText(MainActivity.this, "Events Local DB NULL", Toast.LENGTH_SHORT).show();
                }
            }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
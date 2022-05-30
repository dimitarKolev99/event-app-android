package com.example.myfirstapp;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.controller.EventAdapter;
import com.example.myfirstapp.controller.EventController;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventModel;
import com.example.myfirstapp.model.EventModelImpl;
import com.example.myfirstapp.utils.BitmapToByteArrayHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    private static final String TAG = "FRAGMENT_TAG";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    RecyclerView recyclerView;

    EventAdapter eventAdapter;

    View view;

    LocalBroadcastManager manager;

    Handler mHandler = new Handler();

    FloatingActionButton addEventBtn;

    EventController eventController;

    private EventModel eventModel;

    public static final int ADD_EVENT_REQUEST = 1;
    public static final int EDIT_EVENT_REQUEST = 2;


    SharedPreferences prefs;

    List<Event> eventList = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        view = inflater.inflate(R.layout.fragment_home, container, false);
        eventModel = new EventModelImpl(MyApplication.getEventDBAdapter());
        eventController = new EventController(eventModel, getContext());

        GetEventAsyncTask getEventAsyncTask = new GetEventAsyncTask(eventController);
        getEventAsyncTask.execute();



        Log.d(TAG, "HERE");
        // btn to add event activity
        addEventBtn = view.findViewById(R.id.add_event_btn);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEditEventActivity.class);
                intent.putExtra(REQUEST_CODE, ADD_EVENT_REQUEST);
                openActivityForResult(intent);
            }
        });

// on update

        /*
        if (eventAdapter.getItemCount() != 0) {
            recyclerView.smoothScrollToPosition(eventAdapter.getItemCount() - 1);
        }

         */

        return view;
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
                        getActivity().recreate();
                        /*
                        Intent data = result.getData();
                        String title = data.getStringExtra(AddEditEventActivity.EXTRA_TITLE);
                        String description = data.getStringExtra(AddEditEventActivity.EXTRA_DESCRIPTION);
                        String date = data.getStringExtra(AddEditEventActivity.EXTRA_DATE);
                        Uri uri = data.getParcelableExtra(AddEditEventActivity.EXTRA_URI);
                        String time = data.getStringExtra(AddEditEventActivity.EXTRA_TIME);
                        String location = data.getStringExtra(AddEditEventActivity.EXTRA_LOCATION);

                        Random random = new Random();

                        BitmapToByteArrayHelper bitmapToByteArrayHelper = new BitmapToByteArrayHelper();

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                        Date datee = new Date(System.currentTimeMillis());

                        if (uri != null) {
                            InputStream inputStream = null;
                            try {
                                if (getContext().getContentResolver() != null) {
                                    inputStream = getContext().getContentResolver().openInputStream(uri);
                                    Log.d(TAG, "FALSe");
                                } else {
                                    Log.d(TAG, "TRUE");
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            Bitmap image = BitmapFactory.decodeStream(inputStream);

                            int id = random.nextInt();
                            String imgName =  "img" + String.valueOf(id) + ".jpg";
                            String path = saveToInternalStorage(image, id);

                            Event event = new Event(random.nextInt(),  prefs.getInt(String.valueOf(R.string.pref_user_id), 0),
                                    title, description, path, imgName,
                                    0, location, date, time, formatter.format(datee),
                                    formatter.format(datee));

//                            new InsertEventAsyncTask(eventController, eventAdapter, getActivity()).execute(event);
//                            eventController.onAddButtonClicked(event);

//                            eventAdapter.submitList(eventController.onViewLoaded());
//
//                              eventAdapter.notifyItemInserted(eventAdapter.getItemCount());
//                            getActivity().finish();                             //FAIL
//                            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));

                        }

                         */

//                        Toast.makeText(getContext(), "Event saved", Toast.LENGTH_SHORT).show();

                    } else if (result.getResultCode() == Activity.RESULT_OK &&
                            result.getData().getIntExtra(REQUEST_CODE, 0) == EDIT_EVENT_REQUEST) {
                        int id = result.getData().getIntExtra(AddEditEventActivity.EXTRA_ID, -1);

                        if (id == -1) {
                            Toast.makeText(getContext(), "Event couldn't be updated", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String title = result.getData().getStringExtra(AddEditEventActivity.EXTRA_TITLE);
                        String description = result.getData().getStringExtra(AddEditEventActivity.EXTRA_DESCRIPTION);

//                        Event event = new Event(title, description, 0);
//                        event.setId(id);

//                        eventViewModel.update(event);
//                        eventController.onAddButtonClicked(event);


                        Toast.makeText(getContext(), "Event updated!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Saving event failed", Toast.LENGTH_SHORT).show();
                    }


                }
            }
    );

    private String saveToInternalStorage(Bitmap bitmapImage, int id){
        ContextWrapper cw = new ContextWrapper(getContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        String name = "img" + String.valueOf(id) + ".jpg";
        File mypath = new File(directory, name);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }




    public void setViews(List<Event> eventList) {
        eventAdapter = new EventAdapter(eventList);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        setItemTouchHelper();
//        setClickListener(eventAdapter);
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
//                eventViewModel.delete(eventAdapter.getEventAt(viewHolder.getAdapterPosition()));
                if (eventController.onRemoveButtonClicked(eventAdapter.getEventAt(viewHolder.getAdapterPosition()))) {
                    eventAdapter.notifyDataSetChanged();
//                    eventAdapter.submitList(eventController.getList());
                    Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Deletion failed", Toast.LENGTH_SHORT).show();
                }

            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.main_frag_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_events:
//                eventViewModel.deleteAllEvents();
                Toast.makeText(getContext(), "All notes deleted", Toast.LENGTH_SHORT).show();
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void doCall() {

        //final String data = NetworkService.INSTANCE.getUsers("https://fakestoreapi.com/products/");
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                String[] data = new String[] {"1", "2", "3", "4", "5", "6"};
//                viewModel.setText("wow");
//                setViews(data);

                /*
                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
                Intent intent = new Intent("com.action.test");
                intent.putExtra("key",data);
                manager.sendBroadcast(intent);

                 */


            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /*
    private void setClickListener(EventAdapter eventAdapter) {
        eventAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onEventClick(Event event) {
                Intent intent = new Intent(getActivity(), AddEditEventActivity.class);
                intent.putExtra(AddEditEventActivity.EXTRA_ID, event.getId());
                intent.putExtra(AddEditEventActivity.EXTRA_TITLE, event.getTitle());
                intent.putExtra(AddEditEventActivity.EXTRA_DESCRIPTION, event.getDescription());
                intent.putExtra("REQUEST_CODE", EDIT_EVENT_REQUEST);
                openActivityForResult(intent);
            }
        });


    }

     */



    /*
    private void doCall() {

        final String data = NetworkService.INSTANCE.getUsers("https://fakestoreapi.com/products/");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String[] data = new String[]{"1", "2", "3", "4", "5", "6"};
                customAdapter = new CustomAdapter(data);
                recyclerView = view.findViewById(R.id.recyclerView);
                recyclerView.setAdapter(customAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
//                tvDisplay.setText(data);
            }
        });

    }

     */

    private static class InsertEventAsyncTask extends AsyncTask<Event, Void, List<Event>> {
        private EventController eventController;
        private EventAdapter eventAdapter;
        private FragmentActivity fragmentActivity;

        private InsertEventAsyncTask(EventController eventController, EventAdapter eventAdapter,
                                     FragmentActivity fragmentActivity) {
            this.eventController = eventController;
            this.eventAdapter = eventAdapter;
            this.fragmentActivity = fragmentActivity;
        }

        @Override
        protected List<Event> doInBackground(Event... events) {
            eventController.onAddButtonClicked(events[0]);
            return eventController.onViewLoaded();
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);
            List<Event> events1 = new ArrayList<Event>();
            events1.addAll(events);
            for (int i = 0; i < events1.size(); i++) {
                Log.d(TAG, events1.get(i).getTitle());
            }
            eventAdapter.notifyDataSetChanged();
            fragmentActivity.recreate();
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
            setViews(events);
//            eventAdapter.notifyDataSetChanged();
//            fragmentActivity.recreate();
        }
    }


}


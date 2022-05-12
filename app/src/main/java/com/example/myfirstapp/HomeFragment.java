package com.example.myfirstapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.controller.EventAdapter;
import com.example.myfirstapp.controller.OnItemClickListener;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment {


    private static final String TAG = "FRAGMENT_TAG";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    RecyclerView recyclerView;

    EventAdapter eventAdapter;

    View view;

    LocalBroadcastManager manager;

    Handler mHandler = new Handler();

    FloatingActionButton addEventBtn;

    private SharedViewModel viewModel;
    private EventViewModel eventViewModel;

    public static final int ADD_EVENT_REQUEST = 1;
    public static final int EDIT_EVENT_REQUEST = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        view = inflater.inflate(R.layout.fragment_home, container, false);

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

        eventAdapter = new EventAdapter();

        setViews();
        eventViewModel = new ViewModelProvider(getActivity()).get(EventViewModel.class);
        eventViewModel.getAllEvents().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                Toast.makeText(getActivity(), "Changed", Toast.LENGTH_SHORT).show();
                eventAdapter.submitList(events);
                recyclerView.smoothScrollToPosition(eventAdapter.getItemCount() - 1);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                eventViewModel.delete(eventAdapter.getEventAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

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

                        Intent data = result.getData();
                        String title = data.getStringExtra(AddEditEventActivity.EXTRA_TITLE);
                        String description = data.getStringExtra(AddEditEventActivity.EXTRA_DESCRIPTION);

                        Event event = new Event(title, description, 0);
                        eventViewModel.insert(event);

                        Toast.makeText(getContext(), "Event saved", Toast.LENGTH_SHORT).show();

                    } else if (result.getResultCode() == Activity.RESULT_OK &&
                            result.getData().getIntExtra(REQUEST_CODE, 0) == EDIT_EVENT_REQUEST) {
                        int id = result.getData().getIntExtra(AddEditEventActivity.EXTRA_ID, -1);

                        if (id == -1) {
                            Toast.makeText(getContext(), "Event couldn't be updated", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String title = result.getData().getStringExtra(AddEditEventActivity.EXTRA_TITLE);
                        String description = result.getData().getStringExtra(AddEditEventActivity.EXTRA_DESCRIPTION);

                        Event event = new Event(title, description, 0);
                        event.setId(id);

                        eventViewModel.update(event);

                        Toast.makeText(getContext(), "Event updated!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Saving event failed", Toast.LENGTH_SHORT).show();
                    }


                }
            }
    );



    public void setViews() {
        //eventAdapter = new EventAdapter();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        setItemTouchHelper();
        setClickListener(eventAdapter);
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
                eventViewModel.delete(eventAdapter.getEventAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
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
                eventViewModel.deleteAllEvents();
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

}


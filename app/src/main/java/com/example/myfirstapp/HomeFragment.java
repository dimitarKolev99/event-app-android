package com.example.myfirstapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.controller.CustomAdapter;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventViewModel;
import com.example.myfirstapp.network.NetworkService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class HomeFragment extends Fragment {


    private static final String TAG = "FRAGMENT_TAG";
    RecyclerView recyclerView;

    CustomAdapter customAdapter;

    View view;

    LocalBroadcastManager manager;

    Handler mHandler = new Handler();

    private SharedViewModel viewModel;
    private EventViewModel eventViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                doCall();
            }
        }).start();

         */

        view = inflater.inflate(R.layout.fragment_home, container, false);
//        setViews(myValue);

        eventViewModel = new ViewModelProvider(getActivity()).get(EventViewModel.class);
        eventViewModel.getAllEvents().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                Toast.makeText(getActivity(), "Changed", Toast.LENGTH_SHORT).show();
                setViews(events);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*
        eventViewModel = new ViewModelProvider(getActivity()).get(EventViewModel.class);
        eventViewModel.getAllEvents().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                Toast.makeText(getActivity(), "Changed", Toast.LENGTH_SHORT).show();
                setViews(events);
            }
        });

         */
    }

    public void setViews(List<Event> data) {
        customAdapter = new CustomAdapter(data);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void doCall() {

        final String data = NetworkService.INSTANCE.getUsers("https://fakestoreapi.com/products/");
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

    private void initBroadCastReceiver() {
        manager = LocalBroadcastManager.getInstance(getContext());
        MyBroadCastReceiver receiver = new MyBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        //whatever
        filter.addAction("com.action.test");
        manager.registerReceiver(receiver,filter);
    }

    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //e.g
            String key = intent.getStringExtra("key");
        }
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


package com.example.myfirstapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.controller.CustomAdapter;
import com.example.myfirstapp.network.NetworkService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {


    RecyclerView recyclerView;

    CustomAdapter customAdapter;

    View view;

    LocalBroadcastManager manager;

    public interface HomeFragmentListener {
        void onInputASent(CharSequence input);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        String[] myValue = bundle.getStringArray("message");
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setViews(myValue);

        return view;
    }

    public void setViews(String[] data) {
        customAdapter = new CustomAdapter(data);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
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


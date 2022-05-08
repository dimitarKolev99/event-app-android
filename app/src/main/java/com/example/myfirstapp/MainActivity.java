package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myfirstapp.controller.CustomAdapter;
import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.model.EventViewModel;
import com.example.myfirstapp.network.NetworkService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MYTAG";
    /*
        RecyclerView recyclerView;
        Handler mHandler = new Handler();
        CustomAdapter customAdapter;

         */
    Handler mHandler = new Handler();
    BottomNavigationView bottomNav;
//    LocalBroadcastManager manager;
    HomeFragment homeFragment;
    MapFragment mapFragment;
    FavoritesFragment favoritesFragment;
    private SharedViewModel viewModel;
    private EventViewModel eventViewModel;
    FragmentManager fragmentManager;
//    FragmentTransaction fragmentTransaction;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        eventViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {

                //update RecyclerView
//                viewModel.setEvents(events);
            }
        });

         */
        /*
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                doCall();
            }
        }).start();

         */
//        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        Intent intent = getIntent();
        String[] data = intent.getStringArrayExtra("data");

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        homeFragment = new HomeFragment();
        mapFragment = new MapFragment();
        favoritesFragment = new FavoritesFragment();
        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment, "HOME_FRAGMENT")
                .setReorderingAllowed(true)
                .addToBackStack(homeFragment.getClass().getName())
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;





                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = homeFragment;
                            fragmentManager.popBackStack(getCurrentFragment().getClass().getName(),
                                    FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        case R.id.nav_map:
                            selectedFragment = mapFragment;
                            break;
                        case R.id.nav_favorites:
                            selectedFragment = favoritesFragment;
                            break;
                    }



                    fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment)
//                                        .setReorderingAllowed(true)
                                        .addToBackStack(selectedFragment.getClass().getName())
                                        .commit();

                    int index = fragmentManager.getBackStackEntryCount() - 1;
                    /*
                    FragmentManager.BackStackEntry backEntry = (FragmentManager.BackStackEntry) getFragmentManager().getBackStackEntryAt(index);
                    String tag = backEntry.getName();
                    android.app.Fragment fragment = getFragmentManager().findFragmentByTag(tag);


                     */
                    Log.d(TAG, String.valueOf(index));





                    return true;
                }
            };


    public Fragment getCurrentFragment() {
        return fragmentManager.findFragmentById(R.id.fragment_container);
    }

    private void replaceFragment (Fragment fragment){
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = fragmentManager;
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }



    private void doCall() {

        final String data = NetworkService.INSTANCE.getUsers("https://fakestoreapi.com/products/");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String[] data = new String[] {"1", "2", "3", "4", "5", "6"};
//                viewModel.setText(data);
            }
        });

    }
}
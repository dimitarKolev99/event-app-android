package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.example.myfirstapp.controller.EventController;
import com.example.myfirstapp.model.EventModel;
import com.example.myfirstapp.model.EventModelImpl;
import com.example.myfirstapp.network.Controller;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MYTAG";

    Handler mHandler = new Handler();
    BottomNavigationView bottomNav;

    EventController eventController;

    private EventModel eventModel;

    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventModel = new EventModelImpl(MyApplication.getEventDBAdapter());
        eventController = new EventController(eventModel, this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        new Thread(new Runnable() {
            @Override
            public void run() {
                doCall();
            }
        }).start();

        Intent intent = getIntent();
        String[] data = intent.getStringArrayExtra("data");

        bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        goToFragment(HomeFragment.newInstance());

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_my_events:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyEventsFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        HomeFragment instance = HomeFragment.newInstance();
                        pop();
                        goToFragment(instance);
                        break;
                    case R.id.nav_map:
                        MapFragment mapFragmentInstance = MapFragment.newInstance();
                        pop();
                        goToFragment(mapFragmentInstance);
                        break;
                    case R.id.nav_favorites:
                        FavoritesFragment favoritesFragment = FavoritesFragment.newInstance();
                        pop();
                        goToFragment(favoritesFragment);
                        break;
                }

                return true;
            };

    public void goToFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment).addToBackStack("").commit();
        //ft.add(R.id.fragment_container, fragment).addToBackStack("").commit();
    }

    public void pop() {
        getFragmentManager().popBackStack();
    }

    private void doCall() {

        //final String data = NetworkService.INSTANCE.getUsers("https://fakestoreapi.com/products/");
        Controller controller = new Controller();
        controller.start();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String[] data = new String[]{"1", "2", "3", "4", "5", "6"};
//                viewModel.setText(data);
            }
        });

    }
}
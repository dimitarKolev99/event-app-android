package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.myfirstapp.model.EventViewModel;
import com.example.myfirstapp.network.Controller;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MYTAG";
    /*
        RecyclerView recyclerView;
        Handler mHandler = new Handler();
        CustomAdapter customAdapter;

         */
    Handler mHandler = new Handler();
    BottomNavigationView bottomNav;

    HomeFragment homeFragment;
    MapFragment mapFragment;
    FavoritesFragment favoritesFragment;
    private SharedViewModel viewModel;
    private EventViewModel eventViewModel;
    FragmentManager fragmentManager;





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

        //viewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doCall();
            }
        }).start();


//        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        Intent intent = getIntent();
        String[] data = intent.getStringArrayExtra("data");

        bottomNav = findViewById(R.id.bottom_navigation);
//        bottomNav.setOnNavigationItemSelectedListener(navListener);


        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        final NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNav, navController);

        homeFragment = new HomeFragment();
        mapFragment = new MapFragment();
        favoritesFragment = new FavoritesFragment();
        fragmentManager = getSupportFragmentManager();

        /*
        fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment, "HOME_FRAGMENT")
                .setReorderingAllowed(true)
                .addToBackStack(homeFragment.getClass().getName())
                .commit();

         */
    }

    /*
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



                    Log.d(TAG, String.valueOf(index));


                    return true;
                }
            };

        */




    private void doCall() {

        //final String data = NetworkService.INSTANCE.getUsers("https://fakestoreapi.com/products/");
        Controller controller = new Controller();
        controller.start();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String[] data = new String[] {"1", "2", "3", "4", "5", "6"};
//                viewModel.setText(data);
            }
        });

    }
}
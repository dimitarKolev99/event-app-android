package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.example.myfirstapp.controller.CustomAdapter;
import com.example.myfirstapp.network.NetworkService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {

    /*
    RecyclerView recyclerView;
    Handler mHandler = new Handler();
    CustomAdapter customAdapter;

     */
    Handler mHandler = new Handler();
    BottomNavigationView bottomNav;
    LocalBroadcastManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Thread(new Runnable() {
            @Override
            public void run() {
                doCall();
            }
        }).start();



        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_map:
                            selectedFragment = new MapFragment();
                            break;
                        case R.id.nav_favorites:
                            selectedFragment = new FavoritesFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment)
                    .commit();

                    return true;
                }
            };


    private void doCall() {

        final String data = NetworkService.INSTANCE.getUsers("https://fakestoreapi.com/products/");
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                String[] data = new String[] {"1", "2", "3", "4", "5", "6"};
                Bundle bundle = new Bundle();

                bundle.putStringArray("message", data);
                HomeFragment homeFrag = new HomeFragment();
                homeFrag.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFrag)
                        .commit();
                /*
                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
                Intent intent = new Intent("com.action.test");
                intent.putExtra("key",data);
                manager.sendBroadcast(intent);

                 */


            }
        });

    }



    /*

    public void apiCall(View v) {
        Log.v("API", "API CALLED");
        new Thread(new Runnable() {
            @Override
            public void run() {
                doCall();
            }
        }).start();
    }

     */
}
package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myfirstapp.network.NetworkService;

public class SplashActivity extends AppCompatActivity {

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doCall();
            }
        }).start();

        /*
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

         */

    }

    private void doCall() {

        final String data = NetworkService.INSTANCE.getUsers("https://fakestoreapi.com/products/");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String[] data = new String[] {"1", "2", "3", "4", "5", "6"};

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
                finish();
            }
        });

    }
}
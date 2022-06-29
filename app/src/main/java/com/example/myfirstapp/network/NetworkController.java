package com.example.myfirstapp.network;

import android.os.Build;
import android.util.Log;

import com.example.myfirstapp.model.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkController implements Callback<List<Event>> {

    static final String BASE_URL = "http://192.168.0.101:3000";

    static final String TAG = "API";

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        NetworkService networkService = retrofit.create(NetworkService.class);

        Call<List<Event>> call = networkService.getAllEvents();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
        if (response.isSuccessful()) {
            List<Event> eventList = response.body();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && eventList != null) {
                eventList.forEach(event -> Log.d(TAG, String.valueOf(event.getTitle())));
            }
        } else {
            Log.d(TAG, String.valueOf(response.errorBody()));
        }
    }

    @Override
    public void onFailure(Call<List<Event>> call, Throwable t) {
        t.printStackTrace();
    }

}

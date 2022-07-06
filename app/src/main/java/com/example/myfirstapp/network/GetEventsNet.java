package com.example.myfirstapp.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.controller.EventAdapter;
import com.example.myfirstapp.controller.SaveEventHelper;
import com.example.myfirstapp.controller.SaveEventHelperImpl;
import com.example.myfirstapp.model.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetEventsNet implements Callback<List<Event>> {

    private Context context;
    private EventAdapter eventAdapter;
    private RecyclerView recyclerView;

    public GetEventsNet(Context context,
                        SaveEventHelperImpl saveEventHelper,
                        EventAdapter eventAdapter,
                        RecyclerView recyclerView) {
        this.context = context;
        this.saveEventHelper = saveEventHelper;
        this.eventAdapter = eventAdapter;
        this.recyclerView = recyclerView;

    }

    SaveEventHelper saveEventHelper;

    public void getAllEvents() {

        Call<List<Event>> call = RetrofitClient.getInstance().getNetworkService().getAllEvents();

        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {

        if (response.isSuccessful()) {
            saveEventHelper.processResponse(response.body(), context, eventAdapter, recyclerView);
        } else {
            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
            Log.d("ERROR: ", response.message());
        }
    }

    @Override
    public void onFailure(Call<List<Event>> call, Throwable t) {
        t.printStackTrace();
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}

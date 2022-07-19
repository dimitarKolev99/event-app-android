package com.example.myfirstapp.network;

import android.content.Context;
import android.widget.Toast;

import com.example.myfirstapp.MainActivity;
import com.example.myfirstapp.model.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateEvent implements Callback<List<Event>> {

    private Context context;

    public UpdateEvent(Context context) {
        this.context = context;
    }

    public void updateEvent(Event event) {
        Call<List<Event>> call = RetrofitClient.getInstance().getNetworkService().updateEvent(event);

        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {

        if (response.code() == 200) {
            Toast.makeText(context, "Event updated successfully!", Toast.LENGTH_SHORT).show();
            MainActivity.getWebSocket().send("hi");
        } else {
            Toast.makeText(context, "Updating event failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<List<Event>> call, Throwable t) {
        t.printStackTrace();
        Toast.makeText(context, "Updating event failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}

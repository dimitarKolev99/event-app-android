package com.example.myfirstapp.network;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.myfirstapp.model.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostEventRequest implements Callback<List<Event>> {

    private Context context;

    public PostEventRequest(Context context) {
        this.context = context;
    }

    public void postEvent(Event event) {


        Call<List<Event>> call = RetrofitClient.getInstance().getNetworkService().postEvent(event);

        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
        if (response.isSuccessful()) {
            Toast.makeText(context, "Event posted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Posting event failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<List<Event>> call, Throwable t) {
        t.printStackTrace();
        Toast.makeText(context, "Posting event failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

}

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

public class PostEventRequest implements Callback<Event> {

    Context context;

    public PostEventRequest(Context context) {
        this.context = context;
    }

    public void postEvent(Event event) {

        Call<Event> call = RetrofitClient.getInstance().getNetworkService().postEvent(event);

        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Event> call, Response<Event> response) {
        if (response.isSuccessful()) {
            Event res = response.body();
            Log.d("API: ", res.getGson(res));
        } else {
            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
            Log.d("ERROR: ", response.message());
        }
    }

    @Override
    public void onFailure(Call<Event> call, Throwable t) {
        t.printStackTrace();
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
    }

}

package com.example.myfirstapp.network;

import com.example.myfirstapp.model.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {
    String BASE_URL = "http://192.168.0.101:3000/";

    @Headers({"Connection: close"})
    @GET("events")
    Call<List<Event>> getAllEvents();

    @POST("events")
    Call<List<Event>> postEvent(@Body Event event);

    @PUT("events")
    Call<List<Event>> updateEvent(@Body Event event);

    @DELETE("events/{id}")
    Call<List<Event>> deleteEvent(@Path("id") String id);

}

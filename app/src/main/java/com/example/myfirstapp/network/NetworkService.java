package com.example.myfirstapp.network;

import com.example.myfirstapp.model.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {
    String BASE_URL = "http://10.0.2.2:3000/";

    @GET("events")
    Call<List<Event>> getAllEvents();

    @POST("events")
    Call<Event> postEvent(@Body Event event);

    @PUT("events/{id}")
    Call<Event> updateEvent(@Path("id") String id, @Body Event event);

    @DELETE("events/{id}")
    Call<Event> deleteEvent(@Path("id") String id);

}

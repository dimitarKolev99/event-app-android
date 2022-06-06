package com.example.myfirstapp.network;

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
    @GET("events")
    Call<List<Event>> getAllEvents();

    @GET("events/{id}")
    Call<List<Event>> getAllEventsFromUser(@Path("id") String id);

    @POST("events")
    Call<Event> postEvent(@Body Event event);

    @PUT("events/{id}")
    Call<Event> updateEvent(@Path("id") String id, @Body Event event);

    @DELETE("events/{id}")
    Call<Event> deleteEvent(@Path("id") String id, @Body Event event);

    @DELETE("events/all/{id}")
    Call<List<Event>> deleteAllEventsFromUser(@Path("id") String id);



    @GET("users/{id}/events")
    Call<List<Event>> getEventsByUser(@Path("id") String id);

    @GET("events")
    Call<Event> getEventById(@Query("id") Integer id);








}

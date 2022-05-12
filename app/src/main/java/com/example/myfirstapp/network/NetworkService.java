package com.example.myfirstapp.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {
    @GET("events")
    Call<List<Event>> loadEvents();

    @GET("events/{id}/users")
    Call<List<User>> getUsersByEvent(@Path("id") String id);

    @GET("users/{id}/events")
    Call<List<Event>> getEventsByUser(@Path("id") String id);

    @GET("events")
    Call<Event> getEventById(@Query("id") Integer id);

    //The @Body annotation on a method parameter tells Retrofit to use the object as the request body for the call.
    @POST("users")
    Call<User> postUser(@Body User user);




}

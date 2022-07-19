package com.example.myfirstapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


import static org.junit.Assert.*;

import com.example.myfirstapp.model.Event;
import com.example.myfirstapp.network.GetRequest;
import com.example.myfirstapp.network.NetworkService;
import com.example.myfirstapp.network.RetrofitClient;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkTest {
    public static final int EVENT_ID = 0;
    public static final String EVENT_TITLE = "event1";
    public static final String EVENT_DESCRIPTION = "event_description";
    public static final int EVENT_ORGANIZER_ID = 1;
    public static final int EVENT_COUNT = 2;
    public static final String EVENT_TIME = "20:00";
    public static final String EVENT_DATE = "21.02.2022";
    public static final String EVENT_LOCATION = "Germany";
    public static final String EVENT_CREATED_AT = "now";
    public static final String EVENT_UPDATED_AT = "now";


    RetrofitClient retrofitClient;
    GetRequest getRequest;
    Event event;
    NetworkService networkService;
    MockWebServer mockWebServer;

    @Before
    public void setUp() throws IOException {
        event = new Event(EVENT_ID, EVENT_ORGANIZER_ID, EVENT_TITLE, 0, 0);
        List<Event> listToBeSent = new ArrayList<>();
        listToBeSent.add(event);
        mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setBody(new Gson().toJson(listToBeSent)).setResponseCode(200));
        mockWebServer.start();

        HttpUrl baseURL = mockWebServer.url("/");
        
        Retrofit retrofit = new Retrofit.Builder().baseUrl(String.valueOf(baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        networkService = retrofit.create(NetworkService.class);

    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testGetRequest() {

        Call<List<Event>> call = networkService.getAllEvents();
        try {
            Response<List<Event>> response = call.execute();
            List<Event> eventList = response.body();
            assertNotEquals(0, eventList.size());
            assertEquals(200, response.code());
            assertEquals(EVENT_TITLE, eventList.get(0).getTitle());
            assertEquals(EVENT_ID, eventList.get(0).getEventid());
            assertEquals(EVENT_ORGANIZER_ID, eventList.get(0).getOrganizerid());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
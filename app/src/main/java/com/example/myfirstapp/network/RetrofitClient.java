package com.example.myfirstapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private NetworkService networkService;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NetworkService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        networkService = retrofit.create(NetworkService.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }
}

package com.example.myfirstapp.network;

import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myfirstapp.MainActivity;
import com.example.myfirstapp.controller.EventAdapter;
import com.example.myfirstapp.controller.EventController;
import com.example.myfirstapp.controller.OnResponseListener;
import com.example.myfirstapp.controller.SaveEventHelper;
import com.example.myfirstapp.controller.SaveEventHelperImpl;
import com.example.myfirstapp.model.Event;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetRequest implements Callback<List<Event>> {

    public static final int GET_ALL_EVENTS = 0;
    public static final int POST_EVENT = 1;
    public static final int EDIT_EVENT = 2;
    public static final int DELETE_EVENT = 3;
    
    private OnResponseListener onResponseListener;
    

    public void performRequest(int requestType, String id, Event event) {
        Call<List<Event>> call = RetrofitClient.getInstance().getNetworkService().getAllEvents();

        /*
        switch (requestType) {
            case GET_ALL_EVENTS:
                call = RetrofitClient.getInstance().getNetworkService().getAllEvents();
            case POST_EVENT:
                call = RetrofitClient.getInstance().getNetworkService().postEvent(event);
            case EDIT_EVENT:
                call = RetrofitClient.getInstance().getNetworkService().updateEvent(event);
            case DELETE_EVENT:
                call = RetrofitClient.getInstance().getNetworkService().deleteEvent(id);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestType);
        }

         */
        
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {

        if (onResponseListener != null) {
            onResponseListener.onResponse(response.code(), response.body());
        }

    }

    @Override
    public void onFailure(Call<List<Event>> call, Throwable t) {
        t.printStackTrace();
        if (onResponseListener != null) {
            onResponseListener.onResponse(404, new ArrayList<Event>());
        }
    }

    public void setOnResponseListener(OnResponseListener listener) {
        this.onResponseListener = listener;
    }
}

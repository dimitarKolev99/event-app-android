package com.example.myfirstapp.controller;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;
import com.example.myfirstapp.model.Event;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{
    private static final String TAG = "CustomAdapter";

    private String[] mDataSet;
    private List<Event> events = new ArrayList<>();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });

            titleView = itemView.findViewById(R.id.event_title);
        }

        public TextView getTextView() {
            return titleView;
        }
    }

    public CustomAdapter(List<Event> data) {
        events = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Event currentEvent = events.get(position);
        Log.d(TAG, "Element " + position + " set.");
        holder.getTextView().setText(currentEvent.getTitle());
    }



    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

}

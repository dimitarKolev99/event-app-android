package com.example.myfirstapp.controller;

import android.app.Activity;
import android.app.UiAutomation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.MainActivity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.model.Event;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class EventAdapter extends ListAdapter<Event, EventAdapter.EventViewHolder> {
    private static final String TAG = "CustomAdapter";

    private OnItemClickListener listener;

    private final Activity ActivityObj;

    public EventAdapter(Activity activity) {
        super(DIFF_CALLBACK);
        this.ActivityObj = activity;
    }

    private static final DiffUtil.ItemCallback<Event> DIFF_CALLBACK = new DiffUtil.ItemCallback<Event>() {
        @Override
        public boolean areItemsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription());
        }
    };

    public class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView descrView;
        private final TextView dateView;
        private final TextView timeView;
        private final ImageView image;


        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onEventClick(getItem(position));
                    }

                }
            });

            titleView = itemView.findViewById(R.id.event_title);
            descrView = itemView.findViewById(R.id.event_description);
            dateView = itemView.findViewById(R.id.event_date);
            timeView = itemView.findViewById(R.id.event_time);
            image = itemView.findViewById(R.id.imageView);

        }

        public TextView getTextView() {
            return titleView;
        }

        public TextView getDescrView() {
            return descrView;
        }

        public TextView getDateView() {
            return dateView;
        }

        public TextView getTimeView() {
            return timeView;
        }

        public ImageView getImage() {
            return image;
        }
    }



    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_row, parent, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        Event currentEvent = getItem(position);

        Log.d(TAG, "Element " + position + " set.");
        holder.getTextView().setText(currentEvent.getTitle());
        holder.getDescrView().setText(currentEvent.getDescription());
        holder.getDateView().setText(currentEvent.getDate());
        holder.getTimeView().setText(currentEvent.getTime());

        loadImageFromStorage(currentEvent.getImage(), currentEvent.getImgName(), holder.getImage());
//        holder.getImage().setImageBitmap(bitmap);
    }

    private void loadImageFromStorage(String path, String imgName,ImageView imageView)
    {

        try {
            File f = new File(path, imgName);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    public Event getEventAt(int position) {
        return getItem(position);
    }

    public int getItemCount() {
        return getCurrentList().size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}

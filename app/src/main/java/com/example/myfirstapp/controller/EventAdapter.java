package com.example.myfirstapp.controller;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;
import com.example.myfirstapp.model.Event;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private static final String TAG = "CustomAdapter";

    private OnItemClickListener listener;

    List<Event> eventList = new ArrayList<Event>();

    private Context context;

    public EventAdapter(List<Event> eventList, Context context) {
        this.eventList.addAll(eventList);
        this.context = context;
    }

    public void updateEventsListItems(List<Event> events) {
        final EventDIffCallback diffCallback = new EventDIffCallback(this.eventList, events);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.eventList.clear();
        this.eventList.addAll(events);
        diffResult.dispatchUpdatesTo(this);
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView descrView;
        private final TextView dateView;
        private final TextView timeView;
        private final ImageView image;
        private Button favBtn;


        public EventViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onEventClick(getEventAt(position));
                        Toast.makeText(view.getContext(), "Clicked item" + position, Toast.LENGTH_SHORT).show();
                    }

                }
            });

            titleView = itemView.findViewById(R.id.event_title);
            descrView = itemView.findViewById(R.id.event_description);
            dateView = itemView.findViewById(R.id.event_date);
            timeView = itemView.findViewById(R.id.event_time);
            image = itemView.findViewById(R.id.imageView);
            favBtn = itemView.findViewById(R.id.favBtn);

            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Event currentEvent = getEventAt(position);
                    favBtn.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                }
            });
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
        return new EventViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        Event currentEvent = getEventAt(position);

        holder.getTextView().setText(currentEvent.getTitle());


        /*
        Log.d("image path", currentEvent.getImagePath());
        Log.d("image name", currentEvent.getImageName());

         */

        loadImageFromStorage(currentEvent.getImagePath(), currentEvent.getImageName(), holder.getImage());


    }

    private void loadImageFromStorage(String path, String imgName, ImageView imageView)
    {
        /*
        String path1 = "https://picsum.photos/id/237/200/300";
        Log.d("picasso", path+"/img"+imgName+".jpg");


         */
        File f = new File(path+"/img"+imgName+".jpg");
        Picasso.with(context).load(f).fit().centerCrop()
                .placeholder(AppCompatResources.getDrawable(context, R.drawable.ic_event_icon))
                .error(AppCompatResources.getDrawable(context, R.drawable.ic_close))
                .into(imageView);
    }

    public Event getEventAt(int position) {
        return eventList.get(position);
    }

    public int getItemCount() {
        return eventList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}

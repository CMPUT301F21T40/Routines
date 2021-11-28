package com.example.routines;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


/**
 * A custom list to add the event info
 * Purpose: To create the list that all the events will be stored in
 * Outstanding Issues: None
 * @author zezhou Xiong
 * @see Event
 */
public class EventCustomList extends ArrayAdapter<Event> {

    private ArrayList<Event> events;
    private Context context;

    public EventCustomList(Context context, ArrayList<Event> events){
        super(context, 0, events);
        this.events = events;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.event_custom_list, parent, false);
        }
        // a event list to store the event
        Event event = events.get(position);
        TextView eventName = view.findViewById(R.id.event_name);
        TextView eventDate = view.findViewById(R.id.event_date);
        TextView eventLocation = view.findViewById(R.id.event_location);

        eventName.setText(event.getEventName());
        eventDate.setText(event.getEventDate());
        eventLocation.setText(event.getEventLocation());

        return view;
    }



}


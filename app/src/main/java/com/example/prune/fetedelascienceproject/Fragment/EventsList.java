package com.example.prune.fetedelascienceproject.Fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prune.fetedelascienceproject.Adapter.EventListAdapter;
import com.example.prune.fetedelascienceproject.MainActivity;
import com.example.prune.fetedelascienceproject.R;
import com.example.prune.fetedelascienceproject.object.Events;

import java.util.List;

public class EventsList extends Fragment {
    private View view;
    private MainActivity activity;
    private RecyclerView list;
    private List<Events> events;
    private EventListAdapter adapter;

    public List<Events> getEvents() {
        return events;
    }

    public void setEventList(List<Events> eventList){
        this.events = eventList;
        if(adapter != null) {
            adapter.setList(eventList);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmenteventlist, container, false);
        activity = ((MainActivity) getActivity());
        list = view.findViewById(R.id.eventList);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new EventListAdapter(activity, events);
        list.setAdapter(adapter);
        for(Events event : events){
            event.attach(adapter);
        }
        return view;
    }
}

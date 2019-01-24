package com.example.prune.fetedelascienceproject.object;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Plan {

    private String planID;
    private List<Events> events;
    private List<String> ids;
    private String name;

    public Plan() {
        planID = UUID.randomUUID().toString();
        events = new ArrayList<>();
    }

    public void add(Events event) {
        if (!events.contains(event)) {
            events.add(event);
            Log.d("STP MARCHE", "add: Event has been added");
        }
    }

    public void remove(Events event) {
        if(events.contains(event)){
            events.remove(event);
        }

    }

    @Exclude
    public List<Events> getEvents() {
        return events;
    }

    public void setEvents(List<Events> events) {
        this.events = events;
    }

    public List<String> getids() {
        return ids;
    }

    public void ids(List<String> ids) {
        this.ids = ids;
    }

    public void setId(String planID) { this.planID = planID; }
    public String getId() {
        return planID;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void populateEvents(List<Events> allEvents) {
        events = new ArrayList<>();
        if(ids != null) {
            for(String id : ids) {
                for(Events event : allEvents) {
                    if(event.id.equals(id)) {
                        System.out.println("added");
                        events.add(event);
                    }
                }
            }
        }
    }

    public void populateids() {
        ids = new ArrayList<>();
        for(Events event : events) { ids.add(event.id); }
    }

    public boolean isInList(List<Plan> courses) {
        for(Plan course : courses) {
            if(course.planID == planID) {
                return true;
            }
        }
        return false;
    }
}

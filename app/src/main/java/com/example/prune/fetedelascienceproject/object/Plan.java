package com.example.prune.fetedelascienceproject.object;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Plan {

    private String planID;
    private List<Events> events;
    private List<String> recordids;
    private String name;

    public Plan() {
        planID = UUID.randomUUID().toString();
        events = new ArrayList<>();
    }

    public void add(Events event) {
        if (!events.contains(event)) {
            events.add(event);
        }
    }

    public void remove(Events event) {
        if(events.contains(event)){
            events.remove(event);
        }

    }


    public List<Events> getEvents() {
        return events;
    }

    public void setEvents(List<Events> events) {
        this.events = events;
    }

    public List<String> getRecordids() {
        return recordids;
    }

    public void setRecordids(List<String> recordids) {
        this.recordids = recordids;
    }

    public void setCourseid(String planID) { this.planID = planID; }
    public String getCourseid() {
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
        if(recordids != null) {
            for(String recordid : recordids) {
                for(Events event : allEvents) {
                    if(event.id.equals(recordid)) {
                        System.out.println("added");
                        events.add(event);
                    }
                }
            }
        }
    }

    public void populateRecordids() {
        recordids = new ArrayList<>();
        for(Events event : events) { recordids.add(event.id); }
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

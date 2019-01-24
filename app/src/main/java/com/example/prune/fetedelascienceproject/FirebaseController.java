package com.example.prune.fetedelascienceproject;

import android.app.Activity;
import android.util.Log;

import com.example.prune.fetedelascienceproject.object.Events;
import com.example.prune.fetedelascienceproject.object.FirebaseEvent;
import com.example.prune.fetedelascienceproject.object.Plan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FirebaseController {

    public  String eventcons(String id) {
        return "event-" + id;
    }
    public  String plancons(String id){ return "plan-"+id;}
    public  boolean isEvent(String event){ return event.startsWith("event-");}
    public boolean isPlan(String plan){return plan.startsWith("plan-");}

    public MainActivity activity;
    public DatabaseReference databaseref;
    public JsonParse jsonparser;

    public FirebaseController(MainActivity activity,JsonParse jsonparser){
        this.activity=activity;
        Log.d("SAYWHAT", "FirebaseController: ON TEST");
        databaseref= FirebaseDatabase.getInstance().getReference();
        databaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()) {
                    if(isEvent(snap.getKey())) { onEventFirebase(snap); }
                    if(isPlan(snap.getKey())) { PlanFirebase(snap); }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Err", "onCancelled: " + databaseError);
            }
        });
        this.jsonparser=jsonparser;
    }

    public void onEventFirebase(DataSnapshot snap){
        FirebaseEvent ev = snap.getValue(FirebaseEvent.class);
        if(ev != null) {
            for (Events event : jsonparser.getEvents()) {
                if (event.id.equals(ev.id)) {
                    event.setEventFirebase(ev);
                }
            }
        }
    }
    public void saveEventFirebase(FirebaseEvent event) {
        databaseref.child(eventcons(event.id)).setValue(event);
    }

    public void PlanFirebase (DataSnapshot snap){
        Plan course = snap.getValue(Plan.class);
        if(course != null) {
            activity.addPlan(course);
        }
    }
    public void savePlanFirebase(Plan p){
        p.populateids();
        databaseref.child(plancons(p.getId())).setValue(p);
    }


}

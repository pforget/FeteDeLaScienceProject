package com.example.prune.fetedelascienceproject.object;

import com.google.android.gms.maps.model.LatLng;

import java.util.Observer;

public class Events {


    public String title;
    public String id;
    public String description;
    public String descriptionbig;
    public String zelda;
    public double[] geolocalisation;
    public String image;
    public String apercu;

    private Observer observer;
    public void attach(Observer observer) {
        this.observer = observer;
    }
    public LatLng location;

    private FirebaseEvent eventFirebase;

    public FirebaseEvent getEventFirebase() {
        return eventFirebase;
    }

    public void setEventFirebase(FirebaseEvent eventFirebase) {
        this.eventFirebase = eventFirebase;
        if(observer != null){
            observer.update(null, this);
        }
    }

    public boolean hasGeolocalisation() {
        return (geolocalisation != null) && (geolocalisation.length >= 2);
    }
}

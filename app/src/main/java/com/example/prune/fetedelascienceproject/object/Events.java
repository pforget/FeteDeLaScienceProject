package com.example.prune.fetedelascienceproject.object;

import com.google.android.gms.maps.model.LatLng;

import java.util.Observer;

public class Events {

    public String id;
    public class Fields {
        public String title;

        public String description;
        public String descriptionbig;
        public String zelda;
        public double[] geolocalisation;
        public String image;
        public String apercu;
    }
    public Events.Fields fields;

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
        return (fields.geolocalisation != null) && (fields.geolocalisation.length >= 2);
    }
}

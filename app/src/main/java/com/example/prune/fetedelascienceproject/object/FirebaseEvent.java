package com.example.prune.fetedelascienceproject.object;

public class FirebaseEvent {
    public String id;
    public int nbVotes;
    public float rating;
    public int remaining;

    public FirebaseEvent() { }

    public FirebaseEvent(String recordid, int nbVotes, float rating, int remaining) {
        this.id = recordid;
        this.nbVotes = nbVotes;
        this.rating = rating;
        this.remaining = remaining;
    }
}

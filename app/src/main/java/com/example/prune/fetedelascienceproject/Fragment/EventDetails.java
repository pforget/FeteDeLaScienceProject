package com.example.prune.fetedelascienceproject.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prune.fetedelascienceproject.FirebaseController;
import com.example.prune.fetedelascienceproject.R;
import com.example.prune.fetedelascienceproject.MainActivity;
import com.example.prune.fetedelascienceproject.object.Events;
import com.example.prune.fetedelascienceproject.object.FirebaseEvent;
import com.squareup.picasso.Picasso;

public class EventDetails extends Fragment {

    private View view;
    private MainActivity activity;
    private int position;
    private Events event;
    private TransportMode transportMode = TransportMode.DRIVE;
    private boolean isManager = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmenteventdetails, container, false);
        activity = ((MainActivity) getActivity());
        NumberPicker np = view.findViewById(R.id.remainingPlacesNumberPicker);
        np.setMinValue(0);
        np.setMaxValue(1000);
        np.setWrapSelectorWheel(false);
        view.findViewById(R.id.applyButton).setOnClickListener(
                (view) -> {
                    event.getEventFirebase().remaining = np.getValue();
                    ((TextView) this.view
                            .findViewById(R.id.remainingPlacesTextView))
                            .setText("" + event.getEventFirebase().remaining);
                    activity.getFirebaseController().saveEventFirebase(event.getEventFirebase());
                    Toast.makeText(activity, "Remaining places set to " + event.getEventFirebase().remaining, Toast.LENGTH_LONG).show();
                });
        view.findViewById(R.id.planEvent).setOnClickListener(v -> {
            activity.addToPlan(event);
        });
        return view;
    }

    public void setPos(int position) {
        this.position = position;
        setEvent(activity.getJsonParser().getEvents().get(position));
    }

    public void setEvent(Events event) {
        this.event = event;
        if(!event.hasGeolocalisation()) {
            view.findViewById(R.id.routeLinearLayout).setVisibility(View.GONE);
            view.findViewById(R.id.warningText).setVisibility(View.VISIBLE);
        }
        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(event != null) {
            ((TextView) view.findViewById(R.id.titleEvent)).setText(event.fields.title);
            String desc = (event.fields.descriptionbig != null) ?
                    event.fields.descriptionbig : event.fields.descriptionbig;
            ((TextView) view.findViewById(R.id.descriptionEvent)).setText(desc);
            Picasso.with(getActivity()).load(event.fields.image).into((ImageView) view.findViewById(R.id.imageEvent));
            view.findViewById(R.id.rateButton).setEnabled(true);
            //Rating initialise with EventFirebase
            if(event.getEventFirebase() == null) {
                FirebaseEvent eventFirebase = new FirebaseEvent();
                eventFirebase.id = event.id;
                eventFirebase.nbVotes = 0;
                eventFirebase.rating = 0;
                eventFirebase.remaining = -1;
                event.setEventFirebase(eventFirebase);
            }
            ((RatingBar) view.findViewById(R.id.ratingEvent)).setRating(event.getEventFirebase().rating);
            ((TextView) view.findViewById(R.id.numberVote)).setText(event.getEventFirebase().nbVotes + " avis" );
            if(event.getEventFirebase().remaining != -1){
                ((NumberPicker) view.findViewById(R.id.remainingPlacesNumberPicker)).setValue(this.event.getEventFirebase().remaining);
                ((TextView) view
                        .findViewById(R.id.remainingPlacesTextView))
                        .setText("" + event.getEventFirebase().remaining);
            }else{
                ((NumberPicker) view.findViewById(R.id.remainingPlacesNumberPicker)).setValue(0);
                ((TextView) view.findViewById(R.id.remainingPlacesTextView)).setText("idk");
            }

        }
    }

    public void share() {
        String name = getResources().getString(R.string.app_name);
        String subject = name + " : " + event.fields.title;
        String body = subject + "\n" + event.fields.zelda;
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(intent, "Share using"));
    }

    public Events getEvent() {
        return event;
    }

    public TransportMode getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(TransportMode transportMode) {
        this.transportMode = transportMode;
    }

    public void setRate(FirebaseController Firebase){
        event = getEvent();
        int nbVotesf = event.getEventFirebase().nbVotes + 1;
        event.getEventFirebase().rating = (((RatingBar) view.findViewById(R.id.ratingEvent)).getRating() + (event.getEventFirebase().rating * event.getEventFirebase().nbVotes)) / nbVotesf;
        event.getEventFirebase().nbVotes = nbVotesf;
        Firebase.saveEventFirebase(event.getEventFirebase());
        ((RatingBar) view.findViewById(R.id.ratingEvent)).setRating(event.getEventFirebase().rating);
        ((TextView) view.findViewById(R.id.numberVote)).setText(event.getEventFirebase().nbVotes);
        view.findViewById(R.id.rateButton).setEnabled(false);
    }

    public void setManager() {
        isManager = !isManager;
        view.findViewById(R.id.remainingPlacesNumberPicker).setVisibility(isManager ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.remainingPlacesTextView).setVisibility(isManager ? View.GONE : View.VISIBLE);
        view.findViewById(R.id.applyButton).setVisibility(isManager ? View.VISIBLE : View.GONE);
    }

    public enum TransportMode{
        DRIVE,
        WALK,
        BIKE
    }
}

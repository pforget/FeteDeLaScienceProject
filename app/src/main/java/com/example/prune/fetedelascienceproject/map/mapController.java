package com.example.prune.fetedelascienceproject.map;

import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.example.prune.fetedelascienceproject.Fragment.EventsList;
import com.example.prune.fetedelascienceproject.MainActivity;
import com.example.prune.fetedelascienceproject.object.Events;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.SortedMap;
import java.util.TreeMap;

public class mapController implements OnMapReadyCallback {

    private final LocationManager locationManager;
    private final EventsList eventList;
    private final MainActivity activity;
    private double maxDist = 10000000;
    private Location currentLocation;
    private int maxEvents = 100;
    private GoogleMap googleMap;

    public Events centerOnEvent = null;

    public mapController(MainActivity activity, LocationManager locationManager, EventsList eventList) {
        this.locationManager = locationManager;
        this.eventList = eventList;
        this.activity = activity;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        try {
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if(currentLocation == null){
                currentLocation = new Location(LocationManager.GPS_PROVIDER);
                currentLocation.setLongitude(2.3488);
                currentLocation.setLatitude(48.8534);
            }
            this.googleMap.setMyLocationEnabled(true);
            this.googleMap.setOnCameraIdleListener(this::handleIdleCamera);
        }catch(SecurityException e){
            e.printStackTrace();
        }

        this.googleMap.setOnMarkerClickListener(this::addTagDataToFrag);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        SortedMap<Double, Events> distanceMap = getSortedDistanceMap(currentLocation);

        addToGoogleMap(distanceMap, builder);

        if(centerOnEvent != null){
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerOnEvent.location, 15));
            centerOnEvent = null;
        }else {
            try {
                LatLngBounds bounds = builder.build();
                int padding = 50; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                this.googleMap.moveCamera(cu);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Toast.makeText(activity, "No event was found closer to " + maxDist + " meters around you", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean addTagDataToFrag(Marker marker) {
        activity.displayEventDetails((Events) marker.getTag());
        return true;
    }

    private SortedMap<Double, Events> getSortedDistanceMap(Location locationTo){
        SortedMap<Double, Events> sortedMap = new TreeMap<>();

        Location location = new Location(LocationManager.GPS_PROVIDER);
        for(Events event : eventList.getEvents()){
            if(event.hasGeolocalisation()) {
                LatLng latLng = new LatLng(event.fields.geolocalisation[0], event.fields.geolocalisation[1]);
                location.setLatitude(latLng.latitude);
                location.setLongitude(latLng.longitude);
                double distance = location.distanceTo(locationTo);
                event.location = latLng;
                if(distance < maxDist){
                    sortedMap.put(distance, event);
                }
            }
        }
        return sortedMap;
    }

    private void addToGoogleMap(SortedMap<Double, Events> distanceMap, LatLngBounds.Builder builder){
        int maxEventsTemp = maxEvents;
        for(Events event : distanceMap.values()){
            if(maxEventsTemp-- <= 0){
                return;
            }
            Marker marker = googleMap.addMarker(new MarkerOptions().position(event.location).title(event.fields.title));
            marker.setTag(event);
            if(builder != null){
                builder.include(event.location);
            }
        }
    }

    private void handleIdleCamera() {
        addClosestToMapCenter(null);
    }

    private void addClosestToMapCenter(LatLngBounds.Builder builder) {
        Location locationTarget = new Location(LocationManager.GPS_PROVIDER);
        googleMap.clear();
        LatLng latLngTarget = googleMap.getCameraPosition().target;
        locationTarget.setLatitude(latLngTarget.latitude);
        locationTarget.setLongitude(latLngTarget.longitude);
        SortedMap<Double, Events> distanceMap = getSortedDistanceMap(locationTarget);
        addToGoogleMap(distanceMap, builder);
    }

    public void handleSearchSubmit(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        addClosestToMapCenter(builder);
        try {
            LatLngBounds bounds = builder.build();
            int padding = 50; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            this.googleMap.moveCamera(cu);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Toast.makeText(activity, "No event matching the query was found  closer to " + maxDist + " meters around you", Toast.LENGTH_LONG).show();
        }
    }
}

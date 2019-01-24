package com.example.prune.fetedelascienceproject;

import android.Manifest;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.prune.fetedelascienceproject.map.mapController;
import com.example.prune.fetedelascienceproject.object.*;
import com.example.prune.fetedelascienceproject.Fragment.*;
import com.google.android.gms.maps.MapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private JsonParse jsonParser;
    private FirebaseAuth mauth;
    private FirebaseController firebaseController;
    private MapFragment mMapFragment;
    private EventsList eventList;
    private PlanList planList;
    private EventDetails eventDetails;
    private FirebaseUser user;
    private LocationManager locationManager;
    private mapController map;
    private boolean isFirst = true;
    private boolean isSharable;

    private Plan plan;
    private List<Plan> plans;

    public JsonParse getJsonParser() {
        return jsonParser;
    }

    public FirebaseController getFirebaseController(){return firebaseController;}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Check permissions and ask for it
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        setContentView(R.layout.activity_main);
        jsonParser = new JsonParse(getAssets(),"jsonsciencecorrect.json");
        firebaseController = new FirebaseController(this,jsonParser);
       // toolbar = findViewById(R.id.mainToolbar);
        //setSupportActionBar(toolbar);
        plan = new Plan();
        mauth = FirebaseAuth.getInstance();
        plans = new ArrayList<>();
        eventList = new EventsList();
        eventList.setEventList(jsonParser.getEvents());
        planList = new PlanList();
        planList.setEventList(plan.getEvents());
        eventDetails = new EventDetails();
        mMapFragment = MapFragment.newInstance();
        System.out.println("Je suis passé par la new instance");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        map = new mapController(this, locationManager, eventList);
        user = mauth.getCurrentUser();
        displayEventList();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        for(int i = 0; i < permissions.length; i++){
            if(permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[i] == -1) {
                System.exit(2);
            }
        }
    }
    public void addPlan(Plan plan) {
        if(!plan.isInList(plans)) {
            plan.populateEvents(jsonParser.getEvents());
            plans.add(plan);
            planList.setPlanList(plans);
        }

    }
    public void displayEventList() {
        isSharable = false;
        displayFullFragment(eventList);
    }
    private void displayFullFragment(Fragment fragment) {
        if(!fragment.isVisible()) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Log.d("DisplayFragment","je suis passé la");
            transaction.replace(R.id.fragment_main, fragment);
            if(isFirst) {
                isFirst = false;
            }else{
                transaction.addToBackStack(null);
            }
            transaction.commit();
            getFragmentManager().executePendingTransactions();
        }
    }

    public void displayEventDetails(Events event){
        isSharable = true;
        displayFullFragment(eventDetails);
        eventDetails.setEvent(event);
    }
    public void displayEventDetails(int pos) {
        isSharable = true;
        displayFullFragment(eventDetails);
        eventDetails.setPos(pos);
    }
    public void displayPlanList() {
        isSharable = false;
        displayFullFragment(planList);
    }
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            jsonParser.setQuery(query);
            displayEventList();
        } else {
            jsonParser.setQuery(null);
            displayEventList();
        }
    }
    @Override protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    public void displayMap(){
        isSharable = false;
        displayFullFragment(mMapFragment);
        mMapFragment.getMapAsync(map);
    }
    public void DisplayOnMap(View view) {
        displayMap();
        map.centerOnEvent = eventDetails.getEvent();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.toolbar_search).getActionView();
        ComponentName name = new ComponentName(getApplicationContext(), MainActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(name));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {
                if(mMapFragment.isVisible()){
                    map.handleSearchSubmit();
                }
                return true;
            }
            @Override public boolean onQueryTextChange(String query) {
                if(query.isEmpty()) {
                    jsonParser.setQuery(null);
                    if (mMapFragment.isVisible()) {
                        map.handleSearchSubmit();
                    }
                }else {
                    jsonParser.setQuery(query);
                }
                eventList.setEventList(jsonParser.getEvents());
                return true;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_list : displayEventList();break;
            case R.id.toolbar_map : displayMap();break;
            case R.id.toolbar_share :  eventDetails.share();break;
            case R.id.toolbar_manager : eventDetails.setManager();break;
            case R.id.toolbar_plans:  displayPlanList();break;
            case R.id.toolbar_signout : mauth.signOut();finish();

        }return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.toolbar_share).setVisible(isSharable);
        Log.d("IsUserAdmin", user.getEmail());
        menu.findItem(R.id.toolbar_manager).setVisible(eventDetails.isVisible() && user.getEmail().equals("admin@admin.fr"));
        return true;
    }

    public void intentGMRoute(View view){

        char transportMode;
        switch(eventDetails.getTransportMode()){
            case WALK: transportMode = 'w'; break;
            case BIKE: transportMode = 'b'; break;
            case DRIVE:
            default:transportMode = 'd';
        }
        Events event = eventDetails.getEvent();
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+event.fields.geolocalisation[0]+","+event.fields.geolocalisation[1]+"&mode="+transportMode);
        Intent i = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        i.setPackage("com.google.android.apps.maps");
        startActivity(i);
    }
    public void onRateClicked(View view) {
        eventDetails.setRate(firebaseController);
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.driveRadioButton:
                if (checked)
                    eventDetails.setTransportMode(EventDetails.TransportMode.DRIVE);
                break;
            case R.id.walkRadioButton:
                if (checked)
                    eventDetails.setTransportMode(EventDetails.TransportMode.WALK);
                break;
            case R.id.bikeRadioButton:
                if (checked)
                    eventDetails.setTransportMode(EventDetails.TransportMode.BIKE);
                break;
        }
    }

    public void addToPlan(Events event) {
        if(!plan.getEvents().contains(event)) {
            plan.add(event);
            Toast.makeText(this, "Event added to plan.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Event already in plan.", Toast.LENGTH_LONG).show();
        }
        planList.setEventList(plan.getEvents());
    }

    public void removeFromPlan(Events event) {
        plan.remove(event);
        planList.setEventList(plan.getEvents());
    }

    public boolean publishPlan(String name) {
        Log.d("IciOuLabas", "publishPlan: Try to publish" + name + plan.getEvents().size());
        if((plan.getEvents().size() > 0) && (name != "")) {
            Toast.makeText(this, "Plan posted.", Toast.LENGTH_LONG).show();
            plan.setName(name);
            firebaseController.savePlanFirebase(plan);
            plan = new Plan();
            planList.setEventList(plan.getEvents());
            return true;
        } else {
            Toast.makeText(this, "plan not posted", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}

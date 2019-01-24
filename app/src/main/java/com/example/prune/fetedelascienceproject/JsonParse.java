package com.example.prune.fetedelascienceproject;

import android.content.res.AssetManager;
import android.util.Log;

import com.example.prune.fetedelascienceproject.object.Events;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonParse {

    private AssetManager assets;
    private Gson gson;
    private String path;
    private List<Events> events;
    private String query;

    public JsonParse(AssetManager assets, String path){
        this.assets=assets;
        this.path=path;
        gson=new Gson();
        events = GetJsonEvent();
    }
    public List<Events> getEvents() {
        if(query == null) {
            return events;
        } else {
            List<Events> result = new ArrayList<>();
            for(Events event : events) {
                String title = event.fields.title.toLowerCase();
                String q = query.toLowerCase();
                if(title.contains(q)) {
                    result.add(event);
                }
            }
            return result;
        }
    }

    public void setQuery(String query) { this.query = query; }
    private String ParseJsonToString() {
        String json = null;
        try {
            InputStream stream = assets.open(path);
            int size = stream.available();
            Log.d("SIZE", String.valueOf(size));
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d("erreur", ex.getMessage());
        }
        Log.d("Jsonnombre", json);
        return json;
    }

    private List<Events> GetJsonEvent() {
        List<Events> events = new ArrayList<>();
        for(Events event : Arrays.asList(gson.fromJson(ParseJsonToString(), Events[].class))) {
            if(event.fields.title != null) { events.add(event); }
        }
        Log.d("Nombre event trouver",String.valueOf(events.size()));
        return events;
    }
}

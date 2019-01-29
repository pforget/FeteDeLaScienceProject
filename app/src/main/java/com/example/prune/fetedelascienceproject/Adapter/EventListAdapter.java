package com.example.prune.fetedelascienceproject.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prune.fetedelascienceproject.MainActivity;
import com.example.prune.fetedelascienceproject.R;
import com.example.prune.fetedelascienceproject.object.Events;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class EventListAdapter extends Lists<Events> implements Observer {
    private Map<Events, ViewHolder> eventToViewHolder = new HashMap<>();

    public EventListAdapter(Context context, List<Events> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Events event = list.get(position);
        ((TextView) holder.view.findViewById(R.id.titre)).setText(event.fields.title);
        Log.d("eventlistadapter", "Description event " + event.fields.description);
        ((TextView) holder.view.findViewById(R.id.description)).setText(event.fields.description);
        if(event.fields.apercu != null) {
            Log.d("tryit", "Je suis passé dans le non null " + event.fields.apercu);
            Picasso.with(context).load(event.fields.apercu).into((ImageView) holder.view.findViewById(R.id.image));
        }else{
            holder.view.findViewById(R.id.image).setVisibility(View.GONE);
        }
        holder.view.setOnClickListener(l -> ((MainActivity) context).displayEventDetails(position));
        eventToViewHolder.put(event, holder);
    }

    @Override
    public int inflateId() {
        return R.layout.fragmenteventlist_item;
    }

    @Override
    public void update(Observable o, Object arg) {
    }

}

package com.example.prune.fetedelascienceproject.Adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.prune.fetedelascienceproject.R;
import com.example.prune.fetedelascienceproject.object.Events;
import com.example.prune.fetedelascienceproject.object.Plan;

import java.util.List;

public class PlansAdapter extends Lists<Plan> {

    public PlansAdapter(Context context, List<Plan> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Plan plan = list.get(position);
        ((TextView) holder.view.findViewById(R.id.name)).setText(plan.getName());

        String titles = "";
        for(Events e : plan.getEvents()) { titles += e.fields.title + "\n"; }
        ((TextView) holder.view.findViewById(R.id.events)).setText(titles);
    }

    @Override
    public int inflateId() {
        return R.layout.fragmentplans;
    }
}


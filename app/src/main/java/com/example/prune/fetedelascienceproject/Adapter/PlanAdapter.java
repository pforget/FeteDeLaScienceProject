package com.example.prune.fetedelascienceproject.Adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.prune.fetedelascienceproject.MainActivity;
import com.example.prune.fetedelascienceproject.R;
import com.example.prune.fetedelascienceproject.object.Events;

import java.util.List;

public class PlanAdapter extends Lists<Events> {

    public PlanAdapter(Context context, List<Events> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Events event = list.get(position);
        ((TextView) holder.view.findViewById(R.id.titre)).setText(event.fields.title);
        holder.view.findViewById(R.id.remove).setOnClickListener(l -> {
            ((MainActivity) context).removeFromPlan(event);
        });
    }

    @Override
    public int inflateId() {
        return R.layout.fragmentplan;
    }
}



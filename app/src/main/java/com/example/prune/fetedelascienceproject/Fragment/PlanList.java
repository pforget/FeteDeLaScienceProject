package com.example.prune.fetedelascienceproject.Fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.prune.fetedelascienceproject.Adapter.PlanAdapter;
import com.example.prune.fetedelascienceproject.Adapter.PlansAdapter;
import com.example.prune.fetedelascienceproject.MainActivity;
import com.example.prune.fetedelascienceproject.R;
import com.example.prune.fetedelascienceproject.object.Events;
import com.example.prune.fetedelascienceproject.object.Plan;

import java.util.List;

public class PlanList extends Fragment {

    private View view;
    private MainActivity activity;
    private List<Events> events;
    private PlanAdapter pAdapter;
    private List<Plan> plans;
    private PlansAdapter plansAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmentplanlist, container, false);
        activity = ((MainActivity) getActivity());
        RecyclerView myList = view.findViewById(R.id.Myplans);
        myList.setLayoutManager(new LinearLayoutManager(getActivity()));
        pAdapter = new PlanAdapter(activity, events);
        myList.setAdapter(pAdapter);

        ((Button) view.findViewById(R.id.planPOst)).setOnClickListener(v -> {
            String name = ((EditText) view.findViewById(R.id.planName)).getText().toString();
            boolean published = activity.publishPlan(name);
            if(published) {
                ((EditText) view.findViewById(R.id.planName)).getText().clear();
            }
        });

        RecyclerView allList = view.findViewById(R.id.plans_all);
        allList.setLayoutManager(new LinearLayoutManager(getActivity()));
        plansAdapter = new PlansAdapter(activity, plans);
        if(plans != null) { plansAdapter.setList(plans); }
        allList.setAdapter(plansAdapter);

        return view;
    }

    public void setEventList(List<Events> events){
        this.events = events;
        if(pAdapter != null) { pAdapter.setList(events); }
    }

    public void setPlanList(List<Plan> courses){
        this.plans = courses;
        if(plansAdapter != null) { plansAdapter.setList(courses); }
    }

}

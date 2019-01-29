package com.example.prune.fetedelascienceproject.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class Lists <T> extends RecyclerView.Adapter<Lists.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }

    protected Context context;
    protected List<T> list;

    public Lists(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(inflateId(), parent, false));
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }

    public void setList(List<T> list) {
        this.list = list;
        System.out.println(list);
        notifyDataSetChanged();
    }

    public abstract int inflateId();
}



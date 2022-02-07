package com.shiveluch.nuclearwinter;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ActionsAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Info> objects;

    protected ListView mListView;
    public ActionsAdapter(Context context, ArrayList<Info> stalkers, Activity activity) {
        super();
        ctx = context;
        objects = stalkers;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = activity.findViewById(R.id.stalkerslist);
    }

    // ???-?? ?????????
    @Override
    public int getCount() {
        return objects.size();
    }

    // ??????? ?? ???????
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.actionsitem, parent, false);
        }

        Info p = getStalkers(position);
        ((TextView) view.findViewById(R.id.actionitem)).setText(p.name);
        if (p.description.equals("1"))
        {
            ((TextView) view.findViewById(R.id.actionitem)).setTextColor(Color.parseColor("#00ff00"));
        }
        if (p.description.equals("0"))
        {
            ((TextView) view.findViewById(R.id.actionitem)).setTextColor(Color.parseColor("#ffffff"));
        }


        return view;
    }

    Info getStalkers(int position) {
        return ((Info) getItem(position));
    }

}
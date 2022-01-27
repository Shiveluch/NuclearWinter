package com.shiveluch.nuclearwinter;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class GroupLocAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<GroupLocations> objects;

    protected ListView mListView;
    public GroupLocAdapter(Context context, ArrayList<GroupLocations> stalkers, Activity activity) {
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
            view = lInflater.inflate(R.layout.stalkersadapter, parent, false);
        }

       GroupLocations p = getStalkers(position);
        ((TextView) view.findViewById(R.id.infoname)).setText(p.playername);
        ((TextView) view.findViewById(R.id.p_passcode)).setText(p.passcode);
        ((ImageView) view.findViewById(R.id.infopic)).setImageResource(p.picture);
        ((TextView) view.findViewById(R.id.p_lat)).setText(p.lat);
        ((TextView) view.findViewById(R.id.p_lon)).setText(p.lon);
        return view;
    }

    GroupLocations getStalkers(int position) {
        return ((GroupLocations) getItem(position));
    }

}
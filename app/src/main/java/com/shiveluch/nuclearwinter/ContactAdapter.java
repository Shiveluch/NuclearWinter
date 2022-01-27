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

public class ContactAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Contacts> objects;

    protected ListView mListView;
    public ContactAdapter(Context context, ArrayList<Contacts> stalkers, Activity activity) {
        super();
        ctx = context;
        objects = stalkers;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
            view = lInflater.inflate(R.layout.contactitem, parent, false);
        }

        Contacts p = getStalkers(position);
        ((TextView) view.findViewById(R.id.contact)).setText("+ "+p.name);
        if (p.color==0)
        {
            ((TextView) view.findViewById(R.id.contact)).setTextColor(Color.parseColor("#ffffff"));

        }

        if (p.color==1)
        {
            ((TextView) view.findViewById(R.id.contact)).setTextColor(Color.parseColor("#5af8b7"));

        }
     return view;
    }

    Contacts getStalkers(int position) {
        return ((Contacts) getItem(position));
    }

}
package com.shiveluch.nuclearwinter;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MonsterDesc extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Info> objects;

    public MonsterDesc(Context context, ArrayList<Info> stalkers, Activity activity) {
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
            view = lInflater.inflate(R.layout.actionsitem, parent, false);
        }

        Info p = getStalkers(position);
        span (view.findViewById(R.id.actionitem),p.name);
        //((TextView) view.findViewById(R.id.actionitem)).setText(p.name);


        return view;
    }

    Info getStalkers(int position) {
        return ((Info) getItem(position));
    }

    private void span(TextView tv, String data) {
       SpannableString text2 = new SpannableString(data);
       int pos= data.indexOf(':');
       text2.setSpan(new ForegroundColorSpan(Color.GREEN), pos+1, data.length(), 0);
       tv.setText(text2, TextView.BufferType.SPANNABLE);
    }
}
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

public class BattleLogAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<BattleItem> objects;
    public BattleLogAdapter(Context context, ArrayList<BattleItem> stalkers) {
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
            view = lInflater.inflate(R.layout.battlelogitem, parent, false);
        }

        BattleItem p = getStalkers(position);
        ((TextView) view.findViewById(R.id.moveis)).setText(p.move);
        ((TextView) view.findViewById(R.id.log)).setText(p.log);


        return view;
    }

    BattleItem getStalkers(int position) {
        return ((BattleItem) getItem(position));
    }

}
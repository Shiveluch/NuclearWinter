package com.shiveluch.nuclearwinter;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Posts> objects;
    TextView arrow;
    protected ListView mListView;
    public ChatAdapter(Context context, ArrayList<Posts> posts, Activity activity) {
        super();
        ctx = context;
        objects = posts;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = activity.findViewById(R.id.chat);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
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
            view = lInflater.inflate(R.layout.chatadapter, parent, false);
        }

        Posts p = getPosts(position);
        ((TextView) view.findViewById(R.id.p_time)).setText(p.time);
        ((TextView) view.findViewById(R.id.p_passcode)).setText(p.text);
        ((TextView) view.findViewById(R.id.infoname)).setText(p.playername);
        ((TextView) view.findViewById(R.id.p_id)).setText(p.id);
       return view;
    }

   Posts getPosts(int position) {
        return ((Posts) getItem(position));
    }

}
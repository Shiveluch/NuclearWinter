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

public class MenuAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Menu> objects;

    protected ListView mListView;
    public MenuAdapter(Context context, ArrayList<Menu> menu, Activity activity) {
        super();
        ctx = context;
        objects = menu;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = activity.findViewById(R.id.menu);
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
            view = lInflater.inflate(R.layout.menuadapter, parent, false);
        }

        Menu p = getMenu(position);
        ((TextView) view.findViewById(R.id.infoname)).setText(p.p_menu);
        ((TextView) view.findViewById(R.id.description)).setText(p.p_script);

        return view;
    }

    Menu getMenu(int position) {
        return ((Menu) getItem(position));
    }

}
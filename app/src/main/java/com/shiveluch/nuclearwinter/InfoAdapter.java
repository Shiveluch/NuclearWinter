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

import com.squareup.picasso.Picasso;

public class InfoAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Info> objects;

    protected ListView mListView;
    public InfoAdapter(Context context, ArrayList<Info> info, Activity activity) {
        super();
        ctx = context;
        objects = info;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = activity.findViewById(R.id.info);
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
            view = lInflater.inflate(R.layout.infoadapter, parent, false);
        }
        Context context= ctx.getApplicationContext();
        String url="http://a0568345.xsph.ru/winter/res/";

        Info p = getInfo(position);
        ((TextView) view.findViewById(R.id.infoname)).setText(p.name);
        ((TextView) view.findViewById(R.id.description)).setText(p.description);
        Picasso.with(context)
                .load(url+p.image)
                .placeholder(R.drawable.error)
                .error(R.drawable.error)
                .into( ((ImageView) view.findViewById(R.id.infopic)));

        return view;
    }

    Info getInfo(int position) {
        return ((Info) getItem(position));
    }

}

package com.shiveluch.nuclearwinter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class fight extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_fight);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        loadActionsList();
        loadMonsterChar();
        TextView distanceleft=findViewById(R.id.distanceleft);
        span("Дистанция до цели: 250 м.", distanceleft, 18);

    }

    private void span(String _text, TextView _tv, int _start)
    {
        String text2 = _text;
        Spannable spannable = new SpannableString(text2);
        spannable.setSpan(new ForegroundColorSpan(Color.GREEN), _start, _text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        _tv.setText(spannable, TextView.BufferType.SPANNABLE);

    }

    private void loadActionsList() {
        ArrayList<Info> actions = new ArrayList<>();
        actions.add(new Info("Сменить позицию","",""));
        actions.add(new Info("Выстрел из основного оружия","",""));
        actions.add(new Info("Выстрел из вспомогательного оружия","",""));
        actions.add(new Info("Использовать аптечку","",""));
        actions.add(new Info("Использовать бинт","",""));
        ActionsAdapter adapter = new ActionsAdapter(this,actions,this);
        ListView actionsList=findViewById(R.id.actionslist);
        actionsList.setAdapter(adapter);
        actionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Action", actions.get(i).name );
            }
        });
    }

    private void loadMonsterChar() {
        ArrayList<Info> monsterchar = new ArrayList<>();
        monsterchar.add(new Info("Скорость: 10 метров","",""));
        monsterchar.add(new Info("Дистанция атаки: 10 метров","",""));
        monsterchar.add(new Info("Урон за 1 атаку: до 35%","",""));
        monsterchar.add(new Info("Здоровье: 100%","",""));


        ActionsAdapter adapter = new ActionsAdapter(this,monsterchar,this);
        ListView monsterchars=findViewById(R.id.monsterchar);
        monsterchars.setAdapter(adapter);
        monsterchars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Action", monsterchar.get(i).name );
            }
        });
    }
}
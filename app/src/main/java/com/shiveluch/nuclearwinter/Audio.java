package com.shiveluch.nuclearwinter;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Random;

public class Audio {

    MediaPlayer mp;
    Context context;

    public Audio(Context ct) {
        this.context = ct;
    }

    public void playClick() {
        mp = MediaPlayer.create(context, R.raw.signal);

        mp.start();
    }

    public void playDetector() {
        mp = MediaPlayer.create(context, R.raw.detector);
        mp.start();
    }

    public void playStart() {
        mp = MediaPlayer.create(context, R.raw.sidor);
        mp.start();
    }

    public void playGreeting()
    {
        ArrayList<Integer> sounds = new ArrayList<>();
        sounds.add(R.raw.greeting1);
        sounds.add(R.raw.greeting2);
        sounds.add(R.raw.greeting3);
        sounds.add(R.raw.greeting4);
        sounds.add(R.raw.greeting5);
        sounds.add(R.raw.greeting6);
        sounds.add(R.raw.greeting7);
        sounds.add(R.raw.greeting8);
        sounds.add(R.raw.greeting9);
        Random rnd = new Random();
        int play = rnd.nextInt(sounds.size());
        mp = MediaPlayer.create(context, sounds.get(play));
        mp.start();

    }

    public void playSidor()
    {
        ArrayList<Integer> sounds = new ArrayList<>();
        sounds.add(R.raw.sidors1);
        sounds.add(R.raw.sidors2);
        sounds.add(R.raw.sidors3);
        Random rnd = new Random();
        int play = rnd.nextInt(sounds.size());
        mp = MediaPlayer.create(context, sounds.get(play));
        mp.start();

    }

    public void playSah()
    {
        ArrayList<Integer> sounds = new ArrayList<>();
        sounds.add(R.raw.saharov);
        Random rnd = new Random();
        int play = rnd.nextInt(sounds.size());
        mp = MediaPlayer.create(context, sounds.get(play));
        mp.start();

    }

    public void playMutant(int id)
    {
        ArrayList<Integer> sounds = new ArrayList<>();
        sounds.add(id);
        Random rnd = new Random();
        int play = rnd.nextInt(sounds.size());
        mp = MediaPlayer.create(context, sounds.get(play));
        mp.start();

    }

    public void shot(int shotid)
    {
        mp = MediaPlayer.create(context, shotid);
        mp.start();

    }
}
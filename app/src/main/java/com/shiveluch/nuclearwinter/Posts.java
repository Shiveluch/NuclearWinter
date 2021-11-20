package com.shiveluch.nuclearwinter;

public class Posts {
    String playername;
    String text;
    String time;
    String id;
   int color;

    public Posts(String _id, String _playername, String _text,String _time, int _color) {
        playername = _playername;
        text = _text;
        time = _time;
        color=_color;
        id=_id;
    }

}

package com.shiveluch.nuclearwinter;

public class GroupLocations {
    String playername;
    String passcode;
    String lat,lon;
    int picture;
    double distance;


    public GroupLocations(String _playername, String _passcode, int _picture, String _lat, String _lon, double _distance ) {
        distance=_distance;
        playername = _playername;
        passcode = _passcode;
        picture= _picture;
        lat=_lat;
        lon=_lon;

    }

}
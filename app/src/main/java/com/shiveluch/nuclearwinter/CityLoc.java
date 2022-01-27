package com.shiveluch.nuclearwinter;

public class CityLoc {
    int id;
    String city;
    double latitude, longitude;

    public  CityLoc ( String _city, double _lat, double _lon, int _id)
    {
        id=_id;
        city=_city;
        latitude = _lat;
        longitude = _lon;
    }
}

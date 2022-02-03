package com.shiveluch.nuclearwinter;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class BotMutant {
    LatLng startPosition;
    double currentLat;
    double currentLon;
    Marker marker;
    String mutantName;
    int drawmarker;
    int speed;
    double distance;
    double directX=0;
    double directY=0;
    int moves=0;
    boolean selected=false;
    double max = 0.0001;
    double min= -0.0001;
    int id;
    public BotMutant (LatLng _startposition, int _speed, Marker _marker, String _mutantname, int _drawmarker, int _id)
    {
        startPosition = _startposition;
        speed=_speed;
        currentLat = _startposition.latitude;
        currentLon = _startposition.longitude;
        marker=_marker;
        mutantName=_mutantname;
        drawmarker=_drawmarker;
        id=_id;
    }

    public LatLng getPosition()
    {
        LatLng currentPosition;
        if (moves<=0) {
            moves = getMoves();
            directX = getMoveLat();
            directY = getMoveLon();
        }
        currentLat = currentLat+directX;
        currentLon= currentLon+directY;
        moves--;
        currentPosition = new LatLng(currentLat,currentLon);
        return currentPosition;
    }


    public double getDistance()
    {
        distance = distanceInKmBetweenEarthCoordinates(startPosition.latitude, startPosition.longitude,currentLat,currentLon)*1000;

        return distance;
    }

    public ArrayList<Integer> mutantIcons()
    {
        ArrayList<Integer> mIcons = new ArrayList<>();
        mIcons.add(R.drawable.krovosos);
        mIcons.add(R.drawable.snork);
        mIcons.add(R.drawable.controler);
        return mIcons;
    }

    private double distanceInKmBetweenEarthCoordinates(double lat1, double lon1, double lat2, double lon2) {
        int earthRadiusKm = 6371;
        double dLat = degreesToRadians(lat2 - lat1);
        double dLon = degreesToRadians(lon2 - lon1);

        lat1 = degreesToRadians(lat1);
        lat2 = degreesToRadians(lat2);

        double a = sin(dLat / 2) * sin(dLat / 2) +
                sin(dLon / 2) * sin(dLon / 2) * cos(lat1) * cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c;
    }

    private double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    public int getDirectY()
    {
        Random rnd = new Random();
        int directY = rnd.nextInt(10);
        return directY;
    }

    public int getDirectX()
    {
        Random rnd = new Random();
        int directX = rnd.nextInt(10);
        return directX;
    }

    public int getMoves()
    {
        Random rnd = new Random();
        moves = rnd.nextInt(10);
        return moves;
    }

    private double getMoveLat()
    {
        Random rnd = new Random();
        double randomLatValue = min + (max - min) * rnd.nextDouble();
        String formattedLatDouble = new DecimalFormat("#0.0000000").format(randomLatValue);
        double movelat = Double.parseDouble(formattedLatDouble.replace(",","."));
        return movelat;
    }

    private double getMoveLon()
    {
        Random rnd = new Random();
        double randomLonValue = min + (max - min) * rnd.nextDouble();
        String formattedLonDouble = new DecimalFormat("#0.0000000").format(randomLonValue);
        double movelon = Double.parseDouble(formattedLonDouble.replace(",","."));
        return movelon;
    }

}

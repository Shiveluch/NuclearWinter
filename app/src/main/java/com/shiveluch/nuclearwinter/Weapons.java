package com.shiveluch.nuclearwinter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Weapons {


    public ArrayList<Integer> setWeapons() {
        ArrayList<Integer> weapons = new ArrayList<>();
        weapons.add(R.drawable.pistol);
        weapons.add(R.drawable.obrezangl);
        weapons.add(R.drawable.ak47angl);
        weapons.add(R.drawable.svdangl);
        return weapons;

    }
}


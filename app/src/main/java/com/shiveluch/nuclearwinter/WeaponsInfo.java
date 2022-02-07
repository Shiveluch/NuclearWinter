package com.shiveluch.nuclearwinter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class WeaponsInfo implements Parcelable {

    String weapon, ammo;
    int id, maxdistance, mindistance, shots, uron, image, imageright, ammoimage;

    public WeaponsInfo (int _id, String _weapon, int _maxdistance, int _mindistance, int _shots,
                    int _uron, int _image, int _imageright, int _ammoimage, String _ammo)
    {
        id=_id;
        weapon = _weapon;
        maxdistance=_maxdistance;
        mindistance=_mindistance;
        shots=_shots;
        uron=_uron;
        image=_image;
        imageright=_imageright;
        ammoimage=_ammoimage;
        ammo=_ammo;
    }

    protected WeaponsInfo(Parcel in) {
        id = in.readInt();
        weapon = in.readString();
        maxdistance = in.readInt();
        mindistance = in.readInt();
        shots = in.readInt();
        uron = in.readInt();
        image = in.readInt();
        imageright=in.readInt();
        ammoimage = in.readInt();
        ammo = in.readString();
    }

    public static final Creator<WeaponsInfo> CREATOR = new Creator<WeaponsInfo>() {
        @Override
        public WeaponsInfo createFromParcel(Parcel in) {
            return new WeaponsInfo(in);
        }

        @Override
        public WeaponsInfo[] newArray(int size) {
            return new WeaponsInfo[size];
        }
    };

    public ArrayList<Integer> setWeapons()
    {
        ArrayList <Integer> weapons = new ArrayList<>();
        weapons.add(R.drawable.pistol);
        weapons.add(R.drawable.obrezangl);
        weapons.add(R.drawable.ak47angl);
        weapons.add(R.drawable.svdangl);
        return weapons;

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(weapon);
        parcel.writeInt(maxdistance);
        parcel.writeInt(mindistance);
        parcel.writeInt(shots);
        parcel.writeInt(uron);
        parcel.writeInt(image);
        parcel.writeInt(imageright);
        parcel.writeInt(ammoimage);
        parcel.writeString(ammo);
    }



}

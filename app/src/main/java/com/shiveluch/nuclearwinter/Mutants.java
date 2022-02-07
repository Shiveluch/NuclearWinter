package com.shiveluch.nuclearwinter;

import android.os.Parcel;
import android.os.Parcelable;

public class Mutants implements Parcelable {
    String name;
    int hits;
    int distance;
    int cost;
    int power;
   int icon;
   int id;
   int picture;
   String info;
   int sound;
   int speed;
    public Mutants (String _name, int _hits, int _distance, int _cost, int _power, int _icon, int _id, String _info, int _sound, int _picture, int _speed) {
        name = _name;
        hits = _hits;
        distance = _distance;
        cost = _cost;
        power= _power;
        icon=_icon;
        id=_id;
        info=_info;
        sound=_sound;
        picture=_picture;
        speed=_speed;
    }

    protected Mutants(Parcel in) {
        name = in.readString();
        hits = in.readInt();
        distance = in.readInt();
        cost = in.readInt();
        power = in.readInt();
        icon = in.readInt();
        id = in.readInt();
        picture = in.readInt();
        info = in.readString();
        sound = in.readInt();
        speed =in.readInt();
    }

    public static final Creator<Mutants> CREATOR = new Creator<Mutants>() {
        @Override
        public Mutants createFromParcel(Parcel in) {
            return new Mutants(in);
        }

        @Override
        public Mutants[] newArray(int size) {
            return new Mutants[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(hits);
        parcel.writeInt(distance);
        parcel.writeInt(cost);
        parcel.writeInt(power);
        parcel.writeInt(icon);
        parcel.writeInt(id);
        parcel.writeInt(picture);
        parcel.writeString(info);
        parcel.writeInt(sound);
        parcel.writeInt(speed);
    }
}

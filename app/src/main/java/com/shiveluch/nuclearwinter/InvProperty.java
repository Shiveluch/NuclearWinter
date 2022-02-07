package com.shiveluch.nuclearwinter;

import android.os.Parcel;
import android.os.Parcelable;

public class InvProperty implements Parcelable {
    String invname;
    int property;
    public InvProperty (String _invname, int _property)
    {
        invname = _invname;
        property=_property;
    }

    protected InvProperty(Parcel in) {
        invname = in.readString();
        property = in.readInt();
    }

    public static final Creator<InvProperty> CREATOR = new Creator<InvProperty>() {
        @Override
        public InvProperty createFromParcel(Parcel in) {
            return new InvProperty(in);
        }

        @Override
        public InvProperty[] newArray(int size) {
            return new InvProperty[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(invname);
        parcel.writeInt(property);
    }
}

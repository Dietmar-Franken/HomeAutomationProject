package com.example.garima.homeautomationproject.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GARIMA on 9/21/2017.
 */

public class RoomModel implements Parcelable {
    public RoomModel() {

    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public static Creator<RoomModel> getCREATOR() {
        return CREATOR;
    }

    String homeName;
    String roomName;

    public RoomModel(Parcel in) {
        homeName = in.readString();
        roomName = in.readString();
    }

    public static final Creator<RoomModel> CREATOR = new Creator<RoomModel>() {
        @Override
        public RoomModel createFromParcel(Parcel in) {
            return new RoomModel(in);
        }

        @Override
        public RoomModel[] newArray(int size) {
            return new RoomModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(homeName);
        parcel.writeString(roomName);
    }
}

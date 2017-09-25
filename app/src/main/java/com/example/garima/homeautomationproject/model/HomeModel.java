package com.example.garima.homeautomationproject.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GARIMA on 9/18/2017.
 */

public class HomeModel extends BaseModel implements Parcelable {
    public HomeModel() {

    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public static Creator<HomeModel> getCREATOR() {
        return CREATOR;
    }

    private String homeName;

    public HomeModel(Parcel in) {
        homeName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(homeName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HomeModel> CREATOR = new Creator<HomeModel>() {
        @Override
        public HomeModel createFromParcel(Parcel in) {
            return new HomeModel(in);
        }

        @Override
        public HomeModel[] newArray(int size) {
            return new HomeModel[size];
        }
    };
}

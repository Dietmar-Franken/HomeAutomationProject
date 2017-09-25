package com.example.garima.homeautomationproject.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GARIMA on 9/7/2017.
 */

public class DeviceModel extends BaseModel implements Parcelable  {
    private String roomName;
    private String deviceName;
    private String deviceCode;
    private String deviceType;
    private String homeName;


    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }



    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public static Creator<DeviceModel> getCREATOR() {
        return CREATOR;
    }




    public DeviceModel(Parcel in) {
        roomName = in.readString();
        deviceName = in.readString();
        deviceCode = in.readString();
        deviceType = in.readString();
        homeName = in.readString();
    }

    public DeviceModel() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roomName);
        dest.writeString(deviceName);
        dest.writeString(deviceCode);
        dest.writeString(deviceType);
        dest.writeString(homeName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeviceModel> CREATOR = new Creator<DeviceModel>() {
        @Override
        public DeviceModel createFromParcel(Parcel in) {
            return new DeviceModel(in);
        }

        @Override
        public DeviceModel[] newArray(int size) {
            return new DeviceModel[size];
        }
    };

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }




    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

}

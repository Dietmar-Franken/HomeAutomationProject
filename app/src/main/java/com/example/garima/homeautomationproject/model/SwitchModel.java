package com.example.garima.homeautomationproject.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GARIMA on 9/11/2017.
 */

public class SwitchModel extends BaseModel implements Parcelable {
    private String switchName;
    private String roomName;
    private String deviceName;
    private String deviceCode;
    private String  switchState;
    private String homeName;
    private String switchType;
    private String deviceType;


    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }


    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }



    public String getSwitchState() {
        return switchState;
    }

    public void setSwitchState(String switchState) {
        this.switchState = switchState;
    }

    public static Creator<SwitchModel> getCREATOR() {
        return CREATOR;
    }



    public SwitchModel(Parcel in) {
        switchName = in.readString();
        roomName = in.readString();
        deviceName = in.readString();
        deviceCode = in.readString();
        switchType = in.readString();
        homeName = in.readString();
        deviceType =in.readString();
    }

    public SwitchModel() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(switchName);
        dest.writeString(roomName);
        dest.writeString(deviceName);
        dest.writeString(deviceCode);
        dest.writeString(switchType);
        dest.writeString(homeName);
        dest.writeString(deviceType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SwitchModel> CREATOR = new Creator<SwitchModel>() {
        @Override
        public SwitchModel createFromParcel(Parcel in) {
            return new SwitchModel(in);
        }

        @Override
        public SwitchModel[] newArray(int size) {
            return new SwitchModel[size];
        }
    };

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

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


    public String getSwitchType() {
        return switchType;
    }

    public void setSwitchType(String switchType) {
        this.switchType = switchType;
    }


}

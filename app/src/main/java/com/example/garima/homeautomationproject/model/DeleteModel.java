package com.example.garima.homeautomationproject.model;

/**
 * Created by GARIMA on 9/13/2017.
 */

public class DeleteModel extends BaseModel {
    private String deletePosition;
    private String switchType;
    public String getDeletePosition() {
        return deletePosition;
    }

    public void setDeletePosition(String deletePosition) {
        this.deletePosition = deletePosition;
    }

    public String getSwitchType() {
        return switchType;
    }

    public void setSwitchType(String switchType) {
        this.switchType = switchType;
    }


}

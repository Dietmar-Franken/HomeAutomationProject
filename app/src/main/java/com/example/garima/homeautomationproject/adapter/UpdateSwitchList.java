package com.example.garima.homeautomationproject.adapter;

import android.view.View;

import com.example.garima.homeautomationproject.model.RoomModel;
import com.example.garima.homeautomationproject.model.SwitchModel;

import java.util.ArrayList;

/**
 * Created by GARIMA on 9/21/2017.
 */

public interface UpdateSwitchList {
    public void UpdateSwitchList(ArrayList<SwitchModel> switchModelArrayList);
    void onItemClick(int position, View v);

}

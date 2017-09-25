package com.example.garima.homeautomationproject.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garima.homeautomationproject.R;
import com.example.garima.homeautomationproject.adapter.SwitchListAdapter;
import com.example.garima.homeautomationproject.adapter.UpdateSwitchList;
import com.example.garima.homeautomationproject.database.DatabaseHandler;
import com.example.garima.homeautomationproject.model.DeviceModel;
import com.example.garima.homeautomationproject.model.SwitchModel;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

import static com.example.garima.homeautomationproject.constant.Constant.DEVICE_MODEL;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH1;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH2;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH3;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH4;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH_TYPE_FAN;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH_TYPE_LIGHT;

public class SwitchActivity extends AppCompatActivity implements UpdateSwitchList {
    private TextView toolbarTextView;
    private ImageView toolbarImageView;
    private FloatingActionButton addSwitchFab;
    private RecyclerView recyclerView;
    private DatabaseHandler databaseHandler;
    private SwitchListAdapter switchListAdapter;
    private ArrayList<SwitchModel> switchModelArrayList = new ArrayList<>();
    private DeviceModel deviceModel;
    private int limit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch2);
        databaseHandler = new DatabaseHandler(this);
        getLayoutsId();
        setOnClickListener();
        setRecyclerView();
        registerForContextMenu(recyclerView);
        setOnClickListener();
        limitForAddingSwitch();
    }
    private void getLayoutsId() {
        toolbarTextView = (TextView) findViewById(R.id.toolbarTextView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        toolbarImageView = (ImageView) findViewById(R.id.backImageView);
        addSwitchFab = (FloatingActionButton) findViewById(R.id.addSwitchFab);

    }

    private void setOnClickListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.backImageView:
                        finish();
                        break;
                    case R.id.addSwitchFab:
                        openAlertDialog();
                        break;
                }

            }
        };
        toolbarImageView.setOnClickListener(clickListener);
        addSwitchFab.setOnClickListener(clickListener);
    }

    private void openAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SwitchActivity.this);
        alertDialog.setTitle("Add Switch ");
       final EditText switchNameEditText = new EditText(SwitchActivity.this);
        switchNameEditText.setSingleLine();
        switchNameEditText.setHint("Enter Switch Name");
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(switchNameEditText);
        alertDialog.setView(layout);
        alertDialog.setIcon(R.drawable.switchboard);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       // String deviceName = switchNameEditText.getText().toString();

                   /*   //  if (TextUtils.isEmpty(deviceName) || TextUtils.isEmpty(switchNameEditText.getText().toString())) {
                            return;
                        }*/
                        String switchName = switchNameEditText.getText().toString();

                        if (!databaseHandler.checkSwitchNameExist(switchName,deviceModel.getHomeName(),deviceModel.getRoomName(),deviceModel.getDeviceCode())) {
                            Toast.makeText(SwitchActivity.this, "This Device ID Already exist", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            SwitchModel switchModel = new SwitchModel();
                            switchModel.setDeviceCode(deviceModel.getDeviceCode());
                            switchModel.setHomeName(deviceModel.getHomeName());
                            switchModel.setRoomName(deviceModel.getRoomName());
                            switchModel.setDeviceType(deviceModel.getDeviceType());
                            switchModel.setDeviceName(deviceModel.getDeviceName());
                            switchModel.setSwitchName(switchName);
                            if(deviceModel.getDeviceType().equalsIgnoreCase(SWITCH1)) {
                                switchModel.setSwitchType(SWITCH_TYPE_FAN);
                            }
                            else {
                                switchModel.setSwitchType(SWITCH_TYPE_LIGHT);
                            }
                            switchModelArrayList.add(switchModel);
                            databaseHandler.addSwitch(switchModel);
                        }
                        if (switchModelArrayList.size() >= 1) {
                            switchListAdapter.update(switchModelArrayList);
                        } else {

                            setRecyclerView();
                        }

                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void setRecyclerView() {
        deviceModel =  getIntent().getParcelableExtra(DEVICE_MODEL);
        String addDevice = getString(R.string.add_switch);
        toolbarTextView.setText(addDevice + " in" + ' ' + deviceModel.getDeviceName());
        switchModelArrayList = databaseHandler.getSwitchList(deviceModel.getDeviceCode(),deviceModel.getHomeName(),deviceModel.getRoomName());
        switchListAdapter = new SwitchListAdapter(this, switchModelArrayList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(switchListAdapter);
    }


    @Override
    public void UpdateSwitchList(ArrayList<SwitchModel> switchModelArrayList) {
        this.switchModelArrayList = switchModelArrayList;
        if(switchModelArrayList.size()>=limit)
        {
            addSwitchFab.setClickable(false);
        }
        else {
            addSwitchFab.setClickable(true);
        }


    }

    @Override
    public void onItemClick(int position, View v) {
        Toast.makeText(this,"mdmfdig",Toast.LENGTH_LONG).show();

    }
    private void limitForAddingSwitch()
    {
        if(deviceModel.getDeviceType().equalsIgnoreCase(SWITCH1))
        {
            limit =1;
        }
        if(deviceModel.getDeviceType().equalsIgnoreCase(SWITCH2))
        {
            limit =2;
        }
        if(deviceModel.getDeviceType().equalsIgnoreCase(SWITCH3))
        {
            limit =3;
        }
        if(deviceModel.getDeviceType().equalsIgnoreCase(SWITCH4))
        {
            limit =4;
        }
    }
}

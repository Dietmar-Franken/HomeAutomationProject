package com.example.garima.homeautomationproject.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garima.homeautomationproject.R;
import com.example.garima.homeautomationproject.adapter.DeviceListAdapter;
import com.example.garima.homeautomationproject.database.DatabaseHandler;
import com.example.garima.homeautomationproject.model.DeviceModel;
import com.example.garima.homeautomationproject.model.RoomModel;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

import static com.example.garima.homeautomationproject.constant.Constant.ROOM_MODEL;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH1;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH2;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH3;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH4;

public class DeviceActivity extends AppCompatActivity {
    private TextView toolbarTextView;
    private ImageView toolbarImageView;
    private FloatingActionButton addSwitchFab;
    private RecyclerView recyclerView;
    private DatabaseHandler databaseHandler;
    private String roomName;
    private DeviceListAdapter switchBoardListAdapter;
    private ArrayList<DeviceModel> deviceModelArrayList = new ArrayList<>();
    private Spinner switchBoardSpinner;
    private EditText switchBoardET;
    private String selectedItem = SWITCH1;
    private RoomModel roomModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_board);
        databaseHandler = new DatabaseHandler(this);
        getLayoutsId();
        setOnClickListener();
        setRecyclerView();
        registerForContextMenu(recyclerView);
        setOnClickListener();


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
                        setArrayAdapter();
                        setSpinnerListener();
                        break;
                }

            }
        };
        toolbarImageView.setOnClickListener(clickListener);
        addSwitchFab.setOnClickListener(clickListener);
    }

    private void openAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeviceActivity.this);
        alertDialog.setTitle("Add Switch Board");
        alertDialog.setMessage("Enter Switch Board ID and Switch Board Location");

        final EditText deviceIdEditText = new EditText(DeviceActivity.this);
        deviceIdEditText.setHint("Switch Board ID");
        deviceIdEditText.setSingleLine();
        int maxLength = 5;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        deviceIdEditText.setFilters(FilterArray);
        deviceIdEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        final EditText deviceNameEditText = new EditText(DeviceActivity.this);
        deviceNameEditText.setSingleLine();
        deviceNameEditText.setHint("Switch Board Location");

        LinearLayout.LayoutParams switchBoardTypeParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        switchBoardSpinner = new Spinner(DeviceActivity.this);
        switchBoardSpinner.setLayoutParams(switchBoardTypeParams);

        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1, 0);
        View view = new View(DeviceActivity.this);
        view.setBackground(getResources().getDrawable(R.color.colorPrimary));
        view.setLayoutParams(viewParams);


        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(deviceIdEditText);
        layout.addView(deviceNameEditText);
        layout.addView(switchBoardSpinner);
        layout.addView(view);
        alertDialog.setView(layout);

        alertDialog.setIcon(R.drawable.switchboard);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String deviceName = deviceNameEditText.getText().toString();

                        if (TextUtils.isEmpty(deviceName) || TextUtils.isEmpty(deviceIdEditText.getText().toString())) {
                            return;
                        }
                        String deviceCode = deviceIdEditText.getText().toString();

                        if (!databaseHandler.checkDeviceAlreadyExist(deviceCode,roomModel.getHomeName(),roomModel.getRoomName())) {
                            Toast.makeText(DeviceActivity.this, "This Device ID Already exist", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            DeviceModel deviceModel = new DeviceModel();
                            deviceModel.setDeviceCode(deviceCode);
                            deviceModel.setHomeName(roomModel.getHomeName());
                            deviceModel.setRoomName(roomModel.getRoomName());
                            deviceModel.setDeviceType(selectedItem);
                            deviceModel.setDeviceName(deviceName);
                            deviceModelArrayList.add(deviceModel);
                            databaseHandler.addDevice(deviceModel);
                        }
                        if (deviceModelArrayList.size() >= 1) {
                            switchBoardListAdapter.update(deviceModelArrayList);
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
        roomModel =  getIntent().getParcelableExtra(ROOM_MODEL);
        String addDevice = getString(R.string.add_switch_board);
        toolbarTextView.setText(addDevice + " in" + ' ' + roomModel.getRoomName());
        deviceModelArrayList = (ArrayList<DeviceModel>) databaseHandler.getDeviceList(roomModel.getRoomName(),roomModel.getHomeName());
        switchBoardListAdapter = new DeviceListAdapter(this, deviceModelArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(switchBoardListAdapter);
    }


    private ArrayList<String> createSwitchBoardTypeList() {
        ArrayList<String> switchBoardTypeList = new ArrayList<>();
        switchBoardTypeList.add("1 SWITCH");
        switchBoardTypeList.add("2 SWITCH");
        switchBoardTypeList.add("4 SWITCH");
        switchBoardTypeList.add("8 SWITCH");
        return switchBoardTypeList;
    }

    private void setSpinnerListener() {
        switchBoardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               String selectedString = createSwitchBoardTypeList().get(i);
                if(selectedString.equalsIgnoreCase("1 SWITCH"))
                {
                    selectedItem =SWITCH1;
                }
                if(selectedString.equalsIgnoreCase("2 SWITCH"))
                {
                    selectedItem =SWITCH2;
                }
                if(selectedString.equalsIgnoreCase("3 SWITCH"))
                {
                    selectedItem =SWITCH3;
                }
                if(selectedString.equalsIgnoreCase("4 SWITCH"))
                {
                    selectedItem =SWITCH4;
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setArrayAdapter() {
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, createSwitchBoardTypeList());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        switchBoardSpinner.setAdapter(arrayAdapter);
    }

}

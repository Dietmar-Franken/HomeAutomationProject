package com.example.garima.homeautomationproject.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garima.homeautomationproject.R;
import com.example.garima.homeautomationproject.activity.SwitchActivity;
import com.example.garima.homeautomationproject.database.DatabaseHandler;
import com.example.garima.homeautomationproject.model.DeviceModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.garima.homeautomationproject.constant.Constant.DEVICE_MODEL;

/**
 * Created by GARIMA on 9/7/2017.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.SwitchBoardListViewHolder>{
    private List<DeviceModel> deviceModelList;
    private Context context;
    private LayoutInflater layoutInflater;
    private DatabaseHandler databaseHandler;



    @Override
    public DeviceListAdapter.SwitchBoardListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        databaseHandler = new DatabaseHandler(context);
        View itemView = layoutInflater.inflate(R.layout.switch_board_row_item, parent, false);
        DeviceListAdapter.SwitchBoardListViewHolder switchBoardListViewHolder = new DeviceListAdapter.SwitchBoardListViewHolder(itemView);
        return switchBoardListViewHolder;
    }

    public DeviceListAdapter(Context context, List<DeviceModel> switchBoardList) {
        this.deviceModelList = switchBoardList;
        this.context = context;

    }

    @Override
    public void onBindViewHolder(DeviceListAdapter.SwitchBoardListViewHolder holder, int position) {
        if (deviceModelList != null && deviceModelList.size() > position) {
            DeviceModel switchBoard = deviceModelList.get(position);
            if (switchBoard != null) {
                holder.switchBoardNameTextView.setText(switchBoard.getDeviceName());
                holder.switchNumberTextView.setText(switchBoard.getDeviceCode());
                setClickListener(holder.menuLinearLayout, switchBoard, position);
                setClickListener(holder.mainLinearLayout, switchBoard, position);

            }

        }

    }

    public class SwitchBoardListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView switchBoardNameTextView;
        private TextView switchNumberTextView;
        private LinearLayout menuLinearLayout;
        private LinearLayout mainLinearLayout;



        public SwitchBoardListViewHolder(View view) {
            super(view);
            switchBoardNameTextView = (TextView) view.findViewById(R.id.switchBoardNameTextView);
            switchNumberTextView = (TextView) view.findViewById(R.id.switchBoardIDTextView);
            menuLinearLayout = (LinearLayout)view.findViewById(R.id.menuLinearLayout);
            mainLinearLayout = (LinearLayout)view.findViewById(R.id.mainLinearLayout);


        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public int getItemCount() {
        if (deviceModelList == null) {
            return 0;
        }
        return deviceModelList.size();
    }

    public void update(ArrayList<DeviceModel> datas) {
        deviceModelList = datas;
        notifyDataSetChanged();
    }
    private void setOnClickListener(final TextView textView, final int position)
    {

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, textView);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:


                                //handle menu1 click
                                break;
                            case R.id.delete:
                                //handle menu2 click
                                break;

                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }


        });
    }
    private void setClickListener(final LinearLayout linearLayout, final DeviceModel deviceModel, final int position) {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.menuLinearLayout: {
                        PopupMenu popup = new PopupMenu(context, linearLayout);
                        popup.inflate(R.menu.options_menu);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                    case R.id.edit:
                                        showEditDialog(deviceModel);
                                        break;
                                    case R.id.delete:
                                        showDeleteDialog(deviceModel);
                                        break;

                                }
                                return false;
                            }


                        });
                        //displaying the popup
                        popup.show();
                    }
                    break;
                    case R.id.mainLinearLayout: {
                        Intent intent = new Intent(context, SwitchActivity.class);
                        intent.putExtra(DEVICE_MODEL, deviceModel);
                        context.startActivity(intent);
                    }
                    break;

                }


            }
        });

    }

    private void showDeleteDialog(final DeviceModel deviceModel) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Delete");
        alertDialog.setMessage("Are You Sure ,Do You want to delete this Device");
        alertDialog.setIcon(R.drawable.room);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      //  String deviceName = deviceModel.getDeviceName();
                        databaseHandler.deleteDevice(deviceModel);
                        deviceModelList = databaseHandler.getDeviceList(deviceModel.getRoomName(),deviceModel.getHomeName());
                        update((ArrayList<DeviceModel>) deviceModelList);


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

    private void showEditDialog(final DeviceModel deviceModel) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Edit Device");
        alertDialog.setMessage("Change Device ID and Device Name");
        final String roomName = deviceModel.getRoomName();
        final  String prevDeviceCode = deviceModel.getDeviceCode();

        final EditText deviceIdEditText = new EditText(context);
        deviceIdEditText.setText(deviceModel.getDeviceCode());
        deviceIdEditText.setSingleLine();
        deviceIdEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        final EditText deviceNameEditText = new EditText(context);
        deviceNameEditText.setSingleLine();
        deviceNameEditText.setText(deviceModel.getDeviceName());

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(deviceIdEditText);
        layout.addView(deviceNameEditText);
        alertDialog.setView(layout);

        alertDialog.setIcon(R.drawable.switchboard);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String deviceName = deviceNameEditText.getText().toString();
                        String deviceCode = deviceIdEditText.getText().toString();

                        if (TextUtils.isEmpty(deviceName) || TextUtils.isEmpty(deviceCode)) {
                            return;
                        }


                        if (!databaseHandler.checkDeviceAlreadyExist(deviceModel.getDeviceCode(),deviceModel.getHomeName(),deviceModel.getRoomName())) {
                            Toast.makeText(context, "This device code Already exist", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            DeviceModel deviceModel = new DeviceModel();
                            deviceModel.setDeviceCode(deviceCode);
                            deviceModel.setRoomName(roomName);
                            deviceModel.setDeviceName(deviceName);
                            databaseHandler.updateDevice(prevDeviceCode,deviceModel);
                            deviceModelList = databaseHandler.getDeviceList(deviceModel.getRoomName(),deviceModel.getHomeName());
                            update((ArrayList<DeviceModel>) deviceModelList);


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
    }




package com.example.garima.homeautomationproject.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import com.example.garima.homeautomationproject.activity.DeviceActivity;
import com.example.garima.homeautomationproject.database.DatabaseHandler;
import com.example.garima.homeautomationproject.model.RoomModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.garima.homeautomationproject.constant.Constant.ROOM_MODEL;

/**
 * Created by GARIMA on 9/5/2017.
 */

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomListViewHolder> {
    private List<RoomModel> roomModelList;
    private Context context;
    private LayoutInflater layoutInflater;
    private LinearLayout roomDescriptionLL;
    private LinearLayout menuLinearLayout;
    private RoomModel roomModel;
    private UpdateRoomList updateRoomList;

    private DatabaseHandler databaseHandler;


    @Override
    public RoomListAdapter.RoomListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        databaseHandler = new DatabaseHandler(context);
        View itemView = layoutInflater.inflate(R.layout.room_row_item, parent, false);
        RoomListViewHolder roomListViewHolder = new RoomListViewHolder(itemView);
        return roomListViewHolder;
    }

    public RoomListAdapter(Context context, List<RoomModel> roomModelList,UpdateRoomList updateRoomList) {
        this.roomModelList = roomModelList;
        this.context = context;
        this.updateRoomList=updateRoomList;

    }

    @Override
    public void onBindViewHolder(RoomListAdapter.RoomListViewHolder holder, int position) {
        if (roomModelList != null && roomModelList.size() > position) {
            roomModel = roomModelList.get(position);
            if (roomModel != null) {
                holder.roomNameTextView.setText(roomModel.getRoomName());

            }
            setClickListener(holder.menuLinearLayout, roomModel, position);
            setClickListener(holder.roomDescriptionLinearLayout, roomModel, position);
        }

    }


    public class RoomListViewHolder extends RecyclerView.ViewHolder {
        private TextView roomNameTextView;
        private TextView roomMenuTextView;
        private LinearLayout menuLinearLayout;
        private LinearLayout roomDescriptionLinearLayout;


        public RoomListViewHolder(View view) {
            super(view);
            roomNameTextView = (TextView) view.findViewById(R.id.roomNameTextView);
            roomMenuTextView = (TextView) view.findViewById(R.id.roomMenuTextView);
            menuLinearLayout = (LinearLayout) view.findViewById(R.id.menuLinearLayout);
            roomDescriptionLinearLayout = (LinearLayout) view.findViewById(R.id.roomDiscriptionLL);


        }


    }

    @Override
    public int getItemCount() {
        if (roomModelList == null) {
            return 0;
        }

         updateRoomList.updateRoomList((ArrayList<RoomModel>) roomModelList);
        return roomModelList.size();
    }

    public void update(ArrayList<RoomModel> datas) {
        roomModelList = datas;
        notifyDataSetChanged();
    }

    private void setClickListener(final LinearLayout linearLayout, final RoomModel roomModel, final int position) {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.menuLinearLayout: {
                        PopupMenu popup = new PopupMenu(context, linearLayout);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.options_menu);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.edit: {
                                        String prevRoomName = roomModelList.get(position).getRoomName();
                                        String homeName = roomModelList.get(position).getHomeName();
                                        showEditDialog(prevRoomName, homeName);
                                    }
                                        break;
                                    case R.id.delete:
                                        String roomName = roomModelList.get(position).getRoomName();
                                        String homeName = roomModelList.get(position).getHomeName();
                                        showDeleteDialog(roomName,homeName);
                                        break;

                                }
                                return false;
                            }
                        });
                        //displaying the popup
                        popup.show();
                    }
                    break;
                    case R.id.roomDiscriptionLL: {
                        Intent intent = new Intent(context, DeviceActivity.class);

                        intent.putExtra(ROOM_MODEL, roomModel);
                        context.startActivity(intent);
                    }
                    break;

                }

            }
        });
    }

    private void showEditDialog(final String prevRoomName, final String homeName) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Edit");
        alertDialog.setMessage("Change Room Name");


        final EditText deviceNameEditText = new EditText(context);
        deviceNameEditText.setSingleLine();
        deviceNameEditText.setText(prevRoomName);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(deviceNameEditText);
        alertDialog.setView(layout);

        alertDialog.setIcon(R.drawable.room);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String roomName = deviceNameEditText.getText().toString();

                        if (TextUtils.isEmpty(roomName)) {
                            return;
                        }


                        if (!databaseHandler.checkRoomAlreadyExist(roomName,homeName)) {
                            Toast.makeText(context, "This room Name Already exist", Toast.LENGTH_LONG).show();
                            return;
                        } else {


                            int result = databaseHandler.updateRoomName(prevRoomName, roomName,homeName);
                            if (result == 1) {
                                roomModelList = databaseHandler.getRoomList(homeName);
                                update((ArrayList<RoomModel>) roomModelList);
                            }
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

    private void showDeleteDialog(final String roomName, final String homeName) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Delete");
        alertDialog.setMessage("Are You Sure ,Do You want to delete this Room");
        alertDialog.setIcon(R.drawable.room);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHandler.deleteRoom(roomName,homeName);
                        roomModelList = databaseHandler.getRoomList(homeName);
                        update((ArrayList<RoomModel>) roomModelList);


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

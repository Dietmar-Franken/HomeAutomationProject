package com.example.garima.homeautomationproject.activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.garima.homeautomationproject.adapter.RoomListAdapter;
import com.example.garima.homeautomationproject.adapter.UpdateRoomList;
import com.example.garima.homeautomationproject.database.DatabaseHandler;
import com.example.garima.homeautomationproject.model.HomeModel;
import com.example.garima.homeautomationproject.model.RoomModel;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

import static com.example.garima.homeautomationproject.constant.Constant.HOME_MODEL;

public class RoomActivity extends AppCompatActivity implements UpdateRoomList{
    private FloatingActionButton addRoomFloatingButton;
    private ArrayList<RoomModel> roomModelArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RoomListAdapter roomListAdapter;
    private DatabaseHandler databaseHandler;
    private HomeModel homeModel;
    private TextView toolbarTextView;
    private ImageView backImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        databaseHandler = new DatabaseHandler(this);
        findViewById();
        setRecyclerView();
        setOnClickListener();
        registerForContextMenu(recyclerView);
    }
    private void findViewById() {
        addRoomFloatingButton = (FloatingActionButton)findViewById(R.id.addRoomFab);
        recyclerView =(RecyclerView) findViewById(R.id.recyclerView);
        toolbarTextView =(TextView) findViewById(R.id.toolbarTextView);
        backImageView =(ImageView) findViewById(R.id.backImageView);


    }


    private void setOnClickListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.addRoomFab:
                        openAlertDialog();
                        break;
                    case R.id.backImageView:
                        finish();
                        break;

                }

            }
        };
        addRoomFloatingButton.setOnClickListener(clickListener);
        backImageView.setOnClickListener(clickListener);
    }


    private void openAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Room");
        alertDialog.setMessage("Enter Room Name");
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.room);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String roomName = input.getText().toString();
                        if(TextUtils.isEmpty(roomName))
                        {
                            return;
                        }

                        if(!databaseHandler.checkRoomAlreadyExist(roomName,homeModel.getHomeName())) {
                            Toast.makeText(RoomActivity.this,"This room Name Already exist",Toast.LENGTH_LONG).show();
                            return;
                        }
                        else
                        {
                            RoomModel roomModel = new RoomModel();
                            roomModel.setRoomName(roomName);
                            roomModel.setHomeName(homeModel.getHomeName());
                            roomModelArrayList.add(roomModel);
                            databaseHandler.addRoom(roomModel);
                        }
                        if (roomModelArrayList.size() >= 1) {
                            roomListAdapter.update(roomModelArrayList);
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
        Intent i = getIntent();
        homeModel =i.getParcelableExtra(HOME_MODEL);
        toolbarTextView.setText("Add Room In "+homeModel.getHomeName());
        roomModelArrayList = (ArrayList<RoomModel>) databaseHandler.getRoomList(homeModel.getHomeName());
        roomListAdapter = new RoomListAdapter(this, roomModelArrayList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(roomListAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        // setRecyclerView();
    }


    @Override
    public void updateRoomList(ArrayList<RoomModel> roomModelArrayList) {
        this.roomModelArrayList= roomModelArrayList;
    }
}

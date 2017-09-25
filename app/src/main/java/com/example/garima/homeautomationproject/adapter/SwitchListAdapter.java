package com.example.garima.homeautomationproject.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garima.homeautomationproject.R;
import com.example.garima.homeautomationproject.activity.BulbOperationActivity;
import com.example.garima.homeautomationproject.database.DatabaseHandler;
import com.example.garima.homeautomationproject.model.SwitchModel;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static com.example.garima.homeautomationproject.constant.Constant.SWITCH_MODEL;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH_TYPE_FAN;


public class SwitchListAdapter extends RecyclerView.Adapter<SwitchListAdapter.SwitchlistViewHolder> implements View.OnClickListener{
    private ArrayList<SwitchModel> switchModelArrayList;
    private Context context;
    private LayoutInflater layoutInflater;
    private LinearLayout roomDescriptionLL;
    private LinearLayout menuLinearLayout;
    private SwitchModel switchModel;
    private UpdateSwitchList updateSwitchList;
    private int Position;

    private DatabaseHandler databaseHandler;
    private Thread m_objectThreadClient;
    private Socket clientSocket;
    private WifiManager wifiManager;
    private String dhcS;


    @Override
    public SwitchListAdapter.SwitchlistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        databaseHandler = new DatabaseHandler(context);
        View itemView = layoutInflater.inflate(R.layout.switch_row_item, parent, false);
        SwitchListAdapter.SwitchlistViewHolder switchlistViewHolder = new SwitchListAdapter.SwitchlistViewHolder(itemView);
        return switchlistViewHolder;
    }

    public SwitchListAdapter(Context context, ArrayList<SwitchModel> switchModelArrayList,UpdateSwitchList updateSwitchList) {
        this.switchModelArrayList = switchModelArrayList;
        this.context = context;
        this.updateSwitchList = updateSwitchList;

    }

    @Override
    public void onBindViewHolder(SwitchListAdapter.SwitchlistViewHolder holder, int Position) {
        this.Position  = Position;
        if (switchModelArrayList != null && switchModelArrayList.size() > Position) {
            switchModel = switchModelArrayList.get(Position);
            if (switchModel != null) {
                holder.switchNametextView.setText(switchModel.getSwitchName());

            }
            if(switchModel.getSwitchType().equalsIgnoreCase(SWITCH_TYPE_FAN))
            {
                holder.switchImageView.setImageResource(R.drawable.fan);
            }
            else {
                holder.switchImageView.setImageResource(R.drawable.bulb);

            }
            holder.switchDescriptionLL.setOnClickListener(this);
            setClickListener(holder.menuLinearLayout, switchModel, Position);
            setClickListener(holder.switchDescriptionLL, switchModel, Position);
        }

    }

    @Override
    public void onClick(View view) {
        updateSwitchList.onItemClick(Position,roomDescriptionLL);

    }


    public class SwitchlistViewHolder extends RecyclerView.ViewHolder {
        private TextView switchNametextView;
        private TextView switchMenuTextView;
        private LinearLayout menuLinearLayout;
        private LinearLayout switchDescriptionLL;
        private ImageView switchImageView;


        public SwitchlistViewHolder(View view) {
            super(view);
            switchNametextView = (TextView) view.findViewById(R.id.switchNameTextView);
            switchMenuTextView = (TextView) view.findViewById(R.id.switchMenuTextView);
            menuLinearLayout = (LinearLayout) view.findViewById(R.id.menuLinearLayout);
            switchDescriptionLL = (LinearLayout) view.findViewById(R.id.mainLinearLayout);
            switchImageView = (ImageView) view.findViewById(R.id.switchImage);


        }


    }

    @Override
    public int getItemCount() {
        if (switchModelArrayList == null) {
            return 0;
        }

        updateSwitchList.UpdateSwitchList(switchModelArrayList);
        return switchModelArrayList.size();
    }

    public void update(ArrayList<SwitchModel> datas) {
        switchModelArrayList = datas;
        notifyDataSetChanged();
    }

    private void setClickListener(final LinearLayout linearLayout, final SwitchModel switchModel, final int position) {
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
                                        String prevRoomName = switchModelArrayList.get(position).getRoomName();
                                        String homeName = switchModelArrayList.get(position).getHomeName();
                                        showEditDialog(prevRoomName, homeName);
                                    }
                                    break;
                                    case R.id.delete:
                                        String roomName = switchModelArrayList.get(position).getRoomName();
                                        String homeName = switchModelArrayList.get(position).getHomeName();
                                        showDeleteDialog(switchModel);
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
                        wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                        DhcpInfo dhcp = wifiManager.getDhcpInfo();
                        int dhc = dhcp.serverAddress;
                        dhcS = (dhc & 0xFF) + "." + ((dhc >> 8) & 0xFF) + "." + ((dhc >> 16) & 0xFF) + "." + ((dhc >> 24) & 0xFF);
                        checkStatus();
                        Intent intent = new Intent(context, BulbOperationActivity.class);
                        intent.putExtra(SWITCH_MODEL, switchModel);
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
        alertDialog.setMessage("Change Switch Name");


        final EditText switchNameEdittext = new EditText(context);
        switchNameEdittext.setSingleLine();
        switchNameEdittext.setText(prevRoomName);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(switchNameEdittext);
        alertDialog.setView(layout);

        alertDialog.setIcon(R.drawable.switchboard);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String switchName = switchNameEdittext.getText().toString();

                        if (TextUtils.isEmpty(switchName)) {
                            return;
                        }


                        if (!databaseHandler.checkSwitchNameExist(switchName,switchModel.getDeviceCode(),switchModel.getHomeName(),switchModel.getRoomName())) {
                            Toast.makeText(context, "This room Name Already exist", Toast.LENGTH_LONG).show();
                            return;
                        } else {


                            int result = databaseHandler.updateSwitch(switchName, switchModel);
                            if (result == 1) {
                                switchModelArrayList = databaseHandler.getSwitchList(switchModel.getDeviceCode(),switchModel.getHomeName(),switchModel.getRoomName());
                                update(switchModelArrayList);
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

    private void showDeleteDialog(final SwitchModel switchModel) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Delete");
        alertDialog.setMessage("Are You Sure ,Do You want to delete this Room");
        alertDialog.setIcon(R.drawable.room);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHandler.deleteSwitch(switchModel);
                        switchModelArrayList = databaseHandler.getSwitchList(switchModel.getDeviceCode(),switchModel.getHomeName(),switchModel.getRoomName());
                        update(switchModelArrayList);


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
    public void checkStatus() {
        final String message = ("01"+","+switchModel.getDeviceCode()).trim();
        m_objectThreadClient = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clientSocket = new Socket(dhcS, 8080);
                    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                    oos.writeObject(message);
                    Message serverMessage = Message.obtain();
                    ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                    String strMessage = (String) ois.readObject();
                    serverMessage.obj = strMessage;
                    mHandler.sendMessage(serverMessage);
                    oos.close();
                    ois.close();

                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }

            }
        });
        m_objectThreadClient.start();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message arg) {
            messageDisplay(arg.obj.toString());
        }

        private void messageDisplay(String servermessage) {
            Toast.makeText(context, servermessage, Toast.LENGTH_LONG).show();
            // serverMessage.setText(""+servermessage);

        }
    };
}

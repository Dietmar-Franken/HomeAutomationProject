package com.example.garima.homeautomationproject.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.garima.homeautomationproject.R;
import com.example.garima.homeautomationproject.model.SwitchModel;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static com.example.garima.homeautomationproject.R.styleable.CompoundButton;
import static com.example.garima.homeautomationproject.constant.Constant.OFF;
import static com.example.garima.homeautomationproject.constant.Constant.ON;
import static com.example.garima.homeautomationproject.constant.Constant.ROOM_NAME;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH1;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH2;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH3;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH4;
import static com.example.garima.homeautomationproject.constant.Constant.SWITCH_MODEL;

public class BulbOperationActivity extends AppCompatActivity {
    private ImageView bulbImageView;
    private Switch aSwitch;
    private Thread m_objectThreadClient;
    private Socket clientSocket;
    private WifiManager wifiManager;
    private String dhcS;
    private SwitchModel switchModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_operation);
        switchModel = getIntent().getParcelableExtra(SWITCH_MODEL);
        bulbImageView = (ImageView) findViewById(R.id.bulbImage);
        aSwitch = (Switch) findViewById(R.id.switchButton);
        bulbImageView = (ImageView) findViewById(R.id.bulbImage);
        aSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @SuppressLint("WifiManagerLeak")
            @Override
            public void onCheckedChanged(android.widget.CompoundButton compoundButton, boolean b) {
                if (b) {
                    wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    DhcpInfo dhcp = wifiManager.getDhcpInfo();
                    int dhc = dhcp.serverAddress;
                    dhcS = (dhc & 0xFF) + "." + ((dhc >> 8) & 0xFF) + "." + ((dhc >> 16) & 0xFF) + "." + ((dhc >> 24) & 0xFF);
                    Start();
                    RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);

                    rotateAnimation.setInterpolator(new LinearInterpolator());
                    rotateAnimation.setDuration(500);
                    rotateAnimation.setRepeatCount(Animation.INFINITE);

                    bulbImageView.startAnimation(rotateAnimation);
                } else {
                    close();
                    // bulbImageView.setImageResource(R.drawable.bulb);
                    bulbImageView.clearAnimation();
                }

            }
        });
    }

    public void Start() {
        String message = "";
        if (switchModel.getDeviceType().equalsIgnoreCase(SWITCH1)) {
            message = ON + "," + switchModel.getDeviceCode() + "," + SWITCH1;

        }
        if (switchModel.getDeviceType().equalsIgnoreCase(SWITCH4)) {
            message = ON + "," + switchModel.getDeviceCode() + "," + SWITCH4;

        }
        if (switchModel.getDeviceType().equalsIgnoreCase(SWITCH2)) {
            message = ON + "," + switchModel.getDeviceCode() + "," + SWITCH2;

        }
        if (switchModel.getDeviceType().equalsIgnoreCase(SWITCH3)) {
            message = ON + "," + switchModel.getDeviceCode() + "," + SWITCH3;

        }
        final String finalMessage = message;
        m_objectThreadClient = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clientSocket = new Socket(dhcS, 8080);
                    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                    oos.writeObject(finalMessage);
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
            Toast.makeText(BulbOperationActivity.this, servermessage, Toast.LENGTH_LONG).show();
            // serverMessage.setText(""+servermessage);

        }
    };

    private void close() {
        String message = "";
        if (switchModel.getDeviceType().equalsIgnoreCase(SWITCH1)) {
            message = OFF + "," + switchModel.getDeviceCode() + "," + SWITCH1;

        }
        if (switchModel.getDeviceType().equalsIgnoreCase(SWITCH4)) {
            message = OFF + "," + switchModel.getDeviceCode() + "," + SWITCH4;

        }
        if (switchModel.getDeviceType().equalsIgnoreCase(SWITCH2)) {
            message = OFF + "," + switchModel.getDeviceCode() + "," + SWITCH2;

        }
        if (switchModel.getDeviceType().equalsIgnoreCase(SWITCH3)) {
            message = OFF + "," + switchModel.getDeviceCode() + "," + SWITCH3;

        }
        final String finalMessage = message;
        m_objectThreadClient = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clientSocket = new Socket(dhcS, 8080);
                    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                    oos.writeObject(finalMessage);
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


}




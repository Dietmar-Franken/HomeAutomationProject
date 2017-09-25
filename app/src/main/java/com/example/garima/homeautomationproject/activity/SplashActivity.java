package com.example.garima.homeautomationproject.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.garima.homeautomationproject.R;
import com.example.garima.homeautomationproject.constant.Constant;
import com.example.garima.homeautomationproject.database.DatabaseHandler;
import com.example.garima.homeautomationproject.utlis.SharedPreferenceUtils;

public class SplashActivity extends AppCompatActivity {
    public static final int RECEIVE_STORAGE_PERMISSION_REQUEST = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        createSplashThread();
    }



    private void createSplashThread() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow+
                    if (!checkWriteExternalStoragePermission()) {
                        requestPermission();
                    } else {
                        moveToNextActivity();
                    }
                } else {
                    moveToNextActivity();
                }

            }
        }, 3000);
    }

    private boolean checkWriteExternalStoragePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void moveToNextActivity() {
        Intent intent;

             intent = new Intent(SplashActivity.this, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, RECEIVE_STORAGE_PERMISSION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RECEIVE_STORAGE_PERMISSION_REQUEST:
                moveToNextActivity();
                break;
        }
    }
}

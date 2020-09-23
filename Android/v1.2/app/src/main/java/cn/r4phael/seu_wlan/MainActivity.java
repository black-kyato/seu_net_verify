package cn.r4phael.seu_wlan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.r4phael.seu_wlan.service.ForegroundService;
import cn.r4phael.seu_wlan.utils.VerifyManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private EditText password;

    Handler handler = new Handler();
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            updateViews();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
            }
        }

        updateViews();
    }

    private void updateViews() {
        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);

        SharedPreferences read = getSharedPreferences("config", MODE_PRIVATE);
        username.setText(read.getString("username", ""));
        password.setText(read.getString("password", ""));

        Button mBtnSave = findViewById(R.id.buttonSave);
        Button mBtnLogin = findViewById(R.id.buttonLogin);
        Button mBtnEnable = findViewById(R.id.buttonEnableService);
        Button mBtnDisable = findViewById(R.id.buttonDisableService);
        Button mBtnStart = findViewById(R.id.buttonStartService);
        Button mBtnStop = findViewById(R.id.buttonStopService);

        if (read.getBoolean("auto_start", false)) {
            mBtnEnable.setEnabled(false);
            mBtnDisable.setEnabled(true);
        } else {
            mBtnEnable.setEnabled(true);
            mBtnDisable.setEnabled(false);
        }

        if (ForegroundService.serviceIsLive) {
            mBtnStart.setEnabled(false);
            mBtnStop.setEnabled(true);
        } else {
            mBtnStart.setEnabled(true);
            mBtnStop.setEnabled((false));
        }

        mBtnSave.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mBtnEnable.setOnClickListener(this);
        mBtnDisable.setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent mForegroundService;
        SharedPreferences.Editor editor;
        switch (v.getId()) {
            case R.id.buttonStartService:
                if (!ForegroundService.serviceIsLive) {
                    // Android 8.0使用startForegroundService在前台启动新服务
                    mForegroundService = new Intent(this, ForegroundService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(mForegroundService);
                    } else {
                        startService(mForegroundService);
                    }
                } else {
                    Toast.makeText(this, "前台服务正在运行中...", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonStopService:
                mForegroundService = new Intent(this, ForegroundService.class);
                stopService(mForegroundService);
                break;
            case R.id.buttonSave:
                String _username = username.getText().toString().trim();
                String _password = password.getText().toString();
                editor = getSharedPreferences("config", MODE_PRIVATE).edit();
                editor.putString("username", _username);
                editor.putString("password", _password);
                editor.apply();
                Toast.makeText(getApplicationContext(), R.string.save_successful, Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonLogin:
                Toast.makeText(getApplicationContext(), VerifyManager.updateData(this), Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonEnableService:
                editor = getSharedPreferences("config", MODE_PRIVATE).edit();
                editor.putBoolean("auto_start", true);
                editor.apply();
                break;
            case R.id.buttonDisableService:
                editor = getSharedPreferences("config", MODE_PRIVATE).edit();
                editor.putBoolean("auto_start", false);
                editor.apply();
                break;
        }

        handler.postDelayed(runnable, 500);
    }
}
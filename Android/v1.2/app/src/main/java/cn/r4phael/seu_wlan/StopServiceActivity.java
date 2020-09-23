package cn.r4phael.seu_wlan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import cn.r4phael.seu_wlan.service.ForegroundService;

public class StopServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_service);

        Intent mForegroundService = new Intent(this.getApplicationContext(), ForegroundService.class);
        stopService(mForegroundService);
        finish();
    }
}
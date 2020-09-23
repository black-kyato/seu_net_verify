package cn.r4phael.seu_wlan.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import cn.r4phael.seu_wlan.service.ForegroundService;

import static android.content.Context.MODE_PRIVATE;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences read = context.getSharedPreferences("config", MODE_PRIVATE);
            if (read.getBoolean("auto_start", false)) {
                if (!ForegroundService.serviceIsLive) {
                    // Android 8.0使用startForegroundService在前台启动新服务
                    Intent mForegroundService = new Intent(context, ForegroundService.class);
                    mForegroundService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(mForegroundService);
                    } else {
                        context.startService(mForegroundService);
                    }
                } else {
                    Toast.makeText(context, "SEU认无感证服务正在运行中...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}

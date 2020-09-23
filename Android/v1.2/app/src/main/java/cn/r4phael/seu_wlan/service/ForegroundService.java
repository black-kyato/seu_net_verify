package cn.r4phael.seu_wlan.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import cn.r4phael.seu_wlan.MainActivity;
import cn.r4phael.seu_wlan.R;
import cn.r4phael.seu_wlan.StopServiceActivity;
import cn.r4phael.seu_wlan.broadcast.WIFIStateChangedBroadcastReceiver;
import cn.r4phael.seu_wlan.utils.VerifyManager;

public class ForegroundService extends Service {

    public static boolean serviceIsLive = false;

    private static final int NOTIFICATION_ID = 1000;

    private static NotificationCompat.Builder builder;

    private static WIFIStateChangedBroadcastReceiver receiver = null;

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        startForeground(NOTIFICATION_ID, createForegroundNotification());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        receiver = new WIFIStateChangedBroadcastReceiver();
        registerReceiver(receiver, intentFilter);
    }

    public static void update(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NotificationManager notificationManager = context.getApplicationContext().getSystemService(NotificationManager.class);
            builder.setWhen(System.currentTimeMillis());
            builder.setContentText(VerifyManager.updateData(context));
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } else {
            Toast.makeText(context, "无感认证服务暂不支持 Android6.0 以下", Toast.LENGTH_LONG).show();
            Log.e("更新通知栏", "失败，版本低于6.0");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ForegroundService.serviceIsLive = true;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        ForegroundService.serviceIsLive = false;
        stopForeground(true);
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private Notification createForegroundNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String notificationChannelId = "notification_channel_id_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Foreground Service Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, channelName, importance);
            notificationChannel.setDescription("Channel description");
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        builder = new NotificationCompat.Builder(this, notificationChannelId);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle(getResources().getString(R.string.title));
        builder.setContentText(VerifyManager.updateData(this));
        builder.setWhen(System.currentTimeMillis());

        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Intent stopIntent = new Intent(this, StopServiceActivity.class);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 1, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_launcher_background, getString(R.string.close), pendingIntent1);

        return builder.build();
    }
}
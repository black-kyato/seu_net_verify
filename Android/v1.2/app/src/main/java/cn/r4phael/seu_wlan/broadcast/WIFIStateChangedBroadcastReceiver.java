package cn.r4phael.seu_wlan.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.r4phael.seu_wlan.service.ForegroundService;

public class WIFIStateChangedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ForegroundService.update(context);
    }

}

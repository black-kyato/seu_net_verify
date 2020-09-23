package cn.r4phael.seu_wlan.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.r4phael.seu_wlan.R;
import cn.r4phael.seu_wlan.http.Protocol;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WIFI_SERVICE;

public class VerifyManager {

    private static String SSID;
    private static final String[] SUPPORTED_SSID = {
            "\"seu-wlan\"", // "seu-wlan"
    };

    static public boolean isSupport(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                String s = wifiInfo.getSSID();
                for (String ssid: SUPPORTED_SSID) {
                    if (s.equals(ssid)) {
                        SSID = ssid;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String updateData(Context context) {
        if (context == null) {
            throw new NullPointerException("上下文为空");
        }
        if (isSupport(context)) {
            verify(context);
            String data = context.getResources().getString(R.string.connected_to);
            return String.format(data, SSID, getVerifyInfo(context));
        } else {
            return context.getResources().getString(R.string.unconntected);
        }
    }

    private static void verify(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.9.10.100:801/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Protocol protocol = retrofit.create(Protocol.class);

        SharedPreferences read = context.getSharedPreferences("config", MODE_PRIVATE);
        String username = read.getString("username", "");
        String password = read.getString("password", "");
        try {
            Call<String> resultCall = protocol.do_login("Portal", "login", "dr1003", 1, ",0,"+username, password,
                    getWifiIp(context), "", "000000000000", "", "", "3.3.2", 4822);
            Response<String> response = resultCall.execute();
            if (response.code() != 200) {
                Log.i("Login", "Http-Code " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getWifiIp(Context context) {
        if (context == null) {
            throw new NullPointerException("上下文为空");
        }
        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (isWifiEnabled(context)) {
            int ipAsInt = wifiMgr.getConnectionInfo().getIpAddress();
            if (ipAsInt == 0) {
                return null;
            } else {
                return int2Inet(ipAsInt);
            }
        } else {
            return null;
        }
    }

    private static String getVerifyInfo(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.9.10.100/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Protocol protocol = retrofit.create(Protocol.class);
        try {
            Call<String> resultCall = protocol.check_verify_status();
            Response<String> response = resultCall.execute();

            Pattern pattern = Pattern.compile("uid='(\\d+)'");
            Matcher matcher = pattern.matcher(response.body());
            if (matcher.find()) {
                String format = context.getResources().getString(R.string.current_user);
                return String.format(format, matcher.group(1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return context.getResources().getString(R.string.unverify);
    }

    private static String int2Inet(int i)  {
        return (i & 0xFF) + "." + ((i >> 8 ) & 0xFF) + "." + ((i >> 16 ) & 0xFF) +"."+((i >> 24 ) & 0xFF );
    }

    private static boolean isWifiEnabled(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            ConnectivityManager connManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifiInfo.isConnected();
        } else {
            return false;
        }
    }

}

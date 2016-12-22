package com.anzmo.netspeed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.anzmo.netspeed.NetSpeedService;

public class NetSpeedReceiver extends BroadcastReceiver {
    public NetSpeedReceiver() {
    }

    public void onReceive(Context var1, Intent var2) {
        if(var1.getSharedPreferences("config", 0).getBoolean("state", true)) {
            if(var2.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                var1.stopService(new Intent(var1, NetSpeedService.class));
            }

            NetworkInfo var3 = ((ConnectivityManager)var1.getSystemService("connectivity")).getActiveNetworkInfo();
            if(var3 != null && var3.isAvailable()) {
                var1.startService(new Intent(var1, NetSpeedService.class));
            }
        }

    }
}

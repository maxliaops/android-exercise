package com.anzmo.netspeed;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NetSpeedService extends Service {
    private Handler handler = new Handler() {
        public void handleMessage(Message var1) {
            switch(NetSpeedService.this.mMode) {
                case 1:
                    NetSpeedService.this.tv_1.setText(NetSpeedService.this.formatNetSpeed(NetSpeedService.this.mRxSpeed + NetSpeedService.this.mTxSpeed));
                    return;
                case 2:
                    NetSpeedService.this.tv_1.setText(NetSpeedService.this.formatNetSpeed(NetSpeedService.this.mRxSpeed));
                    NetSpeedService.this.tv_2.setText(NetSpeedService.this.formatNetSpeed(NetSpeedService.this.mTxSpeed));
                    return;
                case 3:
                    NetSpeedService.this.tv_1.setText(NetSpeedService.this.formatNetSpeed(NetSpeedService.this.mTxSpeed));
                    NetSpeedService.this.tv_2.setText(NetSpeedService.this.formatNetSpeed(NetSpeedService.this.mRxSpeed));
                    return;
                default:
            }
        }
    };
    private int mMode;
    private int mRefreshTime;
    private long mRxSpeed;
    private NetSpeedService.ScreenBroadcastReceiver mScreenReceiver;
    private long mStartTotalRxBytes;
    private long mStartTotalTxBytes;
    private long mTxSpeed;
    private View mView;
    private WindowManager mWm;
    private Runnable runnable = new Runnable() {
        public void run() {
            NetSpeedService.this.mRxSpeed = (TrafficStats.getTotalRxBytes() - NetSpeedService.this.mStartTotalRxBytes) / (long)NetSpeedService.this.mRefreshTime;
            NetSpeedService.this.mTxSpeed = (TrafficStats.getTotalTxBytes() - NetSpeedService.this.mStartTotalTxBytes) / (long)NetSpeedService.this.mRefreshTime;
            NetSpeedService.this.mStartTotalRxBytes = TrafficStats.getTotalRxBytes();
            NetSpeedService.this.mStartTotalTxBytes = TrafficStats.getTotalTxBytes();
            NetSpeedService.this.handler.sendEmptyMessage(0);
            NetSpeedService.this.handler.postDelayed(this, (long)(1000 * NetSpeedService.this.mRefreshTime));
        }
    };
    private TextView tv_1;
    private TextView tv_2;

    public NetSpeedService() {
    }

    private String formatNetSpeed(long var1) {
        String var3 = "";
        if(var1 <= 0L) {
            var3 = "0.00B/s";
        } else if(var1 <= 999L) {
            var3 = (var1 + ".00").substring(0, 4) + "B/s";
        } else if(var1 <= 1022976L) {
            var3 = (String.valueOf((float)var1 / 1024.0F) + "0").substring(0, 4) + "K/s";
        } else if(var1 < 1047527424L) {
            var3 = (String.valueOf((float)var1 / 1048576.0F) + "0").substring(0, 4) + "M/s";
        }

        if(var3.indexOf(".") == 3) {
            var3 = var3.replace(".", "");
        }

        return var3;
    }

    private void initFloatWindow() {
        final SharedPreferences var1 = this.getSharedPreferences("config", 0);
        this.mRefreshTime = var1.getInt("refresh", 1);
        this.mMode = var1.getInt("mode", 1);
        final LayoutParams var2 = new LayoutParams();
        var2.type = 2002;
        var2.format = -2;
        var2.width = -2;
        var2.height = -2;
        var2.gravity = 51;
        var2.x = var1.getInt("paramX", 0);
        var2.y = var1.getInt("paramY", 0);
        if(var1.getBoolean("location", false)) {
            var2.flags = 56;
        } else {
            var2.flags = 40;
        }

        this.mView = View.inflate(this, R.layout.floatwindow_netspeed, (ViewGroup)null);
        this.tv_1 = (TextView)this.mView.findViewById(R.id.tv_floatwindow_text1);
        this.tv_2 = (TextView)this.mView.findViewById(R.id.tv_floatwindow_text2);
        TextView var3 = (TextView)this.mView.findViewById(R.id.tv_floatwindow_bg1);
        TextView var4 = (TextView)this.mView.findViewById(R.id.tv_floatwindow_bg2);
        TextView var5 = (TextView)this.mView.findViewById(R.id.tv_floatwindow_devide);
        ImageView var6 = (ImageView)this.mView.findViewById(R.id.iv_floatwindow);
        LinearLayout var7 = (LinearLayout)this.mView.findViewById(R.id.ll_floatwindow);
        if(this.mMode == 1) {
            this.tv_2.setVisibility(8);
            var4.setVisibility(8);
            var5.setVisibility(8);
        }

        int var8 = var1.getInt("text", 14);
        this.tv_1.setTextSize(2, (float)var8);
        this.tv_2.setTextSize(2, (float)var8);
        var3.setTextSize(2, (float)var8);
        var4.setTextSize(2, (float)var8);
        var5.setTextSize(2, (float)var8);
        if(var1.getBoolean("icon", true)) {
            this.tv_1.measure(0, 0);
            int var12 = this.tv_1.getMeasuredHeight();
            android.view.ViewGroup.LayoutParams var13 = var6.getLayoutParams();
            var13.height = var12;
            var13.width = var12 * 54 / 60;
            var6.setLayoutParams(var13);
            NetworkInfo var14 = ((ConnectivityManager)this.getSystemService("connectivity")).getActiveNetworkInfo();
            if(var14 != null && var14.isAvailable()) {
                switch(var14.getType()) {
                    case 0:
                        var6.setImageResource(R.drawable.inout);
                        break;
                    case 1:
                        var6.setImageResource(R.drawable.wifi);
                        break;
                    default:
                        var6.setVisibility(8);
                }
            }
        } else {
            var6.setVisibility(8);
        }

        GradientDrawable var9 = (GradientDrawable)var7.getBackground();
        var9.setAlpha((int)(2.55D * (double)(100 - var1.getInt("alpha", 70))));
        var9.setCornerRadius((float)(3 * var1.getInt("corner", 3)));
        if(!var1.getBoolean("location", false)) {
            View var10 = this.mView;
            OnTouchListener var11 = new OnTouchListener() {
                int lastX;
                int lastY;
                int paramX;
                int paramY;

                public boolean onTouch(View var1x, MotionEvent var2x) {
                    switch(var2x.getAction()) {
                        case 0:
                            this.lastX = (int)var2x.getRawX();
                            this.lastY = (int)var2x.getRawY();
                            this.paramX = var2.x;
                            this.paramY = var2.y;
                            break;
                        case 1:
                            var1.edit().putInt("paramX", var2.x).commit();
                            var1.edit().putInt("paramY", var2.y).commit();
                            break;
                        case 2:
                            int var5 = (int)var2x.getRawX() - this.lastX;
                            int var6 = (int)var2x.getRawY() - this.lastY;
                            var2.x = var5 + this.paramX;
                            var2.y = var6 + this.paramY;
                            NetSpeedService.this.mWm.updateViewLayout(NetSpeedService.this.mView, var2);
                    }

                    return true;
                }
            };
            var10.setOnTouchListener(var11);
        }

        this.mWm = (WindowManager)this.getSystemService("window");
        this.mWm.addView(this.mView, var2);
    }

    private void registerScreenBroadcastReceiver() {
        this.mScreenReceiver = new NetSpeedService.ScreenBroadcastReceiver();
        IntentFilter var1 = new IntentFilter();
        var1.addAction("android.intent.action.SCREEN_OFF");
        var1.addAction("android.intent.action.SCREEN_ON");
        this.registerReceiver(this.mScreenReceiver, var1);
    }

    public IBinder onBind(Intent var1) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.initFloatWindow();
        this.mStartTotalRxBytes = TrafficStats.getTotalRxBytes();
        this.mStartTotalTxBytes = TrafficStats.getTotalTxBytes();
        this.handler.postDelayed(this.runnable, (long)(1000 * this.mRefreshTime));
        this.registerScreenBroadcastReceiver();
    }

    public void onDestroy() {
        this.handler.removeCallbacks(this.runnable);
        this.mWm.removeView(this.mView);
        this.unregisterReceiver(this.mScreenReceiver);
        super.onDestroy();
    }

    public int onStartCommand(Intent var1, int var2, int var3) {
        super.onStartCommand(var1, var2, var3);
        return 1;
    }

    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private ScreenBroadcastReceiver() {
        }

        public void onReceive(Context var1, Intent var2) {
            if(var2.getAction().equals("android.intent.action.SCREEN_OFF")) {
                NetSpeedService.this.handler.removeCallbacks(NetSpeedService.this.runnable);
            } else if(var2.getAction().equals("android.intent.action.SCREEN_ON") && !NetSpeedService.this.handler.hasMessages(0)) {
                NetSpeedService.this.handler.postDelayed(NetSpeedService.this.runnable, (long)(1000 * NetSpeedService.this.mRefreshTime));
                return;
            }

        }
    }
}
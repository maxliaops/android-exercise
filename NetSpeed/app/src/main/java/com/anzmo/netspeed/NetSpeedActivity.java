package com.anzmo.netspeed;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.anzmo.netspeed.DialogItemView;
import com.anzmo.netspeed.NetSpeedService;
import com.anzmo.netspeed.SetItemView;

public class NetSpeedActivity extends Activity {
    private int alpha;
    private int corner;
    private DialogItemView div01;
    private DialogItemView div02;
    private DialogItemView div03;
    private AlertDialog mDialog;
    private SharedPreferences mPre;
    private SeekBar mSb;
    private TextView mTv;
    private View mView;
    private int mode;
    private int refresh;
    private SetItemView siv01;
    private SetItemView siv02;
    private SetItemView siv03;
    private SetItemView siv04;
    private SetItemView siv05;
    private SetItemView siv06;
    private SetItemView siv07;
    private SetItemView siv08;
    private int text;

    public NetSpeedActivity() {
    }

    public void cancel(View var1) {
        this.mDialog.dismiss();
    }

    public void click01(View var1) {
        if(this.mPre.getBoolean("state", true)) {
            this.siv01.setDesc("网速悬浮窗已关闭");
            this.siv01.setImage(R.drawable.uncheck);
            this.mPre.edit().putBoolean("state", false).commit();
            this.stopService(new Intent(this, NetSpeedService.class));
        } else {
            this.siv01.setDesc("网速悬浮窗已开启");
            this.siv01.setImage(R.drawable.checked);
            this.mPre.edit().putBoolean("state", true).commit();
            this.sendBroadcast(new Intent("com.example.netspeed.START_SERVICE"));
        }
    }

    public void click02(View var1) {
        if(this.mPre.getBoolean("icon", true)) {
            this.siv02.setDesc("显示网络状态图标已关闭");
            this.siv02.setImage(R.drawable.uncheck);
            this.mPre.edit().putBoolean("icon", false).commit();
        } else {
            this.siv02.setDesc("显示网络状态图标已开启");
            this.siv02.setImage(R.drawable.checked);
            this.mPre.edit().putBoolean("icon", true).commit();
        }

        this.restart();
    }

    public void click03(View var1) {
        if(this.mPre.getBoolean("location", false)) {
            this.siv03.setDesc("悬浮窗位置未锁定");
            this.siv03.setImage(R.drawable.uncheck);
            this.mPre.edit().putBoolean("location", false).commit();
        } else {
            this.siv03.setDesc("悬浮窗位置已锁定");
            this.siv03.setImage(R.drawable.checked);
            this.mPre.edit().putBoolean("location", true).commit();
        }

        this.restart();
    }

    public void click04(View var1) {
        this.mView = View.inflate(this, R.layout.dailog_mode, (ViewGroup)null);
        this.div01 = (DialogItemView)this.mView.findViewById(R.id.div01);
        this.div02 = (DialogItemView)this.mView.findViewById(R.id.div02);
        this.div03 = (DialogItemView)this.mView.findViewById(R.id.div03);
        this.div01.setText("总网速");
        this.div02.setText("下载网速+上传网速");
        this.div03.setText("上传网速+下载网速");
        this.mode = this.mPre.getInt("mode", 1);
        switch(this.mode) {
            case 1:
                this.div01.setChoice();
                break;
            case 2:
                this.div02.setChoice();
                break;
            case 3:
                this.div03.setChoice();
        }

        this.mDialog = (new Builder(this)).create();
        this.mDialog.setView(this.mView, 0, 0, 0, 0);
        this.mDialog.show();
    }

    public void click05(View var1) {
        this.mView = View.inflate(this, R.layout.dailog_refresh, (ViewGroup)null);
        this.mSb = (SeekBar)this.mView.findViewById(R.id.sb_refresh);
        this.mTv = (TextView)this.mView.findViewById(R.id.tv_refresh);
        this.refresh = this.mPre.getInt("refresh", 1);
        this.mTv.setText(this.refresh + "秒");
        this.mSb.setMax(4);
        this.mSb.setProgress(-1 + this.refresh);
        this.mSb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar var1, int var2, boolean var3) {
            }

            public void onStartTrackingTouch(SeekBar var1) {
            }

            public void onStopTrackingTouch(SeekBar var1) {
                NetSpeedActivity.this.refresh = 1 + var1.getProgress();
                NetSpeedActivity.this.mTv.setText(NetSpeedActivity.this.refresh + "秒");
            }
        });
        this.mDialog = (new Builder(this)).create();
        this.mDialog.setView(this.mView, 0, 0, 0, 0);
        this.mDialog.show();
    }

    public void click06(View var1) {
        this.mView = View.inflate(this, R.layout.dailog_text, (ViewGroup)null);
        this.mSb = (SeekBar)this.mView.findViewById(R.id.sb_text);
        this.mTv = (TextView)this.mView.findViewById(R.id.tv_text);
        this.text = this.mPre.getInt("text", 14);
        this.mTv.setText(String.valueOf(this.text));
        this.mSb.setMax(12);
        this.mSb.setProgress(-8 + this.text);
        this.mSb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar var1, int var2, boolean var3) {
            }

            public void onStartTrackingTouch(SeekBar var1) {
            }

            public void onStopTrackingTouch(SeekBar var1) {
                NetSpeedActivity.this.text = 8 + var1.getProgress();
                NetSpeedActivity.this.mTv.setText(String.valueOf(NetSpeedActivity.this.text));
            }
        });
        this.mDialog = (new Builder(this)).create();
        this.mDialog.setView(this.mView, 0, 0, 0, 0);
        this.mDialog.show();
    }

    public void click07(View var1) {
        this.mView = View.inflate(this, R.layout.dailog_alpha, (ViewGroup)null);
        this.mSb = (SeekBar)this.mView.findViewById(R.id.sb_alpha);
        this.mTv = (TextView)this.mView.findViewById(R.id.tv_alpha);
        this.alpha = this.mPre.getInt("alpha", 70);
        this.mTv.setText(this.alpha + "%");
        this.mSb.setMax(10);
        this.mSb.setProgress(this.alpha / 10);
        this.mSb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar var1, int var2, boolean var3) {
            }

            public void onStartTrackingTouch(SeekBar var1) {
            }

            public void onStopTrackingTouch(SeekBar var1) {
                NetSpeedActivity.this.alpha = 10 * var1.getProgress();
                NetSpeedActivity.this.mTv.setText(NetSpeedActivity.this.alpha + "%");
            }
        });
        this.mDialog = (new Builder(this)).create();
        this.mDialog.setView(this.mView, 0, 0, 0, 0);
        this.mDialog.show();
    }

    public void click08(View var1) {
        this.mView = View.inflate(this, R.layout.dailog_corners, (ViewGroup)null);
        this.mSb = (SeekBar)this.mView.findViewById(R.id.sb_corners);
        this.mTv = (TextView)this.mView.findViewById(R.id.tv_corners);
        this.corner = this.mPre.getInt("corner", 3);
        this.mTv.setText(String.valueOf(this.corner));
        this.mSb.setMax(5);
        this.mSb.setProgress(this.corner);
        this.mSb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar var1, int var2, boolean var3) {
            }

            public void onStartTrackingTouch(SeekBar var1) {
            }

            public void onStopTrackingTouch(SeekBar var1) {
                NetSpeedActivity.this.corner = var1.getProgress();
                NetSpeedActivity.this.mTv.setText(String.valueOf(NetSpeedActivity.this.corner));
            }
        });
        this.mDialog = (new Builder(this)).create();
        this.mDialog.setView(this.mView, 0, 0, 0, 0);
        this.mDialog.show();
    }

    public void div01(View var1) {
        this.div01.setChoice();
        this.div02.setUnchoice();
        this.div03.setUnchoice();
        this.mode = 1;
    }

    public void div02(View var1) {
        this.div01.setUnchoice();
        this.div02.setChoice();
        this.div03.setUnchoice();
        this.mode = 2;
    }

    public void div03(View var1) {
        this.div01.setUnchoice();
        this.div02.setUnchoice();
        this.div03.setChoice();
        this.mode = 3;
    }

    public void initView() {
        this.setContentView(R.layout.activity_netspeed);
        this.siv01 = (SetItemView)this.findViewById(R.id.siv01);
        this.siv02 = (SetItemView)this.findViewById(R.id.siv02);
        this.siv03 = (SetItemView)this.findViewById(R.id.siv03);
        this.siv04 = (SetItemView)this.findViewById(R.id.siv04);
        this.siv05 = (SetItemView)this.findViewById(R.id.siv05);
        this.siv06 = (SetItemView)this.findViewById(R.id.siv06);
        this.siv07 = (SetItemView)this.findViewById(R.id.siv07);
        this.siv08 = (SetItemView)this.findViewById(R.id.siv08);
        this.siv01.setTitle("悬浮窗状态");
        this.siv02.setTitle("显示网络状态图标");
        this.siv03.setTitle("锁定位置");
        this.siv04.setTitle("显示模式");
        this.siv05.setTitle("刷新时间");
        this.siv06.setTitle("文字大小");
        this.siv07.setTitle("透明度");
        this.siv08.setTitle("边框圆角");
        this.mPre = this.getSharedPreferences("config", 0);
        if(this.mPre.getBoolean("state", true)) {
            this.siv01.setDesc("网速悬浮窗已开启");
            this.siv01.setImage(R.drawable.checked);
        } else {
            this.siv01.setDesc("网速悬浮窗已关闭");
            this.siv01.setImage(R.drawable.uncheck);
        }

        if(this.mPre.getBoolean("icon", true)) {
            this.siv02.setDesc("显示网络状态图标已开启");
            this.siv02.setImage(R.drawable.checked);
        } else {
            this.siv02.setDesc("显示网络状态图标已关闭");
            this.siv02.setImage(R.drawable.uncheck);
        }

        if(this.mPre.getBoolean("location", false)) {
            this.siv03.setDesc("悬浮窗位置已锁定");
            this.siv03.setImage(R.drawable.checked);
        } else {
            this.siv03.setDesc("悬浮窗位置未锁定");
            this.siv03.setImage(R.drawable.uncheck);
        }

        switch(this.mPre.getInt("mode", 1)) {
            case 1:
                this.siv04.setDesc("总网速");
                break;
            case 2:
                this.siv04.setDesc("下载网速+上传网速");
                break;
            case 3:
                this.siv04.setDesc("上传网速+下载网速");
        }

        this.siv05.setDesc(this.mPre.getInt("refresh", 1) + "秒");
        this.siv06.setDesc(String.valueOf(this.mPre.getInt("text", 14)));
        this.siv07.setDesc(this.mPre.getInt("alpha", 70) + "%");
        this.siv08.setDesc(String.valueOf(this.mPre.getInt("corner", 3)));
    }

    public void ok04(View var1) {
        switch(this.mode) {
            case 1:
                this.siv04.setDesc("总网速");
                break;
            case 2:
                this.siv04.setDesc("下载网速+上传网速");
                break;
            case 3:
                this.siv04.setDesc("上传网速+下载网速");
        }

        this.mPre.edit().putInt("mode", this.mode).commit();
        this.mDialog.dismiss();
        this.restart();
    }

    public void ok05(View var1) {
        this.siv05.setDesc(this.refresh + "秒");
        this.mPre.edit().putInt("refresh", this.refresh).commit();
        this.mDialog.dismiss();
        this.restart();
    }

    public void ok06(View var1) {
        this.siv06.setDesc(String.valueOf(this.text));
        this.mPre.edit().putInt("text", this.text).commit();
        this.mDialog.dismiss();
        this.restart();
    }

    public void ok07(View var1) {
        this.siv07.setDesc(this.alpha + "%");
        this.mPre.edit().putInt("alpha", this.alpha).commit();
        this.mDialog.dismiss();
        this.restart();
    }

    public void ok08(View var1) {
        this.siv08.setDesc(String.valueOf(this.corner));
        this.mPre.edit().putInt("corner", this.corner).commit();
        this.mDialog.dismiss();
        this.restart();
    }

    protected void onCreate(Bundle var1) {
        super.onCreate(var1);
        this.requestWindowFeature(1);
        this.initView();
        this.restart();
    }

    public void restart() {
        this.stopService(new Intent(this, NetSpeedService.class));
        this.sendBroadcast(new Intent("com.example.netspeed.START_SERVICE"));
    }
}

package com.rainsong.tianxingnews;

import com.baidu.apistore.sdk.ApiStoreSDK;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApiStoreSDK.init(this, "e0179993772fe66c27acaaa88f60530c");
    }
}

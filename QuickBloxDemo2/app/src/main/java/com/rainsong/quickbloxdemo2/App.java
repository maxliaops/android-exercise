package com.rainsong.quickbloxdemo2;

import android.app.Application;

import com.quickblox.core.QBSettings;
import com.quickblox.core.ServiceZone;
import com.rainsong.quickbloxdemo2.util.Consts;

public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        QBSettings.getInstance().init(this, Consts.APP_ID, Consts.AUTH_KEY, Consts.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Consts.ACCOUNT_KEY);
        QBSettings.getInstance().setEndpoints(Consts.QB_API_DOMAIN, Consts.QB_CHAT_DOMAIN, ServiceZone.PRODUCTION);
        QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
    }

    public static synchronized App getInstance() {
        return instance;
    }

}

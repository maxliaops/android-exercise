package com.rainsong.blockexplorer.app;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.rainsong.blockexplorer.BuildConfig;
import com.rainsong.blockexplorer.data.DaoMaster;
import com.rainsong.blockexplorer.data.DaoSession;
import com.squareup.leakcanary.LeakCanary;

import org.greenrobot.greendao.database.Database;

import timber.log.Timber;

/**
 * Created by maxliaops on 17-12-1.
 */

public class App extends Application {
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Stetho.initializeWithDefaults(this);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "blockexplorer-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}

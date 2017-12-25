package com.rainsong.hireme.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import timber.log.Timber;

/**
 * Created by maxliaops on 17-12-1.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("activity created invisible");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.d("invisible -> visible");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.d("visible -> foreground");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timber.d("foreground -> visible");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.d("visible -> invisible");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Timber.d("onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("activity destroyed");
    }
}

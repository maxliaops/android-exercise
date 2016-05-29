package com.rainsong.quickbloxdemo2.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.rainsong.quickbloxdemo2.R;


/**
 * QuickBlox team
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}

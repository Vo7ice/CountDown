package com.cn.guojinhu.game2048.activity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.cn.guojinhu.game2048.R;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.ic_settings);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}

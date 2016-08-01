package com.cn.guojinhu.game2048.activity;

import android.app.Activity;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.cn.guojinhu.game2048.R;

import static com.cn.guojinhu.game2048.utils.SPUtils.FILE_NAME;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener
        , SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

    public static final String KEY_FULL_SCREEN = "preference_fullcreen";
    public static final String KEY_NIGHT_MODE = "preference_nightmode";
    public static final String KEY_GAMEITEM_LINE = "preference_gameline";
    public static final String KEY_TARGET_SCORE = "preference_target_score";
    public static final String KEY_GOLD_FINGER = "preference_gold_finger";

    private ListPreference preference_gameline;
    private ListPreference preference_targetscore;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        PreferenceManager preferenceManager = getPreferenceManager();
        SharedPreferences sharedPreference = getSharedPreference(activity);
        preferenceManager.setSharedPreferencesName(FILE_NAME);
        addPreferencesFromResource(R.xml.ic_settings);

        final PreferenceScreen preferenceScreen = getPreferenceScreen();
        preference_gameline = (ListPreference) findPreference(KEY_GAMEITEM_LINE);
//        preferenceScreen.getEditor().putInt(BaseApplication.KEY_GAME_LINES,
//                Integer.valueOf(preference_gameline.getValue())).apply();
        Log.d("Vo7ice","value:"+preference_gameline.getValue());
        preference_gameline.setOnPreferenceClickListener(this);

        preference_targetscore = (ListPreference) findPreference(KEY_TARGET_SCORE);
//        preferenceScreen.getEditor().putInt(BaseApplication.KEY_GAME_GOAL,
//                Integer.valueOf(preference_targetscore.getValue())).apply();
        preference_targetscore.setOnPreferenceClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void setDefaultValues(Context context) {
        PreferenceManager.setDefaultValues(context, FILE_NAME, Context.MODE_PRIVATE, R.xml.ic_settings, false);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Activity a = getActivity();
        if (null != a){
            BackupManager.dataChanged(a.getPackageName());
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }
}

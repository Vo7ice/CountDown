package com.cn.guojinhu.game2048.Base;

import android.app.Application;
import android.util.Log;

import com.cn.guojinhu.game2048.fragment.SettingsFragment;
import com.cn.guojinhu.game2048.utils.SPUtils;

public class BaseApplication extends Application {

    private static final String TAG = "BaseApplication";

    public static int SCORE = 0;

    public static String SP_HIGH_SCROE = "SP_HIGHSCROE";

    public static String KEY_HIGH_SCROE = "KEY_HighScore";

    public static String KEY_GAME_LINES = "preference_gameline";

    public static String KEY_TARGET_SCORE = "preference_target_score";

    public static String KEY_GOLD_FINGER = "preference_gold_finger";

    public static String KEY_NIGHT_MODE = "preference_nightmode";

    public static String KEY_FULL_SCREEN = "preference_fullcreen";


    /**
     * Game Goal
     */
    public static int sTargetScore;

    /**
     * GameView行列数
     */
    public static int sGameLines;

    /**
     * Item宽高
     */
    public static int sItemSize;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
        SettingsFragment.setDefaultValues(getApplicationContext());

        sGameLines = Integer.valueOf((String) SPUtils.get(getApplicationContext(), KEY_GAME_LINES, 4 + ""));

        sTargetScore = Integer.valueOf((String) SPUtils.get(getApplicationContext(), KEY_TARGET_SCORE, 2048 + ""));
        sItemSize = 0;

    }


}

package com.cn.guojinhu.game2048.Base;

import android.app.Application;
import android.util.Log;

import com.cn.guojinhu.game2048.activity.SettingsFragment;
import com.cn.guojinhu.game2048.utils.SPUtils;

public class BaseApplication extends Application {

    private static final String TAG = "BaseApplication";

    public static int SCORE = 0;

    public static String SP_HIGH_SCROE = "SP_HIGHSCROE";

    public static String KEY_HIGH_SCROE = "KEY_HighScore";

    public static String KEY_GAME_LINES = "KEY_GAMELINES";

    public static String KEY_GAME_GOAL = "KEY_GameGoal";

    /**
     * Game Goal
     */
    public static int mGameGoal;

    /**
     * GameView行列数
     */
    public static int mGameLines;

    /**
     * Item宽高
     */
    public static int mItemSize;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
        SettingsFragment.setDefaultValues(getApplicationContext());
        
        mGameLines = (int) SPUtils.get(getApplicationContext(), KEY_GAME_LINES, 4);
        mGameGoal = (int) SPUtils.get(getApplicationContext(), KEY_GAME_GOAL, 2048);
        mItemSize = 0;

    }

    

}

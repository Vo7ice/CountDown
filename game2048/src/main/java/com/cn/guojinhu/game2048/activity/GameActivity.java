package com.cn.guojinhu.game2048.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.cn.guojinhu.game2048.Base.BaseActivity;
import com.cn.guojinhu.game2048.R;

public class GameActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.hide();
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_game;
    }
}

package com.cn.guojinhu.game2048.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cn.guojinhu.game2048.Base.BaseActivity;
import com.cn.guojinhu.game2048.Base.BaseApplication;
import com.cn.guojinhu.game2048.R;
import com.cn.guojinhu.game2048.utils.SPUtils;
import com.cn.guojinhu.game2048.view.GameView;

public class GameActivity extends BaseActivity implements GameView.onUIChangeListener, View.OnClickListener {

    private GameView mGameView;

    private TextView mTextScore, mTextRecord, mTextTarget;
    private Button mBtnRestart, mBtnRevert, mBtnSettings;

    private int mTarget;//目标分数

    private int mHighScore;//最高分

    private int mScore = BaseApplication.SCORE;//当前得分


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.hide();
        }
        initViews();

        mGameView = new GameView(GameActivity.this);
        FrameLayout root = (FrameLayout) findViewById(R.id.game_panel_root);

        //RelativeLayout panel = (RelativeLayout) findViewById(R.id.game_panel);
        assert root != null;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        root.addView(mGameView, params);

        mGameView.setOnUIChangeListener(this);
        
    }

    private void initViews() {
        mTextScore = (TextView) findViewById(R.id.text_score);
        mTextRecord = (TextView) findViewById(R.id.text_record);
        mTextTarget = (TextView) findViewById(R.id.text_target);
        mBtnRestart = (Button) findViewById(R.id.btn_restart);
        mBtnRevert = (Button) findViewById(R.id.btn_revert);
        mBtnSettings = (Button) findViewById(R.id.btn_settings);

        mBtnRestart.setOnClickListener(this);
        mBtnRevert.setOnClickListener(this);
        mBtnSettings.setOnClickListener(this);

        mTarget = (int) SPUtils.get(getApplicationContext(), BaseApplication.KEY_GAME_GOAL, 2048);
        mHighScore = (int) SPUtils.get(getApplicationContext(), BaseApplication.KEY_HIGH_SCROE, 0);

        mTextScore.setText(String.valueOf(mScore));
        mTextRecord.setText(String.valueOf(mHighScore));
        mTextTarget.setText(String.valueOf(mTarget));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_game;
    }


    @Override
    public void onScoreChanged(int score) {
        mTextScore.setText(String.valueOf(score));
    }

    @Override
    public void onHighScoreChanged(int score) {
        mTextRecord.setText(String.valueOf(score));
    }

    @Override
    public void onTargetScoreChanged(int target) {
        mTextTarget.setText(String.valueOf(target));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_restart:
                mGameView.startGame();
                mTextScore.setText("0");
                break;
            case R.id.btn_revert:
                mGameView.revertGame();
                break;
            case R.id.btn_settings:
                Intent intent = new Intent(GameActivity.this, SettingsActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

            }
        }
    }
}

package com.cn.guojinhu.Result;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import abe.no.seimei.qrphase.R;

public class ResultActivity extends AppCompatActivity {

    private static final String KEY_RESULT_TYPE = "KEY_RESULT_TYPE";

    private static final int NORMAL = 0;
    private static final int URL = 1;
    private static final int CONTACT = 2;

    private static int DEFAULT = NORMAL;
    /*mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
    | View.SYSTEM_UI_FLAG_FULLSCREEN
    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        int type = getIntent().getIntExtra(KEY_RESULT_TYPE, DEFAULT);
        replaceContent(getSupportFragmentManager(), type);
    }

    private void replaceContent(FragmentManager fragmentManager, int type) {
        switch (type) {

            default:
            case NORMAL:
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.fragment, NormalResultFragment.newInstance(null), NormalResultFragment.class.getSimpleName());
                ft.commit();
                break;
        }
    }
}

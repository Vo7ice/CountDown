package com.cn.guojinhu.Result;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import abe.no.seimei.qrphase.R;

public class ResultActivity extends AppCompatActivity {

    public static final String KEY_RESULT_TYPE = "KEY_RESULT_TYPE";
    public static final String KEY_RESULT = "KEY_RESULT";

    public static final int NORMAL = 0;
    public static final int URL = 1;
    public static final int CONTACT = 2;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_result);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();

        replaceContent(getSupportFragmentManager(), bundle);
    }

    private void replaceContent(FragmentManager fragmentManager, Bundle bundle) {
        int type = bundle.getInt(KEY_RESULT_TYPE, DEFAULT);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        switch (type) {
            default:
            case NORMAL:
                ft.replace(R.id.fragment, NormalResultFragment.newInstance(bundle), NormalResultFragment.class.getSimpleName());
                ft.commit();
                break;
            case URL:
                ft.replace(R.id.fragment, UrlResultFragment.newInstance(bundle), UrlResultFragment.class.getSimpleName());
                ft.commit();
                break;
            case CONTACT:
                ft.replace(R.id.fragment, ContactResultFragment.newInstance(bundle), ContactResultFragment.class.getSimpleName());
                ft.commit();
                break;
        }
    }
}

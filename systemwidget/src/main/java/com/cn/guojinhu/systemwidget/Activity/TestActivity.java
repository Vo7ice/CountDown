package com.cn.guojinhu.systemwidget.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.cn.guojinhu.systemwidget.view.KeyguardView;
import com.cn.guojinhu.systemwidget.R;

public class TestActivity extends AppCompatActivity {

    KeyguardView mView;

    Button button_custom_title_view;
    Button button_custom_image_view;
    Button button_custom_volume_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*mView = (KeyguardView) findViewById(R.id.keyguard);
        mView.setCircleRadius(200f);*/

        findViewById(R.id.button_custom_title_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestActivity.this, CustomTitleActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.button_custom_image_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestActivity.this, CustomImageActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.button_custom_volume_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestActivity.this, CustomVolumeActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.button_webview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });

    }

}
